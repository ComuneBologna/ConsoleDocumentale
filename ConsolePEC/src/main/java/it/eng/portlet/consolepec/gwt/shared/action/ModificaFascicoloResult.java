package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author GiacomoFM
 * @since 13/lug/2017
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ModificaFascicoloResult implements Result {

	private static final long serialVersionUID = 6757765302595464598L;

	@Getter
	private PraticaDTO pratica;
	@Getter
	private String msgError;
	@Getter
	private boolean error = false;

	public ModificaFascicoloResult(PraticaDTO pratica) {
		super();
		this.pratica = pratica;
	}

	public ModificaFascicoloResult(String msgError) {
		super();
		error = true;
		this.msgError = msgError;
	}

}
