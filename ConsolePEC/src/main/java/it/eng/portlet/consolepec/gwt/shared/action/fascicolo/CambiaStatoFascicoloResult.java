package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class CambiaStatoFascicoloResult implements Result {

	private static final long serialVersionUID = 1L;
	private FascicoloDTO fascicolo;
	private List<FascicoloDTO> fascicoli = new ArrayList<FascicoloDTO>();
	private boolean error;
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

	private String errorMsg;
	private ArrayList<String> clientIdEliminati;



	@SuppressWarnings("unused")
	private CambiaStatoFascicoloResult() {
		// For serialization only
	}

	public CambiaStatoFascicoloResult(FascicoloDTO fascicolo, boolean error, String errorMsg) {
		this.fascicolo = fascicolo;
		this.error = error;
		this.errorMsg = errorMsg;
	}
	
	public CambiaStatoFascicoloResult(FascicoloDTO fascicolo){
		this.fascicolo = fascicolo;
		this.error = false;
		this.errorMsg = null;
	}
	
	public void addFascicolo(FascicoloDTO fascicolo){
		this.fascicoli.add(fascicolo);
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}

	
	public List<FascicoloDTO> getFascicoli() {
	
		return fascicoli;
	}

	
	public void setFascicoli(List<FascicoloDTO> fascicoli) {
	
		this.fascicoli = fascicoli;
	}
	
	public ArrayList<String> getClientIdEliminati() {
		
		return clientIdEliminati;
	}

	
	public void setClientIdEliminati(ArrayList<String> clientIdEliminati) {
	
		this.clientIdEliminati = clientIdEliminati;
	}

}
