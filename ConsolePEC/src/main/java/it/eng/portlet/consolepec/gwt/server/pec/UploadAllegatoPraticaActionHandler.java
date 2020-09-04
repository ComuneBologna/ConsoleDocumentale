package it.eng.portlet.consolepec.gwt.server.pec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 * Gestisce l'aggiunta di un allegato alla pecout, in caso di file su FS o da pratica coll
 *
 * @author pluttero
 *
 */
public class UploadAllegatoPraticaActionHandler implements ActionHandler<UploadAllegatoPraticaAction, UploadAllegatoPraticaResult> {

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	@Autowired
	IGestioneAllegati gestAllg;
	@Autowired
	UserSessionUtil gestSess;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	Logger logger = LoggerFactory.getLogger(UploadAllegatoPraticaActionHandler.class);

	public UploadAllegatoPraticaActionHandler() {

	}

	@Override
	public UploadAllegatoPraticaResult execute(UploadAllegatoPraticaAction action, ExecutionContext context) throws ActionException {

		logger.debug("Richiesto upload allegato/i su pecout user: {}", gestSess.getUtenteSpagic().getUsername());
		String encPath = action.getClientID();
		Pratica<?> pratica = null;
		try {
			if (action.getAllegtiDaAltrePratiche() == null) {
				for (TmpFileUploadDTO tmpFile : action.getTmpFiles()) {
					logger.debug("Richiesto upload tempdir: {} allegato: {} su bozza: {}", tmpFile.getDirName(), tmpFile.getFileName(), action.getClientID());
					pratica = gestAllg.aggiungiAllegatoFromFileSystem(encPath, tmpFile.getDirName(), tmpFile.getFileName());
				}
			} else {
				for (String clientID : action.getAllegtiDaAltrePratiche().keySet()) {
					logger.debug("carico allegati da pratica: {}", clientID);
					for (AllegatoDTO allg : action.getAllegtiDaAltrePratiche().get(clientID)) {
						logger.debug("Carico allegato: {}", allg.getNome());
						pratica = gestAllg.aggiungiAllegatoFromPratica(action.getClientID(), clientID, allg.getNome());
						logger.debug("Allegato caricato");
					}
				}
			}
			logger.debug("Termine caricamento su email: {}", pratica.getDati());

			PraticaDTO dto = praticaToDettaglio(pratica);

			praticaSessionUtil.removePraticaFromEncodedPath(dto.getClientID());
			// PecOutDTO dto = utilPratica.emailToDettaglioOUT(pratica);
			return new UploadAllegatoPraticaResult("", false, dto);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new UploadAllegatoPraticaResult(e.getErrorMessage(), true, null);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new UploadAllegatoPraticaResult(ConsolePecConstants.ERROR_MESSAGE, true, null);

		}
	}

	private PraticaDTO praticaToDettaglio(Pratica<?> pratica) {
		return utilPratica.praticaToDTO(pratica);
	}

	@Override
	public void undo(UploadAllegatoPraticaAction action, UploadAllegatoPraticaResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<UploadAllegatoPraticaAction> getActionType() {

		return UploadAllegatoPraticaAction.class;
	}
}
