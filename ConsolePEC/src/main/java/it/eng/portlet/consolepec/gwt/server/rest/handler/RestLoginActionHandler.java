package it.eng.portlet.consolepec.gwt.server.rest.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.exception.ConsoleDocumentaleException;
import it.eng.portlet.consolepec.gwt.server.rest.RestClientInvoker;
import it.eng.portlet.consolepec.gwt.shared.rest.RestLoginAction;
import it.eng.portlet.consolepec.gwt.shared.rest.RestLoginResult;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Giacomo F.M.
 * @since 2019-07-04
 */
@Slf4j
public class RestLoginActionHandler implements ActionHandler<RestLoginAction, RestLoginResult> {

	@Autowired
	private RestClientInvoker restClientInvoker;

	@Override
	public RestLoginResult execute(RestLoginAction action, ExecutionContext context) throws ActionException {
		try {
			if (restClientInvoker.login()) {
				return new RestLoginResult();
			}
			return new RestLoginResult("Errore in fase di autenticazione dei servizi rest");
		} catch (ConsoleDocumentaleException e) {
			log.error("Errore in fasi di autenticazione nei servizi rest", e);
			return new RestLoginResult(e.getOutputMessage());
		}
	}

	@Override
	public Class<RestLoginAction> getActionType() {
		return RestLoginAction.class;
	}

	@Override
	public void undo(RestLoginAction action, RestLoginResult result, ExecutionContext context) throws ActionException {
		// ~
	}

}
