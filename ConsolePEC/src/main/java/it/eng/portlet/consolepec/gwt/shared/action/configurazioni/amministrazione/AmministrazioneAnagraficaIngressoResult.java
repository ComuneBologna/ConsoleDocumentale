package it.eng.portlet.consolepec.gwt.shared.action.configurazioni.amministrazione;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaIngressoResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AmministrazioneAnagraficaIngressoResult implements Result {

	private static final long serialVersionUID = 1L;

	@Getter
	private AnagraficaIngressoResponse anagraficaIngressoResponse;

	@Getter
	private List<AnagraficaIngresso> anagraficheIngressi = new ArrayList<AnagraficaIngresso>();

	@Getter
	private long count;

	@Getter
	private boolean error = false;

	@Getter
	private String errore;

	public AmministrazioneAnagraficaIngressoResult(String errore) {
		this.error = true;
		this.errore = errore;
	}

	public AmministrazioneAnagraficaIngressoResult(List<AnagraficaIngresso> anagraficheIngressi, long count) {
		this.anagraficheIngressi = anagraficheIngressi;
		this.count = count;
	}

	public AmministrazioneAnagraficaIngressoResult(AnagraficaIngressoResponse anagraficaIngresso) {
		this.anagraficaIngressoResponse = anagraficaIngresso;
	}

}
