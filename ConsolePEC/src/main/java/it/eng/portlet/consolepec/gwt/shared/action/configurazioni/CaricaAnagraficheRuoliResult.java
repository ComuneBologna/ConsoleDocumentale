package it.eng.portlet.consolepec.gwt.shared.action.configurazioni;

import java.util.ArrayList;
import java.util.List;

import com.gwtplatform.dispatch.shared.Result;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author biagiot
 *
 */
@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CaricaAnagraficheRuoliResult implements Result {

	private static final long serialVersionUID = 1L;

	private List<AnagraficaRuolo> anagraficheRuoli = new ArrayList<>();
	private List<AnagraficaRuolo> ruoliPersonali = new ArrayList<>();
	private boolean error;
	private String errorMessage;

	public CaricaAnagraficheRuoliResult(List<AnagraficaRuolo> anagraficheRuoli, List<AnagraficaRuolo> ruoliPersonali) {
		this.anagraficheRuoli = anagraficheRuoli;
		this.ruoliPersonali = ruoliPersonali;
		this.error = false;
		this.errorMessage = null;
	}

	public CaricaAnagraficheRuoliResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
}
