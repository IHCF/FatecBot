package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

public class DatabaseFourObject implements ModelDAO {

	private ObjectContainer students = null;
	private boolean state;

	/**
	 * Método Singleton. Para evitar que as conexões sejam fechadas.
	 * 
	 * @return
	 */
	private ObjectContainer connect() {
		if (students == null)
			students = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), "database/students.db4o");
		return students;
	}

	/**
	 * Método para adicionar um novo student ao banco de dados
	 */
	public boolean createStudent(Student student) throws SQLException, FileNotFoundException, IOException {

		state = isUserAvailable(student.getChatId());

		if (state) {
			students = connect();
			students.store(student);
			students.commit();
		}

		return state;
	}

	/**
	 * Método para recuperar do banco de dados algum objeto do tipo Student.
	 */
	public Student selectStudent(Long chatId) throws SQLException, FileNotFoundException, IOException {

		Student studentTemp = null; // Objeto que será retornado
		ObjectSet<Student> allStudents = getAllStudents();

		for (Student student : allStudents) {
			if (student.getChatId().equals(chatId))
				studentTemp = student;
		}
		return studentTemp;
	}

	/**
	 * Método para a deleção do usuário do banco de dados
	 */
	public boolean deleteStudent(Long chatId) throws SQLException, FileNotFoundException, IOException {
		state = false;
		ObjectSet<Student> allStudents = getAllStudents();

		for (Student student : allStudents) {
			if (student.getChatId().equals(chatId)) {
				students.delete(student);
				state = true;
			}
		}
		return state;
	}

	/**
	 * Método para realizar a query de todos os alunos no banco de dados
	 * 
	 * @return
	 */
	private ObjectSet<Student> getAllStudents() {
		students = connect();
		Query query = students.query();
		query.constrain(Student.class);

		// Recupera todos os alunos do banco de dados
		return query.execute();
	}

	/**
	 * Método para verificar se os usuários estão disponíveis para inserção no banco
	 * 
	 * @param chatId
	 * @return
	 */
	private boolean isUserAvailable(Long chatId) {

		// Recupera todos os alunos do banco de dados
		ObjectSet<Student> allStudents = getAllStudents();

		for (Student student : allStudents) {
			if (student.getChatId().equals(chatId))
				return false;
		}

		return true;
	}

}
