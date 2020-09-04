package it.eng.portlet.consolepec.gwt.shared.action.configurazioni;

import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 *
 * @author biagiot
 *
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CaricaProprietaGeneraliResult implements Result {
	private static final long serialVersionUID = 1L;

	private ProprietaGenerali generalProperties;
	private String baseUrlPubblicazioneAllegato;
	
	private boolean error;
	private String errorMessage;

	public CaricaProprietaGeneraliResult(ProprietaGenerali generalProperties, String baseUrlPubblicazioneAllegato) {
		this.generalProperties = generalProperties;
		this.baseUrlPubblicazioneAllegato = baseUrlPubblicazioneAllegato;
		this.error = false;
		this.errorMessage = null;
	}

	public CaricaProprietaGeneraliResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
		this.generalProperties = null;
	}
}
