package it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.DatiGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.CreaComunicazioneDaTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;

public class CreaTemplatePerCopiaTaskApiImpl<T extends DatiGestioneAbstractTemplateTask> extends AbstractTemplateTaskApiImpl<T> implements CreaComunicazioneDaTemplateTaskApi {

	public CreaTemplatePerCopiaTaskApiImpl(XMLGestioneAbstractTemplateTask<T> task) {
		super(task);
	}
	
	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTaskTemplate getTipoApiTask() {
		return TipoApiTaskTemplate.CREA_TEMPLATE_PER_COPIA;
	}

}
