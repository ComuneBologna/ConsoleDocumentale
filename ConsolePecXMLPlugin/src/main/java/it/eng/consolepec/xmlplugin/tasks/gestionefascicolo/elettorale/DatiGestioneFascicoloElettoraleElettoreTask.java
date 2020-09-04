package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class DatiGestioneFascicoloElettoraleElettoreTask extends DatiGestioneFascicoloTask {

	public DatiGestioneFascicoloElettoraleElettoreTask(Assegnatario assegnatario, List<Condivisione> condivisioni) {
		super(assegnatario, condivisioni);
	}
	
	public static class Builder extends DatiGestioneFascicoloTask.Builder {
		@Setter
		Assegnatario assegnatario;
		@Setter
		List<Condivisione> condivisioni = new ArrayList<DatiGestioneFascicoloTask.Condivisione>();

		@Setter
		Integer id;
		@Setter
		Boolean attivo;

		public Builder() {

		}

		public DatiGestioneFascicoloElettoraleElettoreTask construct() {
			DatiGestioneFascicoloElettoraleElettoreTask dati = new DatiGestioneFascicoloElettoraleElettoreTask(assegnatario, condivisioni);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}

}
