package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import it.eng.cobo.consolepec.commons.atti.DocumentoAssegnatario;
import it.eng.cobo.consolepec.commons.atti.DocumentoDestinatarioRichiestaApprovazione;
import it.eng.cobo.consolepec.commons.atti.DocumentoDestinatarioRichiestaApprovazione.DocumentoDestinatarioGruppoRichiestaApprovazione;
import it.eng.cobo.consolepec.commons.atti.DocumentoDestinatarioRichiestaApprovazione.DocumentoDestinatarioUtenteRichiestaApprovazione;
import it.eng.cobo.consolepec.commons.atti.DocumentoRiferimentoAllegato;
import it.eng.cobo.consolepec.commons.atti.DocumentoTaskFirma;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiTask;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.EstrazioneTaskFirmaTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioGruppoRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioUtenteRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.XMLApprovazioneFirmaTask;

import java.util.Collections;
import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * @author GiacomoFM
 * @since 08/mar/2018
 */
public class EstrazioneTaskFirmaTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApiImpl<T> implements EstrazioneTaskFirmaTaskApi {

	public EstrazioneTaskFirmaTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
	}

	@Override
	protected boolean controllaAbilitazioneInterna() {
		return true;
	}

	@Override
	protected ITipoApiTask getTipoApiTask() {
		return TipoApiTask.RICERCA_TASK_FIRMA;
	}

	@Override
	public DocumentoTaskFirma estraiTaskFirma(Fascicolo fascicolo, Integer idTaskFirma) {
		DocumentoTaskFirma doc = null;
		for (Task<?> t : fascicolo.getTasks()) {
			if (DatiTask.TipoTask.RichiestaFirmaTask.equals(t.getTipo()) && t.getDati().getId() == idTaskFirma) {
				XMLApprovazioneFirmaTask tf = (XMLApprovazioneFirmaTask) t;
				doc = cast(fascicolo.getAlfrescoPath(), tf.getDati(), fascicolo.getDati().getAllegati());
			}
		}
		return doc;
	}

	private static DocumentoTaskFirma cast(String alfrescoPath, DatiApprovazioneFirmaTask dati, TreeSet<Allegato> allegatiPratica) {
		DocumentoRiferimentoAllegato allegato = null;
		if (dati.getRiferimentoAllegato() != null) {
			
			String oggetto = null;
			String hash = null;
			for (Allegato ap : allegatiPratica) {
				if (ap.getNome().equals(dati.getRiferimentoAllegato().getNome()) && ap.getCurrentVersion().equals(dati.getRiferimentoAllegato().getCurrentVersion())) {
					oggetto = ap.getOggettoDocumento();
					hash = ap.getHash();
					break;
				}
			}
			
			allegato = new DocumentoRiferimentoAllegato(dati.getRiferimentoAllegato().getNome(), dati.getRiferimentoAllegato().getCurrentVersion(), oggetto, hash);
		}

		DocumentoAssegnatario assegnatario = null;
		if (dati.getAssegnatario() != null) {
			assegnatario = new DocumentoAssegnatario(dati.getAssegnatario().getNome(),  dati.getAssegnatario().getEtichetta(),
			dati.getAssegnatario().getDataInizio(), dati.getAssegnatario().getDataFine());
		}

		List<DocumentoAssegnatario> assegnatariPassati = Collections.emptyList();
		if (dati.getAssegnatariPassati() != null && dati.getAssegnatariPassati().size() > 0) {
			assegnatariPassati = Lists.transform(dati.getAssegnatariPassati(), new Function<DatiTask.Assegnatario, DocumentoAssegnatario>() {
				@Override
				public DocumentoAssegnatario apply(Assegnatario input) {
					return new DocumentoAssegnatario(input.getNome(), input.getEtichetta(), input.getDataInizio(), input.getDataFine());
				}
			});
		}

		TreeSet<DocumentoDestinatarioRichiestaApprovazione> destinatari = null;
		if (dati.getDestinatari() != null) {
			destinatari = castDestinatari(dati.getDestinatari());
		}

		return DocumentoTaskFirma.builder().idTaskFirma(dati.getId()).praticaPath(alfrescoPath).stato(dati.getStato()).tipo(
				dati.getTipo() != null ? dati.getTipo().toString() : null).mittenteOriginale(dati.getMittenteOriginale()).attivo(dati.getAttivo()).valido(
				dati.getValido()).dataCreazione(dati.getDataCreazione()).dataScadenza(dati.getDataScadenza()).allegato(allegato).assegnatario(
				assegnatario).assegnatariPassati(assegnatariPassati).destinatari(destinatari).build();
	}

	private static TreeSet<DocumentoDestinatarioRichiestaApprovazione> castDestinatari(TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatari) {
		TreeSet<DocumentoDestinatarioRichiestaApprovazione> t = new TreeSet<>();
		for (DestinatarioRichiestaApprovazioneFirmaTask d : destinatari) {
			DocumentoDestinatarioRichiestaApprovazione destinatario = null;
			if (d instanceof DestinatarioUtenteRichiestaApprovazioneFirmaTask) {
				destinatario = new DocumentoDestinatarioUtenteRichiestaApprovazione(
						((DestinatarioUtenteRichiestaApprovazioneFirmaTask) d).getNomeUtente(),
						((DestinatarioUtenteRichiestaApprovazioneFirmaTask) d).getNome(),
						((DestinatarioUtenteRichiestaApprovazioneFirmaTask) d).getCognome(),
						((DestinatarioUtenteRichiestaApprovazioneFirmaTask) d).getMatricola(),
						((DestinatarioUtenteRichiestaApprovazioneFirmaTask) d).getSettore(), (d.getStato() != null ? d.getStato().toString() : null));
			}
			if (d instanceof DestinatarioGruppoRichiestaApprovazioneFirmaTask) {
				destinatario = new DocumentoDestinatarioGruppoRichiestaApprovazione((d.getStato() != null ? d.getStato().toString() : null),
						((DestinatarioGruppoRichiestaApprovazioneFirmaTask) d).getNomeGruppo());
			}
			t.add(destinatario);
		}
		return t;
	}
}
