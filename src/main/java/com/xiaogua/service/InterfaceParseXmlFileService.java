package com.xiaogua.service;

public interface InterfaceParseXmlFileService {
	/**
	 * dom4j解析xml文件
	 * 
	 * @param filePath
	 * @param encoding
	 */
	public void parseXmlFileToDbWithDom4j(String filePath, String encoding) throws Exception;

	/**
	 * sax解析xml文件
	 * 
	 * @param filePath
	 * @param encoding
	 * @throws Exception
	 */
	public void parseXmlFileToDbWithSax(String filePath, String encoding) throws Exception;

	/**
	 * stax解析xml文件
	 * 
	 * @param filePath
	 * @param encoding
	 * @throws Exception
	 */
	public void parseXmlFileToDbWithStax(String filePath, String encoding) throws Exception;
	/**
	 * staxmate解析xml文件
	 * 
	 * @param filePath
	 * @param encoding
	 * @throws Exception
	 */
	public void parseXmlFileToDbWithStaxMate(String filePath, String encoding) throws Exception;

}
