package me.mrletsplay.webinterfaceapi.setup.impl;

import me.mrletsplay.mrcore.json.JSONObject;
import me.mrletsplay.webinterfaceapi.Webinterface;
import me.mrletsplay.webinterfaceapi.config.Config;
import me.mrletsplay.webinterfaceapi.config.DefaultSettings;
import me.mrletsplay.webinterfaceapi.setup.ChoiceList;
import me.mrletsplay.webinterfaceapi.setup.SetupStep;

public class DatabaseSetupStep extends SetupStep {

	public static final String ID = "database";

	public DatabaseSetupStep() {
		super(ID, "Database");

		addChoice("database", "Database", new ChoiceList().addChoice("file", "File").addChoice("sql", "SQL"), "file");

		addHeading("SQL Settings");
		addChoice("sql-provider", "SQL Provider", new ChoiceList().addChoice("mysql", "MySQL").addChoice("mariadb", "MariaDB"), "mysql");
		addString("sql-host", "SQL Host", DefaultSettings.SQL_HOST.getDefaultValue());
		addInteger("sql-port", "SQL Port", DefaultSettings.SQL_PORT.getDefaultValue());
		addString("sql-user", "SQL User", DefaultSettings.SQL_USER.getDefaultValue());
		addString("sql-password", "SQL Password", DefaultSettings.SQL_PASSWORD.getDefaultValue());
		addString("sql-database", "SQL Database", DefaultSettings.SQL_DATABASE.getDefaultValue());
		addString("sql-table-prefix", "SQL Table Prefix", DefaultSettings.SQL_TABLE_PREFIX.getDefaultValue());
	}

	@Override
	public String callback(JSONObject data) {
		String database = data.getString("database");

		Config cfg = Webinterface.getConfig();
		cfg.setSetting(DefaultSettings.DATABASE, database);

		if(database.equals("sql")) {
			String provider = data.getString("sql-provider");
			String host = data.getString("sql-host").trim();
			int port = data.getInt("sql-port");
			String user = data.getString("sql-user").trim();
			String password = data.getString("sql-password").trim();
			String sqlDatabase = data.getString("sql-database").trim();
			String tablePrefix = data.getString("sql-table-prefix").trim();

			if(host.isEmpty()) return "SQL host must be set";
			if(port < 1 || port > 65535) return "Invalid SQL port";

			if(user.isEmpty()) return "SQL user must be set";
			if(password.isEmpty()) return "SQL password must be set";
			if(sqlDatabase.isEmpty()) return "SQL database must be set";
			if(tablePrefix.isEmpty()) tablePrefix = null;

			cfg.setSetting(DefaultSettings.SQL_PROVIDER, provider);
			cfg.setSetting(DefaultSettings.SQL_HOST, host);
			cfg.setSetting(DefaultSettings.SQL_PORT, port);
			cfg.setSetting(DefaultSettings.SQL_USER, user);
			cfg.setSetting(DefaultSettings.SQL_PASSWORD, password);
			cfg.setSetting(DefaultSettings.SQL_DATABASE, sqlDatabase);
			cfg.setSetting(DefaultSettings.SQL_TABLE_PREFIX, tablePrefix);
		}

		Webinterface.initializeDatabase();

		return null;
	}

}
