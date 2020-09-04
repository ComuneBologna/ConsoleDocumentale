package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.modulistica;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;

import java.util.ArrayList;
import java.util.List;

import lombok.Setter;

public class DatiGestioneFascicoloModulisticaTask extends DatiGestioneFascicoloTask {

	public DatiGestioneFascicoloModulisticaTask(Assegnatario assegnatario, List<Condivisione> condivisioni) {
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

		public DatiGestioneFascicoloModulisticaTask construct() {
			DatiGestioneFascicoloModulisticaTask dati = new DatiGestioneFascicoloModulisticaTask(assegnatario, condivisioni);
			dati.setAttivo(attivo);
			dati.setId(id);
			return dati;
		}
	}

}
