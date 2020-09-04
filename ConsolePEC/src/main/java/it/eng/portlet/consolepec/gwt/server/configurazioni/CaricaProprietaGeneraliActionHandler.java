package it.eng.portlet.consolepec.gwt.server.configurazioni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaProprietaGeneraliAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaProprietaGeneraliResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;

/**
 *
 * ActionHandler per il caricamento di tutte le proprieta generali Console
 *
 * @author biagiot
 *
 */
public class CaricaProprietaGeneraliActionHandler implements ActionHandler<CaricaProprietaGeneraliAction, CaricaProprietaGeneraliResult> {

	private static final Logger logger = LoggerFactory.getLogger(CaricaProprietaGeneraliActionHandler.class);

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Override
	public CaricaProprietaGeneraliResult execute(CaricaProprietaGeneraliAction action, ExecutionContext context) throws ActionException {

		logger.info("Start CaricaProprietaGeneraliActionHandler");
		CaricaProprietaGeneraliResult result = null;

		try {
			result = new CaricaProprietaGeneraliResult(gestioneConfigurazioni.getProprietaGenerali(), gestioneConfigurazioni.getBaseUrlPubblicazioneAllegati());

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new CaricaProprietaGeneraliResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new CaricaProprietaGeneraliResult(ConsolePecConstants.ERROR_MESSAGE);
		}

		logger.info("End CaricaProprietaGeneraliActionHandler");
		return result;
	}

	@Override
	public Class<CaricaProprietaGeneraliAction> getActionType() {
		return CaricaProprietaGeneraliAction.class;
	}

	@Override
	public void undo(CaricaProprietaGeneraliAction action, CaricaProprietaGeneraliResult result, ExecutionContext context) throws ActionException {}
}
