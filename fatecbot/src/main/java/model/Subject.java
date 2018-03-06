package model;

import com.pengrad.telegrambot.model.request.Keyboard;

public interface Subject {
	public void registerObserver(Observer observer);

	public void notifyObserver(long chatId, String message, Keyboard keyBoard, boolean replyMessage);
}
