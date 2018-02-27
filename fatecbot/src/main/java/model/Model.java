package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;

public class Model implements Subject {

	private List<Observer> observers = new LinkedList<Observer>();
	private Map<Long, Student> studentsNoRegistry = new HashMap<Long, Student>();
	private boolean inRegister;
	private boolean inRegisterName;
	private boolean inRegisterPassword;

	private static Model uniqueInstance;

	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void processMessage(Update update) {
		// ToDO Inserir usuários no banco
		// ToDo Fazer verificação da mensagem de boas-vindas

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
			message = "Insira seu nome de usuário do SIGA";
			inRegisterName = true;
			inRegisterPassword = true;
		}

		else if (inRegisterName) {
			studentsNoRegistry.get(chatId).setUserSiga(update.message().text());
			inRegisterName = false;
			
			message = "Insira sua senha do SIGA";
		}

		else if (inRegisterPassword) {
			studentsNoRegistry.get(chatId).setUserSiga(update.message().text());
			inRegisterPassword = false;
			inRegister = false;
			
			// ToDo Verificar autenticidade das informações
			message = "Cadastro efeituado com sucesso!";
		}

		else if (!inRegister) {
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
