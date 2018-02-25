package pizza_bot.pizza_bot;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import model.Model;
import model.ToolBox;
import view.View;

public class App {
	public static void main(String[] args) {

		JSONObject keys = null;
		Model model = Model.getInstance();
		
		try {
			keys = ToolBox.loadKeys();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		View view = new View(
				keys.get("bot_token").toString(), 
				keys.get("ibmUser").toString(),
				keys.get("ibmPassword").toString(), 
				keys.get("workspace").toString(), model);
		
		System.out.println("Bot pronto!");
		
		model.registerObserver(view);
		
		view.receiveUsersMessages();
	}
}
