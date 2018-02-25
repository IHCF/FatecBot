package controller;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
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

	public void process(Update update, Conversation service, String workspaceId) {
		view.sendTypingMessage(update);
		model.processMessage(update, service, workspaceId);
	}
}
