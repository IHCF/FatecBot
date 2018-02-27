package model;

public class Student {
	private Long chatId;
	private String userSiga;
	private String passSiga;
	private String hashSiga;

	public Student(Long chatId) {
		this.chatId = chatId;
	}

	public Student(Long chatId, String hashSiga) {
		this.chatId = chatId;
		this.hashSiga = hashSiga;
	}

	public String getUserSiga() {
		return userSiga;
	}

	public void setUserSiga(String userSiga) {
		this.userSiga = userSiga;
	}

	public String getPassSiga() {
		return passSiga;
	}

	public void setPassSiga(String passSiga) {
		this.passSiga = passSiga;
	}

}
