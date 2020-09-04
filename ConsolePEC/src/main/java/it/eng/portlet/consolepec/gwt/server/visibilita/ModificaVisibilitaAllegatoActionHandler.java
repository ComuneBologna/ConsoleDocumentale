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

import it.eng.consolepec.client.CambiaVisibilitaAllegatoClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaVisibilitaAllegato;
import it.eng.portlet.consolepec.gwt.shared.action.ModificaVisibilitaAllegatoResult;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class ModificaVisibilitaAllegatoActionHandler implements ActionHandler<ModificaVisibilitaAllegato, ModificaVisibilitaAllegatoResult> {

	Logger logger = LoggerFactory.getLogger(ModificaVisibilitaAllegatoActionHandler.class);

	@Autowired
	private XMLPluginToDTOConverter utilPratica;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	private PraticaSessionUtil praticaSessionUtil;
	@Autowired
	private CambiaVisibilitaAllegatoClient cambiaVisibilitaAllegatoClient;

	public ModificaVisibilitaAllegatoActionHandler() {}

	@Override
	public ModificaVisibilitaAllegatoResult execute(ModificaVisibilitaAllegato action, ExecutionContext context) throws ActionException {

		logger.debug("Modifica visibilita allegato {} della pratica {}", action.getNomeAllegato(), action.getClientID());
		Fascicolo fascicolo;
		try {
			fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(action.getClientID(), TipologiaCaricamento.CARICA);
			logger.debug("Caricato fascicolo: {}", fascicolo.getDati());

			List<String> listaGruppi = getListString(action.getGruppiVisibilita());

			LockedPratica pratica = cambiaVisibilitaAllegatoClient.cambiaVisibilitaAllegato(fascicolo.getDati().getIdDocumentale(), action.getNomeAllegato(), listaGruppi,
					userSessionUtil.getUtenteSpagic());

			fascicolo = (Fascicolo) praticaSessionUtil.loadPraticaInSessione(pratica);

			FascicoloDTO dto = utilPratica.fascicoloToDettaglio(fascicolo);

			praticaSessionUtil.removePraticaFromEncodedPath(action.getClientID());

			ModificaVisibilitaAllegatoResult modificaVisibilitaAllegatoResult = new ModificaVisibilitaAllegatoResult();
			modificaVisibilitaAllegatoResult.setFascicoloDTO(dto);

			return modificaVisibilitaAllegatoResult;

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			ModificaVisibilitaAllegatoResult modificaVisibilitaAllegatoResult = new ModificaVisibilitaAllegatoResult();
			modificaVisibilitaAllegatoResult.setError(true);
			modificaVisibilitaAllegatoResult.setErrorMessage(e.getErrorMessage());
			return modificaVisibilitaAllegatoResult;
		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			ModificaVisibilitaAllegatoResult modificaVisibilitaAllegatoResult = new ModificaVisibilitaAllegatoResult();
			modificaVisibilitaAllegatoResult.setError(true);
			modificaVisibilitaAllegatoResult.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			return modificaVisibilitaAllegatoResult;
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
	public void undo(ModificaVisibilitaAllegato action, ModificaVisibilitaAllegatoResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<ModificaVisibilitaAllegato> getActionType() {
		return ModificaVisibilitaAllegato.class;
	}
}
