package it.eng.consolepec.xmlplugin.pratica.fascicolo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta.TipoAccesso;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.datiaggiuntivi.DatiAggiuntiviUtil;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.EventoIterPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.XMLPratica;
import it.eng.consolepec.xmlplugin.jaxb.Documento;
import it.eng.consolepec.xmlplugin.jaxb.EventoIter;
import it.eng.consolepec.xmlplugin.jaxb.Fascicolo.Allegati;
import it.eng.consolepec.xmlplugin.jaxb.Fascicolo.DatiAggiuntivi;
import it.eng.consolepec.xmlplugin.jaxb.Fascicolo.DatiAggiuntivi.Valori;
import it.eng.consolepec.xmlplugin.jaxb.Fascicolo.EventiIter;
import it.eng.consolepec.xmlplugin.jaxb.Gruppivisibilita;
import it.eng.consolepec.xmlplugin.jaxb.OperazioneOperativitaRidotta;
import it.eng.consolepec.xmlplugin.jaxb.PG;
import it.eng.consolepec.xmlplugin.jaxb.Pratica;
import it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata;
import it.eng.consolepec.xmlplugin.jaxb.Protocollazione.AllegatiProtocollati;
import it.eng.consolepec.xmlplugin.jaxb.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Procedimento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.StepIter;
import it.eng.consolepec.xmlplugin.pratica.fatturazione.DatiFascicoloFatturazione;
import it.eng.consolepec.xmlplugin.tasks.eventiiter.SerializzatoreEventiIter;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask.Condivisione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.AbstractTaskApiImpl.EventiIterFascicolo;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaFascicoloTask;
import it.eng.consolepec.xmlplugin.util.TranslatorBeanCondivisione;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class XMLFascicolo extends XMLPratica<DatiFascicolo> implements Fascicolo {

	private DatiFascicolo dati;
	protected final Logger logger = LoggerFactory.getLogger(XMLFascicolo.class);

	@Override
	public DatiFascicolo getDati() {
		return dati;
	}

	@Override
	public String getSubFolderPath() {
		return "ALLEGATI";
	}

	/* metodi di interfaccia/public */

	@Override
	public Map<String, Object> getMetadataString() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<MetadatiPratica, Object> mapIn = getMetadata();
		for (MetadatiPratica key : mapIn.keySet()) {
			map.put(key.getNome(), mapIn.get(key));
		}
		return map;
	}

	@Override
	public void accessoUtenteEsterno(String utente) throws PraticaException {
		TaskFascicolo<?> taskGestione = null;
		for (Task<?> task : getTasks()) {
			if (task instanceof TaskFascicolo<?>) {
				taskGestione = (TaskFascicolo<?>) task;
				break;
			}
		}
		taskGestione.setCurrentUser(utente);
		new SerializzatoreEventiIter(taskGestione, EventiIterFascicolo.ACCESSO_UTENTE_ESTERNO, utente).serializeIter();

	}

	@Override
	public boolean isAttiva() {
		return !getDati().getStato().equals(DatiFascicolo.Stato.ARCHIVIATO);
	}

	@Override
	public Map<MetadatiPratica, Object> getMetadata() {
		Map<MetadatiPratica, Object> map = new HashMap<MetadatiPratica, Object>();

		TaskFascicolo<?> taskGestione = null;
		RiattivaFascicoloTask taskRiattiva = null;
		for (Task<?> task : getTasks()) {
			if (task instanceof TaskFascicolo<?> && task.isAttivo()) {
				taskGestione = (TaskFascicolo<?>) task;
				break;

			} else if (task instanceof RiattivaFascicoloTask && task.isAttivo()) {
				taskRiattiva = (RiattivaFascicoloTask) task;
				break;
			}
		}

		if (getDati().getAssegnatarioCorrente() != null) {
			map.put(MetadatiPratica.pAssegnatoA, getDati().getAssegnatarioCorrente());

		} else {
			if (taskGestione != null) {
				map.put(MetadatiPratica.pAssegnatoA, taskGestione.getDati().getAssegnatario().getNome());
				if (((DatiGestioneFascicoloTask) taskGestione.getDati()).getCondivisioni().size() > 0) {
					TreeSet<Condivisione> condivisioni = ((DatiGestioneFascicoloTask) taskGestione.getDati()).getCondivisioni();
					map.put(MetadatiPratica.pCondivisoCon, XmlPluginUtil.getListaCondivisioni(condivisioni));
				}
			}
			if (taskRiattiva != null) {
				map.put(MetadatiPratica.pAssegnatoA, taskRiattiva.getDati().getAssegnatario().getNome());
			}
		}

		if (taskGestione != null && map.get(MetadatiPratica.pCondivisoCon) == null) {
			if (((DatiGestioneFascicoloTask) taskGestione.getDati()).getCondivisioni().size() > 0) {
				TreeSet<Condivisione> condivisioni = ((DatiGestioneFascicoloTask) taskGestione.getDati()).getCondivisioni();
				map.put(MetadatiPratica.pCondivisoCon, XmlPluginUtil.getListaCondivisioni(condivisioni));
			}
		}

		map.put(MetadatiPratica.pDataCreazione, dati.getDataCreazione());
		map.put(MetadatiPratica.pIdDocumentale, getDati().getIdDocumentale());
		map.put(MetadatiPratica.pStato, getDati().getStato().toString());
		map.put(MetadatiPratica.pTipoPratica, getDati().getTipo().getNomeTipologia());
		map.put(MetadatiPratica.pTitolo, dati.getTitolo());
		map.put(MetadatiPratica.pLetto, dati.isLetto());
		map.put(MetadatiPratica.pData, dati.getDataCreazione());
		map.put(MetadatiPratica.pInoltratoDa, getInoltratoDa());
		map.put(MetadatiPratica.pProvenienza, getProvenienza());

		if (dati.getStepIter() != null && dati.getStepIter().getNome() != null) {
			map.put(MetadatiPratica.pStepIter, dati.getStepIter().getNome());
		}

		if (dati instanceof DatiFascicoloFatturazione) {
			map.put(MetadatiPratica.pNumeroFattura, ((DatiFascicoloFatturazione) dati).getNumeroFattura());
			map.put(MetadatiPratica.pRagioneSociale, ((DatiFascicoloFatturazione) dati).getRagioneSociale());
			map.put(MetadatiPratica.pPartitaIva, ((DatiFascicoloFatturazione) dati).getPIva());
			map.put(MetadatiPratica.pCodicePartitaIva, ((DatiFascicoloFatturazione) dati).getCodicePIva());
		}

		if (dati.getUtenteCreazione() != null) {
			map.put(MetadatiPratica.pUtenteCreazione, dati.getUtenteCreazione());
		}

		it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila capofila = getPrimoCapofila();
		if (capofila != null) {
			if (capofila.isFromBa01() || (capofila.getAllegatiProtocollati().isEmpty() && capofila.getPraticheCollegateProtocollate().isEmpty())) {
				Collections.sort(capofila.getProtocollazioniCollegate());
				Protocollazione protocollazione = capofila.getProtocollazioniCollegate().get(0);
				map.put(MetadatiPratica.pAnnoPG, protocollazione.getAnnoPG());
				map.put(MetadatiPratica.pNumeroPG, protocollazione.getNumeroPG());
				map.put(MetadatiPratica.pNumeroFascicolo, protocollazione.getNumeroFascicolo());
			} else {
				map.put(MetadatiPratica.pAnnoPG, capofila.getAnnoPG());
				map.put(MetadatiPratica.pNumeroPG, capofila.getNumeroPG());
				map.put(MetadatiPratica.pNumeroFascicolo, capofila.getNumeroFascicolo());
			}
		}

		if (dati.getGruppiVisibilita().size() != 0) {
			map.put(MetadatiPratica.pVisibileA, XmlPluginUtil.getListaVisibilita(dati.getGruppiVisibilita()));
		}

		map.put(MetadatiPratica.pIncaricoA, GenericsUtil.sanitizeNull(dati.getInCaricoA() != null ? dati.getInCaricoA().getUsername() : null));

		map.put(MetadatiPratica.pElencoProtocollazioni, XmlPluginUtil.protocollazioniModulisticaToList(dati.getProtocollazioniCapofila()));
		if (dati.getDatiAggiuntivi() != null) {
			map.put(MetadatiPratica.pDatiAggiuntivi, DatiAggiuntiviUtil.datiAggiuntiviToList(dati.getDatiAggiuntivi()));
		}

		map.put(MetadatiPratica.pElencoProcedimenti, XmlPluginUtil.avvioProcedimentoToList(dati.getProcedimenti()));

		if (dati.getOperatore() != null && dati.getOperatore().getNome() != null) {
			map.put(MetadatiPratica.pOperatore, dati.getOperatore().getNome());
		}

		return map;
	}

	private it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila getPrimoCapofila() {
		List<it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila> protocollazioniCapofila = getDati().getProtocollazioniCapofila();
		if (protocollazioniCapofila.size() == 0) {
			return null;
		}
		Collections.sort(protocollazioniCapofila);
		return protocollazioniCapofila.get(0);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XMLFascicolo) {
			XMLFascicolo other = (XMLFascicolo) obj;
			return other.getAlfrescoPath().equalsIgnoreCase(getAlfrescoPath());
		}

		return false;
	}

	@Override
	public int hashCode() {
		return getAlfrescoPath().hashCode();
	}

	@Override
	public String toString() {
		return getDati().toString();
	}

	@Override
	protected void initPratica(DatiFascicolo t) throws PraticaException {
		this.dati = t;
	}

	/* metodi protected */
	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {
		logger.debug("Serializzazione fascicolo {}", getAlfrescoPath());
		it.eng.consolepec.xmlplugin.jaxb.Fascicolo fascicolo = new it.eng.consolepec.xmlplugin.jaxb.Fascicolo();
		fascicolo.setNote(getDati().getNote());
		jaxb.setFascicolo(fascicolo);
		/* serializzazione dati pratica generici */
		super.serializeDati(jaxb);
		/* serializzazione dati fascicolo */
		jaxb.setTipo(dati.getTipo().getNomeTipologia());

		if (dati.getAnagraficaFascicolo() != null) {
			jaxb.getFascicolo().setAnagraficaFascicolo(XmlPluginUtil.serializeAnagraficaFascicolo(dati.getAnagraficaFascicolo()));
		}

		jaxb.setStato(getDati().getStato().name());

		jaxb.getFascicolo().setTitoloOriginale(getDati().getTitoloOriginale());
		jaxb.getFascicolo().getIdPraticheProcedi().addAll(getDati().getIdPraticheProcedi());

		/* metadati del fascicolo Fatturazione */
		if (dati instanceof DatiFascicoloFatturazione) {
			DatiFascicoloFatturazione datiFascicoloFatturazione = (DatiFascicoloFatturazione) dati;
			jaxb.getFascicolo().setCodicePIva(datiFascicoloFatturazione.getCodicePIva());
			jaxb.getFascicolo().setNumeroFattura(datiFascicoloFatturazione.getNumeroFattura());
			jaxb.getFascicolo().setPIva(datiFascicoloFatturazione.getPIva());
			jaxb.getFascicolo().setRagioneSociale(datiFascicoloFatturazione.getRagioneSociale());
		}

		jaxb.getFascicolo().setCollegamenti(TranslatorBeanCondivisione.getCollegamenti(dati.getCollegamenti()));
		jaxb.getFascicolo().setOperazioni(TranslatorBeanCondivisione.getListaOperazioni(dati.getOperazioni()));

		fascicolo.setAllegati(new Allegati());
		/* informazioni sugli allegati */
		for (Allegato allg : dati.getAllegati()) {
			if (!isAllegatoProtocollato(allg)) {
				Documento documentoJaxb = XmlPluginUtil.getDocumentoFromAllegato(allg);
				jaxb.getFascicolo().getAllegati().getDocumento().add(documentoJaxb);
			}
		}

		// serializzazione eventi iter della pratica
		jaxb.getFascicolo().setEventiIter(new EventiIter());
		for (EventoIterPratica e : dati.getIter()) {
			EventoIter eventoIter = new EventoIter();
			GregorianCalendar gregorianCalendar = new GregorianCalendar();
			gregorianCalendar.setTime(e.getDataEvento());
			try {
				eventoIter.setDataEvento(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
			} catch (DatatypeConfigurationException e1) {
				logger.warn("Errore creazione MLGregorianCalendar", e1);
			}
			eventoIter.setTestoEvento(e.getTestoEvento());
			eventoIter.setTipoEvento(e.getTipoEvento());
			eventoIter.setSerialized(e.isSerialized());
			eventoIter.setAnnoPg(e.getAnnpPG());
			eventoIter.setNumeroPg(e.getNumeroPG());
			eventoIter.setIsSerializationEnabled(e.isSerializationEnabled());
			eventoIter.setCurrentUser(e.getUser());
			jaxb.getFascicolo().getEventiIter().getEventoIter().add(eventoIter);
		}

		List<ProtocollazioneCapofila> protocollazione = fascicolo.getProtocollazione();
		List<it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila> protocollazioniCapofila = dati.getProtocollazioniCapofila();
		for (it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila protCapofila : protocollazioniCapofila) {
			ProtocollazioneCapofila protocollazioneCapofila = serializeProtocollazioneCapofila(protCapofila);
			protocollazione.add(protocollazioneCapofila);
		}

		if (dati.getDatiAggiuntivi() != null) {

			jaxb.getFascicolo().setDatiAggiuntivi(new DatiAggiuntivi());
			jaxb.getFascicolo().getDatiAggiuntivi().setValori(new Valori());

			for (DatoAggiuntivo datoAggiuntivo : dati.getDatiAggiuntivi()) {
				jaxb.getFascicolo().getDatiAggiuntivi().getValori().getValore().add(XmlPluginUtil.serializeDatoAggiuntivo(datoAggiuntivo));
			}
		}

		List<it.eng.consolepec.xmlplugin.jaxb.Procedimento> listaProcedimenti = fascicolo.getProcedimenti();
		for (Procedimento proc : dati.getProcedimenti()) {
			it.eng.consolepec.xmlplugin.jaxb.Procedimento xmlProc = new it.eng.consolepec.xmlplugin.jaxb.Procedimento();
			xmlProc.setCodUtente(proc.getCodUtente());
			PG pg = new PG();
			pg.setAnnoPG(new BigInteger(proc.getAnnoPG().toString()));
			pg.setNumeroPG(proc.getNumeroPG());
			xmlProc.setPG(pg);
			xmlProc.setCodTipologiaProcedimento(new BigInteger(proc.getCodTipologiaProcedimento().toString()));
			xmlProc.setDataInizioDecorrenzaProcedimento(DateUtils.dateToXMLGrCal(proc.getDataInizioDecorrenzaProcedimento()));
			xmlProc.setModAvvioProcedimento(proc.getModAvvioProcedimento());
			if (proc.getCodUnitaOrgCompetenza() != null) {
				xmlProc.setCodUnitaOrgCompetenza(new BigInteger(proc.getCodUnitaOrgCompetenza().toString()));
			}
			if (proc.getCodUnitaOrgResponsabile() != null) {
				xmlProc.setCodUnitaOrgResponsabile(new BigInteger(proc.getCodUnitaOrgResponsabile().toString()));
			}
			xmlProc.setFlagInterruzione(proc.getFlagInterruzione());
			if (proc.getCodQuartiere() != null) {
				xmlProc.setCodQuartiere(new BigInteger(proc.getCodQuartiere().toString()));
			}
			if (proc.getDurata() != null) {
				xmlProc.setDurata(new BigInteger(proc.getDurata().toString()));
			}
			if (proc.getTermine() != null) {
				xmlProc.setTermine(new BigInteger(proc.getTermine().toString()));
			}
			// xmlProc.setDataDecorrenza(XmlPluginUtil.dateToXMLGrCal(proc.getDataDecorrenza()));
			xmlProc.setResponsabile(proc.getResponsabile());
			xmlProc.setProvenienza(proc.getProvenienza());
			xmlProc.setDataChiusura(DateUtils.dateToXMLGrCal(proc.getDataChiusura()));
			xmlProc.setModalitaChiusura(proc.getModalitaChiusura());
			if (proc.getAnnoPGChiusura() != null && proc.getNumeroPGChiusura() != null) {
				PG pgChiusura = new PG();
				pgChiusura.setAnnoPG(new BigInteger(proc.getAnnoPGChiusura().toString()));
				pgChiusura.setNumeroPG(proc.getNumeroPGChiusura());
				xmlProc.setChiusuraPG(pgChiusura);
			}
			listaProcedimenti.add(xmlProc);

		}

		if (dati.getStepIter() != null) {
			it.eng.consolepec.xmlplugin.jaxb.StepIter stepIter = new it.eng.consolepec.xmlplugin.jaxb.StepIter();
			stepIter.setNome(dati.getStepIter().getNome());
			stepIter.setFinale(dati.getStepIter().getFinale());
			stepIter.setIniziale(dati.getStepIter().getIniziale());
			stepIter.setCreaBozza(dati.getStepIter().getCreaBozza());
			stepIter.getDestinatariNotifica().addAll(dati.getStepIter().getDestinatariNotifica());
			stepIter.setDataAggiornamento(DateUtils.dateToXMLGrCal(dati.getStepIter().getDataAggiornamento()));
			jaxb.getFascicolo().setStepIter(stepIter);
		}

	}

	@Override
	protected void serializePraticheCollegate(Pratica praticaJaxb, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata> praticheCollegate) {
		for (it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.PraticaCollegata p : praticheCollegate) {
			if (!isPraticaCollegataProtocollata(p)) {
				logger.debug("Serializzazione info pratica collegata: {}", p);
				it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata pc = new it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata();
				pc.setAlfrescoPath(p.getAlfrescoPath());
				pc.setTipo(p.getTipo());
				pc.setDataCaricamento(DateUtils.dateToXMLGrCal(p.getDataCaricamento()));
				praticaJaxb.getPraticaCollegata().add(pc);
			}
		}
	}

	private boolean isPraticaCollegataProtocollata(it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.PraticaCollegata p) {
		for (DatiFascicolo.ProtocollazioneCapofila protocollazioneCapofila : dati.getProtocollazioniCapofila()) {
			for (DatiFascicolo.PraticaCollegata pratica : protocollazioneCapofila.getPraticheCollegateProtocollate()) {
				if (pratica.equals(p)) {
					return true;
				}
			}
			for (Protocollazione protocollazioneCollegata : protocollazioneCapofila.getProtocollazioniCollegate()) {
				for (DatiFascicolo.PraticaCollegata pratica : protocollazioneCollegata.getPraticheCollegateProtocollate()) {
					if (pratica.equals(p)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isAllegatoProtocollato(Allegato allg) {
		for (DatiFascicolo.ProtocollazioneCapofila protocollazioneCapofila : dati.getProtocollazioniCapofila()) {
			for (Allegato allegato : protocollazioneCapofila.getAllegatiProtocollati()) {
				if (allegato.getNome().equals(allg.getNome())) {
					return true;
				}
			}
			for (Protocollazione protocollazione : protocollazioneCapofila.getProtocollazioniCollegate()) {
				for (Allegato allegato : protocollazione.getAllegatiProtocollati()) {
					if (allegato.getNome().equals(allg.getNome())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private static List<Documento> getAllegatiProtocollati(List<it.eng.consolepec.xmlplugin.jaxb.ProtocollazioneCapofila> protocollazione) {
		List<Documento> alleagatoProtocollato = new ArrayList<Documento>();
		for (ProtocollazioneCapofila protocollazioneCapofila : protocollazione) {
			alleagatoProtocollato.addAll(protocollazioneCapofila.getAllegatiProtocollati().getAllegatoProtocollato());
			for (it.eng.consolepec.xmlplugin.jaxb.Protocollazione prot : protocollazioneCapofila.getProtocollazioniCollegate()) {
				for (Documento documento : prot.getAllegatiProtocollati().getAllegatoProtocollato()) {
					alleagatoProtocollato.add(documento);
				}
			}
		}
		return alleagatoProtocollato;
	}

	private ProtocollazioneCapofila serializeProtocollazioneCapofila(it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila protocollazioneCapofila) {
		if (protocollazioneCapofila == null) {
			return null;
		}

		ProtocollazioneCapofila p = new ProtocollazioneCapofila();
		p.setAnnoPG(new BigInteger(protocollazioneCapofila.getAnnoPG().toString()));

		if (protocollazioneCapofila.getDataprotocollazione() != null) {
			GregorianCalendar dataPRot = new GregorianCalendar();
			dataPRot.setTime(protocollazioneCapofila.getDataprotocollazione());
			try {
				p.setDataprotocollazione(DatatypeFactory.newInstance().newXMLGregorianCalendar(dataPRot));
			} catch (DatatypeConfigurationException e) {
				logger.error("DatatypeConfigurationException", e);
			}
		}
		p.setNumeroPG(protocollazioneCapofila.getNumeroPG());
		p.setNumerofascicolo(protocollazioneCapofila.getNumeroFascicolo());
		Integer annoFascicolo = protocollazioneCapofila.getAnnoFascicolo();
		p.setAnnofascicolo(annoFascicolo != null ? new BigInteger(annoFascicolo.toString()) : null);
		p.setOggetto(protocollazioneCapofila.getOggetto());
		p.setProvenienza(protocollazioneCapofila.getProvenienza());
		p.setRubrica(protocollazioneCapofila.getRubrica());
		p.setSezione(protocollazioneCapofila.getSezione());
		p.setTipologiadocumento(protocollazioneCapofila.getTipologiadocumento());
		p.setTitolo(protocollazioneCapofila.getTitolo());
		p.setNote(protocollazioneCapofila.getNote());
		p.setUtenteprotocollazione(protocollazioneCapofila.getUtenteprotocollazione());
		p.setTipoProtocollazione(XmlPluginUtil.serializeTipoProtocollazione(protocollazioneCapofila.getTipoProtocollazione()));
		p.setFromBA01(protocollazioneCapofila.isFromBa01());

		ProtocollazioneCapofila.AllegatiProtocollati alleagtiProtocollati = new ProtocollazioneCapofila.AllegatiProtocollati();
		List<Documento> listaAllegatiProtocollati = alleagtiProtocollati.getAllegatoProtocollato();

		List<Allegato> allegatiProtocollati = protocollazioneCapofila.getAllegatiProtocollati();
		for (Allegato allegato : allegatiProtocollati) {
			Documento documento = XmlPluginUtil.getDocumentoFromAllegato(allegato);

			/* caricamento dei gruppi abilitati a vedere il fascicolo */
			documento.setGruppivisibilita(new Gruppivisibilita());
			for (GruppoVisibilita gruppo : allegato.getGruppiVisibilita()) {
				documento.getGruppivisibilita().getGruppovisibilita().add(gruppo.getNomeGruppo());
			}

			listaAllegatiProtocollati.add(documento);
		}
		p.setAllegatiProtocollati(alleagtiProtocollati);

		List<it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.PraticaCollegata> praticheCollegateProtocollate = protocollazioneCapofila.getPraticheCollegateProtocollate();

		ProtocollazioneCapofila.PraticheCollegateProtocollate praticheCollegate = new ProtocollazioneCapofila.PraticheCollegateProtocollate();
		List<it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata> listaPraticheCollegate = praticheCollegate.getPraticaCollegata();
		for (it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.PraticaCollegata praticaCollegataProtocollata : praticheCollegateProtocollate) {
			PraticaCollegata praticaCollegata = new PraticaCollegata();
			praticaCollegata.setAlfrescoPath(praticaCollegataProtocollata.getAlfrescoPath());
			praticaCollegata.setTipo(praticaCollegataProtocollata.getTipo());
			praticaCollegata.setDataCaricamento(DateUtils.dateToXMLGrCal(praticaCollegataProtocollata.getDataCaricamento()));

			listaPraticheCollegate.add(praticaCollegata);
		}
		p.setPraticheCollegateProtocollate(praticheCollegate);

		p.setDataArrivo(protocollazioneCapofila.getDataArrivo());
		p.setOraArrivo(protocollazioneCapofila.getOraArrivo());

		List<Protocollazione> protocollazioniCollegate = protocollazioneCapofila.getProtocollazioniCollegate();
		try {
			serializeProtocollazione(protocollazioniCollegate, p.getProtocollazioniCollegate());
		} catch (DatatypeConfigurationException e) {
			logger.error("Errore serializzazione protocollazione ", e);
		}

		return p;

	}

	private static void serializeProtocollazione(List<Protocollazione> protocollazioniCollegate,
			List<it.eng.consolepec.xmlplugin.jaxb.Protocollazione> protocollazioniCollegateJaxb) throws DatatypeConfigurationException {
		if (protocollazioniCollegate != null) {
			for (Protocollazione prot : protocollazioniCollegate) {
				it.eng.consolepec.xmlplugin.jaxb.Protocollazione protocollazione = new it.eng.consolepec.xmlplugin.jaxb.Protocollazione();
				AllegatiProtocollati allegatiProtocollati = new AllegatiProtocollati();
				List<Documento> listaAllegatiProtocollati = allegatiProtocollati.getAllegatoProtocollato();
				for (Allegato allegato : prot.getAllegatiProtocollati()) {
					Documento documento = XmlPluginUtil.getDocumentoFromAllegato(allegato);
					/* caricamento dei gruppi abilitati a vedere il fascicolo */
					documento.setGruppivisibilita(new Gruppivisibilita());
					for (GruppoVisibilita gruppo : allegato.getGruppiVisibilita()) {
						documento.getGruppivisibilita().getGruppovisibilita().add(gruppo.getNomeGruppo());
					}
					listaAllegatiProtocollati.add(documento);
				}
				List<it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.PraticaCollegata> praticheCollegateProtocollate = prot.getPraticheCollegateProtocollate();
				it.eng.consolepec.xmlplugin.jaxb.Protocollazione.PraticheCollegateProtocollate praticheCollegateProtocollate2 = new it.eng.consolepec.xmlplugin.jaxb.Protocollazione.PraticheCollegateProtocollate();
				List<it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata> listaPraticheCollegate = praticheCollegateProtocollate2.getPraticaCollegata();
				for (it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.PraticaCollegata praticaCollegataProtocollata : praticheCollegateProtocollate) {
					PraticaCollegata praticaCollegata = new PraticaCollegata();
					praticaCollegata.setAlfrescoPath(praticaCollegataProtocollata.getAlfrescoPath());
					praticaCollegata.setTipo(praticaCollegataProtocollata.getTipo());

					praticaCollegata.setDataCaricamento(DateUtils.dateToXMLGrCal(praticaCollegataProtocollata.getDataCaricamento()));

					listaPraticheCollegate.add(praticaCollegata);
				}
				protocollazione.setAllegatiProtocollati(allegatiProtocollati);
				protocollazione.setPraticheCollegateProtocollate(praticheCollegateProtocollate2);
				protocollazione.setAnnoPG(new BigInteger(prot.getAnnoPG().toString()));

				if (prot.getDataprotocollazione() != null) {
					GregorianCalendar dataPRot = new GregorianCalendar();
					dataPRot.setTime(prot.getDataprotocollazione());
					protocollazione.setDataprotocollazione(DatatypeFactory.newInstance().newXMLGregorianCalendar(dataPRot));
				}
				protocollazione.setNumeroPG(prot.getNumeroPG());
				protocollazione.setNumerofascicolo(prot.getNumeroFascicolo());
				Integer annoFascicolo = prot.getAnnoFascicolo();
				protocollazione.setAnnofascicolo(annoFascicolo != null ? new BigInteger(annoFascicolo.toString()) : null);
				protocollazione.setOggetto(prot.getOggetto());
				protocollazione.setPraticheCollegateProtocollate(praticheCollegateProtocollate2);
				protocollazione.setProvenienza(prot.getProvenienza());
				protocollazione.setRubrica(prot.getRubrica());
				protocollazione.setSezione(prot.getSezione());
				protocollazione.setTipologiadocumento(prot.getTipologiadocumento());
				protocollazione.setTitolo(prot.getTitolo());
				protocollazione.setNote(prot.getNote());
				protocollazione.setUtenteprotocollazione(prot.getUtenteprotocollazione());
				protocollazione.setNumeroRegistro(prot.getNumeroRegistro());
				protocollazione.setTipoProtocollazione(XmlPluginUtil.serializeTipoProtocollazione(prot.getTipoProtocollazione()));
				if (prot.getAnnoRegistro() != null) {
					protocollazione.setAnnoRegistro(new BigInteger(prot.getAnnoRegistro().toString()));
				}

				protocollazione.setDataArrivo(prot.getDataArrivo());
				protocollazione.setOraArrivo(prot.getOraArrivo());

				protocollazioniCollegateJaxb.add(protocollazione);
			}
		}
	}

	@Override
	protected void loadDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {
		logger.debug("Deserializzazione fascicolo");
		DatiFascicolo.Builder builder = getBuilder();
		/* informazioni sulla protocollazione */

		builder.setConsoleFileName(jaxb.getConsoleFileName());
		if (jaxb.getDataCreazione() != null) {
			builder.setDataCreazione(jaxb.getDataCreazione().toGregorianCalendar().getTime());
		}
		builder.setFolderPath(jaxb.getFolderPath());
		builder.setProvenienza(jaxb.getProvenienza());
		builder.setTitolo(jaxb.getTitolo());
		builder.setTipologiaPratica(new TipologiaPratica(jaxb.getTipo()));
		builder.setIdDocumentale(jaxb.getIdDocumentale());
		builder.setUtenteCreazione(jaxb.getUtenteCreazione());
		builder.setUsernameCreazione(jaxb.getUsernameCreazione());

		/* dati del fascicolo Fatturazione */
		if (jaxb.getTipo().equals(TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA.getNomeTipologia())) {
			DatiFascicoloFatturazione.Builder builderFatturazione = (it.eng.consolepec.xmlplugin.pratica.fatturazione.DatiFascicoloFatturazione.Builder) builder;
			builderFatturazione.setNumeroFattura(jaxb.getFascicolo().getNumeroFattura());
			builderFatturazione.setCodicePIva(jaxb.getFascicolo().getCodicePIva());
			builderFatturazione.setRagioneSociale(jaxb.getFascicolo().getRagioneSociale());
			builderFatturazione.setPIva(jaxb.getFascicolo().getPIva());
		}

		if (jaxb.getIncaricoa() != null) {
			Date dataPresaInCarico = jaxb.getIncaricoa().getDatapresaincarico() == null ? new Date() : DateUtils.xmlGrCalToDate(jaxb.getIncaricoa().getDatapresaincarico());
			Utente inCaricoA = new Utente(jaxb.getIncaricoa().getUsername(), jaxb.getIncaricoa().getNome(), jaxb.getIncaricoa().getCognome(), jaxb.getIncaricoa().getMatricola(),
					jaxb.getIncaricoa().getCodicefiscale(), dataPresaInCarico);
			builder.setInCaricoA(inCaricoA);
		}
		/* caricamento dei gruppi abilitati */
		if (jaxb.getGruppivisibilita() != null) {
			List<GruppoVisibilita> gruppi = XmlPluginUtil.getGruppiVisibilita(jaxb);
			builder.setGruppiVisibilita(gruppi);
		}

		initExtendedDati(builder, jaxb);

		/* informazioni specifiche di fascicolo */
		dati = builder.construct();

		dati.setTitoloOriginale(jaxb.getFascicolo().getTitoloOriginale());
		dati.getIdPraticheProcedi().addAll(jaxb.getFascicolo().getIdPraticheProcedi());

		dati.getCollegamenti().addAll(TranslatorBeanCondivisione.getCollegamenti(jaxb.getFascicolo().getCollegamenti()));
		dati.getOperazioni().addAll(TranslatorBeanCondivisione.getListaOperazioni(jaxb.getFascicolo().getOperazioni()));

		/* informazioni sugli allegati */
		Allegati allg = jaxb.getFascicolo().getAllegati();
		Set<Allegato> allegati = new LinkedHashSet<DatiPratica.Allegato>();
		if (allg != null) {
			for (Documento doc : allg.getDocumento()) {
				Allegato allegato = XmlPluginUtil.getAllegatoFromDocumento(doc, dati);
				allegati.add(allegato);
			}
		}

		List<Documento> allegatiProtocollati = getAllegatiProtocollati(jaxb.getFascicolo().getProtocollazione());
		if (allegatiProtocollati != null) {
			for (Documento doc : allegatiProtocollati) {
				Allegato allegato = XmlPluginUtil.getAllegatoFromDocumento(doc, dati);
				allegati.add(allegato);
			}
		}

		dati.getAllegati().addAll(new ArrayList<Allegato>(allegati));
		dati.setLetto(jaxb.isLetto());
		dati.setStato(DatiFascicolo.Stato.valueOf(jaxb.getStato()));
		dati.setNote(jaxb.getFascicolo().getNote());

		if (!Strings.isNullOrEmpty(jaxb.getFascicolo().getAnagraficaFascicolo())) {
			dati.setAnagraficaFascicolo(XmlPluginUtil.deserializeAnagraficaFascicolo(jaxb.getFascicolo().getAnagraficaFascicolo()));
		}

		Set<DatiPratica.PraticaCollegata> pCollegate = new LinkedHashSet<DatiPratica.PraticaCollegata>();

		for (it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata pc : jaxb.getPraticaCollegata()) {
			it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata pc2 = dati.new PraticaCollegata(pc.getAlfrescoPath(), pc.getTipo(), DateUtils.xmlGrCalToDate(pc.getDataCaricamento()));
			pc2.setAlfrescoPath(pc.getAlfrescoPath());
			pc2.setTipo(pc.getTipo());
			pc2.setDataCaricamento(DateUtils.xmlGrCalToDate(pc.getDataCaricamento()));

			logger.debug("Deserializzazione info pratica collegata: {}", pc2);
			pCollegate.add(pc2);
		}

		for (ProtocollazioneCapofila protocollazioneCapofila : jaxb.getFascicolo().getProtocollazione()) {
			for (PraticaCollegata pCapofila : protocollazioneCapofila.getPraticheCollegateProtocollate().getPraticaCollegata()) {
				it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata pc2 = dati.new PraticaCollegata(pCapofila.getAlfrescoPath(), pCapofila.getTipo(),
						DateUtils.xmlGrCalToDate(pCapofila.getDataCaricamento()));
				pc2.setAlfrescoPath(pCapofila.getAlfrescoPath());
				pc2.setTipo(pCapofila.getTipo());
				pc2.setDataCaricamento(DateUtils.xmlGrCalToDate(pCapofila.getDataCaricamento()));

				logger.debug("Deserializzazione info pratica collegata: {}", pc2);
				pCollegate.add(pc2);
			}
			for (it.eng.consolepec.xmlplugin.jaxb.Protocollazione pProt : protocollazioneCapofila.getProtocollazioniCollegate()) {
				for (PraticaCollegata pCapofila : pProt.getPraticheCollegateProtocollate().getPraticaCollegata()) {
					it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata pc2 = dati.new PraticaCollegata(pCapofila.getAlfrescoPath(), pCapofila.getTipo(),
							DateUtils.xmlGrCalToDate(pCapofila.getDataCaricamento()));
					pc2.setAlfrescoPath(pCapofila.getAlfrescoPath());
					pc2.setTipo(pCapofila.getTipo());
					pc2.setDataCaricamento(DateUtils.xmlGrCalToDate(pCapofila.getDataCaricamento()));

					logger.debug("Deserializzazione info pratica collegata: {}", pc2);
					pCollegate.add(pc2);
				}
			}
		}

		ArrayList<DatiFascicolo.PraticaCollegata> praticheCollegate = new ArrayList<DatiFascicolo.PraticaCollegata>(pCollegate);
		for (DatiFascicolo.PraticaCollegata pc : praticheCollegate) {
			addPraticaCollegata(pc);
		}

		List<it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila> listProtocollazioneCapofila = new ArrayList<it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila>();

		for (ProtocollazioneCapofila protocollazioneCapofila : jaxb.getFascicolo().getProtocollazione()) {
			listProtocollazioneCapofila.add(loadProtocollazioneCapofila(protocollazioneCapofila));
		}
		dati.setProtocollazioniCapofila(listProtocollazioneCapofila);

		List<EventoIter> eventiIter = jaxb.getFascicolo().getEventiIter() == null ? new ArrayList<EventoIter>() : jaxb.getFascicolo().getEventiIter().getEventoIter();
		for (EventoIter eventoIter : eventiIter) {
			EventoIterPratica eventoIterPratica = dati.new EventoIterPratica();
			eventoIterPratica.setDataEvento(eventoIter.getDataEvento().toGregorianCalendar().getTime());
			eventoIterPratica.setSerialized(eventoIter.isSerialized());
			eventoIterPratica.setSerializationEnabled(eventoIter.isIsSerializationEnabled());
			eventoIterPratica.setTestoEvento(eventoIter.getTestoEvento());
			eventoIterPratica.setTipoEvento(eventoIter.getTipoEvento());
			eventoIterPratica.setAnnpPG(eventoIter.getAnnoPg());
			eventoIterPratica.setNumeroPG(eventoIter.getNumeroPg());
			eventoIterPratica.setUser(eventoIter.getCurrentUser());
			dati.getIter().add(eventoIterPratica);
		}

		if (jaxb.getFascicolo() != null && jaxb.getFascicolo().getDatiAggiuntivi() != null && jaxb.getFascicolo().getDatiAggiuntivi().getValori() != null) {

			for (final it.eng.consolepec.xmlplugin.jaxb.DatoAggiuntivo jaxbVal : jaxb.getFascicolo().getDatiAggiuntivi().getValori().getValore()) {
				dati.getDatiAggiuntivi().add(XmlPluginUtil.loadDatoAggiuntivo(jaxbVal));
			}
		}

		List<it.eng.consolepec.xmlplugin.jaxb.Procedimento> procedimenti = jaxb.getFascicolo().getProcedimenti();
		for (it.eng.consolepec.xmlplugin.jaxb.Procedimento xmlProc : procedimenti) {

			Procedimento p = new Procedimento();
			p.setCodUtente(xmlProc.getCodUtente());
			p.setAnnoPG(xmlProc.getPG().getAnnoPG().intValue());
			p.setNumeroPG(xmlProc.getPG().getNumeroPG());
			p.setCodTipologiaProcedimento(xmlProc.getCodTipologiaProcedimento().intValue());
			p.setDataInizioDecorrenzaProcedimento(DateUtils.xmlGrCalToDate(xmlProc.getDataInizioDecorrenzaProcedimento()));
			p.setModAvvioProcedimento(xmlProc.getModAvvioProcedimento());
			if (xmlProc.getCodUnitaOrgResponsabile() != null) {
				p.setCodUnitaOrgResponsabile(xmlProc.getCodUnitaOrgResponsabile().intValue());
			}
			if (xmlProc.getCodUnitaOrgCompetenza() != null) {
				p.setCodUnitaOrgCompetenza(xmlProc.getCodUnitaOrgCompetenza().intValue());
			}
			p.setFlagInterruzione(xmlProc.getFlagInterruzione());
			if (xmlProc.getCodQuartiere() != null) {
				p.setCodQuartiere(xmlProc.getCodQuartiere().intValue());
			}
			if (xmlProc.getDurata() != null) {
				p.setDurata((xmlProc.getDurata().intValue()));
			}
			if (xmlProc.getTermine() != null) {
				p.setTermine(xmlProc.getTermine().intValue());
			}
			// p.setDataDecorrenza(XmlPluginUtil.xmlGrCalToDate(xmlProc.getDataDecorrenza()));
			p.setResponsabile(xmlProc.getResponsabile());
			p.setProvenienza(xmlProc.getProvenienza());
			p.setDataChiusura(DateUtils.xmlGrCalToDate(xmlProc.getDataChiusura()));
			p.setModalitaChiusura(xmlProc.getModalitaChiusura());
			if (xmlProc.getChiusuraPG() != null && xmlProc.getChiusuraPG().getAnnoPG() != null && xmlProc.getChiusuraPG().getNumeroPG() != null) {
				p.setAnnoPGChiusura(xmlProc.getChiusuraPG().getAnnoPG().intValue());
				p.setNumeroPGChiusura(xmlProc.getChiusuraPG().getNumeroPG());
			}
			dati.getProcedimenti().add(p);
		}

		if (jaxb.getFascicolo().getStepIter() != null) {
			it.eng.consolepec.xmlplugin.jaxb.StepIter i = jaxb.getFascicolo().getStepIter();
			dati.setStepIter(new StepIter(i.getNome(), i.isFinale(), i.isIniziale(), i.isCreaBozza(), i.getDestinatariNotifica(), DateUtils.xmlGrCalToDate(i.getDataAggiornamento())));
		}

		loadAssegnazioneEsterna(jaxb, dati);
		loadNotifiche(jaxb, dati);

		if (jaxb.getOperatore() != null) {
			dati.getOperatore().setNome(jaxb.getOperatore());
		}

		if (jaxb.getOperativitaRidotta() != null) {
			OperativitaRidotta operativitaRidotta = new OperativitaRidotta();

			for (OperazioneOperativitaRidotta ovrJaxb : jaxb.getOperativitaRidotta().getOperazioni()) {
				it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta ovr = new it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta(
						ovrJaxb.getNomeOperazione());

				for (String accessoConsentito : ovrJaxb.getAccessiConsentiti()) {
					TipoAccesso tipoAccesso = TipoAccesso.fromStringValue(accessoConsentito);
					if (tipoAccesso != null) {
						ovr.getAccessiConsentiti().add(tipoAccesso);
					}
				}

				operativitaRidotta.getOperazioni().add(ovr);
			}

			dati.setOperativitaRidotta(operativitaRidotta);

		}

	}

	protected void initExtendedDati(Builder builder, Pratica jaxb) {

	}

	/**
	 * @return
	 */
	protected Builder getBuilder() {
		return new Builder();
	}

	private it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila loadProtocollazioneCapofila(ProtocollazioneCapofila protocollazioneCapofilaJaxb) {

		List<Documento> alleagatiProtocollati = protocollazioneCapofilaJaxb.getAllegatiProtocollati().getAllegatoProtocollato();
		List<Allegato> allegati = new ArrayList<DatiPratica.Allegato>();
		for (Documento documento : alleagatiProtocollati) {
			Allegato allegato = XmlPluginUtil.getAllegatoFromDocumento(documento, dati);
			allegati.add(allegato);
		}
		List<PraticaCollegata> praticheCollegate = protocollazioneCapofilaJaxb.getPraticheCollegateProtocollate().getPraticaCollegata();
		List<DatiFascicolo.PraticaCollegata> praticheCollegateProtocollate = new ArrayList<DatiFascicolo.PraticaCollegata>();
		for (PraticaCollegata pratica : praticheCollegate) {
			praticheCollegateProtocollate.add(dati.new PraticaCollegata(pratica.getAlfrescoPath(), pratica.getTipo(), DateUtils.xmlGrCalToDate(pratica.getDataCaricamento())));
		}
		List<Protocollazione> protocollazioniCollegate = loadProtocollazioniCollegate(protocollazioneCapofilaJaxb.getProtocollazioniCollegate());

		DatiFascicolo.ProtocollazioneCapofilaBuilder builderCapofila = new DatiFascicolo.ProtocollazioneCapofilaBuilder(dati);
		builderCapofila.setAllegatiProtocollati(allegati);
		if (protocollazioneCapofilaJaxb.getAnnoPG() != null) {
			builderCapofila.setAnnoPG(protocollazioneCapofilaJaxb.getAnnoPG().intValue());
		}
		if (protocollazioneCapofilaJaxb.getDataprotocollazione() != null) {
			builderCapofila.setDataprotocollazione(protocollazioneCapofilaJaxb.getDataprotocollazione().toGregorianCalendar().getTime());
		}
		builderCapofila.setNumeroFascicolo(protocollazioneCapofilaJaxb.getNumerofascicolo());
		BigInteger annofascicolo = protocollazioneCapofilaJaxb.getAnnofascicolo();
		builderCapofila.setAnnoFascicolo(annofascicolo != null ? annofascicolo.intValue() : null);
		builderCapofila.setNumeroPG(protocollazioneCapofilaJaxb.getNumeroPG());
		builderCapofila.setOggetto(protocollazioneCapofilaJaxb.getOggetto());
		builderCapofila.setPraticheCollegateProtocollate(praticheCollegateProtocollate);
		builderCapofila.setProtocollazioniCollegate(protocollazioniCollegate);
		builderCapofila.setProvenienza(protocollazioneCapofilaJaxb.getProvenienza());
		builderCapofila.setRubrica(protocollazioneCapofilaJaxb.getRubrica());
		builderCapofila.setSezione(protocollazioneCapofilaJaxb.getSezione());
		builderCapofila.setTipologiadocumento(protocollazioneCapofilaJaxb.getTipologiadocumento());
		builderCapofila.setTitolo(protocollazioneCapofilaJaxb.getTitolo());
		builderCapofila.setNote(protocollazioneCapofilaJaxb.getNote());
		builderCapofila.setUtenteprotocollazione(protocollazioneCapofilaJaxb.getUtenteprotocollazione());
		builderCapofila.setNumeroRegistro(protocollazioneCapofilaJaxb.getNumeroRegistro());
		builderCapofila.setTipoProtocollazione(XmlPluginUtil.loadTipoProtocollazione(protocollazioneCapofilaJaxb.getTipoProtocollazione()));
		builderCapofila.setFromBa01(protocollazioneCapofilaJaxb.isFromBA01());
		if (protocollazioneCapofilaJaxb.getAnnoRegistro() != null) {
			builderCapofila.setAnnoPG(protocollazioneCapofilaJaxb.getAnnoRegistro().intValue());
		}
		builderCapofila.setDataArrivo(protocollazioneCapofilaJaxb.getDataArrivo());
		builderCapofila.setOraArrivo(protocollazioneCapofilaJaxb.getOraArrivo());

		return builderCapofila.construct();
	}

	private List<Protocollazione> loadProtocollazioniCollegate(List<it.eng.consolepec.xmlplugin.jaxb.Protocollazione> protocollazioniCollegate) {
		List<Protocollazione> loadProtocollazioniCollegate = new ArrayList<DatiFascicolo.Protocollazione>();
		for (it.eng.consolepec.xmlplugin.jaxb.Protocollazione prot : protocollazioniCollegate) {
			List<Documento> alleagatiProtocollati = prot.getAllegatiProtocollati().getAllegatoProtocollato();
			List<DatiPratica.Allegato> allegati = new ArrayList<DatiPratica.Allegato>();
			for (Documento documento : alleagatiProtocollati) {
				Allegato allegato = XmlPluginUtil.getAllegatoFromDocumento(documento, dati);
				allegati.add(allegato);
			}

			List<PraticaCollegata> praticheCollegate = prot.getPraticheCollegateProtocollate().getPraticaCollegata();
			List<DatiFascicolo.PraticaCollegata> praticheCollegateProtocollate = new ArrayList<DatiFascicolo.PraticaCollegata>();
			for (PraticaCollegata pratica : praticheCollegate) {
				praticheCollegateProtocollate.add(dati.new PraticaCollegata(pratica.getAlfrescoPath(), pratica.getTipo(), DateUtils.xmlGrCalToDate(pratica.getDataCaricamento())));
			}

			DatiFascicolo.ProtocollazioneBuilder builder = new DatiFascicolo.ProtocollazioneBuilder(dati);

			builder.setAllegatiProtocollati(allegati);
			builder.setAnnoPG(prot.getAnnoPG().intValue());
			if (prot.getDataprotocollazione() != null) {
				builder.setDataprotocollazione(prot.getDataprotocollazione().toGregorianCalendar().getTime());
			}
			builder.setNumeroFascicolo(prot.getNumerofascicolo());
			BigInteger annofascicolo = prot.getAnnofascicolo();
			builder.setAnnoFascicolo(annofascicolo != null ? annofascicolo.intValue() : null);
			builder.setNumeroPG(prot.getNumeroPG());
			builder.setOggetto(prot.getOggetto());
			builder.setPraticheCollegateProtocollate(praticheCollegateProtocollate);
			builder.setProvenienza(prot.getProvenienza());
			builder.setRubrica(prot.getRubrica());
			builder.setSezione(prot.getSezione());
			builder.setTipologiadocumento(prot.getTipologiadocumento());
			builder.setTitolo(prot.getTitolo());
			builder.setNote(prot.getNote());
			builder.setUtenteprotocollazione(prot.getUtenteprotocollazione());
			builder.setNumeroRegistro(prot.getNumeroRegistro());
			builder.setTipoProtocollazione(XmlPluginUtil.loadTipoProtocollazione(prot.getTipoProtocollazione()));
			if (prot.getAnnoRegistro() != null) {
				builder.setAnnoPG(prot.getAnnoRegistro().intValue());
			}
			builder.setDataArrivo(prot.getDataArrivo());
			builder.setOraArrivo(prot.getOraArrivo());

			loadProtocollazioniCollegate.add(builder.construct());
		}
		return loadProtocollazioniCollegate;
	}

	@Override
	public DatiFascicoloTaskAdapter getDatiPraticaTaskAdapter() {
		DatiFascicolo dati = getDati();
		return new DatiFascicoloTaskAdapter(dati);
	}

	@Override
	public boolean isRiattivabile() {
		Set<Task<?>> tasks2 = this.getTasks();
		for (Task<?> t : tasks2) {
			if (t.isAttivo() && t instanceof RiattivaFascicoloTask) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected Date getDate() {
		return dati.getDataCreazione();
	}

	@Override
	protected String getProvenienza() {
		return getDati().getProvenienza();
	}

	@Override
	public boolean isProtocollaAbilitato() {
		return !getDati().getStato().equals(Stato.ARCHIVIATO);
	}
}
