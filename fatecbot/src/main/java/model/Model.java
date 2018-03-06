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
		boolean replyMessage = false; // Utilizado para que a mensagem seja exibida já no campo de reply
		String message = "";

		if (update.message().text().equals("/start")) {
			message = "Olá, seja bem-vindo ao *FatecBot*, assistente simples e fácil de utilizar, vou te ajudar com o SIGA. Para que eu tenha acesso as suas informações"
					+ " faça o registro utilizando o comando: \n/registro";
			inRegister = true;
			studentsNoRegistry.put(chatId, new Student(chatId));
		} else if (update.message().text().equals("/registro") && inRegister) {
			inRegisterName = true;
			inRegisterPassword = true;
			replyMessage = true;

			message = "Insira seu nome de *usuário* do SIGA";
		} else if (inRegisterName) {
			studentsNoRegistry.get(chatId).setUserSiga(update.message().text());
			inRegisterName = false;
			replyMessage = true;

			message = "Insira sua *senha* do SIGA";
		} else if (inRegisterPassword) {
			studentsNoRegistry.get(chatId).setPassSiga(update.message().text());
			Student student = studentsNoRegistry.get(chatId);

			try {
				mLab = new MongoLab();
				mLab.addUser(student);

				inRegisterPassword = false;
				inRegister = false;

				message = "Cadastro feito com sucesso. ";

			} catch (Exception e) {
				inRegisterPassword = true;
				inRegister = true;
				notifyObserver(chatId, "Erro ao realizar o cadastro, tente novamente utilizando /registro", null,
						false);
			}

			// ToDo Verificar autenticidade das informações
			// Para isso será utilizado a API do Filipe
		} else if (update.message().text().equals("Ajuda")) {
			message = "Acesse nossa wiki para obter mais informações:\n https://goo.gl/cjDgFM";
		} else if (update.message().text().equals("Sobre")) {
			message = "Chatbot criado como parte da matéria de Engenharia de software 3. Além disso foi criado pensando em facilitar a vida de todos os alunos da **Fatec**";
		}

		if (!inRegister && message.equals("Cadastro feito com sucesso. ")) {
			message += "Escolha uma das opções para se comunicar comigo";
			keyBoard = new ReplyKeyboardMarkup(new String[] { "Horário", "Calendario" },
					new String[] { "Faltas", "Baixar histórico escolar" }, new String[] { "Ajuda", "Sobre" });
		}

		notifyObserver(update.message().chat().id(), message, keyBoard, replyMessage);
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
