package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class CondivisioneFascicoloMultiplaResult implements Result {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5558196627424192993L;
	private boolean error;
	private String errorMessage;
	private List<CondivisioneFascicoloResult> condivisioniResult = new ArrayList<CondivisioneFascicoloResult>();

	public CondivisioneFascicoloMultiplaResult() {
	}

	public List<CondivisioneFascicoloResult> getCondivisioniResult() {
		return condivisioniResult;
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
