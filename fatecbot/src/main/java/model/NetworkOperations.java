package model;

import java.io.IOException;
import java.util.List;

import org.json.simple.JSONObject;

public interface NetworkOperations {
	
	// Será inserido o fatec-api
	String URL = "";
	
	JSONObject sendGet() throws IOException;

	int sendPost(List<String> params);
}
