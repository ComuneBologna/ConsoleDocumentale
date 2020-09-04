package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class DatiGestioneFascicoloSportPortoTask extends DatiGestioneFascicoloTask {

	public DatiGestioneFascicoloSportPortoTask(Assegnatario assegnatario, List<Condivisione> condivisioni) {
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

		public DatiGestioneFascicoloSportPortoTask construct() {
			DatiGestioneFascicoloSportPortoTask dati = new DatiGestioneFascicoloSportPortoTask(assegnatario, condivisioni);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}

}
