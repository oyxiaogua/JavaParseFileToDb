package com.xiaogua.service.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.jsfr.json.JsonPathListener;
import org.jsfr.json.JsonSurfer;
import org.jsfr.json.ParsingContext;
import org.jsfr.json.SurfingConfiguration;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.jsoniter.JsonIterator;
import com.xiaogua.dao.GenerateRandDataDao;
import com.xiaogua.dao.MySqlManagerDao;
import com.xiaogua.service.InterfaceParseJsonFileService;

public class ParseJsonFileServiceImpl implements InterfaceParseJsonFileService {
	private Logger log = LoggerFactory.getLogger(ParseJsonFileServiceImpl.class);
	private final String sql = "insert into tmysql_test_person_info(id,first_name,second_name,birthday,gender,marital_status,work_date,hobby) values ";

	public void parseJsonFileToDbWithJsonSurferSimple(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		final AtomicLong counter = new AtomicLong();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		JsonSurfer surfer = JsonSurfer.simple();
		SurfingConfiguration surfingConfiguration = SurfingConfiguration.builder()
				.bind("$..personinfo", new JsonPathListener() {
					public void onValue(Object value, ParsingContext context) throws Exception {
						JSONObject obj = (JSONObject) value;
						sqlBuffer.append("(").append(obj.get("id")).append(",'").append(obj.get("first_name"))
								.append("','").append(obj.get("second_name")).append("','").append(obj.get("birthday"))
								.append("','").append(obj.get("gender")).append("','").append(obj.get("marital_status"))
								.append("','").append(obj.get("work_date")).append("','")
								.append(obj.get("hobby").toString().replace("'", "\\\'")).append("'),");
						long currentTotal = counter.incrementAndGet();
						if (currentTotal > 0 && currentTotal % 1000 == 0) {
							log.info("method {},process json number {}", "parseJsonFileToDbWithJsonSurferSimple",
									currentTotal);
							saveJsonDataToDb(sqlBuffer, sql);
						}
					}
				}).skipOverlappedPath().build();
		surfer.surf(GenerateRandDataDao.read(filePath, encoding), surfingConfiguration);
		log.info("method {},process remain json number {}", "parseJsonFileToDbWithJsonSurferSimple", counter.get());
		processRemainJsonData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseJsonFileToDbWithJsonSurferSimple",
				filePath, counter.get(), (System.currentTimeMillis() - start));
	}

	public void parseJsonFileToDbWithJsonSurferGson(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		final AtomicLong counter = new AtomicLong();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		JsonSurfer surfer = JsonSurfer.gson();
		JsonPathListener pathListener = new JsonPathListener() {
			public void onValue(Object value, ParsingContext context) throws Exception {
				JsonObject node = (JsonObject) value;
				sqlBuffer.append("(").append(node.get("id").getAsLong()).append(",'")
						.append(node.get("first_name").getAsString()).append("','")
						.append(node.get("second_name").getAsString()).append("','")
						.append(node.get("birthday").getAsString()).append("','")
						.append(node.get("gender").getAsString()).append("','")
						.append(node.get("marital_status").getAsString()).append("','")
						.append(node.get("work_date").getAsString()).append("','")
						.append(node.get("hobby").getAsString().replace("'", "\\\'")).append("'),");
				long currentTotal = counter.incrementAndGet();
				if (currentTotal > 0 && currentTotal % 1000 == 0) {
					log.info("method {},process json number {}", "parseJsonFileToDbWithJsonSurferGson", currentTotal);
					saveJsonDataToDb(sqlBuffer, sql);
				}
			}
		};
		SurfingConfiguration surfingConfiguration = SurfingConfiguration.builder().bind("$..personinfo", pathListener)
				.skipOverlappedPath().build();
		surfer.surf(GenerateRandDataDao.read(filePath, encoding), surfingConfiguration);
		log.info("method {},process remain json number {}", "parseJsonFileToDbWithJsonSurferGson", counter.get());
		processRemainJsonData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseJsonFileToDbWithJsonSurferGson",
				filePath, counter.get(), (System.currentTimeMillis() - start));

	}

	public void parseJsonFileToDbWithJsonSurferJackson(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		final AtomicLong counter = new AtomicLong();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		JsonSurfer surfer = JsonSurfer.jackson();
		JsonPathListener pathListener = new JsonPathListener() {
			public void onValue(Object value, ParsingContext context) throws Exception {
				ObjectNode node = (ObjectNode) value;
				sqlBuffer.append("(").append(node.get("id").asLong()).append(",'")
						.append(node.get("first_name").asText()).append("','").append(node.get("second_name").asText())
						.append("','").append(node.get("birthday").asText()).append("','")
						.append(node.get("gender").asText()).append("','").append(node.get("marital_status").asText())
						.append("','").append(node.get("work_date").asText()).append("','")
						.append(node.get("hobby").asText().replace("'", "\\\'")).append("'),");
				long currentTotal = counter.incrementAndGet();
				if (currentTotal > 0 && currentTotal % 1000 == 0) {
					log.info("method {},process json number {}", "parseJsonFileToDbWithJsonSurferJackson", currentTotal);
					saveJsonDataToDb(sqlBuffer, sql);
				}
			}
		};
		SurfingConfiguration surfingConfiguration = SurfingConfiguration.builder().bind("$..personinfo", pathListener)
				.skipOverlappedPath().build();
		surfer.surf(GenerateRandDataDao.read(filePath, encoding), surfingConfiguration);
		log.info("method {},process remain json number {}", "parseJsonFileToDbWithJsonSurferJackson", counter.get());
		processRemainJsonData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseJsonFileToDbWithJsonSurferJackson",
				filePath, counter.get(), (System.currentTimeMillis() - start));

	}

	public void parseJsonFileToDbWithGson(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		long countNum = 0;
		Gson gson = new Gson();
		JsonReader gsonReader = new JsonReader(GenerateRandDataDao.read(filePath, encoding));
		gsonReader.beginObject();
		gsonReader.nextName();
		gsonReader.beginArray();
		Map<String, Object> elementMap = null;
		while (gsonReader.hasNext()) {
			gsonReader.beginObject();
			gsonReader.nextName();
			elementMap = gson.fromJson(gsonReader, Map.class);
			sqlBuffer.append("(").append(elementMap.get("id")).append(",'").append(elementMap.get("first_name"))
					.append("','").append(elementMap.get("second_name")).append("','")
					.append(elementMap.get("birthday")).append("','").append(elementMap.get("gender")).append("','")
					.append(elementMap.get("marital_status")).append("','").append(elementMap.get("work_date"))
					.append("','").append(elementMap.get("hobby").toString().replace("'", "\\\'")).append("'),");
			countNum++;
			if (countNum > 0 && countNum % 1000 == 0) {
				log.info("method {},process json number {}", "parseJsonFileToDbWithGson", countNum);
				saveJsonDataToDb(sqlBuffer, sql);
			}
			gsonReader.endObject();
		}
		elementMap = null;
		log.info("method {},process remain json number {}", "parseJsonFileToDbWithGson", countNum);
		processRemainJsonData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseJsonFileToDbWithGson", filePath,
				countNum, (System.currentTimeMillis() - start));

	}

	public void parseJsonFileToDbWithJackson(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		long countNum = 0;
		ObjectMapper om = new ObjectMapper();
		JsonFactory jacksonFacoty = new JsonFactory();
		JsonParser jacksonParser = jacksonFacoty.createParser(GenerateRandDataDao.read(filePath, encoding));
		jacksonParser.nextToken();
		jacksonParser.nextToken();
		jacksonParser.nextToken();
		TreeNode tree = null;
		while (jacksonParser.nextToken() == JsonToken.START_OBJECT) {
			jacksonParser.nextToken();
			jacksonParser.nextToken();
			tree = om.readTree(jacksonParser);
			sqlBuffer.append("(").append(((IntNode) tree.get("id")).asInt()).append(",'")
					.append(((TextNode) tree.get("first_name")).asText()).append("','")
					.append(((TextNode) tree.get("second_name")).asText()).append("','")
					.append(((TextNode) tree.get("birthday")).asText()).append("','")
					.append(((TextNode) tree.get("gender")).asText()).append("','")
					.append(((TextNode) tree.get("marital_status")).asText()).append("','")
					.append(((TextNode) tree.get("work_date")).asText()).append("','")
					.append(((TextNode) tree.get("hobby")).asText().replace("'", "\\\'")).append("'),");
			countNum++;
			if (countNum > 0 && countNum % 1000 == 0) {
				log.info("method {},process json number {}", "parseJsonFileToDbWithJackson", countNum);
				saveJsonDataToDb(sqlBuffer, sql);
			}
			jacksonParser.nextToken();
		}
		tree = null;
		log.info("method {},process remain json number {}", "parseJsonFileToDbWithJackson", countNum);
		processRemainJsonData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseJsonFileToDbWithJackson", filePath,
				countNum, (System.currentTimeMillis() - start));

	}
	
    public void parseJsonFileToDbWithJsoniter(String filePath, String encoding,final int bufSize) throws Exception {
    	long start = System.currentTimeMillis();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		InputStream in=new FileInputStream(filePath);
		long countNum = 0;
		JsonIterator iter = JsonIterator.parse(in,bufSize);
		iter.readObject();//root
		while(iter.readArray()){
			for (String field = iter.readObject(); field != null; field = iter.readObject()) {
				switch (field) {
				 case "personinfo":
					    sqlBuffer.append("(");
						 for (String field2 = iter.readObject(); field2 != null; field2 = iter.readObject()) {
		                     switch (field2) {
		                        case "id":
		                    	   sqlBuffer.append(iter.readInt()).append(",'");
		                    	   break;
		                        case "first_name":
		                        case "second_name":
		                        case "birthday":
		                        case "gender":
		                        case "marital_status":
		                        case "work_date":
		                    	  sqlBuffer.append(iter.readString()).append("','");
		                    	  break;
		                        case "hobby":
		                          sqlBuffer.append(iter.readString().replace("'", "\\\'")).append("'),");	
		                          break;
		                        default:
		                          iter.skip();
		                        }
		                    }
					  countNum++;	 
					  if (countNum > 0 && countNum % 1000 == 0) {
						log.info("method {},process json number {}", "parseJsonFileToDbWithJackson", countNum);
						saveJsonDataToDb(sqlBuffer, sql);
					  }
					  break;
		          default:
		             iter.skip();
				 }
			 }
		}
		log.info("method {},process remain json number {}", "parseJsonFileToDbWithJsoniter", countNum);
		processRemainJsonData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseJsonFileToDbWithJsoniter", filePath,
				countNum, (System.currentTimeMillis() - start));
    }

	private void saveJsonDataToDb(StringBuffer sqlBuffer, final String sql) {
		sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
		MySqlManagerDao.insertDataToDb(sqlBuffer.toString());
		sqlBuffer.setLength(0);
		sqlBuffer.append(sql);
	}

	private void processRemainJsonData(StringBuffer sqlBuffer) {
		String tmpSql = sqlBuffer.toString();
		if (tmpSql.length() > 0 && !tmpSql.endsWith("values ")) {
			sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
			MySqlManagerDao.insertDataToDb(sqlBuffer.toString());
		}
	}

}
