package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ModelDAO {
	private static Connection connect() throws SQLException, FileNotFoundException, IOException {

		String[] dataBaseInformations = ToolBox.loadDataBaseConfigures();
		return DriverManager.getConnection(dataBaseInformations[0], dataBaseInformations[1], dataBaseInformations[2]);
	}

	public static int createStudent(Long chatId, String nick, String username, String password)
			throws SQLException, FileNotFoundException, IOException {

		String sql = "INSERT INTO aluno (telegram_code, nome, usuario_siga, senha_siga, ultima_atualizacao) VALUES (?, ?, ?, ?, ?)";
		Connection conn = ModelDAO.connect();
		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setLong(1, chatId);
		ps.setString(2, nick);
		ps.setString(3, username);
		ps.setString(4, password);
		ps.setString(5, LocalDate.now().toString());
		ps.execute();

		return 1;
	};

	public static Student selectStudent(Long chatId) throws SQLException, FileNotFoundException, IOException {

		Student student = null;
		String sql = "SELECT usuario_siga, senha_siga FROM aluno WHERE telegram_code = ?";

		Connection conn = ModelDAO.connect();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, Long.toString(chatId));

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			try {
				// Verifica pois pode ocorrer de não encontrar o usuário
				if (rs.getString("usuario_siga") != null) {
					student = new Student(rs.getString("usuario_siga"), rs.getString("senha_siga"));
				}
			} catch (Exception e) {
				return student;
			}
		}
		return student;
	}
}
