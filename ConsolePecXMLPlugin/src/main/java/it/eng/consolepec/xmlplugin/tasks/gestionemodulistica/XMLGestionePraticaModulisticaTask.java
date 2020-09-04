package it.eng.consolepec.xmlplugin.tasks.gestionemodulistica;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.DatiTask.TipoTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.factory.XMLTask;
import it.eng.consolepec.xmlplugin.jaxb.GestioneModulistica;
import it.eng.consolepec.xmlplugin.jaxb.Task;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Stato;
import it.eng.consolepec.xmlplugin.pratica.modulistica.XMLPraticaModulistica;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.ContestoEsecuzione;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class XMLGestionePraticaModulisticaTask extends XMLTask<DatiGestionePraticaModulisticaTask> implements GestionePraticaModulisticaTask {

	private DatiGestionePraticaModulisticaTask dati = null;
	protected Logger logger = LoggerFactory.getLogger(XMLGestionePraticaModulisticaTask.class);
	protected XMLPraticaModulistica pratica;

	private final ContestoEsecuzione<GestionePraticaModulisticaTask> contestoEsecuzione = new ContestoEsecuzione<GestionePraticaModulisticaTask>();

	@SuppressWarnings("unchecked")
	@Override
	public <TS extends it.eng.consolepec.xmlplugin.factory.Task<?>> ContestoEsecuzione<TS> getContestoEsecuzione() {
		return (ContestoEsecuzione<TS>) contestoEsecuzione;
	}

	/* constructor */

	public XMLGestionePraticaModulisticaTask() {

	}

	@Override
	public DatiGestionePraticaModulisticaTask getDati() {
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
	public void archivia() {
		if (isArchiviaAbilitato()) {
			termina();
			pratica.getDati().setStato(Stato.ARCHIVIATA);
		} else
			throw new PraticaException("Operazione non consentita");
	}

	@Override
	public boolean isArchiviaAbilitato() {
		return pratica.getDati().getStato().name().equals(Stato.IN_GESTIONE.name());
	}

	@Override
	public boolean isEliminaAbilitato() {
		if (!pratica.hasPraticheCollegate()) {
			return pratica.getDati().getStato().name().equals(Stato.ARCHIVIATA.name()) || pratica.getDati().getStato().name().equals(Stato.IN_GESTIONE.name());
		}
		return false;
	}

	@Override
	public boolean isAttivo() {

		return getDati().getAttivo();
	}

	@Override
	public Pratica<?> getEnclosingPratica() {

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

		if (!(pratica instanceof XMLPraticaModulistica) || t.getGestioneModulistica() == null)
			throw new PraticaException("Il task di tipo GestioneModulistica supporta solo pratiche di tipo Modulistica e deve essere basato su Task GestioneModulistica");
		this.pratica = (XMLPraticaModulistica) pratica;

		DatiGestionePraticaModulisticaTask.Builder builder = new DatiGestionePraticaModulisticaTask.Builder();
		Assegnatario asscorrente = assegnatarioFromJaxb(t.getAssegnatari().getAssegnatarioCorrente());
		pratica.getDati().setAssegnatarioCorrente(asscorrente.getNome());

		builder.setAssegnatario(asscorrente);
		builder.setAttivo(true);
		dati = builder.construct();

		super.loadDatiTaskGenerico(t, dati);
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica praticajaxb) {

		try {
			Task out = new Task();
			DatiGestionePraticaModulisticaTask dati = getDati();

			super.serializeDatiTaskGenerico(out, dati, praticajaxb);
			out.setGestioneModulistica(new GestioneModulistica());
			praticajaxb.getTasks().getTask().add(out);

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione task gestionePEC");
		}
	}

	@Override
	protected void initTask(Pratica<?> pratica, DatiGestionePraticaModulisticaTask dati) {

		if (!(pratica instanceof XMLPraticaModulistica))
			throw new PraticaException("Il task di tipo GestioneModulistica supporta solo pratiche di tipo XMLPraticaModulistica");
		this.pratica = (XMLPraticaModulistica) pratica;
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
			throw new PraticaException("Il modulo non puo' essere riattivato.");
		}
	}

	private void riattiva() {
		pratica.getDatiPraticaTaskAdapter().setStato(Stato.IN_GESTIONE);
		getDati().setAttivo(true);
		getDati().getAssegnatario().setDataFine(null);
	}

	@Override
	public void termina() {
		getDati().setAttivo(false);
		getDati().getAssegnatario().setDataFine(new Date());
	}

	@Override
	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		String nomeAllegato = null;
		boolean isProtocollato = XmlPluginUtil.isAllegatoProtocollato(allegato, getEnclosingPratica());
		Allegato aggiungiAllegato = null;
		if (isProtocollato) {
			nomeAllegato = XmlPluginUtil.getNewNomeAllegatoProtocollatoFascicolo(allegato, getEnclosingPratica());
			aggiungiAllegato = handler.aggiungiAllegato(new StringBuilder().append(pratica.getDati().getFolderPath()).append("/").append(getEnclosingPratica().getSubFolderPath()).toString(),
					nomeAllegato);
			pratica.getDati().getAllegati().add(aggiungiAllegato);
		} else {
			nomeAllegato = allegato.getNome();
			aggiungiAllegato = handler.aggiungiAllegato(new StringBuilder().append(pratica.getDati().getFolderPath()).append("/").append(getEnclosingPratica().getSubFolderPath()).toString(),
					nomeAllegato);
			if (XmlPluginUtil.allegatoInPratica(aggiungiAllegato, getEnclosingPratica()).size() != 0)
				pratica.getDati().getAllegati().removeAll(XmlPluginUtil.allegatoInPratica(aggiungiAllegato, getEnclosingPratica()));
			pratica.getDati().getAllegati().add(aggiungiAllegato);
		}

	}

	@Override
	public boolean isRiassegnaAbilitato() {
		return pratica.getDati().getStato().name().equals(Stato.IN_GESTIONE.name());
	}

	@Override
	public TipoTask getTipo() {
		return TipoTask.GestionePraticaModulisticaTask;
	}

	@Override
	public boolean isCreaFascicoloAbilitato() {
		return !getEnclosingPratica().hasPraticheCollegate() && pratica.getDati().getStato().name().equals(Stato.IN_GESTIONE.name());
	}

	@Override
	public void riassegna(AnagraficaRuolo nuovoAssegnatario, String operatore) {
		if (nuovoAssegnatario == null)
			throw new PraticaException("Specificare un assegnatario valido");
		Assegnatario corrente = getDati().getAssegnatario();
		corrente.setDataFine(new Date());
		Date dataFine = corrente.getDataFine();
		getDati().getAssegnatariPassati().add(corrente);
		getEnclosingPratica().getDati().setLetto(false);
		getEnclosingPratica().getDati().getGruppiVisibilita().add(new GruppoVisibilita(nuovoAssegnatario.getRuolo()));
		getDati().setAssegnatario(new Assegnatario(nuovoAssegnatario.getRuolo(), nuovoAssegnatario.getEtichetta(), new Date(), dataFine));

		if (!GenericsUtil.isNullOrEmpty(operatore)) {
			getEnclosingPratica().getDati().getOperatore().setNome(operatore);
		}
	}

	@Override
	public boolean isAggiungiFascicoloAbilitato() {
		return !getEnclosingPratica().hasPraticheCollegate();
	}
}
