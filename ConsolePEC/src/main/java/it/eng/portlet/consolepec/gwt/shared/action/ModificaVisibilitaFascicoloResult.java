package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class ModificaVisibilitaFascicoloResult implements Result {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3882621918622310750L;
	private String errorMessage;
	private boolean error;
	private FascicoloDTO fascicoloDTO;

	public ModificaVisibilitaFascicoloResult() {
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public FascicoloDTO getFascicoloDTO() {
		return fascicoloDTO;
	}

	public void setFascicoloDTO(FascicoloDTO fascicoloDTO) {
		this.fascicoloDTO = fascicoloDTO;
	}
}
