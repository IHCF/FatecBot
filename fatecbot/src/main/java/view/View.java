package view;

import java.io.File;
import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.request.SendMessage;

import controller.AbsenceController;
import controller.AuthController;
import controller.DatabaseController;
import controller.DeleteController;
import controller.HistoryController;
import controller.ProcessController;
import controller.SchedulesController;
import controller.WantAbsenceController;
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
					e.printStackTrace();
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
							false, false, false);
					setController(new AuthController(model, this));
				}

				else if (update.message().text().equals("/registro")) {
					state = State.IS_REGISTERING.getState();
					update(update.message().chat().id(), "Insira seu nome de usuário do SIGA", false, true, false);
					setController(new AuthController(model, this));
				}

				else if (update.message().text().equals("/recuperar")) {
					state = State.IS_RECOVERY_USER.getState();
					setController(new AuthController(model, this));
				}

				else if (update.message().text().equals("Ver faltas 🔥")) {
					setController(new AbsenceController(model, this));
				}

				else if (update.message().text().equals("Ver horário 🕐")) {
					setController(new SchedulesController(model, this));
				}

				else if (update.message().text().equals("Configurações 🔧")) {
					update(update.message().chat().id(),
							"As opções de configuração disponíveis são as seguintes: \n\n /remove - Comando para revogar acesso do bot aos dados do SIGA",
							false, false, false);
				}

				else if (update.message().text().equals("/remove")) {
					setController(new DeleteController(model, this));
				}

				else if (update.message().text().contains("/change_databases_")) {
					System.out.println("Troca BD");
					setController(new DatabaseController(model, this));
				}

				else if (update.message().text().equals("Posso faltar ? 📊")) {
					setController(new WantAbsenceController(model, this));
				}

				else if (update.message().text().equals("Gerar relatório escolar 📝")) {
					setController(new HistoryController(model, this));
				}

			} else if (state == State.IS_REGISTERING.getState()) {
				state = State.IS_REGISTERING_USERNAME.getState();
				update(update.message().chat().id(), "Insira sua senha do SIGA", false, true, false);
			} else if (state == State.IS_REGISTERING_USERNAME.getState()) {
				state = State.IS_REGISTERING_PASSWORD.getState();
			}
			processController.process(update);
		}

	}

	/**
	 * Método que recebe informações que devem ser pasadas ao usuário.
	 */
	public void update(long chatId, Object message, boolean keyBoard, boolean replyMessage, boolean isDocument) {

		if (replyMessage) {
			bot.execute(new SendMessage(chatId, (String) message).replyMarkup(new ForceReply()));
		} else if (keyBoard) {
			bot.execute(new SendMessage(chatId, (String) message).replyMarkup(new ReplyKeyboardMarkup(
					new KeyboardButton[] { new KeyboardButton("Ver faltas \uD83D\uDD25"),
							new KeyboardButton("Ver horário \uD83D\uDD50") },
					new KeyboardButton[] { new KeyboardButton("Posso faltar ? \uD83D\uDCCA"),
							new KeyboardButton("Gerar relatório escolar \uD83D\uDCDD") },
					new KeyboardButton[] { new KeyboardButton("Configurações \uD83D\uDD27") })));
		} else if (isDocument) {
			bot.execute(new SendDocument(chatId, (File) message));
		} else {
			bot.execute(new SendMessage(chatId, (String) message));
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
