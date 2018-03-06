package view;

import java.util.List;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ChatAction;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetUpdates;
import com.pengrad.telegrambot.request.SendChatAction;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.GetUpdatesResponse;

import controller.MessageController;
import model.Model;
import model.Observer;

public class View implements Observer {

	@SuppressWarnings("unused")
	private Model model;

	private TelegramBot bot;
	private Integer offSet = 0;
	private MessageController controller;
	private GetUpdatesResponse updatesResponse;

	public View(String botToken, Model model) {

		this.model = model;

		bot = new TelegramBot(botToken);
		controller = new MessageController(model, this);
	}

	public void receiveUsersMessages() {
		bot.setUpdatesListener(new UpdatesListener() {
			public int process(List<Update> arg0) {
				try {
					processConversation();
				} catch (Exception e) {
					System.out.println("Erro: " + e);
				}
				return UpdatesListener.CONFIRMED_UPDATES_ALL;
			}
		});
	}

	public void processConversation() {
		updatesResponse = bot.execute(new GetUpdates().limit(100).offset(offSet));
		List<Update> updates = updatesResponse.updates();

		for (Update update : updates) {
			controller.process(update);
			offSet = update.updateId() + 1;
		}
	}

	public void update(long chatId, String message, Keyboard keyBoard, boolean replyMessage) {

		if (replyMessage) {
			bot.execute(new SendMessage(chatId, message).replyMarkup(new ForceReply()).parseMode(ParseMode.Markdown));
		} else if (keyBoard == null) {
			bot.execute(new SendMessage(chatId, message).parseMode(ParseMode.Markdown));
		} else {
			bot.execute(new SendMessage(chatId, message).replyMarkup(keyBoard));
		}
	}

	public void sendTypingMessage(Update update) {
		bot.execute(new SendChatAction(update.message().chat().id(), ChatAction.typing.name()));
	}
}
