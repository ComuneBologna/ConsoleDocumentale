package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.EliminaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

import java.util.Date;

public class EliminaTaskPECApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements EliminaTaskPECApi {

	public EliminaTaskPECApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.ELIMINA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		
		PraticaEmail pratica = task.getEnclosingPratica();
		if (!pratica.hasPraticheCollegate()) {
			return pratica.getDati().getStato().name()
					.equals(Stato.ARCHIVIATA.name())
					|| pratica.getDati().getStato().name()
							.equals(Stato.IN_GESTIONE.name());
		}
		return false;
	}

	@Override
	public void elimina() {
		task.termina();
		task.getEnclosingPratica().getDati().setStato(Stato.ELIMINATA);
		task.getDati().getAssegnatario().setDataFine(new Date());

		generaEvento(EventiIterPEC.ELIMINA, task.getCurrentUser());
	}

}
