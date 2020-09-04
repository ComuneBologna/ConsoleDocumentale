package it.eng.portlet.consolepec.gwt.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientOperatore;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaOperatore;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaOperatoreResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class ModificaOperatoreActionHandler implements ActionHandler<ModificaOperatore, ModificaOperatoreResult> {

	private static Logger logger = LoggerFactory.getLogger(ModificaOperatoreActionHandler.class);

	@Autowired
	SpagicClientOperatore spagicClientOperatore;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	XMLPluginToDTOConverter util;

	public ModificaOperatoreActionHandler() {}

	@Override
	public ModificaOperatoreResult execute(ModificaOperatore action, ExecutionContext context) throws ActionException {
		try {
			String clientID = action.getPratica().getClientID();
			Pratica<?> pratica = praticaSessionUtil.loadPraticaFromEncodedPath(clientID);
			LockedPratica praticaLock = spagicClientOperatore.modifica(pratica.getAlfrescoPath(), action.getOperatore(), userSessionUtil.getUtenteSpagic());

			praticaSessionUtil.removePraticaFromEncodedPath(clientID);

			pratica = praticaSessionUtil.loadPraticaInSessione(praticaLock);

			PraticaDTO praticaDTO = util.praticaToDTO(pratica);

			return new ModificaOperatoreResult(praticaDTO);

		} catch (SpagicClientException e) {
			return new ModificaOperatoreResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new ModificaOperatoreResult(ConsolePecConstants.ERROR_MESSAGE);

		}

	}

	@Override
	public void undo(ModificaOperatore action, ModificaOperatoreResult result, ExecutionContext context) throws ActionException {

	}

	@Override
	public Class<ModificaOperatore> getActionType() {
		return ModificaOperatore.class;
	}

}
