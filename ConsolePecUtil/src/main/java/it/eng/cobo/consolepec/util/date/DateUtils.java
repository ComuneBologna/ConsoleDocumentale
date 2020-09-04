package it.eng.cobo.consolepec.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;

public class DateUtils {

	public static DateFormat DATEFORMAT_DATE = new SimpleDateFormat(ConsoleConstants.FORMATO_DATA);
	public static DateFormat DATEFORMAT_US_DATE = new SimpleDateFormat(ConsoleConstants.FORMATO_US_DATA);
	public static DateFormat DATEFORMAT_DATEH = new SimpleDateFormat(ConsoleConstants.FORMATO_DATAORA);
	public static DateFormat DATEFORMAT_TIMESEC = new SimpleDateFormat(ConsoleConstants.FORMATO_TIMESEC);
	public static DateFormat DATEFORMAT_DATAORAMILLIS = new SimpleDateFormat(ConsoleConstants.FORMATO_DATAORAMILLIS);
	public static DateFormat DATEFORMAT_DATAORASEC_1 = new SimpleDateFormat(ConsoleConstants.FORMATO_DATAORASEC_1);
	public static DateFormat DATEFORMAT_DATAORASEC_2 = new SimpleDateFormat(ConsoleConstants.FORMATO_DATAORASEC_2);
	public static DateFormat DATEFORMAT_ISO8601 = new SimpleDateFormat(ConsoleConstants.FORMATO_ISO8601);
	public static DateFormat DATEFORMAT_ISO8601_2 = new SimpleDateFormat(ConsoleConstants.FORMATO_ISO8601_2);
	public static DateFormat DATEFORMAT_ISO8601_3 = new SimpleDateFormat(ConsoleConstants.FORMATO_ISO8601_3);
	public static DateFormat DATEFORMAT_GLOBAL = new SimpleDateFormat(ConsoleConstants.FORMATO_GLOBAL);
	public static DateFormat DATEFORMAT_DATAORA_1 = new SimpleDateFormat(ConsoleConstants.FORMATO_DATAORA_1);
	public static DateFormat DATEFORMAT_DATAORA_2 = new SimpleDateFormat(ConsoleConstants.FORMATO_DATAORA_2);
	public static DateFormat DATEFORMAT_DATAORASEC_3 = new SimpleDateFormat(ConsoleConstants.FORMATO_DATAORASEC_3);
	public static DateFormat DATEFORMAT_DATE_1 = new SimpleDateFormat(ConsoleConstants.FORMATO_DATA_1);

	public static Date convert(String data, DateFormat... dateFormats) throws InvalidArgumentException {

		for (DateFormat dateFormat : dateFormats) {

			try {
				return dateFormat.parse(data);

			} catch (ParseException e) {
				continue;
			}
		}

		throw new InvalidArgumentException("La data " + data + " non rispetta i formati", true);

	}

	public static XMLGregorianCalendar dateToXMLGrCal(Date date) {
		if (date == null)
			return null;
		@SuppressWarnings("static-access") GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
		} catch (DatatypeConfigurationException e) {
			throw new RuntimeException(e);
		}
	}

	public static Date xmlGrCalToDate(XMLGregorianCalendar date) {
		return (date == null) ? null : date.toGregorianCalendar().getTime();
	}

	/**
	 * Ritorna la data con giorno-mese-anno valorizzati e ore-minuti-secondi-milliseconi a zero
	 */
	public static Date getMidnightDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	/**
	 * Ritorna la data ODIERNA con giorno-mese-anno valorizzati e ore-minuti-secondi-milliseconi a zero
	 */
	public static Date getMidnightToday() {
		return getMidnightDate(new Date());
	}

	public static XMLGregorianCalendar stringToXMLGregorianCalendar(String stringDate) throws ParseException, DatatypeConfigurationException {
		Date date = DateUtils.DATEFORMAT_ISO8601_2.parse(stringDate);
		GregorianCalendar gregorianCalendar = (GregorianCalendar) GregorianCalendar.getInstance();
		gregorianCalendar.setTime(date);
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
	}

	/**
	 * Ritorna la data in stringa secondo il patternA da una data in stringa con un pattern patternDA
	 */
	public static String convertStringDateToAnnotherFormatterStringDate(String dataInStringa, String patternDa, String patternA) {
		DateFormat formatterDa = new SimpleDateFormat(patternDa);

		Date data;
		try {
			data = formatterDa.parse(dataInStringa);

			DateFormat df = new SimpleDateFormat(patternA);
			String reportDate = df.format(data);

			return reportDate;
		} catch (ParseException e) {
			return null;
		}
	}
}
