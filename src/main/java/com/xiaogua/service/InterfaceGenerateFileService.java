package com.xiaogua.service;

public interface InterfaceGenerateFileService {
	/**
	 * 生成测试csv文件
	 * 
	 * @param filePath
	 * @param encoding
	 * @param countNum
	 */
	public void generateCsvFile(String filePath, String encoding, int countNum);

	/**
	 * 生成测试xml文件
	 * 
	 * @param filePath
	 * @param encoding
	 * @param countNum
	 */
	public void generateXmlFile(String filePath, String encoding, int countNum);

	/**
	 * 生成测试json文件
	 * 
	 * @param filePath
	 * @param encoding
	 * @param countNum
	 */
	public void generateJsonFile(String filePath, String encoding, int countNum);
}
