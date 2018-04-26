package controller;

import com.pengrad.telegrambot.model.Update;

import model.Model;
import view.View;

public class HistoryController implements ProcessController{
	
	private Model model;
	private View view;
	
	public HistoryController(Model model, View view) {
		this.model = model;
		this.view = view;
	}
	
	public void process(Update update) {
		view.sendTypingMessage(update);
		model.generateHistory(update.message().chat().id());
	}
	
}
