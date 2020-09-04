package it.eng.portlet.consolepec.gwt.server.profilazione;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaSupervisoriUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.CaricaSupervisoriUtenteResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;

/**
 *
 * @author biagiot
 *
 */
public class CaricaSupervisoriUtenteActionHandler implements ActionHandler<CaricaSupervisoriUtenteAction, CaricaSupervisoriUtenteResult> {

	private final static Logger logger = LoggerFactory.getLogger(CaricaSupervisoriUtenteActionHandler.class);

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Override
	public CaricaSupervisoriUtenteResult execute(CaricaSupervisoriUtenteAction action, ExecutionContext context) throws ActionException {
		logger.info("Inizio esecuzione CaricaSupervisoriUtenteActionHandler");

		CaricaSupervisoriUtenteResult result;

		Map<String, List<AnagraficaRuolo>> ruoliSuperutenti = new HashMap<String, List<AnagraficaRuolo>>();
		Map<String, List<AnagraficaRuolo>> ruoliMatriceVisibilita = new HashMap<String, List<AnagraficaRuolo>>();

		try {

			for (AnagraficaRuolo ar : gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli()) {
				List<AnagraficaRuolo> rsup = gestioneConfigurazioni.getRuoliSuperutentiModifica(ar.getRuolo());
				if (rsup != null) {
					ruoliSuperutenti.put(ar.getRuolo(), rsup);
				}

				List<AnagraficaRuolo> rmatr = gestioneConfigurazioni.getRuoliSuperutentiMatriceVisibilita(ar.getRuolo());
				if (rmatr != null) {
					ruoliMatriceVisibilita.put(ar.getRuolo(), rmatr);
				}
			}

			result = new CaricaSupervisoriUtenteResult(ruoliSuperutenti, ruoliMatriceVisibilita);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new CaricaSupervisoriUtenteResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new CaricaSupervisoriUtenteResult(ConsolePecConstants.ERROR_MESSAGE);
		}

		logger.info("Fine esecuzione CaricaSupervisoriUtenteActionHandler");
		return result;
	}

	@Override
	public Class<CaricaSupervisoriUtenteAction> getActionType() {
		return CaricaSupervisoriUtenteAction.class;
	}

	@Override
	public void undo(CaricaSupervisoriUtenteAction action, CaricaSupervisoriUtenteResult result, ExecutionContext context) throws ActionException {}

}
