package it.eng.portlet.consolepec.gwt.server.pec;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.GestioneEmailOutClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ReinoltroAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ReinoltroResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCacheSecondoLivello;

public class ReinoltroActionHandler implements ActionHandler<ReinoltroAction, ReinoltroResult> {

	private static final Logger logger = LoggerFactory.getLogger(ReinoltroActionHandler.class);

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	GestioneEmailOutClient gestioneEmailOutClient;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	@Autowired
	GestioneTaskPratiche gestioneTask;

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Autowired
	GestioneEmailOutClient inviaBozzaClient;

	@Autowired
	IndirizzoEmailCacheSecondoLivello indirizzoEmailCacheSecondoLivello;

	public ReinoltroActionHandler() {}

	@Override
	public ReinoltroResult execute(ReinoltroAction action, ExecutionContext context) throws ActionException {
		ReinoltroResult reinoltroResult = null;

		try {
			PraticaEmailOut emailOriginale = praticaSessionUtil.loadPraticaEmailOutFromEncodedPath(action.getIdMailOriginale(), TipologiaCaricamento.RICARICA);

			List<String> allegati = new ArrayList<String>();
			for (Allegato a : emailOriginale.getDati().getAllegati()) {
				allegati.add(a.getNome());
			}

			LockedPratica lockedPratica = gestioneEmailOutClient.creaBozza(emailOriginale.getFascicoliCollegati().get(0).getAlfrescoPath(), null, emailOriginale.getDati().getMittente(),
					action.getDestinatari(), action.getCopia(), emailOriginale.getDati().getOggetto(), emailOriginale.getDati().getBody(), emailOriginale.getDati().getFirma(),
					emailOriginale.getDati().getIdDocumentale(), allegati, emailOriginale.getAlfrescoPath(), userSessionUtil.getUtenteSpagic());

			PraticaEmailOut bozza = (PraticaEmailOut) praticaSessionUtil.loadPraticaInSessione(lockedPratica);

			lockedPratica = inviaBozzaClient.inviaBozza(bozza.getDati().getIdDocumentale(), userSessionUtil.getUtenteSpagic());
			Pratica<?> pratica = praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			PecOutDTO pecOut = utilPratica.emailToDettaglioOUT((PraticaEmailOut) pratica);
			praticaSessionUtil.removePraticaFromEncodedPath(pecOut.getClientID());

			praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(emailOriginale.getFascicoliCollegati().get(0).getAlfrescoPath()));
			praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(emailOriginale.getAlfrescoPath()));
			reinoltroResult = new ReinoltroResult(pecOut);

			try {
				for (String destinatario : action.getDestinatari()) {
					indirizzoEmailCacheSecondoLivello.checkIndirizzoEmail(destinatario, gestioneProfilazioneUtente.getDatiUtente());
				}

				for (String destinatario : action.getCopia()) {
					indirizzoEmailCacheSecondoLivello.checkIndirizzoEmail(destinatario, gestioneProfilazioneUtente.getDatiUtente());
				}

			} catch (Exception e) {
				logger.warn("Errore durante il salvataggio degli indirizzi email", e);
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			reinoltroResult = new ReinoltroResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			reinoltroResult = new ReinoltroResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		return reinoltroResult;
	}

	@Override
	public void undo(ReinoltroAction action, ReinoltroResult result, ExecutionContext context) throws ActionException {
		//
	}

	@Override
	public Class<ReinoltroAction> getActionType() {
		return ReinoltroAction.class;
	}
}
