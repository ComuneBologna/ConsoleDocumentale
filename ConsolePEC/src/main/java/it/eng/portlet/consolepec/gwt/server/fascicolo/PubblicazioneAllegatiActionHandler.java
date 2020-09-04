package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.Date;
import java.util.List;

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
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.PubblicazioneAllegati;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.PubblicazioneAllegatiResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class PubblicazioneAllegatiActionHandler implements ActionHandler<PubblicazioneAllegati, PubblicazioneAllegatiResult> {

	Logger logger = LoggerFactory.getLogger(PubblicazioneAllegatiActionHandler.class);

	@Autowired
	SpagicClientPubblicazioneAllegato spagicClientPubblicazioneAllegato;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	XMLPluginToDTOConverter utilPratica;

	public PubblicazioneAllegatiActionHandler() {}

	@Override
	public PubblicazioneAllegatiResult execute(PubblicazioneAllegati action, ExecutionContext context) throws ActionException {
		try {
			logger.debug("Inizio action handler di pubblicazione allegati");
			String praticaPath = Base64Utils.URLdecodeAlfrescoPath(action.getPraticaPath());
			String allegatoNome = action.getNomeAllegato();
			Date dataInizio = action.getDataInizio();
			Date dataFine = action.getDataFine();
			List<String> destinatari = action.getDestinatariEmail(); // può essere nullo, il caso è gestito dal service
			String testo = action.getTestoEmail(); // può essere nullo, il caso è gestito dal service
			logger.debug("Invocazione client spagic");
			LockedPratica lockedPratica = spagicClientPubblicazioneAllegato.pubblicaAllegato(praticaPath, allegatoNome, dataInizio, dataFine, destinatari, testo, userSessionUtil.getUtenteSpagic());
			logger.debug("Recupero il fascicolo");
			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			logger.debug("Caricato il Fascicolo: {}", fascicolo.getDati());
			FascicoloDTO fascicoloDTO = utilPratica.fascicoloToDettaglio(fascicolo);
			logger.debug("Ricaricato il dto fascicolo: {} ", fascicoloDTO);
			PubblicazioneAllegatiResult result = new PubblicazioneAllegatiResult(fascicoloDTO, allegatoNome);
			return result;

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new PubblicazioneAllegatiResult(null, null, e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new PubblicazioneAllegatiResult(null, null, ConsolePecConstants.ERROR_MESSAGE, true);

		} finally {
			logger.debug("Fine");
		}
	}

	@Override
	public void undo(PubblicazioneAllegati action, PubblicazioneAllegatiResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<PubblicazioneAllegati> getActionType() {
		return PubblicazioneAllegati.class;
	}
}
