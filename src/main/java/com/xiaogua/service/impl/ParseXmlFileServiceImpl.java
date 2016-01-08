package com.xiaogua.service.impl;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.codehaus.staxmate.SMInputFactory;
import org.codehaus.staxmate.in.SMInputCursor;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.fasterxml.jackson.dataformat.xml.deser.XmlTokenStream;
import com.xiaogua.dao.GenerateRandDataDao;
import com.xiaogua.dao.MySqlManagerDao;
import com.xiaogua.parse.Dom4j_XmlElement_Handler;
import com.xiaogua.parse.SAX_ParseXml_Handler;
import com.xiaogua.service.InterfaceParseXmlFileService;

public class ParseXmlFileServiceImpl implements InterfaceParseXmlFileService {
	private Logger log = LoggerFactory.getLogger(ParseXmlFileServiceImpl.class);
	private final String sql = "insert into tmysql_test_person_info(id,first_name,second_name,birthday,gender,marital_status,work_date,hobby) values ";

	public void parseXmlFileToDbWithDom4j(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		SAXReader sax = new SAXReader();
		Dom4j_XmlElement_Handler handler = new Dom4j_XmlElement_Handler();
		sax.addHandler("/root/personinfo", handler);
		sax.read(GenerateRandDataDao.read(filePath, encoding));
		String tmpSql = handler.getSqlBuffer().toString();
		if (tmpSql.length() > 0 && !tmpSql.endsWith("values ")) {
			log.info("method {},process remain xml data", "parseXmlFileToDbWithDom4j");
			handler.getSqlBuffer().deleteCharAt(handler.getSqlBuffer().length() - 1);
			MySqlManagerDao.insertDataToDb(handler.getSqlBuffer().toString());
		}
		handler.getSqlBuffer().setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseXmlFileToDbWithDom4j", filePath,
				handler.getCountNum(), (System.currentTimeMillis() - start));
	}

	public void parseXmlFileToDbWithSax(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		SAX_ParseXml_Handler saxHandler = new SAX_ParseXml_Handler();
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();
		xr.setContentHandler(saxHandler);
		xr.parse(new InputSource(GenerateRandDataDao.read(filePath, encoding)));
		String tmpSql = saxHandler.getSqlBuffer().toString();
		if (tmpSql.length() > 0 && !tmpSql.endsWith("values ")) {
			log.info("method {},process remain xml data", "parseXmlFileToDbWithSax");
			saxHandler.getSqlBuffer().deleteCharAt(saxHandler.getSqlBuffer().length() - 1);
			MySqlManagerDao.insertDataToDb(saxHandler.getSqlBuffer().toString());
		}
		saxHandler.getSqlBuffer().setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseXmlFileToDbWithSax", filePath,
				saxHandler.getCountNum(), (System.currentTimeMillis() - start));
	}

	public void parseXmlFileToDbWithStax(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		long countNum = 0;
		boolean isStart = false;
		String currentName = null;
		String elementValue = null;
		StartElement startElement = null;
		Characters characters = null;
		EndElement endElement = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty("com.ctc.wstx.minTextSegment", Integer.valueOf(Integer.MAX_VALUE));
		XMLEventReader eventReader = factory.createXMLEventReader(GenerateRandDataDao.read(filePath, encoding));
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			switch (event.getEventType()) {
			case XMLStreamConstants.START_ELEMENT:
				startElement = event.asStartElement();
				String qName = startElement.getName().getLocalPart();
				if ("root".equalsIgnoreCase(qName)) {
					break;
				}
				if (!isStart) {
					sqlBuffer.append(sql);
				}
				if ("personinfo".equalsIgnoreCase(qName)) {
					sqlBuffer.append("(");
					isStart = true;
				} else {
					currentName = qName;
				}
				break;
			case XMLStreamConstants.CHARACTERS:
				if (currentName == null) {
					break;
				}
				characters = event.asCharacters();
				elementValue = characters.getData();
				if ("id".equals(currentName)) {
					sqlBuffer.append(elementValue).append(",");
				} else if (!"hobby".equals(currentName)) {
					sqlBuffer.append("'").append(elementValue).append("',");
				}
				break;
			case XMLStreamConstants.CDATA:
				if (currentName == null) {
					break;
				}
				characters = event.asCharacters();
				elementValue = characters.getData().replace("'", "\\\'");
				sqlBuffer.append("'").append(elementValue).append("'");
				break;
			case XMLStreamConstants.END_ELEMENT:
				endElement = event.asEndElement();
				String endElementName = endElement.getName().getLocalPart();
				if ("personinfo".equalsIgnoreCase(endElementName)) {
					countNum++;
					sqlBuffer.append("),");
					saveXmlDataToDb(sqlBuffer, countNum);
				} else {
					if (endElementName.equals(currentName)) {
						currentName = null;
					}
				}
				break;
			}
		}
		processRemainXmlData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseXmlFileToDbWithStax", countNum,
				filePath, (System.currentTimeMillis() - start));

	}

	public void parseXmlFileToDbWithStaxMate(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		long countNum = 0;

		sqlBuffer.append(sql);
		SMInputFactory inf = new SMInputFactory(XMLInputFactory.newInstance());
		SMInputCursor rootC = inf.rootElementCursor(GenerateRandDataDao.read(filePath, encoding)).advance();
		SMInputCursor valueC = rootC.childElementCursor();
		String elementName = null;
		while (valueC.getNext() != null) {
			SMInputCursor subCursor = valueC.childElementCursor();
			sqlBuffer.append("(");
			while (subCursor.getNext() != null) {
				elementName = subCursor.getLocalName();
				if ("id".equals(elementName)) {
					sqlBuffer.append(subCursor.getElemLongValue()).append(",");
				} else if ("hobby".equals(elementName)) {
					sqlBuffer.append("'")
							.append(subCursor.getElemStringValue().trim().replace("\\\"", "\"").replace("'", "\\\'"))
							.append("'");
				} else {
					sqlBuffer.append("'").append(subCursor.getElemStringValue()).append("',");
				}
			}
			sqlBuffer.append("),");
			countNum++;
			saveXmlDataToDb(sqlBuffer, countNum);
		}
		processRemainXmlData(sqlBuffer);
		rootC.getStreamReader().closeCompletely();
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseXmlFileToDbWithStaxMate", countNum,
				filePath, (System.currentTimeMillis() - start));
	}

	private void saveXmlDataToDb(StringBuffer sqlBuffer, long countNum) {
		if (countNum > 0 && countNum % 1000 == 0) {
			sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
			log.info("process number:{}", countNum);
			MySqlManagerDao.insertDataToDb(sqlBuffer.toString());
			sqlBuffer.setLength(0);
			sqlBuffer.append(sql);
		}
	}

	private void processRemainXmlData(StringBuffer sqlBuffer) {
		String tmpSql = sqlBuffer.toString();
		if (tmpSql.length() > 0 && !tmpSql.endsWith("values ")) {
			log.info("process remain data");
			if(tmpSql.endsWith(",")){
				sqlBuffer.deleteCharAt(sqlBuffer.length() - 1);
			}else{
				sqlBuffer.append(")");
			}
			MySqlManagerDao.insertDataToDb(sqlBuffer.toString());
		}
	}

	public void parseXmlFileToDbWithStax2(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		long countNum = 0;
		String qName = null;
		boolean isStart = false;
		StartElement startElement = null;
		EndElement endElement = null;
		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader eventReader = factory.createXMLEventReader(GenerateRandDataDao.read(filePath, encoding));
		while (eventReader.hasNext()) {
			XMLEvent event = eventReader.nextEvent();
			if (event.isStartElement()) {
				startElement = event.asStartElement();
				qName = startElement.getName().getLocalPart();
				if ("personinfo".equals(qName)) {
					sqlBuffer.append("(");
					isStart = true;
				}
				if (isStart && event.isStartElement()) {
					qName = event.asStartElement().getName().getLocalPart();
					if ("id".equals(qName)) {
						event = eventReader.nextEvent();
						sqlBuffer.append(event.asCharacters().getData()).append(",");
					} else if (!"personinfo".equals(qName) && !"hobby".equals(qName)) {
						event = eventReader.nextEvent();
						sqlBuffer.append("'").append(event.asCharacters().getData()).append("',");
					} else if ("hobby".equals(qName)) {
						event = eventReader.nextEvent();
						event = eventReader.nextEvent();
						sqlBuffer.append("'").append(
								event.asCharacters().getData().trim().replace("\\\"", "\"").replace("'", "\\\'"))
								.append("'");
					}
				}
			}
			if (event.isEndElement()) {
				endElement = event.asEndElement();
				qName = endElement.getName().getLocalPart();
				if ("personinfo".equals(qName)) {
					countNum++;
					sqlBuffer.append("),");
					saveXmlDataToDb(sqlBuffer, countNum);
					isStart = false;
				}
			}
		}
		processRemainXmlData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseXmlFileToDbWithStax", countNum,
				filePath, (System.currentTimeMillis() - start));
	}

	public void parseXmlFileToDbWithJacksonXml(String filePath, String encoding) throws Exception {
		long start = System.currentTimeMillis();
		StringBuffer sqlBuffer = new StringBuffer(5120);
		sqlBuffer.append(sql);
		long countNum = 0;
		XMLStreamReader sr = XMLInputFactory.newInstance()
				.createXMLStreamReader(GenerateRandDataDao.read(filePath, encoding));
		sr.nextTag();
		XmlTokenStream tokens = new XmlTokenStream(sr, null);
		int tokenIndex = tokens.next();;
		String currentElementName = null, currentElementValue = null;
		boolean isStart = true;
		while ((tokenIndex=tokens.getCurrentToken()) != XmlTokenStream.XML_END) {
			if (tokenIndex == XmlTokenStream.XML_START_ELEMENT) {
				if ("personinfo".equals(tokens.getLocalName())) {
					if (isStart) {
						sqlBuffer.append("(");
						isStart=false;
						countNum++;
					} else {
						countNum++;
						sqlBuffer.append("),");
						saveXmlDataToDb(sqlBuffer, countNum);
						sqlBuffer.append("(");
					}
				}
			} else if (tokenIndex == XmlTokenStream.XML_TEXT) {
				currentElementName = tokens.getLocalName();
				currentElementValue = tokens.getText();
				//System.out.println(currentElementName + "-->" + currentElementValue);
				if ("id".equals(currentElementName)) {
					sqlBuffer.append(currentElementValue).append(",");
				} else if (!"hobby".equals(currentElementName)) {
					sqlBuffer.append("'").append(currentElementValue).append("',");
				} else if ("hobby".equals(currentElementName)) {
					sqlBuffer.append("'").append(currentElementValue.trim().replace("\\\"", "\"").replace("'", "\\\'"))
							.append("'");
				}
			}
			tokenIndex = tokens.next();
		}
		processRemainXmlData(sqlBuffer);
		sqlBuffer.setLength(0);
		log.info("method {},parse json file {},total num={}, cost time={}", "parseXmlFileToDbWithJacksonXml", countNum,
				filePath, (System.currentTimeMillis() - start));

	}
}
