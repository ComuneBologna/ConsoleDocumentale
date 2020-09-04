package it.eng.portlet.consolepec.gwt.server.configurazioni.amministrazione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaIngressoResponse;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.consolepec.client.AmministrazioneAnagraficaIngressoClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.server.rest.RestClientInvoker;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaIngressoAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaIngressoAction.Operazione;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaIngressoResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author biagiot
 *
 */
public class AmministrazioneAnagraficaIngressoActionHandler implements ActionHandler<AmministrazioneAnagraficaIngressoAction, AmministrazioneAnagraficaIngressoResult> {

	private static final Logger logger = LoggerFactory.getLogger(AmministrazioneAnagraficaIngressoActionHandler.class);

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	AmministrazioneAnagraficaIngressoClient amministrazioneAnagraficaIngressoClient;

	@Autowired
	RestClientInvoker restClientInvoker;

	@Override
	public AmministrazioneAnagraficaIngressoResult execute(AmministrazioneAnagraficaIngressoAction action, ExecutionContext arg1) throws ActionException {
		AmministrazioneAnagraficaIngressoResult result = null;

		if (action.getOperazione() == null) {
			return new AmministrazioneAnagraficaIngressoResult("Nessuna operazione specificata");
		}

		if ((Operazione.MODIFICA.equals(action.getOperazione()) || Operazione.INSERIMENTO.equals(action.getOperazione())) && //
				(action.getAnagraficaIngresso() == null //
						|| action.getAnagraficaIngresso().getIndirizzo() == null || action.getAnagraficaIngresso().getIndirizzo().trim().isEmpty() //
						|| action.getAnagraficaIngresso().getServer() == null || action.getAnagraficaIngresso().getServer().trim().isEmpty() //
						|| action.getAnagraficaIngresso().getUtenza() == null || action.getAnagraficaIngresso().getUtenza().trim().isEmpty() //
						|| action.getAnagraficaIngresso().getFolderIn() == null || action.getAnagraficaIngresso().getFolderIn().trim().isEmpty() //
						|| action.getAnagraficaIngresso().getFolderTo() == null || action.getAnagraficaIngresso().getFolderTo().trim().isEmpty())) {

			return new AmministrazioneAnagraficaIngressoResult("Ingresso non valido");
		}

		try {

			logger.info("Inizio operazione {}", action.getOperazione().toString());

			switch (action.getOperazione()) {
			case INSERIMENTO:

				if (gestioneConfigurazioni.getAnagraficaIngresso(action.getAnagraficaIngresso().getNomeTipologia(), action.getAnagraficaIngresso().getIndirizzo()) != null) {
					result = new AmministrazioneAnagraficaIngressoResult("L'ingresso " + action.getAnagraficaIngresso().getIndirizzo() + " esiste già");

				} else if (!ValidationUtilities.validateEmailAddress(action.getAnagraficaIngresso().getIndirizzo())) {
					result = new AmministrazioneAnagraficaIngressoResult("Indirizzo email non valido");

				} else {
					AnagraficaIngressoResponse ar = amministrazioneAnagraficaIngressoClient.crea(action.getAnagraficaIngresso(), action.isCreaEmailOut(), userSessionUtil.getUtenteSpagic());
					result = new AmministrazioneAnagraficaIngressoResult(ar);

					if (ar.getAnagraficaIngresso() != null) {

						try {
							restClientInvoker.refreshAPI();

						} catch (Exception e) {
							logger.error("Errore durante il refresh dei microservizi del documentale", e);
						}

						gestioneConfigurazioni.reloadAnagraficheIngressi(userSessionUtil.getUtenteSpagic());
					}
				}

				break;

			case MODIFICA:

				if (!ValidationUtilities.validateEmailAddress(action.getAnagraficaIngresso().getIndirizzo())) {
					result = new AmministrazioneAnagraficaIngressoResult("Indirizzo email non valido");

				} else {
					AnagraficaIngressoResponse ar = amministrazioneAnagraficaIngressoClient.modifica(action.getAnagraficaIngresso().getIndirizzo(), action.getAnagraficaIngresso(),
							userSessionUtil.getUtenteSpagic());
					result = new AmministrazioneAnagraficaIngressoResult(ar);

					if (ar.getAnagraficaIngresso() != null) {

						try {
							restClientInvoker.refreshAPI();

						} catch (Exception e) {
							logger.error("Errore durante il refresh dei microservizi del documentale", e);
						}

						gestioneConfigurazioni.reloadAnagraficheIngressi(userSessionUtil.getUtenteSpagic());
					}
				}

				break;

			case RICERCA:
				result = new AmministrazioneAnagraficaIngressoResult(amministrazioneAnagraficaIngressoClient.ricerca(action.getFiltri(), action.getLimit(), action.getOffset(), action.getOrderBy(),
						action.getSort(), userSessionUtil.getUtenteSpagic()), amministrazioneAnagraficaIngressoClient.count(action.getFiltri(), userSessionUtil.getUtenteSpagic()));
				break;

			case AGGIORNA_PRIMO_ASSEGNATARIO:

				if (action.getTipologiaEmail() == null || action.getTipologiaEmail().isEmpty() || action.getIndirizzoEmail() == null || action.getIndirizzoEmail().isEmpty()) {
					return new AmministrazioneAnagraficaIngressoResult("Tipologia o indirizzo mail non valido");
				}

				if (action.getNuovoAssegnatario() == null || action.getNuovoAssegnatario().isEmpty()) {
					return new AmministrazioneAnagraficaIngressoResult("Assegnatario non valido");
				}

				AnagraficaIngresso ai = amministrazioneAnagraficaIngressoClient.aggiornaPrimoAssegnatario(action.getTipologiaEmail(), action.getIndirizzoEmail(), action.getNuovoAssegnatario(),
						userSessionUtil.getUtenteSpagic());
				AnagraficaIngressoResponse resp = new AnagraficaIngressoResponse();
				resp.setAnagraficaIngresso(ai);
				result = new AmministrazioneAnagraficaIngressoResult(resp);
				gestioneConfigurazioni.reloadAbilitazioniRuoli(userSessionUtil.getUtenteSpagic());
				gestioneConfigurazioni.reloadAnagraficheIngressi(userSessionUtil.getUtenteSpagic());
				break;

			default:
				result = new AmministrazioneAnagraficaIngressoResult(ConsolePecConstants.ERROR_MESSAGE);
				break;
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new AmministrazioneAnagraficaIngressoResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new AmministrazioneAnagraficaIngressoResult(ConsolePecConstants.ERROR_MESSAGE);

		}

		return result;
	}

	@Override
	public Class<AmministrazioneAnagraficaIngressoAction> getActionType() {
		return AmministrazioneAnagraficaIngressoAction.class;
	}

	@Override
	public void undo(AmministrazioneAnagraficaIngressoAction arg0, AmministrazioneAnagraficaIngressoResult arg1, ExecutionContext arg2) throws ActionException {}

}
