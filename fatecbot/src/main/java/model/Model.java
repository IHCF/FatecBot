package model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import view.Observer;

public class Model implements Subject {

	private ConnectAPI api = new ConnectAPI();
	private ForecastSearch fSearch = new ForecastSearch();
	private List<Observer> observers = new LinkedList<Observer>();

	private static Model uniqueInstance;

	// Cria HashMap com os dias da Semana
	// private static final Map<Integer, String> WEEK_DAY;
	// static {
	// WEEK_DAY = new HashMap<Integer, String>();
	// WEEK_DAY.put(1, "Segunda-feira");
	// WEEK_DAY.put(2, "Terça-feira");
	// WEEK_DAY.put(3, "Quarta-feira");
	// WEEK_DAY.put(4, "Quinta-feira");
	// WEEK_DAY.put(5, "Sexta-feira");
	// }

	// Cria HashMap com os estados da disciplina
	private static final Map<String, String> DISCIPLINE_STATE;
	static {
		DISCIPLINE_STATE = new HashMap<String, String>();
		DISCIPLINE_STATE.put("approved", "Aprovado");
		DISCIPLINE_STATE.put("attending", "Em Curso");
		DISCIPLINE_STATE.put("not-attended", "Não cursado");
		DISCIPLINE_STATE.put("dismissed", "Dispensado");
	}

	// Tornando a classe Singleton.
	public static Model getInstance() {
		if (uniqueInstance == null) {
			uniqueInstance = new Model();
		}
		return uniqueInstance;
	}

	public void addUser(Long chatId, String nick, String sigaId, String password) {

		String message = "";
		boolean keyboard = false;
		try {
			ModelDAO.createStudent(chatId, nick, sigaId, password);
			message = "Usuário cadastrado com sucesso. Utilize os botões para se comunicar comigo";
			keyboard = true;
		} catch (Exception e) {
			message = "Ops! Acho que você já está cadastrado! Utilize o /recuperar, assim consigo recuperar suas informações e continuar te ajudando.";
		}

		notifyObserver(chatId, message, keyboard, false, false);
	}

	public Student recoveryUser(Long chatId, boolean login) {

		try {
			Student student = ModelDAO.selectStudent(chatId);

			// Caso seja login e algum usuário tenha sido encontrado
			if (student != null && login) {
				notifyObserver(chatId,
						"Usuário encontrado! Agora basta utilizar os comandos, que coleto os dados do SIGA", true,
						false, false);

				return student;
				// Caso seja login e nenhum usuário tenha sido encontrado
			} else if (login) {
				notifyObserver(chatId, "Não encontrei nenhum registro seu. Utilize o /registro para fazer o cadastro",
						false, false, false);
				return null;
				// Caso a busca de usuário não seja no login, e algum usuário foi encontrado
			} else if (student != null && !login == true) {
				return student;
			}
		} catch (Exception e) {
			notifyObserver(chatId, "Erro ao tentar recuperar os dados", false, false, false);
		}

		return null;
	}

	public void removeUser(Long chatId) {
		try {
			ModelDAO.deleteStudent(chatId);
		} catch (Exception e) {
			notifyObserver(chatId, "Eita! Tipo um problema ao tentar remover seu usuário. Tente novamente mais tarde",
					false, false, false);
		}
		notifyObserver(chatId, "Usuário revogado com sucesso! \uD83D\uDD35", false, false, false);
	}

	public void getAbsenses(Long chatId) {
		Student student = recoveryUser(chatId, false);

		if (student != null) {
			StringBuilder absensesBuilder = new StringBuilder();
			absensesBuilder.append("Suas faltas: \n");
			try {
				// Recupera as informações da API
				JsonElement absenses = api.sendPost(student.getSigaId(), student.getPasswordSiga());

				for (JsonElement element : absenses.getAsJsonObject().get("disciplines").getAsJsonArray()) {
					absensesBuilder.append("Matéria: " + element.getAsJsonObject().get("name") + "\n");
					absensesBuilder.append("Professor(a): " + element.getAsJsonObject().get("teacherName") + "\n");
					absensesBuilder.append("Quantidade de faltas: " + element.getAsJsonObject().get("absenses") + "\n");
					absensesBuilder.append("- - - - - - - - - - - -\n");
				}

			} catch (IOException e) {
				// Caso a API não consiga entregar informações do usuário
				notifyObserver(chatId,
						"Acho que não consegui recuperar suas informações =(. Se precisar utilize /registrar novamente!",
						false, false, false);
			}

			notifyObserver(chatId, absensesBuilder.toString(), true, false, false);

		} else {
			notifyObserver(chatId, "Seus dados ainda não fora registrados, utilize /registrar para fazer o cadastro",
					false, false, false);
		}
	}

