package it.eng.consolepec.xmlplugin.pratica.comunicazione;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.EventoIterPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.XMLPratica;
import it.eng.consolepec.xmlplugin.jaxb.Comunicazione.Allegati;
import it.eng.consolepec.xmlplugin.jaxb.Comunicazione.EventiIter;
import it.eng.consolepec.xmlplugin.jaxb.Documento;
import it.eng.consolepec.xmlplugin.jaxb.EventoIter;
import it.eng.consolepec.xmlplugin.jaxb.Gruppivisibilita;
import it.eng.consolepec.xmlplugin.jaxb.Pratica;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione.Invio;
import it.eng.consolepec.xmlplugin.tasks.gestionemodulistica.GestionePraticaModulisticaTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class XMLComunicazione extends XMLPratica<DatiComunicazione> implements Comunicazione {

	private DatiComunicazione dati;
	protected Logger logger = LoggerFactory.getLogger(XMLComunicazione.class);

	public XMLComunicazione() {
		// richiesto da reflection
	}

	@Override
	public DatiComunicazioneTaskAdapter getDatiPraticaTaskAdapter() {
		DatiComunicazione t = getDati();
		return new DatiComunicazioneTaskAdapter(t);
	}

	@Override
	public String getSubFolderPath() {
		return "COMUNICAZIONE";
	}

	@Override
	public DatiComunicazione getDati() {
		return dati;
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {
		logger.debug("Serializzazione pratica modulistica {}", getAlfrescoPath());
		try {
			/* serializzazione dati generici */
			super.serializeDati(jaxb);

			if (getDati().getStato() != null) {
				jaxb.setStato(getDati().getStato().name());
			}

			jaxb.setComunicazione(new it.eng.consolepec.xmlplugin.jaxb.Comunicazione());

			jaxb.getComunicazione().setCodComunicazione(getDati().getCodComunicazione());
			jaxb.getComunicazione().setDescComunicazione(getDati().getDescComunicazione());
			jaxb.getComunicazione().setIdTemplate(getDati().getIdTemplate());

			for (Invio invio : getDati().getInvii()) {

				it.eng.consolepec.xmlplugin.jaxb.InvioComunicazione jaxbInvio = new it.eng.consolepec.xmlplugin.jaxb.InvioComunicazione();

				jaxbInvio.setFlgTestProd(invio.getFlgTestProd());
				if (invio.getNumRecordTest() != null)
					jaxbInvio.setNumRecordTest(invio.getNumRecordTest().toString());
				jaxbInvio.setPecDestinazioneTest(invio.getPecDestinazioneTest());
				jaxbInvio.setCodEsito(invio.getCodEsito());

				jaxb.getComunicazione().getInvii().add(jaxbInvio);
			}
			jaxb.getComunicazione().setAllegati(new Allegati());
			/* informazioni sugli allegati */
			for (Allegato allg : dati.getAllegati()) {
				Documento documentoJaxb = XmlPluginUtil.getDocumentoFromAllegato(allg);

				documentoJaxb.setGruppivisibilita(new Gruppivisibilita());
				for (GruppoVisibilita gruppo : allg.getGruppiVisibilita()) {
					documentoJaxb.getGruppivisibilita().getGruppovisibilita().add(gruppo.getNomeGruppo());
				}
				jaxb.getComunicazione().getAllegati().getDocumento().add(documentoJaxb);
			}

			jaxb.getComunicazione().setEventiIter(new EventiIter());
			for (EventoIterPratica e : dati.getIter()) {
				EventoIter eventoIter = new EventoIter();
				GregorianCalendar gregorianCalendar = new GregorianCalendar();
				gregorianCalendar.setTime(e.getDataEvento());
				try {
					eventoIter.setDataEvento(DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar));
				} catch (DatatypeConfigurationException e1) {}
				eventoIter.setTestoEvento(e.getTestoEvento());
				eventoIter.setTipoEvento(e.getTipoEvento());
				eventoIter.setSerialized(e.isSerialized());
				eventoIter.setAnnoPg(e.getAnnpPG());
				eventoIter.setNumeroPg(e.getNumeroPG());
				eventoIter.setIsSerializationEnabled(e.isSerializationEnabled());
				eventoIter.setCurrentUser(e.getUser());
				jaxb.getComunicazione().getEventiIter().getEventoIter().add(eventoIter);
			}

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione");
		}

	}

	@Override
	protected void loadDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {
		DatiComunicazione.Builder builder = new DatiComunicazione.Builder();

		builder.setConsoleFileName(jaxb.getConsoleFileName());
		if (jaxb.getDataCreazione() != null)
			builder.setDataCreazione(jaxb.getDataCreazione().toGregorianCalendar().getTime());
		builder.setFolderPath(jaxb.getFolderPath());
		builder.setIdDocumentale(jaxb.getIdDocumentale());
		builder.setUtenteCreazione(jaxb.getUtenteCreazione());
		builder.setUsernameCreazione(jaxb.getUsernameCreazione());

		if (jaxb.getGruppivisibilita() != null) {
			List<GruppoVisibilita> gruppi = XmlPluginUtil.getGruppiVisibilita(jaxb);
			builder.setGruppiVisibilita(gruppi);
		}

		if (jaxb.getIncaricoa() != null) {
			Date dataPresaInCarico = jaxb.getIncaricoa().getDatapresaincarico() == null ? new Date() : DateUtils.xmlGrCalToDate(jaxb.getIncaricoa().getDatapresaincarico());
			Utente inCaricoA = new Utente(jaxb.getIncaricoa().getUsername(), jaxb.getIncaricoa().getNome(), jaxb.getIncaricoa().getCognome(), jaxb.getIncaricoa().getMatricola(),
					jaxb.getIncaricoa().getCodicefiscale(), dataPresaInCarico);
			builder.setInCaricoA(inCaricoA);
		}

		dati = builder.construct();

		dati.setLetto(jaxb.isLetto());
		if (jaxb.getStato() != null) {
			dati.setStato(DatiComunicazione.Stato.valueOf(jaxb.getStato()));
		}

		dati.setCodComunicazione(jaxb.getComunicazione().getCodComunicazione());
		dati.setDescComunicazione(jaxb.getComunicazione().getDescComunicazione());
		dati.setIdTemplate(jaxb.getComunicazione().getIdTemplate());

		for (it.eng.consolepec.xmlplugin.jaxb.InvioComunicazione invio : jaxb.getComunicazione().getInvii()) {

			Invio i = new Invio();

			i.setFlgTestProd(invio.getFlgTestProd());
			if (invio.getNumRecordTest() != null)
				i.setNumRecordTest(Integer.valueOf(invio.getNumRecordTest()));
			i.setPecDestinazioneTest(invio.getPecDestinazioneTest());
			i.setCodEsito(invio.getCodEsito());

			dati.getInvii().add(i);
		}

		Allegati allg = jaxb.getComunicazione().getAllegati();
		Set<Allegato> allegati = new LinkedHashSet<DatiPratica.Allegato>();
		if (allg != null) {
			for (Documento doc : allg.getDocumento()) {
				Allegato allegato = XmlPluginUtil.getAllegatoFromDocumento(doc, dati);
				allegati.add(allegato);
			}
		}

		dati.getAllegati().addAll(new ArrayList<Allegato>(allegati));

		List<EventoIter> eventiIter = jaxb.getComunicazione().getEventiIter() == null ? new ArrayList<EventoIter>() : jaxb.getComunicazione().getEventiIter().getEventoIter();
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

		for (it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata pc : jaxb.getPraticaCollegata()) {
			it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata pc2 = dati.new PraticaCollegata(pc.getAlfrescoPath(), pc.getTipo(), DateUtils.xmlGrCalToDate(pc.getDataCaricamento()));
			pc2.setAlfrescoPath(pc.getAlfrescoPath());
			pc2.setTipo(pc.getTipo());
			pc2.setDataCaricamento(DateUtils.xmlGrCalToDate(pc.getDataCaricamento()));

			logger.debug("Deserializzazione info pratica collegata: {}", pc2);
			addPraticaCollegata(pc2);
		}
	}

	@Override
	protected void initPratica(DatiComunicazione dati) throws PraticaException {
		this.dati = dati;
	}

	@Override
	public boolean isAttiva() {
		return true;
	}

	@Override
	public Map<MetadatiPratica, Object> getMetadata() {
		Map<MetadatiPratica, Object> map = new HashMap<MetadatiPratica, Object>();

		map.put(MetadatiPratica.pDataCreazione, dati.getDataCreazione());
		map.put(MetadatiPratica.pIdDocumentale, getDati().getIdDocumentale());
		map.put(MetadatiPratica.pTipoPratica, getDati().getTipo().getNomeTipologia());
		map.put(MetadatiPratica.pTitolo, dati.getTitolo());
		map.put(MetadatiPratica.pLetto, dati.isLetto());
		map.put(MetadatiPratica.pData, dati.getDataCreazione());
		map.put(MetadatiPratica.pInoltratoDa, getInoltratoDa());
		map.put(MetadatiPratica.pProvenienza, getProvenienza());
		map.put(MetadatiPratica.pStato, getDati().getStato().toString());

		if (dati.getUtenteCreazione() != null)
			map.put(MetadatiPratica.pUtenteCreazione, dati.getUtenteCreazione());

		if (dati.getGruppiVisibilita().size() != 0)
			map.put(MetadatiPratica.pVisibileA, XmlPluginUtil.getListaVisibilita(dati.getGruppiVisibilita()));

		map.put(MetadatiPratica.pIncaricoA, GenericsUtil.sanitizeNull(dati.getInCaricoA() != null ? dati.getInCaricoA().getUsername() : null));

		if (dati.getGruppiVisibilita().size() != 0)
			map.put(MetadatiPratica.pVisibileA, XmlPluginUtil.getListaVisibilita(dati.getGruppiVisibilita()));

		if (getDati().getAssegnatarioCorrente() != null) {
			map.put(MetadatiPratica.pAssegnatoA, getDati().getAssegnatarioCorrente());

		} else {
			for (Task<?> task : getTasks()) {
				if (task instanceof GestionePraticaModulisticaTask && task.isAttivo()) {
					GestionePraticaModulisticaTask taskGestione = (GestionePraticaModulisticaTask) task;
					map.put(MetadatiPratica.pAssegnatoA, taskGestione.getDati().getAssegnatario().getNome());
					break;
				}
			}
		}

		map.put(MetadatiPratica.pCodiceComunicazione, dati.getCodComunicazione());
		map.put(MetadatiPratica.pDescrizioneComunicazione, getDati().getDescComunicazione());
		map.put(MetadatiPratica.pIdTemplateComunicazione, getDati().getIdTemplate());

		return map;
	}

	@Override
	public Map<String, Object> getMetadataString() {
		Map<String, Object> metadati = new HashMap<String, Object>();
		Map<MetadatiPratica, Object> metadatiIn = getMetadata();
		for (MetadatiPratica key : metadatiIn.keySet()) {
			metadati.put(key.getNome(), metadatiIn.get(key));
		}

		return metadati;
	}

	@Override
	public int hashCode() {
		return getAlfrescoPath().hashCode();
	};

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XMLComunicazione) {
			XMLComunicazione other = (XMLComunicazione) obj;
			return other.getAlfrescoPath().equalsIgnoreCase(getAlfrescoPath());
		}

		return false;
	}

	@Override
	public String toString() {
		return "Comunicazione: " + dati.toString();
	}

	@Override
	public boolean isRiattivabile() {

		return false;
	}

	@Override
	public boolean isProtocollaAbilitato() {
		return false;
	}

	@Override
	protected Date getDate() {
		return dati.getDataCreazione();
	}

	@Override
	protected String getProvenienza() {
		return dati.getProvenienza();
	}

	@Override
	protected void serializePraticheCollegate(Pratica praticaJaxb, List<PraticaCollegata> praticheCollegate) {
		for (it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.PraticaCollegata p : praticheCollegate) {
			it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata pc = new it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata();
			pc.setAlfrescoPath(p.getAlfrescoPath());
			pc.setTipo(p.getTipo());
			pc.setDataCaricamento(DateUtils.dateToXMLGrCal(p.getDataCaricamento()));
			praticaJaxb.getPraticaCollegata().add(pc);
		}
	}

}
