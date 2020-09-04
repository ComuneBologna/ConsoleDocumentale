package it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class AmministrazioneAnagraficaFascicoloResult implements Result {
	
	private static final long serialVersionUID = 1L;

	@Getter
	private AnagraficaFascicolo anagraficaFascicolo;
	
	@Getter
	private List<AnagraficaFascicolo> anagraficheFascicoli = new ArrayList<AnagraficaFascicolo>();
	
	@Getter
	private long count;
	
	@Getter
	private boolean error = false;
	
	@Getter
	private String messageError;
	
	public AmministrazioneAnagraficaFascicoloResult(String messageError) {
		this.error = true;
		this.messageError = messageError;
	}
	
	public AmministrazioneAnagraficaFascicoloResult(List<AnagraficaFascicolo> anagraficheFascicoli, long count) {
		this.anagraficheFascicoli = anagraficheFascicoli;
		this.count = count;
	}
	
	public AmministrazioneAnagraficaFascicoloResult(AnagraficaFascicolo anagraficaFascicolo) {
		this.anagraficaFascicolo = anagraficaFascicolo;
	}
	
}