	public void generateHistory(Long chatId) {
		Student student = recoveryUser(chatId, false);

		if (student != null) {
			StringBuilder stringBuilder = new StringBuilder();
			try {
				// Recupera as informações da API
				JsonElement response = api.sendPost(student.getSigaId(), student.getPasswordSiga());
				JsonElement history = response.getAsJsonObject().get("history");

				for (JsonElement entry : history.getAsJsonObject().get("entries").getAsJsonArray()) {
					JsonElement discipline = entry.getAsJsonObject().get("discipline");
					stringBuilder.append(discipline.getAsJsonObject().get("code").getAsString() + " &");
					stringBuilder.append(discipline.getAsJsonObject().get("name").getAsString() + " &");
					stringBuilder.append(discipline.getAsJsonObject().get("period").getAsString() + " &");
					stringBuilder.append(
							DISCIPLINE_STATE.get(discipline.getAsJsonObject().get("state").getAsString()) + " &");
					stringBuilder.append(discipline.getAsJsonObject().get("grade").getAsString() + " &");
					stringBuilder.append(discipline.getAsJsonObject().get("frequency").getAsString() + "\\% &");
					stringBuilder.append(discipline.getAsJsonObject().get("absenses").getAsString() + " &");
					stringBuilder.append(entry.getAsJsonObject().get("observation").getAsString() + " \\\\");
				}
				notifyObserver(chatId, Report.generateReport(stringBuilder.toString()), false, false, true);

			} catch (IOException e) {
				notifyObserver(chatId, "Não consegui recuperar as informações =(", false, false, false);
			}

		} else {
			notifyObserver(chatId, "Seus dados ainda não fora registrados, utilize /registrar para fazer o cadastro",
					false, false, false);
		}
	}

	public void getSchedules(Long chatId) throws IOException {
		Student student = recoveryUser(chatId, false);

		if (student != null) {
			JsonElement element = api.sendPost(student.getSigaId(), student.getPasswordSiga());

			// Cria tipo para serializar o json em um List
			Type listType = new TypeToken<ArrayList<Schedule>>() {
			}.getType();

			// Transforma o json em objeto
			List<Schedule> schedules = new Gson().fromJson(element.getAsJsonObject().get("schedules").getAsJsonArray(),
					listType);

			StringBuilder schedulesBuilder = new StringBuilder();
			schedulesBuilder.append("Suas aulas\n");

			for (Schedule schedule : schedules) {
				schedulesBuilder.append("Dia da semana: " + schedule.getWeekday() + "\n");
				for (Period period : schedule.getPeriods()) {

					// Tratando String para que seja exibido apenas as horas
					String startAt = period.getStartAt().split("T")[1];
					startAt = startAt.substring(0, startAt.length() - 5);

					String endAt = period.getEndAt().split("T")[1];
					endAt = endAt.substring(0, endAt.length() - 5);

					schedulesBuilder.append("Matéria: " + period.getDiscipline().getCode() + "\n");
					schedulesBuilder.append("Horário de inicio: " + startAt + "\n");
					schedulesBuilder.append("Horário de fim: " + endAt + "\n\n");
				}
				schedulesBuilder.append("- - - - - - - - - - - -\n");
			}

			notifyObserver(chatId, schedulesBuilder.toString(), false, false, false);

		} else {
			notifyObserver(chatId, "Seus dados ainda não fora registrados, utilize /registrar para fazer o cadastro",
					false, false, false);
		}
	}

	public void wantAbsence(Long chatId) {

	}

	public void registerObserver(Observer observer) {
		observers.add(observer);
	}

	public void notifyObserver(long chatId, Object message, boolean keyBoard, boolean replyMessage,
			boolean isDocument) {
		for (Observer observer : observers) {
			observer.update(chatId, message, keyBoard, replyMessage, isDocument);
		}
	}
}
