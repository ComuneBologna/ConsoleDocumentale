package it.eng.portlet.consolepec.gwt.shared.action.fascicolo.amianto;

import com.gwtplatform.dispatch.shared.Result;

public class EstrazioneAmiantoResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7751638060601499570L;

	private String messageError;
	private boolean error;
	private String fileName;
	private String fileDir;
	
	public EstrazioneAmiantoResult() {
	}

	public EstrazioneAmiantoResult(String fileName, String fileDir) {
		this.fileName = fileName;
		this.fileDir = fileDir;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileDir() {
		return fileDir;
	}
	
	public void setFileDir(String fileDir) {
		this.fileDir = fileDir;
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
