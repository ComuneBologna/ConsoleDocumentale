package it.eng.portlet.consolepec.gwt.shared.action.profilazione;

import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * 
 * @author biagiot
 * 
 */
@Data
@NoArgsConstructor
public class CaricaAbilitazioniUtenteResult implements Result {

	private static final long serialVersionUID = 1L;

	private AutorizzazioneHandler autorizzazioneHandler;
	private boolean error;
	private String errorMessage;

	public CaricaAbilitazioniUtenteResult(AutorizzazioneHandler autorizzazioneHandler) {
		this.autorizzazioneHandler = autorizzazioneHandler;
		this.error = false;
		this.errorMessage = null;
	}

	public CaricaAbilitazioniUtenteResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
}
