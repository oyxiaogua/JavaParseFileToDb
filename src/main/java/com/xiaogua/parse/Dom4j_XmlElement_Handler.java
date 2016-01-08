package com.xiaogua.parse;

import java.util.List;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaogua.dao.MySqlManagerDao;

public class Dom4j_XmlElement_Handler implements ElementHandler {
	private Logger log = LoggerFactory.getLogger(Dom4j_XmlElement_Handler.class);
	private StringBuffer sqlBuffer = new StringBuffer(5120);
	private final String sql = "insert into tmysql_test_person_info(id,first_name,second_name,birthday,gender,marital_status,work_date,hobby) values ";
	private long countNum = 0;

	public void onStart(ElementPath elementPath) {
		Element e = elementPath.getCurrent();
		e.detach();
	}

	public void onEnd(ElementPath elementPath) {
		Element e = elementPath.getCurrent();
		parseElement(e);
		e.detach();
	}

	public void parseElement(Element e) {
		if (countNum == 0) {
			sqlBuffer.append(sql);
		}
		List<Element> list = e.elements();
		if (list.size() > 0) {
			sqlBuffer.append("(").append(list.get(0).getTextTrim()).append(",'").append(list.get(1).getTextTrim())
					.append("','").append(list.get(2).getTextTrim()).append("','").append(list.get(3).getTextTrim())
					.append("','").append(list.get(4).getTextTrim()).append("','").append(list.get(5).getTextTrim())
					.append("','").append(list.get(6).getTextTrim()).append("','")
					.append(list.get(7).getTextTrim().replace("'", "\\\'")).append("'),");
			countNum++;
			if (countNum > 0 && countNum % 1000 == 0) {
				sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
				log.info("method {},process number:{}","Dom4j_XmlElement_Handler",countNum);
				MySqlManagerDao.insertDataToDb(sqlBuffer.toString());
				sqlBuffer.setLength(0);
				sqlBuffer.append(sql);
			}
		}
	}

	public StringBuffer getSqlBuffer() {
		return sqlBuffer;
	}

	public long getCountNum() {
		return countNum;
	}
}