package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;

public class XMLGestioneFascicoloElettoraleComunicazioniTask extends XMLTaskFascicolo<DatiGestioneFascicoloElettoraleComunicazioniTask> implements GestioneFascicoloElettoraleComunicazioniTask {

	@Override
	protected DatiGestioneFascicoloElettoraleComunicazioniTask getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloElettoraleComunicazioniTask.Builder builder = new DatiGestioneFascicoloElettoraleComunicazioniTask.Builder();
		builder.setAssegnatario(asscorrente);
		return builder.construct();
	}
}
