package model;

import view.Observer;

public interface Subject {
	public void registerObserver(Observer observer);

	public void notifyObserver(long chatId, Object message, boolean keyBoard, boolean replyMessage, boolean isDocument);
}
