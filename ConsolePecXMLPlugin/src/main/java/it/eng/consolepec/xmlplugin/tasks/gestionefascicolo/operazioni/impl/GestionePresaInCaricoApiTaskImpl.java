package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.GestionePresaInCaricoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class GestionePresaInCaricoApiTaskImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements GestionePresaInCaricoApiTask {

	public GestionePresaInCaricoApiTaskImpl(XMLTaskFascicolo<T> xmlTaskFascicolo) {
		super(xmlTaskFascicolo);
	}

	@Override
	public void prendiInCarico(Utente user) {
		generaEvento(EventiIterFascicolo.PRENDI_IN_CARICO, task.getCurrentUser());
	}

	@Override
	public void rilasciaInCarico(Utente user) {
		generaEvento( EventiIterFascicolo.RILASCIA_IN_CARICO, task.getCurrentUser());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().getDati().getInCaricoA() == null;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.PRESA_IN_CARICO;
	}

}
