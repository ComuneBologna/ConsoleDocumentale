package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverterUtil;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.FirmaAllegatoFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.FirmaAllegatoFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.firma.GestioneFirma;
import it.eng.portlet.consolepec.spring.firma.impl.CredenzialiFirma;

public class FirmaAllegatoFascicoloActionHandler implements ActionHandler<FirmaAllegatoFascicoloAction, FirmaAllegatoFascicoloResult> {

	private final Logger logger = LoggerFactory.getLogger(FirmaAllegatoFascicoloActionHandler.class);

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

	public FirmaAllegatoFascicoloActionHandler() {}

	@Override
	public FirmaAllegatoFascicoloResult execute(FirmaAllegatoFascicoloAction action, ExecutionContext context) throws ActionException {
		FirmaAllegatoFascicoloResult result = null;
		try {

			String fascicoloPath = Base64Utils.URLdecodeAlfrescoPath(action.getClientID());
			String idDocumentale = XmlPluginUtil.convertIdDocumentaleFromAlfrescoPath(fascicoloPath);

			logger.debug("Richiesta firma allegati fascicolo: {} username_liferay:{} numero allegati: {}", idDocumentale, userSessionUtil.getUtenteSpagic().getUsername(),
					action.getAllegati() != null ? action.getAllegati().length : 0);

			/* traduco credenziali firma gwt in credenziali spring */
			CredenzialiFirma cf = translateCredential(action);
			List<String> allegati = xmlPluginToDTOConverterUtil.getAllegatiDaFirmare(action.getAllegati());

			Pratica<?> pratica = gestioneFirma.firmaDocumento(idDocumentale, allegati, cf, action.getTipologiaFirma(), false);
			FascicoloDTO dto = xmlPluginToDTOConverter.fascicoloToDettaglio((Fascicolo) pratica);
			result = new FirmaAllegatoFascicoloResult(dto, null, false);

		} catch (ApplicationException exc) {
			logger.error(exc.getLocalizedMessage(), exc);
			result = new FirmaAllegatoFascicoloResult(null, exc.getMessage(), true);

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
			result = new FirmaAllegatoFascicoloResult(null, ConsolePecConstants.ERROR_MESSAGE, true);
		}

		return result;
	}

	private CredenzialiFirma translateCredential(FirmaAllegatoFascicoloAction action) {
		CredenzialiFirma cf = new CredenzialiFirma(action.getCredenzialiFirma().isCredenzialeUsernameModificata() ? action.getCredenzialiFirma().getUsername() : null,
				action.getCredenzialiFirma().isCredenzialePasswordModificata() ? action.getCredenzialiFirma().getPassword() : null, action.getCredenzialiFirma().getOtp(),
				action.getCredenzialiFirma().isSalvaCredenziali());
		return cf;
	}

	@Override
	public void undo(FirmaAllegatoFascicoloAction action, FirmaAllegatoFascicoloResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<FirmaAllegatoFascicoloAction> getActionType() {
		return FirmaAllegatoFascicoloAction.class;
	}
}
