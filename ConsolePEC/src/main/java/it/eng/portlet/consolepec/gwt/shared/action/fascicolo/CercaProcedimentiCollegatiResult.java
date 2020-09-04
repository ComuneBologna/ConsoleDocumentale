package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import com.gwtplatform.dispatch.shared.Result;

public class CercaProcedimentiCollegatiResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6229256690433114726L;
	private int numProcedimenti;
	private boolean error;
	private String errorMsg;
	
	public CercaProcedimentiCollegatiResult() {
	}

	public int getNumProcedimenti() {
		return numProcedimenti;
	}

	public void setNumProcedimenti(int numProcedimenti) {
		this.numProcedimenti = numProcedimenti;
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
