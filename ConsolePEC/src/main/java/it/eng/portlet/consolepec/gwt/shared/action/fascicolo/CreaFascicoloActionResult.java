
package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CreaFascicoloActionResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String messageError = "";
	private Boolean error = false;
	private FascicoloDTO fascicoloDTO;

	
	public FascicoloDTO getFascicoloDTO() {
	
		return fascicoloDTO;
	}
	
	public void setFascicoloDTO(FascicoloDTO fascicoloDTO) {
	
		this.fascicoloDTO = fascicoloDTO;
	}

	public CreaFascicoloActionResult() {

	}

	public String getMessageError() {

		return messageError;
	}

	public void setMessageError(String messageError) {

		this.messageError = messageError;
	}

	public Boolean getError() {

		return error;
	}

	public void setError(Boolean error) {

		this.error = error;
	}
}
