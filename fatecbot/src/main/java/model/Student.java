package model;

public class Student {
	private String idSiga;
	private String passwordSiga;

	public Student(String idSiga, String passwordSiga) {
		this.idSiga = idSiga;
		this.passwordSiga = passwordSiga;
	}

	public String getSigaId() {
		return idSiga;
	}

	public String getPasswordSiga() {
		return passwordSiga;
	}
}
