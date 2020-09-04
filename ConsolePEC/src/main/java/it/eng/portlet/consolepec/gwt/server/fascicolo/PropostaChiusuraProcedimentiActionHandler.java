package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoInput;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoInput.TipoEstrazione;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoOutput;
import it.eng.cobo.consolepec.commons.procedimento.PropostaChiusuraProcedimentoOutput.ProcedimentoDaChiudere;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.PropostaChiusuraProcedimentiAction;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.PropostaChiusuraProcedimentiResult;
import it.eng.portlet.consolepec.gwt.shared.dto.TipologiaProcedimentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoMiniDto;
import it.eng.portlet.consolepec.gwt.shared.procedimenti.StatoProcedimento;
import it.eng.portlet.consolepec.spring.bean.procedimenti.GestioneProcedimenti;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.bean.tipologiaprocedimenti.RecuperoTipologiaProcedimenti;

public class PropostaChiusuraProcedimentiActionHandler implements ActionHandler<PropostaChiusuraProcedimentiAction, PropostaChiusuraProcedimentiResult> {

	@Autowired
	UserSessionUtil userSessionUtil;

	@Autowired
	GestioneProcedimenti gestioneProcedimenti;

	@Autowired
	RecuperoTipologiaProcedimenti recuperoTipologiaProcedimenti;

	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	private static final Logger logger = LoggerFactory.getLogger(PropostaChiusuraProcedimentiActionHandler.class);

	public PropostaChiusuraProcedimentiActionHandler() {}

	@Override
	public PropostaChiusuraProcedimentiResult execute(PropostaChiusuraProcedimentiAction action, ExecutionContext context) throws ActionException {

		logger.info("Inizio esecuzione servizio di Proposta Chiusura Procedimento: {}", action);
		PropostaChiusuraProcedimentiResult result = null;

		try {
			PropostaChiusuraProcedimentoInput input = new PropostaChiusuraProcedimentoInput();
			input.setAnnoProtocollazione(action.getAnnoPgNonCapofila());
			input.setNumProtocollazione(action.getNumPgNonCapofila());
			input.setTipoEstrazione(TipoEstrazione.A);

			PropostaChiusuraProcedimentoOutput output = gestioneProcedimenti.propostaChiusuraProcedimento(input);

			List<TipologiaProcedimentoDto> elencoTipologieProcedimenti = recuperoTipologiaProcedimenti.getElencoTipologieProcedimenti();
			HashMap<Integer, String> mappaTipologie = new HashMap<Integer, String>();
			for (TipologiaProcedimentoDto tipologia : elencoTipologieProcedimenti) {
				if (!mappaTipologie.containsKey(tipologia.getCodiceProcedimento())) {
					mappaTipologie.put(tipologia.getCodiceProcedimento(), tipologia.getDescrizione());
				}
			}

			List<ProcedimentoMiniDto> procedimenti = new ArrayList<ProcedimentoMiniDto>();
			for (ProcedimentoDaChiudere proc : output.getProcedimentiProposti()) {
				ProcedimentoMiniDto dto = new ProcedimentoMiniDto();
				dto.setCodTipologiaProcedimento(proc.getCodTipologiaProcedimento());
				dto.setAnnoPG(Integer.parseInt(proc.getAnnoProtocolloCapofila()));
				dto.setNumeroPG(proc.getNumeroProtocolloCapofila());
				String descrizione = null;
				for (Integer codice : mappaTipologie.keySet()) {
					if (codice.intValue() == proc.getCodTipologiaProcedimento().intValue()) {
						descrizione = mappaTipologie.get(codice);
					}
				}
				dto.setDescrTipologiaProcedimento(descrizione);
				dto.setDataInizioDecorrenzaProcedimento(proc.getDataInizioDecorrenza());
				dto.setStato(StatoProcedimento.AVVIATO);
				procedimenti.add(dto);
			}

			result = new PropostaChiusuraProcedimentiResult(output.getIdDocumentaleFascicolo(), procedimenti);

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			result = new PropostaChiusuraProcedimentiResult(e.getErrorMessage());

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			result = new PropostaChiusuraProcedimentiResult(ConsolePecConstants.ERROR_MESSAGE);

		} finally {
			logger.info("Fine esecuzione servizio di Proposta Chiusura Procedimento: {}", action);

		}

		return result;
	}

	@Override
	public void undo(PropostaChiusuraProcedimentiAction action, PropostaChiusuraProcedimentiResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<PropostaChiusuraProcedimentiAction> getActionType() {
		return PropostaChiusuraProcedimentiAction.class;
	}
}
