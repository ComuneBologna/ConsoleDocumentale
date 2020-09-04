package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.AssegnazioneEsternaClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ModificaAbilitazioniAssegnaUtenteEsternoAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.ModificaAbilitazioniAssegnaUtenteEsternoResult;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class ModificaAbilitazioniAssegnaUtenteEsternoActionHandler implements ActionHandler<ModificaAbilitazioniAssegnaUtenteEsternoAction, ModificaAbilitazioniAssegnaUtenteEsternoResult> {

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	AssegnazioneEsternaClient assegnazioneEsternaClient;

	private Logger logger = LoggerFactory.getLogger(ModificaAbilitazioniAssegnaUtenteEsternoActionHandler.class);

	public ModificaAbilitazioniAssegnaUtenteEsternoActionHandler() {}

	@Override
	public ModificaAbilitazioniAssegnaUtenteEsternoResult execute(ModificaAbilitazioniAssegnaUtenteEsternoAction action, ExecutionContext context) throws ActionException {

		logger.debug("Action handler per la modifica delle abilitazioni dell'assegnazione dell'utente esterno");

		try {
			String praticaPath = Base64Utils.URLdecodeAlfrescoPath(action.getPraticaPath()); // devo passare il path del fasciolo in chiaro
			assegnazioneEsternaClient.modificaAssegnazioneEsterna(praticaPath, action.getOperazioni(), userSessionUtil.getUtenteSpagic());
			praticaSessionUtil.removePraticaFromEncodedPath(action.getPraticaPath());
			return new ModificaAbilitazioniAssegnaUtenteEsternoResult();

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new ModificaAbilitazioniAssegnaUtenteEsternoResult(e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new ModificaAbilitazioniAssegnaUtenteEsternoResult(ConsolePecConstants.ERROR_MESSAGE, true);

		}

	}

	@Override
	public void undo(ModificaAbilitazioniAssegnaUtenteEsternoAction action, ModificaAbilitazioniAssegnaUtenteEsternoResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<ModificaAbilitazioniAssegnaUtenteEsternoAction> getActionType() {
		return ModificaAbilitazioniAssegnaUtenteEsternoAction.class;
	}
}
