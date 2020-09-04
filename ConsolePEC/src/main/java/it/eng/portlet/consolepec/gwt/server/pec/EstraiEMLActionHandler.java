package it.eng.portlet.consolepec.gwt.server.pec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Strings;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.EstraiEMLPecClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.pec.EstraiEMLAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.EstraiEMLResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 *
 * @author biagiot
 *
 */
public class EstraiEMLActionHandler implements ActionHandler<EstraiEMLAction, EstraiEMLResult> {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	EstraiEMLPecClient estraiEMLPecClient;

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	public EstraiEMLActionHandler() {

	}

	@Override
	public EstraiEMLResult execute(EstraiEMLAction action, ExecutionContext context) throws ActionException {
		logger.info("Start EstraiEMLActionHandler");

		try {
			if (Strings.isNullOrEmpty(action.getPecPath()) || Strings.isNullOrEmpty(action.getPraticaPath())) {
				return new EstraiEMLResult("Path della pratica e/o della pec non presente");
			}

			String pecPath = Base64Utils.URLdecodeAlfrescoPath(action.getPecPath());
			String praticaPath = Base64Utils.URLdecodeAlfrescoPath(action.getPraticaPath());
			LockedPratica lockedPratica = estraiEMLPecClient.estraiEMLPec(pecPath, praticaPath, userSessionUtil.getUtenteSpagic());

			Pratica<?> pratica = praticaSessionUtil.loadPraticaInSessione(lockedPratica);
			PraticaDTO praticaDTO = xmlPluginToDTOConverter.praticaToDTO(pratica);

			logger.info("End EstraiEMLActionHandler");
			return new EstraiEMLResult(praticaDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new EstraiEMLResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new EstraiEMLResult(ConsolePecConstants.ERROR_MESSAGE);

		}

	}

	@Override
	public Class<EstraiEMLAction> getActionType() {
		return EstraiEMLAction.class;
	}

	@Override
	public void undo(EstraiEMLAction action, EstraiEMLResult result, ExecutionContext context) throws ActionException {}
}
