package it.eng.portlet.consolepec.gwt.server.richiedifirma;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientTaskFirma;
import it.eng.consolepec.spagicclient.model.taskfirma.DestinatarioGruppoClient;
import it.eng.consolepec.spagicclient.model.taskfirma.DestinatarioTaskFirmaClient;
import it.eng.consolepec.spagicclient.model.taskfirma.DestinatarioUtenteTaskFirmaClient;
import it.eng.consolepec.spagicclient.model.taskfirma.RichiestaTaskFirmaResult;
import it.eng.consolepec.spagicclient.model.taskfirma.TipoRichiestaTaskFirmaClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.richiedifirma.RichiediFirmaVistoAction;
import it.eng.portlet.consolepec.gwt.shared.action.richiedifirma.RichiediFirmaVistoActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioGruppoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DestinatarioUtenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.richiedifirma.AllegatoRichiediFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.richiedifirma.RichiediFirmaVistoDTO;
import it.eng.portlet.consolepec.spring.bean.configurazioni.GestioneConfigurazioni;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.genericdata.IndirizzoEmailCacheSecondoLivello;

/**
 *
 * @author biagiot
 *
 */
public class RichiediFirmaVistoActionHandler implements ActionHandler<RichiediFirmaVistoAction, RichiediFirmaVistoActionResult> {

	private Logger logger = LoggerFactory.getLogger(RichiediFirmaVistoActionHandler.class);

	@Autowired SpagicClientTaskFirma spagicClientTaskFirma;

	@Autowired UserSessionUtil userSessionUtil;

	@Autowired PraticaSessionUtil praticaSessionUtil;

	@Autowired XMLPluginToDTOConverter xmlPluginToDTOConverter;

	@Autowired IndirizzoEmailCacheSecondoLivello indirizzoEmailCacheSecondoLivello;

	@Autowired GestioneConfigurazioni gestioneConfigurazioni;

	@Autowired GestioneProfilazioneUtente gestioneProfilazioneUtente;

	public RichiediFirmaVistoActionHandler() {

	}

	@Override
	public RichiediFirmaVistoActionResult execute(RichiediFirmaVistoAction action, ExecutionContext context) throws ActionException {
		logger.info("Start RichiediFirmaVistoActionHandler");

		RichiediFirmaVistoActionResult result = new RichiediFirmaVistoActionResult();
		result.setError(false);

		RichiediFirmaVistoDTO dto = action.getRichiediFirmaVisto();
		if (dto == null) {
			result.setError(true);
			result.setMessError(ConsolePecConstants.ERROR_MESSAGE);
			return result;
		}

		try {
			if (dto.getIndirizziEmailNotifica() != null) {
				for (String mail : dto.getIndirizziEmailNotifica()) {
					try {
						indirizzoEmailCacheSecondoLivello.checkIndirizzoEmail(mail, gestioneProfilazioneUtente.getDatiUtente());

					} catch (Exception e) {
						logger.warn("Errore durante il salvataggio degli indirizzi email", e);
					}
				}
			}

		} catch (Exception e) {
			logger.error("Salvataggio indirizzi di notifica non avvenuto", e);
		}

		try {

			String praticaPath = Base64Utils.URLdecodeAlfrescoPath(dto.getClientIdFascicolo());
			List<String> nomiAllegati = new ArrayList<String>();
			for (AllegatoRichiediFirmaDTO allegato : dto.getAllegati()) {
				nomiAllegati.add(allegato.getNome());
			}

			List<DestinatarioTaskFirmaClient> destinatari = new ArrayList<DestinatarioTaskFirmaClient>();

			for (DestinatarioDTO destinatario : dto.getDestinatari()) {
				destinatari.add(convertDestinatarioToClient(destinatario));
			}

			TipoRichiestaTaskFirmaClient tipo = TipoRichiestaTaskFirmaClient.valueOf(action.getRichiediFirmaVisto().getTipoRichiesta().name());
			String ruoloConsole = gestioneConfigurazioni.getAnagraficaRuoloByEtichetta(dto.getProponente().getNomeGruppo()).getRuolo();
			dto.getProponente().setNomeGruppo(ruoloConsole);

			RichiestaTaskFirmaResult richiestaResult = spagicClientTaskFirma.richiediFirmaVistoDocumenti(praticaPath, nomiAllegati, userSessionUtil.getUtenteSpagic(), tipo, dto.getOggettoDocumento(),
					dto.getProponente().getNomeGruppo(), destinatari, dto.getNote(), dto.getIndirizziEmailNotifica(), dto.getMittenteOriginale(), dto.getDataScadenza(), dto.getOraScadenza(),
					dto.getMinutoScadenza(), dto.getMotivazione());

			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(richiestaResult.getLockedPratica());
			FascicoloDTO fascicoloDTO = xmlPluginToDTOConverter.fascicoloToDettaglio(fascicolo);
			result.setPratica(fascicoloDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result.setError(true);
			result.setMessError(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result.setError(true);
			result.setMessError(ConsolePecConstants.ERROR_MESSAGE);
		}

		logger.info("End RichiediFirmaVistoActionHandler");
		return result;
	}

	private DestinatarioTaskFirmaClient convertDestinatarioToClient(DestinatarioDTO destinatarioDTO) {

		if (destinatarioDTO instanceof DestinatarioUtenteDTO) {
			DestinatarioUtenteDTO destinatario = (DestinatarioUtenteDTO) destinatarioDTO;

			if (destinatario.getUserId().equals(gestioneProfilazioneUtente.getDatiUtente().getUsername())) {
				throw new IllegalArgumentException("Destinatario non valido");
			}

			DestinatarioUtenteTaskFirmaClient result = new DestinatarioUtenteTaskFirmaClient(destinatario.getUserId(), destinatario.getNome(), destinatario.getCognome(), destinatario.getMatricola(),
					destinatario.getSettore());
			return result;

		} else if (destinatarioDTO instanceof DestinatarioGruppoDTO) {
			DestinatarioGruppoDTO destinatario = (DestinatarioGruppoDTO) destinatarioDTO;
			DestinatarioGruppoClient result = new DestinatarioGruppoClient(destinatario.getNomeGruppoConsole());
			return result;

		} else {
			throw new IllegalArgumentException("Tipo destinatario non valido");
		}

	}

	@Override
	public Class<RichiediFirmaVistoAction> getActionType() {
		return RichiediFirmaVistoAction.class;
	}

	@Override
	public void undo(RichiediFirmaVistoAction arg0, RichiediFirmaVistoActionResult arg1, ExecutionContext arg2) throws ActionException {}
}
