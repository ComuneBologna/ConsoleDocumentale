package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.GeneraTitoloFascicoloClient;
import it.eng.consolepec.spagicclient.bean.request.fascicolo.FascicoloRequest;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GeneraTitoloFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.GeneraTitoloFascicoloResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.converters.requestfascicolo.RequestFascicoloConverter;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class GeneraTitoloFascicoloActionHandler implements ActionHandler<GeneraTitoloFascicoloAction, GeneraTitoloFascicoloResult> {

	@Autowired
	GeneraTitoloFascicoloClient generaTitoloFascicoloClient;

	@Autowired
	RequestFascicoloConverter requestFascicoloConverter;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	private static final Logger logger = LoggerFactory.getLogger(GeneraTitoloFascicoloActionHandler.class);

	@Override
	public GeneraTitoloFascicoloResult execute(GeneraTitoloFascicoloAction action, ExecutionContext arg1) throws ActionException {

		logger.info("Generazione titolo automatico: {}", action.getCreaFascicoloDTO());

		GeneraTitoloFascicoloResult result = new GeneraTitoloFascicoloResult();

		if (action.getCreaFascicoloDTO() == null) {
			result.setError(true);
			result.setErrorMessage("Dati in ingresso non validi");
			return result;
		}

		try {
			action.getCreaFascicoloDTO().setAssegnatario(gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(action.getCreaFascicoloDTO().getAssegnatario()).getRuolo());
			FascicoloRequest fascicoloRequest = requestFascicoloConverter.convertToFascicoloRequest(action.getCreaFascicoloDTO());
			result.setTitolo(generaTitoloFascicoloClient.generaTitoloFascicoloDaTemplate(fascicoloRequest, userSessionUtil.getUtenteSpagic()));

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result.setError(true);
			result.setErrorMessage(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result.setError(true);
			result.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
		}

		return result;
	}

	@Override
	public Class<GeneraTitoloFascicoloAction> getActionType() {
		return GeneraTitoloFascicoloAction.class;
	}

	@Override
	public void undo(GeneraTitoloFascicoloAction arg0, GeneraTitoloFascicoloResult arg1, ExecutionContext arg2) throws ActionException {}

}
