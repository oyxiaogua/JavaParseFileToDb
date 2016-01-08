package com.xiaogua.service;

public interface InterfaceParseCsvFileService {
	/**
	 * 解析csv文件
	 * 
	 * @param filePath
	 * @param encoding
	 */
	public void parseCsvFileToDbWithSmfParsers(String filePath, String encoding) throws Exception;

	/**
	 * UnivocityParsers解析csv文件
	 * 
	 * @param filePath
	 * @param encoding
	 * @throws Exception
	 */
	public void parseCsvFileToDbWithUnivocityParsers(String filePath, String encoding) throws Exception;

	/**
	 * Jackson解析csv文件
	 * 
	 * @param filePath
	 * @param encoding
	 * @throws Exception
	 */
	public void parseCsvFileToDbWithJacksonParser(String filePath, String encoding) throws Exception;

	/**
	 * super csv解析csv文件
	 * 
	 * @param filePath
	 * @param encoding
	 * @throws Exception
	 */
	public void parseCsvFileToDbWithSuperCsv(String filePath, String encoding) throws Exception;

}
