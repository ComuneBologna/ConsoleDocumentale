package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import java.util.Date;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.DatiPraticaEmailTaskAdapter;
import it.eng.consolepec.xmlplugin.pratica.email.XMLPraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PECInviataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class PECInviataTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements PECInviataTaskApi {

	public PECInviataTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public void pecInviata(String messageId, Date dataInvio) {
		task.getEnclosingPratica().getDati().setStato(Stato.PRESAINCARICO);
		task.getEnclosingPratica().getDati().setDataInvio(dataInvio);
		DatiPraticaEmailTaskAdapter datiPraticaEmailTaskAdapter = (DatiPraticaEmailTaskAdapter) ((XMLPraticaEmail) task.getEnclosingPratica()).getDatiPraticaTaskAdapter();
		datiPraticaEmailTaskAdapter.setMessageId(messageId);
		generaEvento(EventiIterPEC.PEC_INVIATA);
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.PEC_INVIATA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.INATTESADIPRESAINCARICO.equals(task.getEnclosingPratica().getDati().getStato());
	}

}
