package model;

public class Student {
	private Long chatId;
	private String userSiga;
	private String passSiga;

	public Student(Long chatId) {
		this.chatId = chatId;
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

	public Long getChatId() {
		return chatId;
	}

	public void setChatId(Long chatId) {
		this.chatId = chatId;
	}

}
