package it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaRuoloResponse;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.gwtplatform.dispatch.shared.Result;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor(access=AccessLevel.PROTECTED)
public class AmministrazioneAnagraficaRuoloResult implements Result {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private boolean error = false;
	
	@Getter
	@Setter
	private String errorMessage;
	
	@Getter
	private List<AnagraficaRuolo> anagraficheRuolo = new ArrayList<AnagraficaRuolo>();
	
	@Getter
	private long count;
	
	@Getter
	private AnagraficaRuoloResponse anagraficaRuoloResponse;
	
	public AmministrazioneAnagraficaRuoloResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
	
	public AmministrazioneAnagraficaRuoloResult(List<AnagraficaRuolo> anagraficheRuolo, long count) {
		this.anagraficheRuolo = anagraficheRuolo;
		this.count = count;
	}
	
	public AmministrazioneAnagraficaRuoloResult(AnagraficaRuoloResponse anagraficaRuoloResponse) {
		this.anagraficaRuoloResponse = anagraficaRuoloResponse;
	}
}
