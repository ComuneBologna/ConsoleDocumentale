package it.eng.portlet.consolepec.gwt.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.action.FirmaAllegatoPecOutBozzaAction;
import it.eng.portlet.consolepec.gwt.shared.action.FirmaAllegatoPecOutBozzaActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.firma.GestioneFirma;
import it.eng.portlet.consolepec.spring.firma.impl.CredenzialiFirma;

public class FirmaAllegatoPecOutBozzaActionHandler implements ActionHandler<FirmaAllegatoPecOutBozzaAction, FirmaAllegatoPecOutBozzaActionResult> {

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	XMLPluginToDTOConverterUtil xmlPluginToDTOConverterUtil;

	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	GestioneFirma gestioneFirma;

	Logger logger = LoggerFactory.getLogger(FirmaAllegatoPecOutBozzaActionHandler.class);

	public FirmaAllegatoPecOutBozzaActionHandler() {}

	@Override
	public FirmaAllegatoPecOutBozzaActionResult execute(final FirmaAllegatoPecOutBozzaAction action, ExecutionContext context) throws ActionException {
		FirmaAllegatoPecOutBozzaActionResult result = null;
		try {

			String bozzaPath = Base64Utils.URLdecodeAlfrescoPath(action.getClientID());
			String idDocumentale = XmlPluginUtil.convertIdDocumentaleFromAlfrescoPath(bozzaPath);

			logger.debug("Richiesta firma allegati bozza: {} username_liferay:{} numero allegati: {}", idDocumentale, userSessionUtil.getUtenteSpagic().getUsername(),
					action.getAllegati() != null ? action.getAllegati().length : 0);

			/* traduco credenziali firma gwt in credenziali spring */
			CredenzialiFirma cf = new CredenzialiFirma(action.getCredenzialiFirma().isCredenzialeUsernameModificata() ? action.getCredenzialiFirma().getUsername() : null,
					action.getCredenzialiFirma().isCredenzialePasswordModificata() ? action.getCredenzialiFirma().getPassword() : null, action.getCredenzialiFirma().getOtp(),
					action.getCredenzialiFirma().isSalvaCredenziali());
			List<String> allegati = xmlPluginToDTOConverterUtil.getAllegatiDaFirmare(action.getAllegati());

			Pratica<?> pratica = gestioneFirma.firmaDocumento(idDocumentale, allegati, cf, action.getTipologiaFirma(), true);
			PecOutDTO dto = xmlPluginToDTOConverter.emailToDettaglioOUT((PraticaEmailOut) pratica);
			result = new FirmaAllegatoPecOutBozzaActionResult(dto, null, false);

		} catch (ApplicationException exc) {
			logger.error(exc.getLocalizedMessage(), exc);
			result = new FirmaAllegatoPecOutBozzaActionResult(null, exc.getMessage(), true);

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			result = new FirmaAllegatoPecOutBozzaActionResult(null, e.getMessage(), true);
		}

		return result;

	}

	@Override
	public void undo(FirmaAllegatoPecOutBozzaAction action, FirmaAllegatoPecOutBozzaActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<FirmaAllegatoPecOutBozzaAction> getActionType() {
		return FirmaAllegatoPecOutBozzaAction.class;
	}
}
