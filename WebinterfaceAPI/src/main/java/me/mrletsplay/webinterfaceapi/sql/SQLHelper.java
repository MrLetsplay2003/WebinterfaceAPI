package me.mrletsplay.webinterfaceapi.sql;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp2.BasicDataSource;

import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.exception.StorageException;

public class SQLHelper {

	private static String prefix;
	private static BasicDataSource dataSource;

	public static void initialize() {
		Config config = Webinterface.getConfig();
		String provider = config.getSetting(DefaultSettings.SQL_PROVIDER);
		String host = config.getSetting(DefaultSettings.SQL_HOST);
		int port = config.getSetting(DefaultSettings.SQL_PORT);
		String user = config.getSetting(DefaultSettings.SQL_USER);
		String pass = config.getSetting(DefaultSettings.SQL_PASSWORD);
		String database = config.getSetting(DefaultSettings.SQL_DATABASE);
		prefix = config.getSetting(DefaultSettings.SQL_TABLE_PREFIX);

		String url = "jdbc:" + provider + "://" + host + ":" + port;
		dataSource = new BasicDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(pass);
		dataSource.setMaxTotal(10);
		dataSource.setDefaultQueryTimeout(10);
		dataSource.setMaxWaitMillis(30000);
		dataSource.setDefaultSchema(database);
		dataSource.setDefaultCatalog(database);
	}

	public static String tableName(String tableName) {
		return prefix == null ? tableName : (prefix + "_" + tableName);
	}

	public static void run(SQLAction action) {
		if(dataSource == null) throw new IllegalStateException("SQL is not configured");
		try(Connection c = dataSource.getConnection()) {
			action.run(c);
		}catch(SQLException e) {
			throw new StorageException("SQL error", e);
		}
	}

	public static <T> T run(ReturningSQLAction<T> action) {
		if(dataSource == null) throw new IllegalStateException("SQL is not configured");
		try(Connection c = dataSource.getConnection()) {
			return action.run(c);
		}catch(SQLException e) {
			throw new StorageException("SQL error", e);
		}
	}

}
