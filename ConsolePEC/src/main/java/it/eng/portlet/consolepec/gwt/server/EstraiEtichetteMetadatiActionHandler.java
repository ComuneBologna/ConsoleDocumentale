package it.eng.portlet.consolepec.gwt.server;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.EstraiEtichetteMetadatiAction;
import it.eng.portlet.consolepec.gwt.shared.action.EstraiEtichetteMetadatiResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneMetadatiPratica;

/**
 *
 * @author biagiot
 *
 */
public class EstraiEtichetteMetadatiActionHandler implements ActionHandler<EstraiEtichetteMetadatiAction, EstraiEtichetteMetadatiResult> {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	GestioneMetadatiPratica gestioneMetadatiPratica;

	@Override
	public EstraiEtichetteMetadatiResult execute(EstraiEtichetteMetadatiAction action, ExecutionContext context) throws ActionException {
		logger.info("Start EstraiEtichetteMetadatiResult");
		EstraiEtichetteMetadatiResult result = null;

		try {
			Map<String, String> etichetteMetadatiMap = gestioneMetadatiPratica.getEtichetteMetadatiMap(action.getTipoPratica());
			result = new EstraiEtichetteMetadatiResult(etichetteMetadatiMap);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new EstraiEtichetteMetadatiResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new EstraiEtichetteMetadatiResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		logger.info("End EstraiEtichetteMetadatiResult");
		return result;
	}

	@Override
	public Class<EstraiEtichetteMetadatiAction> getActionType() {
		return EstraiEtichetteMetadatiAction.class;
	}

	@Override
	public void undo(EstraiEtichetteMetadatiAction arg0, EstraiEtichetteMetadatiResult arg1, ExecutionContext arg2) throws ActionException {}

}
