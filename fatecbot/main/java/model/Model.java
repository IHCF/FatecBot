package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class Model implements Subject {

	private Map<Long, Student> studentsNoRegistry = new HashMap<Long, Student>();
	private List<Observer> observers = new LinkedList<Observer>();

	private boolean inRegister;
	private boolean inRegisterName;
	private boolean inRegisterPassword;
	private MongoLab mLab;

	private static Model uniqueInstance;

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void processMessage(Update update) {

		Long chatId = update.message().chat().id();
		Keyboard keyBoard = null;
		String message = null;

		if (update.message().text().equals("/start")) {
			message = "Olá, seja bem-vindo ao FatecBot, assistente simples e fácil de utilizar. Para que eu tenha acesso as informações digite:"
					+ "\n/registro";
			inRegister = true;
			studentsNoRegistry.put(chatId, new Student(chatId));
		}

		else if (update.message().text().equals("/registro") && inRegister) {
			inRegisterName = true;
			inRegisterPassword = true;

			message = "Insira seu nome de usuário do SIGA";
		}

		else if (inRegisterName) {
			studentsNoRegistry.get(chatId).setUserSiga(update.message().text());
			inRegisterName = false;

			message = "Insira sua senha do SIGA";
		}

		else if (inRegisterPassword) {
			studentsNoRegistry.get(chatId).setPassSiga(update.message().text());
			Student student = studentsNoRegistry.get(chatId);

			try {
				mLab = new MongoLab();
				mLab.addUser(student);

				inRegisterPassword = false;
				inRegister = false;

				message = "Cadastro feito com sucesso";
				
			} catch (Exception e) {
				inRegisterPassword = true;
				inRegister = true;
				notifyObserver(chatId, "Erro ao realizar o cadastro, tente novamente utilizando /registro", null);
			}

			// ToDo Verificar autenticidade das informações
			// Para isso será utilizado a API do Filipe
		}

		if (!inRegister) {
			message = "Escolha uma das opções";
			keyBoard = new ReplyKeyboardMarkup(new String[] { "Horario", "Calendario" },
					new String[] { "Faltas", "Baixar histórico escolar" }, new String[] { "Ajuda", "Sobre" });
		}

		notifyObserver(update.message().chat().id(), message, keyBoard);
	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObserver(long chatId, String message, Keyboard keyBoard) {
		for (Observer observer : observers) {
			observer.update(chatId, message, keyBoard);
		}
	}
}
