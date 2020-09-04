package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientPubblicazioneAllegato;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.RimozionePubblicazioneAllegati;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.RimozionePubblicazioneAllegatiResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class RimozionePubblicazioneAllegatiActionHandler implements ActionHandler<RimozionePubblicazioneAllegati, RimozionePubblicazioneAllegatiResult> {

	Logger logger = LoggerFactory.getLogger(RimozionePubblicazioneAllegatiActionHandler.class);

	@Autowired
	SpagicClientPubblicazioneAllegato spagicClientPubblicazioneAllegato;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	XMLPluginToDTOConverter utilPratica;

	public RimozionePubblicazioneAllegatiActionHandler() {}

	@Override
	public RimozionePubblicazioneAllegatiResult execute(RimozionePubblicazioneAllegati action, ExecutionContext context) throws ActionException {
		try {
			logger.debug("Inizio action handler di rimozione pubblicazione allegati");
			String praticaPath = Base64Utils.URLdecodeAlfrescoPath(action.getPraticaPath());
			String allegatoNome = action.getNomeAllegato();
			logger.debug("Invocazione client spagic");
			LockedPratica lockedPratica = spagicClientPubblicazioneAllegato.rimuoviPubblicazioneAllegato(praticaPath, allegatoNome, userSessionUtil.getUtenteSpagic());
			logger.debug("Recupero il fascicolo");
			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			logger.debug("Caricato il Fascicolo: {}", fascicolo.getDati());
			FascicoloDTO fascicoloDTO = utilPratica.fascicoloToDettaglio(fascicolo);
			logger.debug("Ricaricato il dto fascicolo: {} ", fascicoloDTO);
			RimozionePubblicazioneAllegatiResult result = new RimozionePubblicazioneAllegatiResult(fascicoloDTO);
			return result;

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new RimozionePubblicazioneAllegatiResult(null, e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new RimozionePubblicazioneAllegatiResult(null, ConsolePecConstants.ERROR_MESSAGE, true);

		} finally {
			logger.debug("Fine");
		}
	}

	@Override
	public void undo(RimozionePubblicazioneAllegati action, RimozionePubblicazioneAllegatiResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<RimozionePubblicazioneAllegati> getActionType() {
		return RimozionePubblicazioneAllegati.class;
	}
}
