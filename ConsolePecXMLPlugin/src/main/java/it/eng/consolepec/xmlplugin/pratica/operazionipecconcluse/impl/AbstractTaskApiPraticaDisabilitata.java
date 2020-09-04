package it.eng.consolepec.xmlplugin.pratica.operazionipecconcluse.impl;

import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.AbstractTaskPECApiImpl;

public abstract class AbstractTaskApiPraticaDisabilitata<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> {

	public AbstractTaskApiPraticaDisabilitata(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public final boolean isOperazioneAbilitata() {
		if (controllaAbilitazioneUtente() && !task.isAttivo()) {
			return controllaAbilitazioneInterna();
		}
		return false;
	}

}
