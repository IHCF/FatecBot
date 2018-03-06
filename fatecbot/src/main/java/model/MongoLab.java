package model;

import java.io.IOException;

import org.bson.Document;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoLab {

	private MongoClient mongoClient;
	private JSONObject keys;

	public MongoLab() throws IOException, ParseException {
		keys = ToolBox.loadKeys();
	}

	private MongoCollection<Document> connect() {

		mongoClient = new MongoClient(
				new MongoClientURI(String.format("mongodb://%s:%s@ds251518.mlab.com:51518/fatecbot-db",
						keys.get("MLAB_USER"), keys.get("MLAB_PASSWORD"))));

		MongoDatabase db = mongoClient.getDatabase("fatecbot-db");

		return db.getCollection("users");
	}

	public void addUser(Student student) throws MongoException {

		Document document = new Document("CHATID", student.getChatId()).append("SIGA_USER", student.getUserSiga())
				.append("SIGA_PASS", student.getPassSiga());

		// A cada nova requisição será feita a conexão com o banco de dados. Isso evita
		// problemas com segurança
		MongoCollection<Document> users = connect();

		users.insertOne(document);
		mongoClient.close();
	}

	public Document getUser(Long chatId) throws MongoException {

		MongoCollection<Document> users = connect();

		FindIterable<Document> result = users.find(new Document("CHATID", chatId));

		Document document = null;
		for (Document docs : result) {
			document = docs;
		}

		mongoClient.close();
		return document;
	}

	public boolean removeUser(Long chatId) throws MongoException {
		MongoCollection<Document> users = connect();

		users.findOneAndDelete(new Document("CHATID", chatId));

		return true;
	}
}
