package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneFascicoloAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.CondizioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.consolepec.spagicclient.SpagicClientCreateFascicolo;
import it.eng.consolepec.spagicclient.bean.request.fascicolo.FascicoloRequest;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.converters.requestfascicolo.RequestFascicoloConverter;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CreaFascicoloActionHandler implements ActionHandler<CreaFascicoloAction, CreaFascicoloActionResult> {

	@Autowired
	SpagicClientCreateFascicolo spagicClientCreateFascicolo;
	@Autowired
	XMLPluginToDTOConverter util;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	GestioneConfigurazioni gestioneConfigurazioni;
	@Autowired
	RequestFascicoloConverter requestFascicoloConverter;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	private final Logger logger = LoggerFactory.getLogger(CreaFascicoloActionHandler.class);

	public CreaFascicoloActionHandler() {

	}

	@Override
	public CreaFascicoloActionResult execute(CreaFascicoloAction action, ExecutionContext context) throws ActionException {

		logger.debug("CreaFascicoloActionResult {}");
		CreaFascicoloActionResult actionResult = new CreaFascicoloActionResult();
		actionResult.setError(false);
		actionResult.setMessageError("");

		if (action.getTipologiaFascicolo() == null) {
			actionResult.setError(true);
			actionResult.setMessageError("Tipologia fascicolo non valida");
			return actionResult;
		}

		final AnagraficaFascicolo af = gestioneConfigurazioni.getAnagraficaFascicolo(action.getTipologiaFascicolo().getNomeTipologia());
		if (af == null || af.getStato().equals(Stato.DISATTIVA)) {
			actionResult.setError(true);
			actionResult.setMessageError("Fascicolo non attivo");
			return actionResult;
		}

		QueryAbilitazione<CreazioneFascicoloAbilitazione> qb = new QueryAbilitazione<CreazioneFascicoloAbilitazione>();
		qb.addCondition(new CondizioneAbilitazione<CreazioneFascicoloAbilitazione>() {

			@Override
			protected boolean valutaCondizione(CreazioneFascicoloAbilitazione abilitazione) {
				return abilitazione.getTipo().equals(af.getNomeTipologia());
			}
		});

		if (!gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneFascicoloAbilitazione.class, qb)) {
			actionResult.setError(true);
			actionResult.setMessageError("Utente non abilitato alla creazione di " + af.getNomeTipologia());
			return actionResult;
		}

		try {
			LockedPratica lockedPratica = null;
			action.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(action.getAssegnatario()).getRuolo());
			FascicoloRequest fascicoloRequest = requestFascicoloConverter.convertToFascicoloRequest(action);

			lockedPratica = spagicClientCreateFascicolo.createFascicolo(fascicoloRequest, userSessionUtil.getUtenteSpagic());

			logger.info("FASCICOLO: {}", lockedPratica.getMetadatiXml());

			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			FascicoloDTO fascicoloDTO = util.fascicoloToDettaglio(fascicolo);
			actionResult.setFascicoloDTO(fascicoloDTO);

			praticaSessionUtil.removePraticaFromEncodedPath(action.getClientID());

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			actionResult.setError(true);
			actionResult.setMessageError(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			actionResult.setError(true);
			actionResult.setMessageError(ConsolePecConstants.ERROR_MESSAGE);
		}

		return actionResult;
	}

	@Override
	public void undo(CreaFascicoloAction action, CreaFascicoloActionResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<CreaFascicoloAction> getActionType() {

		return CreaFascicoloAction.class;
	}
}
