package fatec_bot.fatec_bot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import model.Model;
import model.Report;
import model.ToolBox;
import view.View;

public class App {
	public static void main(String[] args) throws IOException, ParseException {

		String telegramKey = null;
		Model model = Model.getInstance();
		
		try {
			telegramKey = ToolBox.loadTelegramKey();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		View view = new View(telegramKey, model);

		System.out.println("Bot inicializado");

		model.registerObserver(view);
		view.receiveUsersMessages();
	}
}
