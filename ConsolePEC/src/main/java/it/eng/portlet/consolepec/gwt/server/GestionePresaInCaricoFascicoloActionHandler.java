package it.eng.portlet.consolepec.gwt.server;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.GestionePresaInCaricoFascicoloAction;
import it.eng.portlet.consolepec.gwt.shared.action.GestionePresaInCaricoFascicoloActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestionePresaInCarico;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

public class GestionePresaInCaricoFascicoloActionHandler implements ActionHandler<GestionePresaInCaricoFascicoloAction, GestionePresaInCaricoFascicoloActionResult> {
	@Autowired
	IGestionePresaInCarico gestionePresaInCarico;
	@Autowired
	XMLPluginToDTOConverter util;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	private Logger logger = LoggerFactory.getLogger(GestionePresaInCaricoFascicoloActionHandler.class);

	public GestionePresaInCaricoFascicoloActionHandler() {}

	@Override
	public GestionePresaInCaricoFascicoloActionResult execute(GestionePresaInCaricoFascicoloAction action, ExecutionContext context) throws ActionException {
		GestionePresaInCaricoFascicoloActionResult result = new GestionePresaInCaricoFascicoloActionResult();
		boolean hasInCarico = false;
		String id = action.getClientID();

		try {
			hasInCarico = gestionePresaInCarico.hasInCaricoUtenteCorrente(id);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result.setError(true);
			result.setErrorMsg(e.getErrorMessage());
			return result;

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result.setError(true);
			result.setErrorMsg(ConsolePecConstants.ERROR_MESSAGE);
			return result;
		}

		try {
			Pratica<?> pratica;
			if (hasInCarico) {
				pratica = gestionePresaInCarico.rilascia(id);
			} else {
				pratica = gestionePresaInCarico.prendiInCarico(id);
			}

			// in caso di fascicolo scarico di nuovo tutte le pratiche ad esso collegate
			if (pratica instanceof Fascicolo) {
				Fascicolo fascicolo = (Fascicolo) pratica;
				List<PraticaCollegata> praticheCollegate = fascicolo.getAllPraticheCollegate();
				for (PraticaCollegata p : praticheCollegate) {
					praticaSessionUtil.removePraticaFromEncodedPath(Base64Utils.URLencodeAlfrescoPath(p.getAlfrescoPath()));
				}
			}

			PraticaDTO praticaDTO = util.praticaToDTO(pratica);

			result.setPraticaDTO(praticaDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result.setError(true);
			result.setErrorMsg(e.getErrorMessage());
			return result;

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result.setError(true);
			result.setErrorMsg(ConsolePecConstants.ERROR_MESSAGE);
			return result;
		}

		return result;
	}

	@Override
	public void undo(GestionePresaInCaricoFascicoloAction action, GestionePresaInCaricoFascicoloActionResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<GestionePresaInCaricoFascicoloAction> getActionType() {
		return GestionePresaInCaricoFascicoloAction.class;
	}
}
