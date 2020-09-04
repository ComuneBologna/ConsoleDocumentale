package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RispondiEmailDaTemplateTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;

public class RispondiEmailDaTemplateTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RispondiEmailDaTemplateTaskApi {

	public RispondiEmailDaTemplateTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.RISPONDI_MAIL_DA_TEMPLATE;
	}

}
