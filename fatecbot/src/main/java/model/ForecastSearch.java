package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.web.util.UriComponentsBuilder;

@XmlRootElement(name = "cidades")
public class ForecastSearch implements Information {

	private List<ForecastCity> listCity;
	private BufferedReader parse;
	private final static String USER_AGENT = "Mozilla/50.0";

	public List<ForecastCity> getCidade() {
		return listCity;
	}

	@XmlElement
	public void setCidade(List<ForecastCity> listCity) {
		this.listCity = listCity;
	}

	private URL builtUrlCidade() throws MalformedURLException {
		return new URL(UriComponentsBuilder.newInstance().scheme(PROTOCOL).host(INPE_API_FORECAST)
				.path(INPE_API_FORECAST_PATH).toUriString());
	}

	public ForecastCity getForecast() throws MalformedURLException, JAXBException, IOException {

		// Cria a URL que será utilizada
		URL url = builtUrlCidade();
		JAXBContext jaxbContext;
		Unmarshaller unmarshaller;
		parse = null;
		StringBuffer responseBuffer = new StringBuffer();

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);

		parse = new BufferedReader(new InputStreamReader(con.getInputStream()));

		String inputLine;
		while ((inputLine = parse.readLine()) != null) {
			responseBuffer.append(inputLine);
		}

		jaxbContext = JAXBContext.newInstance(ForecastSearch.class);
		unmarshaller = jaxbContext.createUnmarshaller();
		StringReader reader = new StringReader(responseBuffer.toString());
		ForecastCity city = (ForecastCity) unmarshaller.unmarshal(reader);

		return city;
	}
}
