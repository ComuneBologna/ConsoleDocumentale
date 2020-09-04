package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientGestioneFascicolo;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.EliminaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.EliminaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneTaskPratiche;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class EliminaFascicoloActionHandler implements ActionHandler<EliminaFascicolo, EliminaFascicoloResult> {

	Logger logger = LoggerFactory.getLogger(EliminaFascicoloActionHandler.class);

	@Autowired
	XMLPluginToDTOConverter praticheUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	SpagicClientGestioneFascicolo spagicClientGestioneFascicolo;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneTaskPratiche gestioneTask;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	public EliminaFascicoloActionHandler() {}

	@Override
	public EliminaFascicoloResult execute(EliminaFascicolo action, ExecutionContext context) throws ActionException {
		EliminaFascicoloResult result = null;
		try {
			String decPath = Base64Utils.URLdecodeAlfrescoPath(action.getPathFascicolo());
			logger.debug("Eliminazione Fascicolo. pathFascicoloClient: {}", decPath);
			Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(action.getPathFascicolo(), TipologiaCaricamento.CARICA);
			// GestioneFascicoloTask gestioneFascicoloTask = praticheUtil.estraiGestioneFascicoloTaskCorrente(fascicolo);
			TaskFascicolo<?> gestioneFascicoloTask = gestioneTask.estraiTaskCorrente(fascicolo, TaskFascicolo.class, gestioneProfilazioneUtente.getDatiUtente().getAnagraficheRuoli());
			if (gestioneFascicoloTask != null && gestioneFascicoloTask.controllaAbilitazione(TipoApiTask.ELIMINA_FASCICOLO)) {
				spagicClientGestioneFascicolo.elimina(decPath, userSessionUtil.getUtenteSpagic());
				logger.debug("Eliminazione Fascicolo. Eliminazione terminata correttamente.");
				result = new EliminaFascicoloResult("", true);
			} else {
				result = new EliminaFascicoloResult("Fasicolo non eliminabile", false);
			}
			praticaSessionUtil.removePraticaFromEncodedPath(action.getPathFascicolo());

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new EliminaFascicoloResult(e.getErrorMessage(), false);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new EliminaFascicoloResult("Errore nell'eliminazione del fascicolo", false);
		}

		return result;
	}

	@Override
	public void undo(EliminaFascicolo action, EliminaFascicoloResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<EliminaFascicolo> getActionType() {
		return EliminaFascicolo.class;
	}
}
