package it.eng.portlet.consolepec.gwt.shared.action.assegna.fasciolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class RiassegnaFascicoloResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorMsg;
	private Boolean error;

	private List<FascicoloDTO> fascicoli;

	@SuppressWarnings("unused")
	private RiassegnaFascicoloResult() {
		// For serialization only
	}

	public RiassegnaFascicoloResult(List<FascicoloDTO> tempFascicoli, String errorMsg, Boolean error) {
		this.fascicoli = tempFascicoli;
		this.errorMsg = errorMsg;
		this.error = error;
	}


	public String getErrorMsg() {
		return errorMsg;
	}

	public Boolean getError() {
		return error;
	}

	
	public List<FascicoloDTO> getFascicoli() {
	
		return fascicoli;
	}

	
	public void setFascicoli(List<FascicoloDTO> fascicoli) {
	
		this.fascicoli = fascicoli;
	}
}
