package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.StatoDestinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PecAccettataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

/**
 * @author Giacomo F.M.
 * @since 2019-09-17
 */
public class PecAccettataTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements PecAccettataTaskApi {

	public PecAccettataTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.PEC_ACCETTATA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	public void pecAccettata() {

		if (task.getEnclosingPratica().getDati().getDestinatarioPrincipale() != null) {
			impostaAccettazione(task.getEnclosingPratica().getDati().getDestinatarioPrincipale());
		}
		for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatari()) {
			impostaAccettazione(d);
		}
		for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatariCC()) {
			impostaAccettazione(d);
		}

		task.getEnclosingPratica().getDati().setRicevutaAccettazione(true);
		if (task.getEnclosingPratica().getDati().getStato().getIdStato() < Stato.PRESAINCARICO.getIdStato()) {
			task.getEnclosingPratica().getDati().setStato(Stato.PRESAINCARICO);

		} else if (task.getEnclosingPratica().getDati().getStato().equals(Stato.CONSEGNA_SENZA_ACCETTAZIONE)) {
			task.getEnclosingPratica().getDati().setStato(Stato.CONSEGNATA);
			task.termina();
		}
	}

	private static void impostaAccettazione(final Destinatario d) {
		d.setAccettazione(true);

		if (!StatoDestinatario.NON_CONSEGNATO.equals(d.getStatoDestinatario()) && !StatoDestinatario.CONSEGNATO.equals(d.getStatoDestinatario()))
			d.setStatoDestinatario(StatoDestinatario.ACCETTATO);
	}

}
