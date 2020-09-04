package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.StoricoVersioni.InformazioniTaskFirma;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.TaskFactory;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RichiestaApprovazioneFirmaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.ApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoPropostaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class RichiestaApprovazioneFirmaTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RichiestaApprovazioneFirmaTaskApi {

	public RichiestaApprovazioneFirmaTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public Integer inviaInApprovazione(RichiestaApprovazioneFirmaBean bean, List<String> destinatariNotifica, String note, String fullNameUtente) {
		Fascicolo fascicolo = (Fascicolo) this.task.getEnclosingPratica();
		String currentUser = this.task.getCurrentUser();
		Assegnatario assegnatario = new Assegnatario(bean.getGruppoProponente(), "", new Date(), task.getDati().getAssegnatario().getDataFine());
		ApprovazioneFirmaTask task = aggiungiTaskDiFirma(fascicolo, assegnatario, bean.getTipoRichiestaFirma(), bean.getAllegato(), bean.getOggettoDocumento(), bean.getDestinatari(),
				destinatariNotifica, currentUser, bean.getMittenteOriginale(), bean.getDataScadenza());

		List<DestinatarioRichiestaApprovazioneFirmaTask> destinatari = new ArrayList<DestinatarioRichiestaApprovazioneFirmaTask>(bean.getDestinatari());
		InformazioniTaskFirma informazioniTaskFirma = new InformazioniTaskFirma(bean.getOggettoDocumento(), assegnatario.getNome(), bean.getTipoRichiestaFirma(), destinatari,
				StatoRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE, bean.getMittenteOriginale(), bean.getDataScadenza(), StatoRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE, null);

		TaskDiFirmaUtil.storicizzaAllegato(bean.getAllegato(), fascicolo, bean.getAllegato().getCurrentVersion(), informazioniTaskFirma, fullNameUtente);
		TaskDiFirmaUtil.updateNotePratica(fascicolo, note);
		TaskDiFirmaUtil.cleanAllegatiProtocollati(bean.getAllegato(), fascicolo);

		ParametriExtra parametriExtra = new ParametriExtra();
		parametriExtra.getIndirizziEmail().addAll(destinatariNotifica);
		parametriExtra.setIdTask(task.getDati().getId());
		parametriExtra.setNoteTask(note);

		if (destinatariNotifica != null && destinatariNotifica.size() > 0) {
			generaEvento(parametriExtra, EventiIterFascicolo.RICHIEDI_APPROVAZIONE_FIRMA_CON_NOTIFICA, task.getCurrentUser(), bean.getAllegato().getNome(),
					GenericsUtil.convertCollectionToString(new ArrayList<String>(destinatariNotifica)));
		} else {
			generaEvento(parametriExtra, EventiIterFascicolo.RICHIEDI_APPROVAZIONE_FIRMA, task.getCurrentUser(), bean.getAllegato().getNome());
		}

		return task.getDati().getId();
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.RICHIESTA_APPROVAZIONE_FIRMA;
	}

	private ApprovazioneFirmaTask aggiungiTaskDiFirma(Fascicolo fascicolo, Assegnatario assegnatario, TipoPropostaApprovazioneFirmaTask tipoRichiestaFirma, Allegato a, String oggettoDocumento,
			TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatari, List<String> destinatariNotifica, String currentUser, String mittenteOriginale, Date dataScadenza) {
		if (Boolean.TRUE.equals(a.getLock())) {
			throw new PraticaException("L'allegato " + a.getNome() + " è bloccato dal task " + a.getLockedBy());
		}

		boolean isAllegatoProtocollato = XmlPluginUtil.isAllegatoProtocollato(a, fascicolo);
		if (TipoPropostaApprovazioneFirmaTask.FIRMA.equals(tipoRichiestaFirma) && isAllegatoProtocollato) {
			throw new PraticaException("Impossibile inviare una proposta di firma per un allegato protocollato: " + a.getNome());
		}

		DatiApprovazioneFirmaTask.Builder builderTaskFirma = new DatiApprovazioneFirmaTask.Builder();
		builderTaskFirma.setAssegnatario(assegnatario);
		builderTaskFirma.setAttivo(true);
		builderTaskFirma.setDataCreazione(new Date());
		builderTaskFirma.setDestinatari(destinatari);
		builderTaskFirma.setRiferimentoAllegato(
				new it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.RiferimentoAllegatoApprovazioneFirmaTask(a.getNome(), a.getCurrentVersion()));
		builderTaskFirma.setTipo(tipoRichiestaFirma);
		builderTaskFirma.setStato(StatoRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.name());
		builderTaskFirma.setDataScadenza(dataScadenza);
		builderTaskFirma.setMittenteOriginale(mittenteOriginale);

		DatiApprovazioneFirmaTask datiRichiestaFirmaTask = builderTaskFirma.construct();
		TaskFactory tf = new XMLTaskFactory();
		ApprovazioneFirmaTask approvazioneFirmaTask = tf.newTaskInstance(ApprovazioneFirmaTask.class, fascicolo, datiRichiestaFirmaTask);
		a.setOggettoDocumento(oggettoDocumento);
		a.setLock(true);
		a.setLockedBy(approvazioneFirmaTask.getDati().getId());
		((XMLApprovazioneFirmaTask) approvazioneFirmaTask).setCurrentUser(currentUser);
		return approvazioneFirmaTask;
	}
}
