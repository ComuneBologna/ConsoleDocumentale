package it.eng.consolepec.xmlplugin.factory;

import java.math.BigInteger;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.consolepec.xmlplugin.exception.OperazioneNonConsentita;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.jaxb.AssegnatarioTask;
import it.eng.consolepec.xmlplugin.jaxb.Pratica;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;

/**
 * Metodi di servizio che deve implementare ogni task. Ogni classe concreta deve ridefinire opportunamente equals e hashCode
 *
 * @author pluttero
 * @param <T>
 *
 */
public abstract class XMLTask<T extends DatiTask> implements Task<T> {

	private static Logger logger = LoggerFactory.getLogger(XMLTask.class);

	protected HashMap<ITipoApiTask, ITaskApi> operazioni = new HashMap<ITipoApiTask, ITaskApi>();
	private Set<String> operazioniAbilitate = new HashSet<String>();
	private String currentUser;
	private boolean utenteEsterno;

	/* metodi protected */

	protected abstract void serializeDati(Pratica praticajaxb);

	protected abstract void loadDatiTask(it.eng.consolepec.xmlplugin.factory.Pratica<?> pratica, it.eng.consolepec.xmlplugin.jaxb.Task t);

	@Override
	public boolean controllaAbilitazione(ITipoApiTask tipoApiTask) {
		ITaskApi iTaskApi = operazioni.get(tipoApiTask);
		if (iTaskApi != null) {
			return iTaskApi.isOperazioneAbilitata();
		}

		return false;
	}

	@Override
	public void setOperazioniAbilitate(Set<String> operazioniAbilitate) {
		this.operazioniAbilitate = operazioniAbilitate;
	}

	@Override
	public Set<String> getOperazioniAbilitate() {
		return operazioniAbilitate;
	}

	protected void checkAbilitazione(ITipoApiTask tipoApiTask) {
		if (operazioni.get(tipoApiTask) == null || !operazioni.get(tipoApiTask).isOperazioneAbilitata()) {
			throw new OperazioneNonConsentita(tipoApiTask);
		}
	}

	/**
	 * Invocato all'atto della prima creazione del task
	 *
	 * @param pratica
	 * @param bean
	 */
	protected abstract void initTask(it.eng.consolepec.xmlplugin.factory.Pratica<?> pratica, T bean);

	protected static int getNextId(it.eng.consolepec.xmlplugin.factory.Pratica<? extends DatiPratica> pratica) {
		Integer id = 0;
		for (it.eng.consolepec.xmlplugin.factory.Task<? extends DatiTask> t : pratica.getTasks()) {
			id = id > t.getDati().getId() ? id : t.getDati().getId();
		}
		id++;
		logger.debug("Prossimo task id per pratica {}: {}", pratica.getDati().getFolderPath(), id);
		return id;
	}

	protected void loadDatiTaskGenerico(it.eng.consolepec.xmlplugin.jaxb.Task t, DatiTask dati) {
		logger.debug("Popolamento dati task generico");
		for (AssegnatarioTask at : t.getAssegnatari().getAssegnatarioPassato()) {
			dati.getAssegnatariPassati().add(assegnatarioFromJaxb(at));
		}
		dati.setAttivo(t.isAttivo());
		dati.setId(t.getId() != null ? t.getId().intValue() : null);
		dati.setStato(t.getStato());
	}

	protected static void serializeDatiTaskGenerico(it.eng.consolepec.xmlplugin.jaxb.Task out, DatiTask dati, Pratica pratica) {
		logger.debug("Serializzazione dati task generico");
		try {
			out.setId(BigInteger.valueOf(dati.getId()));
			out.setAttivo(dati.getAttivo());
			out.setAssegnatari(new it.eng.consolepec.xmlplugin.jaxb.Task.Assegnatari());
			// assegnatario corrente
			out.getAssegnatari().setAssegnatarioCorrente(assegnatarioToJaxb(dati.getAssegnatario()));

			if (pratica != null && out.isAttivo()) {
				pratica.setAssegnatarioCorrente(out.getAssegnatari().getAssegnatarioCorrente().getNome());
			}

			// assegnatari passati
			for (Assegnatario assegnatario : dati.getAssegnatariPassati()) {
				out.getAssegnatari().getAssegnatarioPassato().add(assegnatarioToJaxb(assegnatario));
			}

			out.setStato(dati.getStato());

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore nel serializeDatiTaskGenerico");

		}
	}

	protected static Assegnatario assegnatarioFromJaxb(AssegnatarioTask a) {
		GregorianCalendar cal = null;
		Date dataInizio = null;
		Date dataFine = null;

		/* de-serializzazione */
		if (a.getDataFine() != null) {
			cal = a.getDataFine().toGregorianCalendar();
			dataFine = cal.getTime();
		}
		if (a.getDataInizio() != null) {
			cal = a.getDataInizio().toGregorianCalendar();
			dataInizio = cal.getTime();
		}
		return new Assegnatario(a.getNome(), a.getEtichetta(), dataInizio, dataFine);
	}

	protected static AssegnatarioTask assegnatarioToJaxb(Assegnatario a) throws DatatypeConfigurationException {
		AssegnatarioTask at = new AssegnatarioTask();
		GregorianCalendar cal = new GregorianCalendar();
		if (a.getDataInizio() != null) {
			cal.setTimeInMillis(a.getDataInizio().getTime());
			at.setDataInizio(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
		}
		if (a.getDataFine() != null) {
			cal.setTimeInMillis(a.getDataFine().getTime());
			at.setDataFine(DatatypeFactory.newInstance().newXMLGregorianCalendar(cal));
		}
		at.setNome(a.getNome());
		at.setEtichetta(a.getEtichetta());
		return at;

	}

	public abstract void termina();

	@Override
	public void prendiInCarico(Utente user) {
		Utente inCaricoA = getEnclosingPratica().getDati().getInCaricoA();
		if (inCaricoA == null || inCaricoA.getUsername().equals(user.getUsername())) {
			user.setDataPresaInCarico(new Date());
			getEnclosingPratica().getDati().setInCaricoA(user);
		} else {
			throw new PraticaException("La pratica è già in uso all'utente: " + getEnclosingPratica().getDati().getInCaricoA());
		}

	}

	@Override
	public void rilasciaInCarico(Utente user) {
		Utente inCaricoA = getEnclosingPratica().getDati().getInCaricoA();
		if (inCaricoA != null && inCaricoA.getUsername().equals(user.getUsername())) {
			getEnclosingPratica().getDati().setInCaricoA(null);
		} else {
			throw new PraticaException("Errore nel rilascio della pratica");
		}
	}

	@Override
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	@Override
	public String getCurrentUser() {
		return this.currentUser;
	}

	@Override
	public boolean isUtenteEsterno() {
		return utenteEsterno;
	}

	@Override
	public void setUtenteEsterno(boolean utenteEsterno) {
		this.utenteEsterno = utenteEsterno;
	}
}
