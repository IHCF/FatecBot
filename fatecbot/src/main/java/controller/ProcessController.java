package controller;

import com.pengrad.telegrambot.model.Update;

public interface ProcessController {
	
	public void process(Update update);
}
