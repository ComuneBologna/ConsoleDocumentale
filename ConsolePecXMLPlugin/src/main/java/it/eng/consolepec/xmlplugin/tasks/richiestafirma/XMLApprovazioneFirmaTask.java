package it.eng.consolepec.xmlplugin.tasks.richiestafirma;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.util.date.DateUtils;
import it.eng.consolepec.xmlplugin.exception.OperazioneNonConsentita;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.DatiTask.TipoTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.factory.XMLTask;
import it.eng.consolepec.xmlplugin.jaxb.RichiestaFirma;
import it.eng.consolepec.xmlplugin.jaxb.Task;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.ContestoEsecuzione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.InvalidaApprovazioneFirmaTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.AggiungiAllegatoApiTaskImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.EvadiApprovazioneFirmaTaskApiImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.FirmaAllegatiApiTaskImpl;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.VersionaAllegatoTaskFirmaApiTaskImpl;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.DestinatarioRichiestaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.RiferimentoAllegatoApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoPropostaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.DatiApprovazioneFirmaTask.TipoRispostaApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni.impl.GestioneApprovazioneFirmaApiTaskImpl;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.operazioni.impl.RiassegnaPraticaApprovazioneFirmaTaskApiImpl;
import it.eng.consolepec.xmlplugin.util.TaskDiFirmaUtil;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class XMLApprovazioneFirmaTask extends XMLTask<DatiApprovazioneFirmaTask> implements ApprovazioneFirmaTask {

	private final Logger logger = LoggerFactory.getLogger(XMLApprovazioneFirmaTask.class);

	private DatiApprovazioneFirmaTask dati;
	private Pratica<?> pratica;
	private final ContestoEsecuzione<ApprovazioneFirmaTask> contestoEsecuzione = new ContestoEsecuzione<ApprovazioneFirmaTask>();

	@SuppressWarnings("unchecked")
	@Override
	public <TS extends it.eng.consolepec.xmlplugin.factory.Task<?>> ContestoEsecuzione<TS> getContestoEsecuzione() {
		return (ContestoEsecuzione<TS>) contestoEsecuzione;
	}

	@Override
	public DatiApprovazioneFirmaTask getDati() {
		return dati;
	}

	@Override
	public Map<String, Object> getMetadata() {
		logger.warn("getMetadata() Non implementato");
		Map<String, Object> map = new HashMap<String, Object>();
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
	public TipoTask getTipo() {
		return TipoTask.RichiestaFirmaTask;
	}

	@Override
	public void riattivaConCheck() {
		if (pratica.isRiattivabile()) {
			riattiva();
		} else {
			throw new PraticaException("La pratica non puo' essere riattivata.");
		}
	}

	private void riattiva() {
		getDati().setAttivo(true);
		getDati().getAssegnatario().setDataFine(null);
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica praticajaxb) {

		try {
			Task out = new Task();
			DatiApprovazioneFirmaTask dati = getDati();

			super.serializeDatiTaskGenerico(out, dati, null);

			RichiestaFirma richiestaFirma = new RichiestaFirma();
			richiestaFirma.setDataCreazione(DateUtils.dateToXMLGrCal(dati.getDataCreazione()));
			it.eng.consolepec.xmlplugin.jaxb.RiferimentoAllegatoJaxb ref = new it.eng.consolepec.xmlplugin.jaxb.RiferimentoAllegatoJaxb();
			ref.setNome(dati.getRiferimentoAllegato().getNome());
			ref.setCurrentVersion(dati.getRiferimentoAllegato().getCurrentVersion());
			richiestaFirma.setRiferimentoAllegato(ref);
			richiestaFirma.setTipo(dati.getTipo().name());

			for (DestinatarioRichiestaApprovazioneFirmaTask d : dati.getDestinatari()) {
				richiestaFirma.getDestinatari().add(TaskDiFirmaUtil.convertDestinatarioToJaxb(d));
			}

			if (dati.getDataScadenza() != null) richiestaFirma.setDataScadenza(DateUtils.dateToXMLGrCal(dati.getDataScadenza()));

			richiestaFirma.setMittenteOriginale(dati.getMittenteOriginale());
			richiestaFirma.setValido(dati.getValido());
			out.setRichiestaFirma(richiestaFirma);
			praticajaxb.getTasks().getTask().add(out);

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione task RichiestaFirma");
		}

	}

	@Override
	protected void loadDatiTask(Pratica<?> pratica, Task t) {

		if (t.getRichiestaFirma() == null) throw new PraticaException("Il task di tipo RichiestaFirma deve essere basato su Task RichiestaFirma");
		this.pratica = pratica;

		DatiApprovazioneFirmaTask.Builder builder = new DatiApprovazioneFirmaTask.Builder();
		Assegnatario asscorrente = assegnatarioFromJaxb(t.getAssegnatari().getAssegnatarioCorrente());
		RichiestaFirma richiestaFirma = t.getRichiestaFirma();
		builder.setDataCreazione(DateUtils.xmlGrCalToDate(richiestaFirma.getDataCreazione()));
		TreeSet<DestinatarioRichiestaApprovazioneFirmaTask> destinatari = new TreeSet<DestinatarioRichiestaApprovazioneFirmaTask>();
		for (it.eng.consolepec.xmlplugin.jaxb.DestinatarioRichiestaFirmaJaxb d : richiestaFirma.getDestinatari()) {
			destinatari.add(TaskDiFirmaUtil.convertDestinatarioFromJaxb(d));
		}
		builder.setDestinatari(destinatari);
		it.eng.consolepec.xmlplugin.jaxb.RiferimentoAllegatoJaxb rifAllegato = richiestaFirma.getRiferimentoAllegato();
		builder.setRiferimentoAllegato(new RiferimentoAllegatoApprovazioneFirmaTask(rifAllegato.getNome(), rifAllegato.getCurrentVersion()));
		builder.setTipo(TipoPropostaApprovazioneFirmaTask.fromValue(richiestaFirma.getTipo()));
		builder.setAssegnatario(asscorrente);
		builder.setAttivo(true);

		builder.setMittenteOriginale(richiestaFirma.getMittenteOriginale());
		if (richiestaFirma.getDataScadenza() != null) builder.setDataScadenza(DateUtils.xmlGrCalToDate(richiestaFirma.getDataScadenza()));

		builder.setValido(richiestaFirma.isValido());

		dati = builder.construct();
		super.loadDatiTaskGenerico(t, dati);
	}

	@Override
	protected void initTask(Pratica<?> pratica, DatiApprovazioneFirmaTask bean) {

		this.pratica = pratica;
		this.dati = bean;
		dati.setId(getNextId(pratica));
		dati.setAttivo(true);
	}

	@Override
	public void termina() {
		getDati().setAttivo(false);
		getDati().getAssegnatario().setDataFine(new Date());
	}

	/* metodi object */

	@Override
	public int hashCode() {

		return dati.getId().hashCode();
	}

	@Override
	public String toString() {

		return dati.toString();
	}

	@Override
	public void firmaAllegati(List<Allegato> allegati) throws PraticaException {

		XMLTaskFascicolo<DatiGestioneFascicoloTask> gestioneTask = XmlPluginUtil.getGestioneFasicoloTaskCorrente(pratica, getCurrentUser());
		FirmaAllegatiApiTaskImpl<DatiGestioneFascicoloTask> t = new FirmaAllegatiApiTaskImpl<DatiGestioneFascicoloTask>(gestioneTask);
		if (t.isOperazioneAbilitata()) t.firmaAllegati(allegati);
		else throw new OperazioneNonConsentita(TipoApiTask.FIRMA);
	}

	@Override
	public boolean isOperazioneAbilitata() {
		return isAttivo();
	}

	@Override
	public void riassegna(AnagraficaRuolo nuovoAssegnatario, List<GruppoVisibilita> gruppiVisibilita, List<Pratica<?>> praticheCollegate, String operatore,
			List<String> indirizziNotifica) throws PraticaException {

		XMLTaskFascicolo<DatiGestioneFascicoloTask> gestioneTask = XmlPluginUtil.getGestioneFasicoloTaskCorrente(pratica, getCurrentUser());
		RiassegnaPraticaApprovazioneFirmaTaskApiImpl t = new RiassegnaPraticaApprovazioneFirmaTaskApiImpl(gestioneTask);
		if (t.isOperazioneAbilitata()) t.riassegna(nuovoAssegnatario, gruppiVisibilita, praticheCollegate, operatore, indirizziNotifica);

		else throw new OperazioneNonConsentita(TipoApiTask.RIASSEGNA);
	}

	@Override
	public void firma(Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione) {
		GestioneApprovazioneFirmaApiTaskImpl t = new GestioneApprovazioneFirmaApiTaskImpl(this);
		t.firma(a, userID, ruolo, note, destinatariNotifica, motivazione);
	}

	@Override
	public void approva(Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione) {
		GestioneApprovazioneFirmaApiTaskImpl t = new GestioneApprovazioneFirmaApiTaskImpl(this);
		t.approva(a, userID, ruolo, note, destinatariNotifica, motivazione);
	}

	@Override
	public void diniega(Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione) {
		GestioneApprovazioneFirmaApiTaskImpl t = new GestioneApprovazioneFirmaApiTaskImpl(this);
		t.diniega(a, userID, ruolo, note, destinatariNotifica, motivazione);
	}

	@Override
	public void rispondi(TipoRispostaApprovazioneFirmaTask tipoRisposta, Allegato a, String userID, String ruolo, String note, List<String> destinatariNotifica, String motivazione) {
		GestioneApprovazioneFirmaApiTaskImpl t = new GestioneApprovazioneFirmaApiTaskImpl(this);
		t.rispondi(tipoRisposta, a, userID, ruolo, note, destinatariNotifica, motivazione);
	}

	@Override
	public void versionaAllegato(Allegato allegato, boolean protocollato, AggiungiAllegato handler) throws Exception {
		XMLTaskFascicolo<DatiGestioneFascicoloTask> gestioneTask = XmlPluginUtil.getGestioneFasicoloTaskCorrente(pratica, getCurrentUser());
		VersionaAllegatoTaskFirmaApiTaskImpl<DatiGestioneFascicoloTask> t = new VersionaAllegatoTaskFirmaApiTaskImpl<DatiGestioneFascicoloTask>(gestioneTask);

		if (t.isOperazioneAbilitata()) t.versionaAllegato(allegato, protocollato, handler);
		else throw new OperazioneNonConsentita(TipoApiTask.VERSIONA_ALLEGATO_TASK_FIRMA);
	}

	@Override
	public void evadi(List<String> ruoli) {
		EvadiApprovazioneFirmaTaskApiImpl t = new EvadiApprovazioneFirmaTaskApiImpl(this);
		t.evadi(ruoli);
	}

	@Override
	public void invalida() {
		InvalidaApprovazioneFirmaTaskApiImpl t = new InvalidaApprovazioneFirmaTaskApiImpl(this);
		t.invalida();
	}

	@Override
	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		XMLTaskFascicolo<DatiGestioneFascicoloTask> gestioneTask = XmlPluginUtil.getGestioneFasicoloTaskCorrente(pratica, getCurrentUser());
		AggiungiAllegatoApiTaskImpl<DatiGestioneFascicoloTask> t = new AggiungiAllegatoApiTaskImpl<DatiGestioneFascicoloTask>(gestioneTask);
		if (t.isOperazioneAbilitata()) t.aggiungiAllegato(allegato, handler);
		else throw new OperazioneNonConsentita(TipoApiTask.AGGIUNGI_ALLEGATO);
	}
}
