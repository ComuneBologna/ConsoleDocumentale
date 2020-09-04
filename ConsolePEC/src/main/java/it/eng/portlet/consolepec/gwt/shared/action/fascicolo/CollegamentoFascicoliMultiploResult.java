package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class CollegamentoFascicoliMultiploResult implements Result {

	private List<CollegamentoFascicoliResult> collegamentoFascicoliResult = new ArrayList<>();

	private boolean error;
	private String errorMessage;

	private static final long serialVersionUID = 9135341458127467876L;

	public CollegamentoFascicoliMultiploResult() {
	}

	public List<CollegamentoFascicoliResult> getCollegamentoFascicoliResult() {
		return collegamentoFascicoliResult;
	}

	public void setCollegamentoFascicoliResult(List<CollegamentoFascicoliResult> collegamentoFascicoliResult) {
		this.collegamentoFascicoliResult = collegamentoFascicoliResult;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
