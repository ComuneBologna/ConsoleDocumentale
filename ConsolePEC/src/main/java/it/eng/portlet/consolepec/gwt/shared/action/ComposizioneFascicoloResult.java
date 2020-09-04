package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.CatenaDocumentaleDTO;

import com.gwtplatform.dispatch.shared.Result;

public class ComposizioneFascicoloResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private CatenaDocumentaleDTO catenaDocumentaleDTO;
	
	private String errorMsg;
	private boolean error;

	@SuppressWarnings("unused")
	private ComposizioneFascicoloResult() {
		// For serialization only
	}
	
	public ComposizioneFascicoloResult(CatenaDocumentaleDTO catenaDocumentaleDTO) {
		this.catenaDocumentaleDTO = catenaDocumentaleDTO;
	}
	
	public ComposizioneFascicoloResult(String errorMsg, Boolean error) {
		this.error = error;
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public boolean isError() {
		return error;
	}

	public CatenaDocumentaleDTO getCatenaDocumentaleDTO() {
		return catenaDocumentaleDTO;
	}
}
