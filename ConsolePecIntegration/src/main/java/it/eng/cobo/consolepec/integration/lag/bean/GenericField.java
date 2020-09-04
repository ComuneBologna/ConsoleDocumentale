package it.eng.cobo.consolepec.integration.lag.bean;

import lombok.Getter;
import lombok.Setter;

public class GenericField {

	@Getter private int begin;
	@Getter private int end;
	@Setter private String value;

	public GenericField(int begin, int end) {
		this.begin = begin;
		this.end = end;
	}

	public String getValue() {
		return rpad(value, end - begin, " ");
	}

	private static String rpad(String in, int length, String pad) {
		String result = new String(in == null ? "" : in);
		while (result.length() < length) {
			result = result + pad;
		}
		return result.substring(0, length);
	}
}
