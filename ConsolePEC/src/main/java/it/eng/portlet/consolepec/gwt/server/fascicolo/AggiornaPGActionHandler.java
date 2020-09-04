package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.spagicclient.SpagicClientGestioneFascicolo;
import it.eng.consolepec.spagicclient.bean.request.Protocollazione;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverterUtil;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AggiornaPG;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AggiornaPGResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

/**
 * Servizio generico di recupero ruoli di riassegnazione e creazione pratica
 *
 */
public class AggiornaPGActionHandler implements ActionHandler<AggiornaPG, AggiornaPGResult> {

	private static Logger logger = LoggerFactory.getLogger(AggiornaPGActionHandler.class);

	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	XMLPluginToDTOConverter xmlConverter;
	@Autowired
	XMLPluginToDTOConverterUtil util;

	@Autowired
	SpagicClientGestioneFascicolo spagicClientGestioneFascicolo;

	public AggiornaPGActionHandler() {}

	@Override
	public AggiornaPGResult execute(AggiornaPG action, ExecutionContext context) throws ActionException {
		try {

			String pathFascicolo = Base64Utils.URLdecodeAlfrescoPath(action.getFascicoloPath());

			logger.debug("AggiornaPG fascicolo: " + pathFascicolo);

			List<Protocollazione> protocollazioni = new ArrayList<Protocollazione>();

			for (ElementoGruppoProtocollato pg : action.getProtocollazioni()) {
				protocollazioni.add(new Protocollazione(pg.getNumeroPG(), util.safeCast(pg.getAnnoPG(), Integer.class)));
			}

			LockedPratica lockedPratica = spagicClientGestioneFascicolo.aggiornaPG(pathFascicolo, protocollazioni, userSessionUtil.getUtenteSpagic());

			Fascicolo fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(lockedPratica);

			FascicoloDTO fascicoloDTO = xmlConverter.fascicoloToDettaglio(fascicolo);
			logger.debug("Ricaricato il dto fascicolo: {} ", fascicoloDTO);

			return new AggiornaPGResult(fascicoloDTO);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new AggiornaPGResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new AggiornaPGResult(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.debug("Fine aggiorna PG");
		}
	}

	@Override
	public Class<AggiornaPG> getActionType() {
		return AggiornaPG.class;
	}

	@Override
	public void undo(AggiornaPG action, AggiornaPGResult result, ExecutionContext context) throws ActionException {}

}
