package it.eng.portlet.consolepec.gwt.server.fascicolo;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.consolepec.spagicclient.bean.request.procedimenti.GestioneProcedimentoRequest;
import it.eng.consolepec.spagicclient.bean.response.procedimenti.GestioneProcedimentoResponse;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AvvioProcedimento;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.AvvioProcedimentoResult;
import it.eng.portlet.consolepec.spring.bean.procedimenti.GestioneProcedimenti;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;
import it.eng.portlet.consolepec.spring.bean.session.user.UserSessionUtil;
import it.eng.portlet.consolepec.spring.bean.tipologiaprocedimenti.RecuperoTipologiaProcedimenti;

public class AvvioProcedimentoActionHandler implements ActionHandler<AvvioProcedimento, AvvioProcedimentoResult> {

	@Autowired
	UserSessionUtil userSessionUtil;
	@Autowired
	GestioneProcedimenti gestioneProcedimenti;
	@Autowired
	RecuperoTipologiaProcedimenti recuperoTipologiaProcedimenti;
	@Autowired
	PraticaSessionUtil praticaSessionUtil;

	private Logger logger = LoggerFactory.getLogger(AvvioProcedimentoActionHandler.class);

	public AvvioProcedimentoActionHandler() {}

	@Override
	public AvvioProcedimentoResult execute(AvvioProcedimento action, ExecutionContext context) throws ActionException {

		logger.debug("Action handler per l'avvio dei procedimenti");

		String fascicoloPath = Base64Utils.URLdecodeAlfrescoPath(action.getFascicoloPath()); // devo passare il path del fasciolo in chiaro

		GestioneProcedimentoRequest dto = new GestioneProcedimentoRequest();
		dto.setOperazione(ConsoleConstants.Procedimenti.FLAG_CHIUSURA_PROCEDIMENTI);
		dto.setTipoProtocollo(ConsoleConstants.Procedimenti.TIPO_PROTOCOLLO_PROCEDIMENTI);
		dto.setCodComune(ConsoleConstants.Procedimenti.COD_COMUNE_PROCEDIMENTI);
		dto.setCodUtente(ConsoleConstants.Procedimenti.COD_UTENTE_PROCEDIMENTI);
		dto.setAnnoProtocollazione(action.getAnnoProtocollazione());
		dto.setCodQuartiere(action.getCodQuartiere());
		dto.setCodTipologiaProcedimento(action.getCodTipologiaProcedimento());
		dto.setDataInizioDecorrenzaProcedimento(action.getDataInizioProcedimento());
		dto.setModAvvioProcedimento(action.getModAvvioProcedimento());
		dto.setNumProtocollazione(action.getNumProtocollazione());

		try {
			logger.info("Avvio procedimento protocollazione {}/{} pratica {}", dto.getNumProtocollazione(), dto.getAnnoProtocollazione(), fascicoloPath);
			GestioneProcedimentoResponse avviaProcedimento = null;

			if (action.getEmails().size() > 0) {
				avviaProcedimento = gestioneProcedimenti.avviaProcedimento(dto, fascicoloPath, action.getEmails());
			} else {
				avviaProcedimento = gestioneProcedimenti.avviaProcedimento(dto, fascicoloPath);
			}

			AvvioProcedimentoResult avvioProcedimentoResult = new AvvioProcedimentoResult(action.getFascicoloPath());
			avvioProcedimentoResult.setCodiceProcedimento(dto.getCodTipologiaProcedimento());
			avvioProcedimentoResult.setDescrizioneProcedimento(recuperoTipologiaProcedimenti.getDescrizioneProcedimento(dto.getCodTipologiaProcedimento()));
			avvioProcedimentoResult.setDataInizioProcedimento(dto.getDataInizioDecorrenzaProcedimento() == null ? new Date() : dto.getDataInizioDecorrenzaProcedimento());
			avvioProcedimentoResult.setTempoNormatoInGiorni(avviaProcedimento.getTermine());

			praticaSessionUtil.removePraticaFromEncodedPath(action.getFascicoloPath());

			return avvioProcedimentoResult;

		} catch (SpagicClientException e) {
			logger.error("Errore", e);
			return new AvvioProcedimentoResult(fascicoloPath, e.getErrorMessage(), true);

		} catch (Exception e) {
			logger.error("Errore imprevisto", e);
			return new AvvioProcedimentoResult(fascicoloPath, ConsolePecConstants.ERROR_MESSAGE, true);
		}
	}

	@Override
	public void undo(AvvioProcedimento action, AvvioProcedimentoResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<AvvioProcedimento> getActionType() {
		return AvvioProcedimento.class;
	}
}
