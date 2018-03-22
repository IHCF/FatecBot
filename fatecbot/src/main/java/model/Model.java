package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.pengrad.telegrambot.model.request.Keyboard;

public class Model implements Subject {

	private ConnectAPI api = new ConnectAPI();
	private List<Observer> observers = new LinkedList<Observer>();

	private static Model uniqueInstance;

	// Adicionado map para simular base de dados
	Map<Long, String[]> usersConnected = new HashMap<Long, String[]>();

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void addUser(Long chatId, String name, String password) {
		// ToDo: Adicionar forma de acesso ao SQLite
		usersConnected.put(chatId, new String[] { name, password });
		notifyObserver(chatId, "Usuário cadastrado com sucesso", null, false);
	}

	public String[] recoveryUser(Long chatId) {
		// ToDo: Adicionar forma de acesso ao MySQL
		return usersConnected.get(chatId);
	}

	public void getAbsenses(Long chatId) {
		String[] userInfo = recoveryUser(chatId);

		if (userInfo.length != 0) {
			StringBuilder absensesBuilder = new StringBuilder();
			absensesBuilder.append("Suas faltas: \n");
			try {
				JsonArray absenses = api.sendPost(userInfo[0], userInfo[1]);

				for (JsonElement element : absenses) {
					absensesBuilder.append("Matéria: " + element.getAsJsonObject().get("name") + "\n");
					absensesBuilder.append("Professor(a): " + element.getAsJsonObject().get("teacherName") + "\n");
					absensesBuilder.append("Quantidade de faltas: " + element.getAsJsonObject().get("absenses") + "\n");
					absensesBuilder.append("- - - - - - - - - - - -\n");
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

			notifyObserver(chatId, absensesBuilder.toString(), null, false);

		} else {
			notifyObserver(chatId, "Seus dados ainda não fora registrados, utilize /registrar para fazer o cadastro",
					null, false);
		}
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObserver(long chatId, String message, Keyboard keyBoard, boolean replyMessage) {
		for (Observer observer : observers) {
			observer.update(chatId, message, keyBoard, replyMessage);
		}
	}
}
