package it.eng.portlet.consolepec.gwt.shared.action.genericdata;

import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

/**
 * 
 * @author biagiot
 *
 */
public class CaricaIndirizziEmailActionResult implements Result {

	private static final long serialVersionUID = 1L;
	private List<String> indirizziEmail;
	private boolean error = false;
	private String errorMessage;
	
	public CaricaIndirizziEmailActionResult() {
		this.error = false;
	}
	
	public CaricaIndirizziEmailActionResult(List<String> indirizziEmail) {
		this.indirizziEmail = indirizziEmail;
		this.error = false;
	}
	
	public CaricaIndirizziEmailActionResult(String errorMessage) {
		this.errorMessage = errorMessage;
		this.error = true;
	}
	
	public List<String> getIndirizziEmail() {
		return indirizziEmail;
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
