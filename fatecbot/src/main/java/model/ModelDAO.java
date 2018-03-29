package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ModelDAO implements DataBase {
	public Connection connect() throws SQLException {
		return DriverManager.getConnection(String.format(URL, USER, PASSWORD));
	}
}
