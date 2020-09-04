package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.DettagliAllegatoDTO;

import com.gwtplatform.dispatch.shared.Result;

public class GetDettagliAllegatoResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DettagliAllegatoDTO dto;
	private String errMsg;
	private Boolean error;

	@SuppressWarnings("unused")
	private GetDettagliAllegatoResult() {

	}

	public DettagliAllegatoDTO getDto() {
		return dto;
	}

	public void setDto(DettagliAllegatoDTO dto) {
		this.dto = dto;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public GetDettagliAllegatoResult(DettagliAllegatoDTO dto, String errMsg, Boolean error) {
		this.dto = dto;
		this.errMsg = errMsg;
		this.error = error;
	}
}
