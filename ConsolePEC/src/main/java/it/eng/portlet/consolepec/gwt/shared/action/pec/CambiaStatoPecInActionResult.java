package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class CambiaStatoPecInActionResult implements Result {

	private static final long serialVersionUID = -875441267774610518L;
	private String messErr = "";
	private Boolean isError = false;
	private List<PecInDTO> dettagliRighe = new ArrayList<PecInDTO>();
	
	public void setMessErr(String messErr) {
		this.messErr = messErr;
	}

	public void setIsError(Boolean isError) {
		this.isError = isError;
	}

	public CambiaStatoPecInActionResult() {
		// For serialization only
	}

	public CambiaStatoPecInActionResult(String messErr, Boolean isError) {
		this.messErr = messErr;
		this.isError = isError;
	}

	public String getMessErr() {
		return messErr;
	}

	public Boolean getIsError() {
		return isError;
	}

	public List<PecInDTO> getDettagliRighe() {
		return dettagliRighe;
	}
	
}
