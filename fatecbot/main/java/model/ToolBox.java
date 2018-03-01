package model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ToolBox {
	public static JSONObject loadKeys() throws FileNotFoundException, IOException, ParseException {
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(new FileReader("config/keys.json"));
	}
}
