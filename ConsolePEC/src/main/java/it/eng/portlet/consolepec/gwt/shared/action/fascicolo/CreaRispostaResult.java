package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.shared.Result;

public class CreaRispostaResult implements Result {

	private static final long serialVersionUID = 1L;
	private FascicoloDTO fascicoloDTO;
	private PecOutDTO dettaglioMail;
	private String messError;
	private Boolean error;

	@SuppressWarnings("unused")
	private CreaRispostaResult() {
		// For serialization only
	}

	public CreaRispostaResult(FascicoloDTO fascicolo, PecOutDTO dettaglioMail, String messError, Boolean error) {
		this.dettaglioMail = dettaglioMail;
		this.messError = messError;
		this.error = error;
		this.fascicoloDTO = fascicolo;
	}

	public PecOutDTO getPecOutDTO() {
		return dettaglioMail;
	}

	public String getMessError() {
		return messError;
	}

	public Boolean getError() {
		return error;
	}

	public FascicoloDTO getFascicoloDTO() {
		return fascicoloDTO;
	}

	public void setFascicoloDTO(FascicoloDTO fascicoloDTO) {
		this.fascicoloDTO = fascicoloDTO;
	}
}
