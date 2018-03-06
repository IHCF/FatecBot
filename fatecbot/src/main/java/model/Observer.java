package model;

import com.pengrad.telegrambot.model.request.Keyboard;

public interface Observer {
	public void update(long chatId, String message, Keyboard keyBoard, boolean replyMessage);
}
