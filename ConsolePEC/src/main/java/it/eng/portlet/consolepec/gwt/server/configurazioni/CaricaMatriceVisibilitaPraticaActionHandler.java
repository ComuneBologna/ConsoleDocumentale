package it.eng.portlet.consolepec.gwt.server.configurazioni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaMatriceVisibilitaPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaMatriceVisibilitaPraticaResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;

/**
 *
 * @author biagiot
 *
 */
public class CaricaMatriceVisibilitaPraticaActionHandler implements ActionHandler<CaricaMatriceVisibilitaPraticaAction, CaricaMatriceVisibilitaPraticaResult> {

	private final static Logger logger = LoggerFactory.getLogger(CaricaMatriceVisibilitaPraticaActionHandler.class);

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Override
	public CaricaMatriceVisibilitaPraticaResult execute(CaricaMatriceVisibilitaPraticaAction action, ExecutionContext context) throws ActionException {
		logger.info("Inizio esecuzione CaricaMatriceVisibilitaPraticaActionHandler");

		CaricaMatriceVisibilitaPraticaResult result;

		try {
			if (action.getTipoPratica() == null) {
				logger.error("Tipo pratica non valido");
				result = new CaricaMatriceVisibilitaPraticaResult("Tipo pratica non valido");

			} else {
				result = new CaricaMatriceVisibilitaPraticaResult(gestioneConfigurazioni.getRuoliSuperutentiMatriceVisibilita(action.getTipoPratica()));
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new CaricaMatriceVisibilitaPraticaResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new CaricaMatriceVisibilitaPraticaResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		logger.info("Fine esecuzione CaricaMatriceVisibilitaPraticaActionHandler");
		return result;
	}

	@Override
	public Class<CaricaMatriceVisibilitaPraticaAction> getActionType() {
		return CaricaMatriceVisibilitaPraticaAction.class;
	}

	@Override
	public void undo(CaricaMatriceVisibilitaPraticaAction action, CaricaMatriceVisibilitaPraticaResult result, ExecutionContext context) throws ActionException {}

}
