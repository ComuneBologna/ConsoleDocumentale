package it.eng.consolepec.xmlplugin.pratica.modulistica;

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

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.XMLPratica;
import it.eng.consolepec.xmlplugin.jaxb.Documento;
import it.eng.consolepec.xmlplugin.jaxb.Gruppivisibilita;
import it.eng.consolepec.xmlplugin.jaxb.Modulistica;
import it.eng.consolepec.xmlplugin.jaxb.Modulistica.Allegati;
import it.eng.consolepec.xmlplugin.jaxb.Pratica;
import it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata;
import it.eng.consolepec.xmlplugin.jaxb.Protocollazione.AllegatiProtocollati;
import it.eng.consolepec.xmlplugin.jaxb.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.jaxb.ValoreModulo.Tabella;
import it.eng.consolepec.xmlplugin.jaxb.ValoreModulo.Tabella.Riga;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.NodoModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.NodoModulistica.TipoNodoModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Sezione;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Stato;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.TabellaModulo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ValoreModulo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ValoreModulo.TipoValoreModulo;
import it.eng.consolepec.xmlplugin.tasks.gestionemodulistica.GestionePraticaModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPraticaModulisticaTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class XMLPraticaModulistica extends XMLPratica<DatiModulistica> implements PraticaModulistica {

	private DatiModulistica dati;
	protected Logger logger = LoggerFactory.getLogger(XMLPraticaModulistica.class);

	public XMLPraticaModulistica() {
		// richiesto da reflection
	}

	@Override
	public DatiPraticaModulisticaTaskAdapter getDatiPraticaTaskAdapter() {
		DatiModulistica t = getDati();
		return new DatiPraticaModulisticaTaskAdapter(t);
	}

	@Override
	public String getSubFolderPath() {
		return "MODULISTICA";
	}

	@Override
	public DatiModulistica getDati() {
		return dati;
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {
		logger.debug("Serializzazione pratica modulistica {}", getAlfrescoPath());
		try {
			/* serializzazione dati generici */
			super.serializeDati(jaxb);
			jaxb.setTipo(dati.getTipo().getNomeTipologia());
			jaxb.setStato(getDati().getStato().name());

			Modulistica modulistica = new Modulistica();
			modulistica.setNome(dati.getNome());
			jaxb.setModulistica(modulistica);

			for (NodoModulistica nodo : dati.getValori()) {
				jaxb.getModulistica().getValori().add(createXmlNodoModulistica(nodo));
			}

			modulistica.setAllegati(new Allegati());
			/* informazioni sugli allegati */
			for (Allegato allg : dati.getAllegati()) {
				if (!isAllegatoProtocollato(allg)) {
					jaxb.getModulistica().getAllegati().getDocumento().add(XmlPluginUtil.getDocumentoFromAllegato(allg));
				}
			}

			List<ProtocollazioneCapofila> protocollazione = modulistica.getProtocollazione();
			List<it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila> protocollazioniCapofila = dati.getProtocollazioniCapofila();
			for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila protCapofila : protocollazioniCapofila) {
				ProtocollazioneCapofila protocollazioneCapofila = serializeProtocollazioneCapofila(protCapofila);
				protocollazione.add(protocollazioneCapofila);
			}

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione");
		}

	}

	private it.eng.consolepec.xmlplugin.jaxb.ValoreModulo createXmlNodoModulistica(NodoModulistica nodo) {
		if (nodo.getTipoNodo() == TipoNodoModulistica.VALORE_MODULO) {
			return createXmlValoreModulo((ValoreModulo) nodo);
		}
		if (nodo.getTipoNodo() == TipoNodoModulistica.SEZIONE) {
			Sezione sezione = (Sezione) nodo;
			it.eng.consolepec.xmlplugin.jaxb.ValoreModulo s = new it.eng.consolepec.xmlplugin.jaxb.ValoreModulo();
			s.setTipoNodo(it.eng.consolepec.xmlplugin.jaxb.TipoNodoModulistica.SEZIONE);
			s.setTitolo(sezione.getTitolo());
			for (NodoModulistica nodoSezione : sezione.getNodi()) {
				s.getValoriSezione().add(createXmlNodoModulistica(nodoSezione));
			}
			return s;
		}
		if (nodo.getTipoNodo() == null) {
			return createXmlValoreModulo((ValoreModulo) nodo);
		}
		return null;
	}

	private it.eng.consolepec.xmlplugin.jaxb.ValoreModulo createXmlValoreModulo(ValoreModulo valore) {
		it.eng.consolepec.xmlplugin.jaxb.ValoreModulo xmlValore = new it.eng.consolepec.xmlplugin.jaxb.ValoreModulo();
		xmlValore.setNome(valore.getNome());
		xmlValore.setValore(valore.getValore());
		xmlValore.setTipo(valore.getTipo().name());
		xmlValore.setEtichetta(valore.getEtichetta());
		xmlValore.setDescrizione(valore.getDescrizione());
		xmlValore.setVisibile(valore.isVisibile());
		xmlValore.setTipoNodo(it.eng.consolepec.xmlplugin.jaxb.TipoNodoModulistica.VALORE_MODULO);

		Tabella tabella = null;
		if (valore.getTabella() != null) {
			tabella = new Tabella();
			for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.TabellaModulo.Riga riga : valore.getTabella().getRighe()) {
				Riga jaxbRiga = new Riga();
				for (ValoreModulo colonna : riga.getColonne()) {
					jaxbRiga.getColonne().add(createXmlValoreModulo(colonna));
				}
				tabella.getRiga().add(jaxbRiga);
			}
		}
		xmlValore.setTabella(tabella);

		return xmlValore;
	}

	private boolean isAllegatoProtocollato(Allegato allg) {
		for (DatiModulistica.ProtocollazioneCapofila protocollazioneCapofila : dati.getProtocollazioniCapofila()) {
			for (Allegato allegato : protocollazioneCapofila.getAllegatiProtocollati()) {
				if (allegato.equals(allg))
					return true;
			}
			for (DatiModulistica.Protocollazione protocollazione : protocollazioneCapofila.getProtocollazioniCollegate()) {
				for (Allegato allegato : protocollazione.getAllegatiProtocollati()) {
					if (allegato.equals(allg))
						return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void serializePraticheCollegate(Pratica praticaJaxb, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata> praticheCollegate) {
		for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.PraticaCollegata p : praticheCollegate) {
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

	private boolean isPraticaCollegataProtocollata(it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.PraticaCollegata p) {
		for (DatiModulistica.ProtocollazioneCapofila protocollazioneCapofila : dati.getProtocollazioniCapofila()) {
			for (DatiModulistica.PraticaCollegata pratica : protocollazioneCapofila.getPraticheCollegateProtocollate()) {
				if (pratica.equals(p))
					return true;
			}
			for (Protocollazione protocollazioneCollegata : protocollazioneCapofila.getProtocollazioniCollegate()) {
				for (DatiModulistica.PraticaCollegata pratica : protocollazioneCollegata.getPraticheCollegateProtocollate()) {
					if (pratica.equals(p))
						return true;
				}
			}
		}
		return false;
	}

	private ProtocollazioneCapofila serializeProtocollazioneCapofila(it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila protCapofila) {
		if (protCapofila == null)
			return null;

		ProtocollazioneCapofila p = new ProtocollazioneCapofila();
		p.setAnnoPG(new BigInteger(protCapofila.getAnnoPG().toString()));

		if (protCapofila.getDataprotocollazione() != null) {
			GregorianCalendar dataPRot = new GregorianCalendar();
			dataPRot.setTime(protCapofila.getDataprotocollazione());
			try {
				p.setDataprotocollazione(DatatypeFactory.newInstance().newXMLGregorianCalendar(dataPRot));
			} catch (DatatypeConfigurationException e) {
				logger.error("DatatypeConfigurationException", e);
			}
		}
		p.setNumeroPG(protCapofila.getNumeroPG());
		p.setNumeropraticamodulistica(protCapofila.getNumeroPraticaModulistica());
		p.setOggetto(protCapofila.getOggetto());
		p.setProvenienza(protCapofila.getProvenienza());
		p.setRubrica(protCapofila.getRubrica());
		p.setSezione(protCapofila.getSezione());
		p.setTipologiadocumento(protCapofila.getTipologiadocumento());
		p.setTitolo(protCapofila.getTitolo());
		p.setUtenteprotocollazione(protCapofila.getUtenteprotocollazione());
		p.setTipoProtocollazione(XmlPluginUtil.serializeTipoProtocollazione(protCapofila.getTipoProtocollazione()));
		p.setFromBA01(protCapofila.isFromBa01());

		ProtocollazioneCapofila.AllegatiProtocollati alleagtiProtocollati = new ProtocollazioneCapofila.AllegatiProtocollati();
		List<Documento> listaAllegatiProtocollati = alleagtiProtocollati.getAllegatoProtocollato();

		List<Allegato> allegatiProtocollati = protCapofila.getAllegatiProtocollati();
		for (Allegato allegato : allegatiProtocollati) {
			Documento documento = XmlPluginUtil.getDocumentoFromAllegato(allegato);
			/* caricamento dei gruppi abilitati a vedere la pratica */
			documento.setGruppivisibilita(new Gruppivisibilita());
			for (GruppoVisibilita gruppo : allegato.getGruppiVisibilita()) {
				documento.getGruppivisibilita().getGruppovisibilita().add(gruppo.getNomeGruppo());
			}

			listaAllegatiProtocollati.add(documento);
		}
		p.setAllegatiProtocollati(alleagtiProtocollati);

		List<it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.PraticaCollegata> praticheCollegateProtocollate = protCapofila.getPraticheCollegateProtocollate();

		ProtocollazioneCapofila.PraticheCollegateProtocollate praticheCollegate = new ProtocollazioneCapofila.PraticheCollegateProtocollate();
		List<it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata> listaPraticheCollegate = praticheCollegate.getPraticaCollegata();
		for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.PraticaCollegata praticaCollegataProtocollata : praticheCollegateProtocollate) {
			PraticaCollegata praticaCollegata = new PraticaCollegata();
			praticaCollegata.setAlfrescoPath(praticaCollegataProtocollata.getAlfrescoPath());
			praticaCollegata.setTipo(praticaCollegataProtocollata.getTipo());
			praticaCollegata.setDataCaricamento(DateUtils.dateToXMLGrCal(praticaCollegataProtocollata.getDataCaricamento()));

			listaPraticheCollegate.add(praticaCollegata);
		}
		p.setPraticheCollegateProtocollate(praticheCollegate);

		List<Protocollazione> protocollazioniCollegate = protCapofila.getProtocollazioniCollegate();
		try {
			serializeProtocollazione(protocollazioniCollegate, p.getProtocollazioniCollegate());
		} catch (DatatypeConfigurationException e) {
			logger.error("Errore serializzazione protocollazione ", e);
		}

		return p;

	}

	private static void serializeProtocollazione(List<Protocollazione> protocollazioniCollegate,
			List<it.eng.consolepec.xmlplugin.jaxb.Protocollazione> protocollazioniCollegateJaxb) throws DatatypeConfigurationException {
		if (protocollazioniCollegate != null)
			for (Protocollazione prot : protocollazioniCollegate) {
				it.eng.consolepec.xmlplugin.jaxb.Protocollazione protocollazione = new it.eng.consolepec.xmlplugin.jaxb.Protocollazione();
				AllegatiProtocollati allegatiProtocollati = new AllegatiProtocollati();
				List<Documento> listaAllegatiProtocollati = allegatiProtocollati.getAllegatoProtocollato();
				for (Allegato allegato : prot.getAllegatiProtocollati()) {
					Documento documento = XmlPluginUtil.getDocumentoFromAllegato(allegato);
					/* caricamento dei gruppi abilitati a vedere la pratica */
					documento.setGruppivisibilita(new Gruppivisibilita());
					for (GruppoVisibilita gruppo : allegato.getGruppiVisibilita()) {
						documento.getGruppivisibilita().getGruppovisibilita().add(gruppo.getNomeGruppo());
					}

					listaAllegatiProtocollati.add(documento);
				}
				List<it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.PraticaCollegata> praticheCollegateProtocollate = prot.getPraticheCollegateProtocollate();
				it.eng.consolepec.xmlplugin.jaxb.Protocollazione.PraticheCollegateProtocollate praticheCollegateProtocollate2 = new it.eng.consolepec.xmlplugin.jaxb.Protocollazione.PraticheCollegateProtocollate();
				List<it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata> listaPraticheCollegate = praticheCollegateProtocollate2.getPraticaCollegata();
				for (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.PraticaCollegata praticaCollegataProtocollata : praticheCollegateProtocollate) {
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
				protocollazione.setNumeropraticamodulistica(prot.getNumeroPratica());
				protocollazione.setOggetto(prot.getOggetto());
				protocollazione.setPraticheCollegateProtocollate(praticheCollegateProtocollate2);
				protocollazione.setProvenienza(prot.getProvenienza());
				protocollazione.setRubrica(prot.getRubrica());
				protocollazione.setSezione(prot.getSezione());
				protocollazione.setTipologiadocumento(prot.getTipologiadocumento());
				protocollazione.setTitolo(prot.getTitolo());
				protocollazione.setUtenteprotocollazione(prot.getUtenteprotocollazione());
				protocollazione.setNumeroRegistro(prot.getNumeroRegistro());
				protocollazione.setTipoProtocollazione(XmlPluginUtil.serializeTipoProtocollazione(prot.getTipoProtocollazione()));
				if (prot.getAnnoRegistro() != null)
					protocollazione.setAnnoRegistro(new BigInteger(prot.getAnnoRegistro().toString()));

				protocollazioniCollegateJaxb.add(protocollazione);
			}
	}

	@Override
	protected void loadDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {
		DatiModulistica.Builder builder = new DatiModulistica.Builder();
		builder.setConsoleFileName(jaxb.getConsoleFileName());
		if (jaxb.getDataCreazione() != null)
			builder.setDataCreazione(jaxb.getDataCreazione().toGregorianCalendar().getTime());
		builder.setFolderPath(jaxb.getFolderPath());
		builder.setProvenienza(jaxb.getProvenienza());
		builder.setTitolo(jaxb.getTitolo());
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
		dati.setStato(Stato.valueOf(jaxb.getStato()));
		dati.setLetto(jaxb.isLetto());

		dati.setNome(jaxb.getModulistica().getNome());

		if (jaxb.getModulistica().getValori() != null) {

			for (it.eng.consolepec.xmlplugin.jaxb.ValoreModulo n : jaxb.getModulistica().getValori()) {
				dati.getValori().add(createNodoModulistica(n));
			}
		}
		/* informazioni sugli allegati */
		Allegati allg = jaxb.getModulistica().getAllegati();
		Set<Allegato> allegati = new LinkedHashSet<DatiPratica.Allegato>();
		if (allg != null)
			for (Documento doc : allg.getDocumento()) {
				Allegato allegato = XmlPluginUtil.getAllegatoFromDocumento(doc, dati);
				allegati.add(allegato);
			}

		List<Documento> allegatiProtocollati = getAllegatiProtocollati(jaxb.getModulistica().getProtocollazione());
		if (allegatiProtocollati != null)
			for (Documento doc : allegatiProtocollati) {
				Allegato allegato = XmlPluginUtil.getAllegatoFromDocumento(doc, dati);
				allegati.add(allegato);
			}

		dati.getAllegati().addAll(new ArrayList<Allegato>(allegati));

		Set<DatiPratica.PraticaCollegata> pCollegate = new LinkedHashSet<DatiPratica.PraticaCollegata>();

		for (PraticaCollegata pc : jaxb.getPraticaCollegata()) {
			it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata pc2 = dati.new PraticaCollegata(pc.getAlfrescoPath(), pc.getTipo(), DateUtils.xmlGrCalToDate(pc.getDataCaricamento()));
			pc2.setAlfrescoPath(pc.getAlfrescoPath());
			pc2.setTipo(pc.getTipo());
			pc2.setDataCaricamento(DateUtils.xmlGrCalToDate(pc.getDataCaricamento()));
			logger.debug("Deserializzazione info pratica collegata: {}", pc2);
			pCollegate.add(pc2);
		}

		for (ProtocollazioneCapofila protocollazioneCapofila : jaxb.getModulistica().getProtocollazione()) {
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

		ArrayList<DatiPratica.PraticaCollegata> praticheCollegate = new ArrayList<DatiPratica.PraticaCollegata>(pCollegate);
		for (DatiFascicolo.PraticaCollegata pc : praticheCollegate)
			addPraticaCollegata(pc);

		List<it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila> listProtocollazioneCapofila = new ArrayList<it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila>();

		for (ProtocollazioneCapofila protocollazioneCapofila : jaxb.getModulistica().getProtocollazione())
			listProtocollazioneCapofila.add(loadProtocollazioneCapofila(protocollazioneCapofila));
		dati.setProtocollazioniCapofila(listProtocollazioneCapofila);

	}

	private NodoModulistica createNodoModulistica(it.eng.consolepec.xmlplugin.jaxb.ValoreModulo n) {
		if (n.getTipoNodo() == it.eng.consolepec.xmlplugin.jaxb.TipoNodoModulistica.VALORE_MODULO) {
			return createValoreModulo(n);
		}
		if (n.getTipoNodo() == it.eng.consolepec.xmlplugin.jaxb.TipoNodoModulistica.SEZIONE) {
			Sezione s = new Sezione();
			s.setTitolo(n.getTitolo());

			for (it.eng.consolepec.xmlplugin.jaxb.ValoreModulo nodoSezione : n.getValoriSezione()) {
				s.getNodi().add(createNodoModulistica(nodoSezione));
			}
			return s;
		}
		if (n.getTipoNodo() == null) {
			return createValoreModulo(n);
		}
		return null;
	}

	private ValoreModulo createValoreModulo(it.eng.consolepec.xmlplugin.jaxb.ValoreModulo v) {
		ValoreModulo valore = new ValoreModulo();
		valore.setNome(v.getNome());
		valore.setValore(v.getValore());
		valore.setTipo(TipoValoreModulo.valueOf(v.getTipo()));
		valore.setEtichetta(v.getEtichetta());
		valore.setDescrizione(v.getDescrizione());
		valore.setVisibile(v.isVisibile());

		TabellaModulo tabella = null;
		if (v.getTabella() != null) {
			tabella = new TabellaModulo();
			for (Riga jaxbRiga : v.getTabella().getRiga()) {
				it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.TabellaModulo.Riga riga = new it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.TabellaModulo.Riga();
				for (it.eng.consolepec.xmlplugin.jaxb.ValoreModulo colonna : jaxbRiga.getColonne()) {
					riga.getColonne().add(createValoreModulo(colonna));
				}
				tabella.getRighe().add(riga);
			}

		}
		valore.setTabella(tabella);
		return valore;
	}

	private static List<Documento> getAllegatiProtocollati(List<it.eng.consolepec.xmlplugin.jaxb.ProtocollazioneCapofila> protocollazione) {
		List<Documento> alleagatoProtocollato = new ArrayList<Documento>();
		for (ProtocollazioneCapofila protocollazioneCapofila : protocollazione) {
			alleagatoProtocollato.addAll(protocollazioneCapofila.getAllegatiProtocollati().getAllegatoProtocollato());
			for (it.eng.consolepec.xmlplugin.jaxb.Protocollazione prot : protocollazioneCapofila.getProtocollazioniCollegate()) {
				for (Documento documento : prot.getAllegatiProtocollati().getAllegatoProtocollato())
					alleagatoProtocollato.add(documento);
			}
		}
		return alleagatoProtocollato;
	}

	private it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila loadProtocollazioneCapofila(ProtocollazioneCapofila protocollazioneCapofilaJaxb) {

		List<Documento> alleagatiProtocollati = protocollazioneCapofilaJaxb.getAllegatiProtocollati().getAllegatoProtocollato();
		List<Allegato> allegati = new ArrayList<DatiPratica.Allegato>();
		for (Documento documento : alleagatiProtocollati) {
			Allegato allegato = XmlPluginUtil.getAllegatoFromDocumento(documento, dati);
			allegati.add(allegato);
		}
		List<PraticaCollegata> praticheCollegate = protocollazioneCapofilaJaxb.getPraticheCollegateProtocollate().getPraticaCollegata();
		List<DatiModulistica.PraticaCollegata> praticheCollegateProtocollate = new ArrayList<DatiModulistica.PraticaCollegata>();
		for (PraticaCollegata pratica : praticheCollegate) {
			praticheCollegateProtocollate.add(dati.new PraticaCollegata(pratica.getAlfrescoPath(), pratica.getTipo(), DateUtils.xmlGrCalToDate(pratica.getDataCaricamento())));
		}
		List<Protocollazione> protocollazioniCollegate = loadProtocollazioniCollegate(protocollazioneCapofilaJaxb.getProtocollazioniCollegate());

		DatiModulistica.ProtocollazioneCapofilaBuilder builderCapofila = new DatiModulistica.ProtocollazioneCapofilaBuilder(dati);
		builderCapofila.setAllegatiProtocollati(allegati);
		if (protocollazioneCapofilaJaxb.getAnnoPG() != null)
			builderCapofila.setAnnoPG(protocollazioneCapofilaJaxb.getAnnoPG().intValue());
		if (protocollazioneCapofilaJaxb.getDataprotocollazione() != null)
			builderCapofila.setDataprotocollazione(protocollazioneCapofilaJaxb.getDataprotocollazione().toGregorianCalendar().getTime());
		builderCapofila.setNumeroPraticaModulistica(protocollazioneCapofilaJaxb.getNumeropraticamodulistica());
		builderCapofila.setNumeroPG(protocollazioneCapofilaJaxb.getNumeroPG());
		builderCapofila.setOggetto(protocollazioneCapofilaJaxb.getOggetto());
		builderCapofila.setPraticheCollegateProtocollate(praticheCollegateProtocollate);
		builderCapofila.setProtocollazioniCollegate(protocollazioniCollegate);
		builderCapofila.setProvenienza(protocollazioneCapofilaJaxb.getProvenienza());
		builderCapofila.setRubrica(protocollazioneCapofilaJaxb.getRubrica());
		builderCapofila.setSezione(protocollazioneCapofilaJaxb.getSezione());
		builderCapofila.setTipologiadocumento(protocollazioneCapofilaJaxb.getTipologiadocumento());
		builderCapofila.setTitiolo(protocollazioneCapofilaJaxb.getTitolo());
		builderCapofila.setUtenteprotocollazione(protocollazioneCapofilaJaxb.getUtenteprotocollazione());
		builderCapofila.setNumeroRegistro(protocollazioneCapofilaJaxb.getNumeroRegistro());
		builderCapofila.setTipoProtocollazione(XmlPluginUtil.loadTipoProtocollazione(protocollazioneCapofilaJaxb.getTipoProtocollazione()));
		builderCapofila.setFromBa01(protocollazioneCapofilaJaxb.isFromBA01());
		if (protocollazioneCapofilaJaxb.getAnnoRegistro() != null)
			builderCapofila.setAnnoPG(protocollazioneCapofilaJaxb.getAnnoRegistro().intValue());
		return builderCapofila.construct();
	}

	private List<Protocollazione> loadProtocollazioniCollegate(List<it.eng.consolepec.xmlplugin.jaxb.Protocollazione> protocollazioniCollegate) {
		List<Protocollazione> loadProtocollazioniCollegate = new ArrayList<DatiModulistica.Protocollazione>();
		for (it.eng.consolepec.xmlplugin.jaxb.Protocollazione prot : protocollazioniCollegate) {
			List<Documento> alleagatiProtocollati = prot.getAllegatiProtocollati().getAllegatoProtocollato();
			List<DatiPratica.Allegato> allegati = new ArrayList<DatiPratica.Allegato>();
			for (Documento documento : alleagatiProtocollati) {

				Allegato allegato = XmlPluginUtil.getAllegatoFromDocumento(documento, dati);
				allegati.add(allegato);
			}

			List<PraticaCollegata> praticheCollegate = prot.getPraticheCollegateProtocollate().getPraticaCollegata();
			List<DatiModulistica.PraticaCollegata> praticheCollegateProtocollate = new ArrayList<DatiModulistica.PraticaCollegata>();
			for (PraticaCollegata pratica : praticheCollegate) {
				praticheCollegateProtocollate.add(dati.new PraticaCollegata(pratica.getAlfrescoPath(), pratica.getTipo(), DateUtils.xmlGrCalToDate(pratica.getDataCaricamento())));
			}

			DatiModulistica.ProtocollazioneBuilder builder = new DatiModulistica.ProtocollazioneBuilder(dati);

			builder.setAllegatiProtocollati(allegati);
			builder.setAnnoPG(prot.getAnnoPG().intValue());
			if (prot.getDataprotocollazione() != null)
				builder.setDataprotocollazione(prot.getDataprotocollazione().toGregorianCalendar().getTime());
			builder.setNumeroPraticaModulistica(prot.getNumeropraticamodulistica());
			builder.setNumeroPG(prot.getNumeroPG());
			builder.setOggetto(prot.getOggetto());
			builder.setPraticheCollegateProtocollate(praticheCollegateProtocollate);
			builder.setProvenienza(prot.getProvenienza());
			builder.setRubrica(prot.getRubrica());
			builder.setSezione(prot.getSezione());
			builder.setTipologiadocumento(prot.getTipologiadocumento());
			builder.setTitiolo(prot.getTitolo());
			builder.setUtenteprotocollazione(prot.getUtenteprotocollazione());
			builder.setNumeroRegistro(prot.getNumeroRegistro());
			builder.setTipoProtocollazione(XmlPluginUtil.loadTipoProtocollazione(prot.getTipoProtocollazione()));
			if (prot.getAnnoRegistro() != null)
				builder.setAnnoPG(prot.getAnnoRegistro().intValue());
			loadProtocollazioniCollegate.add(builder.construct());
		}
		return loadProtocollazioniCollegate;
	}

	@Override
	protected void initPratica(DatiModulistica dati) throws PraticaException {
		this.dati = dati;
	}

	@Override
	public Map<MetadatiPratica, Object> getMetadata() {
		Map<MetadatiPratica, Object> map = new HashMap<MetadatiPratica, Object>();

		map.put(MetadatiPratica.pDataCreazione, dati.getDataCreazione());
		map.put(MetadatiPratica.pIdDocumentale, getDati().getIdDocumentale());
		if (getDati().getStato() != null)
			map.put(MetadatiPratica.pStato, getDati().getStato().toString());
		map.put(MetadatiPratica.pTipoPratica, getDati().getTipo().getNomeTipologia());
		map.put(MetadatiPratica.pTitolo, dati.getTitolo());
		map.put(MetadatiPratica.pLetto, dati.isLetto());
		map.put(MetadatiPratica.pData, dati.getDataCreazione());
		map.put(MetadatiPratica.pInoltratoDa, getInoltratoDa());
		map.put(MetadatiPratica.pProvenienza, getProvenienza());

		if (dati.getUtenteCreazione() != null)
			map.put(MetadatiPratica.pUtenteCreazione, dati.getUtenteCreazione());

		it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila capofila = getPrimoCapofila();
		if (capofila != null) {
			if (capofila.isFromBa01()) {
				Collections.sort(capofila.getProtocollazioniCollegate());
				it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Protocollazione protocollazione = capofila.getProtocollazioniCollegate().get(0);
				map.put(MetadatiPratica.pAnnoPG, protocollazione.getAnnoPG());
				map.put(MetadatiPratica.pNumeroPG, protocollazione.getNumeroPG());
			} else {
				map.put(MetadatiPratica.pAnnoPG, capofila.getAnnoPG());
				map.put(MetadatiPratica.pNumeroPG, capofila.getNumeroPG());
			}
		}

		if (dati.getGruppiVisibilita().size() != 0)
			map.put(MetadatiPratica.pVisibileA, XmlPluginUtil.getListaVisibilita(dati.getGruppiVisibilita()));

		map.put(MetadatiPratica.pIncaricoA, GenericsUtil.sanitizeNull(dati.getInCaricoA() != null ? dati.getInCaricoA().getUsername() : null));

		map.put(MetadatiPratica.pElencoProtocollazioni, XmlPluginUtil.protocollazioniToList(dati.getProtocollazioniCapofila()));

		if (dati.getGruppiVisibilita().size() != 0)
			map.put(MetadatiPratica.pVisibileA, XmlPluginUtil.getListaVisibilita(dati.getGruppiVisibilita()));

		map.put(MetadatiPratica.pNomeModulo, dati.getNome());
		if (dati.getValori().isEmpty() == false)
			map.put(MetadatiPratica.pValoriModulo, XmlPluginUtil.valoriModuloToList(dati.getValori()));

		if (getDati().getAssegnatarioCorrente() != null) {
			map.put(MetadatiPratica.pAssegnatoA, getDati().getAssegnatarioCorrente());

		} else {
			for (Task<?> task : getTasks()) {
				if (task instanceof GestionePraticaModulisticaTask && task.isAttivo()) {
					GestionePraticaModulisticaTask taskGestione = (GestionePraticaModulisticaTask) task;
					map.put(MetadatiPratica.pAssegnatoA, taskGestione.getDati().getAssegnatario().getNome());
					break;
				} else if (task instanceof RiattivaPraticaModulisticaTask && task.isAttivo()) {
					RiattivaPraticaModulisticaTask taskRiattiva = (RiattivaPraticaModulisticaTask) task;
					map.put(MetadatiPratica.pAssegnatoA, taskRiattiva.getDati().getAssegnatario().getNome());
					break;
				}
			}
		}

		return map;
	}

	private it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila getPrimoCapofila() {
		List<it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ProtocollazioneCapofila> protocollazioniCapofila = getDati().getProtocollazioniCapofila();
		if (protocollazioniCapofila.size() == 0)
			return null;
		Collections.sort(protocollazioniCapofila);
		return protocollazioniCapofila.get(0);
	}

	@Override
	public boolean isAttiva() {
		return true;
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
		if (obj instanceof XMLPraticaModulistica) {
			XMLPraticaModulistica other = (XMLPraticaModulistica) obj;
			return other.getAlfrescoPath().equalsIgnoreCase(getAlfrescoPath());
		}

		return false;
	}

	@Override
	public String toString() {
		return "PraticaModulistica: " + dati.toString();
	}

	@Override
	public boolean isRiattivabile() {
		Set<Task<?>> tasks2 = this.getTasks();
		for (Task<?> t : tasks2)
			if (t.isAttivo() && t instanceof RiattivaPraticaModulisticaTask)
				return true;
		return false;
	}

	@Override
	public boolean isProtocollaAbilitato() {
		return true;
	}

	@Override
	protected Date getDate() {
		return dati.getDataCreazione();
	}

	@Override
	protected String getProvenienza() {
		return dati.getProvenienza();
	}

}
