package it.eng.cobo.consolepec.integration.lag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

	public final static String SECOND_FORMAT = "dd/MM/yyyy HH:mm:ss";
	public final static String MINUTE_FORMAT = "dd/MM/yyyy HH:mm";
	public final static String HOUR_FORMAT = "dd/MM/yyyy HH";
	public final static String DAY_FORMAT = "dd/MM/yyyy";
	public final static String WEEK_FORMAT = "EEE dd MMM";
	public final static String MONTH_FORMAT = "MM/yyyy";
	public final static String YEAR_FORMAT = "yyyy";
	public final static String TIME_FORMAT = "HH:mm";
	public final static String HOST_FORMAT = "yyyyMMdd";

	public static boolean isNull(Object valore) {
		return valore == null || valore.toString().equals("");
	}

	public static boolean isNotNull(Object valore) {
		return !isNull(valore);
	}

	public static Date parseDate(String date, String format) throws ParseException {
		if (isNull(date)) return null;

		if (format == null) format = DAY_FORMAT;

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.ITALIAN);
		simpleDateFormat.setLenient(false);

		return simpleDateFormat.parse(date);
	}

	/**
	 * Effettua il padding a sinistra della stringa indicata
	 * 
	 * @param in
	 * @param length
	 * @param pad
	 * @return la stringa modificata
	 */
	public static String lpad(String in, int length, String pad) {
		String result = new String(in == null ? "" : in);

		while (result.length() < length) {
			result = pad + result;
		}

		return result.substring(result.length() - length, result.length());
	}

	/**
	 * Effettua il padding a destra della stringa indicata
	 * 
	 * @param in
	 * @param length
	 * @param pad
	 * @return la stringa modificata
	 */
	public static String rpad(String in, int length, String pad) {
		String result = new String(in == null ? "" : in);

		while (result.length() < length) {
			result = result + pad;
		}

		return result.substring(0, length);
	}
}
