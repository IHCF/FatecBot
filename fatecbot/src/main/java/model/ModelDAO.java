package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

public interface ModelDAO {

	boolean createStudent(Student student) throws SQLException, FileNotFoundException, IOException;

	Student selectStudent(Long chatId) throws SQLException, FileNotFoundException, IOException;

	boolean deleteStudent(Long chatId) throws SQLException, FileNotFoundException, IOException;
}
