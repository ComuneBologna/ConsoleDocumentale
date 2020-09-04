package it.eng.portlet.consolepec.gwt.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TmpFileUploadDTO implements IsSerializable{

	private String fileName;
	private String dirName;

	public TmpFileUploadDTO() {
		// per la serializzazione gwt
	}
	
	public TmpFileUploadDTO(String fileName, String dirName) {
		this.fileName = fileName;
		this.dirName = dirName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

}
