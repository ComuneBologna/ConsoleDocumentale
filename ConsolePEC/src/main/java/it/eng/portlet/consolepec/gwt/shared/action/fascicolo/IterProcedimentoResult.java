package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoMiniDto;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class IterProcedimentoResult implements Result {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1892019743965946679L;
	private String fascicoloPath;
	private String errorMsg;
	private boolean error;
	
	private List<ProcedimentoMiniDto> procedimenti = new ArrayList<ProcedimentoMiniDto>();

	public IterProcedimentoResult() {
	}
	
	public IterProcedimentoResult(String fascicoloPath, boolean error,String errorMsg) {
		this.fascicoloPath = fascicoloPath;
		this.error = error;
		this.errorMsg = errorMsg;
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

	public void setFascicoloPath(String fascicoloPath) {
		this.fascicoloPath = fascicoloPath;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public List<ProcedimentoMiniDto> getProcedimenti() {
		return procedimenti;
	}
}
