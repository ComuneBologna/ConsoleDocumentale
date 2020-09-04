package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.DatiPraticaEmailTaskAdapter;
import it.eng.consolepec.xmlplugin.pratica.email.XMLPraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PECNonInviataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class PECNonInviataTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements PECNonInviataTaskApi {

	public PECNonInviataTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public void pecNonInviata(String messageId) {
		task.getEnclosingPratica().getDati().setStato(Stato.NON_INVIATA);
		DatiPraticaEmailTaskAdapter datiPraticaEmailTaskAdapter = (DatiPraticaEmailTaskAdapter) ((XMLPraticaEmail) task.getEnclosingPratica()).getDatiPraticaTaskAdapter();
		datiPraticaEmailTaskAdapter.setMessageId(messageId);
		generaEvento(EventiIterPEC.PEC_NON_INVIATA);

	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.PEC_NON_INVIATA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.INATTESADIPRESAINCARICO.equals(task.getEnclosingPratica().getDati().getStato());
	}

}
