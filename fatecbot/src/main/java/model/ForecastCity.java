package model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cidade")
public class ForecastCity {

	private String name;
	private String uf;
	private String update;
	private List<Forecast> forecast;

	public String getName() {
		return name;
	}

	@XmlElement(name = "nome")
	public void setName(String name) {
		this.name = name;
	}

	public String getUf() {
		return uf;
	}

	@XmlElement(name = "uf")
	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getUpdate() {
		return update;
	}

	@XmlElement(name = "atualizacao")
	public void setUpdate(String update) {
		this.update = update;
	}

	public List<Forecast> getForecast() {
		return forecast;
	}

	@XmlElement(name = "previsao")
	public void setForecast(List<Forecast> forecast) {
		this.forecast = forecast;
	}

	public Forecast first() {
		return forecast.get(0);
	}

}
