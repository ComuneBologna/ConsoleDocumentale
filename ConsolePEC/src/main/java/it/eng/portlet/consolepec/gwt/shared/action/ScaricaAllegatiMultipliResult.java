package it.eng.portlet.consolepec.gwt.shared.action;

import com.gwtplatform.dispatch.shared.Result;

public class ScaricaAllegatiMultipliResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1265314508244681912L;
	private String name;
	private String dir;
	private boolean error;
	private String errorMsg;

	@SuppressWarnings("unused")
	private ScaricaAllegatiMultipliResult() {

		// For serialization only
	}

	/**
	 * 
	 * @param dir
	 * @param name
	 */
	public ScaricaAllegatiMultipliResult(String dir, String name) {
		this.name = name;
		this.dir = dir;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
