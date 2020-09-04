package it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.StoricoVersioni.InformazioniTaskFirma;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioGruppoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioUtenteRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoDestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoPropostaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoRispostaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni.AbstractApprovazioneFirmaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni.GestioneApprovazioneFirmaApiTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;

/**
 * FIXME FM - vedere se si riesce a estendere AbstractTaskApiImpl o meglio tirare fuori una superclasse
 *
 * @author fmattiazzo
 *
 */
public class GestioneApprovazioneFirmaApiTaskImpl extends AbstractApprovazioneFirmaTaskApi implements GestioneApprovazioneFirmaApiTask {

	public enum OperazioniGestioneApprovazione {
		FIRMA("FIRMA"), APPROVA("APPROVA"), DINIEGA("DINIEGA"), RISPOSTA_POSITIVA("RISPOSTA POSITIVA"), RISPOSTA_NEGATIVA("RISPOSTA NEGATIVA"),
		RISPOSTA_POSITIVA_CON_PRESCRIZIONI("RISPOSTA POSITIVA CON PRESCRIZIONI"), RISPOSTA_SOSPESA("RISPOSTA SOSPESA"), RISPOSTA_RIFIUTATA("RISPOSTA RIFIUTATA");

		private String label;

		OperazioniGestioneApprovazione(String label) {
			this.label = label;
		}

		public String getLabel() {
			return this.label;
		}
	}

	public GestioneApprovazioneFirmaApiTaskImpl(XMLApprovazioneFirmaTask task) {
		super(task);
	}

	@Override
	public void firma(Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione) {
		updateTask(a, userID, ruolo, StatoDestinatarioRichiestaApprovazioneFirmaTask.APPROVATO, note, motivazione);
		ParametriExtra parametriExtra = new ParametriExtra();
		parametriExtra.getIndirizziEmail().addAll(destinatariNotifica);
		parametriExtra.setIdTask(task.getDati().getId());
		parametriExtra.setOperazioneTask(OperazioniGestioneApprovazione.FIRMA.getLabel());
		parametriExtra.setNoteTask(note);

		if (destinatariNotifica != null && destinatariNotifica.size() > 0) {
			generaEvento(parametriExtra, EventiIterApprovazioneFirma.GESTIONE_APPROVAZIONE_FIRMA_CON_NOTIFICA, task.getCurrentUser(), a.getNome(), OperazioniGestioneApprovazione.FIRMA.getLabel(),
					GenericsUtil.convertCollectionToString(new ArrayList<String>(destinatariNotifica)));
		} else generaEvento(parametriExtra, EventiIterApprovazioneFirma.GESTIONE_APPROVAZIONE_FIRMA, task.getCurrentUser(), a.getNome(), OperazioniGestioneApprovazione.FIRMA.getLabel());
	}

	@Override
	public void approva(Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione) {
		updateTask(a, userID, ruolo, StatoDestinatarioRichiestaApprovazioneFirmaTask.APPROVATO, note, motivazione);
		ParametriExtra parametriExtra = new ParametriExtra();
		parametriExtra.getIndirizziEmail().addAll(destinatariNotifica);
		parametriExtra.setIdTask(task.getDati().getId());
		parametriExtra.setOperazioneTask(OperazioniGestioneApprovazione.APPROVA.getLabel());
		parametriExtra.setNoteTask(note);

		if (destinatariNotifica != null && destinatariNotifica.size() > 0) {
			generaEvento(parametriExtra, EventiIterApprovazioneFirma.GESTIONE_APPROVAZIONE_FIRMA_CON_NOTIFICA, task.getCurrentUser(), a.getNome(), OperazioniGestioneApprovazione.APPROVA.getLabel(),
					GenericsUtil.convertCollectionToString(new ArrayList<String>(destinatariNotifica)));
		} else generaEvento(parametriExtra, EventiIterApprovazioneFirma.GESTIONE_APPROVAZIONE_FIRMA, task.getCurrentUser(), a.getNome(), OperazioniGestioneApprovazione.APPROVA.getLabel());
	}

