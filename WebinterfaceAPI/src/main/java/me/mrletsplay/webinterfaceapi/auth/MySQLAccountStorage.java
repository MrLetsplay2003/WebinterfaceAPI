package me.mrletsplay.webinterfaceapi.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbcp2.BasicDataSource;

import me.mrletsplay.webinterfaceapi.exception.StorageException;


public class MySQLAccountStorage implements AccountStorage {

	private String url;
	private String username;
	private String password;
	private String database;
	private String tablePrefix;
	private BasicDataSource dataSource;

	public MySQLAccountStorage(String url, String username, String password, String database, String tablePrefix) {
		this.url = url;
		this.username = username;
		this.password = password;
		this.database = database;
		this.tablePrefix = tablePrefix;
	}

	private String prefix(String tableName) {
		if(tablePrefix == null || tablePrefix.isBlank()) {
			return tableName;
		}

		return tablePrefix + "_" + tableName;
	}

	private void run(SQLAction action) {
		try(Connection c = dataSource.getConnection()) {
			action.run(c);
		}catch(SQLException e) {
			throw new StorageException("MySQL error", e);
		}
	}

	private <T> T run(ReturningSQLAction<T> action) {
		try(Connection c = dataSource.getConnection()) {
			return action.run(c);
		}catch(SQLException e) {
			throw new StorageException("MySQL error", e);
		}
	}

	@Override
	public void initialize() {
		this.dataSource = new BasicDataSource();
		dataSource.setUrl(url);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		dataSource.setMaxTotal(10);
		dataSource.setDefaultQueryTimeout(10);
		dataSource.setMaxWaitMillis(30000);
		dataSource.setDefaultSchema(database);
		dataSource.setDefaultCatalog(database);

		run(c -> {
			try(PreparedStatement st = c.prepareStatement("CREATE TABLE IF NOT EXISTS " + prefix("accounts") + "(`Id` VARCHAR(255) PRIMARY KEY, Username VARCHAR(255) NOT NULL, Email VARCHAR(255) DEFAULT NULL, Avatar TEXT DEFAULT NULL)")) {
				st.execute();
			}
		});

		run(c -> {
			try(PreparedStatement st = c.prepareStatement("CREATE TABLE IF NOT EXISTS " + prefix("account_connections") + "(AccountId VARCHAR(255) NOT NULL, Connection VARCHAR(255) NOT NULL, UserId VARCHAR(255) NOT NULL, UserName VARCHAR(255) NOT NULL, UserEmail VARCHAR(255) DEFAULT NULL, UserAvatar TEXT DEFAULT NULL, PRIMARY KEY (AccountId, Connection), FOREIGN KEY (AccountId) REFERENCES " + prefix("accounts") + "(`Id`) ON DELETE CASCADE)")) {
				st.execute();
			}
		});

		run(c -> {
			try(PreparedStatement st = c.prepareStatement("CREATE TABLE IF NOT EXISTS " + prefix("account_permissions") + "(AccountId VARCHAR(255) NOT NULL, Permission TEXT NOT NULL, FOREIGN KEY (AccountId) REFERENCES " + prefix("accounts") + "(`Id`) ON DELETE CASCADE)")) {
				st.execute();
			}
		});
	}

	@Override
	public Account createAccount(AccountConnection connection) {
		Account acc = new Account(UUID.randomUUID().toString(), connection.getUserName());
		updateAccount(acc);
		return acc;
	}

