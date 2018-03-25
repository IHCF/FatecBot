package model;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

public class Model implements Subject {

	private ConnectAPI api = new ConnectAPI();
	private List<Observer> observers = new LinkedList<Observer>();

	private static Model uniqueInstance;

	// Adicionado map para simular base de dados (Temporário)
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
		notifyObserver(chatId, "Usuário cadastrado com sucesso. Utilize os botões para se comunicar comigo", true,
				false);
	}

	public String[] recoveryUser(Long chatId, boolean login) {

		String[] userInfo = usersConnected.get(chatId);

		// Caso seja login e algum usuário tenha sido encontrado
		if (userInfo != null && login) {
			notifyObserver(chatId, "Usuário encontrado! Agora basta utilizar os comandos, que coleto os dados do SIGA",
					true, false);

			return userInfo;
			// Caso seja login e nenhum usuário tenha sido encontrado
		} else if (login) {
			notifyObserver(chatId, "Não encontrei nenhum registro seu. Utilize o /registro para fazer o cadastro",
					false, false);
			return null;
			// Caso a busca de usuário não seja no login, e algum usuário foi encontrado
		} else if (userInfo != null && !login == true) {
			return userInfo;
		}

		return null;
	}

	public void getAbsenses(Long chatId) {
		String[] userInfo = recoveryUser(chatId, false);

		if (userInfo != null) {
			StringBuilder absensesBuilder = new StringBuilder();
			absensesBuilder.append("Suas faltas: \n");
			try {
				// Recupera as informações da API
				JsonArray absenses = api.sendPost(userInfo[0], userInfo[1]);

				for (JsonElement element : absenses) {
					absensesBuilder.append("Matéria: " + element.getAsJsonObject().get("name") + "\n");
					absensesBuilder.append("Professor(a): " + element.getAsJsonObject().get("teacherName") + "\n");
					absensesBuilder.append("Quantidade de faltas: " + element.getAsJsonObject().get("absenses") + "\n");
					absensesBuilder.append("- - - - - - - - - - - -\n");
				}

			} catch (IOException e) {
				// Caso a API não consiga entregar informações do usuário
				notifyObserver(chatId,
						"Acho que não consegui recuperar suas informações =(. Se precisar utilize /registrar novamente!",
						false, false);
			}

			notifyObserver(chatId, absensesBuilder.toString(), true, false);

		} else {
			notifyObserver(chatId, "Seus dados ainda não fora registrados, utilize /registrar para fazer o cadastro",
					false, false);
		}
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObserver(long chatId, String message, boolean keyBoard, boolean replyMessage) {
		for (Observer observer : observers) {
			observer.update(chatId, message, keyBoard, replyMessage);
		}
	}
}
