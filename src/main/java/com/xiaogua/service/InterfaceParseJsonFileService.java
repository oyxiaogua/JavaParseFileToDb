package com.xiaogua.service;

public interface InterfaceParseJsonFileService {

	/**
	 * 利用JsonSurfer解析json文件
	 * 
	 * @param filePath
	 * @param encoding
	 */
	public void parseJsonFileToDbWithJsonSurferSimple(String filePath, String encoding) throws Exception;

	/**
	 * 利用JsonSurfer解析json文件
	 * 
	 * @param filePath
	 * @param encoding
	 */
	public void parseJsonFileToDbWithJsonSurferGson(String filePath, String encoding) throws Exception;

	/**
	 * 利用JsonSurfer解析json文件
	 * 
	 * @param filePath
	 * @param encoding
	 */
	public void parseJsonFileToDbWithJsonSurferJackson(String filePath, String encoding) throws Exception;

	/**
	 * 利用Gson解析json文件
	 * 
	 * @param filePath
	 * @param encoding
	 */
	public void parseJsonFileToDbWithGson(String filePath, String encoding) throws Exception;

	/**
	 * 利用Jackson解析json文件
	 * 
	 * @param filePath
	 * @param encoding
	 */
	public void parseJsonFileToDbWithJackson(String filePath, String encoding) throws Exception;
	
	/**
	 * 利用jsurfer-core解析json文件
	 * @param filePath
	 * @param encoding
	 * @throws Exception
	 */
	public void parseJsonFileToDbWithJsoniter(String filePath, String encoding,final int bufSize) throws Exception;

}
