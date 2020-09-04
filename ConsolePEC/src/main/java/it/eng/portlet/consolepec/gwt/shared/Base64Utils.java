package it.eng.portlet.consolepec.gwt.shared;

import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.io.BaseEncoding;

public class Base64Utils{
	private static Logger logger = Logger.getLogger("Base64Utils");

	public static String URLdecodeAlfrescoPath(String path) {
		try {

			byte[] decode = BaseEncoding.base64Url().omitPadding().decode(path);
			return new String(decode, "UTF-8");
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Errore in deserializzazione URL", e);
			return null;
		}
	}

	public static String URLencodeAlfrescoPath(String path) {
		try {

			String encode = BaseEncoding.base64Url().omitPadding().encode(path.getBytes("UTF-8"));
			return encode;
		} catch (UnsupportedEncodingException e) {

			logger.log(Level.SEVERE, "Errore in serializzazione URL", e);
			return null;
		}
	}
}
