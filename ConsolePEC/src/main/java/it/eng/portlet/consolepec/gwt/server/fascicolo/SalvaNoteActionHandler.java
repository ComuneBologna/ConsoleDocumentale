package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.ModificaNoteClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.SalvaNoteAction;
import it.eng.portlet.consolepec.gwt.shared.action.SalvaNoteResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class SalvaNoteActionHandler implements ActionHandler<SalvaNoteAction, SalvaNoteResult> {

	private static final Logger logger = LoggerFactory.getLogger(SalvaNoteActionHandler.class);

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	ModificaNoteClient modificaNoteClient;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	@Autowired
	XMLPluginToDTOConverter xmlPluginToDTOConverter;

	@Override
	public SalvaNoteResult execute(SalvaNoteAction action, ExecutionContext context) throws ActionException {

		String idDocumentale = Base64Utils.URLdecodeAlfrescoPath(action.getClientIdFascicolo());
		logger.info("Inizio esecuzione salvataggio note per fascicolo: {}", idDocumentale);

		try {
			LockedPratica result = modificaNoteClient.modificaNote(idDocumentale, action.getNote(), userSessionUtil.getUtenteSpagic());
			praticaSessionUtil.loadPraticaFromEncodedPath(action.getClientIdFascicolo(), TipologiaCaricamento.RICARICA);
			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(result);
			FascicoloDTO fascicoloDTO = xmlPluginToDTOConverter.fascicoloToDettaglio(fascicolo);
			return new SalvaNoteResult(fascicoloDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new SalvaNoteResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new SalvaNoteResult(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.info("Fine esecuzione salvataggio note per fascicolo: {}", idDocumentale);
		}
	}

	@Override
	public Class<SalvaNoteAction> getActionType() {
		return SalvaNoteAction.class;
	}

	@Override
	public void undo(SalvaNoteAction action, SalvaNoteResult result, ExecutionContext context) throws ActionException {}

}
