package it.eng.portlet.consolepec.gwt.shared.drive.action;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.drive.DriveElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-05-31
 */
@NoArgsConstructor
public class CercaElementoResult implements Result {

	private static final long serialVersionUID = -7721950452964698193L;

	@Getter
	private DriveElement elemento;

	@Getter
	private String msgError = "";
	@Getter
	private boolean error = false;

	public CercaElementoResult(DriveElement elemento) {
		super();
		this.elemento = elemento;
	}

	public CercaElementoResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}

}
