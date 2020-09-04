package it.eng.portlet.consolepec.gwt.shared.action.configurazioni;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.gwtplatform.dispatch.shared.Result;

/**
 * 
 * @author biagiot
 *
 */
@Data
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class CaricaAnagrafichePraticheResult implements Result {

	private static final long serialVersionUID = 1L;

	private List<AnagraficaFascicolo> anagraficheFascicoli = new ArrayList<>();
	private List<AnagraficaIngresso> anagraficheIngressi = new ArrayList<>();
	private List<AnagraficaModello> anagraficheModelli = new ArrayList<>();
	private List<AnagraficaEmailOut> anagraficheMailInUscita = new ArrayList<>();
	private List<AnagraficaComunicazione> anagraficheComunicazioni = new ArrayList<>();
	private List<AnagraficaPraticaModulistica> anagrafichePraticaModulistica = new ArrayList<>();
	private boolean error;
	private String errorMessage;
	
	public CaricaAnagrafichePraticheResult(List<AnagraficaFascicolo> anagraficheFascicoli, List<AnagraficaIngresso> anagraficheIngressi, List<AnagraficaModello> anagraficheModelli, List<AnagraficaEmailOut> anagraficheMailInUscita, List<AnagraficaComunicazione> anagraficheComunicazioni, List<AnagraficaPraticaModulistica> anagrafichePraticaModulistica) {
		this.anagraficheFascicoli = anagraficheFascicoli;
		this.anagraficheIngressi = anagraficheIngressi;
		this.anagraficheModelli = anagraficheModelli;
		this.anagraficheMailInUscita = anagraficheMailInUscita;
		this.anagraficheComunicazioni = anagraficheComunicazioni;
		this.anagrafichePraticaModulistica = anagrafichePraticaModulistica;
		this.error = false;
		this.errorMessage = null;
	}
	
	public CaricaAnagrafichePraticheResult(String errorMessage) {
		this.error = true;
		this.errorMessage = errorMessage;
	}
}
