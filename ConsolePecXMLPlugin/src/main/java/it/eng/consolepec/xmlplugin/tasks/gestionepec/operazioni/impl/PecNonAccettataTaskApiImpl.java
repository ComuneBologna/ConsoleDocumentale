package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.StatoDestinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PecNonAccettataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

/**
 * @author Giacomo F.M.
 * @since 2019-09-17
 */
public class PecNonAccettataTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements PecNonAccettataTaskApi {

	public PecNonAccettataTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.PEC_NON_ACCETTATA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	public void pecNonAccettata(String errore) {
		if (task.getEnclosingPratica().getDati().getDestinatarioPrincipale() != null) {
			impostaNonAccettazione(task.getEnclosingPratica().getDati().getDestinatarioPrincipale(), errore);
		}
		for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatari()) {
			impostaNonAccettazione(d, errore);
		}
		for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatariCC()) {
			impostaNonAccettazione(d, errore);
		}

		if (task.getEnclosingPratica().getDati().getStato() == null || task.getEnclosingPratica().getDati().getStato().getIdStato() < Stato.MANCATA_ACCETTAZIONE.getIdStato()) {
			task.getEnclosingPratica().getDati().setStato(Stato.MANCATA_ACCETTAZIONE);
		}
	}

	private static void impostaNonAccettazione(Destinatario d, String errore) {
		d.setErrore(errore);
		d.setStatoDestinatario(StatoDestinatario.NON_ACCETTATO);
	}

}
