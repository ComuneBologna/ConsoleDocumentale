package it.eng.consolepec.xmlplugin.pratica.template;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.EventoIterPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.XMLPratica;
import it.eng.consolepec.xmlplugin.jaxb.CampoMetadato;
import it.eng.consolepec.xmlplugin.jaxb.CampoTemplate;
import it.eng.consolepec.xmlplugin.jaxb.Documento;
import it.eng.consolepec.xmlplugin.jaxb.EventoIter;
import it.eng.consolepec.xmlplugin.jaxb.Gruppivisibilita;
import it.eng.consolepec.xmlplugin.jaxb.Pratica;
import it.eng.consolepec.xmlplugin.pratica.template.CampoTemplate.TipoCampoTemplate;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.GestioneAbstractTemplateTask;
import it.eng.consolepec.xmlplugin.tasks.riattiva.RiattivaTemplateTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public abstract class XMLAbstractTemplate<T extends DatiAbstractTemplate> extends XMLPratica<T> implements AbstractTemplate<T> {

	protected T dati;

	@Override
	public DatiAbstractTemplateTaskAdapter getDatiPraticaTaskAdapter() {
		DatiAbstractTemplate t = getDati();
		return new DatiAbstractTemplateTaskAdapter(t);
	}

	@Override
	public String getSubFolderPath() {
		return "TEMPLATE";
	}

	@Override
	public Map<MetadatiPratica, Object> getMetadata() {
		Map<MetadatiPratica, Object> map = new HashMap<MetadatiPratica, Object>();

		map.put(MetadatiPratica.pDataCreazione, getDati().getDataCreazione());
		map.put(MetadatiPratica.pIdDocumentale, getDati().getIdDocumentale());
		map.put(MetadatiPratica.pTipoPratica, getDati().getTipo().getNomeTipologia());
		map.put(MetadatiPratica.pTitolo, getDati().getTitolo());
		map.put(MetadatiPratica.pLetto, getDati().isLetto());
		map.put(MetadatiPratica.pData, getDati().getDataCreazione());
		map.put(MetadatiPratica.pInoltratoDa, getInoltratoDa());
		map.put(MetadatiPratica.pProvenienza, getProvenienza());

		if (getDati().getUtenteCreazione() != null)
			map.put(MetadatiPratica.pUtenteCreazione, getDati().getUtenteCreazione());

		if (getDati().getGruppiVisibilita().size() != 0)
			map.put(MetadatiPratica.pVisibileA, XmlPluginUtil.getListaVisibilita(getDati().getGruppiVisibilita()));

		map.put(MetadatiPratica.pIncaricoA, GenericsUtil.sanitizeNull(getDati().getInCaricoA() != null ? getDati().getInCaricoA().getUsername() : null));

		if (getDati().getGruppiVisibilita().size() != 0)
			map.put(MetadatiPratica.pVisibileA, XmlPluginUtil.getListaVisibilita(getDati().getGruppiVisibilita()));

		if (getDati().getAssegnatarioCorrente() != null) {
			map.put(MetadatiPratica.pAssegnatoA, getDati().getAssegnatarioCorrente());

		} else {
			for (Task<?> task : getTasks()) {
				if (task instanceof GestioneAbstractTemplateTask && task.isAttivo()) {
					GestioneAbstractTemplateTask<?> taskGestione = (GestioneAbstractTemplateTask<?>) task;
					map.put(MetadatiPratica.pAssegnatoA, taskGestione.getDati().getAssegnatario().getNome());
					break;
				} else if (task instanceof RiattivaTemplateTask && task.isAttivo()) {
					RiattivaTemplateTask taskRiattiva = (RiattivaTemplateTask) task;
					map.put(MetadatiPratica.pAssegnatoA, taskRiattiva.getDati().getAssegnatario().getNome());
					break;
				}
			}
		}

		map.put(MetadatiPratica.pNomeTemplate, getDati().getNome());
		map.put(MetadatiPratica.pDescrizioneTemplate, getDati().getDescrizione());
		map.put(MetadatiPratica.pStatoTemplate, getDati().getStato().getLabel());
		return map;
	}

	@Override
	protected void serializePraticheCollegate(Pratica praticaJaxb, List<PraticaCollegata> praticheCollegate) {
		// i template non hanno pratiche collegate, quindi va bene un'implementazione vuota
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
	public String toString() {
		return "Template: " + getDati().toString();
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
		return getDati().getDataCreazione();
	}

	@Override
	protected String getProvenienza() {
		return getDati().getProvenienza();
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {

		logger.debug("Serializzazione pratica template {}", getAlfrescoPath());
		try {
			/* serializzazione dati generici */
			super.serializeDati(jaxb);
			jaxb.setLetto(getDati().isLetto());
			if (getDati().getStato() != null) {
				jaxb.setStato(getDati().getStato().name());
			}

			serializeDatiTemplate(jaxb);

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione");
		}

	}

	@Override
	protected void loadDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {

		logger.debug("Deserializzazione pratica template {}", getAlfrescoPath(jaxb));
		try {

			loadDatiTemplate(jaxb);

			dati.setLetto(jaxb.isLetto());
			if (jaxb.getStato() != null) {
				dati.setStato(DatiAbstractTemplate.Stato.valueOf(jaxb.getStato()));
			}

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di deserializzazione");
		}
	}

	/*
	 * METODI ASTRATTI PER LOAD E SERIALIZE DELLE SOTTOCLASSI
	 */

	protected abstract void loadDatiTemplate(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb);

	protected abstract void serializeDatiTemplate(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb);

	/*
	 * METODI DI UTILITY PER EVITARE COPIA E INCOLLA
	 */

	protected Collection<? extends EventoIter> getEventiIter() {

		List<EventoIter> eventi = new ArrayList<EventoIter>();
		for (EventoIterPratica e : getDati().getIter()) {
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
			eventi.add(eventoIter);
		}
		return eventi;
	}

	protected Collection<? extends Documento> getAllegati() {

		List<Documento> allegati = new ArrayList<Documento>();
		for (Allegato allg : getDati().getAllegati()) {
			Documento documentoJaxb = XmlPluginUtil.getDocumentoFromAllegato(allg);
			/* caricamento dei gruppi abilitati a vedere il template */
			documentoJaxb.setGruppivisibilita(new Gruppivisibilita());
			for (GruppoVisibilita gruppo : allg.getGruppiVisibilita()) {
				documentoJaxb.getGruppivisibilita().getGruppovisibilita().add(gruppo.getNomeGruppo());
			}
			allegati.add(documentoJaxb);
		}
		return allegati;
	}

	protected Collection<? extends CampoTemplate> getCampi() {

		List<CampoTemplate> campi = new ArrayList<CampoTemplate>();
		for (it.eng.consolepec.xmlplugin.pratica.template.CampoTemplate campo : getDati().getCampi()) {
			CampoTemplate jaxbCampo = new CampoTemplate();
			jaxbCampo.setNome(campo.getNome());
			jaxbCampo.setTipo(campo.getTipo().name());
			jaxbCampo.setFormato(campo.getFormato());
			jaxbCampo.setRegexValidazione(campo.getRegexValidazione());
			jaxbCampo.setLunghezzaMassima(campo.getLunghezzaMassima());
			jaxbCampo.getValoriLista().addAll(campo.getValoriLista());

			if (campo.getCampoMetadato() != null) {
				CampoMetadato cm = new CampoMetadato();
				cm.setIdMetadato(campo.getCampoMetadato().getIdMetadato());
				cm.setEtichetta(campo.getCampoMetadato().getEtichettaMetadato());
				jaxbCampo.setCampoMetadato(cm);
			}

			campi.add(jaxbCampo);
		}
		return campi;
	}

	protected Collection<? extends it.eng.consolepec.xmlplugin.pratica.template.CampoTemplate> getCampi(List<CampoTemplate> campiJaxb) {

		List<it.eng.consolepec.xmlplugin.pratica.template.CampoTemplate> campi = new ArrayList<it.eng.consolepec.xmlplugin.pratica.template.CampoTemplate>();
		for (CampoTemplate jaxbCampo : campiJaxb) {
			String nome = jaxbCampo.getNome();
			TipoCampoTemplate tipo = TipoCampoTemplate.valueOf(jaxbCampo.getTipo());
			String formato = jaxbCampo.getFormato();
			String regexValidazione = jaxbCampo.getRegexValidazione();
			Integer lunghezzaMassima = jaxbCampo.getLunghezzaMassima();
			List<String> valoriLista = jaxbCampo.getValoriLista();

			it.eng.consolepec.xmlplugin.pratica.template.CampoMetadato cm = null;
			if (jaxbCampo.getCampoMetadato() != null)
				cm = new it.eng.consolepec.xmlplugin.pratica.template.CampoMetadato(jaxbCampo.getCampoMetadato().getIdMetadato(), jaxbCampo.getCampoMetadato().getEtichetta());

			it.eng.consolepec.xmlplugin.pratica.template.CampoTemplate campo = new it.eng.consolepec.xmlplugin.pratica.template.CampoTemplate(nome, tipo, formato, regexValidazione, lunghezzaMassima,
					valoriLista, cm);
			campi.add(campo);
		}
		return campi;
	}

	protected void loadDatiComuniBuilder(T.Builder builder, Pratica jaxb) {
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
	}

	protected List<EventoIterPratica> getEventiIterPratica(List<EventoIter> eventiIter) {

		List<EventoIterPratica> lista = new ArrayList<EventoIterPratica>();
		if (eventiIter != null) {
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
				lista.add(eventoIterPratica);
			}
		}
		return lista;
	}
}
