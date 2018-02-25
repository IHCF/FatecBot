package model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;

import com.ibm.watson.developer_cloud.conversation.v1.Conversation;
import com.ibm.watson.developer_cloud.conversation.v1.model.InputData;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.pengrad.telegrambot.model.Update;

public class Model implements Subject {

	private List<Observer> observers = new LinkedList<Observer>();

	private static Model uniqueInstance;

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void processMessage(Update update, Conversation service, String workspaceId) {

		InputData input = new InputData.Builder(update.message().text()).build();
		MessageOptions options = new MessageOptions.Builder(workspaceId).input(input).build();

		MessageResponse response = service.message(options).execute();

		JSONObject obj = new JSONObject(response);
		Map mapText = (Map) obj.get("output");

		notifyObserver(update.message().chat().id(),
				new JSONObject(mapText).get("text").toString().replace("[", "").replace("]", ""));
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObserver(long chatId, String message) {
		for (Observer observer : observers) {
			observer.update(chatId, message);
		}
	}
}
