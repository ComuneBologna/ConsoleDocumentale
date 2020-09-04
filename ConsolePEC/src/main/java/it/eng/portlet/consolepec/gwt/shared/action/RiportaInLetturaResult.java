package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import com.gwtplatform.dispatch.shared.Result;

public class RiportaInLetturaResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8091184682717165867L;

	private String messageError;
	private boolean error;
	private FascicoloDTO fascicolo;
	private PecInDTO pecIn;
	
	public RiportaInLetturaResult() {

	}

	public RiportaInLetturaResult(String messErr) {
		this.messageError = messErr;
		this.error = true;
	}
	
	public RiportaInLetturaResult(FascicoloDTO fascicolo) {
		super();
		this.fascicolo = fascicolo;
	}

	public RiportaInLetturaResult(PecInDTO pecIn) {
		super();
		this.pecIn = pecIn;
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}

	public PecInDTO getPecIn() {
		return pecIn;
	}

	public String getMessageError() {
		return messageError;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

}
