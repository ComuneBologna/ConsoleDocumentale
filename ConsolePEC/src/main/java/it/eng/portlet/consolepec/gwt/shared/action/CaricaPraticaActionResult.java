package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CaricaPraticaActionResult implements Result {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8684697555194971923L;
	private PraticaDTO praticaDTO;
	private boolean error;
	private String errorMsg;

	public CaricaPraticaActionResult() {
	}

	public PraticaDTO getPraticaDTO() {
		return praticaDTO;
	}

	public void setPraticaDTO(PraticaDTO praticaDTO) {
		this.praticaDTO = praticaDTO;
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
