package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl;

import java.util.List;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.StatoDestinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.DestinatarioRicevuta;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.XMLGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.PecPreavvisoMancataConsegnaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;

/**
 * @author Giacomo F.M.
 * @since 2019-09-17
 */
public class PecPreavvisoMancataConsegnaTaskApiImpl<T extends DatiGestionePECTask> extends AbstractTaskPECApiImpl<T> implements PecPreavvisoMancataConsegnaTaskApi {

	public PecPreavvisoMancataConsegnaTaskApiImpl(XMLGestionePECTask task) {
		super(task);
	}

	@Override
	protected TipoApiTaskPEC getTipoApiTask() {
		return TipoApiTaskPEC.PEC_PREAVVISO_MANCATA_CONSEGNA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	public void pecPreavvisoMancataConsegna(Ricevuta ricevutaConsegna) {

		String destinatarioRicevuta = ricevutaConsegna.getConsegna();
		String errore = componiErrore(ricevutaConsegna);
		aggiornaTipoDestinatari(ricevutaConsegna.getDestinatari());

		if (task.getEnclosingPratica().getDati().getDestinatarioPrincipale() != null
				&& destinatarioRicevuta.equals(task.getEnclosingPratica().getDati().getDestinatarioPrincipale().getDestinatario())) {
			impostaPreavvisoMancataConsegna(task.getEnclosingPratica().getDati().getDestinatarioPrincipale(), errore);
		}
		for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatari()) {
			if (destinatarioRicevuta.equals(d.getDestinatario())) {
				impostaPreavvisoMancataConsegna(d, errore);
			}
		}
		for (Destinatario d : task.getEnclosingPratica().getDati().getDestinatariCC()) {
			if (destinatarioRicevuta.equals(d.getDestinatario())) {
				impostaPreavvisoMancataConsegna(d, errore);
			}
		}

		if (!task.getEnclosingPratica().getDati().getStato().equals(Stato.PARZIALMENTECONSEGNATA)) {
			task.getEnclosingPratica().getDati().setStato(Stato.PREAVVISO_MANCATA_CONSEGNA);
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

	private static void impostaPreavvisoMancataConsegna(Destinatario d, String errore) {
		d.setErrore(errore);
		d.setTipo(TipoEmail.certificato);
		d.setStatoDestinatario(StatoDestinatario.NON_CONSEGNATO);
	}

	private static String componiErrore(Ricevuta ricevuta) {
		StringBuilder sb = new StringBuilder();
		if (ricevuta.getErrore() != null) {
			sb.append(ricevuta.getErrore().getLabel());
			if (ricevuta.getErroreEsteso() != null && !ricevuta.getErroreEsteso().isEmpty()) {
				sb.append(":").append(ricevuta.getErroreEsteso());
			}
		}
		return sb.toString();
	}
}
