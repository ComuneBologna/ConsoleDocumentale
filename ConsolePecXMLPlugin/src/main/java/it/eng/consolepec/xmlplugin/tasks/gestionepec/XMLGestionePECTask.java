package it.eng.consolepec.xmlplugin.tasks.gestionepec;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.exception.OperazioneNonConsentita;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.XMLTask;
import it.eng.consolepec.xmlplugin.jaxb.GestionePEC;
import it.eng.consolepec.xmlplugin.jaxb.Task;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.DatiPraticaEmailTaskAdapter;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmail;
import it.eng.consolepec.xmlplugin.pratica.email.XMLPraticaEmail;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.ContestoEsecuzione;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask.Builder;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.RiassegnaTaskPECApi;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.AgganciaAFascicoloTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.CreaFascicoloTaskPECApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.impl.RiassegnaTaskPECApiImpl;

public abstract class XMLGestionePECTask extends XMLTask<DatiGestionePECTask> implements GestionePECTask {

	private DatiGestionePECTask dati = null;
	protected XMLPraticaEmail pratica;
	protected Logger logger = LoggerFactory.getLogger(XMLGestionePECTask.class);
	private final ContestoEsecuzione<GestionePECTask> contestoEsecuzione = new ContestoEsecuzione<GestionePECTask>();

	@SuppressWarnings("unchecked")
	@Override
	public <TS extends it.eng.consolepec.xmlplugin.factory.Task<?>> ContestoEsecuzione<TS> getContestoEsecuzione() {
		return (ContestoEsecuzione<TS>) contestoEsecuzione;
	}

	/* constructor */

	public XMLGestionePECTask() {

	}

	@Override
	public DatiGestionePECTask getDati() {

		return dati;
	}

	/* metodi pubblici di interfaccia */

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
	public PraticaEmail getEnclosingPratica() {
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

		if (!(pratica instanceof PraticaEmail) || t.getGestionePEC() == null) {
			throw new PraticaException("Il task di tipo GestionePEC supporta solo pratiche di tipo PraticaEmail e deve essere basato su Task GestionePEC");
		}
		this.pratica = (XMLPraticaEmail) pratica;

		DatiGestionePECTask.Builder builder = new Builder();
		Assegnatario asscorrente = assegnatarioFromJaxb(t.getAssegnatari().getAssegnatarioCorrente());
		builder.setAssegnatario(asscorrente);
		dati = builder.construct();
		super.loadDatiTaskGenerico(t, dati);

		if (t.isAttivo()) {
			pratica.getDati().setAssegnatarioCorrente(asscorrente.getNome());
		}

		initApiTask();
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica praticajaxb) {

		try {
			Task out = new Task();
			DatiGestionePECTask dati = getDati();

			super.serializeDatiTaskGenerico(out, dati, praticajaxb);
			out.setGestionePEC(new GestionePEC());
			praticajaxb.getTasks().getTask().add(out);

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione task gestionePEC");
		}
	}

	@Override
	protected void initTask(Pratica<?> pratica, DatiGestionePECTask dati) {

		if (!(pratica instanceof XMLPraticaEmail)) {
			throw new PraticaException("Il task di tipo GestionePEC supporta solo pratiche di tipo XMLPraticaEmail");
		}
		this.pratica = (XMLPraticaEmail) pratica;
		this.dati = dati;
		dati.setId(getNextId(pratica));
		dati.setAttivo(true);
		pratica.getDati().setAssegnatarioCorrente(dati.getAssegnatario().getNome());
		initApiTask();
	}

	protected void initApiTask() {
		operazioni.put(TipoApiTaskPEC.RIASSEGNA, new RiassegnaTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.CREA_FASCICOLO, new CreaFascicoloTaskPECApiImpl<DatiGestionePECTask>(this));
		operazioni.put(TipoApiTaskPEC.AGGANCIA_A_FASCICOLO, new AgganciaAFascicoloTaskPECApiImpl<DatiGestionePECTask>(this));

	}

	@Override
	protected void checkAbilitazione(ITipoApiTask tipoApiTask) {
		if (operazioni.get(tipoApiTask) == null || !operazioni.get(tipoApiTask).isOperazioneAbilitata()) {
			throw new OperazioneNonConsentita(tipoApiTask);
		}
	}

	@Override
	public void riattivaConCheck() {
		if (pratica.isRiattivabile()) {
			riattiva();
		} else {
			throw new PraticaException("La mail non puo' essere riattivata.");
		}
	}

	private void riattiva() {
		pratica.getDati().setStato(Stato.IN_GESTIONE);
		getDati().setAttivo(true);
		getDati().getAssegnatario().setDataFine(null);
	}

	@Override
	public void setEmailId(String mailID) {
		if (getEnclosingPratica().getDati().getMessageID() != null) {
			throw new PraticaException("La mail ha gia' un id associato: " + getEnclosingPratica().getDati().getMessageID());
		}

		((DatiPraticaEmailTaskAdapter) pratica.getDatiPraticaTaskAdapter()).setMessageId(mailID);

	}

	@Override
	public boolean isOperazioneAbilitata() {
		return false;
	}

	@Override
	public void riassegna(AnagraficaRuolo nuovoAssegnatario, List<GruppoVisibilita> gruppiVisibilita, String operatore, List<String> indirizziNotifica) {
		checkAbilitazione(TipoApiTaskPEC.RIASSEGNA);
		((RiassegnaTaskPECApi) operazioni.get(TipoApiTaskPEC.RIASSEGNA)).riassegna(nuovoAssegnatario, gruppiVisibilita, operatore, indirizziNotifica);
	}

	public void terminaSenzaRiattivazione() {
		getDati().setAttivo(false);
		getDati().getAssegnatario().setDataFine(new Date());
	}

	/* metodi privati */
	/* metodi statici */
}
