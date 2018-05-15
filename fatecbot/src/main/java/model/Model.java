package model;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import view.Observer;

public class Model implements Subject {

	private ConnectAPI api = new ConnectAPI();
	private ModelDAO database = new DatabaseFourObject();
	private ForecastSearch fSearch = new ForecastSearch();
	private List<Observer> observers = new LinkedList<Observer>();

	private static Model uniqueInstance;

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

		boolean created;
		try {
			created = database.createStudent(new Student(chatId, nick, sigaId, password));
			if (created)
				message = "Usuário cadastrado com sucesso. Utilize os botões para se comunicar comigo";
			else
				message = "Ops! Acho que você já está cadastrado! Utilize o /recuperar, assim consigo recuperar suas informações e continuar te ajudando.";
			keyboard = true;
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		notifyObserver(chatId, message, keyboard, false, false);
	}

	public Student recoveryUser(Long chatId, boolean login) {

		try {
			Student student = database.selectStudent(chatId);

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
			database.deleteStudent(chatId);
		} catch (Exception e) {
			notifyObserver(chatId, "Eita! Tipo um problema ao tentar remover seu usuário. Tente novamente mais tarde",
					false, false, false);
		}
		notifyObserver(chatId, "Usuário revogado com sucesso! \uD83D\uDD35", false, false, false);
	}

	public void getAbsenses(Long chatId) {
		Student student = recoveryUser(chatId, false);

		if (student != null) {

			try {
				// Cria tipo para deserializar o json em um List
				Type listType = new TypeToken<ArrayList<Discipline>>() {
				}.getType();

				// Realiza a recuperação dos dados
				JsonElement element = api.sendPost(student.getSigaId(), student.getPasswordSiga());

				// Transforma o JSON em List<Discipline>
				List<Discipline> disciplines = new Gson()
						.fromJson(element.getAsJsonObject().get("disciplines").getAsJsonArray(), listType);

				StringBuilder absensesBuilder = new StringBuilder();
				absensesBuilder.append("Suas faltas: \n");
				for (Discipline discipline : disciplines) {
					absensesBuilder.append("Matéria: " + discipline.getName() + "\n");
					absensesBuilder.append("Professor(a): " + discipline.getTeacherName() + "\n");
					absensesBuilder.append("Quantidade de faltas: " + discipline.getAbsenses() + "\n");
					absensesBuilder.append("- - - - - - - - - - - -\n");
				}

				notifyObserver(chatId, absensesBuilder.toString(), true, false, false);
			} catch (Exception e) {
				notifyObserver(chatId, "Eita, tive alguns problemas para acessar seus dados. Tente mais tarde", true,
						false, false);
			}
		}
	}

	public void generateHistory(Long chatId) {
		Student student = recoveryUser(chatId, false);

		if (student != null) {

			// Cria tipo para deserializar o json em um List
			Type listType = new TypeToken<ArrayList<Entry>>() {
			}.getType();

			// Realiza a recuperação dos dados
			JsonElement element;
			try {
				element = api.sendPost(student.getSigaId(), student.getPasswordSiga());

				// Transforma o JSON em List<Discipline>
				List<Entry> history = new Gson().fromJson(
						element.getAsJsonObject().get("history").getAsJsonObject().get("entries").getAsJsonArray(),
						listType);

				StringBuilder stringBuilder = new StringBuilder();
				for (Entry entry : history) {
					Discipline discipline = entry.getDiscipline();

					stringBuilder.append(discipline.getCode() + " &");
					stringBuilder.append(discipline.getName() + " &");
					stringBuilder.append(discipline.getPeriod() + " &");
					stringBuilder.append(discipline.getState() + " &");
					stringBuilder.append(discipline.getGrade() + " &");
					stringBuilder.append(discipline.getFrequency() + "\\% &");
					stringBuilder.append(discipline.getAbsenses() + " &");
					stringBuilder.append(entry.getObservation() + " \\\\");
				}

				notifyObserver(chatId, Report.generateReport(stringBuilder.toString()), false, false, true);
			} catch (IOException e) {
				notifyObserver(chatId, "Não consegui recuperar as informações =(. Tente mais tarde", false, false,
						false);
			}

		} else {
			notifyObserver(chatId, "Seus dados ainda não fora registrados, utilize /registro para fazer o cadastro",
					false, false, false);
		}
	}

