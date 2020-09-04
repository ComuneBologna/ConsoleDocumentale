package it.eng.portlet.consolepec.gwt.shared.action;

import java.util.Map;

import com.gwtplatform.dispatch.shared.Result;


/**
 *
 * @author biagiot
 *
 */
public class EstraiEtichetteMetadatiResult implements Result {

	private static final long serialVersionUID = 1L;

	private Map<String, String> etichetteMetadatiMap;
	private boolean error;
	private String errorMessage;

	public EstraiEtichetteMetadatiResult() {

	}

	public EstraiEtichetteMetadatiResult(Map<String, String> etichetteMetadatiMap) {
		this.etichetteMetadatiMap = etichetteMetadatiMap;
		this.errorMessage = null;
		this.error = false;
	}

	public EstraiEtichetteMetadatiResult(String errorMessage) {
		this.errorMessage = errorMessage;
		this.error = true;
	}

	public Map<String, String> getEtichetteMetadatiMap() {
		return etichetteMetadatiMap;
	}

	public void setEtichetteMetadatiMap(Map<String, String> metadatiEtichetteMap) {
		this.etichetteMetadatiMap = metadatiEtichetteMap;
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
