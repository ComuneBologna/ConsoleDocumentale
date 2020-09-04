package it.eng.portlet.consolepec.gwt.server.template;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.GestioneEmailOutClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaBozzaDaTemplateAction;
import it.eng.portlet.consolepec.gwt.shared.action.template.CreaBozzaDaTemplateActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CreaBozzaDaTemplateActionHandler extends CreaGenericoDaTemplateActionHandler<CreaBozzaDaTemplateAction, CreaBozzaDaTemplateActionResult> {

	private static final Logger logger = LoggerFactory.getLogger(CreaBozzaDaTemplateActionHandler.class);

	@Autowired
	GestioneEmailOutClient gestioneEmailOutClient;

	@Autowired
	UserSessionUtil userSessionUtil;

	public CreaBozzaDaTemplateActionHandler() {}

	@Override
	protected CreaBozzaDaTemplateActionResult getResult(CreaBozzaDaTemplateAction action) throws ActionException {

		try {

			String pathFascicolo = decodePathFascicolo(action);
			String pathTemplate = decodePathTemplate(action);

			Map<String, Object> campi = new LinkedHashMap<String, Object>();
			for (Entry<String, String> entry : action.getValori().entrySet()) {
				campi.put(entry.getKey(), entry.getValue());
			}

			LockedPratica lockedPratica = gestioneEmailOutClient.creaBozzaDaTemplate(pathFascicolo, pathTemplate, campi, null, null, userSessionUtil.getUtenteSpagic());

			PecOutDTO bozzaDTO = ricaricaBozza(lockedPratica);
			Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(action.getPathFascicolo(), TipologiaCaricamento.RICARICA);
			FascicoloDTO fascicoloDTO = utilPratica.fascicoloToDettaglio(fascicolo);
			return new CreaBozzaDaTemplateActionResult(fascicoloDTO, bozzaDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new CreaBozzaDaTemplateActionResult(true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new CreaBozzaDaTemplateActionResult(true, ConsolePecConstants.ERROR_MESSAGE);

		}

	}

	protected PecOutDTO ricaricaBozza(LockedPratica lockedPratica) {

		logger.debug("Carico la bozza appena creata");
		PraticaEmailOut bozza = (PraticaEmailOut) praticaSessionUtil.loadPraticaInSessione(lockedPratica);
		logger.debug("Caricata la PraticaEmail: {}", bozza.getDati());
		PecOutDTO bozzaDTO = utilPratica.emailToDettaglioOUT(bozza);
		logger.debug("Caricato il dto pecout: {}", bozzaDTO);
		return bozzaDTO;
	}

	@Override
	public void undo(CreaBozzaDaTemplateAction action, CreaBozzaDaTemplateActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CreaBozzaDaTemplateAction> getActionType() {
		return CreaBozzaDaTemplateAction.class;
	}
}
