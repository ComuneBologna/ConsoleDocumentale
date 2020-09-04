package it.eng.portlet.consolepec.gwt.shared.action.assegna.modulo;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class RiassegnaModuloResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String errorMsg;
	private Boolean error;

	private List<PraticaModulisticaDTO> pratiche;

	@SuppressWarnings("unused")
	private RiassegnaModuloResult() {
		// For serialization only
	}

	public RiassegnaModuloResult(List<PraticaModulisticaDTO> pratiche, String errorMsg, Boolean error) {
		this.pratiche = pratiche;
		this.errorMsg = errorMsg;
		this.error = error;
	}


	public String getErrorMsg() {
		return errorMsg;
	}

	public Boolean getError() {
		return error;
	}

	
	public List<PraticaModulisticaDTO> getPratiche() {
	
		return pratiche;
	}

	
	public void setPratiche(List<PraticaModulisticaDTO> pratiche) {
	
		this.pratiche = pratiche;
	}
}
