package it.eng.portlet.consolepec.gwt.shared.action.profilazione;

import it.eng.cobo.consolepec.commons.profilazione.Utente;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author GiacomoFM
 * @since 20/ott/2017
 */
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CaricaDatiUtenteResult implements Result {

	private static final long serialVersionUID = -7881063950338455821L;

	private Utente utente;
	private boolean error;
	private String errorMessage;
	
	public CaricaDatiUtenteResult(Utente utente) {
		this.utente = utente;
		this.error = false;
		this.errorMessage = null;
	}
	
	public CaricaDatiUtenteResult(String errorMessage) {
		this.errorMessage = errorMessage;
		this.error = true;
	}
}
