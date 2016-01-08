package com.xiaogua.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.xiaogua.dao.MySqlManagerDao;

public class SAX_ParseXml_Handler extends DefaultHandler {
	private Logger log = LoggerFactory.getLogger(SAX_ParseXml_Handler.class);
	private StringBuffer sqlBuffer = new StringBuffer(5120);
	private final String sql = "insert into tmysql_test_person_info(id,first_name,second_name,birthday,gender,marital_status,work_date,hobby) values ";
	private long countNum = 0;
	private String currentElement = null;
	private boolean isStart = false;

	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		if ("root".equals(qName)) {
			return;
		}
		if (!isStart) {
			sqlBuffer.append(sql);
		}
		if ("personinfo".equals(qName)) {
			// 元素对象开始
			sqlBuffer.append("(");
			isStart = true;
		} else {
			currentElement = qName;
		}
	}

	public void characters(char[] chars, int start, int length) {
		if (currentElement == null) {
			return;
		}
		String xmlValue = new String(chars, start, length);
		switch (currentElement) {
		case "id":
			sqlBuffer.append(xmlValue).append(",");
			break;
		case "hobby":
			xmlValue = xmlValue.trim().replace("'", "\\\'");
			sqlBuffer.append("'").append(xmlValue).append("'");
			break;
		default:
			sqlBuffer.append("'").append(xmlValue).append("',");
			break;
		}
	}

	public void endElement(String uri, String localName, String qName) {
		if ("personinfo".equals(qName)) {
			countNum++;
			sqlBuffer.append("),");
			if (countNum > 0 && countNum % 1000 == 0) {
				log.info("method {},process number:{}","SAX_ParseXml_Handler",countNum);
				sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
				MySqlManagerDao.insertDataToDb(sqlBuffer.toString());
				sqlBuffer.setLength(0);
				sqlBuffer.append(sql);
			}
		} else if (qName.equals(currentElement)) {
			currentElement = null;
		}
	}

	public StringBuffer getSqlBuffer() {
		return sqlBuffer;
	}

	public long getCountNum() {
		return countNum;
	}
}
