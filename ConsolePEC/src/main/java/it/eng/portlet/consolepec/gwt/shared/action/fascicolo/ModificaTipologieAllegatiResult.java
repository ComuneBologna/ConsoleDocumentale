package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import com.gwtplatform.dispatch.shared.Result;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author GiacomoFM
 * @since 11/dic/2018
 */
@NoArgsConstructor
public class ModificaTipologieAllegatiResult implements Result {

	private static final long serialVersionUID = -8129631877122245314L;

	@Getter private boolean error = false;
	@Getter private String errorMessage;

	public ModificaTipologieAllegatiResult(String errorMessage) {
		this.errorMessage = errorMessage;
		this.error = true;
	}

}
