package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.gwtplatform.dispatch.shared.Result;

public class ProtocollaFascicoloNuovoActionResult implements Result {

	private static final long serialVersionUID = 8631605322501163467L;

	private String numeroPgCapofila;
	private String annoPgCapofila;
	private String numeroPg;
	private String annoPg;

	private String messageError;
	private boolean error;

	private String warningMessage;
	private boolean warning;

	private PecInDTO pecInDTO;
	private PecOutDTO pecOutDTO;
	private FascicoloDTO fascicoloDTO;

	public ProtocollaFascicoloNuovoActionResult() {
	}

	public String getNumeroPgCapofila() {
		return numeroPgCapofila;
	}

	public void setNumeroPgCapofila(String numeroPgCapofila) {
		this.numeroPgCapofila = numeroPgCapofila;
	}

	public String getAnnoPgCapofila() {
		return annoPgCapofila;
	}

	public void setAnnoPgCapofila(String annoPgCapofila) {
		this.annoPgCapofila = annoPgCapofila;
	}

	public String getNumeroPg() {
		return numeroPg;
	}

	public void setNumeroPg(String numeroPg) {
		this.numeroPg = numeroPg;
	}

	public String getAnnoPg() {
		return annoPg;
	}

	public void setAnnoPg(String annoPg) {
		this.annoPg = annoPg;
	}

	public String getMessageError() {
		return messageError;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public Boolean getError() {
		return error;
	}

	public void setError(Boolean error) {
		this.error = error;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

	public Boolean getWarning() {
		return warning;
	}

	public void setWarning(Boolean warning) {
		this.warning = warning;
	}

	public PecInDTO getPecInDTO() {
		return pecInDTO;
	}

	public void setPecInDTO(PecInDTO pecInDTO) {
		this.pecInDTO = pecInDTO;
	}

	public PecOutDTO getPecOutDTO() {
		return pecOutDTO;
	}

	public void setPecOutDTO(PecOutDTO pecOutDTO) {
		this.pecOutDTO = pecOutDTO;
	}

	public FascicoloDTO getFascicoloDTO() {
		return fascicoloDTO;
	}

	public void setFascicoloDTO(FascicoloDTO fascicoloDTO) {
		this.fascicoloDTO = fascicoloDTO;
	}

}
