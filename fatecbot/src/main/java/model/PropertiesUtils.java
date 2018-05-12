package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

	public static String loadTelegramKey() throws FileNotFoundException, IOException {

		Properties prop = new Properties();
		InputStream input = new FileInputStream("src/main/resources/telegram.properties");

		prop.load(input);

		return prop.getProperty("TELEGRAM_TOKEN");
	}

	public static String loadAdminKey() throws IOException {

		Properties prop = new Properties();
		InputStream input = new FileInputStream("src/main/resources/telegram.properties");

		prop.load(input);

		return prop.getProperty("ADMIN_ID");
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

	public static String[] loadForecastApiInfos() throws IOException {

		String[] props = new String[2];
		Properties prop = new Properties();
		InputStream input = new FileInputStream("src/main/resources/api.properties");

		prop.load(input);

		props[0] = prop.getProperty("INPE_API_FORECAST").replace("%22", "");
		props[1] = prop.getProperty("INPE_API_FORECAST_PATH").replace("%22", "");

		return props;
	}

}
