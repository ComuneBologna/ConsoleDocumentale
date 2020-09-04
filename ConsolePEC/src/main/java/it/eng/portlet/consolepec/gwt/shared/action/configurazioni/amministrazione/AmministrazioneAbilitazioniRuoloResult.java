package it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.util.AbilitazioneRuoloSingola;

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
public class AmministrazioneAbilitazioniRuoloResult implements Result {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	private boolean error = false;
	
	@Getter
	@Setter
	private String errorMessage;
	
	@Getter
	private List<AbilitazioniRuolo> abilitazioniRuoli = new ArrayList<AbilitazioniRuolo>();
	
	@Getter
	private List<AbilitazioneRuoloSingola> abilitazioniRuoloSingole = new ArrayList<AbilitazioneRuoloSingola>();
	
	@Getter
	private int count;
	
	@Getter
	private AbilitazioniRuolo abilitazioniRuolo;
	
	
	public AmministrazioneAbilitazioniRuoloResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
	
	public AmministrazioneAbilitazioniRuoloResult(AbilitazioniRuolo abilitazioniRuolo) {
		this.abilitazioniRuolo = abilitazioniRuolo;
	}
	
	public AmministrazioneAbilitazioniRuoloResult(List<AbilitazioniRuolo> abilitazioniRuoli) {
		this.abilitazioniRuoli = abilitazioniRuoli;
	}
	
	public AmministrazioneAbilitazioniRuoloResult(List<AbilitazioneRuoloSingola> abilitazioniRuoloSingole, int count) {
		this.abilitazioniRuoloSingole = abilitazioniRuoloSingole;
		this.count = count;
	}
}
