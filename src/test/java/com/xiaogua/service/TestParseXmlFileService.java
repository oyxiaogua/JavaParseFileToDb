package com.xiaogua.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.xiaogua.dao.MySqlManagerDao;
import com.xiaogua.service.impl.ParseXmlFileServiceImpl;

public class TestParseXmlFileService {
	private static String clearSql = "truncate table tmysql_test_person_info";
	private InterfaceParseXmlFileService parseXmlFileService;
	private String xmlPath = "e:/test_tmp/big_xml.xml";
	private String encoding = "UTF-8";

	@Before
	public void setUp() {
		parseXmlFileService = new ParseXmlFileServiceImpl();
	}

	@BeforeClass
	public static void cleanTableData() {
		MySqlManagerDao.executeUpdateSql(clearSql);
	}

	@Test
	public void testParseXmlFileToDbWithDom4j() throws Exception {
		// 1436090
		parseXmlFileService.parseXmlFileToDbWithDom4j(xmlPath, encoding);
	}

	@Test
	public void testParseXmlFileToDbWithSax() throws Exception {
		// 177286
		parseXmlFileService.parseXmlFileToDbWithSax(xmlPath, encoding);
	}

	@Test
	public void testParseXmlFileToDbWithStax() throws Exception {
		// 270813
		parseXmlFileService.parseXmlFileToDbWithStax(xmlPath, encoding);
	}

	@Test
	public void testParseXmlFileToDbWithStaxMate() throws Exception {
		// 203191
		parseXmlFileService.parseXmlFileToDbWithStaxMate(xmlPath, encoding);
	}

	@Test
	public void testParseXmlFileToDbWithStax2() throws Exception {
		// 183156
		parseXmlFileService.parseXmlFileToDbWithStax2(xmlPath, encoding);
	}

	@Test
	public void testParseXmlFileToDbWithJacksonXml() throws Exception {
		//210004
		parseXmlFileService.parseXmlFileToDbWithJacksonXml(xmlPath, encoding);
	}
}
