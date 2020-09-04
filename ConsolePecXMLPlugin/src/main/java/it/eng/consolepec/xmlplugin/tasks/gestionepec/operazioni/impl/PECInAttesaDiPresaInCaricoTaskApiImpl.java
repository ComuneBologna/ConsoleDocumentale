package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import java.util.Date;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.StatoDestinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PECInAttesaDiPresaInCaricoTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

public class PECInAttesaDiPresaInCaricoTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements PECInAttesaDiPresaInCaricoTaskApi {

	public PECInAttesaDiPresaInCaricoTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	public void pecInAttesaDiPresaInCarico(Integer identificativoEmail) {
		task.getEnclosingPratica().getDati().setStato(Stato.INATTESADIPRESAINCARICO);
		task.getEnclosingPratica().getDati().setIdEmailServer(identificativoEmail);
		task.getEnclosingPratica().getDati().setDataInvio(new Date());

		for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatari())
			d.setStatoDestinatario(StatoDestinatario.INVIATO);
		for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatariCC())
			d.setStatoDestinatario(StatoDestinatario.INVIATO);
		if (task.getEnclosingPratica().getDati().getDestinatarioPrincipale() != null)
			task.getEnclosingPratica().getDati().getDestinatarioPrincipale().setStatoDestinatario(StatoDestinatario.INVIATO);

		generaEvento(EventiIterPEC.PEC_IN_ATTESA_PRESA_IN_CARICO);
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.PEC_IN_ATTESA_PRESA_IN_CARICO;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return Stato.DA_INVIARE.equals(task.getEnclosingPratica().getDati().getStato());
	}

}
