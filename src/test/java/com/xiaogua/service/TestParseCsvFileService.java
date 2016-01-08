package com.xiaogua.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xiaogua.dao.MySqlManagerDao;
import com.xiaogua.service.impl.ParseCsvFileServiceImpl;

public class TestParseCsvFileService {
	private static String clearSql = "truncate table tmysql_test_person_info";
	private InterfaceParseCsvFileService parseCsvFileService;
	private String csvPath = "e:/test_tmp/big_csv.csv";
	private String encoding = "UTF-8";

	@Before
	public void setUp() {
		parseCsvFileService = new ParseCsvFileServiceImpl();
	}

	@BeforeClass
	public static void cleanTableData() {
		MySqlManagerDao.executeUpdateSql(clearSql);
	}

	@Test
	public void testParseCsvFileToDbWithSmfParsers() throws Exception {
		parseCsvFileService.parseCsvFileToDbWithSmfParsers(csvPath, encoding);
	}

	@Test
	public void testPparseCsvFileToDbWithUnivocityParsers() throws Exception {
		parseCsvFileService.parseCsvFileToDbWithUnivocityParsers(csvPath, encoding);
	}

	@Test
	public void testParseCsvFileToDbWithJacksonParser() throws Exception {
		parseCsvFileService.parseCsvFileToDbWithJacksonParser(csvPath, encoding);
	}

	@Test
	public void testParseCsvFileToDbWithSuperCsv() throws Exception {
		parseCsvFileService.parseCsvFileToDbWithSuperCsv(csvPath, encoding);
	}
}