	@Override
	public void diniega(Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione) {
		updateTask(a, userID, ruolo, StatoDestinatarioRichiestaApprovazioneFirmaTask.DINIEGATO, note, motivazione);
		ParametriExtra parametriExtra = new ParametriExtra();
		parametriExtra.getIndirizziEmail().addAll(destinatariNotifica);
		parametriExtra.setIdTask(task.getDati().getId());
		parametriExtra.setOperazioneTask(OperazioniGestioneApprovazione.DINIEGA.getLabel());
		parametriExtra.setNoteTask(note);

		if (destinatariNotifica != null && destinatariNotifica.size() > 0) {
			generaEvento(parametriExtra, EventiIterApprovazioneFirma.GESTIONE_APPROVAZIONE_FIRMA_CON_NOTIFICA, task.getCurrentUser(), a.getNome(), OperazioniGestioneApprovazione.DINIEGA.getLabel(),
					GenericsUtil.convertCollectionToString(new ArrayList<String>(destinatariNotifica)));
		} else generaEvento(parametriExtra, EventiIterApprovazioneFirma.GESTIONE_APPROVAZIONE_FIRMA, task.getCurrentUser(), a.getNome(), OperazioniGestioneApprovazione.DINIEGA.getLabel());
	}

	@Override
	public void rispondi(TipoRispostaApprovazioneFirmaTask tipoRisposta, Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione) {
		OperazioniGestioneApprovazione operazioneTask = null;
		StatoDestinatarioRichiestaApprovazioneFirmaTask stato = null;

		switch (tipoRisposta) {
		case RISPOSTA_NEGATIVA:
			operazioneTask = OperazioniGestioneApprovazione.RISPOSTA_NEGATIVA;
			stato = StatoDestinatarioRichiestaApprovazioneFirmaTask.RISPOSTA_NEGATIVA;
			break;

		case RISPOSTA_POSITIVA_CON_PRESCRIZIONI:
			operazioneTask = OperazioniGestioneApprovazione.RISPOSTA_POSITIVA_CON_PRESCRIZIONI;
			stato = StatoDestinatarioRichiestaApprovazioneFirmaTask.RISPOSTA_POSITIVA_CON_PRESCRIZIONI;
			break;

		case RISPOSTA_POSITIVA:
			operazioneTask = OperazioniGestioneApprovazione.RISPOSTA_POSITIVA;
			stato = StatoDestinatarioRichiestaApprovazioneFirmaTask.RISPOSTA_POSITIVA;
			break;

		case RISPOSTA_SOSPESA:
			operazioneTask = OperazioniGestioneApprovazione.RISPOSTA_SOSPESA;
			stato = StatoDestinatarioRichiestaApprovazioneFirmaTask.RISPOSTA_SOSPESA;
			break;

		case RISPOSTA_RIFIUTATA:
			operazioneTask = OperazioniGestioneApprovazione.RISPOSTA_RIFIUTATA;
			stato = StatoDestinatarioRichiestaApprovazioneFirmaTask.RISPOSTA_RIFIUTATA;
			break;

		default:
			throw new PraticaException("Tipo operazione di risposta non valida");
		}

		updateTask(a, userID, ruolo, stato, note, motivazione);

		ParametriExtra parametriExtra = new ParametriExtra();
		parametriExtra.getIndirizziEmail().addAll(destinatariNotifica);
		parametriExtra.setIdTask(task.getDati().getId());
		parametriExtra.setOperazioneTask(operazioneTask.getLabel());
		parametriExtra.setNoteTask(note);

		if (destinatariNotifica != null && destinatariNotifica.size() > 0) {
			generaEvento(parametriExtra, EventiIterApprovazioneFirma.GESTIONE_APPROVAZIONE_FIRMA_CON_NOTIFICA, task.getCurrentUser(), a.getNome(), operazioneTask.getLabel(),
					GenericsUtil.convertCollectionToString(new ArrayList<String>(destinatariNotifica)));
		} else generaEvento(parametriExtra, EventiIterApprovazioneFirma.GESTIONE_APPROVAZIONE_FIRMA, task.getCurrentUser(), a.getNome(), operazioneTask.getLabel());
	}

