package it.eng.portlet.consolepec.gwt.server.pec;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ElettoraleAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ElettoraleAction.OperazioneElettorale;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ElettoraleResult;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.elettorale.GestioneElettorale;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

public class ElettoraleActionHandler implements ActionHandler<ElettoraleAction, ElettoraleResult> {

	private static Logger logger = LoggerFactory.getLogger(ElettoraleActionHandler.class);

	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	XMLPluginToDTOConverter util;
	@Autowired
	GestioneElettorale gestioneElettorale;

	public ElettoraleActionHandler() {}

	@Override
	public ElettoraleResult execute(ElettoraleAction action, ExecutionContext context) throws ActionException {

		String alfrescoPath = Base64Utils.URLdecodeAlfrescoPath(action.getClientID());
		logger.debug("Inizio elaborazione elettorale email {}.", alfrescoPath);

		try {

			// ricavo la lista delle pratiche da ricaricare lato server e da passare al client per il medesimo scopo
			List<String> clientIDPraticheCollegate = new ArrayList<String>();
			PraticaEmailIn emailPreElaborazione = praticaSessionUtil.loadPraticaEmailInFromEncodedPath(action.getClientID(), TipologiaCaricamento.RICARICA);
			for (PraticaCollegata fascicoliCollegati : emailPreElaborazione.getFascicoliCollegati()) {
				String clientID = Base64Utils.URLencodeAlfrescoPath(fascicoliCollegati.getAlfrescoPath());
				praticaSessionUtil.removePraticaFromEncodedPath(clientID);
				clientIDPraticheCollegate.add(clientID);
			}

			// elaborazione elettorale
			if (OperazioneElettorale.IMPORTA.equals(action.getOperazioneElettorale())) {

				logger.debug("Inizio elaborazione elettorale di importazione email {}.", alfrescoPath);
				gestioneElettorale.importa(alfrescoPath);

			} else if (OperazioneElettorale.ANNULLA.equals(action.getOperazioneElettorale())) {

				logger.debug("Inizio elaborazione elettorale di importazione email {}.", alfrescoPath);
				gestioneElettorale.annulla(alfrescoPath);

			} else {
				logger.error("Elaborazione elettorale non supportata email {}", alfrescoPath);
				return new ElettoraleResult(true, "Elaborazione elettorale non supportata");
			}

			PraticaEmailIn email = praticaSessionUtil.loadPraticaEmailInFromEncodedPath(action.getClientID(), TipologiaCaricamento.RICARICA);

			logger.debug("Elaborazione elettorale email {} terminata con successo.", alfrescoPath);
			return new ElettoraleResult(util.emailToDettaglioIN(email), clientIDPraticheCollegate);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new ElettoraleResult(true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new ElettoraleResult(true, ConsolePecConstants.ERROR_MESSAGE);

		}

	}

	@Override
	public void undo(ElettoraleAction action, ElettoraleResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<ElettoraleAction> getActionType() {
		return ElettoraleAction.class;
	}
}
