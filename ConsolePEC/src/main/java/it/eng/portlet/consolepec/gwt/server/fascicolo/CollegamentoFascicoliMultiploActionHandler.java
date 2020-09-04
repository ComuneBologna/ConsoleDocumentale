package it.eng.portlet.consolepec.gwt.server.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoli;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoliMultiplo;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoliMultiploResult;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CollegamentoFascicoliResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.GestioneCollegamentiFascicolo;

import org.springframework.beans.factory.annotation.Autowired;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class CollegamentoFascicoliMultiploActionHandler implements ActionHandler<CollegamentoFascicoliMultiplo, CollegamentoFascicoliMultiploResult> {

	@Autowired
	GestioneCollegamentiFascicolo gestioneCollegamentiFascicolo;

	public CollegamentoFascicoliMultiploActionHandler() {
	}

	@Override
	public CollegamentoFascicoliMultiploResult execute(CollegamentoFascicoliMultiplo action, ExecutionContext context) throws ActionException {
		CollegamentoFascicoliMultiploResult collegamentoFascicoliMultiploResult = new CollegamentoFascicoliMultiploResult();
		for (CollegamentoFascicoli collegamentoFascicoli : action.getCollegamentoFascicoliList()) {
			CollegamentoFascicoliResult collegamentoFascicoliResult = gestioneCollegamentiFascicolo.gestioneCollegamentoFascicolo(collegamentoFascicoli);
			collegamentoFascicoliMultiploResult.getCollegamentoFascicoliResult().add(collegamentoFascicoliResult);
			if(collegamentoFascicoliResult.isError())
				collegamentoFascicoliResult.setError(true);
		}
		return collegamentoFascicoliMultiploResult;
	}

	@Override
	public void undo(CollegamentoFascicoliMultiplo action, CollegamentoFascicoliMultiploResult result, ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<CollegamentoFascicoliMultiplo> getActionType() {
		return CollegamentoFascicoliMultiplo.class;
	}
}
