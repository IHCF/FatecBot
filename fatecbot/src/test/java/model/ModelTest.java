package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ModelTest extends TestCase {

	private MongoLab mLab;
	private Student student;
	private final long chatId = 123456789;

	@Before
	public void setUp() {

		try {
			mLab = new MongoLab();
			student = new Student(chatId);
		} catch (Exception e) {
			fail("Erro ao tentar inicializar a classe de conexão");
		}

	}

	/**
	 * Teste para verificar a adição de docs no mLab
	 */
	@Test
	public void testAddUser() {
		try {

			student.setUserSiga("JUnit_Test_User");
			student.setPassSiga("JUnit_Test_Password");

			mLab.addUser(student);
		} catch (Exception e) {
			fail("Erro ao tentar manipular os dados");
		}
	}

	/**
	 * Teste para verificar a remoção de docs no mLab
	 */
	@Test
	public void testRemoveUser() {

		try {
			mLab.removeUser(chatId);
		} catch (Exception e) {
			fail("Erro ao tentar remover usuário do banco de dados");
		}
	}

	@After
	public void tearDown() {
		mLab = null;
		student = null;
	}
}
