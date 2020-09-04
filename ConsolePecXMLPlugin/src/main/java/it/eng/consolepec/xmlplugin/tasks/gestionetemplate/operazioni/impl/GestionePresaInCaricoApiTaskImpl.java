package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.DatiGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.GestionePresaInCaricoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;

public class GestionePresaInCaricoApiTaskImpl<T extends DatiGestioneAbstractTemplateTask> extends AbstractTemplateTaskApiImpl<T> implements GestionePresaInCaricoApiTask {

	public GestionePresaInCaricoApiTaskImpl(XMLGestioneAbstractTemplateTask<T> taskTemplate) {
		super(taskTemplate);
	}

	@Override
	public void prendiInCarico(Utente user) {
		generaEvento(EventiIterTemplate.PRENDI_IN_CARICO, task.getCurrentUser());
	}

	@Override
	public void rilasciaInCarico(Utente user) {
		generaEvento(EventiIterTemplate.RILASCIA_IN_CARICO, task.getCurrentUser());
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.getEnclosingPratica().getDati().getInCaricoA() == null;
	}

	@Override
	protected TipoApiTaskTemplate getTipoApiTask() {
		return TipoApiTaskTemplate.PRESA_IN_CARICO;
	}

}
