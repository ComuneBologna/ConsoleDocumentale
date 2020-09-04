package it.eng.portlet.consolepec.gwt.server.profilazione;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.GestionePreferenzeUtenteAction;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.GestionePreferenzeUtenteAction.TipoOperazione;
import it.eng.portlet.consolepec.gwt.shared.action.profilazione.GestionePreferenzeUtenteResult;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;

/**
 *
 * @author biagiot
 *
 */
public class GestionePreferenzeUtenteActionHandler implements ActionHandler<GestionePreferenzeUtenteAction, GestionePreferenzeUtenteResult> {

	private static final Logger logger = LoggerFactory.getLogger(GestionePreferenzeUtenteActionHandler.class);

	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	@Override
	public GestionePreferenzeUtenteResult execute(GestionePreferenzeUtenteAction action, ExecutionContext context) throws ActionException {
		logger.info("Inizio GestionePreferenzeUtenteActionHandler");

		GestionePreferenzeUtenteResult result = null;

		if (TipoOperazione.CARICA.equals(action.getTipoOperazione())) {
			logger.info("Caricamento preferenze utente");

			try {
				result = new GestionePreferenzeUtenteResult(gestioneProfilazioneUtente.getPreferenzeUtente(), gestioneProfilazioneUtente.getPreferenzeFirmaDigitale(),
						gestioneProfilazioneUtente.getPreferenzeRiassegnazione());

			} catch (SpagicClientException e) {
				logger.error("Errore", e);
				result = new GestionePreferenzeUtenteResult(e.getErrorMessage());

			} catch (Exception e) {
				logger.error("Errore imprevisto", e);
				result = new GestionePreferenzeUtenteResult(ConsolePecConstants.ERROR_MESSAGE);
			}

		} else if (TipoOperazione.AGGIORNA.equals(action.getTipoOperazione())) {

			try {

				if (action.getPreferenzeUtente() != null) {
					logger.info("Aggiornamento preferenze utente");
					gestioneProfilazioneUtente.aggiornaPreferenzeUtente(action.getPreferenzeUtente());
				}

				if (action.getPreferenzeFirmaDigitale() != null) {
					logger.info("Aggiornamento preferenze firma digitale");
					gestioneProfilazioneUtente.aggiornaPreferenzeFirmaDigitale(action.getPreferenzeFirmaDigitale());
				}

				if (action.getPreferenzeRiassegnazione() != null) {
					logger.info("Aggiornamento preferenze riassegnazione");
					gestioneProfilazioneUtente.aggiornaPreferenzeRiassegnazione(action.getPreferenzeRiassegnazione());
				}

				result = new GestionePreferenzeUtenteResult();

			} catch (SpagicClientException e) {
				logger.error("Errore", e);
				result = new GestionePreferenzeUtenteResult(e.getErrorMessage());

			} catch (Exception e) {
				logger.error("Errore imprevisto", e);
				result = new GestionePreferenzeUtenteResult(ConsolePecConstants.ERROR_MESSAGE);
			}

		} else if (TipoOperazione.ELIMINA.equals(action.getTipoOperazione())) {

			try {

				if (action.getPreferenzeFirmaDigitale() != null) {
					logger.info("Aggiornamento preferenze firma digitale");
					gestioneProfilazioneUtente.eliminaPreferenzeFirmaDigitale();
				}

				if (action.getPreferenzeRiassegnazione() != null) {
					logger.info("Aggiornamento preferenze riassegnazione");
					gestioneProfilazioneUtente.eliminaPreferenzeRiassegnazione();
				}

				result = new GestionePreferenzeUtenteResult();

			} catch (SpagicClientException e) {
				logger.error("Errore", e);
				result = new GestionePreferenzeUtenteResult(e.getErrorMessage());

			} catch (Exception e) {
				logger.error("Errore imprevisto", e);
				result = new GestionePreferenzeUtenteResult(ConsolePecConstants.ERROR_MESSAGE);
			}
		}

		logger.info("Fine GestionePreferenzeUtenteActionHandler");
		return result;
	}

	@Override
	public Class<GestionePreferenzeUtenteAction> getActionType() {
		return GestionePreferenzeUtenteAction.class;
	}

	@Override
	public void undo(GestionePreferenzeUtenteAction arg0, GestionePreferenzeUtenteResult arg1, ExecutionContext arg2) throws ActionException {}
}
