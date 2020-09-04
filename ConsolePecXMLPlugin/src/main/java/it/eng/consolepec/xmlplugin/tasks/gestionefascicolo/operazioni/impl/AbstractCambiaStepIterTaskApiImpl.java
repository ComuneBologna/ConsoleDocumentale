package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione.CambiaStepIterCondizioneEsecuzione;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.ApprovazioneTaskException;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.StepIter;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.CambiaStepIterTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;

public abstract class AbstractCambiaStepIterTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements CambiaStepIterTaskApi {

	public AbstractCambiaStepIterTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	private void esecuzioneInterna(String step, boolean finale, boolean iniziale, boolean creaBozza, List<String> destinatariNotifica) {

		getDatiFascicolo().setStepIter(new StepIter(step, finale, iniziale, creaBozza, destinatariNotifica, new Date()));

		if (finale) {

			if (TaskDiFirmaUtil.hasApprovazioneFirmaTask(this.task.getEnclosingPratica()))
				throw new ApprovazioneTaskException("Non è possibile archiviare il fascicolo perchè è in corso una richiesta di approvazione");

			// archivia
			if (this.task.controllaAbilitazione(TipoApiTask.CONCLUDI_FASCICOLO)) {
				task.concludiFascicolo();

			} else {
				throw new PraticaException("Utente non abilitato all'archiviazione del fascicolo");
			}

		} else {
			task.getEnclosingPratica().getDati().setLetto(false);

		}
	}

	@Override
	public void cambiaStep(String step, boolean finale, boolean iniziale, boolean creaBozza, List<String> destinatariNotifica) {
		esecuzioneInterna(step, finale, iniziale, creaBozza, destinatariNotifica);
		eseguiEsecuzione(new CambiaStepIterCondizioneEsecuzione(step));
		ParametriExtra parametriExtra = new ParametriExtra();
		parametriExtra.getIndirizziEmail().addAll(destinatariNotifica);
		generaEvento(parametriExtra, EventiIterFascicolo.CAMBIA_STEP_ITER_CON_NOTIFICA, task.getCurrentUser(), step.toUpperCase(), GenericsUtil.convertCollectionToString(destinatariNotifica));
	}

	@Override
	public void cambiaStep(String step, boolean finale, boolean iniziale, boolean creaBozza) {
		esecuzioneInterna(step, finale, iniziale, creaBozza, new ArrayList<String>());
		eseguiEsecuzione(new CambiaStepIterCondizioneEsecuzione(step));
		generaEvento(EventiIterFascicolo.CAMBIA_STEP_ITER, task.getCurrentUser(), step.toUpperCase());
	}
}
