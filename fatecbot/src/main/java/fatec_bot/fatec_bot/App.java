package fatec_bot.fatec_bot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import com.google.gson.JsonObject;

import model.Model;
import model.ToolBox;
import view.View;

public class App {
	public static void main(String[] args) throws IOException, ParseException {

		JsonObject keys = null;
		Model model = Model.getInstance();

		try {
			keys = ToolBox.loadKeys();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		View view = new View(keys.get("TELEGRAM_TOKEN").toString().replaceAll("\"", ""), model);

		System.out.println("Bot inicializado");

		model.registerObserver(view);
		view.receiveUsersMessages();
	}
}
