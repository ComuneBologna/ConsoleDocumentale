package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.GestioneEmailOutClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaRisposta;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaRispostaResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CreaRispostaActionHandler implements ActionHandler<CreaRisposta, CreaRispostaResult> {

	private static final Logger logger = LoggerFactory.getLogger(CreaRispostaActionHandler.class);

	@Autowired
	GestioneEmailOutClient gestioneEmailOutClient;

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	public CreaRispostaActionHandler() {}

	@Override
	public CreaRispostaResult execute(CreaRisposta action, ExecutionContext context) throws ActionException {

		try {
			String decPath = Base64Utils.URLdecodeAlfrescoPath(action.getPathFascicolo());
			LockedPratica lockedPratica = null;

			if (action.getIdMailSelezionata() != null) {
				PraticaEmailIn praticaEmailIn = praticaSessionUtil.loadPraticaEmailInFromEncodedPath(action.getIdMailSelezionata(), TipologiaCaricamento.CARICA);
				String intestazione = "<br><br><br><p>------------------------------<br> Per conto di: " + praticaEmailIn.getDati().getMittente() + " ha scritto:</p>";
				String bodyString = praticaEmailIn.getDati().getBody() != null ? praticaEmailIn.getDati().getBody() : "";

				lockedPratica = gestioneEmailOutClient.creaBozza(decPath, null, null, Arrays.asList(praticaEmailIn.getDati().getMittente()), null, "RE: " + praticaEmailIn.getDati().getOggetto(),
						intestazione + bodyString, gestioneProfilazioneUtente.getPreferenzeUtente().getFirmaEmail(), praticaEmailIn.getDati().getIdDocumentale(), null, null,
						userSessionUtil.getUtenteSpagic());
			} else {
				lockedPratica = gestioneEmailOutClient.creaBozza(decPath, null, null, null, null, null, null, gestioneProfilazioneUtente.getPreferenzeUtente().getFirmaEmail(), null, null, null,
						userSessionUtil.getUtenteSpagic());
			}

			PraticaEmailOut bozza = (PraticaEmailOut) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			PecOutDTO bozzaDTO = utilPratica.emailToDettaglioOUT(bozza);

			Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(action.getPathFascicolo(), TipologiaCaricamento.RICARICA);
			FascicoloDTO fascicoloDTO = utilPratica.fascicoloToDettaglio(fascicolo);
			CreaRispostaResult result = new CreaRispostaResult(fascicoloDTO, bozzaDTO, null, false);

			return result;

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new CreaRispostaResult(null, null, e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new CreaRispostaResult(null, null, "Errore nella creazione della bozza", true);

		}

	}

	@Override
	public void undo(CreaRisposta action, CreaRispostaResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CreaRisposta> getActionType() {
		return CreaRisposta.class;
	}
}
