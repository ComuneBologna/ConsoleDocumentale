package it.eng.portlet.consolepec.gwt.server.pec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientGestioneAllegatoPratica;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CancellaAllegatoPecOut;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CancellaAllegatoPecOutResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CancellaAllegatoPecOutActionHandler implements ActionHandler<CancellaAllegatoPecOut, CancellaAllegatoPecOutResult> {
	@Autowired
	SpagicClientGestioneAllegatoPratica spagicClientGestioneAllegatoPratica;

	@Autowired
	XMLPluginToDTOConverter utilPratica;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	UserSessionUtil userSessionUtil;

	Logger logger = LoggerFactory.getLogger(CancellaAllegatoPecOutActionHandler.class);

	public CancellaAllegatoPecOutActionHandler() {

	}

	@Override
	public CancellaAllegatoPecOutResult execute(CancellaAllegatoPecOut action, ExecutionContext context) throws ActionException {

		PraticaEmailOut email = null;
		for (AllegatoDTO allegato : action.getAllegati()) {
			try {
				email = praticaSessionUtil.loadPraticaEmailOutFromEncodedPath(action.getClientID(), TipologiaCaricamento.CARICA);
				String path = email.getAlfrescoPath();
				LockedPratica removeFile = spagicClientGestioneAllegatoPratica.removeFile(path, allegato.getNome(), userSessionUtil.getUtenteSpagic());
				email = (PraticaEmailOut) praticaSessionUtil.loadPraticaInSessione(removeFile);

			} catch (SpagicClientException e) {
				logger.error("Errore", e);
				return new CancellaAllegatoPecOutResult(e.getErrorMessage(), true, null);

			} catch (Exception e) {
				logger.error("Errore imprevisto", e);
				return new CancellaAllegatoPecOutResult(ConsolePecConstants.ERROR_MESSAGE, true, null);
			}
		}

		PecOutDTO pecOut = utilPratica.emailToDettaglioOUT(email);
		return new CancellaAllegatoPecOutResult("", false, pecOut);
	}

	@Override
	public void undo(CancellaAllegatoPecOut action, CancellaAllegatoPecOutResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<CancellaAllegatoPecOut> getActionType() {

		return CancellaAllegatoPecOut.class;
	}
}