	@Override
	public void updateAccount(Account account) {
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("INSERT INTO " + prefix("accounts") + "(`Id`, Username, Email, Avatar) VALUES(?, ?, ?, ?) ON DUPLICATE KEY UPDATE Username = VALUES(Username), Email = VALUES(Email), Avatar = VALUES(Avatar)")) {
				st.setString(1, account.getID());
				st.setString(2, account.getUsername());
				st.setString(3, account.getEmail());
				st.setString(4, account.getAvatar());
				st.execute();
			}
		});
	}

	@Override
	public void deleteAccount(String accountID) {
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("DELETE FROM " + prefix("accounts") + " WHERE `Id` = ?")) {
				st.setString(1, accountID);
				st.execute();
			}
		});
	}

	private Account getAccount(ResultSet r) throws SQLException {
		return new Account(r.getString("Id"), r.getString("Username"), r.getString("Avatar"), r.getString("Email"));
	}

	@Override
	public Account getAccountByID(String id) {
		return run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + prefix("accounts") + " WHERE `Id` = ?")) {
				st.setString(1, id);
				try(ResultSet r = st.executeQuery()) {
					if(!r.next()) return null;
					return getAccount(r);
				}
			}
		});
	}

	@Override
	public Account getAccountByEmail(String email) {
		return run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + prefix("accounts") + " WHERE Email = ?")) {
				st.setString(1, email);
				try(ResultSet r = st.executeQuery()) {
					if(!r.next()) return null;
					return getAccount(r);
				}
			}
		});
	}

	@Override
	public Account getAccountByConnectionSpecificID(String connectionName, String id, boolean caseInsensitive) {
		return run(c -> {
			try(PreparedStatement st = c.prepareStatement("Select * FROM " + prefix("account_connections") + " JOIN " + prefix("accounts") + " ON " + prefix("accounts") + ".`Id` = " + prefix("account_connections") + ".AccountId WHERE Connection = ? AND UserId = ?")) {
				st.setString(1, connectionName);
				st.setString(2, id);
				try(ResultSet r = st.executeQuery()) {
					if(!r.next()) return null;
					return getAccount(r);
				}
			}
		});
	}

	@Override
	public List<Account> getAccounts() {
		return run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + prefix("accounts"))) {
				try(ResultSet r = st.executeQuery()) {
					List<Account> accs = new ArrayList<>();
					while(r.next()) {
						accs.add(getAccount(r));
					}
					return accs;
				}
			}
		});
	}

	private AccountConnection getConnection(ResultSet r) throws SQLException {
		return new AccountConnection(r.getString("Connection"), r.getString("UserId"), r.getString("UserName"), r.getString("UserEmail"), r.getString("UserAvatar"));
	}

	@Override
	public List<AccountConnection> getConnections(String accountID) {
		return run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + prefix("account_connections") + " WHERE AccountId = ?")) {
				st.setString(1, accountID);
				try(ResultSet r = st.executeQuery()) {
					List<AccountConnection> cons = new ArrayList<>();
					while(r.next()) {
						cons.add(getConnection(r));
					}
					return cons;
				}
			}
		});
	}

	@Override
	public AccountConnection getConnection(String accountID, String connectionName) {
		return run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT * FROM " + prefix("account_connections") + " WHERE AccountId = ? AND Connection = ?")) {
				st.setString(1, accountID);
				st.setString(2, connectionName);
				try(ResultSet r = st.executeQuery()) {
					if(!r.next()) return null;
					return getConnection(r);
				}
			}
		});
	}

	@Override
	public void addConnection(String accountID, AccountConnection connection) {
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("INSERT IGNORE INTO " + prefix("account_connections") + "(AccountId, Connection, UserId, UserName, UserEmail, UserAvatar) VALUES(?, ?, ?, ?, ?, ?)")) {
				st.setString(1, accountID);
				st.setString(2, connection.getConnectionName());
				st.setString(3, connection.getUserID());
				st.setString(4, connection.getUserName());
				st.setString(5, connection.getUserEmail());
				st.setString(6, connection.getUserAvatar());
				st.execute();
			}
		});
	}

	@Override
	public void removeConnection(String accountID, String connectionName) {
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("DELETE FROM " + prefix("account_connections") + " WHERE AccountId = ? AND Connection = ?")) {
				st.setString(1, accountID);
				st.setString(2, connectionName);
				st.execute();
			}
		});
	}

	@Override
	public List<String> getPermissions(String accountID) {
		return run(c -> {
			try(PreparedStatement st = c.prepareStatement("SELECT Permission FROM " + prefix("account_permissions") + " WHERE AccountId = ?")) {
				st.setString(1, accountID);
				try(ResultSet r = st.executeQuery()) {
					List<String> perms = new ArrayList<>();
					while(r.next()) {
						perms.add(r.getString("Permission"));
					}
					return perms;
				}
			}
		});
	}

	@Override
	public void setPermissions(String accountID, List<String> permissions) {
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("DELETE FROM " + prefix("account_permissions") + " WHERE AccountId = ?")) {
				st.setString(1, accountID);
				st.execute();
			}

			if(!permissions.isEmpty()) {
				try(PreparedStatement st = c.prepareStatement("INSERT INTO " + prefix("account_permissions") + "(AccountId, Permission) VALUES(?, ?)")) {
					for(String perm : permissions) {
						st.setString(1, accountID);
						st.setString(2, perm);
						st.addBatch();
					}
					st.executeBatch();
				}
			}
		});
	}

	@Override
	public void addPermission(String accountID, String permission) {
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("INSERT INTO " + prefix("account_permissions") + "(AccountId, Permission) VALUES(?, ?)")) {
				st.setString(1, accountID);
				st.setString(2, permission);
				st.execute();
			}
		});
	}

	@Override
	public void removePermission(String accountID, String permission) {
		run(c -> {
			try(PreparedStatement st = c.prepareStatement("DELETE FROM " + prefix("account_permissions") + " WHERE AccountId = ? AND Permission = ?")) {
				st.setString(1, accountID);
				st.setString(2, permission);
				st.execute();
			}
		});
	}

	@FunctionalInterface
	private interface SQLAction {

		public void run(Connection connection) throws SQLException;

	}

	@FunctionalInterface
	private interface ReturningSQLAction<T> {

		public T run(Connection connection) throws SQLException;

	}

}
