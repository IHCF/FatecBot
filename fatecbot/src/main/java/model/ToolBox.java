package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class ToolBox {

	public static String loadTelegramKey() throws FileNotFoundException, IOException {

		Properties prop = new Properties();
		InputStream input = new FileInputStream("src/main/resources/telegram.properties");

		prop.load(input);

		return prop.getProperty("TELEGRAM_TOKEN");
	}

	public static String[] loadDataBaseConfigures() throws FileNotFoundException, IOException {

		Properties prop = new Properties();
		InputStream input = new FileInputStream("src/main/resources/database.properties");

		prop.load(input);

		return new String[] { prop.getProperty("URL") + prop.getProperty("DATABASE_NAME"), prop.getProperty("USER"),
				prop.getProperty("PASSWORD") };
	}

	public static String loadApiKey() throws FileNotFoundException, IOException {

		Properties prop = new Properties();
		InputStream input = new FileInputStream("src/main/resources/api.properties");

		prop.load(input);

		return prop.getProperty("URL_ADDRESS");
	}

	public static int getDayWeek() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		
		return c.get(Calendar.DAY_OF_WEEK);
	}
}
