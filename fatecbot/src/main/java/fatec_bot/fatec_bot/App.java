package fatec_bot.fatec_bot;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import model.Model;
import model.ToolBox;
import view.View;

public class App {
	public static void main(String[] args) throws IOException, ParseException {

		JSONObject keys = null;
		Model model = Model.getInstance();

		try {
			keys = ToolBox.loadKeys();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (org.json.simple.parser.ParseException e) {
			e.printStackTrace();
		}

		View view = new View(keys.get("TELEGRAM_TOKEN").toString(), model);

		System.out.println("Bot inicializado");

		model.registerObserver(view);
		view.receiveUsersMessages();
	}
}
