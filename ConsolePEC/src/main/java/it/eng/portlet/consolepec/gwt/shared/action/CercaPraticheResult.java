package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class CercaPraticheResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8582019266906482929L;
	private List<PraticaDTO> pratiche;
	private String messError;
	private Boolean error;
	private Integer maxResult;
	private Boolean estimate = true;

	public CercaPraticheResult() {
		// For serialization only
	}

	public CercaPraticheResult(List<PraticaDTO> pratiche, String messError, Boolean error) {
		this.pratiche = pratiche;
		this.messError = messError;
		this.error = error;
	}

	public List<PraticaDTO> getPratiche() {
		return pratiche;
	}

	public String getMessError() {
		return messError;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;

	}

	public void setMessError(String mess) {
		this.messError = mess;
	}

	public void setPratiche(List<PraticaDTO> pratiche) {
		this.pratiche = pratiche;
	}

	public Integer getMaxResult() {
		return maxResult;
	}

	public void setMaxResult(Integer maxResult) {
		this.maxResult = maxResult;
	}

	public Boolean getEstimate() {
		return estimate;
	}

	public void setEstimate(Boolean estimate) {
		this.estimate = estimate;
	}
}
