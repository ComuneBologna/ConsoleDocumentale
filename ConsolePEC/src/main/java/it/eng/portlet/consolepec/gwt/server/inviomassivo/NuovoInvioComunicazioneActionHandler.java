package it.eng.portlet.consolepec.gwt.server.inviomassivo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientGestioneComunicazioniMassive;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.Comunicazione;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.server.fascicolo.CreaFascicoloActionHandler;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.NuovoInvioComunicazioneAction;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.NuovoInvioComunicazioneActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.spring.bean.converters.requestfascicolo.RequestFascicoloConverter;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class NuovoInvioComunicazioneActionHandler implements ActionHandler<NuovoInvioComunicazioneAction, NuovoInvioComunicazioneActionResult> {

	@Autowired
	SpagicClientGestioneComunicazioniMassive spagicClientGestioneComunicazioniMassive;
	@Autowired
	XMLPluginToDTOConverter util;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	RequestFascicoloConverter requestFascicoloConverter;

	private final Logger logger = LoggerFactory.getLogger(CreaFascicoloActionHandler.class);

	public NuovoInvioComunicazioneActionHandler() {

	}

	@Override
	public Class<NuovoInvioComunicazioneAction> getActionType() {

		return NuovoInvioComunicazioneAction.class;
	}

	@Override
	public NuovoInvioComunicazioneActionResult execute(NuovoInvioComunicazioneAction action, ExecutionContext context) throws ActionException {
		logger.debug("CreaFascicoloActionResult {}");

		try {

			ComunicazioneDTO comunicazioneDTO = action.getComunicazione();

			String pathComunicazione = Base64Utils.URLdecodeAlfrescoPath(comunicazioneDTO.getClientID());

			LockedPratica lockedPratica;

			if (action.isTest()) {
				lockedPratica = spagicClientGestioneComunicazioniMassive.creaInvioTest(pathComunicazione, action.getAllegato().getNome(), action.getDestinatarioTest(), action.getNumeroTest(),
						userSessionUtil.getUtenteSpagic());
			} else {
				lockedPratica = spagicClientGestioneComunicazioniMassive.creaInvio(pathComunicazione, action.getAllegato().getNome(), userSessionUtil.getUtenteSpagic());
			}

			logger.info("COMUNICAZIONE: {}", lockedPratica.getMetadatiXml());

			Comunicazione comunicazione = (Comunicazione) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			comunicazioneDTO = util.comunicazioneToDettaglio(comunicazione);

			praticaSessionUtil.removePraticaFromEncodedPath(comunicazioneDTO.getClientID());

			return new NuovoInvioComunicazioneActionResult(comunicazioneDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new NuovoInvioComunicazioneActionResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new NuovoInvioComunicazioneActionResult(ConsolePecConstants.ERROR_MESSAGE);

		}

	}

	@Override
	public void undo(NuovoInvioComunicazioneAction action, NuovoInvioComunicazioneActionResult result, ExecutionContext context) throws ActionException {

	}
}
