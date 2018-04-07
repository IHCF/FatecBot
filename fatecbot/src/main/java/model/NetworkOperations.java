package model;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


public interface NetworkOperations {

	public String USER_AGENT = "Mozilla/50.0";
	
	JsonArray sendGet() throws IOException;

	JsonElement sendPost(String login, String password) throws IOException;
}
