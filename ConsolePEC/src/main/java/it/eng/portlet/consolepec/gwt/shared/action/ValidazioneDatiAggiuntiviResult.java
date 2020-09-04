package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.ValidazioneDatoAggiuntivoDTO;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class ValidazioneDatiAggiuntiviResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String messError;
	private Boolean error;
	
	private List<ValidazioneDatoAggiuntivoDTO> validazioneDatiAggiuntivi = new ArrayList<ValidazioneDatoAggiuntivoDTO>();
	private List<String> erroriDaVisualizzare = new ArrayList<String>();
	
	@SuppressWarnings("unused")
	private ValidazioneDatiAggiuntiviResult() {
	
		// For serialization only
	}

	public ValidazioneDatiAggiuntiviResult(String messError) {
	
		this.messError = messError;
		this.error = true;
	}

	/*public ValidazioneDatiAggiuntiviResult(List<ValidazioneDatoAggiuntivoDTO> validazioneDatiAggiuntivi) {
		super();
		this.validazioneDatiAggiuntivi = validazioneDatiAggiuntivi;
		this.error = false;
	}*/
	public ValidazioneDatiAggiuntiviResult(List<ValidazioneDatoAggiuntivoDTO> validazioneDatiAggiuntivi,  List<String> erroriDaVisualizzare) {
		super();
		this.validazioneDatiAggiuntivi = validazioneDatiAggiuntivi;
		this.erroriDaVisualizzare = erroriDaVisualizzare;
		this.error = false;
	}
	
	public List<String> getErroriDaVisualizzare() {
		return erroriDaVisualizzare;
	}

	public String getMessError() {
	
		return messError;
	}

	public Boolean getError() {
	
		return error;
	}

	public List<ValidazioneDatoAggiuntivoDTO> getValidazioneDatiAggiuntivi() {
		return validazioneDatiAggiuntivi;
	}
	
}
