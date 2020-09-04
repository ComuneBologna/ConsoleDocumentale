package it.eng.consolepec.xmlplugin.util;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;

import java.util.List;

public class XmlPluginFormatUtil {

	public static String format(String valore){
		return GenericsUtil.isNotNullOrEmpty(valore) ? valore : "nullo";
	}
	public static String format(Number valore){
		return valore != null ? valore.toString() : "nullo";
	}
	
	public static String format(List<String> valori){
		return valori != null && !valori.isEmpty() ? "["  + GenericsUtil.convertCollectionToString(valori) + "]" : "nullo";
	}
}
