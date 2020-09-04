package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class DatiGestioneFascicoloSportRenoTask extends DatiGestioneFascicoloTask {

	public DatiGestioneFascicoloSportRenoTask(Assegnatario assegnatario, List<Condivisione> condivisioni) {
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

		public DatiGestioneFascicoloSportRenoTask construct() {
			DatiGestioneFascicoloSportRenoTask dati = new DatiGestioneFascicoloSportRenoTask(assegnatario, condivisioni);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}

}
