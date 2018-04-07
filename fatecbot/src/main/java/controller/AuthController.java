package controller;

import com.pengrad.telegrambot.model.Update;

import model.Model;
import model.State;
import model.Student;
import view.View;

public class AuthController implements ProcessController {

	private Model model;
	private View view;

	private String sigaId = null;
	private String password = null;

	public AuthController(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	public void process(Update update) {
		if (view.getState() != State.IS_NOTHING.getState()) {
			view.sendTypingMessage(update);

			if (view.getState() == State.IS_RECOVERY_USER.getState()) {
				Student student = model.recoveryUser(update.message().chat().id(), true);

				// Verificando se o usuário está registrado
				if (student != null) {
					sigaId = student.getSigaId();
					password = student.getPasswordSiga();
				}
			} else if (view.getState() == State.IS_REGISTERING_USERNAME.getState()) {
				sigaId = update.message().text();
			} else if (view.getState() == State.IS_REGISTERING_PASSWORD.getState()) {
				password = update.message().text();
			}

			if (sigaId != null && password != null) {
				if (view.getState() != State.IS_RECOVERY_USER.getState()) {
					System.out.println("Entrei aqui");
					model.addUser(update.message().chat().id(), update.message().chat().firstName(), sigaId, password);
				}
				view.setState(State.IS_NOTHING.getState());
			}
		}

	}
}
