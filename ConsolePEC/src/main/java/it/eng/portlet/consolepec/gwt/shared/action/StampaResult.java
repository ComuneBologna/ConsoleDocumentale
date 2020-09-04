package it.eng.portlet.consolepec.gwt.shared.action;

import com.gwtplatform.dispatch.shared.Result;

public class StampaResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = -677198641375544563L;
	private String errorMsg;
	private boolean error;
	private String fileName;
	private String fileDir;


	protected StampaResult() {
		// For serialization only
	}
	
	public StampaResult(String fileDir, String fileName) {
		this.fileDir = fileDir;
		this.fileName = fileName;
	}
	
	public StampaResult(String errorMsg, Boolean error) {
		this.error = error;
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public boolean isError() {
		return error;
	}

	public String getFileName() {
		return fileName;
	}

	public String getFileDir() {
		return fileDir;
	}

}
