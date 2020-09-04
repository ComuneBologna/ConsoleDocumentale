package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.DatiGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.EliminaTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;

public class EliminaTaskTemplateApiImpl<T extends DatiGestioneAbstractTemplateTask> extends AbstractTemplateTaskApiImpl<T> implements EliminaTemplateTaskApi {

	public EliminaTaskTemplateApiImpl(XMLGestioneAbstractTemplateTask<T> task) {
		super(task);
	}

	@Override
	protected TipoApiTaskTemplate getTipoApiTask() {
		return TipoApiTaskTemplate.ELIMINA;
	}
	
	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

}
