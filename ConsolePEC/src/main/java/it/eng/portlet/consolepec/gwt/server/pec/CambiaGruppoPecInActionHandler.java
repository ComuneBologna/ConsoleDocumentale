package it.eng.portlet.consolepec.gwt.server.pec;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import it.eng.consolepec.client.RiassegnaPraticaClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.portlet.consolepec.gwt.server.AbstractCambiaGruppoActionHandler;
import it.eng.portlet.consolepec.gwt.server.XMLPluginToDTOConverter;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.pec.RiassegnaPecIn;
import it.eng.portlet.consolepec.gwt.shared.action.assegna.pec.RiassegnaPecInResult;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.profilazione.GestioneProfilazioneUtente;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;

public class CambiaGruppoPecInActionHandler extends AbstractCambiaGruppoActionHandler<RiassegnaPecIn, RiassegnaPecInResult> {

	private final static Logger logger = LoggerFactory.getLogger(CambiaGruppoPecInActionHandler.class);

	@Autowired
	XMLPluginToDTOConverter praticaUtil;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;
	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	RiassegnaPraticaClient riassegnaPraticaClient;
	@Autowired
	GestioneProfilazioneUtente gestioneProfilazioneUtente;

	public CambiaGruppoPecInActionHandler() {}

	@Override
	public RiassegnaPecInResult execute(RiassegnaPecIn action, ExecutionContext context, PreferenzeRiassegnazione preferenzeRiassegnazion) {

		RiassegnaPecInResult res = null;
		List<PecInDTO> temp = new ArrayList<PecInDTO>();
		try {
			// Caricamento della pratica
			for (String id : action.getIds()) {
				PraticaEmailIn pratica = praticaSessionUtil.loadPraticaEmailInFromEncodedPath(id, TipologiaCaricamento.CARICA);
				LockedPratica lp = riassegnaPraticaClient.riassegna(pratica.getDati().getIdDocumentale(), preferenzeRiassegnazion.getRuolo(), action.getIndirizziNotifica(), action.getOperatore(),
						action.getNote(), userSessionUtil.getUtenteSpagic());

				praticaSessionUtil.removePraticaFromEncodedPath(id);
				pratica = (PraticaEmailIn) praticaSessionUtil.loadPraticaInSessione(lp);
				temp.add(praticaUtil.emailToDettaglioIN(pratica));
			}

			res = new RiassegnaPecInResult();
			res.setListPecInDTO(temp);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			res = new RiassegnaPecInResult(null, e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			res = new RiassegnaPecInResult(null, ConsolePecConstants.ERROR_MESSAGE, true);
		}

		return res;
	}

	@Override
	public void undo(RiassegnaPecIn action, RiassegnaPecInResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<RiassegnaPecIn> getActionType() {
		return RiassegnaPecIn.class;
	}

	@Override
	public GestioneProfilazioneUtente getGestioneProfilazioneUtente() {
		return gestioneProfilazioneUtente;
	}
}
