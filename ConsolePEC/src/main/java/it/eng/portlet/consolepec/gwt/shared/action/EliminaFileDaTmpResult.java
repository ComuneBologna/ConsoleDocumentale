package it.eng.portlet.consolepec.gwt.shared.action;

import lombok.Getter;

import com.gwtplatform.dispatch.shared.Result;

/**
 *
 * @author biagiot
 *
 */

@Getter
public class EliminaFileDaTmpResult implements Result {

	private static final long serialVersionUID = 1L;

	private boolean result;
	private String errorMessage;

	public EliminaFileDaTmpResult(String errorMessage) {
		this.errorMessage = errorMessage;
		this.result = false;
	}

	public EliminaFileDaTmpResult(boolean result) {
		this.result = result;
	}

	protected EliminaFileDaTmpResult() {
		// Ser
	}
}

