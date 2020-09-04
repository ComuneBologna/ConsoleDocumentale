package it.eng.portlet.consolepec.gwt.shared.action.modulistica;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaModulisticaDTO;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class CambiaStatoPraticaModulisticaResult implements Result {

	private static final long serialVersionUID = 1L;
	private PraticaModulisticaDTO praticaModulistica;
	private List<PraticaModulisticaDTO> praticheOperazioneMassiva = new ArrayList<PraticaModulisticaDTO>();
	private boolean error;
	private String errorMsg;
	private ArrayList<String> clientIdEliminati;

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

	public void setPraticaModulistica(PraticaModulisticaDTO praticaModulistica) {
		this.praticaModulistica = praticaModulistica;
	}

	@SuppressWarnings("unused")
	private CambiaStatoPraticaModulisticaResult() {
		// For serialization only
	}

	public CambiaStatoPraticaModulisticaResult(PraticaModulisticaDTO praticaModulistica, boolean error, String errorMsg) {
		this.praticaModulistica = praticaModulistica;
		this.error = error;
		this.errorMsg = errorMsg;
	}
	
	public CambiaStatoPraticaModulisticaResult(PraticaModulisticaDTO praticaModulistica){
		this.praticaModulistica = praticaModulistica;
		this.error = false;
		this.errorMsg = null;
	}
	public CambiaStatoPraticaModulisticaResult(List<PraticaModulisticaDTO> praticheOperazioneMassiva){
		this.praticheOperazioneMassiva = praticheOperazioneMassiva;
		this.error = false;
		this.errorMsg = null;
	}
	
	public PraticaModulisticaDTO getPraticaModulistica() {
		return praticaModulistica;
	}

	public ArrayList<String> getClientIdEliminati() {
		return clientIdEliminati;
	}

	public void setClientIdEliminati(ArrayList<String> clientIdEliminati) {
		this.clientIdEliminati = clientIdEliminati;
	}

	public List<PraticaModulisticaDTO> getPraticheOperazioneMassiva() {
		return praticheOperazioneMassiva;
	}

	

}
