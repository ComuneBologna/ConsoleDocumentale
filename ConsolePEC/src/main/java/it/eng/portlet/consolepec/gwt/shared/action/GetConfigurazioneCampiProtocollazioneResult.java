package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.dto.Campo;

import java.util.Map;

import com.gwtplatform.dispatch.shared.Result;

public class GetConfigurazioneCampiProtocollazioneResult implements Result {

	private static final long serialVersionUID = 1L;
	private Map<String, Campo> campi;
	private String messErr;
	private boolean error;

	@SuppressWarnings("unused")
	private GetConfigurazioneCampiProtocollazioneResult() {
		// For serialization only
	}

	public GetConfigurazioneCampiProtocollazioneResult(Map<String, Campo> campi, String messErr, Boolean error) {
		this.campi = campi;
		this.messErr = messErr;
		this.error = error;
	}

	public GetConfigurazioneCampiProtocollazioneResult(Map<String, Campo> campi) {
		this.error = false;
		this.campi = campi;
	}

	public Map<String, Campo> getCampi() {
		return campi;
	}

	public void setCampi(Map<String, Campo> campi) {
		this.campi = campi;
	}

	public String getMessErr() {
		return messErr;
	}

	public void setMessErr(String messErr) {
		this.messErr = messErr;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
