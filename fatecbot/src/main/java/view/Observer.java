package view;

public interface Observer {
	public void update(long chatId, Object message, boolean keyBoard, boolean replyMessage, boolean isDocument);
}
