package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Postgres implements ModelDAO {
	private static Connection connect() throws SQLException, FileNotFoundException, IOException {

		String[] dataBaseInformations = PropertiesUtils.loadDataBaseConfigures();
		return DriverManager.getConnection(dataBaseInformations[0], dataBaseInformations[1], dataBaseInformations[2]);
	}

	public boolean createStudent(Student student) throws SQLException, FileNotFoundException, IOException {

		String sql = "INSERT INTO aluno (alu_telegram_code, alu_nome, alu_usuario_siga, alu_senha_siga) VALUES (?, ?, ?, ?)";
		Connection conn = Postgres.connect();
		PreparedStatement ps = conn.prepareStatement(sql);

		// Trata a exceção para devolver a quem chamou o método somente o necessário
		try {
			ps.setLong(1, student.getChatId());
			ps.setString(2, student.getNickname());
			ps.setString(3, student.getSigaId());
			ps.setString(4, student.getPasswordSiga());
			ps.execute();
		} catch (Exception e) {
			return false;
		}

		return true;
	};

	public Student selectStudent(Long chatId) throws SQLException, FileNotFoundException, IOException {

		Student student = null;
		String sql = "SELECT alu_usuario_siga, alu_senha_siga FROM aluno WHERE alu_telegram_code = ?";

		Connection conn = Postgres.connect();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, Long.toString(chatId));

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			try {
				// Verifica pois pode ocorrer de não encontrar o usuário
				if (rs.getString("alu_usuario_siga") != null) {
					student = new Student(chatId, rs.getString("alu_usuario_siga"), rs.getString("alu_senha_siga"));
				}
			} catch (Exception e) {
				return null;
			}
		}
		return student;
	}

	public boolean deleteStudent(Long chatId) throws SQLException, FileNotFoundException, IOException {

		// Aplicado desta forma para gerar o softdelete
		String sql = "UPDATE aluno SET alu_ativo = 0 WHERE alu_telegram_code = ?";

		Connection conn = Postgres.connect();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, Long.toString(chatId));

		ps.executeUpdate();
	
		return true;
	}
}
