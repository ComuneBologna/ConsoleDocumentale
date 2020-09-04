package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import java.util.ArrayList;
import java.util.List;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.StatoDestinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.DestinatarioRicevuta;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PecConsegnataTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

/**
 * @author Giacomo F.M.
 * @since 2019-09-17
 */
public class PecConsegnataTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements PecConsegnataTaskApi {

	public PecConsegnataTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.PEC_CONSEGNATA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	public void pecConsegnata(Ricevuta ricevutaConsegna) {

		String destinatarioRicevuta = ricevutaConsegna.getConsegna();
		aggiornaTipoDestinatari(ricevutaConsegna.getDestinatari());

		if (task.getEnclosingPratica().getDati().getDestinatarioPrincipale() != null
				&& destinatarioRicevuta.equals(task.getEnclosingPratica().getDati().getDestinatarioPrincipale().getDestinatario())) {
			impostaConsegna(task.getEnclosingPratica().getDati().getDestinatarioPrincipale());
		}
		for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatari()) {
			if (destinatarioRicevuta.equals(d.getDestinatario())) {
				impostaConsegna(d);
			}
		}
		for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatariCC()) {
			if (destinatarioRicevuta.equals(d.getDestinatario())) {
				impostaConsegna(d);
			}
		}

		task.getEnclosingPratica().getDati().setRicevutaConsegna(true);

		if (isConsegnata()) {

			if (isAccettata()) {
				task.getEnclosingPratica().getDati().setStato(Stato.CONSEGNATA);
				task.termina();

			} else {
				task.getEnclosingPratica().getDati().setStato(Stato.CONSEGNA_SENZA_ACCETTAZIONE);
			}

		} else {
			task.getEnclosingPratica().getDati().setStato(Stato.PARZIALMENTECONSEGNATA);
		}
	}

	private void aggiornaTipoDestinatari(List<DestinatarioRicevuta> destinatari) {

		for (DestinatarioRicevuta destinatarioRicevuta : destinatari) {

			if (task.getEnclosingPratica().getDati().getDestinatarioPrincipale() != null
					&& destinatarioRicevuta.getEmail().equals(task.getEnclosingPratica().getDati().getDestinatarioPrincipale().getDestinatario())) {

				task.getEnclosingPratica().getDati().getDestinatarioPrincipale().setTipo(TipoEmail.valueOf(destinatarioRicevuta.getTipo()));
			}

			for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatari()) {
				if (destinatarioRicevuta.getEmail().equals(d.getDestinatario())) {
					d.setTipo(TipoEmail.valueOf(destinatarioRicevuta.getTipo()));
				}
			}

			for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatariCC()) {
				if (destinatarioRicevuta.getEmail().equals(d.getDestinatario())) {
					d.setTipo(TipoEmail.valueOf(destinatarioRicevuta.getTipo()));
				}
			}
		}
	}

	private static void impostaConsegna(final Destinatario d) {
		d.setConsegna(true);
		d.setTipo(TipoEmail.certificato);
		d.setStatoDestinatario(StatoDestinatario.CONSEGNATO);
	}

	private boolean isAccettata() {
		return task.getEnclosingPratica().getDati().isRicevutaAccettazione();
	}

	private boolean isConsegnata() {
		List<Destinatario> fullList = new ArrayList<>();
		if (task.getEnclosingPratica().getDati().getDestinatarioPrincipale() != null) {
			fullList.add(task.getEnclosingPratica().getDati().getDestinatarioPrincipale());
		}
		fullList.addAll(task.getEnclosingPratica().getDati().getDestinatari());
		fullList.addAll(task.getEnclosingPratica().getDati().getDestinatariCC());

		for (Destinatario d : fullList) {

			if (d.getTipo().equals(TipoEmail.certificato) && !d.isConsegna())
				return false;
		}

		return true;
	}
}
