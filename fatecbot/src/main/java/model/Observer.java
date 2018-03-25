package model;

public interface Observer {
	public void update(long chatId, String message, boolean keyBoard, boolean replyMessage);
}
