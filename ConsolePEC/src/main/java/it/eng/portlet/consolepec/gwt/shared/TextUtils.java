package it.eng.portlet.consolepec.gwt.shared;


public class TextUtils {

	public static String reduce(String text, int maxLength, int maxRows) {
		if(text == null)
			return text;
		String eol = null;		// determino in maniera abbastanza empirica l'eol
		String[] eolVari = {"\r\n", "\n", "\r", "\n\r"};
		for(String ls : eolVari){
			if(text.matches(ls)){
				eol = ls;
				break;
			}
		}
		if(eol == null)
			eol = "\n";
		StringBuffer sb = new StringBuffer();
		String[] tokens = text.split(eol);
		for (int i = 0; i < tokens.length; i++)
			if (i < maxRows && (sb.length() + tokens[i].length()) < maxLength)
				sb.append((tokens.length==1) ? tokens[i] : tokens[i] + eol);
		return sb.toString();
	}

	public static String textToHTML(String text) {
		if (text.contains("<"))
			return text;
		text = text.replaceAll("\r\n|\n\r|\r|\n", "<br/>");
		text = text.replaceAll("\t", "&nbsp;");
		text = text.replaceAll(" ", "&nbsp;");
		return text;
	}
	
	public static String getIdDocumentaleFromClientID(String clientId){
		String alfrescoPath = Base64Utils.URLdecodeAlfrescoPath(clientId);
		return alfrescoPath.split("/")[4];
	}

}
