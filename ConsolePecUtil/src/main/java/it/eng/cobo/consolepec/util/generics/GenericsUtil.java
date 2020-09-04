package it.eng.cobo.consolepec.util.generics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

public class GenericsUtil {

	public static boolean isSame(Date d1, Date d2) {
		if (d1 == null)
			return d2 == null;
		return d1.equals(d2);
	}

	public static boolean isSame(Double d1, Double d2) {
		if (d1 == null)
			return d2 == null;

		return d1.equals(d2);
	}

	public static boolean isSame(Object d1, Object d2) {
		if (d1 == null)
			return d2 == null;

		return d1.equals(d2);
	}

	public static boolean isSame(String s1, String s2) {
		if (isNullOrEmpty(s1))
			return isNullOrEmpty(s2);
		return s1.equals(s2);
	}

	public static boolean isSame(List<String> s1, List<String> s2) {
		if (s1 == null)
			return s2 == null;
		if (s2 == null)
			return s1 == null;

		Collections.sort(s1);
		Collections.sort(s2);
		return s1.equals(s2);
	}

	public static boolean isNullOrEmpty(String input) {
		return input == null || input.equalsIgnoreCase("");
	}

	public static boolean isNotNullOrEmpty(String input) {
		return !isNullOrEmpty(input);
	}

	/*
	 * STRINGHE
	 */

	public static String sanitizeNull(String input) {
		return sanitizeNull(input, "");
	}

	public static String sanitizeNull(String input, String placeholder) {
		if (input == null || input.trim().length() == 0)
			return placeholder;
		else
			return input;
	}

	public static String sanitizeNullObject(Object input) {
		return sanitizeNullObject(input, "");
	}

	public static String sanitizeNullObject(Object input, String placeholder) {
		if (input != null) {
			return sanitizeNull(input.toString(), placeholder);
		} else {
			return sanitizeNull(null, placeholder);
		}
	}

	public static String safeTrim(String string) {
		if (Strings.isNullOrEmpty(string))
			return null;
		else
			return string.trim();
	}

	public static String convertCollectionToString(List<String> elements) {
		if (elements != null && elements.size() == 0)
			return null;
		StringBuilder sb = new StringBuilder(elements.get(0));
		for (int i = 1; i < elements.size(); i++) {
			sb.append(", ");
			sb.append(elements.get(i));
		}
		return sb.toString();

	}

	public static String convertMapToString(Map<String, String> campi) {

		if (campi != null && campi.size() == 0)
			return null;
		StringBuilder sb = new StringBuilder();
		List<String> keys = new ArrayList<String>(campi.keySet());
		sb.append(keys.get(0)).append("=").append(campi.get(keys.get(0)));
		for (int i = 1; i < keys.size(); i++) {
			sb.append(", ").append(keys.get(i)).append("=").append(campi.get(keys.get(i)));
		}
		return sb.toString();
	}

	/**
	 * Ritorna una stringa unica formata dalla concatenazione di tutte le stringhe intervallate dal separatore.
	 */
	public static String concatena(final String separatore, final String... strings) {
		if (strings.length > 0) {
			StringBuilder sb = new StringBuilder(strings[0]);
			for (int i = 1; i < strings.length; i++) {
				if (!Strings.isNullOrEmpty(strings[i])) {
					sb.append(Strings.nullToEmpty(separatore)).append(strings[i]);
				}
			}
			return sb.toString();
		}
		return "";
	}

	public static String normalizzaValoreTesto(String input) {
		return (input == null || input.isEmpty()) ? null : input;
	}

	public static String normalizzaValoreNumerico(Long input) {
		return (input == null) ? null : Long.toString(input);
	}

	public static String format(List<String> strings) {
		StringBuilder bl = new StringBuilder();
		for (String s : strings)
			bl.append(s);
		return bl.toString();
	}

	public static String normalizzaSpaziRitorniTabulazioni(String input) {
		return (input == null) ? input : input.replaceAll("\t", "").replaceAll("\n", "").replaceAll("\r", "").trim();
	}

	/*
	 * HTML
	 */

	public static String convertNewLinesToHTML(String text) {
		text = text.replaceAll("(\r\n|\n\r|\r|\n)", "<br />");
		return text;
	}
}
