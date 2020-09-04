package it.eng.portlet.consolepec.gwt.server.configurazioni;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaSettoriAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaSettoriResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * ActionHandler per il recupero di tutti i settori
 *
 * @author biagiot
 *
 */
public class CaricaSettoriActionHandler implements ActionHandler<CaricaSettoriAction, CaricaSettoriResult> {

	private static final Logger logger = LoggerFactory.getLogger(CaricaSettoriActionHandler.class);

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	public CaricaSettoriResult execute(CaricaSettoriAction action, ExecutionContext context) throws ActionException {
		logger.info("Inizio CaricaSettoriActionHandler");
		CaricaSettoriResult result = null;

		try {
			if (action.isRicarica()) {
				gestioneConfigurazioni.reloadSettori(userSessionUtil.getUtenteSpagic());
			}

			List<Settore> settori = gestioneConfigurazioni.getSettori();
			result = new CaricaSettoriResult(settori);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new CaricaSettoriResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new CaricaSettoriResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		logger.info("Fine CaricaSettoriActionHandler");
		return result;
	}

	@Override
	public Class<CaricaSettoriAction> getActionType() {
		return CaricaSettoriAction.class;
	}

	@Override
	public void undo(CaricaSettoriAction arg0, CaricaSettoriResult arg1, ExecutionContext arg2) throws ActionException {
		// ~
	}
}
