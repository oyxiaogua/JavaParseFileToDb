package com.xiaogua.service.impl;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xiaogua.dao.GenerateRandDataDao;
import com.xiaogua.service.InterfaceGenerateFileService;

public class GenerateFileServiceImpl implements InterfaceGenerateFileService {
	private Logger log=LoggerFactory.getLogger(GenerateFileServiceImpl.class);
	
	private String[] statusArr = new String[] { "0", "1" };
	private String[] hobbyArr = new String[] { "篮球,\\\"台球\\\"", "足球,\\\"爬山\\\"", "排球", "羽毛球,\\\"竞走\\\"", "乒乓球",
			"围棋,\\\"马拉松\\\"", "象棋,\\\"保龄球\\\"", "'游泳'", "'\\\"击剑", "徒步\\\"'" };

	public void generateCsvFile(String filePath, String encoding, int countNum) {
		String str=null;
		String xmlTmpStr="#id#,#firstName#,#secondName#,#birthday#,#gender#,#maritalStatus#,#workDate#,\"#hobby#\"";
		long id = 1L;
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
			bw.write("id,first_name,second_name,birthday,gender,marital_status,work_date,hobby");
			bw.write("\r\n");
			for (int i = 1; i < countNum; i++) {
				str = xmlTmpStr.replace("#id#", Long.toString(id))
						.replace("#firstName#", GenerateRandDataDao.getFirstName())
						.replace("#secondName#", GenerateRandDataDao.getSecondName())
						.replace("#birthday#",
								GenerateRandDataDao.generateRandDate("1980-01-01 0:0:0", "1990-12-31 0:0:0"))
						.replace("#gender#", GenerateRandDataDao.generateRandStr(statusArr))
						.replace("#maritalStatus#", GenerateRandDataDao.generateRandStr(statusArr))
						.replace("#workDate#",
								GenerateRandDataDao.generateRandDate("2000-01-01 0:0:0", "2013-12-31 0:0:0"))
						.replace("#hobby#", GenerateRandDataDao.generateRandStr(hobbyArr).replace("\\\"", "\"\""));
				bw.write(str);
				bw.write("\r\n");
				if (i > 0 && i % 1000 == 0) {
					log.info("method {},generate number:{}","generateCsvFile",i);
				}
				id++;
			}
			str = xmlTmpStr.replace("#id#", Long.toString(id))
					.replace("#firstName#", GenerateRandDataDao.getFirstName())
					.replace("#secondName#", GenerateRandDataDao.getSecondName())
					.replace("#birthday#", GenerateRandDataDao.generateRandDate("1980-01-01 0:0:0", "1990-12-31 0:0:0"))
					.replace("#gender#", GenerateRandDataDao.generateRandStr(statusArr))
					.replace("#maritalStatus#", GenerateRandDataDao.generateRandStr(statusArr))
					.replace("#workDate#", GenerateRandDataDao.generateRandDate("2000-01-01 0:0:0", "2013-12-31 0:0:0"))
					.replace("#hobby#", GenerateRandDataDao.generateRandStr(hobbyArr).replace("\\\"", "\"\""));
			bw.write(str);
			bw.write("\r\n");
		} catch (Exception e) {
			log.error("generateCsvFile", e);
		}
	}

	public void generateXmlFile(String filePath, String encoding, int countNum) {
		String str=null;
		String xmlTmpStr="\t<personinfo>\n" +
						"    \t<id>#id#</id>\n" + 
						"    \t<first_name>#firstName#</first_name>\n" + 
						"    \t<second_name>#secondName#</second_name>\n" + 
						"    \t<birthday>#birthday#</birthday>\n" + 
						"    \t<gender>#gender#</gender>\n" + 
						"    \t<marital_status>#maritalStatus#</marital_status>\n" + 
						"    \t<work_date>#workDate#</work_date>\n" + 
						"    \t<hobby>\n" + 
						"        \t<![CDATA[#hobby#]]>\n" + 
						"    \t</hobby>\n" + 
						"\t</personinfo>";
		long id = 1L;
		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
			bw.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
			bw.write("<root>\r\n");
			for (int i = 1; i < countNum; i++) {
				str = xmlTmpStr.replace("#id#", Long.toString(id))
						.replace("#firstName#", GenerateRandDataDao.getFirstName())
						.replace("#secondName#", GenerateRandDataDao.getSecondName())
						.replace("#birthday#",
								GenerateRandDataDao.generateRandDate("1980-01-01 0:0:0", "1990-12-31 0:0:0"))
						.replace("#gender#", GenerateRandDataDao.generateRandStr(statusArr))
						.replace("#maritalStatus#", GenerateRandDataDao.generateRandStr(statusArr))
						.replace("#workDate#",
								GenerateRandDataDao.generateRandDate("2000-01-01 0:0:0", "2013-12-31 0:0:0"))
						.replace("#hobby#", GenerateRandDataDao.generateRandStr(hobbyArr));
				bw.write(str);
				bw.write("\r\n");
				if (i > 0 && i % 1000 == 0) {
					log.info("method {},generate number:{}","generateXmlFile",i);
				}
				id++;
			}
			str = xmlTmpStr.replace("#id#", Long.toString(id))
					.replace("#firstName#", GenerateRandDataDao.getFirstName())
					.replace("#secondName#", GenerateRandDataDao.getSecondName())
					.replace("#birthday#", GenerateRandDataDao.generateRandDate("1980-01-01 0:0:0", "1990-12-31 0:0:0"))
					.replace("#gender#", GenerateRandDataDao.generateRandStr(statusArr))
					.replace("#maritalStatus#", GenerateRandDataDao.generateRandStr(statusArr))
					.replace("#workDate#", GenerateRandDataDao.generateRandDate("2000-01-01 0:0:0", "2013-12-31 0:0:0"))
					.replace("#hobby#", GenerateRandDataDao.generateRandStr(hobbyArr));
			bw.write(str);
			bw.write("\r\n");
			bw.write("</root>");
		} catch (Exception e) {
			log.error("generateXmlFile", e);
		}
	}

	public void generateJsonFile(String filePath, String encoding, int countNum) {
		String str=null;
        String jsonTmpStr=
           "        {\n" +
           "            \"personinfo\": {\n" + 
           "                \"id\": #id#,\n" +	   
           "                \"first_name\": \"#firstName#\",\n" + 
           "                \"second_name\": \"#secondName#\",\n" + 
           "                \"birthday\": \"#birthday#\",\n" + 
           "                \"gender\": \"#gender#\",\n" + 
           "                \"marital_status\": \"#maritalStatus#\",\n" + 
           "                \"work_date\": \"#workDate#\",\n" + 
           "                \"hobby\": \"#hobby#\"\n"+ 
           "            }\n" + 
           "        }";
        long id=1L;
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath),encoding))) {
			bw.write("{\"root\":\r\n [\r\n");
			for (int i = 1; i < countNum; i++) {
				str = jsonTmpStr.replace("#id#", Long.toString(id))
						.replace("#firstName#", GenerateRandDataDao.getFirstName())
						.replace("#secondName#", GenerateRandDataDao.getSecondName())
						.replace("#birthday#",
								GenerateRandDataDao.generateRandDate("1980-01-01 0:0:0", "1990-12-31 0:0:0"))
						.replace("#gender#", GenerateRandDataDao.generateRandStr(statusArr))
						.replace("#maritalStatus#", GenerateRandDataDao.generateRandStr(statusArr))
						.replace("#workDate#",
								GenerateRandDataDao.generateRandDate("2000-01-01 0:0:0", "2013-12-31 0:0:0"))
						.replace("#hobby#", GenerateRandDataDao.generateRandStr(hobbyArr));
				bw.write(str);
				bw.write(",\r\n");
				if (i > 0 && i % 1000 == 0) {
					log.info("method {},generate number:{}","generateJsonFile",i);
				}
				id++;
			}
			str = jsonTmpStr.replace("#id#", Long.toString(id))
					.replace("#firstName#", GenerateRandDataDao.getFirstName())
					.replace("#secondName#", GenerateRandDataDao.getSecondName())
					.replace("#birthday#", GenerateRandDataDao.generateRandDate("1980-01-01 0:0:0", "1990-12-31 0:0:0"))
					.replace("#gender#", GenerateRandDataDao.generateRandStr(statusArr))
					.replace("#maritalStatus#", GenerateRandDataDao.generateRandStr(statusArr))
					.replace("#workDate#", GenerateRandDataDao.generateRandDate("2000-01-01 0:0:0", "2013-12-31 0:0:0"))
					.replace("#hobby#", GenerateRandDataDao.generateRandStr(hobbyArr));
			bw.write(str);
			bw.write("\r\n ]\r\n}");
        }catch (Exception e) {
			log.error("generateJsonFile", e);
		}
	}

}
