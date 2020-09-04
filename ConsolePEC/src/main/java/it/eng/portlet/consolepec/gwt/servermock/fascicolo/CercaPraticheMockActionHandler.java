package it.eng.portlet.consolepec.gwt.servermock.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPraticheResult;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.List;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CercaPraticheMockActionHandler implements ActionHandler<CercaPratiche, CercaPraticheResult> {

	public CercaPraticheMockActionHandler() {
	}

	@Override
	public CercaPraticheResult execute(CercaPratiche action, ExecutionContext context) throws ActionException {
		List<PraticaDTO> pratiche = caricaParatiche();
		String messError = "";
		Boolean error = false;
		CercaPraticheResult res = new CercaPraticheResult(pratiche, messError, error);
		res.setMaxResult(pratiche.size() + 100);
		res.setEstimate(true);
		return res;
	}

	private List<PraticaDTO> caricaParatiche() {
		return FascicoloDB.getInstance().asList();
	}

	@Override
	public void undo(CercaPratiche action, CercaPraticheResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CercaPratiche> getActionType() {
		return CercaPratiche.class;
	}
}
