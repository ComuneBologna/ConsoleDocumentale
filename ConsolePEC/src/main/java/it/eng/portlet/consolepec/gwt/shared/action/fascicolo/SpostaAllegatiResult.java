package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SpostaAllegatiResult implements Result {

	private static final long serialVersionUID = 222280685886054835L;

	private String errorMessage;
	private boolean error = false;
	private FascicoloDTO fascicoloDestinatario;

	public SpostaAllegatiResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
		this.fascicoloDestinatario = null;
	}

	public SpostaAllegatiResult(FascicoloDTO fascicoloDestinatario) {
		this.error = false;
		this.errorMessage = null;
		this.fascicoloDestinatario = fascicoloDestinatario;
	}
}
