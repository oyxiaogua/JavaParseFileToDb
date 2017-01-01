package com.xiaogua.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xiaogua.dao.MySqlManagerDao;
import com.xiaogua.service.impl.ParseJsonFileServiceImpl;

public class TestParseJsonFileService {
	private static String clearSql = "truncate table tmysql_test_person_info";
	private InterfaceParseJsonFileService parseJsonFileService;
	private String jsonPath = "e:/test_tmp/big_json.json";
	private String encoding = "UTF-8";

	@Before
	public void setUp() {
		parseJsonFileService = new ParseJsonFileServiceImpl();
	}

	@BeforeClass
	public static void cleanTableData() {
		MySqlManagerDao.executeUpdateSql(clearSql);
	}

	@Test
	public void testParseJsonFileToDbWithJsonSurferSimple() throws Exception {
		parseJsonFileService.parseJsonFileToDbWithJsonSurferSimple(jsonPath, encoding);
	}

	@Test
	public void testParseJsonFileToDbWithJsonSurferGson() throws Exception {
		parseJsonFileService.parseJsonFileToDbWithJsonSurferGson(jsonPath, encoding);
	}

	@Test
	public void testParseJsonFileToDbWithJsonSurferJackson() throws Exception {
		parseJsonFileService.parseJsonFileToDbWithJsonSurferJackson(jsonPath, encoding);
	}

	@Test
	public void testParseJsonFileToDbWithGson() throws Exception {
		//31161
		parseJsonFileService.parseJsonFileToDbWithGson(jsonPath, encoding);
	}

	@Test
	public void testParseJsonFileToDbWithJackson() throws Exception {
		//29610
		parseJsonFileService.parseJsonFileToDbWithJackson(jsonPath, encoding);
	}
	
	@Test
	public void testParseJsonFileToDbWithJsoniter() throws Exception {
		//30259
		final int bufSize=10240;
		parseJsonFileService.parseJsonFileToDbWithJsoniter(jsonPath, encoding,bufSize);
	}
}
