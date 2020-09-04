package it.eng.portlet.consolepec.gwt.shared.drive.action;

import com.gwtplatform.dispatch.shared.Result;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-07-09
 */
@NoArgsConstructor
public class EliminaElementoResult implements Result {

	private static final long serialVersionUID = -142344613377199863L;

	@Getter
	private String msgError = "";
	@Getter
	private boolean error = false;

	public EliminaElementoResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}

}
