package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;

import java.util.ArrayList;
import java.util.List;

public class UploadAllegatoFascicolo extends LiferayPortletUnsecureActionImpl<UploadAllegatoFascicoloResult> {
	
	private String clientID;
	// file temporanei caricati nella tmp del server
	private List<TmpFileUploadDTO> tmpFiles = new ArrayList<TmpFileUploadDTO>();

	@SuppressWarnings("unused")
	private UploadAllegatoFascicolo() {
		// For serialization only
	}

	public UploadAllegatoFascicolo(String clientID) {
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

	public List<TmpFileUploadDTO> getTmpFiles() {
		return tmpFiles;
	}

	public void setTmpFiles(List<TmpFileUploadDTO> tmpFiles) {
		this.tmpFiles.clear();
		this.tmpFiles.addAll(tmpFiles);
	}

	public List<String> getFileNames() {
		List<String> tmpNames = new ArrayList<String>();
		for(TmpFileUploadDTO tmpFile : tmpFiles)
			tmpNames.add(tmpFile.getFileName());
		return tmpNames;	
	}

}
