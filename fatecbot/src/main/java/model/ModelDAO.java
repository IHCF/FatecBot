package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelDAO {
	private static Connection connect() throws SQLException, FileNotFoundException, IOException {

		String[] dataBaseInformations = ToolBox.loadDataBaseConfigures();

		return DriverManager.getConnection(dataBaseInformations[0], dataBaseInformations[1], dataBaseInformations[2]);
	}

	public static int createStudent(Long chatId, String username, String password)
			throws SQLException, FileNotFoundException, IOException {

		String sql = "INSERT INTO aluno (telegram_code, usuario_siga, senha_siga) VALUES (?, ?, ?)";
		Connection conn = ModelDAO.connect();
		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setLong(1, chatId);
		ps.setString(2, username);
		ps.setString(3, password);
		ps.execute();

		return 1;
	};

	public static String[] selectStudent(Long chatId) throws SQLException, FileNotFoundException, IOException {

		String[] user = new String[2];
		String sql = "SELECT usuario_siga, senha_siga FROM ALUNO WHERE telegram_code = ?";

		Connection conn = ModelDAO.connect();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, Long.toString(chatId));

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			user[0] = rs.getString("usuario_siga");
			user[1] = rs.getString("senha_siga");
		}

		return user;
	}

}
