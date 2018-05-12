package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "previsao")
public class Forecast {

	private String day;
	private String time;
	private int maximum;
	private int minimum;

	public String getDay() {
		return day;
	}

	@XmlElement(name = "dia")
	public void setDay(String day) {
		this.day = day;
	}

	public String getTime() {
		return time;
	}

	@XmlElement(name = "tempo")
	public void setTime(String time) {
		this.time = time;
	}

	public int getMaximum() {
		return maximum;
	}

	@XmlElement(name = "maxima")
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}

	public int getMinimum() {
		return minimum;
	}

	@XmlElement(name = "minima")
	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public boolean isCold() {
		boolean response = false;
		if (minimum <= 15 && maximum <= 23)
			response = true;
		return response;
	}

	public boolean isMild() {
		boolean response = false;
		if (minimum <= 23 && maximum < 28)
			response = true;
		return response;
	}

	public boolean isHot() {
		boolean response = false;
		if (minimum < 25 && maximum > 28)
			response = true;
		return response;
	}

}
