package it.eng.portlet.consolepec.gwt.server.pec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.ModificaNoteClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.SalvaNotePecInAction;
import it.eng.portlet.consolepec.gwt.shared.action.SalvaNotePecInResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class SalvaNotePecInActionHandler implements ActionHandler<SalvaNotePecInAction, SalvaNotePecInResult> {

	private static final Logger logger = LoggerFactory.getLogger(SalvaNotePecInActionHandler.class);

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	ModificaNoteClient modificaNoteClient;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	@Override
	public SalvaNotePecInResult execute(SalvaNotePecInAction action, ExecutionContext context) throws ActionException {

		String idDocumentale = Base64Utils.URLdecodeAlfrescoPath(action.getClientIdPecIn());
		logger.info("Inizio esecuzione salvataggio note per pec in: {}", idDocumentale);

		try {
			LockedPratica result = modificaNoteClient.modificaNote(idDocumentale, action.getNote(), userSessionUtil.getUtenteSpagic());
			praticaSessionUtil.loadPraticaFromEncodedPath(action.getClientIdPecIn(), TipologiaCaricamento.RICARICA);
			PraticaEmailIn email = (PraticaEmailIn) praticaSessionUtil.loadPraticaInSessione(result);
			PecInDTO pecDto = xmlPluginToDTOConverter.emailToDettaglioIN(email);
			return new SalvaNotePecInResult(pecDto);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new SalvaNotePecInResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new SalvaNotePecInResult(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.info("Fine esecuzione salvataggio note per pec in: {}", idDocumentale);
		}
	}

	@Override
	public Class<SalvaNotePecInAction> getActionType() {
		return SalvaNotePecInAction.class;
	}

	@Override
	public void undo(SalvaNotePecInAction action, SalvaNotePecInResult result, ExecutionContext context) throws ActionException {}

}