	private void updateTask(Allegato a, String userID, String ruolo, StatoDestinatarioRichiestaApprovazioneFirmaTask statoDestinatario, String note, String motivazione) {
		/*
		 * CHECK CHIUSURA TASK
		 */
		boolean checkStatoTask = StatoRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.name().equalsIgnoreCase(task.getDati().getStato());
		if (!checkStatoTask) throw new PraticaException("Impossibile modificare un task di firma non in approvazione");

		/*
		 * CHECK OPERAZIONE ABILITATA
		 */
		checkAbilitazioneOperazione(task.getDati().getDestinatari(), userID, ruolo);

		/*
		 * CHECK DEL DESTINATARIO
		 */
		List<DestinatarioRichiestaApprovazioneFirmaTask> destinatari = new ArrayList<DestinatarioRichiestaApprovazioneFirmaTask>();
		DestinatarioRichiestaApprovazioneFirmaTask destinatario = null;
		for (DestinatarioRichiestaApprovazioneFirmaTask d : task.getDati().getDestinatari())

			if (checkDestinatario(d, userID, ruolo) && destinatario == null) {
				destinatario = d;
			} else {
				destinatari.add(d);
			}

		if (destinatario == null) throw new PraticaException("L'utente " + userID + " non è fra i destinatari del task di firma");

		destinatario.setStato(statoDestinatario);
		destinatari.add(destinatario);

		/*
		 * GESTIONE CHIUSURA TASK
		 */
		List<TipoPropostaApprovazioneFirmaTask> tipiPropostaByStatoDestinatario = statoDestinatario.getTipiProposta();
		boolean tipoPropostaOk = false;
		for (TipoPropostaApprovazioneFirmaTask tipoProposta : tipiPropostaByStatoDestinatario) {

			if (!tipoPropostaOk && task.getDati().getTipo().equals(tipoProposta)) {
				tipoPropostaOk = true;

				switch (tipoProposta) {
				case FIRMA:
				case VISTO:
					gestioneChiusuraTask(statoDestinatario, Arrays.asList(StatoDestinatarioRichiestaApprovazioneFirmaTask.DINIEGATO), StatoRichiestaApprovazioneFirmaTask.APPROVATO, a);
					break;

				case PARERE:
					gestioneChiusuraTask(statoDestinatario, null, StatoRichiestaApprovazioneFirmaTask.PARERE_RICEVUTO, a);
					break;

				default:
					throw new PraticaException("Tipo proposta non valido");
				}
			}
		}

		if (!tipoPropostaOk) throw new PraticaException("Tipo proposta non valido");

		/*
		 * STORICO VERSIONI
		 */
		if (statoDestinatario != StatoDestinatarioRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE) {

			StatoRichiestaApprovazioneFirmaTask operazioneEffettuata = null;
			switch (statoDestinatario) {
			case APPROVATO:
				operazioneEffettuata = StatoRichiestaApprovazioneFirmaTask.APPROVATO;
				break;

			case DINIEGATO:
				operazioneEffettuata = StatoRichiestaApprovazioneFirmaTask.DINIEGATO;
				break;

			case RISPOSTA_NEGATIVA:
			case RISPOSTA_POSITIVA_CON_PRESCRIZIONI:
			case RISPOSTA_POSITIVA:
			case RISPOSTA_SOSPESA:
			case RISPOSTA_RIFIUTATA:
				operazioneEffettuata = StatoRichiestaApprovazioneFirmaTask.PARERE_RICEVUTO;
				break;

			default:
				throw new PraticaException("Errore in fase di creazione dello storico dell'allegato");
			}

			InformazioniTaskFirma informazioniTaskFirma = new InformazioniTaskFirma(a.getOggettoDocumento(), this.task.getDati().getAssegnatario().getNome(), this.task.getDati().getTipo(),
					destinatari, operazioneEffettuata, this.task.getDati().getMittenteOriginale(), this.task.getDati().getDataScadenza(), getStatoTask(), motivazione);
			TaskDiFirmaUtil.versionaRiferimentoAllegato(a, this.task, informazioniTaskFirma, userID);
		}

		TaskDiFirmaUtil.updateNotePratica(task.getEnclosingPratica(), note);
		TaskDiFirmaUtil.cleanAllegatiProtocollati(a, task.getEnclosingPratica());
	}

