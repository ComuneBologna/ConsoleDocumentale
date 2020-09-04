package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.impl.SpagicClientGestioneAllegatoPraticaRemoteProxy;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaTipologiaAllegato;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CambiaTipologiaAllegatoResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 * Servizio generico di recupero ruoli di riassegnazione e creazione pratica
 *
 */
public class CambiaTipologiaAllegatoActionHandler implements ActionHandler<CambiaTipologiaAllegato, CambiaTipologiaAllegatoResult> {

	private static Logger logger = LoggerFactory.getLogger(CambiaStepIterActionHandler.class);

	@Autowired
	SpagicClientGestioneAllegatoPraticaRemoteProxy spagicClientGestioneAllegatoPraticaRemoteProxy;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	XMLPluginToDTOConverter utilPratica;

	public CambiaTipologiaAllegatoActionHandler() {}

	@Override
	public CambiaTipologiaAllegatoResult execute(CambiaTipologiaAllegato action, ExecutionContext context) throws ActionException {
		try {

			String pathFascicolo = Base64Utils.URLdecodeAlfrescoPath(action.getFascicoloPath());
			String tipologiaAllegato = action.getTipologiaAllegato();

			List<String> nomiAllegati = action.getNomiAllegati();

			logger.debug("Cambio tipologia allegato. fascicolo:" + pathFascicolo);

			LockedPratica lockedPratica = spagicClientGestioneAllegatoPraticaRemoteProxy.cambiaTipologiaAllegato(pathFascicolo, nomiAllegati, tipologiaAllegato, userSessionUtil.getUtenteSpagic());

			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);

			FascicoloDTO fascicoloDTO = utilPratica.fascicoloToDettaglio(fascicolo);
			logger.debug("Ricaricato il dto fascicolo: {} ", fascicoloDTO);

			return new CambiaTipologiaAllegatoResult(fascicoloDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new CambiaTipologiaAllegatoResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new CambiaTipologiaAllegatoResult(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.debug("Fine cambio Tipologia Allegato");
		}
	}

	@Override
	public Class<CambiaTipologiaAllegato> getActionType() {
		return CambiaTipologiaAllegato.class;
	}

	@Override
	public void undo(CambiaTipologiaAllegato action, CambiaTipologiaAllegatoResult result, ExecutionContext context) throws ActionException {}

}
