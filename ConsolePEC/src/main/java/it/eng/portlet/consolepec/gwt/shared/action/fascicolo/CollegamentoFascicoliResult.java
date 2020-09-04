package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import com.gwtplatform.dispatch.shared.Result;

public class CollegamentoFascicoliResult implements Result {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2784738287746627194L;
	private boolean error;
	private String errorMsg;

	public CollegamentoFascicoliResult() {
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
