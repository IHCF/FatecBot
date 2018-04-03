package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectAPI implements NetworkOperations {

	public JsonArray sendGet() throws IOException {
		URL urlObj = new URL(URL_ADDRESS);
		HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = conn.getResponseCode();

		if (responseCode == HttpsURLConnection.HTTP_OK) {
			BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));

			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = input.readLine()) != null) {
				response.append(inputLine);
			}

			input.close();

			return new JsonParser().parse(response.toString()).getAsJsonArray();
		}

		return null;
	}

	public JsonElement sendPost(String login, String password) throws IOException {
		final MediaType jsonMediaType = MediaType.parse("application/json");

		try {
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("login", login);
			jsonObject.addProperty("password", password);

			RequestBody requestBody = RequestBody.create(jsonMediaType, new Gson().toJson(jsonObject));

			OkHttpClient client = new OkHttpClient();

			Request request = new Request.Builder().url(URL_ADDRESS).post(requestBody)
					.addHeader("content-type", "application/json").build();

			Response response = client.newCall(request).execute();

			String res = response.body().string();

			return new JsonParser().parse(res);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
