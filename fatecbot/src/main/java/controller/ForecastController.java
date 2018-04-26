package controller;

import com.pengrad.telegrambot.model.Update;

import model.Model;
import view.View;

public class ForecastController implements ProcessController {

	private Model model;
	private View view;

	public ForecastController(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void process(Update update) {
		view.sendTypingMessage(update);
		model.searchForecast(update.message().chat().id());
	}

}
