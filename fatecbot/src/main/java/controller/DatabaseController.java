package controller;

import java.io.IOException;

import com.pengrad.telegrambot.model.Update;

import model.DatabaseFourObject;
import model.Model;
import model.Postgres;
import model.PropertiesUtils;
import view.View;

/**
 * Controller para definir qual será o banco de dados utilizado pela aplicação
 */
public class DatabaseController implements ProcessController {

	private Model model;
	private View view;

	public DatabaseController(Model model, View view) {
		this.model = model;
		this.view = view;
	}

	public void process(Update update) {
		view.sendTypingMessage(update);

		Long adminKey = null;
		String reply = "Banco de dados atualizado !";
		String message = update.message().text();
		Long chatId = update.message().chat().id();

		// Recupera o chatid do administrador do bot
		try {
			adminKey = Long.parseLong(PropertiesUtils.loadAdminKey());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Verificando qual banco de dados foi selecionado
		// pelo usuário
		if (message.equals("/change_databases_pg") && chatId.equals(adminKey)) {
			model.setDatabase(new Postgres());
		}

		else if (message.equals("/change_databases_dbo") && chatId.equals(adminKey)) {
			model.setDatabase(new DatabaseFourObject());
		}
		// Com este comando é verificado se o usuário não é administrador e altera a
		// forma de exibição para o usuário
		if (!chatId.equals(adminKey)) {
			reply = "Ops! Não entendi o que você disse =(";
		}
		view.update(chatId, reply, false, false, false);

	}
}
