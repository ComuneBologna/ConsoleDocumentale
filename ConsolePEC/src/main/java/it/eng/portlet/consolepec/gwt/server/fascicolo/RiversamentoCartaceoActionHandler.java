package it.eng.portlet.consolepec.gwt.server.fascicolo;

import it.eng.portlet.consolepec.gwt.server.StampaActionHandler;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.action.RiversamentoCartaceo;
import it.eng.portlet.consolepec.gwt.shared.action.RiversamentoCartaceoResult;
import it.eng.portlet.consolepec.spring.bean.stampe.GestioneStampe.StampaBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class RiversamentoCartaceoActionHandler extends StampaActionHandler<RiversamentoCartaceo, RiversamentoCartaceoResult> {

	Logger logger = LoggerFactory.getLogger(RiversamentoCartaceoActionHandler.class);

	public RiversamentoCartaceoActionHandler() {
	}

	

	@Override
	public void undo(RiversamentoCartaceo action, RiversamentoCartaceoResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<RiversamentoCartaceo> getActionType() {
		return RiversamentoCartaceo.class;
	}



	@Override
	public StampaBean getStampa(RiversamentoCartaceo action) throws Exception {
		String praticaPath = Base64Utils.URLdecodeAlfrescoPath(action.getFascicoloPath());
		String annoPG = action.getAnnoPG();
		String numPG = action.getNumeroPG();
		logger.info("Riversamento cartaceo per il PG {}/{} (pratica : {})", numPG, annoPG, praticaPath);
		return gestioneStampe.stampaRiversamentoCartaceo(praticaPath, annoPG, numPG);
	}



	@Override
	public RiversamentoCartaceoResult getResultSuccess(String fileDir, String fileName) {
		return new RiversamentoCartaceoResult(fileDir, fileName);
	}



	@Override
	public RiversamentoCartaceoResult getResultFailed(String errorMsg, Boolean error) {
		return new RiversamentoCartaceoResult(errorMsg, error);
	}
}
