package it.eng.portlet.consolepec.gwt.shared.action.rubrica;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 14/nov/2018
 */
@NoArgsConstructor
public class ImportaLagResult implements Result {

	private static final long serialVersionUID = -8767738748340107664L;

	@Getter private Anagrafica anagrafica;

	@Getter private String msgError = "";
	@Getter private boolean error = false;

	public ImportaLagResult(Anagrafica anagrafica) {
		super();
		this.anagrafica = anagrafica;
	}

	public ImportaLagResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}
}
