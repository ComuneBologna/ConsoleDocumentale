package it.eng.consolepec.xmlplugin.tasks.riattiva;

import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.DatiTask.TipoTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.XMLPratica;
import it.eng.consolepec.xmlplugin.factory.XMLTask;
import it.eng.consolepec.xmlplugin.jaxb.RiportaInGestione;
import it.eng.consolepec.xmlplugin.jaxb.Task;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.ContestoEsecuzione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.riattiva.DatiRiattivazioneTask.Builder;

public abstract class XMLRiattivaTask extends XMLTask<DatiRiattivazioneTask> implements RiattivaTask {

	private DatiRiattivazioneTask dati = null;
	private String currentUser = null;
	protected XMLPratica<?> pratica;
	protected Logger logger = LoggerFactory.getLogger(XMLRiattivaTask.class);
	private final ContestoEsecuzione<RiattivaTask> contestoEsecuzione = new ContestoEsecuzione<RiattivaTask>();

	@SuppressWarnings("unchecked")
	@Override
	public <TS extends it.eng.consolepec.xmlplugin.factory.Task<?>> ContestoEsecuzione<TS> getContestoEsecuzione() {
		return (ContestoEsecuzione<TS>) contestoEsecuzione;
	}

	@Override
	public DatiRiattivazioneTask getDati() {
		return dati;
	}

	@Override
	public Map<String, Object> getMetadata() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("pDataInizio", getDati().getAssegnatario().getDataInizio());
		map.put("pDataFine", getDati().getAssegnatario().getDataFine());

		return map;
	}

	@Override
	public boolean isAttivo() {
		return getDati().getAttivo();
	}

	@Override
	public Pratica<?> getEnclosingPratica() {
		return pratica;
	}

	@Override
	public void termina() {
		getDati().setAttivo(false);
		getDati().getAssegnatario().setDataFine(new Date());
	}

	@Override
	public TipoTask getTipo() {
		return TipoTask.RiportaInLavorazioneTask;
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica praticajaxb) {
		try {
			Task out = new Task();
			DatiRiattivazioneTask dati = getDati();

			super.serializeDatiTaskGenerico(out, dati, praticajaxb);
			RiportaInGestione riportaInGestione = new RiportaInGestione();
			riportaInGestione.setIdTaskDaRiattivare(new BigInteger("" + dati.getIdTaskDaRiattivare()));
			out.setRiportaInGestione(riportaInGestione);
			praticajaxb.getTasks().getTask().add(out);

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione task riportaInGestioneTask");
		}

	}

	@Override
	protected void loadDatiTask(Pratica<?> pratica, Task t) {
		if (t.getRiportaInGestione() == null) {
			throw new PraticaException("Il task di tipo riporta in gestione non è presente.");
		}
		this.pratica = (XMLPratica<?>) pratica;

		DatiRiattivazioneTask.Builder builder = new Builder();
		Assegnatario asscorrente = assegnatarioFromJaxb(t.getAssegnatari().getAssegnatarioCorrente());
		builder.setAssegnatario(asscorrente);
		dati = builder.construct();
		super.loadDatiTaskGenerico(t, dati);

		if (t.isAttivo()) {
			pratica.getDati().setAssegnatarioCorrente(asscorrente.getNome());
		}

		dati.setIdTaskDaRiattivare(t.getRiportaInGestione().getIdTaskDaRiattivare().intValue());

	}

	@Override
	protected void initTask(Pratica<?> pratica, DatiRiattivazioneTask dati) {
		this.pratica = (XMLPratica<?>) pratica;
		this.dati = dati;
		dati.setId(getNextId(pratica));
		dati.setAttivo(true);
		pratica.getDati().setAssegnatarioCorrente(dati.getAssegnatario().getNome());
	}

	@Override
	public void riattivaConCheck() {
		throw new UnsupportedOperationException();
	}

	/* metodi object */

	@Override
	public int hashCode() {

		return dati.getId().hashCode();
	}

	@Override
	public String toString() {

		return dati.toString();
	};

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RiattivaTask) {
			RiattivaTask task2 = (RiattivaTask) obj;
			return task2.getDati().getId().equals(getDati().getId());
		}
		return false;
	}

	@Override
	public void riattivaTaskAssociato() {
		Set<it.eng.consolepec.xmlplugin.factory.Task<?>> tasks = pratica.getTasks();
		Integer idTaskDaRiattivare = null;
		for (it.eng.consolepec.xmlplugin.factory.Task<?> task : tasks) {
			if (task instanceof RiattivaTask && task.isAttivo()) {
				idTaskDaRiattivare = ((RiattivaTask) task).getDati().getIdTaskDaRiattivare();
			}
		}

		for (it.eng.consolepec.xmlplugin.factory.Task<?> task : tasks) {
			if (task.getDati().getId().equals(idTaskDaRiattivare)) {
				if (task instanceof TaskFascicolo<?>) {
					((TaskFascicolo<?>) task).setCurrentUser(currentUser);
				}
				task.riattivaConCheck();
			}
		}

		for (it.eng.consolepec.xmlplugin.factory.Task<?> task : tasks) {
			if (task instanceof RiattivaTask && task.isAttivo()) {
				((XMLTask<?>) task).termina();
			}
		}
	}

	@Override
	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}
}
