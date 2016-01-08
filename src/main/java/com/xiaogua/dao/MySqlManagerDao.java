package com.xiaogua.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySqlManagerDao {
	private static Logger log = LoggerFactory.getLogger(MySqlManagerDao.class);
	private static BasicDataSource dataSource;
	private static String jdbcDriver = "com.mysql.jdbc.Driver";
	private static String database = "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useOldAliasMetadataBehavior=true&useSSL=false";
	private static String user = "root";
	private static String password = "1234";

	public MySqlManagerDao() {
		super();
	}

	static {
		dataSource = new BasicDataSource();
		dataSource.setDriverClassName(jdbcDriver);
		dataSource.setUrl(database);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
	}

	public static Connection getMysqlConnection() throws Exception {
		return dataSource.getConnection();
	}

	public static void insertDataToDb(String dbSql) {
		try (Connection conn = getMysqlConnection(); Statement statement = conn.createStatement()) {
			statement.executeUpdate(dbSql.toString());
		} catch (Exception e) {
			log.error("insertDataToDb", e);
		}
	}

	public static void executeUpdateSql(String cleanSql) {
		try (Connection conn = getMysqlConnection(); PreparedStatement ps = conn.prepareStatement(cleanSql)) {
			ps.executeUpdate();
		} catch (Exception e) {
			log.error("executeUpdateSql", e);
		}
	}

}
