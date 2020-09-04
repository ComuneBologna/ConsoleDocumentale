package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RiconsegnaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class RiconsegnaTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements RiconsegnaTaskPECApi {

	public RiconsegnaTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public void riconsegna() {
		String idDocumentale = task.getEnclosingPratica().getDati().getIdDocumentale();
		if(!controllaAbilitazioneInterna())
			throw new PraticaException("Impossibile rifiutare la pratica (mancano le condizioni necessarie)");
		task.terminaSenzaRiattivazione();
		task.getEnclosingPratica().getDati().setStato(Stato.RICONSEGNATA);
		generaEvento( EventiIterPEC.RICONSEGNA, idDocumentale);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		DatiEmail dati = task.getEnclosingPratica().getDati();
		return dati.isNotificaRifiutoInoltro() && !dati.getDestinatariInoltro().isEmpty() && dati.getProgressivoInoltro() != null;
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.RICONSEGNA;
	}

}
