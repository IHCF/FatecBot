package model;

import java.util.Calendar;
import java.util.Date;

public class ModelUtils {
	public static int getDayWeek() {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());

		return c.get(Calendar.DAY_OF_WEEK);
	}
}
