package it.eng.consolepec.spagicclient.remoteproxy;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ResponseWithAttachementsDto<TResponse> {

	private TResponse response;
	private Map<String, InputStream> attachements = new HashMap<String, InputStream>();

	public TResponse getResponse() {
		return response;
	}

	public void setResponse(TResponse response) {
		this.response = response;
	}

	public Map<String, InputStream> getAttachements() {
		return attachements;
	}

	public void setAttachements(Map<String, InputStream> attachements) {
		this.attachements = attachements;
	}

}
