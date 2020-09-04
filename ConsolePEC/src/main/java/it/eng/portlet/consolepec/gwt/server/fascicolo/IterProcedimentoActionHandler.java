package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.bologna.comune.spagic.procedimenti.iter.Record1;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.consolepec.spagicclient.bean.request.procedimenti.IterProcedimentoRequest;
import it.eng.consolepec.spagicclient.bean.response.procedimenti.IterProcedimentoResponse;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.IterProcedimento;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.IterProcedimentoResult;
import it.eng.portlet.consolepec.gwt.shared.dto.TipologiaProcedimentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoMiniDto;
import it.eng.portlet.consolepec.gwt.shared.procedimenti.StatoProcedimento;
import it.eng.portlet.consolepec.spring.bean.procedimenti.GestioneProcedimenti;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.bean.tipologiaprocedimenti.RecuperoTipologiaProcedimenti;

@Deprecated
public class IterProcedimentoActionHandler implements ActionHandler<IterProcedimento, IterProcedimentoResult> {

	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneProcedimenti gestioneProcedimenti;
	@Autowired
	RecuperoTipologiaProcedimenti recuperoTipologiaProcedimenti;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	private Logger logger = LoggerFactory.getLogger(AvvioProcedimentoActionHandler.class);

	public IterProcedimentoActionHandler() {}

	@Override
	public IterProcedimentoResult execute(IterProcedimento action, ExecutionContext context) throws ActionException {

		logger.debug("Action handler dell'iter dei procedimenti");

		List<TipologiaProcedimentoDto> elencoTipologieProcedimenti = recuperoTipologiaProcedimenti.getElencoTipologieProcedimenti();
		HashMap<Integer, String> mappaTipologie = new HashMap<Integer, String>();
		for (TipologiaProcedimentoDto tipologia : elencoTipologieProcedimenti) {
			if (!mappaTipologie.containsKey(tipologia.getCodiceProcedimento())) {
				mappaTipologie.put(tipologia.getCodiceProcedimento(), tipologia.getDescrizione());
			}
		}

		IterProcedimentoRequest iterRequest = new IterProcedimentoRequest();
		iterRequest.setTipoProtocollo(ConsoleConstants.Procedimenti.TIPO_PROTOCOLLO_PROCEDIMENTI);
		iterRequest.setCodComune(ConsoleConstants.Procedimenti.COD_COMUNE_PROCEDIMENTI);
		iterRequest.setAnnoProtocollazione(action.getAnnoPgCapofila());
		iterRequest.setNumProtocollazione(action.getNumPgCapofila());

		IterProcedimentoResult result = null;

		try {
			IterProcedimentoResponse response = gestioneProcedimenti.getIterProcedimento(iterRequest);
			result = new IterProcedimentoResult();
			result.setFascicoloPath(action.getFascicoloPath());
			for (Record1 r : response.getDatiProcedimento()) {
				ProcedimentoMiniDto dto = new ProcedimentoMiniDto();
				dto.setCodTipologiaProcedimento(r.getCodProcedimento());
				dto.setAnnoPG(Integer.parseInt(r.getAnnoProtocollazione()));
				dto.setNumeroPG(r.getNumProtocollazione());
				String descrizione = null;
				for (Integer codice : mappaTipologie.keySet()) {
					if (codice.intValue() == r.getCodProcedimento().intValue()) {
						descrizione = mappaTipologie.get(codice);
					}
				}
				dto.setDescrTipologiaProcedimento(descrizione);
				dto.setTermine(r.getTermineNormato().intValue());
				dto.setDataInizioDecorrenzaProcedimento(DateUtils.xmlGrCalToDate(r.getDataAvvioProc()));
				dto.setStato(r.getDataChiusuraProc() == null ? StatoProcedimento.AVVIATO : StatoProcedimento.CHIUSO); // TODO FM Da modificare quando si gestirà l'iter completo dei procedimenti
				result.getProcedimenti().add(dto);
			}

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new IterProcedimentoResult(action.getFascicoloPath(), true, e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new IterProcedimentoResult(action.getFascicoloPath(), true, ConsolePecConstants.ERROR_MESSAGE);
		}

		return result;
	}

	@Override
	public void undo(IterProcedimento action, IterProcedimentoResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<IterProcedimento> getActionType() {
		return IterProcedimento.class;
	}
}
