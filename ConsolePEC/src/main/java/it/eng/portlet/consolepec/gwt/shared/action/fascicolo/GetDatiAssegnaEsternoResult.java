package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class GetDatiAssegnaEsternoResult implements Result {
	/**
	 * 
	 */
	private static final long serialVersionUID = -196062789028768748L;
	private boolean error;
	private String errorMsg;
	private List<String> emails = new ArrayList<String>();
	
	public GetDatiAssegnaEsternoResult() {
	}
	
	public GetDatiAssegnaEsternoResult(boolean error, String errorMsg) {
		super();
		this.error = error;
		this.errorMsg = errorMsg;
	}

	public GetDatiAssegnaEsternoResult(List<String> emails) {
		super();
		this.emails = emails;
	}

	public boolean isError() {
		return error;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public List<String> getEmails() {
		return emails;
	}
	
}
