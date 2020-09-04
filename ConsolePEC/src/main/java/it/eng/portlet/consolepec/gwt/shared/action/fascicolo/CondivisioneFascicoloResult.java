package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import com.gwtplatform.dispatch.shared.Result;

public class CondivisioneFascicoloResult implements Result {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -196062789028768748L;
	private boolean error;
	private String errorMsg;

	public CondivisioneFascicoloResult() {
	}
	
	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
