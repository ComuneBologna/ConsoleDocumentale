package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.gwtplatform.dispatch.shared.Result;

public class ModificaOperatoreResult  implements Result {
	
	private static final long serialVersionUID = 1L;
	private String msgError;
	private boolean error = false;
	private PraticaDTO pratica;
	
	protected ModificaOperatoreResult() {

	}

	public ModificaOperatoreResult(PraticaDTO pratica) {
		super();
		this.pratica = pratica;
	}

	public ModificaOperatoreResult(String msgError) {
		super();
		this.msgError = msgError;
		this.error = true;
	}

	public String getMsgError() {
		return msgError;
	}

	public boolean isError() {
		return error;
	}

	public PraticaDTO getPratica() {
		return pratica;
	}
	
	
	
}