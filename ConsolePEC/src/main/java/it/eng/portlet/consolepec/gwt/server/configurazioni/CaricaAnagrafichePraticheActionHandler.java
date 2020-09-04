package it.eng.portlet.consolepec.gwt.server.configurazioni;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaAnagrafichePraticheAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.CaricaAnagrafichePraticheResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author biagiot
 *
 */
public class CaricaAnagrafichePraticheActionHandler implements ActionHandler<CaricaAnagrafichePraticheAction, CaricaAnagrafichePraticheResult> {

	private static final Logger logger = LoggerFactory.getLogger(CaricaAnagrafichePraticheActionHandler.class);

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Override
	public CaricaAnagrafichePraticheResult execute(CaricaAnagrafichePraticheAction action, ExecutionContext arg1) throws ActionException {
		logger.info("Start CaricaAnagrafichePraticheActionHandler");
		CaricaAnagrafichePraticheResult result;

		try {
			if (action.isRicarica()) {
				gestioneConfigurazioni.reloadAnagraficheFascicoli(userSessionUtil.getUtenteSpagic());
				gestioneConfigurazioni.reloadAnagraficheIngressi(userSessionUtil.getUtenteSpagic());
			}

			result = new CaricaAnagrafichePraticheResult(gestioneConfigurazioni.getAnagraficheFascicoli(), gestioneConfigurazioni.getAnagraficheIngressi(),
					gestioneConfigurazioni.getAnagraficheModelli(), gestioneConfigurazioni.getAnagraficheMailInUscita(), gestioneConfigurazioni.getAnagraficheComunicazioni(),
					gestioneConfigurazioni.getAnagrafichePraticaModulistica());

		} catch (SpagicClientException e) {
			logger.error("Errore durante il caricamento delle anagrafiche delle pratiche", e);
			result = new CaricaAnagrafichePraticheResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore durante il caricamento delle anagrafiche delle pratiche", e);
			result = new CaricaAnagrafichePraticheResult(ConsolePecConstants.ERROR_MESSAGE);
		}

		logger.info("End CaricaAnagrafichePraticheActionHandler");
		return result;
	}

	@Override
	public Class<CaricaAnagrafichePraticheAction> getActionType() {
		return CaricaAnagrafichePraticheAction.class;
	}

	@Override
	public void undo(CaricaAnagrafichePraticheAction arg0, CaricaAnagrafichePraticheResult arg1, ExecutionContext arg2) throws ActionException {}
}
