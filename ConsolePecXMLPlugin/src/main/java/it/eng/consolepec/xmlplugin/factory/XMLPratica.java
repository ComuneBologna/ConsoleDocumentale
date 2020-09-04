package it.eng.consolepec.xmlplugin.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta.TipoAccesso;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Notifica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipologiaNotifica;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.PraticaObserver.VersionDownload;
import it.eng.consolepec.xmlplugin.factory.PraticaObserver.VersionLoad;
import it.eng.consolepec.xmlplugin.jaxb.AssegnazioneEsterna;
import it.eng.consolepec.xmlplugin.jaxb.Documento;
import it.eng.consolepec.xmlplugin.jaxb.Gruppivisibilita;
import it.eng.consolepec.xmlplugin.jaxb.Notifica.ParametriExtra;
import it.eng.consolepec.xmlplugin.jaxb.OperativitaRidotta;
import it.eng.consolepec.xmlplugin.jaxb.Pratica;
import it.eng.consolepec.xmlplugin.jaxb.Utente;
import it.eng.consolepec.xmlplugin.util.TranslatorBeanCondivisione;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

/**
 * Pratica basata su file XML. Le istanze sono ritornate dalla factory, i client lavorano via interfaccia Pratica
 *
 * @author pluttero
 *
 */
public abstract class XMLPratica<T extends DatiPratica> implements it.eng.consolepec.xmlplugin.factory.Pratica<T> {
	public static final String VERSIONE = "1.1";
	protected Logger logger = LoggerFactory.getLogger(XMLPratica.class);
	private final Set<XMLTask<?>> tasks = new HashSet<XMLTask<?>>();
	@Getter
	@Setter(value = AccessLevel.PROTECTED)
	boolean isXmlValid;

	/* metodi pubblici di interfaccia */

	@Override
	public abstract T getDati();

	@Override
	public void putTask(Task<?> task) {
		if (task instanceof XMLTask<?>) {
			putTask((XMLTask<?>) task);
		} else {
			throw new PraticaException("Il tipo di task non è supportato da questa pratica");
		}
	}

	@Override
	public Set<Task<?>> getTasks() {
		/*
		 * metodo di interfaccia, costruisco una nuova collection, in quanto l'utilizzatore non deve poter modificare il set
		 */
		HashSet<Task<?>> result = new HashSet<Task<?>>();
		for (XMLTask<?> task : tasks)
			result.add(task);
		return result;
	};

	@Override
	public String getAlfrescoPath() {
		StringBuilder path = new StringBuilder();
		path.append(getDati().getFolderPath()).append("/").append(getDati().getConsoleFileName());
		return path.toString();
	}

	protected String getAlfrescoPath(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) {

		StringBuilder path = new StringBuilder();
		path.append(jaxb.getFolderPath()).append("/").append(jaxb.getConsoleFileName());
		return path.toString();
	}

	/* metodi pubblici di classe */

	public DatiPraticaTaskAdapter getDatiPraticaTaskAdapter() {
		T t = getDati();
		return new DatiPraticaTaskAdapter(t);
	}

	/* metodi package ad uso della factory */

	void putTask(XMLTask<?> task) {
		this.tasks.add(task);
	}

