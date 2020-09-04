package it.eng.portlet.consolepec.gwt.server.fascicolo;

import it.eng.portlet.consolepec.gwt.server.StampaActionHandler;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.StampaRicevuteConsegna;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.StampaRicevuteConsegnaResult;
import it.eng.portlet.consolepec.spring.bean.stampe.GestioneStampe.StampaBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class StampaRicevuteConsegnaActionHandler extends StampaActionHandler<StampaRicevuteConsegna, StampaRicevuteConsegnaResult> {
	
	Logger logger = LoggerFactory.getLogger(StampaRicevuteConsegnaActionHandler.class);
	
	public StampaRicevuteConsegnaActionHandler() {
	}

	@Override
	public Class<StampaRicevuteConsegna> getActionType() {
		return StampaRicevuteConsegna.class;
	}

	@Override
	public void undo(StampaRicevuteConsegna arg0, StampaRicevuteConsegnaResult arg1, ExecutionContext arg2) throws ActionException {
	}

	@Override
	public StampaBean getStampa(StampaRicevuteConsegna action) throws Exception {
		String praticaPath = Base64Utils.URLdecodeAlfrescoPath(action.getPraticaPath());
		logger.info("Ricevuta di consegna per la pratica: {}",praticaPath);
		return gestioneStampe.stampaRicevutaDiConsegna(praticaPath);
	}

	@Override
	public StampaRicevuteConsegnaResult getResultSuccess(String fileDir, String fileName) {
		return new StampaRicevuteConsegnaResult(fileDir, fileName);
	}

	@Override
	public StampaRicevuteConsegnaResult getResultFailed(String errorMsg, Boolean error) {
		return new StampaRicevuteConsegnaResult(errorMsg, error);
	}

	


		
		
}
