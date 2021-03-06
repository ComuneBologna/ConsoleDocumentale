package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class DatiGestioneFascicoloSportSaragozzaTask extends DatiGestioneFascicoloTask {

	public DatiGestioneFascicoloSportSaragozzaTask(Assegnatario assegnatario, List<Condivisione> condivisioni) {
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

		public DatiGestioneFascicoloSportSaragozzaTask construct() {
			DatiGestioneFascicoloSportSaragozzaTask dati = new DatiGestioneFascicoloSportSaragozzaTask(assegnatario, condivisioni);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}

}
