package com.budget.droid;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class Util {
	public static boolean isEmpty(String string){
		if (string == null) {
			return true;
		}
		if (string.equals("")) {
			return true;
		}
		return false;
	}

	public static GregorianCalendar stringToDate(String date){
		StringTokenizer st = new StringTokenizer(date, "-");
		int year = Integer.parseInt(st.nextToken());
		int month = Integer.parseInt(st.nextToken());
		int day = Integer.parseInt(st.nextToken());
		return new GregorianCalendar(year, month, day);
	}
	public static String dateToString(Calendar date){
		StringBuilder sb = new StringBuilder();
		sb.append(date.get(Calendar.YEAR));
		sb.append('-');
		sb.append(date.get(Calendar.MONTH));
		sb.append('-');
		sb.append(date.get(Calendar.DAY_OF_MONTH));
		return sb.toString();
	}
}