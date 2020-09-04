package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.EliminaBozzaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class EliminaBozzaTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements EliminaBozzaTaskPECApi {

	public EliminaBozzaTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.ELIMINA_BOZZA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		PraticaEmail pratica = task.getEnclosingPratica();
		return pratica.getDati().getStato().equals(Stato.BOZZA);
	}

}
