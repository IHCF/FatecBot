package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "previsao")
public class Forecast {

	private String dia;
	private String tempo;
	private int maxima;
	private int minima;

	public String getDia() {
		return dia;
	}

	@XmlElement
	public void setDia(String dia) {
		this.dia = dia;
	}

	public String getTempo() {
		return tempo;
	}

	@XmlElement
	public void setTempo(String tempo) {
		this.tempo = tempo;
	}

	public int getMaxima() {
		return maxima;
	}

	@XmlElement
	public void setMaxima(int maxima) {
		this.maxima = maxima;
	}

	public int getMinima() {
		return minima;
	}

	@XmlElement
	public void setMinima(int minima) {
		this.minima = minima;
	}

	public boolean isCold() {
		boolean response = false;
		if (minima <= 15 && maxima <= 23)
			response = true;
		return response;
	}

	public boolean isMild() {
		boolean response = false;
		if (minima <= 23 && maxima < 28)
			response = true;
		return response;
	}

	public boolean isHot() {
		boolean response = false;
		if (minima < 25 && maxima > 28)
			response = true;
		return response;
	}

}
