package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.gwtplatform.dispatch.shared.Result;

public class GestionePresaInCaricoFascicoloActionResult implements Result {
	/**
	 * 
	 */
	private static final long serialVersionUID = -215434653058389556L;
	private String errorMsg;
	private boolean error;
	private PraticaDTO praticaDTO;

	public GestionePresaInCaricoFascicoloActionResult() {
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public PraticaDTO getPraticaDTO() {
		return praticaDTO;
	}

	public void setPraticaDTO(PraticaDTO praticaDTO) {
		this.praticaDTO = praticaDTO;
	}

}
