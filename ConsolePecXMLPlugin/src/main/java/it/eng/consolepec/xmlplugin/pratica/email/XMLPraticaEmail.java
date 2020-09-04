package it.eng.consolepec.xmlplugin.pratica.email;

import java.math.BigInteger;
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

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.EventoIterPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.DatiPraticaTaskAdapter;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.XMLPratica;
import it.eng.consolepec.xmlplugin.jaxb.Documento;
import it.eng.consolepec.xmlplugin.jaxb.EventoIter;
import it.eng.consolepec.xmlplugin.jaxb.PEC;
import it.eng.consolepec.xmlplugin.jaxb.PEC.Allegati;
import it.eng.consolepec.xmlplugin.jaxb.PEC.DestinatariCC;
import it.eng.consolepec.xmlplugin.jaxb.PEC.EventiIter;
import it.eng.consolepec.xmlplugin.jaxb.Pratica;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.DestinatarioInteroperabile;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Interoperabile;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.PG;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.ProtocollazionePEC;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.ProtocollazionePECBuilder;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.TipoEmail;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.TipologiaSegnatura;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECInTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECInTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECOutTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaPECTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public abstract class XMLPraticaEmail extends XMLPratica<DatiEmail> implements PraticaEmail {

	private DatiEmail dati;
	protected Logger logger = LoggerFactory.getLogger(XMLPraticaEmail.class);

	public XMLPraticaEmail() {
		// richiesto da reflection
	}

	@Override
	public DatiPraticaTaskAdapter getDatiPraticaTaskAdapter() {
		DatiEmail t = getDati();
		return new DatiPraticaEmailTaskAdapter(t);
	}

	@Override
	public String getSubFolderPath() {
		return "MESSAGGIO";
	}

	@Override
	public DatiEmail getDati() {
		return dati;
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {
		GregorianCalendar cal = new GregorianCalendar();
		try {
			/* serializzazione dati generici */
			super.serializeDati(jaxb);

			if (dati.getProvenienza() != null)
				jaxb.setProvenienza(dati.getProvenienza().toLowerCase());

			/* serializzazione dati PEC */
			jaxb.setStato(dati.getStato().name());
			jaxb.setPEC(new PEC());
			jaxb.getPEC().setMessageIDReinoltro(dati.getMessageIDReinoltro());
			jaxb.getPEC().setMessageID(dati.getMessageID());
			jaxb.getPEC().setIdEmailServer(dati.getIdEmailServer());
			if (dati.getDataRicezione() != null) {
				cal.setTime(dati.getDataRicezione());
				jaxb.getPEC().setDataRicezione(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
			}
			jaxb.getPEC().setMittente(dati.getMittente());
			jaxb.getPEC().setOggetto(dati.getOggetto());
			jaxb.getPEC().setBody(dati.getBody());
			jaxb.getPEC().setFirma(dati.getFirma());

			jaxb.getPEC().setNote(dati.getNote());
			if (dati.getDataInvio() != null) {
				cal.setTime(dati.getDataInvio());
				jaxb.getPEC().setDataInvio(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
			}

			jaxb.getPEC().setReplyTo(dati.getReplyTo());

			/* informazioni sui destinatari */
			jaxb.getPEC().setDestinatari(new PEC.Destinatari());
			/* impostazione del destinatario principale */
			if (dati.getDestinatarioPrincipale() != null) {
				it.eng.consolepec.xmlplugin.jaxb.Destinatario destinatarioPrincipale = new it.eng.consolepec.xmlplugin.jaxb.Destinatario();
				destinatarioPrincipale.setConsegna(dati.getDestinatarioPrincipale().isConsegna());
				destinatarioPrincipale.setAccettazione(dati.getDestinatarioPrincipale().isAccettazione());
				destinatarioPrincipale.setTipo(dati.getDestinatarioPrincipale().getTipo().name());
				if (dati.getDestinatarioPrincipale().getDestinatario() != null)
					destinatarioPrincipale.setEmail(dati.getDestinatarioPrincipale().getDestinatario().toLowerCase());
				destinatarioPrincipale.setErrore(dati.getDestinatarioPrincipale().getErrore());
				if (dati.getDestinatarioPrincipale().getStatoDestinatario() != null)
					destinatarioPrincipale.setStatoDest(dati.getDestinatarioPrincipale().getStatoDestinatario().name());
				jaxb.getPEC().getDestinatari().setDestinatarioPrincipale(destinatarioPrincipale);
			}
			jaxb.getPEC().setHasRicevutaAccettazione(dati.isRicevutaAccettazione());
			jaxb.getPEC().setHasRicevutaConsegna(dati.isRicevutaConsegna());
			// serializzazione delle ricevute
			serializeRicevute(jaxb, dati.getRicevute());

			for (Destinatario destinatario : dati.getDestinatari()) {
				it.eng.consolepec.xmlplugin.jaxb.Destinatario dest = new it.eng.consolepec.xmlplugin.jaxb.Destinatario();
				dest.setConsegna(destinatario.isConsegna());
				dest.setAccettazione(destinatario.isAccettazione());
				dest.setTipo(destinatario.getTipo().name());
				if (destinatario.getDestinatario() != null)
					dest.setEmail(destinatario.getDestinatario().toLowerCase());
				dest.setErrore(destinatario.getErrore());
				if (destinatario.getStatoDestinatario() != null)
					dest.setStatoDest(destinatario.getStatoDestinatario().name());
				jaxb.getPEC().getDestinatari().getDestinatario().add(dest);
			}

			jaxb.getPEC().setDestinatariInoltro(new PEC.DestinatariInoltro());
			for (String destinatario : dati.getDestinatariInoltro()) {
				if (destinatario != null)
					jaxb.getPEC().getDestinatariInoltro().getDestinatarioInoltro().add(destinatario);
			}
			jaxb.getPEC().setProgressivoInoltro(dati.getProgressivoInoltro());
			jaxb.getPEC().setNotificaRifiutoInoltro(dati.isNotificaRifiutoInoltro());

			jaxb.getPEC().setTipoEmail(dati.getTipoEmail() == null ? null : dati.getTipoEmail().name());
			jaxb.getPEC().setAllegati(new Allegati());
			/* informazioni sugli allegati */
			for (Allegato allg : dati.getAllegati()) {
				jaxb.getPEC().getAllegati().getDocumento().add(XmlPluginUtil.getDocumentoFromAllegato(allg));
			}
			/* informazioni sui CC */
			jaxb.getPEC().setDestinatariCC(new DestinatariCC());
			for (Destinatario cc : dati.getDestinatariCC()) {
				it.eng.consolepec.xmlplugin.jaxb.Destinatario d = new it.eng.consolepec.xmlplugin.jaxb.Destinatario();
				d.setAccettazione(cc.isAccettazione());
				d.setConsegna(cc.isConsegna());
				if (cc.getDestinatario() != null)
					d.setEmail(cc.getDestinatario().toLowerCase());
				d.setTipo(cc.getTipo().name());
				d.setErrore(cc.getErrore());
				if (cc.getStatoDestinatario() != null)
					d.setStatoDest(cc.getStatoDestinatario().name());
				jaxb.getPEC().getDestinatariCC().getDestinatarioCC().add(d);
			}

			ProtocollazionePEC protocollazionePec = dati.getProtocollazionePec();
			if (protocollazionePec != null) {

				it.eng.consolepec.xmlplugin.jaxb.ProtocollazionePEC protocollazione = new it.eng.consolepec.xmlplugin.jaxb.ProtocollazionePEC();

				protocollazione.setAnnoPG(new BigInteger(String.valueOf(protocollazionePec.getAnnoPG())));
				if (protocollazionePec.getAnnoRegistro() != null)
					protocollazione.setAnnoRegistro(new BigInteger(String.valueOf(protocollazionePec.getAnnoRegistro())));
				protocollazione.setNumerofascicolo(protocollazionePec.getNumeroFascicolo());
				protocollazione.setNumeroPG(protocollazionePec.getNumeroPG());
				protocollazione.setNumeroRegistro(protocollazionePec.getNumeroRegistro());
				protocollazione.setOggetto(protocollazionePec.getOggetto());
				protocollazione.setProvenienza(protocollazionePec.getProvenienza());
				protocollazione.setRubrica(protocollazionePec.getRubrica());
				protocollazione.setSezione(protocollazionePec.getSezione());
				protocollazione.setTipologiadocumento(protocollazionePec.getTipologiadocumento());
				protocollazione.setTitolo(protocollazionePec.getTitolo());
				protocollazione.setUtenteprotocollazione(protocollazionePec.getUtenteprotocollazione());
				protocollazione.setTipoProtocollazione(XmlPluginUtil.serializeTipoProtocollazione(protocollazionePec.getTipoProtocollazione()));

				PG capofila = protocollazionePec.getCapofila();
				if (capofila != null) {
					it.eng.consolepec.xmlplugin.jaxb.PG capofilaJaxb = new it.eng.consolepec.xmlplugin.jaxb.PG();
					capofilaJaxb.setAnnoPG(new BigInteger(String.valueOf(capofila.getAnnoPG())));
					capofilaJaxb.setNumeroPG(capofila.getNumeroPG());
					protocollazione.setCapofila(capofilaJaxb);
				}

				if (protocollazionePec.getDataprotocollazione() != null) {
					GregorianCalendar dataPRot = new GregorianCalendar();
					dataPRot.setTime(protocollazionePec.getDataprotocollazione());
					protocollazione.setDataprotocollazione(DatatypeFactory.newInstance().newXMLGregorianCalendar(dataPRot));
				}
				jaxb.getPEC().setProtocollazione(protocollazione);
			}

			if (dati.getInteroperabile() != null) {

				it.eng.consolepec.xmlplugin.jaxb.Interoperabile interoperabile = new it.eng.consolepec.xmlplugin.jaxb.Interoperabile();
				interoperabile.setStato(dati.getInteroperabile().getStato());
				interoperabile.setOggettoMessaggio(dati.getInteroperabile().getOggettoMessaggio());
				interoperabile.setProvenienzaMessaggio(dati.getInteroperabile().getProvenienzaMessaggio());
				interoperabile.setCodiceAmministrazione(dati.getInteroperabile().getCodiceAmministrazione());
				interoperabile.setCodiceAOO(dati.getInteroperabile().getCodiceAOO());
				interoperabile.setCodiceRegistro(dati.getInteroperabile().getCodiceRegistro());
				interoperabile.setNumeroRegistrazione(dati.getInteroperabile().getNumeroRegistrazione());
				interoperabile.setDataRegistrazione(DateUtils.dateToXMLGrCal(dati.getInteroperabile().getDataRegistrazione()));
				interoperabile.setNomeDocumento(dati.getInteroperabile().getNomeDocumento());
				interoperabile.setOggettoDocumento(dati.getInteroperabile().getOggettoDocumento());
				interoperabile.setTitoloDocumento(dati.getInteroperabile().getTitoloDocumento());
				if (dati.getInteroperabile().getTipologiaSegnatura() != null)
					interoperabile.setTipologiaSegnatura(dati.getInteroperabile().getTipologiaSegnatura().name());
				interoperabile.setNomeAllegatoPrincipale(dati.getInteroperabile().getNomeAllegatoPrincipale());

				if (dati.getInteroperabile().getRisposta() != null) {
					it.eng.consolepec.xmlplugin.jaxb.DestinatarioInteroperabile destJaxb = new it.eng.consolepec.xmlplugin.jaxb.DestinatarioInteroperabile();
					destJaxb.setAggiornata(dati.getInteroperabile().getRisposta().isAggiornata());
					destJaxb.setAnnullata(dati.getInteroperabile().getRisposta().isAnnullata());
					destJaxb.setConfermaRicezione(dati.getInteroperabile().getRisposta().isConfermaRicezione());
					destJaxb.setConfermata(dati.getInteroperabile().getRisposta().isConfermata());
					destJaxb.setEmail(dati.getInteroperabile().getRisposta().getEmail());
					interoperabile.setRisposta(destJaxb);
				}

				for (DestinatarioInteroperabile di : dati.getInteroperabile().getDestinatari()) {
					it.eng.consolepec.xmlplugin.jaxb.DestinatarioInteroperabile destJaxb = new it.eng.consolepec.xmlplugin.jaxb.DestinatarioInteroperabile();
					destJaxb.setAggiornata(di.isAggiornata());
					destJaxb.setAnnullata(di.isAnnullata());
					destJaxb.setConfermaRicezione(di.isConfermaRicezione());
					destJaxb.setConfermata(di.isConfermata());
					destJaxb.setEmail(di.getEmail());
					interoperabile.getDestinatari().add(destJaxb);
				}

				for (DestinatarioInteroperabile cc : dati.getInteroperabile().getCopiaConoscenza()) {
					it.eng.consolepec.xmlplugin.jaxb.DestinatarioInteroperabile ccJaxb = new it.eng.consolepec.xmlplugin.jaxb.DestinatarioInteroperabile();
					ccJaxb.setAggiornata(cc.isAggiornata());
					ccJaxb.setAnnullata(cc.isAnnullata());
					ccJaxb.setConfermaRicezione(cc.isConfermaRicezione());
					ccJaxb.setConfermata(cc.isConfermata());
					ccJaxb.setEmail(cc.getEmail());
					interoperabile.getCopiaConoscenza().add(ccJaxb);
				}

				jaxb.getPEC().setInteroperabile(interoperabile);

			}

			// serializzazione eventi iter della pratica
			jaxb.getPEC().setEventiIter(new EventiIter());
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
				jaxb.getPEC().getEventiIter().getEventoIter().add(eventoIter);

			}

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione");
		}

	}

	private static void serializeRicevute(Pratica jaxb, List<it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta> ricevute) {

		for (it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta r : ricevute) {

			// nota, sia la lista della pratcia che qella del jaxb nonsono mai nulle, al limite sono vuote

			it.eng.consolepec.xmlplugin.jaxb.Ricevuta ric = new it.eng.consolepec.xmlplugin.jaxb.Ricevuta();
			ric.setTipo(it.eng.consolepec.xmlplugin.jaxb.TipoRicevuta.fromValue(r.getTipo().getLabel()));
			ric.setErrore(it.eng.consolepec.xmlplugin.jaxb.ErroreRicevuta.fromValue(r.getErrore().getLabel()));
			ric.setMittente(r.getMittente());

			for (DatiEmail.DestinatarioRicevuta d : r.getDestinatari()) {
				it.eng.consolepec.xmlplugin.jaxb.DestinatarioRicevuta dest = new it.eng.consolepec.xmlplugin.jaxb.DestinatarioRicevuta();
				dest.setEmail(d.getEmail());
				dest.setTipo(d.getTipo());
				ric.getDestinatari().add(dest);
			}

			ric.setRisposte(r.getRisposte());
			ric.setOggetto(r.getOggetto());
			ric.setGestoreEmittente(r.getGestoreEmittente());
			it.eng.consolepec.xmlplugin.jaxb.DataPec data = new it.eng.consolepec.xmlplugin.jaxb.DataPec();
			data.setGiorno(r.getData().getGiorno());
			data.setOra(r.getData().getOra());
			data.setZona(r.getData().getZona());
			ric.setData(data);
			ric.setIdentificativo(r.getIdentificativo());
			ric.setMsgid(r.getMsgid());
			if (r.getTipoConsegna() != null)
				ric.setRicevuta(it.eng.consolepec.xmlplugin.jaxb.TipoConsegna.fromValue(r.getTipoConsegna().getLabel()));
			ric.setConsegna(r.getConsegna());
			ric.setErroreEsteso(r.getErroreEsteso());
			jaxb.getPEC().getRicevute().add(ric);
		}
	}

	@Override
	protected void serializePraticheCollegate(Pratica praticaJaxb, List<PraticaCollegata> praticheCollegate) {
		for (it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.PraticaCollegata p : praticheCollegate) {
			logger.debug("Serializzazione info pratica collegata: {}", p);
			it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata pc = new it.eng.consolepec.xmlplugin.jaxb.PraticaCollegata();
			pc.setAlfrescoPath(p.getAlfrescoPath());
			pc.setTipo(p.getTipo());
			pc.setDataCaricamento(DateUtils.dateToXMLGrCal(p.getDataCaricamento()));
			praticaJaxb.getPraticaCollegata().add(pc);
		}
	}

	@Override
	protected void loadDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {
		PEC pec = jaxb.getPEC();
		DatiEmail.Builder builder = new DatiEmail.Builder();
		builder.setBody(pec.getBody());
		builder.setFirma(pec.getFirma());
		if (jaxb.getDataCreazione() != null)
			builder.setDataCreazione(jaxb.getDataCreazione().toGregorianCalendar().getTime());
		if (pec.getDataRicezione() != null)
			builder.setDataRicezione(pec.getDataRicezione().toGregorianCalendar().getTime());

		it.eng.consolepec.xmlplugin.jaxb.Destinatario destPrincipale = jaxb.getPEC().getDestinatari().getDestinatarioPrincipale();
		if (destPrincipale != null && destPrincipale.getEmail() != null) {
			Destinatario dest = getDestinatario(destPrincipale);
			builder.setDestinatarioPrincipale(dest);
		}
		if (jaxb.getPEC().getDestinatariInoltro() != null) {
			builder.setDestinatariInoltro(getDestInoltro(jaxb.getPEC().getDestinatariInoltro().getDestinatarioInoltro()));
		}
		builder.setProgressivoInoltro(jaxb.getPEC().getProgressivoInoltro());

		/* informazioni sui destinatari */
		it.eng.consolepec.xmlplugin.jaxb.Destinatario[] destinatari = jaxb.getPEC().getDestinatari().getDestinatario().toArray(new it.eng.consolepec.xmlplugin.jaxb.Destinatario[0]);
		builder.setDestinatari(getDestinatari(destinatari));
		builder.setFolderPath(sanitizePath(jaxb.getFolderPath()));
		builder.setConsoleFileName(jaxb.getConsoleFileName());
		builder.setMittente(jaxb.getPEC().getMittente());
		builder.setOggetto(jaxb.getPEC().getOggetto());
		builder.setTitolo(jaxb.getTitolo());
		builder.setUtenteCreazione(jaxb.getUtenteCreazione());
		builder.setUsernameCreazione(jaxb.getUsernameCreazione());
		builder.setMessageIDReinoltro(jaxb.getPEC().getMessageIDReinoltro());
		builder.setReplyTo(jaxb.getPEC().getReplyTo());
		builder.setNote(jaxb.getPEC().getNote());
		if (jaxb.getIncaricoa() != null) {
			Date dataPresaInCarico = jaxb.getIncaricoa().getDatapresaincarico() == null ? new Date() : DateUtils.xmlGrCalToDate(jaxb.getIncaricoa().getDatapresaincarico());
			Utente inCaricoA = new Utente(jaxb.getIncaricoa().getUsername(), jaxb.getIncaricoa().getNome(), jaxb.getIncaricoa().getCognome(), jaxb.getIncaricoa().getMatricola(),
					jaxb.getIncaricoa().getCodicefiscale(), dataPresaInCarico);
			builder.setInCaricoA(inCaricoA);
		}
		if (jaxb.getPEC().getTipoEmail() != null)
			builder.setTipoEmail(TipoEmail.valueOf(jaxb.getPEC().getTipoEmail()));
		builder.setMessageID(pec.getMessageID());
		builder.setIdEmailServer(pec.getIdEmailServer());
		/* informazioni sui destinatariCC */
		List<it.eng.consolepec.xmlplugin.jaxb.Destinatario> destinatariCC = jaxb.getPEC().getDestinatariCC().getDestinatarioCC();
		/* caricamento dei gruppi abilitati */
		if (jaxb.getGruppivisibilita() != null) {
			List<GruppoVisibilita> gruppi = new ArrayList<DatiPratica.GruppoVisibilita>();
			for (String abilitato : jaxb.getGruppivisibilita().getGruppovisibilita()) {
				gruppi.add(new GruppoVisibilita(abilitato));
			}
			builder.setGruppiVisibilita(gruppi);
		}

		builder.setDestinatariCC(getDest(destinatariCC));
		/* informazioni sulla protocollazione */
		if (pec.getProtocollazione() != null) {

			ProtocollazionePECBuilder protocollazionePECBuilder = new ProtocollazionePECBuilder();

			protocollazionePECBuilder.setAnnoPG(pec.getProtocollazione().getAnnoPG().intValue());
			if (pec.getProtocollazione().getAnnoRegistro() != null)
				protocollazionePECBuilder.setAnnoRegistro(pec.getProtocollazione().getAnnoRegistro().intValue());
			if (pec.getProtocollazione().getDataprotocollazione() != null)
				protocollazionePECBuilder.setDataprotocollazione(pec.getProtocollazione().getDataprotocollazione().toGregorianCalendar().getTime());
			protocollazionePECBuilder.setNumeroFascicolo(pec.getProtocollazione().getNumerofascicolo());
			protocollazionePECBuilder.setNumeroPG(pec.getProtocollazione().getNumeroPG());
			protocollazionePECBuilder.setNumeroRegistro(pec.getProtocollazione().getNumeroRegistro());
			protocollazionePECBuilder.setOggetto(pec.getProtocollazione().getOggetto());
			protocollazionePECBuilder.setProvenienza(pec.getProtocollazione().getProvenienza());
			protocollazionePECBuilder.setRubrica(pec.getProtocollazione().getRubrica());
			protocollazionePECBuilder.setSezione(pec.getProtocollazione().getSezione());
			protocollazionePECBuilder.setTipologiadocumento(pec.getProtocollazione().getTipologiadocumento());
			protocollazionePECBuilder.setTitolo(pec.getProtocollazione().getTitolo());
			protocollazionePECBuilder.setUtenteprotocollazione(pec.getProtocollazione().getUtenteprotocollazione());
			protocollazionePECBuilder.setTipoProtocollazione(XmlPluginUtil.loadTipoProtocollazione(pec.getProtocollazione().getTipoProtocollazione()));

			if (pec.getProtocollazione().getCapofila() != null) {
				PG capofila = new PG();
				capofila.setAnnoPG(pec.getProtocollazione().getCapofila().getAnnoPG().intValue());
				capofila.setNumeroPG(pec.getProtocollazione().getCapofila().getNumeroPG());
				protocollazionePECBuilder.setCapofila(capofila);
			}

			builder.setProtocollazionePEC(protocollazionePECBuilder.construct());
		}
		if (pec.getInteroperabile() != null) {
			Interoperabile interoperabile = new Interoperabile();
			interoperabile.setStato(pec.getInteroperabile().getStato());
			interoperabile.setOggettoMessaggio(pec.getInteroperabile().getOggettoMessaggio());
			interoperabile.setProvenienzaMessaggio(pec.getInteroperabile().getProvenienzaMessaggio());
			interoperabile.setCodiceAmministrazione(pec.getInteroperabile().getCodiceAmministrazione());
			interoperabile.setCodiceAOO(pec.getInteroperabile().getCodiceAOO());
			interoperabile.setCodiceRegistro(pec.getInteroperabile().getCodiceRegistro());
			interoperabile.setNumeroRegistrazione(pec.getInteroperabile().getNumeroRegistrazione());
			interoperabile.setDataRegistrazione(DateUtils.xmlGrCalToDate(pec.getInteroperabile().getDataRegistrazione()));
			interoperabile.setNomeDocumento(pec.getInteroperabile().getNomeDocumento());
			interoperabile.setOggettoDocumento(pec.getInteroperabile().getOggettoDocumento());
			interoperabile.setTitoloDocumento(pec.getInteroperabile().getTitoloDocumento());
			interoperabile.setNomeAllegatoPrincipale(pec.getInteroperabile().getNomeAllegatoPrincipale());
			if (pec.getInteroperabile().getRisposta() != null) {
				it.eng.consolepec.xmlplugin.jaxb.DestinatarioInteroperabile destJaxb = pec.getInteroperabile().getRisposta();
				interoperabile.setRisposta(
						new DestinatarioInteroperabile(destJaxb.getEmail(), destJaxb.isConfermaRicezione(), destJaxb.isConfermata(), destJaxb.isAggiornata(), destJaxb.isAnnullata()));
			}
			for (it.eng.consolepec.xmlplugin.jaxb.DestinatarioInteroperabile destJaxb : pec.getInteroperabile().getDestinatari())
				interoperabile.getDestinatari().add(
						new DestinatarioInteroperabile(destJaxb.getEmail(), destJaxb.isConfermaRicezione(), destJaxb.isConfermata(), destJaxb.isAggiornata(), destJaxb.isAnnullata()));
			for (it.eng.consolepec.xmlplugin.jaxb.DestinatarioInteroperabile ccJaxb : pec.getInteroperabile().getCopiaConoscenza())
				interoperabile.getCopiaConoscenza().add(
						new DestinatarioInteroperabile(ccJaxb.getEmail(), ccJaxb.isConfermaRicezione(), ccJaxb.isConfermata(), ccJaxb.isAggiornata(), ccJaxb.isAnnullata()));
			if (pec.getInteroperabile().getTipologiaSegnatura() != null)
				interoperabile.setTipologiaSegnatura(TipologiaSegnatura.valueOf(pec.getInteroperabile().getTipologiaSegnatura()));
			builder.setInteroperabile(interoperabile);
		}

		builder.setIdDocumentale(jaxb.getIdDocumentale());
		dati = builder.construct();
		dati.setStato(Stato.valueOf(jaxb.getStato()));
		dati.setLetto(jaxb.isLetto());
		dati.setRicevutaAccettazione(jaxb.getPEC().isHasRicevutaAccettazione());
		dati.setRicevutaConsegna(jaxb.getPEC().isHasRicevutaConsegna());
		// load delle ricevute
		loadRicevute(jaxb.getPEC().getRicevute());

		dati.setNotificaRifiutoInoltro(jaxb.getPEC().isNotificaRifiutoInoltro());

		getDatiPraticaTaskAdapter().setTipo(new TipologiaPratica(jaxb.getTipo()));
		if (jaxb.getPEC().getDataInvio() != null)
			((DatiPraticaEmailTaskAdapter) getDatiPraticaTaskAdapter()).setDataInvio(jaxb.getPEC().getDataInvio().toGregorianCalendar().getTime());
		/* informazioni sugli allegati */
		Allegati allg = jaxb.getPEC().getAllegati();
		for (Documento doc : allg.getDocumento()) {
			dati.getAllegati().add(XmlPluginUtil.getAllegatoFromDocumento(doc, dati));
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

		ArrayList<DatiPratica.PraticaCollegata> praticheCollegate = new ArrayList<DatiPratica.PraticaCollegata>(pCollegate);
		for (DatiFascicolo.PraticaCollegata pc : praticheCollegate)
			addPraticaCollegata(pc);

		List<EventoIter> eventiIter = jaxb.getPEC().getEventiIter() == null ? new ArrayList<EventoIter>() : jaxb.getPEC().getEventiIter().getEventoIter();
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

		loadAssegnazioneEsterna(jaxb, dati);
		loadNotifiche(jaxb, dati);

		if (jaxb.getOperatore() != null) {
			dati.getOperatore().setNome(jaxb.getOperatore());
		}

	}

	private void loadRicevute(List<it.eng.consolepec.xmlplugin.jaxb.Ricevuta> ricevuteJaxb) {

		List<it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta> ricevute = new ArrayList<it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta>();

		for (it.eng.consolepec.xmlplugin.jaxb.Ricevuta r : ricevuteJaxb) {

			// nota, sia la lista della pratcia che qella del jaxb nonsono mai nulle, al limite sono vuote

			it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta ric = new it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta();

			ric.setTipo(it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.TipoRicevuta.fromLabel(r.getTipo().value()));
			ric.setErrore(it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.ErroreRicevuta.fromLabel(r.getErrore().value()));
			ric.setMittente(r.getMittente());

			for (it.eng.consolepec.xmlplugin.jaxb.DestinatarioRicevuta d : r.getDestinatari()) {
				DatiEmail.DestinatarioRicevuta dest = new DatiEmail.DestinatarioRicevuta(d.getEmail(), d.getTipo());
				ric.getDestinatari().add(dest);
			}

			ric.setRisposte(r.getRisposte());
			ric.setOggetto(r.getOggetto());
			ric.setGestoreEmittente(r.getGestoreEmittente());
			it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.DataPec data = new it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.DataPec();
			data.setGiorno(r.getData().getGiorno());
			data.setOra(r.getData().getOra());
			data.setZona(r.getData().getZona());
			ric.setData(data);
			ric.setIdentificativo(r.getIdentificativo());
			ric.setMsgid(r.getMsgid());
			if (r.getRicevuta() != null)
				ric.setTipoConsegna(it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.TipoConsegna.fromLabel(r.getRicevuta().value()));
			ric.setConsegna(r.getConsegna());
			ric.setErroreEsteso(r.getErroreEsteso());
			ricevute.add(ric);
		}

		dati.setRicevute(ricevute);

	}

	private static String[] getDestInoltro(List<String> destinatariInoltro) {
		return destinatariInoltro.toArray(new String[0]);
	}

	private static Destinatario[] getDest(List<it.eng.consolepec.xmlplugin.jaxb.Destinatario> destinatariCC) {
		List<Destinatario> ds = new ArrayList<DatiEmail.Destinatario>();
		for (it.eng.consolepec.xmlplugin.jaxb.Destinatario d : destinatariCC)
			ds.add(getDestinatario(d));
		return ds.toArray(new Destinatario[0]);
	}

	private static Destinatario getDestinatario(it.eng.consolepec.xmlplugin.jaxb.Destinatario d) {
		it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail tipoEmail = it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.valueOf(d.getTipo());
		DatiEmail.Destinatario dest = new Destinatario(d.getEmail(), (tipoEmail == null) ? it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno : tipoEmail);
		if (d.getErrore() != null)
			dest.setErrore(d.getErrore());
		dest.setAccettazione((d.isAccettazione() == null) ? false : d.isAccettazione());
		dest.setConsegna((d.isConsegna() == null) ? false : d.isConsegna());
		if (d.getStatoDest() != null)
			dest.setStatoDestinatario(it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.StatoDestinatario.valueOf(d.getStatoDest()));
		return dest;
	}

	private static Destinatario[] getDestinatari(it.eng.consolepec.xmlplugin.jaxb.Destinatario[] destinatari) {
		List<DatiEmail.Destinatario> list = new ArrayList<DatiEmail.Destinatario>();
		for (it.eng.consolepec.xmlplugin.jaxb.Destinatario d : destinatari)
			list.add(getDestinatario(d));
		return list.toArray(new Destinatario[0]);
	}

	@Override
	protected void initPratica(DatiEmail dati) throws PraticaException {
		this.dati = dati;
		getDatiPraticaTaskAdapter().setTipo(getTipo());
	}

	@Override
	public Map<MetadatiPratica, Object> getMetadata() {
		Map<MetadatiPratica, Object> map = new HashMap<MetadatiPratica, Object>();

		if (getDati().getAssegnatarioCorrente() != null) {
			map.put(MetadatiPratica.pAssegnatoA, getDati().getAssegnatarioCorrente());

		} else {
			for (Task<?> task : getTasks()) {
				if (task instanceof GestionePECTask && task.isAttivo()) {
					GestionePECTask taskGestione = (GestionePECTask) task;
					map.put(MetadatiPratica.pAssegnatoA, taskGestione.getDati().getAssegnatario().getNome());
					break;
				} else if (task instanceof RiattivaPECInTask && task.isAttivo()) {
					RiattivaPECInTask taskRiattiva = (RiattivaPECInTask) task;
					map.put(MetadatiPratica.pAssegnatoA, taskRiattiva.getDati().getAssegnatario().getNome());
					break;
				} else if (task instanceof RiattivaPECOutTask && task.isAttivo()) {
					RiattivaPECOutTask taskRiattiva = (RiattivaPECOutTask) task;
					map.put(MetadatiPratica.pAssegnatoA, taskRiattiva.getDati().getAssegnatario().getNome());
					break;
				}
			}
		}

		map.put(MetadatiPratica.pDataCreazione, dati.getDataCreazione());
		map.put(MetadatiPratica.pDataInvio, dati.getDataInvio());
		map.put(MetadatiPratica.pIdDocumentale, getDati().getIdDocumentale());
		map.put(MetadatiPratica.pProvenienza, getDati().getProvenienza());
		map.put(MetadatiPratica.pStato, getDati().getStato().toString());
		map.put(MetadatiPratica.pTipoPratica, getDati().getTipo().getNomeTipologia());
		map.put(MetadatiPratica.pTitolo, dati.getOggetto());
		if (dati.getUtenteCreazione() != null)
			map.put(MetadatiPratica.pUtenteCreazione, dati.getUtenteCreazione());

		map.put(MetadatiPratica.pIdMessaggioEmail, dati.getMessageID());

		StringBuilder sbDest = new StringBuilder();
		for (Destinatario dest : dati.getDestinatari())
			sbDest.append(dest.getDestinatario()).append(",");
		if (sbDest.length() > 0)
			sbDest.deleteCharAt(sbDest.length() - 1);
		if (dati.getDestinatarioPrincipale() != null)
			sbDest.append(",").append(dati.getDestinatarioPrincipale().getDestinatario());

		map.put(MetadatiPratica.pDestinatario, sbDest.toString());
		map.put(MetadatiPratica.pDataRicezione, dati.getDataRicezione());
		map.put(MetadatiPratica.pInoltratoDa, getInoltratoDa());
		map.put(MetadatiPratica.pProvenienza, getProvenienza());
		StringBuilder sbCC = new StringBuilder();
		for (Destinatario cc : dati.getDestinatariCC())
			sbCC.append(cc.toString()).append(",");
		if (sbCC.length() > 0)
			sbCC.deleteCharAt(sbCC.length() - 1);
		map.put(MetadatiPratica.pCc, sbCC.toString());
		map.put(MetadatiPratica.pTipoMail, dati.getTipoEmail().name());
		map.put(MetadatiPratica.pLetto, dati.isLetto());
		map.put(MetadatiPratica.pRicevutaConsegna, Boolean.valueOf(dati.isRicevutaConsegna()));
		map.put(MetadatiPratica.pRicevutaAccettazione, Boolean.valueOf(dati.isRicevutaAccettazione()));

		/*
		 * FM: i dati delle ricevute non sono ricercabili (per il momento non serve)
		 */

		if (dati.getProtocollazionePec() != null) {
			map.put(MetadatiPratica.pNumeroPG, dati.getProtocollazionePec().getNumeroPG());
			map.put(MetadatiPratica.pAnnoPG, dati.getProtocollazionePec().getAnnoPG());
		}
		map.put(MetadatiPratica.pTipoPratica, dati.getTipo().getNomeTipologia());
		map.put(MetadatiPratica.pData, getDate());

		if (dati.getGruppiVisibilita().size() != 0)
			map.put(MetadatiPratica.pVisibileA, XmlPluginUtil.getListaVisibilita(dati.getGruppiVisibilita()));

		map.put(MetadatiPratica.pIncaricoA, GenericsUtil.sanitizeNull(dati.getInCaricoA() != null ? dati.getInCaricoA().getUsername() : null));

		map.put(MetadatiPratica.pElencoProtocollazioni, XmlPluginUtil.getElencoProtocollazionePec(dati.getProtocollazionePec()));

		if (dati.getInteroperabile() != null) {

			map.put(MetadatiPratica.pMailInteroperabileStato, dati.getInteroperabile().getStato());
			map.put(MetadatiPratica.pMailInteroperabileOggettoMessaggio, dati.getInteroperabile().getOggettoMessaggio());
			map.put(MetadatiPratica.pMailInteroperabileProvenienzaMessaggio, dati.getInteroperabile().getProvenienzaMessaggio());
			map.put(MetadatiPratica.pMailInteroperabileCodiceAmministrazione, dati.getInteroperabile().getCodiceAmministrazione());
			map.put(MetadatiPratica.pMailInteroperabileCodiceAOO, dati.getInteroperabile().getCodiceAOO());
			map.put(MetadatiPratica.pMailInteroperabileCodiceRegistro, dati.getInteroperabile().getCodiceRegistro());
			map.put(MetadatiPratica.pMailInteroperabileNumeroRegistrazione, dati.getInteroperabile().getNumeroRegistrazione());
			map.put(MetadatiPratica.pMailInteroperabileDataRegistrazione, dati.getInteroperabile().getDataRegistrazione());
			map.put(MetadatiPratica.pMailInteroperabileNomeDocumento, dati.getInteroperabile().getNomeDocumento());
			map.put(MetadatiPratica.pMailInteroperabileOggettoDocumento, dati.getInteroperabile().getOggettoDocumento());
			map.put(MetadatiPratica.pMailInteroperabileTitoloDocumento, dati.getInteroperabile().getTitoloDocumento());

			StringBuilder sbInterDest = new StringBuilder();
			for (DestinatarioInteroperabile di : dati.getInteroperabile().getDestinatari())
				sbInterDest.append(di.toString()).append(",");
			if (sbInterDest.length() > 0)
				sbInterDest.deleteCharAt(sbInterDest.length() - 1);
			map.put(MetadatiPratica.pMailInteroperabileDestinatari, sbInterDest.toString());

			StringBuilder sbInterCC = new StringBuilder();
			for (DestinatarioInteroperabile cc : dati.getInteroperabile().getCopiaConoscenza())
				sbInterCC.append(cc.toString()).append(",");
			if (sbInterCC.length() > 0)
				sbInterCC.deleteCharAt(sbInterCC.length() - 1);
			map.put(MetadatiPratica.pMailInteroperabileCc, sbInterCC.toString());

			map.put(MetadatiPratica.pMailInteroperabile, true);

		}

		if (getDati().getDestinatariInoltro() != null) {
			StringBuilder sb = new StringBuilder();
			for (String di : getDati().getDestinatariInoltro())
				sb.append(di).append(",");
			if (sb.length() > 0)
				sb.deleteCharAt(sb.length() - 1);
			map.put(MetadatiPratica.pDestinatariInoltro, sb.toString());
		}
		if (getDati().getProgressivoInoltro() != null)
			map.put(MetadatiPratica.pProgressivoInoltro, getDati().getProgressivoInoltro().toString());
		map.put(MetadatiPratica.pNotificaRifiutoInoltro, Boolean.valueOf(dati.isNotificaRifiutoInoltro()));

		if (dati.getOperatore() != null && dati.getOperatore().getNome() != null) {
			map.put(MetadatiPratica.pOperatore, dati.getOperatore().getNome());
		}

		return map;
	}

	@Override
	public boolean isAttiva() {
		return !(getDati().getStato().equals(DatiEmail.Stato.ARCHIVIATA) || getDati().getStato().equals(DatiEmail.Stato.ELIMINATA) || getDati().getStato().equals(DatiEmail.Stato.SCARTATA));
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
		return dati.getMessageID().hashCode();
	};

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XMLPraticaEmail) {
			XMLPraticaEmail pratica2 = (XMLPraticaEmail) obj;
			return pratica2.getDati().getMessageID().equals(getDati().getMessageID()) && pratica2.getTasks().equals(getTasks());
		}
		return false;
	}

	@Override
	public String toString() {
		return "Pratica: " + dati.toString();
	}

	@Override
	public boolean isMailCompleta() {
		return super.isXmlValid();
	}

	/* metodi interni */
	private static String sanitizePath(String path) {
		if (path != null && path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		return path;
	}

	@Override
	public boolean isRiattivabile() {
		Set<Task<?>> tasks2 = this.getTasks();
		for (Task<?> t : tasks2)
			if (t.isAttivo() && t instanceof RiattivaPECTask) {
				Stato stato = getDati().getStato();
				boolean checkStato = Stato.ELIMINATA.equals(stato) || (Stato.ARCHIVIATA.equals(stato) && getAllPraticheCollegate().size() == 0)
						|| (Stato.RESPINTA.equals(stato) && getAllPraticheCollegate().size() == 0) || Stato.SCARTATA.equals(stato);
				return checkStato && getDati().getProtocollazionePec() == null;
			}
		return false;
	}

	@Override
	public boolean isEmailInteroperabile() {
		return getDati().getInteroperabile() != null;
	}

	@Override
	public boolean isRiattivabileElettorale() {
		return Stato.SCARTATA.equals(getDati().getStato()) && getDati().getProtocollazionePec() == null;
	}

	protected Task<?> getTaskInGestioneDisabilitato() {
		Set<Task<?>> tasks2 = this.getTasks();
		for (Task<?> t : tasks2)
			if (!t.isAttivo() && t instanceof GestionePECInTask)
				return t;
		return null;
	}

}
