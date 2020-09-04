package it.eng.portlet.consolepec.gwt.shared.drive.action;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-06-25
 */
@NoArgsConstructor
public class CreaCartellaResult implements Result {

	private static final long serialVersionUID = 2321434311256825358L;

	@Getter
	private Cartella cartella;

	@Getter
	private String msgError = "";
	@Getter
	private boolean error = false;

	public CreaCartellaResult(Cartella cartella) {
		super();
		this.cartella = cartella;
	}

	public CreaCartellaResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}

}
