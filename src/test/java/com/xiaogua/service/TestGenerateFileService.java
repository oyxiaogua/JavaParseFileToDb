package com.xiaogua.service;

import org.junit.Before;
import org.junit.Test;

import com.xiaogua.service.impl.GenerateFileServiceImpl;

public class TestGenerateFileService {
	private InterfaceGenerateFileService generateFileService;
	private String encoding = "UTF-8";

	@Before
	public void setUp() {
		generateFileService = new GenerateFileServiceImpl();
	}

	@Test
	public void testGenerateCsvFile() {
		generateFileService.generateCsvFile("e:/test_tmp/big_csv.csv", encoding, 200);
	}

	@Test
	public void testGenerateJsonFile() {
		generateFileService.generateJsonFile("e:/test_tmp/big_json.json", encoding, 20);
	}

	@Test
	public void testGenerateXmlFile() {
		generateFileService.generateXmlFile("e:/test_tmp/big_xml.xml", encoding, 20);
	}

}
