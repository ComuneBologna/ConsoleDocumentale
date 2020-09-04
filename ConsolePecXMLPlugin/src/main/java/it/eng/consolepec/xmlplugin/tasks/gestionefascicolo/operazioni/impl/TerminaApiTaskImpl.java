package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TerminaApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.DatiRiattivazioneTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaFascicoloTask;

import java.util.Date;

public class TerminaApiTaskImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements TerminaApiTask {

	public TerminaApiTaskImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void termina() {
		task.getDati().setAttivo(false);
		task.getDati().getAssegnatario().setDataFine(new Date());
		DatiRiattivazioneTask.Builder builderTask = new DatiRiattivazioneTask.Builder();
		builderTask.setAssegnatario(new Assegnatario(task.getDati().getAssegnatario().getNome(), task.getDati().getAssegnatario().getEtichetta(), new Date(System.currentTimeMillis() - 1000), null));
		builderTask.setAttivo(true);
		builderTask.setIdTaskDaRiattivare(task.getDati().getId());
		DatiRiattivazioneTask riportaInGestioneTask = builderTask.construct();
		XMLTaskFactory xtf = new XMLTaskFactory();
		xtf.newTaskInstance(RiattivaFascicoloTask.class, task.getEnclosingPratica(), riportaInGestioneTask);

	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.TERMINA;
	}

}
