package it.eng.portlet.consolepec.gwt.shared.action;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SalvaNoteResult implements Result {

	private static final long serialVersionUID = -1136912092169215995L;

	private boolean error;
	private String errorMessage;

	private FascicoloDTO fascicolo;

	public SalvaNoteResult(FascicoloDTO fascicolo) {
		this.fascicolo = fascicolo;
		this.error = false;
		this.errorMessage = null;
	}

	public SalvaNoteResult(String errorMessage) {
		this.fascicolo = null;
		this.error = true;
		this.errorMessage = errorMessage;
	}
}
