package me.mrletsplay.webinterfaceapi.sql;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface ReturningSQLAction<T> {

	public T run(Connection connection) throws SQLException;

}