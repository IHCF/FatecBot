package model;

import java.io.IOException;

import com.google.gson.JsonArray;


public interface NetworkOperations {

	// Será inserido o fatec-api
	String URL_ADDRESS = "https://wt-18cbb6605ed10ee8879df055d9e341f3-0.run.webtask.io/fatec-eng-iii";

	JsonArray sendGet() throws IOException;

	JsonArray sendPost(String login, String password) throws IOException;
}
