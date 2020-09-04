package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.DatiGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.ModificaTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;

public class ModificaTaskTemplateApiImpl<T extends DatiGestioneAbstractTemplateTask> extends AbstractTemplateTaskApiImpl<T> implements ModificaTemplateTaskApi {

	public ModificaTaskTemplateApiImpl(XMLGestioneAbstractTemplateTask<T> task) {
		super(task);
	}

	@Override
	protected TipoApiTaskTemplate getTipoApiTask() {
		return TipoApiTaskTemplate.MODIFICA;
	}
	
	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	public void modificato() {
		generaEvento(EventiIterTemplate.MODIFICA, task.getCurrentUser());
	}

}
