package it.eng.portlet.consolepec.gwt.shared.action.rubrica;

import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author GiacomoFM
 * @since 27/ott/2017
 */
@NoArgsConstructor
public class EliminaAnagraficaResult implements Result {

	private static final long serialVersionUID = 3114191963999691252L;

	@Getter private String msgError;
	@Getter private boolean error = false;

	public EliminaAnagraficaResult(String msgError) {
		super();
		this.error = true;
		this.msgError = msgError;
	}
}
