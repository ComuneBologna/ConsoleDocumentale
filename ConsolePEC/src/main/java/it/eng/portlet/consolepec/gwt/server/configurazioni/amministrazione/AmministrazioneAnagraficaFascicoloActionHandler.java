package it.eng.portlet.consolepec.gwt.server.configurazioni.amministrazione;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.client.AmministrazioneAnagraficaFascicoloClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.server.rest.RestClientInvoker;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaFascicoloAction.Operazione;
import it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione.AmministrazioneAnagraficaFascicoloResult;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author biagiot
 *
 */
public class AmministrazioneAnagraficaFascicoloActionHandler implements ActionHandler<AmministrazioneAnagraficaFascicoloAction, AmministrazioneAnagraficaFascicoloResult> {

	private static final Logger logger = LoggerFactory.getLogger(AmministrazioneAnagraficaFascicoloActionHandler.class);

	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	AmministrazioneAnagraficaFascicoloClient amministrazioneAnagraficaFascicoloClient;

	@Autowired
	RestClientInvoker restClientInvoker;

	@Override
	public AmministrazioneAnagraficaFascicoloResult execute(AmministrazioneAnagraficaFascicoloAction action, ExecutionContext arg1) throws ActionException {
		AmministrazioneAnagraficaFascicoloResult result = null;

		if (action.getOperazione() == null) {
			return new AmministrazioneAnagraficaFascicoloResult("Nessuna operazione specificata");
		}

		if (!Operazione.RICERCA.equals(action.getOperazione()) && (action.getAnagraficaFascicolo() == null || action.getAnagraficaFascicolo().getNomeTipologia() == null
				|| action.getAnagraficaFascicolo().getNomeTipologia().trim().isEmpty() || action.getAnagraficaFascicolo().getStato() == null
				|| action.getAnagraficaFascicolo().getDettaglioNameToken() == null || action.getAnagraficaFascicolo().getDettaglioNameToken().trim().isEmpty())) {

			return new AmministrazioneAnagraficaFascicoloResult("Fascicolo non valido");
		}

		try {

			logger.info("Inizio operazione {}", action.getOperazione().toString());

			switch (action.getOperazione()) {
			case INSERIMENTO:

				if (!PraticaUtil.validaEtichettaFascicolo(action.getAnagraficaFascicolo().getEtichettaTipologia())
						|| !PraticaUtil.validaNomeTipologiaFascicolo(action.getAnagraficaFascicolo().getNomeTipologia())) {

					result = new AmministrazioneAnagraficaFascicoloResult("Nome tipologia fascicolo non valido");

				} else if (gestioneConfigurazioni.getAnagraficaFascicolo(action.getAnagraficaFascicolo().getNomeTipologia()) != null

						|| gestioneConfigurazioni.getAnagraficaFascicoloByEtichetta(action.getAnagraficaFascicolo().getEtichettaTipologia()) != null) {

					result = new AmministrazioneAnagraficaFascicoloResult("Il fascicolo " + action.getAnagraficaFascicolo().getEtichettaTipologia() + " esiste già");

				} else {

					if (action.getAzioni() != null) {
						for (Azione a : action.getAzioni()) {
							a.setData(new Date());
							a.setUsernameUtente(userSessionUtil.getUtenteSpagic().getUsername());
						}

						action.getAnagraficaFascicolo().getAzioni().addAll(action.getAzioni());
					}

					result = new AmministrazioneAnagraficaFascicoloResult(amministrazioneAnagraficaFascicoloClient.crea(action.getAnagraficaFascicolo(), userSessionUtil.getUtenteSpagic()));

					try {
						restClientInvoker.refreshAPI();

					} catch (Exception e) {
						logger.error("Errore durante il refresh dei microservizi del documentale", e);
					}

					gestioneConfigurazioni.reloadAnagraficheFascicoli(userSessionUtil.getUtenteSpagic());
				}

				break;

			case MODIFICA:
				if (!PraticaUtil.validaEtichettaFascicolo(action.getAnagraficaFascicolo().getEtichettaTipologia())
						|| !PraticaUtil.validaNomeTipologiaFascicolo(action.getAnagraficaFascicolo().getNomeTipologia())) {

					result = new AmministrazioneAnagraficaFascicoloResult("Nome tipologia fascicolo non valido");

				} else {
					if (action.getAzioni() != null) {
						for (Azione a : action.getAzioni()) {
							a.setData(new Date());
							a.setUsernameUtente(userSessionUtil.getUtenteSpagic().getUsername());
						}

						action.getAnagraficaFascicolo().getAzioni().addAll(action.getAzioni());
					}

					result = new AmministrazioneAnagraficaFascicoloResult(amministrazioneAnagraficaFascicoloClient.modifica(action.getAnagraficaFascicolo(), userSessionUtil.getUtenteSpagic()));

					try {
						restClientInvoker.refreshAPI();

					} catch (Exception e) {
						logger.error("Errore durante il refresh dei microservizi del documentale", e);
					}

					gestioneConfigurazioni.reloadAnagraficheFascicoli(userSessionUtil.getUtenteSpagic());
				}

				break;

			case RICERCA:
				result = new AmministrazioneAnagraficaFascicoloResult(amministrazioneAnagraficaFascicoloClient.ricerca(action.getFiltri(), action.getLimit(), action.getOffset(), action.getOrderBy(),
						action.getSort(), userSessionUtil.getUtenteSpagic()), amministrazioneAnagraficaFascicoloClient.count(action.getFiltri(), userSessionUtil.getUtenteSpagic()));
				break;

			default:
				result = new AmministrazioneAnagraficaFascicoloResult(ConsolePecConstants.ERROR_MESSAGE);
				break;
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new AmministrazioneAnagraficaFascicoloResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new AmministrazioneAnagraficaFascicoloResult(ConsolePecConstants.ERROR_MESSAGE);
		}

		return result;
	}

	@Override
	public Class<AmministrazioneAnagraficaFascicoloAction> getActionType() {
		return AmministrazioneAnagraficaFascicoloAction.class;
	}

	@Override
	public void undo(AmministrazioneAnagraficaFascicoloAction arg0, AmministrazioneAnagraficaFascicoloResult arg1, ExecutionContext arg2) throws ActionException {}

}