	public void getSchedules(Long chatId) {
		Student student = recoveryUser(chatId, false);

		if (student != null) {

			try {
				JsonElement element = api.sendPost(student.getSigaId(), student.getPasswordSiga());

				// Cria tipo para deserializar o json em List
				Type listType = new TypeToken<ArrayList<Schedule>>() {
				}.getType();

				// Transforma o json em objeto
				List<Schedule> schedules = new Gson()
						.fromJson(element.getAsJsonObject().get("schedules").getAsJsonArray(), listType);

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
			} catch (Exception e) {
				notifyObserver(chatId, "Opa! Não consegui realizar a operação. Tente mais tarde.", false, false, false);
			}

		} else {
			notifyObserver(chatId, "Seus dados ainda não fora registrados, utilize /registro para fazer o cadastro",
					false, false, false);
		}
	}

	public void wantAbsence(Long chatId) {

		Student student = recoveryUser(chatId, false);

		if (student != null) {
			try {
				// Cria tipo para deserializar o json em um List
				Type listSchedule = new TypeToken<List<Schedule>>() {
				}.getType();
				Type listDisciplines = new TypeToken<ArrayList<Discipline>>() {
				}.getType();

				// Realiza a recuperação dos dados
				JsonElement element = api.sendPost(student.getSigaId(), student.getPasswordSiga());

				// Transforma o JSON em List<Entry>
				List<Schedule> schedules = new Gson()
						.fromJson(element.getAsJsonObject().get("schedules").getAsJsonArray(), listSchedule);
				List<Discipline> disciplines = new Gson()
						.fromJson(element.getAsJsonObject().get("disciplines").getAsJsonArray(), listDisciplines);

				// Recupera o dia atual
				int dayOfWeek = ModelUtils.getDayWeek() - 2;
				List<Period> todayClasses = schedules.get(dayOfWeek).getPeriods();

				// Recupera a temperatura atual
				Forecast forecastToday = fSearch.getForecast().first();

				boolean isAbsense = true; // Flag para indicar que o aluno pode faltar
				// Verificando as faltas das matérias em que o aluno está matriculado
				// Caso ele tenha mais que seis faltas em qualque matéria, o bot recomenda ele
				// não faltar, como um incentivo!
				Discipline discipline = null;
				for (Period period : todayClasses) {
					for (Discipline d : disciplines) {
						if (d.getCode().equals(period.getDiscipline().getCode())) {
							discipline = d;
							break;
						}
					}
					if (discipline.getAbsenses() >= 6) {
						isAbsense = false;
						break;
					}
				}

				StringBuilder response = new StringBuilder();
				if (!isAbsense) {
					if (forecastToday.isCold()) {
						response.append("Mesmo frio (");
					} else if (forecastToday.isHot()) {
						response.append("Mesmo calor (");
					} else if (forecastToday.isMild()) {
						response.append("Mesmo com o clima ameno, bom para ficar sem fazer nada (");
					}
					response.append(forecastToday.getMinimum());
					response.append("°), você precisa ir para a faculdade, está com muitas faltas\n");
					response.append("A matéria em que você está com problemas é: ");
					response.append(discipline.getName());
					response.append(" (" + discipline.getAbsenses() + "faltas )");
				} else {
					if (forecastToday.isCold()) {
						response.append("Está frio (");
					} else if (forecastToday.isHot()) {
						response.append("Está calor (");
					} else if (forecastToday.isMild()) {
						response.append("O clima está ameno, (");
					}
					response.append(forecastToday.getMaximum());
					response.append("°), nem vai para faculdade, suas faltas estão tranquilas");
				}
				notifyObserver(chatId, response.toString(), false, false, false);
			} catch (Exception e) {
				e.printStackTrace();
				notifyObserver(chatId, "Acho que hoje não é dia de se fazer esta pergunta", false, false, false);
			}
		} else {
			notifyObserver(chatId, "Seus dados ainda não fora registrados, utilize /registro para fazer o cadastro",
					false, false, false);
		}

	}

	public void setDatabase(ModelDAO database) {
		this.database = database;
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
