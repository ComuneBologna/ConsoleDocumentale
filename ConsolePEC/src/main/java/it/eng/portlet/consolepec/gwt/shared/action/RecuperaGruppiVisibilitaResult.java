package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.GruppoVisibilita;

import java.util.TreeSet;

import com.gwtplatform.dispatch.shared.Result;

public class RecuperaGruppiVisibilitaResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2490786662010322853L;

	private TreeSet<GruppoVisibilita> gruppoVisibilita = new TreeSet<GruppoVisibilita>();
	private String errorMessage;
	private boolean error;
	
	public RecuperaGruppiVisibilitaResult() {
		super();
	}

	public RecuperaGruppiVisibilitaResult(String errorMessage, boolean error) {
		super();
		this.errorMessage = errorMessage;
		this.error = error;
	}

	public TreeSet<GruppoVisibilita> getGruppoVisibilita() {
		return gruppoVisibilita;
	}

	public void setGruppoVisibilita(TreeSet<GruppoVisibilita> gruppoVisibilita) {
		this.gruppoVisibilita = gruppoVisibilita;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