	/* metodi protected */

	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica jaxb) throws PraticaException {
		GregorianCalendar cal = new GregorianCalendar();
		try {
			jaxb.setVersion(getDati().get_version());
			jaxb.setVersione(VERSIONE);
			jaxb.setSync(getDati().is_sync());
			jaxb.setFolderPath(getDati().getFolderPath());
			jaxb.setConsoleFileName(getDati().getConsoleFileName());
			jaxb.setProvenienza(getDati().getProvenienza());
			jaxb.setUtenteCreazione(getDati().getUtenteCreazione());
			jaxb.setUsernameCreazione(getDati().getUsernameCreazione());
			if (getDati().getDataCreazione() != null) {
				cal.setTime(getDati().getDataCreazione());
				jaxb.setDataCreazione(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
			}
			jaxb.setTipo(getDati().getTipo().getNomeTipologia());
			jaxb.setTitolo(getDati().getTitolo());

			/* informazioni sulle pratiche collegate */
			serializePraticheCollegate(jaxb, getDati().getPraticheCollegate());

			jaxb.setLetto(getDati().isLetto());
			jaxb.setIdDocumentale(getDati().getIdDocumentale());

			/* serializzazione utente in carico */
			if (getDati().getInCaricoA() != null) {
				Utente utente = new Utente();
				utente.setNome(getDati().getInCaricoA().getNome());
				utente.setCognome(getDati().getInCaricoA().getCognome());
				utente.setMatricola(getDati().getInCaricoA().getMatricola());
				utente.setUsername((getDati().getInCaricoA().getUsername()));
				utente.setCodicefiscale(getDati().getInCaricoA().getCodicefiscale());

				Date dataPresaInCarico = getDati().getInCaricoA().getDataPresaInCarico() == null ? new Date() : getDati().getInCaricoA().getDataPresaInCarico();

				utente.setDatapresaincarico(DateUtils.dateToXMLGrCal(dataPresaInCarico));
				jaxb.setIncaricoa(utente);
			}
			// serializzazione gruppi abilitati
			Gruppivisibilita gruppi = new Gruppivisibilita();
			List<String> gruppiAbilitati = gruppi.getGruppovisibilita();
			for (GruppoVisibilita abilitato : getDati().getGruppiVisibilita()) {
				gruppiAbilitati.add(abilitato.getNomeGruppo());
			}
			jaxb.setGruppivisibilita(gruppi);

			if (getDati().getAssegnazioneEsterna() != null) {
				AssegnazioneEsterna ae = new AssegnazioneEsterna();

				ae.getDestinatari().addAll(getDati().getAssegnazioneEsterna().getDestinatari());
				ae.setTestoEmail(getDati().getAssegnazioneEsterna().getTestoMail());
				ae.setOperazioni(TranslatorBeanCondivisione.getListaOperazioni(getDati().getAssegnazioneEsterna().getOperazioniConsentite()));
				ae.setDataNotifica(DateUtils.dateToXMLGrCal(getDati().getAssegnazioneEsterna().getDataNotifica()));

				jaxb.setAssegnazioneEsterna(ae);

			}

			for (Notifica notifica : getDati().getNotifiche()) {
				it.eng.consolepec.xmlplugin.jaxb.Notifica n = new it.eng.consolepec.xmlplugin.jaxb.Notifica();
				n.setOperazione(notifica.getOperazione());
				if (notifica.getTipologia() != null)
					n.setTipologia(notifica.getTipologia().name());

				if (notifica.getParametriExtra() != null) {
					ParametriExtra parametriExtra = new ParametriExtra();
					parametriExtra.setSalvaEMLNotificaEmail(notifica.getParametriExtra().isSalvaEMLNotificaEmail());
					parametriExtra.setCancellaAllegatiCaricati(notifica.getParametriExtra().isCancellaAllegatiCaricati());
					parametriExtra.getIndirizziEmail().addAll(notifica.getParametriExtra().getIndirizziEmail());
					parametriExtra.setMittenteEmail(notifica.getParametriExtra().getMittenteEmail());
					for (Allegato allg : notifica.getParametriExtra().getAllegatiDaCaricare()) {
						parametriExtra.getAllegatiDaCaricare().add(XmlPluginUtil.getDocumentoFromAllegato(allg));
					}
					parametriExtra.setIdTask(notifica.getParametriExtra().getIdTask());
					parametriExtra.setOperazioneTask(notifica.getParametriExtra().getOperazioneTask());
					parametriExtra.setNoteTask(notifica.getParametriExtra().getNoteTask());
					n.setParametriExtra(parametriExtra);

				}

				jaxb.getNotifiche().add(n);
			}

			if (getDati().getOperatore() != null && getDati().getOperatore().getNome() != null) {
				jaxb.setOperatore(getDati().getOperatore().getNome());
			}

			if (getDati().getOperativitaRidotta() != null) {
				OperativitaRidotta operativitaRidotta = new OperativitaRidotta();

				for (OperazioneOperativitaRidotta ovr : getDati().getOperativitaRidotta().getOperazioni()) {
					it.eng.consolepec.xmlplugin.jaxb.OperazioneOperativitaRidotta opJaxb = new it.eng.consolepec.xmlplugin.jaxb.OperazioneOperativitaRidotta();
					opJaxb.setNomeOperazione(ovr.getNomeOperazione());

					for (TipoAccesso accessoConsentito : ovr.getAccessiConsentiti()) {
						opJaxb.getAccessiConsentiti().add(accessoConsentito.name());
					}

					operativitaRidotta.getOperazioni().add(opJaxb);
				}

				jaxb.setOperativitaRidotta(operativitaRidotta);
			}

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione pratica");
		}

	}

	// serve per differenziare la gestione delle pratiche protocollate
	protected abstract void serializePraticheCollegate(Pratica praticaJaxb, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata> praticheCollegate);

	protected abstract void loadDati(Pratica praticaJaxb) throws PraticaException;

	protected abstract void initPratica(T t) throws PraticaException;

	protected Set<XMLTask<?>> getXMLTasks() {
		return tasks;
	}

	@Override
	public void cambiaPath(String newFolderPath) {
		getDatiPraticaTaskAdapter().setFolderPath(newFolderPath);
	}

	@Override
	public void downloadAllegato(Allegato allegato, PraticaObserver.FileDownload handler) {
		if (!getDati().getAllegati().contains(allegato)) {
			throw new PraticaException("L'allegato non appartiene alla pratica associata a questo task");
		}
		StringBuilder path = new StringBuilder();
		path.append(getDati().getFolderPath()).append("/").append(allegato.getFolderRelativePath()).append("/").append(allegato.getNome());
		String pathstr = path.toString();

		handler.onDownloadRequest(pathstr);

	}

	@Override
	public void verificaFirma(Allegato allegato, PraticaObserver.VerificaFirma handler) {
		if (!getDati().getAllegati().contains(allegato)) {
			throw new PraticaException("L'allegato non appartiene alla pratica associata a questo task");
		}
		StringBuilder path = new StringBuilder();
		path.append(getDati().getFolderPath()).append("/").append(allegato.getFolderRelativePath()).append("/").append(allegato.getNome());
		String pathstr = path.toString();
		logger.debug("Verifica firma allegato: {}. Path calcolato: {}", allegato, pathstr);

		handler.onVerificaFirma(pathstr);
	}

	@Override
	public void loadVersioni(Allegato allegato, VersionLoad handler) {

		StringBuilder path = new StringBuilder();
		path.append(getDati().getFolderPath()).append("/").append(allegato.getFolderRelativePath());

		logger.debug("Path dell'allegato: {}/{}.", path.toString(), allegato.getNome());
		List<Versione> versioni = handler.onLoadVersioni(path.toString(), allegato.getNome());

		allegato.getVersioni().addAll(versioni);

	}

	@Override
	public void downloadVersion(String versionid, VersionDownload handler) {
		logger.debug("Download version: {}", versionid);
		handler.onDownloadRequest(versionid);
	}

	protected String getInoltratoDa() {
		Set<Task<?>> tasks = getTasks();
		List<Assegnatario> assegnatariPassati = new ArrayList<Assegnatario>();
		for (Task<?> t : tasks)
			assegnatariPassati = t.getDati().getAssegnatariPassati();
		if (assegnatariPassati.size() == 0)
			return null;
		return assegnatariPassati.get(assegnatariPassati.size() - 1).getNome();
	}

	@Override
	public it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente getInCaricoA() {
		return getDati().getInCaricoA();
	}

	protected abstract Date getDate();

	protected abstract String getProvenienza();

	/*
	 * gestione pratiche collegate
	 */

	@Override
	public void addPraticaCollegata(PraticaCollegata pc) {
		getDati().getPraticheCollegate().add(pc);
	}

	@Override
	public void removePraticaCollegata(PraticaCollegata pc) {
		getDati().getPraticheCollegate().remove(pc);
	}

	@Override
	public void removePraticaCollegata(it.eng.consolepec.xmlplugin.factory.Pratica<? extends DatiPratica> pratica) {
		PraticaCollegata daEliminare = null;
		for (PraticaCollegata pc : getDati().getPraticheCollegate())
			if (pc.getAlfrescoPath().equals(pratica.getAlfrescoPath()))
				daEliminare = pc;
		getDati().getPraticheCollegate().remove(daEliminare);
	}

	@Override
	public boolean hasPraticaCollegata(PraticaCollegata pc) {
		for (PraticaCollegata p : getDati().getPraticheCollegate())
			if (p.equals(pc))
				return true;
		return false;
	}

	@Override
	public boolean hasPraticaCollegata(it.eng.consolepec.xmlplugin.factory.Pratica<? extends DatiPratica> pratica) {
		if (pratica != null)
			for (PraticaCollegata p : getDati().getPraticheCollegate())
				if (p.getAlfrescoPath().equals(pratica.getAlfrescoPath()))
					return true;
		return false;
	}

	@Override
	public boolean hasPraticheCollegate() {
		return !getDati().getPraticheCollegate().isEmpty();
	}

	@Override
	public List<PraticaCollegata> getFascicoliCollegati() {
		List<PraticaCollegata> result = new ArrayList<DatiPratica.PraticaCollegata>();
		for (PraticaCollegata pc : getDati().getPraticheCollegate())
			if (PraticaUtil.isFascicolo(pc.getTipo()))
				result.add(pc);

		return result;
	}

	@Override
	public List<PraticaCollegata> getEmailInCollegate() {
		List<PraticaCollegata> result = new ArrayList<DatiPratica.PraticaCollegata>();
		for (PraticaCollegata pc : getDati().getPraticheCollegate())
			if (PraticaUtil.isIngresso(pc.getTipo()))
				result.add(pc);
		return result;
	}

	@Override
	public List<PraticaCollegata> getEmailOutCollegate() {
		List<PraticaCollegata> result = new ArrayList<DatiPratica.PraticaCollegata>();
		for (PraticaCollegata pc : getDati().getPraticheCollegate())
			if (PraticaUtil.isEmailOut(pc.getTipo()))
				result.add(pc);

		return result;
	}

	@Override
	public List<PraticaCollegata> getModuliCollegati() {
		List<PraticaCollegata> result = new ArrayList<DatiPratica.PraticaCollegata>();
		for (PraticaCollegata pc : getDati().getPraticheCollegate())
			if (PraticaUtil.isPraticaModulistica(pc.getTipo()))
				result.add(pc);

		return result;
	}

	@Override
	public List<PraticaCollegata> getTemplateCollegati() {
		List<PraticaCollegata> result = new ArrayList<DatiPratica.PraticaCollegata>();
		for (PraticaCollegata pc : getDati().getPraticheCollegate())
			if (PraticaUtil.isModello(pc.getTipo()))
				result.add(pc);

		return result;
	}

	@Override
	public List<PraticaCollegata> getAllPraticheCollegate() {
		return new ArrayList<DatiPratica.PraticaCollegata>(getDati().getPraticheCollegate());
	}

	protected void loadAssegnazioneEsterna(Pratica jaxb, it.eng.consolepec.xmlplugin.factory.DatiPratica dati) {
		if (jaxb.getAssegnazioneEsterna() != null) {
			AssegnazioneEsterna ae = jaxb.getAssegnazioneEsterna();
			dati.setAssegnazioneEsterna(new it.eng.consolepec.xmlplugin.factory.DatiPratica.AssegnazioneEsterna(TranslatorBeanCondivisione.getListaOperazioni(ae.getOperazioni()),
					new TreeSet<String>(ae.getDestinatari()), ae.getTestoEmail(), DateUtils.xmlGrCalToDate(ae.getDataNotifica())));
		}
	}

	protected void loadNotifiche(Pratica jaxb, it.eng.consolepec.xmlplugin.factory.DatiPratica dati) {
		for (it.eng.consolepec.xmlplugin.jaxb.Notifica notifica : jaxb.getNotifiche()) {

			it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra parametriExtra = new it.eng.consolepec.xmlplugin.factory.DatiPratica.ParametriExtra();

			if (notifica.getParametriExtra().isSalvaEMLNotificaEmail() != null)
				parametriExtra.setSalvaEMLNotificaEmail(notifica.getParametriExtra().isSalvaEMLNotificaEmail());
			if (notifica.getParametriExtra().isCancellaAllegatiCaricati() != null)
				parametriExtra.setCancellaAllegatiCaricati(notifica.getParametriExtra().isCancellaAllegatiCaricati());
			parametriExtra.getIndirizziEmail().addAll(notifica.getParametriExtra().getIndirizziEmail());
			parametriExtra.setMittenteEmail(notifica.getParametriExtra().getMittenteEmail());
			for (Documento d : notifica.getParametriExtra().getAllegatiDaCaricare()) {
				parametriExtra.getAllegatiDaCaricare().add(XmlPluginUtil.getAllegatoFromDocumento(d, dati));
			}
			if (notifica.getParametriExtra().getIdTask() != null) {
				parametriExtra.setIdTask(notifica.getParametriExtra().getIdTask());
			}
			if (notifica.getParametriExtra().getOperazioneTask() != null) {
				parametriExtra.setOperazioneTask(notifica.getParametriExtra().getOperazioneTask());
			}
			if (notifica.getParametriExtra().getNoteTask() != null) {
				parametriExtra.setNoteTask(notifica.getParametriExtra().getNoteTask());
			}

			dati.getNotifiche().add(new it.eng.consolepec.xmlplugin.factory.DatiPratica.Notifica(notifica.getOperazione(), TipologiaNotifica.fromName(notifica.getTipologia()), parametriExtra));
		}
	}

	/* metodi privati */
}
