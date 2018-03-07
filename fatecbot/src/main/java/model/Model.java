package model;

import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import com.pengrad.telegrambot.model.request.Keyboard;

public class Model implements Subject {

	private List<Observer> observers = new LinkedList<Observer>();

	private MongoLab mLab;

	private static Model uniqueInstance;

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void addUser(Long chatId, String name, String password) {
		// Antes de adicionar verificar conectividade
		try {
			mLab = new MongoLab();
			mLab.addUser(chatId, name, password);
		} catch (Exception e) {
			notifyObserver(chatId, "Falha ao tentar acessar a base de dados", null, false);
		}
	}

	public void recoveryUser(Long chatId) {
		try {
			mLab = new MongoLab();
			Document studentTemp = mLab.getUser(chatId);

			if (!studentTemp.get("SIGA_USER").equals("")) {
				// Temporário até que a API fique pronta =D
				notifyObserver(chatId, "Seja bem-vindo novamente " + studentTemp.getString("SIGA_USER"), null, false);
			}

		} catch (Exception e) {
			notifyObserver(chatId, "Falha ao tentar acessar a base de dados", null, false);
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
