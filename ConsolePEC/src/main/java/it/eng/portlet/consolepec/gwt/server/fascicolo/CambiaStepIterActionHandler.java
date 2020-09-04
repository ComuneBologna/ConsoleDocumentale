package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.consolepec.client.StepIterClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStepIter;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaStepIterResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 * Servizio generico di recupero ruoli di riassegnazione e creazione pratica
 *
 */
public class CambiaStepIterActionHandler implements ActionHandler<CambiaStepIter, CambiaStepIterResult> {

	private static Logger logger = LoggerFactory.getLogger(CambiaStepIterActionHandler.class);

	@Autowired
	StepIterClient stepIterClient;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	public CambiaStepIterActionHandler() {}

	@Override
	public CambiaStepIterResult execute(CambiaStepIter action, ExecutionContext context) throws ActionException {
		try {

			String pathFascicolo = Base64Utils.URLdecodeAlfrescoPath(action.getFascicoloPath());
			StepIter step = action.getStepIter();

			logger.debug("Cambio step iter. fascicolo:" + pathFascicolo + " - step " + step.getNome());
			LockedPratica lockedPratica = stepIterClient.cambiaStepIter(pathFascicolo, step.getNome(), userSessionUtil.getUtenteSpagic());

			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);

			FascicoloDTO fascicoloDTO = utilPratica.fascicoloToDettaglio(fascicolo);
			logger.debug("Ricaricato il dto fascicolo: {} ", fascicoloDTO);

			return new CambiaStepIterResult(fascicoloDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new CambiaStepIterResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new CambiaStepIterResult(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.debug("Fine cambia step Iter");
		}
	}

	@Override
	public Class<CambiaStepIter> getActionType() {
		return CambiaStepIter.class;
	}

	@Override
	public void undo(CambiaStepIter action, CambiaStepIterResult result, ExecutionContext context) throws ActionException {}

}
