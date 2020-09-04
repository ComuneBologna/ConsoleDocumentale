package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.gwtplatform.dispatch.shared.Result;

public class SganciaPecInResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FascicoloDTO fascicoloDTO;
	private String errorMsg;
	private boolean error;
	
	public SganciaPecInResult() {
	}

	public SganciaPecInResult(FascicoloDTO fascicoloDTO) {
		super();
		this.fascicoloDTO = fascicoloDTO;
	}

	public SganciaPecInResult(String errorMsg, boolean error) {
		super();
		this.errorMsg = errorMsg;
		this.error = error;
	}
	
	public SganciaPecInResult(FascicoloDTO fascicoloDTO, String errorMsg, boolean error) {
		super();
		this.fascicoloDTO = fascicoloDTO;
		this.errorMsg = errorMsg;
		this.error = error;
	}

	public FascicoloDTO getFascicoloDTO() {
		return fascicoloDTO;
	}

	public void setFascicoloDTO(FascicoloDTO fascicoloDTO) {
		this.fascicoloDTO = fascicoloDTO;
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

	
}
