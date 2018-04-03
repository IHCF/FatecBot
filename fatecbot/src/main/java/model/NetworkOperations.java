package model;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


public interface NetworkOperations {

	public String URL_ADDRESS = "https://wt-18cbb6605ed10ee8879df055d9e341f3-0.run.webtask.io/fatec-eng-iii";
	public String USER_AGENT = "Mozilla/50.0";
	
	JsonArray sendGet() throws IOException;

	JsonElement sendPost(String login, String password) throws IOException;
}
