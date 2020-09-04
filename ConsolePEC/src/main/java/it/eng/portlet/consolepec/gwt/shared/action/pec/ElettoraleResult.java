package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class ElettoraleResult implements Result {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9142738296913939084L;
	private String errorMsg;
	private boolean error;
	private PecInDTO dto;
	private List<String> clientIDPraticheCollegate = new ArrayList<String>();

	@SuppressWarnings("unused")
	private ElettoraleResult() {
		// For serialization only
	}

	public ElettoraleResult(boolean error, String errorMsg) {
		this.error = error;
		this.errorMsg = errorMsg;
	}
	
	public ElettoraleResult(PecInDTO dto, List<String> clientIDPraticheCollegate){
		this.dto = dto;
		this.error = false;
		this.errorMsg = null;
		this.clientIDPraticheCollegate.addAll(clientIDPraticheCollegate);
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public boolean isError() {
		return error;
	}

	public PecInDTO getDTO() {
		return dto;
	}

	public List<String> getClientIDPraticheCollegate() {
		return clientIDPraticheCollegate;
	}
	

}