	private StatoRichiestaApprovazioneFirmaTask getStatoTask() {
		if (this.task == null || this.task.getDati() == null || this.task.getDati().getStato() == null) {
			throw new PraticaException("Errore in fase di creazione dello storico dell'allegato, manca lo stato complessivo del task");
		}
		return StatoRichiestaApprovazioneFirmaTask.fromValue(this.task.getDati().getStato());
	}

	private void gestioneChiusuraTask(StatoDestinatarioRichiestaApprovazioneFirmaTask statoDestinatario, List<StatoDestinatarioRichiestaApprovazioneFirmaTask> statiDestinatariChiusuraForzata,
			StatoRichiestaApprovazioneFirmaTask statoTaskChiusuraCompleta, Allegato a) {
		boolean chiudi = true;

		if (statiDestinatariChiusuraForzata != null && statiDestinatariChiusuraForzata.contains(statoDestinatario)) {
			task.getDati().setAttivo(false);
			task.getDati().getAssegnatario().setDataFine(new Date());
			task.getDati().setStato(statoDestinatario.name());
			TaskDiFirmaUtil.unlockAllegato(a);

		} else {
			for (DestinatarioRichiestaApprovazioneFirmaTask d : task.getDati().getDestinatari())
				if (StatoDestinatarioRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.equals(d.getStato())) {
					chiudi = false;
					break;
				}

			if (chiudi) {
				task.getDati().setAttivo(false);
				task.getDati().getAssegnatario().setDataFine(new Date());
				task.getDati().setStato(statoTaskChiusuraCompleta.name());
				TaskDiFirmaUtil.unlockAllegato(a);
			}
		}
	}

	private void checkAbilitazioneOperazione(TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatari, String userID, String ruolo) {

		boolean taskRuoli = false;

		for (DestinatarioRichiestaApprovazioneFirmaTask destinatario : destinatari) {

			if (destinatario instanceof DestinatarioGruppoRichiestaApprovazioneFirmaTask) {
				taskRuoli = true;

				if (((DestinatarioGruppoRichiestaApprovazioneFirmaTask) destinatario).getNomeGruppo().equals(ruolo)) {
					if (!destinatario.getStato().equals(StatoDestinatarioRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE)) {
						throw new PraticaException("Il destinatario ha già effettuato l'operazione per il task");
					}
				}

			} else if (!taskRuoli && destinatario instanceof DestinatarioUtenteRichiestaApprovazioneFirmaTask) {
				if (((DestinatarioUtenteRichiestaApprovazioneFirmaTask) destinatario).getNomeUtente().equals(userID)) {
					if (!destinatario.getStato().equals(StatoDestinatarioRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE)) {
						throw new PraticaException("Il destinatario ha già effettuato l'operazione per il task");
					}
				}

			} else {
				throw new PraticaException("Destinatari non validi");
			}

		}

	}

	private boolean checkDestinatario(DestinatarioRichiestaApprovazioneFirmaTask destinatario, String userID, String ruolo) {
		if (destinatario instanceof DestinatarioUtenteRichiestaApprovazioneFirmaTask) {
			DestinatarioUtenteRichiestaApprovazioneFirmaTask d = (DestinatarioUtenteRichiestaApprovazioneFirmaTask) destinatario;
			if (d.getNomeUtente().equalsIgnoreCase(userID) && StatoDestinatarioRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.equals(d.getStato())) return true;

		} else if (destinatario instanceof DestinatarioGruppoRichiestaApprovazioneFirmaTask) {
			DestinatarioGruppoRichiestaApprovazioneFirmaTask d = (DestinatarioGruppoRichiestaApprovazioneFirmaTask) destinatario;
			if (d.getNomeGruppo().equalsIgnoreCase(ruolo) && StatoDestinatarioRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.equals(d.getStato())) return true;
		}

		return false;
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.GESTIONE_APPROVAZIONE_FIRMA;
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return task.isAttivo() && task.getDati().getStato().equals(StatoRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.name()) && !Boolean.FALSE.equals(task.getDati().getValido());
	}
}
