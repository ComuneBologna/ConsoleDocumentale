package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import java.util.ArrayList;
import java.util.List;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.StoricoVersioni.InformazioniTaskFirma;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RitiroApprovazioneFirmaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.ApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.StatoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;

public class RitiroApprovazioneFirmaTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements RitiroApprovazioneFirmaTaskApi {

	public RitiroApprovazioneFirmaTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	public void ritira(Allegato a, ApprovazioneFirmaTask approvazioneFirmaTask, String note, List<String> destinatariNotifica, List<String> ruoli, String fullNameUtente) {
		Fascicolo fascicolo = (Fascicolo) this.task.getEnclosingPratica();
		boolean checkStatoTask = StatoRichiestaApprovazioneFirmaTask.IN_APPROVAZIONE.name().equalsIgnoreCase(approvazioneFirmaTask.getDati().getStato());
		if (!checkStatoTask) throw new PraticaException("Impossibile ritirare un task di firma non in approvazione");

		if (!ruoli.contains(approvazioneFirmaTask.getDati().getAssegnatario().getNome())) throw new PraticaException("L'utente non è abilitato per l'operazione di ritiro");

		approvazioneFirmaTask.getDati().setAttivo(false);
		approvazioneFirmaTask.getDati().setStato(StatoRichiestaApprovazioneFirmaTask.RITIRATO.name());

		InformazioniTaskFirma informazioniTaskFirma = new InformazioniTaskFirma(a.getOggettoDocumento(), approvazioneFirmaTask.getDati().getAssegnatario().getNome(),
				approvazioneFirmaTask.getDati().getTipo(), null, StatoRichiestaApprovazioneFirmaTask.RITIRATO, approvazioneFirmaTask.getDati().getMittenteOriginale(),
				approvazioneFirmaTask.getDati().getDataScadenza(), StatoRichiestaApprovazioneFirmaTask.RITIRATO, null);

		TaskDiFirmaUtil.versionaRiferimentoAllegato(a, approvazioneFirmaTask, informazioniTaskFirma, fullNameUtente);
		TaskDiFirmaUtil.updateNotePratica(fascicolo, note);
		TaskDiFirmaUtil.unlockAllegato(a);
		TaskDiFirmaUtil.cleanAllegatiProtocollati(a, fascicolo);

		ParametriExtra parametriExtra = new ParametriExtra();
		parametriExtra.getIndirizziEmail().addAll(destinatariNotifica);
		parametriExtra.setIdTask(approvazioneFirmaTask.getDati().getId());
		parametriExtra.setNoteTask(note);

		if (destinatariNotifica != null && destinatariNotifica.size() > 0) {
			generaEvento(parametriExtra, EventiIterFascicolo.RITIRA_APPROVAZIONE_FIRMA_CON_NOTIFICA, task.getCurrentUser(), a.getNome(),
					GenericsUtil.convertCollectionToString(new ArrayList<String>(destinatariNotifica)));
		} else {
			generaEvento(parametriExtra, EventiIterFascicolo.RITIRA_APPROVAZIONE_FIRMA, task.getCurrentUser(), a.getNome());
		}
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return getDatiFascicolo().getStato().equals(Stato.IN_GESTIONE) && TaskDiFirmaUtil.hasApprovazioneFirmaTask(this.task.getEnclosingPratica());
	}

	@Override
	protected TipoApiTask getTipoApiTask() {
		return TipoApiTask.RITIRO_APPROVAZIONE_FIRMA;
	}
}
