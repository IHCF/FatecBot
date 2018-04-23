package model;

import view.Observer;

public interface Subject {
	public void registerObserver(Observer observer);

	public void notifyObserver(long chatId, String message, boolean keyBoard, boolean replyMessage);
}
