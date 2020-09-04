package it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.DatiTask.TipoTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.factory.XMLTask;
import it.eng.consolepec.xmlplugin.jaxb.GestioneComunicazione;
import it.eng.consolepec.xmlplugin.jaxb.Task;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.Comunicazione;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.XMLComunicazione;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.ContestoEsecuzione;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.AggiungiAllegatoComunicazioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.InviaComunicazioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.RimuoviAllegatoComunicazioneTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.TipoApiTaskComunicazione;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.impl.AggiungiAllegatoComunicazioneTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.impl.CreaComunicazionePerCopiaTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.impl.InviaComunicazioneTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.impl.RimuoviAllegatoComunicazioneTaskApiImpl;

public class XMLGestioneComunicazioneTask extends XMLTask<DatiGestioneComunicazioneTask> implements GestioneComunicazioneTask {

	private DatiGestioneComunicazioneTask dati = null;
	protected Logger logger = LoggerFactory.getLogger(XMLGestioneComunicazioneTask.class);
	protected XMLComunicazione pratica;
	private final ContestoEsecuzione<GestioneComunicazioneTask> contestoEsecuzione = new ContestoEsecuzione<GestioneComunicazioneTask>();

	@SuppressWarnings("unchecked")
	@Override
	public <TS extends it.eng.consolepec.xmlplugin.factory.Task<?>> ContestoEsecuzione<TS> getContestoEsecuzione() {
		return (ContestoEsecuzione<TS>) contestoEsecuzione;
	}

	/* constructor */

	public XMLGestioneComunicazioneTask() {

	}

	@Override
	public DatiGestioneComunicazioneTask getDati() {
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
	public Comunicazione getEnclosingPratica() {

		return pratica;
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

	/* metodi protected da XMLTask */

	@Override
	protected void loadDatiTask(Pratica<?> pratica, Task t) {

		if (!(pratica instanceof XMLComunicazione) || t.getGestioneComunicazione() == null)
			throw new PraticaException("Il task di tipo GestioneComunicazione supporta solo pratiche di tipo Comunicazione e deve essere basato su Task GestioneComunicazione");
		this.pratica = (XMLComunicazione) pratica;

		DatiGestioneComunicazioneTask.Builder builder = new DatiGestioneComunicazioneTask.Builder();
		Assegnatario asscorrente = assegnatarioFromJaxb(t.getAssegnatari().getAssegnatarioCorrente());
		pratica.getDati().setAssegnatarioCorrente(asscorrente.getNome());

		builder.setAssegnatario(asscorrente);
		builder.setAttivo(true);
		dati = builder.construct();

		initApiTask();

		super.loadDatiTaskGenerico(t, dati);
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica praticajaxb) {

		try {
			Task out = new Task();
			DatiGestioneComunicazioneTask dati = getDati();

			super.serializeDatiTaskGenerico(out, dati, praticajaxb);
			out.setGestioneComunicazione(new GestioneComunicazione());
			praticajaxb.getTasks().getTask().add(out);

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione task GestioneComunciazione");
		}
	}

	@Override
	protected void initTask(Pratica<?> pratica, DatiGestioneComunicazioneTask dati) {

		if (!(pratica instanceof XMLComunicazione))
			throw new PraticaException("Il task di tipo GestioneComunciazione supporta solo pratiche di tipo XMLComunicazione");
		this.pratica = (XMLComunicazione) pratica;
		this.dati = dati;
		dati.setId(getNextId(pratica));
		dati.setAttivo(true);
		pratica.getDati().setAssegnatarioCorrente(dati.getAssegnatario().getNome());
	}

	@Override
	public void riattivaConCheck() {
		if (pratica.isRiattivabile()) {
			riattiva();
		} else {
			throw new PraticaException("La comunicazione non puo' essere riattivato.");
		}
	}

	private void riattiva() {
		getDati().setAttivo(true);
		getDati().getAssegnatario().setDataFine(null);
	}

	@Override
	public void termina() {
		getDati().setAttivo(false);
		getDati().getAssegnatario().setDataFine(new Date());
	}

	@Override
	public TipoTask getTipo() {
		return TipoTask.GestioneComunicazioneTask;
	}

	@Override
	public boolean isOperazioneAbilitata() {
		return false;
	}

	protected void initApiTask() {

		operazioni.put(TipoApiTaskComunicazione.AGGIUNGI_ALLEGATO, new AggiungiAllegatoComunicazioneTaskApiImpl<DatiGestioneComunicazioneTask>(this));
		operazioni.put(TipoApiTaskComunicazione.RIMUOVI_ALLEGATO, new RimuoviAllegatoComunicazioneTaskApiImpl<DatiGestioneComunicazioneTask>(this));

		operazioni.put(TipoApiTaskComunicazione.INVIA_COMUNICAZIONE, new InviaComunicazioneTaskApiImpl<DatiGestioneComunicazioneTask>(this));
		operazioni.put(TipoApiTaskComunicazione.CREA_COMUNICAZIONE_PER_COPIA, new CreaComunicazionePerCopiaTaskApiImpl<DatiGestioneComunicazioneTask>(this));

	}

	@Override
	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		checkAbilitazione(TipoApiTaskComunicazione.AGGIUNGI_ALLEGATO);
		((AggiungiAllegatoComunicazioneTaskApi) operazioni.get(TipoApiTaskComunicazione.AGGIUNGI_ALLEGATO)).aggiungiAllegato(allegato, handler);
	}

	@Override
	public void rimuoviAllegato(Allegato allegato) {
		checkAbilitazione(TipoApiTaskComunicazione.RIMUOVI_ALLEGATO);
		((RimuoviAllegatoComunicazioneTaskApi) operazioni.get(TipoApiTaskComunicazione.RIMUOVI_ALLEGATO)).rimuoviAllegato(allegato);
	}

	@Override
	public void inviata() {
		checkAbilitazione(TipoApiTaskComunicazione.INVIA_COMUNICAZIONE);
		((InviaComunicazioneTaskApi) operazioni.get(TipoApiTaskComunicazione.INVIA_COMUNICAZIONE)).inviata();
	}

	@Override
	public void inviataTest() {
		checkAbilitazione(TipoApiTaskComunicazione.INVIA_COMUNICAZIONE);
		((InviaComunicazioneTaskApi) operazioni.get(TipoApiTaskComunicazione.INVIA_COMUNICAZIONE)).inviataTest();
	}

}
