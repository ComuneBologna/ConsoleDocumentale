package it.eng.portlet.consolepec.gwt.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientGestioneAllegatoPratica;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.server.pec.CancellaAllegatoPecOutActionHandler;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.CancellaAllegatoPratica;
import it.eng.portlet.consolepec.gwt.shared.action.CancellaAllegatoPraticaResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CancellaAllegatoPraticaActionHandler implements ActionHandler<CancellaAllegatoPratica, CancellaAllegatoPraticaResult> {
	@Autowired
	SpagicClientGestioneAllegatoPratica spagicClientGestioneAllegatoPratica;

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	UserSessionUtil userSessionUtil;

	Logger logger = LoggerFactory.getLogger(CancellaAllegatoPecOutActionHandler.class);

	public CancellaAllegatoPraticaActionHandler() {

	}

	@Override
	public CancellaAllegatoPraticaResult execute(CancellaAllegatoPratica action, ExecutionContext context) throws ActionException {

		Pratica<?> pratica = null;
		for (AllegatoDTO allegato : action.getAllegati()) {
			try {
				pratica = praticaSessionUtil.loadPraticaFromEncodedPath(action.getClientID(), TipologiaCaricamento.CARICA);
				String path = pratica.getAlfrescoPath();
				LockedPratica removeFile = spagicClientGestioneAllegatoPratica.removeFile(path, allegato.getNome(), userSessionUtil.getUtenteSpagic());
				pratica = praticaSessionUtil.loadPraticaInSessione(removeFile);

			} catch (SpagicClientException e) {
				logger.error("Errore", e);
				return new CancellaAllegatoPraticaResult(e.getErrorMessage(), true, null);

			} catch (Exception e) {
				logger.error("Errore imprevisto", e);
				return new CancellaAllegatoPraticaResult(ConsolePecConstants.ERROR_MESSAGE, true, null);
			}
		}

		PraticaDTO praticaDTO = utilPratica.praticaToDTO(pratica);
		return new CancellaAllegatoPraticaResult("", false, praticaDTO);
	}

	@Override
	public void undo(CancellaAllegatoPratica action, CancellaAllegatoPraticaResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<CancellaAllegatoPratica> getActionType() {

		return CancellaAllegatoPratica.class;
	}
}
