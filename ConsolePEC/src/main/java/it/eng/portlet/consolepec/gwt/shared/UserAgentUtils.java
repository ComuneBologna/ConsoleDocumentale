package it.eng.portlet.consolepec.gwt.shared;

import com.google.gwt.user.client.Window;

/**
 *
 * @author biagiot
 *
 */
public class UserAgentUtils {

	private final static String CHROME = "chrome";
	private final static String FIREFOX = "firefox";
	private final static String SAFARI = "safari";
	private final static String EDGE = "edge";
	private final static String IE11_v1 = "msie 11";
	private final static String IE11_v2 = "trident/7.0";


	public static String getGWTClientUserAgent() {
		return Window.Navigator.getUserAgent();
	}

	public static boolean isFirefox(String userAgent) {
		return userAgent.toLowerCase().indexOf(FIREFOX) != -1;
	}

	public static boolean isChrome(String userAgent) {
		return userAgent.toLowerCase().indexOf(CHROME) != -1;
	}

	public static boolean isSafari(String userAgent) {
		return  userAgent.toLowerCase().indexOf(SAFARI) != -1 &&  userAgent.toLowerCase().indexOf(CHROME) == -1;
	}

	public static boolean isEdge(String userAgent) {
		return  userAgent.toLowerCase().indexOf(EDGE) != -1;
	}

	public static boolean isIE11(String userAgent) {
		return userAgent.toLowerCase().indexOf(IE11_v1) != -1 || userAgent.toLowerCase().indexOf(IE11_v2) != -1;
	}


	public static boolean isFirefox() {
		String userAgent = getGWTClientUserAgent();
		return userAgent.toLowerCase().indexOf(FIREFOX) != -1;
	}

	public static boolean isChrome() {
		String userAgent = getGWTClientUserAgent();
		return userAgent.toLowerCase().indexOf(CHROME) != -1;
	}

	public static boolean isSafari() {
		String userAgent = getGWTClientUserAgent();
		return  userAgent.toLowerCase().indexOf(SAFARI) != -1 &&  userAgent.toLowerCase().indexOf(CHROME) == -1;
	}

	public static boolean isEdge() {
		String userAgent = getGWTClientUserAgent();
		return  userAgent.toLowerCase().indexOf(EDGE) != -1;
	}

	public static boolean isIE11() {
		String userAgent = getGWTClientUserAgent();
		return userAgent.toLowerCase().indexOf(IE11_v1) != -1 || userAgent.toLowerCase().indexOf(IE11_v2) != -1;
	}

}
