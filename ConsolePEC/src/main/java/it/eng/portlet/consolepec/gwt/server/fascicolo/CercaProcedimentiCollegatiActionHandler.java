package it.eng.portlet.consolepec.gwt.server.fascicolo;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CercaProcedimentiCollegati;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CercaProcedimentiCollegatiResult;
import it.eng.portlet.consolepec.gwt.shared.model.TipologiaCaricamento;
import it.eng.portlet.consolepec.spring.bean.session.pratiche.PraticaSessionUtil;

public class CercaProcedimentiCollegatiActionHandler implements ActionHandler<CercaProcedimentiCollegati, CercaProcedimentiCollegatiResult> {

	@Autowired
	private PraticaSessionUtil praticaSessionUtil;

	public CercaProcedimentiCollegatiActionHandler() {}

	@Override
	public CercaProcedimentiCollegatiResult execute(CercaProcedimentiCollegati action, ExecutionContext context) throws ActionException {
		CercaProcedimentiCollegatiResult result = new CercaProcedimentiCollegatiResult();
		int count = 0;
		for (String praticaPath : action.getIds()) {
			try {
				Fascicolo fascicolo = praticaSessionUtil.loadFascicoloFromEncodedPath(praticaPath, TipologiaCaricamento.CARICA);
				count += fascicolo.getDati().getProcedimenti().size();

			} catch (Exception e) {
				result.setError(true);
				result.setErrorMsg(ConsolePecConstants.ERROR_MESSAGE);
				break;
			}
		}
		result.setNumProcedimenti(count);
		return result;
	}

	@Override
	public void undo(CercaProcedimentiCollegati action, CercaProcedimentiCollegatiResult result, ExecutionContext context) throws ActionException {}

	@Override
	public Class<CercaProcedimentiCollegati> getActionType() {
		return CercaProcedimentiCollegati.class;
	}
}
