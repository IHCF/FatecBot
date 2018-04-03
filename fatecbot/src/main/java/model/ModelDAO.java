package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.cj.jdbc.PreparedStatement;

public class ModelDAO implements DataBase {
	static public Connection connect() throws SQLException {
		return DriverManager.getConnection(String.format(URL, USER, PASSWORD));
	}

	static public int createStudent (Long chatId, String username, String password){
		try {
			Connection con = ModelDAO.connect();
			PreparedStatement ps = (PreparedStatement) con.prepareStatement("INSERT INTO aluno (telegram_code, usuario_siga, senha_siga) VALUES (?, ?, ?)");
			ps.setLong(1, chatId);
			ps.setString(2, username);
			ps.setString(3, password);
			ps.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	};
}
