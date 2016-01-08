package com.xiaogua.service.impl;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.sfm.csv.CsvParser;
import org.sfm.utils.RowHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.AbstractRowProcessor;
import com.univocity.parsers.csv.CsvParserSettings;
import com.xiaogua.dao.GenerateRandDataDao;
import com.xiaogua.dao.MySqlManagerDao;
import com.xiaogua.service.InterfaceParseCsvFileService;

public class ParseCsvFileServiceImpl implements InterfaceParseCsvFileService {
	private Logger log = LoggerFactory.getLogger(ParseCsvFileServiceImpl.class);
	private final String sql = "insert into tmysql_test_person_info(id,first_name,second_name,birthday,gender,marital_status,work_date,hobby) values ";

	public void parseCsvFileToDbWithSmfParsers(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		final AtomicLong counter = new AtomicLong();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		CsvParser.skip(1).reader(GenerateRandDataDao.read(filePath, encoding)).read(new RowHandler<String[]>() {
			public void handle(String[] t) throws Exception {
				processCsvRow(sqlBuffer, counter, sql, t);
			}
		});
		processRemainCsvData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseCsvFileToDbWithSmfParsers", filePath,
				counter.get(), (System.currentTimeMillis() - start));
	}

	public void parseCsvFileToDbWithUnivocityParsers(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		final AtomicLong counter = new AtomicLong();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		CsvParserSettings settings = new CsvParserSettings();
		settings.setIgnoreLeadingWhitespaces(false);
		settings.setIgnoreTrailingWhitespaces(false);
		settings.setSkipEmptyLines(true);
		settings.setHeaderExtractionEnabled(true);
		settings.setRowProcessor(new AbstractRowProcessor() {
			public void rowProcessed(String[] row, ParsingContext context) {
				processCsvRow(sqlBuffer, counter, sql, row);
			}
		});
		com.univocity.parsers.csv.CsvParser parser = new com.univocity.parsers.csv.CsvParser(settings);
		parser.parse(GenerateRandDataDao.read(filePath, encoding));
		processRemainCsvData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseCsvFileToDbWithUnivocityParsers",
				filePath, counter.get(), (System.currentTimeMillis() - start));
	}

	public void parseCsvFileToDbWithJacksonParser(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		final AtomicLong counter = new AtomicLong();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		CsvMapper csvMapper = new CsvMapper();
		csvMapper.enable(com.fasterxml.jackson.dataformat.csv.CsvParser.Feature.WRAP_AS_ARRAY);
		CsvSchema csvSchema = CsvSchema.emptySchema().withSkipFirstDataRow(true);
		MappingIterator<String[]> iterator = csvMapper.readerFor(String[].class).with(csvSchema)
				.readValues(GenerateRandDataDao.read(filePath, encoding));
		while (iterator.hasNext()) {
			String[] row = iterator.next();
			processCsvRow(sqlBuffer, counter, sql, row);
		}
		processRemainCsvData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseCsvFileToDbWithJacksonParser",
				filePath, counter.get(), (System.currentTimeMillis() - start));

	}

	public void parseCsvFileToDbWithSuperCsv(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		final AtomicLong counter = new AtomicLong();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		List<String> rtnList = null;
		try (ICsvListReader listReader = new CsvListReader(GenerateRandDataDao.read(filePath, encoding),
				CsvPreference.STANDARD_PREFERENCE);) {
			listReader.getHeader(true);
			while ((rtnList = listReader.read()) != null) {
				processCsvRow(sqlBuffer, counter, sql, rtnList.toArray(new String[0]));
			}
		} catch (Exception e) {
			log.error("parseCsvFileToDbWithSuperCsv", e);
		}
		processRemainCsvData(sqlBuffer);
		if(rtnList!=null){
			rtnList.clear();
		}
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseCsvFileToDbWithSuperCsv", filePath,
				counter.get(), (System.currentTimeMillis() - start));

	}

	private void processCsvRow(StringBuffer sqlBuffer, AtomicLong counter, String sql, String[] row) {
		sqlBuffer.append("(").append(row[0]).append(",'").append(row[1]).append("','").append(row[2]).append("','")
				.append(row[3]).append("','").append(row[4]).append("','").append(row[5]).append("','").append(row[6])
				.append("','").append(row[7].replace("'", "\\\'")).append("'),");
		long currentTotal = counter.incrementAndGet();
		if (currentTotal > 0 && currentTotal % 1000 == 0) {
			sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
			log.info("process number {}",currentTotal);
			MySqlManagerDao.insertDataToDb(sqlBuffer.toString());
			sqlBuffer.setLength(0);
			sqlBuffer.append(sql);
		}
	}

	private void processRemainCsvData(StringBuffer sqlBuffer) {
		String tmpSql = sqlBuffer.toString();
		if (tmpSql.length() > 0 && !tmpSql.endsWith("values ")) {
			sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
			log.info("process remain data");
			MySqlManagerDao.insertDataToDb(sqlBuffer.toString());
		}
	}

}
