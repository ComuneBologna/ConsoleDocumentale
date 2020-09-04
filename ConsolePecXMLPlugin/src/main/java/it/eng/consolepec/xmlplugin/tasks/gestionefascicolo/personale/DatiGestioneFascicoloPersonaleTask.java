package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.personale;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;

import java.util.ArrayList;

import lombok.Setter;

public class DatiGestioneFascicoloPersonaleTask extends DatiGestioneFascicoloTask {

	public DatiGestioneFascicoloPersonaleTask(Assegnatario assegnatario) {
		super(assegnatario, new ArrayList<Condivisione>());
	}

	public static class Builder extends DatiGestioneFascicoloTask.Builder {
		@Setter
		Assegnatario assegnatario;

		@Setter
		Integer id;
		@Setter
		Boolean attivo;

		public Builder() {

		}

		public DatiGestioneFascicoloPersonaleTask construct() {
			DatiGestioneFascicoloPersonaleTask dati = new DatiGestioneFascicoloPersonaleTask(assegnatario);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}
}
