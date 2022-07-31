package me.mrletsplay.webinterfaceapi.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLAction {

	public void run(Connection connection) throws SQLException;

}