package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Schedule {
	private int weekday;
	private List<Period> periods;
	
	private static final Map<Integer, String> WEEK_DAY;
	static {
		WEEK_DAY = new HashMap<Integer, String>();
		WEEK_DAY.put(1, "Segunda-feira");
		WEEK_DAY.put(2, "Terça-feira");
		WEEK_DAY.put(3, "Quarta-feira");
		WEEK_DAY.put(4, "Quinta-feira");
		WEEK_DAY.put(5, "Sexta-feira");
	}
	
	public List<Period> getPeriods() {
		return periods;
	}

	public String getWeekday() {
		return WEEK_DAY.get(weekday);
	}
}
