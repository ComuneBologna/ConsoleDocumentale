package it.eng.portlet.consolepec.gwt.shared.action.rubrica;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author GiacomoFM
 * @since 16/ott/2017
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreaAnagraficaResult implements Result {

	private static final long serialVersionUID = -6548974561200894563L;

	@Getter private Anagrafica anagrafica;

	@Getter private String msgError;
	@Getter private boolean error = false;

	public CreaAnagraficaResult(Anagrafica anagrafica) {
		super();
		this.anagrafica = anagrafica;
	}

	public CreaAnagraficaResult(String msgError) {
		super();
		error = true;
		this.msgError = msgError;
	}
}
