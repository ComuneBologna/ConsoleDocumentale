package it.eng.portlet.consolepec.gwt.shared.action.rubrica;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author GiacomoFM
 * @since 17/ott/2017
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModificaAnagraficaResult implements Result {

	private static final long serialVersionUID = 1505727341506157234L;

	@Getter private Anagrafica anagrafica;

	@Getter private String msgError;
	@Getter private boolean error = false;

	public ModificaAnagraficaResult(Anagrafica anagrafica) {
		super();
		this.anagrafica = anagrafica;
	}

	public ModificaAnagraficaResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}
}
