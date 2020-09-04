package it.eng.portlet.consolepec.gwt.server.inviomassivo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneComunicazioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.CondizioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.consolepec.spagicclient.SpagicClientGestioneComunicazioniMassive;
import it.eng.consolepec.spagicclient.bean.request.inviomassivo.ComunicazioneRequest;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.Comunicazione;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.server.fascicolo.CreaFascicoloActionHandler;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.CreaComunicazioneAction;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.CreaComunicazioneActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.converters.requestfascicolo.RequestFascicoloConverter;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CreaComunicazioneActionHandler implements ActionHandler<CreaComunicazioneAction, CreaComunicazioneActionResult> {

	@Autowired
	SpagicClientGestioneComunicazioniMassive spagicClientGestioneComunicazioniMassive;
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

	public CreaComunicazioneActionHandler() {

	}

	@Override
	public Class<CreaComunicazioneAction> getActionType() {

		return CreaComunicazioneAction.class;
	}

	@Override
	public CreaComunicazioneActionResult execute(CreaComunicazioneAction action, ExecutionContext context) throws ActionException {
		logger.debug("CreaFascicoloActionResult {}");

		final AnagraficaComunicazione af = gestioneConfigurazioni.getAnagraficaComunicazione(TipologiaPratica.COMUNICAZIONE.getNomeTipologia());
		if (af == null || af.getStato().equals(Stato.DISATTIVA)) {
			return new CreaComunicazioneActionResult("Comunicazione non attiva");
		}

		QueryAbilitazione<CreazioneComunicazioneAbilitazione> qb = new QueryAbilitazione<CreazioneComunicazioneAbilitazione>();
		qb.addCondition(new CondizioneAbilitazione<CreazioneComunicazioneAbilitazione>() {

			@Override
			protected boolean valutaCondizione(CreazioneComunicazioneAbilitazione abilitazione) {
				return abilitazione.getTipo().equals(af.getNomeTipologia());
			}
		});

		if (!gestioneProfilazioneUtente.getAutorizzazioniUtente().isAbilitato(CreazioneComunicazioneAbilitazione.class, qb)) {
			return new CreaComunicazioneActionResult("Utente non abilitato alla creazione della comunicazione");
		}

		try {

			ComunicazioneDTO comunicazioneDTO = action.getComunicazione();
			comunicazioneDTO.setAssegnatario(gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(comunicazioneDTO.getAssegnatario()).getRuolo());
			ComunicazioneRequest comunicazioneRequest = requestFascicoloConverter.convertComunicazioneRequest(comunicazioneDTO);

			LockedPratica lockedPratica = spagicClientGestioneComunicazioniMassive.createComunicazione(comunicazioneRequest, userSessionUtil.getUtenteSpagic());

			logger.info("COMUNICAZIONE: {}", lockedPratica.getMetadatiXml());

			Comunicazione comunicazione = (Comunicazione) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			comunicazioneDTO = util.comunicazioneToDettaglio(comunicazione);

			praticaSessionUtil.removePraticaFromEncodedPath(comunicazioneDTO.getClientID());

			return new CreaComunicazioneActionResult(comunicazioneDTO);
		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new CreaComunicazioneActionResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new CreaComunicazioneActionResult(ConsolePecConstants.ERROR_MESSAGE);

		}

	}

	@Override
	public void undo(CreaComunicazioneAction action, CreaComunicazioneActionResult result, ExecutionContext context) throws ActionException {

	}
}
