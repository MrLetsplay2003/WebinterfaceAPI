package me.mrletsplay.webinterfaceapi.sql;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ServiceLoader;

import org.apache.commons.dbcp2.BasicDataSource;

import me.mrletsplay.mrcore.http.HttpRequest;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.exception.StorageException;
import me.mrletsplay.webinterfaceapi.exception.WebinterfaceInitException;

public class SQLHelper {

	public static final Map<String, String> DRIVERS;
	private static final String REPO_URL = "https://repo1.maven.org/maven2/%s/%s/%s/%s-%s.jar";

	static {
		Map<String, String> drivers = new HashMap<>();
		drivers.put("mysql", "mysql:mysql-connector-java:8.0.30");
		drivers.put("mariadb", "org.mariadb.jdbc:mariadb-java-client:3.0.6");
		DRIVERS = Collections.unmodifiableMap(drivers);
	}

	private static URLClassLoader driverClassLoader;
	private static BasicDataSource dataSource;
	private static String prefix;

	public static void initialize() {
		Config config = Webinterface.getConfig();
		String provider = config.getSetting(DefaultSettings.SQL_PROVIDER);
		String host = config.getSetting(DefaultSettings.SQL_HOST);
		int port = config.getSetting(DefaultSettings.SQL_PORT);
		String user = config.getSetting(DefaultSettings.SQL_USER);
		String pass = config.getSetting(DefaultSettings.SQL_PASSWORD);
		String database = config.getSetting(DefaultSettings.SQL_DATABASE);
		prefix = config.getSetting(DefaultSettings.SQL_TABLE_PREFIX);

		String driver = DRIVERS.get(provider);
		if(driver == null) throw new WebinterfaceInitException("No driver available for SQL provider: " + provider);

		String[] spl = driver.split(":");
		File driverFile = new File(Webinterface.getDataDirectory(), "drivers/" + spl[1] + "-" + spl[2] + ".jar");

		if(!driverFile.exists()) {
			String driverURL = String.format(REPO_URL,
				spl[0].replace(".", "/"), // groupId
				spl[1], // artifactId
				spl[2], // version
				spl[1], // artifactId
				spl[2] // version
			);

			Webinterface.getLogger().info("Downloading SQL driver from " + driverURL);

			try {
				HttpRequest.createGet(driverURL).execute().transferTo(driverFile);
			} catch (IOException e) {
				throw new WebinterfaceInitException("Failed to download SQL driver", e);
			}
		}

		Webinterface.getLogger().debug("Using SQL driver at " + driverFile.getAbsolutePath());

		try {
			driverClassLoader = new URLClassLoader(new URL[] {driverFile.toURI().toURL()}, SQLHelper.class.getClassLoader());
		} catch (MalformedURLException e) {
			throw new WebinterfaceInitException("Failed to initialize class loader for SQL driver", e);
		}

		Optional<Driver> optDriver = ServiceLoader.load(Driver.class, driverClassLoader).findFirst();
		if(!optDriver.isPresent()) throw new WebinterfaceInitException("SQL driver not found in file " + driverFile.getAbsolutePath());

		String url = "jdbc:" + provider + "://" + host + ":" + port;
		dataSource = new BasicDataSource();
		dataSource.setDriverClassLoader(driverClassLoader);
		dataSource.setDriver(optDriver.get());
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(pass);
		dataSource.setMaxTotal(10);
		dataSource.setDefaultQueryTimeout(10);
		dataSource.setMaxWaitMillis(30000);
		dataSource.setDefaultSchema(database);
		dataSource.setDefaultCatalog(database);
	}

	public static void cleanUp() {
		if(driverClassLoader != null) {
			try {
				driverClassLoader.close();
			} catch (IOException e) {
				Webinterface.getLogger().warn("Failed to close class loader for SQL driver", e);
			}

			driverClassLoader = null;
		}

		dataSource = null;
		prefix = null;
	}

	public static boolean isAvailable() {
		return dataSource != null;
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
