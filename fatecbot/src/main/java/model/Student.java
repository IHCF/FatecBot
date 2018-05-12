package model;

public class Student {
	private Long chatId;
	private String nickname;
	private String idSiga;
	private String passwordSiga;

	public Student(Long chatId, String nickname, String idSiga, String passwordSiga) {
		this.chatId = chatId;
		this.idSiga = idSiga;
		this.nickname = nickname;
		this.passwordSiga = passwordSiga;
	}

	public Student(Long chatId, String idSiga, String passwordSiga) {
		this.chatId = chatId;
		this.idSiga = idSiga;
		this.passwordSiga = passwordSiga;
	}

	public String getNickname() {
		return nickname;
	}

	public Long getChatId() {
		return chatId;
	}

	public String getSigaId() {
		return idSiga;
	}

	public String getPasswordSiga() {
		return passwordSiga;
	}
}
