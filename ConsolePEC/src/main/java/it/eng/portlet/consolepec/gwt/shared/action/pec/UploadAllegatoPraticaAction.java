package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UploadAllegatoPraticaAction extends LiferayPortletUnsecureActionImpl<UploadAllegatoPraticaResult> {

	private String clientID;

	// file temporanei caricati nella tmp del server
	private List<TmpFileUploadDTO> tmpFiles = new ArrayList<TmpFileUploadDTO>();
	
	private Map<String, List<AllegatoDTO>> allegtiDaAltrePratiche;
	
	@SuppressWarnings("unused")
	private UploadAllegatoPraticaAction() {

		// For serialization only
	}
	
	public UploadAllegatoPraticaAction(List<TmpFileUploadDTO> tmpFiles, String clientID){
		this.clientID = clientID;
		this.tmpFiles = tmpFiles;
	}
	
	public UploadAllegatoPraticaAction(Map<String, List<AllegatoDTO>> allegati, String clientID){
		this.clientID = clientID;
		this.allegtiDaAltrePratiche = allegati;
	}

	public String getClientID() {
		return clientID;
	}

	public Map<String, List<AllegatoDTO>> getAllegtiDaAltrePratiche() {
		return allegtiDaAltrePratiche;
	}

	public void setAllegtiDaAltrePratiche(Map<String, List<AllegatoDTO>> allegtiDaAltrePratiche) {
		this.allegtiDaAltrePratiche = allegtiDaAltrePratiche;
	}

	public List<TmpFileUploadDTO> getTmpFiles() {
		return tmpFiles;
	}

	public void setTmpFiles(List<TmpFileUploadDTO> tmpFiles) {
		this.tmpFiles = tmpFiles;
	}
	
	public List<String> getFileNames() {
		List<String> tmpNames = new ArrayList<String>();
		for(TmpFileUploadDTO tmpFile : tmpFiles)
			tmpNames.add(tmpFile.getFileName());
		return tmpNames;	
	}
}
