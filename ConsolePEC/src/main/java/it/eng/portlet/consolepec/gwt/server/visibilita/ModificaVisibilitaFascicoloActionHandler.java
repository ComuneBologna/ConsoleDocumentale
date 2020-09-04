package it.eng.portlet.consolepec.gwt.server.visibilita;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.client.CambiaVisibilitaFascicoloClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaVisibilitaFascicolo;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaVisibilitaFascicoloResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class ModificaVisibilitaFascicoloActionHandler implements ActionHandler<ModificaVisibilitaFascicolo, ModificaVisibilitaFascicoloResult> {

	Logger logger = LoggerFactory.getLogger(ModificaVisibilitaFascicoloActionHandler.class);

	@Autowired
	XMLPluginToDTOConverter utilPratica;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	CambiaVisibilitaFascicoloClient cambiaVisibilitaFascicoloClient;

	public ModificaVisibilitaFascicoloActionHandler() {}

	@Override
	public ModificaVisibilitaFascicoloResult execute(ModificaVisibilitaFascicolo action, ExecutionContext context) throws ActionException {
		logger.debug("Modifica visibilita della pratica {}", action.getClientID());
		Fascicolo fascicolo;
		try {
			fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(action.getClientID(), TipologiaCaricamento.CARICA);
			logger.debug("Caricato fascicolo: {}", fascicolo.getDati());

			List<String> listaGruppi = getListString(action.getGruppiVisibilita());

			LockedPratica pratica = cambiaVisibilitaFascicoloClient.cambiaVisibilitaFascicolo(fascicolo.getDati().getIdDocumentale(), listaGruppi, userSessionUtil.getUtenteSpagic());
			fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(pratica);

			FascicoloDTO dto = utilPratica.fascicoloToDettaglio(fascicolo);

			praticaSessionUtil.removePraticaFromEncodedPath(action.getClientID());

			ModificaVisibilitaFascicoloResult modificaVisibilitaFascicoloResult = new ModificaVisibilitaFascicoloResult();
			modificaVisibilitaFascicoloResult.setFascicoloDTO(dto);

			return modificaVisibilitaFascicoloResult;

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			ModificaVisibilitaFascicoloResult modificaVisibilitaFascicoloResult = new ModificaVisibilitaFascicoloResult();
			modificaVisibilitaFascicoloResult.setError(true);
			modificaVisibilitaFascicoloResult.setErrorMessage(e.getErrorMessage());
			return modificaVisibilitaFascicoloResult;

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			ModificaVisibilitaFascicoloResult modificaVisibilitaFascicoloResult = new ModificaVisibilitaFascicoloResult();
			modificaVisibilitaFascicoloResult.setError(true);
			modificaVisibilitaFascicoloResult.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			return modificaVisibilitaFascicoloResult;
		}

	}

	private static List<String> getListString(TreeSet<GruppoVisibilita> gruppiVisibilita) {
		ArrayList<String> list = new ArrayList<String>();
		for (GruppoVisibilita gr : gruppiVisibilita) {
			list.add(gr.getGruppoVisibilita());
		}
		return list;
	}

	@Override
	public void undo(ModificaVisibilitaFascicolo action, ModificaVisibilitaFascicoloResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<ModificaVisibilitaFascicolo> getActionType() {
		return ModificaVisibilitaFascicolo.class;
	}
}
