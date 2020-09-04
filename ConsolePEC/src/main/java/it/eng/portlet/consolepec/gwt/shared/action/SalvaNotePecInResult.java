package it.eng.portlet.consolepec.gwt.shared.action;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SalvaNotePecInResult implements Result {

	private static final long serialVersionUID = 4518610903137229248L;

	private boolean error;
	private String errorMessage;

	private PecInDTO pecInDTO;

	public SalvaNotePecInResult(PecInDTO pecInDTO) {
		this.pecInDTO = pecInDTO;
		this.error = false;
		this.errorMessage = null;
	}

	public SalvaNotePecInResult(String errorMessage) {
		this.pecInDTO = null;
		this.error = true;
		this.errorMessage = errorMessage;
	}
}
