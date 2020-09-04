package it.eng.portlet.consolepec.gwt.shared.drive.action;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author Giacomo F.M.
 * @since 2019-06-12
 */
@NoArgsConstructor
public class AggiornaCartellaResult implements Result {

	private static final long serialVersionUID = -923139441374808046L;

	@Getter
	private Cartella cartella;

	@Getter
	private String msgError = "";
	@Getter
	private boolean error = false;

	public AggiornaCartellaResult(Cartella cartella) {
		super();
		this.cartella = cartella;
	}

	public AggiornaCartellaResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}

}
