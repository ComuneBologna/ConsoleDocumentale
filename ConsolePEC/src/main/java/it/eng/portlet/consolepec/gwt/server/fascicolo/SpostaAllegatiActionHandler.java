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

import it.eng.consolepec.client.SpostaAllegatiClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SpostaAllegatiAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.SpostaAllegatiResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class SpostaAllegatiActionHandler implements ActionHandler<SpostaAllegatiAction, SpostaAllegatiResult> {

	private static final Logger logger = LoggerFactory.getLogger(SpostaAllegatiActionHandler.class);

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	SpostaAllegatiClient spostaAllegatiClient;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	@Override
	public SpostaAllegatiResult execute(SpostaAllegatiAction action, ExecutionContext context) throws ActionException {
		logger.info("Inizio esecuzione servizio di spostamento allegati");

		try {
			if (action == null || action.getAllegatiDaSpostare().isEmpty() || Strings.isNullOrEmpty(action.getFascicoloDestinatarioClientID())
					|| Strings.isNullOrEmpty(action.getFascicoloSorgenteClientID())) {
				return new SpostaAllegatiResult(ConsolePecConstants.ERROR_MESSAGE);
			}

			String idDocumentaleSorgente = Base64Utils.URLdecodeAlfrescoPath(action.getFascicoloSorgenteClientID());
			String idDocumentaleDestinatario = Base64Utils.URLdecodeAlfrescoPath(action.getFascicoloDestinatarioClientID());
			List<String> allegati = new ArrayList<String>();

			for (AllegatoDTO a : action.getAllegatiDaSpostare()) {
				allegati.add(a.getNome());
			}

			LockedPratica lockedPraticaDestinataria = spostaAllegatiClient.spostaAllegati(idDocumentaleSorgente, idDocumentaleDestinatario, allegati, userSessionUtil.getUtenteSpagic());

			// Ricarico la pratica sorgente
			praticaSessionUtil.loadPraticaFromEncodedPath(action.getFascicoloSorgenteClientID(), TipologiaCaricamento.RICARICA);

			// Restituisco la pratica destinataria
			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPraticaDestinataria);
			FascicoloDTO fascicoloDTO = xmlPluginToDTOConverter.fascicoloToDettaglio(fascicolo);

			return new SpostaAllegatiResult(fascicoloDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new SpostaAllegatiResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new SpostaAllegatiResult(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.info("Fine esecuzione servizio di spostamento allegati");

		}
	}

	@Override
	public Class<SpostaAllegatiAction> getActionType() {
		return SpostaAllegatiAction.class;
	}

	@Override
	public void undo(SpostaAllegatiAction action, SpostaAllegatiResult result, ExecutionContext context) throws ActionException {}

}
