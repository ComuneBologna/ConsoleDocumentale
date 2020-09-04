package it.eng.cobo.consolepec.util.file;

import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringEscapeUtils;

import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;

/**
 * @author GiacomoFM
 * @since 19/mar/2019
 */
public class FileUtil {

	public static final String DOT = ".";
	public static final String OPEN = " (";
	public static final String CLOSE = ")";
	public static final String COPIES_ENDING = ".+ \\([0-9]+\\){1}";
	public static final String POSSIBLE_FILE_NAME = ".+\\.[a-zA-Z0-9_-]+";

	private static final String FILENAME_ILLEGAL_REGEX = "[\\\"\\*\\\\\\>\\<\\?\\/\\:\\|\\–\\“\\’\\”\\,\\;\\&\\#]";
	private static final Pattern FILENAME_ILLEGAL_PATTERN_REPLACE = Pattern.compile(FILENAME_ILLEGAL_REGEX);
	private static final int FILENAME_MAX_LENGTH = 255;

	private static String copies(Integer copies) {
		if (copies <= 0) {
			return "";
		}
		return OPEN + (copies + 1) + CLOSE;
	}

	public static boolean isValidFileName(String name) {
		return name != null && !FILENAME_ILLEGAL_PATTERN_REPLACE.matcher(name).find() && name.length() <= FILENAME_MAX_LENGTH;
	}

	public static String getValidFileName(String fileName) throws InvalidArgumentException {

		if (fileName == null || fileName.isEmpty()) {
			throw new InvalidArgumentException("Il nome del file non deve essere vuoto", true);
		}

		String res = fileName.trim();
		res = StringEscapeUtils.unescapeXml(res);
		res = StringEscapeUtils.unescapeHtml(res);
		res = FILENAME_ILLEGAL_PATTERN_REPLACE.matcher(fileName).replaceAll("_");

		if (res.length() > 255) {
			String extension = FilenameUtils.getExtension(res);
			res = res.substring(0, FILENAME_MAX_LENGTH - (extension != null && !extension.trim().isEmpty() ? (extension.length() + 1) : 0));
			res = res + ((extension != null && !extension.trim().isEmpty()) ? ("." + extension) : (""));
		}

		if (res.endsWith(".")) {
			int index = res.lastIndexOf(".");
			res = res.substring(0, index);
		}

		return res;
	}

	public static String createSequentialFileName(String name, Integer copies) {
		if (name == null || name.isEmpty() || copies <= 0) {
			return name;
		}
		if (name.matches(POSSIBLE_FILE_NAME)) {
			return name.substring(0, name.lastIndexOf(DOT)) + copies(copies) + name.substring(name.lastIndexOf(DOT));
		}

		return name + copies(copies);

	}

	public static String recoverOriginalFileName(String name) {
		if (name == null || name.isEmpty()) {
			return name;
		}
		String tmp = name;
		if (name.matches(POSSIBLE_FILE_NAME)) {
			tmp = name.substring(0, name.lastIndexOf(DOT));
		}
		if (tmp.matches(COPIES_ENDING)) {
			return name.matches(POSSIBLE_FILE_NAME) //
					? tmp.substring(0, tmp.lastIndexOf(OPEN)) + name.substring(name.lastIndexOf(DOT)) //
					: tmp.substring(0, tmp.lastIndexOf(OPEN));
		}
		return name;
	}

}
