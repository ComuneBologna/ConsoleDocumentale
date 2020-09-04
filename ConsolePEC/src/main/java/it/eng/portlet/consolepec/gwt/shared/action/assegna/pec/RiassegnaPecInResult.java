package it.eng.portlet.consolepec.gwt.shared.action.assegna.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

public class RiassegnaPecInResult implements Result {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8091184682717165867L;

	private String messageError;
	private boolean error;
	private List<PecInDTO> listPecInDTO = new ArrayList<PecInDTO>();

	public RiassegnaPecInResult() {

	}

	public RiassegnaPecInResult(List<PecInDTO> listPecInDTO, String messErr, boolean isError) {
		this.listPecInDTO = listPecInDTO;
		this.messageError = messErr;
		this.error = isError;
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

	public void setListPecInDTO(List<PecInDTO> listPecInDTO) {
		this.listPecInDTO = listPecInDTO;
	}

	public List<PecInDTO> getListPecInDTO() {
		if (this.listPecInDTO == null)
			this.listPecInDTO = new ArrayList<PecInDTO>();
		return this.listPecInDTO;
	}

}
