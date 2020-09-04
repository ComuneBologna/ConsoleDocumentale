package it.eng.portlet.consolepec.gwt.server.configurazioni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaAnagraficheRuoliAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaAnagraficheRuoliResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author biagiot
 *
 */
public class CaricaAnagraficheRuoliActionHandler implements ActionHandler<CaricaAnagraficheRuoliAction, CaricaAnagraficheRuoliResult> {

	private static final Logger logger = LoggerFactory.getLogger(CaricaAnagraficheRuoliActionHandler.class);

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	public CaricaAnagraficheRuoliResult execute(CaricaAnagraficheRuoliAction action, ExecutionContext arg1) throws ActionException {
		logger.info("Start CaricaAnagraficheRuoliActionHandler");
		CaricaAnagraficheRuoliResult result;

		try {
			if (action.isRicarica()) {
				gestioneConfigurazioni.reloadAbilitazioniRuoli(userSessionUtil.getUtenteSpagic());
				gestioneConfigurazioni.reloadRuoli(userSessionUtil.getUtenteSpagic());
			}

			result = new CaricaAnagraficheRuoliResult(gestioneConfigurazioni.getAnagraficheRuoli(), gestioneConfigurazioni.getAnagraficheRuoliPersonali());

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new CaricaAnagraficheRuoliResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new CaricaAnagraficheRuoliResult(ConsolePecConstants.ERROR_MESSAGE);
		}

		logger.info("End CaricaAnagraficheRuoliActionHandler");
		return result;
	}

	@Override
	public Class<CaricaAnagraficheRuoliAction> getActionType() {
		return CaricaAnagraficheRuoliAction.class;
	}

	@Override
	public void undo(CaricaAnagraficheRuoliAction arg0, CaricaAnagraficheRuoliResult arg1, ExecutionContext arg2) throws ActionException {}
}
