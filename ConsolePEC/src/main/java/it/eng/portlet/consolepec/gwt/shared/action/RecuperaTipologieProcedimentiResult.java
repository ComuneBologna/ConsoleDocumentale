package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.dto.TipologiaProcedimentoDto;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class RecuperaTipologieProcedimentiResult implements Result {

	private static final long serialVersionUID = 1317244230384623209L;

	private String messageError;
	private boolean error;

	private List<TipologiaProcedimentoDto> tipologiaProcedimenti = new ArrayList<TipologiaProcedimentoDto>();

	public RecuperaTipologieProcedimentiResult(List<TipologiaProcedimentoDto> tipologiaProcedimenti) {
		this.tipologiaProcedimenti = tipologiaProcedimenti;
	}

	public RecuperaTipologieProcedimentiResult() {
	}

	public List<TipologiaProcedimentoDto> getTipologiaProcedimenti() {
		return tipologiaProcedimenti;
	}

	public String getMessageError() {
		return messageError;
	}

	public boolean getError() {
		return error;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
