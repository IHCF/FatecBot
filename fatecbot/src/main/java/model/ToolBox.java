package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ToolBox {

	public static String loadTelegramKey() throws FileNotFoundException, IOException {

		Properties prop = new Properties();
		InputStream input = new FileInputStream("resources/telegram.properties");

		prop.load(input);

		return prop.getProperty("TELEGRAM_TOKEN");
	}

	public static String[] loadDataBaseConfigures() throws FileNotFoundException, IOException {

		Properties prop = new Properties();
		InputStream input = new FileInputStream("resources/database.properties");

		prop.load(input);

		return new String[] { prop.getProperty("URL") + prop.getProperty("DATABASE_NAME"), prop.getProperty("USER"),
				prop.getProperty("PASSWORD") };
	}
}
