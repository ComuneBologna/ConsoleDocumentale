package it.eng.portlet.consolepec.gwt.shared.action.pec;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import lombok.Getter;
import lombok.Setter;

import com.gwtplatform.dispatch.shared.Result;

/**
 *
 * @author biagiot
 *
 */

@Getter
@Setter
public class EstraiEMLResult implements Result{

	protected EstraiEMLResult() {
		// SER
	}

	public EstraiEMLResult(PraticaDTO pratica) {
		this.pratica = pratica;
		this.error = false;
		this.errorMessage = null;
	}

	public EstraiEMLResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}

	private static final long serialVersionUID = 1L;

	private String errorMessage;
	private boolean error;
	private PraticaDTO pratica;
}
