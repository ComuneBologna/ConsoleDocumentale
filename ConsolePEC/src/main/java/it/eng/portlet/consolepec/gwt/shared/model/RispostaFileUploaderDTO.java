package it.eng.portlet.consolepec.gwt.shared.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class RispostaFileUploaderDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1552025060643027972L;
	public static String ERR_KEY = "error";
	public static String ERRMSG_KEY = "messError";
	public static String TMPFILES_KEY = "tmpfiles";

	private List<TmpFileUploadDTO> tmpFiles = new ArrayList<TmpFileUploadDTO>();
	private boolean error = false;
	private String messError = "";
	
	
	public RispostaFileUploaderDTO() {

	}

	public RispostaFileUploaderDTO(boolean error, String messError) {
		super();
		this.error = error;
		this.messError = messError;

	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessError() {
		return messError;
	}

	public void setMessError(String messError) {
		this.messError = messError;
	}

	public static RispostaFileUploaderDTO leggiFormJson(String json) {
		RispostaFileUploaderDTO ris = new RispostaFileUploaderDTO();
		JSONValue value;
		try {
			value = JSONParser.parseLenient(json);
		} catch (Exception e) {
			return null;
		}

		JSONObject valueObj = value.isObject();
		ris.setError(valueObj.get(ERR_KEY).isBoolean().booleanValue());
		ris.setMessError(valueObj.get(ERRMSG_KEY).isString().stringValue());
		JSONArray jsonArray = valueObj.get(TMPFILES_KEY).isArray();
		if(jsonArray != null){
			for (int i = 0; i<jsonArray.size(); i++){
				JSONObject file = jsonArray.get(i).isObject();
				ris.addTmpFiles(new TmpFileUploadDTO(file.get("fileName").isString().stringValue(), file.get("dirName").isString().stringValue()));
			}
		}
		return ris;
	}

	public List<TmpFileUploadDTO> getTmpFiles() {
		return tmpFiles;
	}

	public void setTmpFiles(List<TmpFileUploadDTO> tmpFiles) {
		this.tmpFiles.clear();
		this.tmpFiles.addAll(tmpFiles);
	}

	/**
	 * metodo di utility per non stare lì a passare ogni volta la lista intera
	 */
	public void addTmpFiles(TmpFileUploadDTO tmpFile){
		tmpFiles.add(tmpFile);
	}

}
