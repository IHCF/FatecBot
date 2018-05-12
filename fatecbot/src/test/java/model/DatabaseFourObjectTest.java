package model;

import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseFourObjectTest {

	private DatabaseFourObject database;

	@Before
	public void setUp() {
		database = new DatabaseFourObject();
	}

	@After
	public void tearDown() {
		database = null;
	}

	/**
	 * Teste de inserção de usuários no banco. Espera-se true já que a inserção é
	 * válida
	 * 
	 * @throws IOException
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws NumberFormatException
	 */

	@Test
	public void testAInsertStudent() throws NumberFormatException, FileNotFoundException, SQLException, IOException {
		assertTrue(database.createStudent(new Student(Long.parseLong("9998"), "0000", "0000")));
	}
}
