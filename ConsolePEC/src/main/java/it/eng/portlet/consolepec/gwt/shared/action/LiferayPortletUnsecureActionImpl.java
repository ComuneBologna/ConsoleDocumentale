package it.eng.portlet.consolepec.gwt.shared.action;

import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.Result;
import com.gwtplatform.dispatch.shared.SecurityCookie;

/**
 * An {@link Action} that uses the standard service name {@code "dispatch"}. Actions inheriting from this are <b>not</b> secured against XSRF attacks, and they will work even if you do not configure a
 * {@link SecurityCookie}.
 * 
 * @author Philippe Beaudoin
 * 
 * @param <R>
 *            The {@link Result} type.
 */
public class LiferayPortletUnsecureActionImpl<R extends Result> implements Action<R> {
	@Override
	public String getServiceName() {
		return "/" + getPortletContext() + "/dispatch/";
	}

	@Override
	public boolean isSecured() {
		return false;
	}

	public static String getPortletContext() {
		return "ionoi2/consolepec";
	}
}
