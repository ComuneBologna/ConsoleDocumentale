package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.SpostaProtocollazioniClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SpostaProtocollazioniAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SpostaProtocollazioniResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class SpostaProtocollazioniActionHandler implements ActionHandler<SpostaProtocollazioniAction, SpostaProtocollazioniResult> {

	private static final Logger logger = LoggerFactory.getLogger(SpostaProtocollazioniActionHandler.class);

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	SpostaProtocollazioniClient spostaProtocollazioniClient;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	@Override
	public SpostaProtocollazioniResult execute(SpostaProtocollazioniAction action, ExecutionContext context) throws ActionException {
		logger.info("Inizio esecuzione servizio di spostamento protocollazioni: {}", action);

		try {
			if (action == null || (action.getAllegatiProtocollati().isEmpty() && action.getElementiProtocollati().isEmpty()) || Strings.isNullOrEmpty(action.getFascicoloDestinatarioClientID())
					|| Strings.isNullOrEmpty(action.getFascicoloSorgenteClientID())) {
				return new SpostaProtocollazioniResult(ConsolePecConstants.ERROR_MESSAGE);
			}

			String idDocumentaleSorgente = Base64Utils.URLdecodeAlfrescoPath(action.getFascicoloSorgenteClientID());
			String idDocumentaleDestinatario = Base64Utils.URLdecodeAlfrescoPath(action.getFascicoloDestinatarioClientID());
			List<String> allegatiProtocollati = new ArrayList<String>();
			List<String> praticheProtocollate = new ArrayList<String>();

			for (AllegatoDTO a : action.getAllegatiProtocollati()) {
				allegatiProtocollati.add(a.getNome());
			}

			for (ElementoElenco el : action.getElementiProtocollati()) {

				if (el instanceof ElementoPECRiferimento) {
					praticheProtocollate.add(Base64Utils.URLdecodeAlfrescoPath(((ElementoPECRiferimento) el).getRiferimento()));

				} else if (el instanceof ElementoPraticaModulisticaRiferimento) {
					praticheProtocollate.add(Base64Utils.URLdecodeAlfrescoPath(((ElementoPraticaModulisticaRiferimento) el).getRiferimento()));

				} else {
					logger.warn("Tipo di elemento non gestito: {}", el.getClass());
				}
			}

			LockedPratica lockedPraticaDestinataria = spostaProtocollazioniClient.spostaProtocollazioni(idDocumentaleSorgente, idDocumentaleDestinatario, allegatiProtocollati, praticheProtocollate,
					userSessionUtil.getUtenteSpagic());

			// Ricarico la pratica sorgente
			praticaSessionUtil.loadPraticaFromEncodedPath(action.getFascicoloSorgenteClientID(), TipologiaCaricamento.RICARICA);

			// Restituisco la pratica destinataria
			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPraticaDestinataria);
			FascicoloDTO fascicoloDTO = xmlPluginToDTOConverter.fascicoloToDettaglio(fascicolo);

			return new SpostaProtocollazioniResult(fascicoloDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new SpostaProtocollazioniResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new SpostaProtocollazioniResult(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.info("Fine esecuzione servizio di spostamento protocollazioni: {}", action);

		}
	}

	@Override
	public Class<SpostaProtocollazioniAction> getActionType() {
		return SpostaProtocollazioniAction.class;
	}

	@Override
	public void undo(SpostaProtocollazioniAction action, SpostaProtocollazioniResult result, ExecutionContext context) throws ActionException {}

}
