package it.eng.portlet.consolepec.gwt.server.validators;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.RequestProvider;
import com.gwtplatform.dispatch.server.actionvalidator.ActionValidator;
import com.gwtplatform.dispatch.shared.Action;
import com.gwtplatform.dispatch.shared.Result;

import it.eng.portlet.consolepec.gwt.shared.exception.SessionExpiredException;

public class SessionMonitorValidator implements ActionValidator {

	private static final Logger logger = LoggerFactory.getLogger(SessionMonitorValidator.class);

	@Autowired
	RequestProvider requestProvider;

	private String currentUserKey;

	@Override
	public boolean isValid(Action<? extends Result> action) throws SessionExpiredException {
		HttpSession httpSession = requestProvider.getServletRequest().getSession();
		boolean result = httpSession.getAttribute(currentUserKey) != null;

		if (!result) {
			logger.warn("Sessione non valida; Dati nuova sessione: Data creazione: {} - Id: {}", new Date(httpSession.getCreationTime()), httpSession.getId());
			httpSession.invalidate();
			throw new SessionExpiredException();
		}

		return result;
	}

	public void setKeyToCheck(String key) {
		this.currentUserKey = key;
	}

}
