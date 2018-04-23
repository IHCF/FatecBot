package view;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;

import controller.AbsenceController;
import controller.AuthController;
import controller.DeleteController;
import controller.ProcessController;
import controller.SchedulesController;
import model.Model;

public class View implements Observer {

	private Model model;

	private TelegramBot bot;
	private Integer state = State.IS_NOTHING.getState();

	private ProcessController processController;

	public View(String botToken, Model model) {

		this.model = model;

		bot = new TelegramBot(botToken);
	}

	/**
	 * Método para ativar a escuta de mensagens do bot.
	 */
	public void receiveUsersMessages() {
		bot.setUpdatesListener(new UpdatesListener() {
			public int process(List<Update> updates) {
				try {
					processConversation(updates);
				} catch (Exception e) {
					System.out.println("Erro: " + e);
				}
				return CONFIRMED_UPDATES_ALL;
			}
		});
	}

	/**
	 * Método utilizado para processar cada uma das informações recebidas do Chat
	 */
	private void processConversation(List<Update> updates) {
		for (Update update : updates) {

			if (state == State.IS_NOTHING.getState()) {

				if (update.message().text().equals("/start")) {
					update(update.message().chat().id(),
							"Olá, seja bem-vindo ao FatecBot, assistente simples e fácil de utilizar,"
									+ " vou te ajudar com o SIGA. Para que eu tenha acesso as suas informações"
									+ " faça o registro utilizando o comando: \n/registro, caso já seja registrado use:  /recuperar",
							false, false);
					setController(new AuthController(model, this));
				}

				else if (update.message().text().equals("/registro")) {
					state = State.IS_REGISTERING.getState();
					update(update.message().chat().id(), "Insira seu nome de usuário do SIGA", false, true);
				}

				else if (update.message().text().equals("/recuperar")) {
					state = State.IS_RECOVERY_USER.getState();
				}

				else if (update.message().text().equals("Ver faltas")) {
					setController(new AbsenceController(model, this));
				}

				else if (update.message().text().equals("Ver horários")) {
					setController(new SchedulesController(model, this));
				}

				else if (update.message().text().equals("Remover usuário")) {
					setController(new DeleteController(model, this));
				}

			} else if (state == State.IS_REGISTERING.getState()) {
				state = State.IS_REGISTERING_USERNAME.getState();
				update(update.message().chat().id(), "Insira sua senha do SIGA", false, true);
			} else if (state == State.IS_REGISTERING_USERNAME.getState()) {
				state = State.IS_REGISTERING_PASSWORD.getState();
			}
			processController.process(update);
		}

	}

	/**
	 * Método que recebe informações que devem ser pasadas ao usuário.
	 */
	public void update(long chatId, String message, boolean keyBoard, boolean replyMessage) {

		if (replyMessage) {
			bot.execute(new SendMessage(chatId, message).replyMarkup(new ForceReply()));
		} else if (keyBoard) {
			bot.execute(new SendMessage(chatId, message)
					.replyMarkup(new ReplyKeyboardMarkup(new KeyboardButton[] { new KeyboardButton("Ver faltas"),
							new KeyboardButton("Ver horários"), new KeyboardButton("Remover usuário") })));
		} else {
			bot.execute(new SendMessage(chatId, message));
		}
	}

	public void sendTypingMessage(Update update) {
		bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}

	public void setController(ProcessController processController) {
		this.processController = processController;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getState() {
		return state;
	}
}
