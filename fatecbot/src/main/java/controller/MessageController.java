package controller;

import com.pengrad.telegrambot.model.Update;

import model.Model;
import view.View;

public class MessageController {

	private Model model;
	private View view;

	public MessageController(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	public void process(Update update) {
		view.sendTypingMessage(update);
		model.processMessage(update);
	}
}
