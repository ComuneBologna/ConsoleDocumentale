package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Supplier;

import it.eng.cobo.consolepec.commons.atti.DocumentoTaskFirma;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta.OperazioneOperativitaRidotta.TipoAccesso;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.CambiaStepIterConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.ModificaDatoAggiuntivoValoreConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.ModificaDatoAggiuntivoValoriConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.collegamenti.Permessi;
import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.DatiTask.TipoTask;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.IncollaAllegatoHandler;
import it.eng.consolepec.xmlplugin.factory.XMLTask;
import it.eng.consolepec.xmlplugin.jaxb.GestioneFascicolo;
import it.eng.consolepec.xmlplugin.jaxb.Task;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.ContestoEsecuzione;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.Esecuzione.CambiaStepIterEsecuzione;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.Esecuzione.ModificaDatoAggiuntivoValoreEsecuzione;
import it.eng.consolepec.xmlplugin.tasks.esecuzioni.Esecuzione.ModificaDatoAggiuntivoValoriEsecuzione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.*;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl.*;
import it.eng.consolepec.xmlplugin.tasks.operazioni.ModificaNoteTaskApi;
import it.eng.consolepec.xmlplugin.tasks.richiestafirma.ApprovazioneFirmaTask;
import it.eng.consolepec.xmlplugin.util.GestioneProcedimentoBean;
import it.eng.consolepec.xmlplugin.util.IdentificativoProtocollazione;
import it.eng.consolepec.xmlplugin.util.TranslatorBeanCondivisione;

public class XMLTaskFascicolo<T extends DatiGestioneFascicoloTask> extends XMLTask<T> implements TaskFascicolo<T> {

	T dati = null;
	private XMLFascicolo pratica;
	private final Logger logger = LoggerFactory.getLogger(XMLTaskFascicolo.class);
	@SuppressWarnings("unchecked")
	private final GestionePresaInCaricoApiTask gestionePresaInCaricoApiTask = new GestionePresaInCaricoApiTaskImpl<DatiGestioneFascicoloTask>((XMLTaskFascicolo<DatiGestioneFascicoloTask>) this);

	@SuppressWarnings("unchecked")
	private final RiattivaFascicoloTaskApi riattivaFascicoloTaskApi = new RiattivaFascicoloTaskApiImpl<DatiGestioneFascicoloTask>((XMLTaskFascicolo<DatiGestioneFascicoloTask>) this);

	private final ContestoEsecuzione<TaskFascicolo<?>> contestoEsecuzione = new ContestoEsecuzione<TaskFascicolo<?>>();
	{
		contestoEsecuzione.add(CambiaStepIterConseguenzaEsecuzione.class, new Supplier<CambiaStepIterEsecuzione>() {

			@Override
			public CambiaStepIterEsecuzione get() {
				return new CambiaStepIterEsecuzione();
			}
		});

		contestoEsecuzione.add(ModificaDatoAggiuntivoValoriConseguenzaEsecuzione.class, new Supplier<ModificaDatoAggiuntivoValoriEsecuzione>() {

			@Override
			public ModificaDatoAggiuntivoValoriEsecuzione get() {
				return new ModificaDatoAggiuntivoValoriEsecuzione();
			}
		});

		contestoEsecuzione.add(ModificaDatoAggiuntivoValoreConseguenzaEsecuzione.class, new Supplier<ModificaDatoAggiuntivoValoreEsecuzione>() {

			@Override
			public ModificaDatoAggiuntivoValoreEsecuzione get() {
				return new ModificaDatoAggiuntivoValoreEsecuzione();
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <TS extends it.eng.consolepec.xmlplugin.factory.Task<?>> ContestoEsecuzione<TS> getContestoEsecuzione() {
		return (ContestoEsecuzione<TS>) contestoEsecuzione;
	}

	@Override
	public TipoTask getTipo() {
		return TipoTask.GestioneFascicoloTask;
	}

	@Override
	public T getDati() {
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
		return dati.getAttivo();
	}

	@Override
	public Pratica<?> getEnclosingPratica() {
		return pratica;
	}

	protected void setDati(T dati) {
		this.dati = dati;
	}

	@Override
	protected void serializeDati(it.eng.consolepec.xmlplugin.jaxb.Pratica praticajaxb) {
		try {
			Task out = new Task();
			DatiGestioneFascicoloTask dati = getDati();

			super.serializeDatiTaskGenerico(out, dati, praticajaxb);
			out.setGestioneFascicolo(new GestioneFascicolo());
			out.setCondivisioni(TranslatorBeanCondivisione.getElencoCondivisioni(dati.getCondivisioni()));
			praticajaxb.getTasks().getTask().add(out);

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di serializzazione task gestioneFascicolo");
		}
	}

	@Override
	protected void loadDatiTask(Pratica<?> pratica, Task t) {
		if (!(pratica instanceof Fascicolo) || t.getGestioneFascicolo() == null) {
			throw new PraticaException("Il task di tipo GestioneFascicolo supporta solo pratiche di tipo Fascicolo e deve essere basato su Task GestioneFascicolo");
		}
		this.pratica = (XMLFascicolo) pratica;

		Assegnatario asscorrente = assegnatarioFromJaxb(t.getAssegnatari().getAssegnatarioCorrente());
		dati = getDatiTask(asscorrente);
		dati.getCondivisioni().addAll(TranslatorBeanCondivisione.getElencoCondivisioni(t.getCondivisioni()));
		super.loadDatiTaskGenerico(t, dati);

		if (t.isAttivo()) {
			pratica.getDati().setAssegnatarioCorrente(asscorrente.getNome());
		}

		initApiTask();

	}

	@Override
	protected void initTask(Pratica<?> pratica, T bean) {
		if (!(pratica instanceof XMLFascicolo)) {
			throw new PraticaException("Il task di tipo GestioneFascicolo supporta solo pratiche di tipo XMLFascicolo");
		}
		this.pratica = (XMLFascicolo) pratica;
		this.dati = bean;
		dati.setId(getNextId(pratica));
		dati.setAttivo(true);
		pratica.getDati().setAssegnatarioCorrente(bean.getAssegnatario().getNome());
		initApiTask();
	}

	/* metodi object */

	@Override
	public int hashCode() {
		return dati.getId().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof XMLTaskFascicolo<?>) {
			XMLTaskFascicolo<?> task2 = (XMLTaskFascicolo<?>) obj;
			return task2.getDati().getId().equals(getDati().getId());
		}
		return false;
	}

	@Override
	public String toString() {
		return dati.toString();
	}

	@Override
	public boolean isOperazioneAbilitata() {
		return false;
	}

	@Override
	public void riattivaConCheck() {
		riattivaFascicoloTaskApi.riattiva();
	}

	@Override
	public void prendiInCarico(Utente user) {
		super.prendiInCarico(user);
		gestionePresaInCaricoApiTask.prendiInCarico(user);
	}

	@Override
	public void rilasciaInCarico(Utente user) {
		super.rilasciaInCarico(user);
		gestionePresaInCaricoApiTask.rilasciaInCarico(user);
	}

	@Override
	public boolean isGestionePresaInCaricoAbilitata() {
		return gestionePresaInCaricoApiTask.isOperazioneAbilitata();
	}

	protected void initApiTask() {

		operazioni.put(TipoApiTask.AGGANCIA_PRATICA_A_FASCICOLO, new AgganciaPraticaAFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.AGGIUNGI_ALLEGATO, new AggiungiAllegatoApiTaskImpl<T>(this));
		operazioni.put(TipoApiTask.AGGIUNGI_PROTOCOLLAZIONE_BA01, new AggiungiProtocollazioneBA01TaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.AGGIUNGI_REINOLTRO_PROTOCOLLAZIONE, new AggiungiReinoltroAProtocollazioneImpl<T>(this));
		operazioni.put(TipoApiTask.AVVIA_PROCEDIMENTO, new AvviaProcedimentoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CAMBIA_STATO_FASCICOLO, new CambiaStatoFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CAMBIA_TIPO_FASCICOLO, new CambiaTipoFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO, new CambiaVisibilitaFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CHIUDI_PROCEDIMENTO, new ChiudiProcedimentoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.COLLEGA_FASCICOLO, new CollegaFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CONCLUDI_FASCICOLO, new ConcludiFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CONDIVIDI_FASCICOLO, new CondividiFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.ELIMINA_COLLEGAMENTO_FASCICOLO, new EliminaCollegamentoFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.ELIMINA_CONDIVISIONE_FASCICOLO, new EliminaCondivisioneTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.ELIMINA_FASCICOLO, new EliminaFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.FIRMA, new FirmaAllegatiApiTaskImpl<T>(this));
		operazioni.put(TipoApiTask.RICHIESTA_APPROVAZIONE_FIRMA, new RichiestaApprovazioneFirmaTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RITIRO_APPROVAZIONE_FIRMA, new RitiroApprovazioneFirmaTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.PROTOCOLLAZIONE, new ProtocollaTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.PUBBLICA, new PubblicaAllegatoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RIASSEGNA, new RiassegnaFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RIMUOVI_ALLEGATO, new RimuoviAllegatoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RIMUOVI_PRATICA, new RimuoviPraticaTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RIMUOVI_PUBBLICAZIONE, new RimuoviPubblicazioneAllegatoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RISPONDI_MAIL, new RispondiTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.SGANCIA_PEC_IN, new SganciaPecInApiTaskImpl<T>(this));
		operazioni.put(TipoApiTask.TERMINA, new TerminaApiTaskImpl<T>(this));
		operazioni.put(TipoApiTask.RIPORTA_IN_LETTURA, new RiportaInLetturaTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.AGGIUNGI_DATO_AGGIUNTIVO, new AggiungiDatoAggiuntivoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RIMUOVI_DATO_AGGIUNTIVO, new RimuoviDatoAggiuntivoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.ASSEGNA_UTENTE_ESTERNO, new AssegnaUtenteEsternoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO, new ModificaAbilitazioniAssegnaUtenteEsternoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RITORNA_DA_INOLTRARE_ESTERNO, new RitornaDaInoltrareEsternoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RISPONDI_MAIL_DA_TEMPLATE, new RispondiEmailDaTemplateTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.MODIFICA_DATO_AGGIUNTIVO, new ModificaDatoAggiuntivoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.MODIFICA_OPERATORE, new ModificaOperatoreTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CAMBIA_STEP_ITER, new CambiaStepIterTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CAMBIA_STEP_ITER_CON_NOTIFICA, new CambiaStepIterConNotificaTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CREA_PDF_DA_TEMPLATE, new CreaPdfDaTemplateTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RICARICA_ALLEGATO_PROTOCOLLATO, new RicaricaAllegatoProtocollatoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.AGGIORNA_PG, new AggiornaPGTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.ESTRAI_EML_PEC, new EstraiEMLPecTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.MODIFICA_FASCICOLO, new ModificaFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.VERSIONA_ALLEGATO_TASK_FIRMA, new VersionaAllegatoTaskFirmaApiTaskImpl<T>(this));
		operazioni.put(TipoApiTask.CAMBIA_TIPOLOGIA_ALLEGATO, new CambiaTipologiaAllegatoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.COLLEGA_PRATICA_PROCEDI, new CollegaPraticaProcediTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.EMISSIONE_PERMESSO, new EmissionePermessoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.RICERCA_TASK_FIRMA, new EstrazioneTaskFirmaTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.INSERISCI_O_MODIFICA_METADATI_ALLEGATO, new InserisciModificaMetadatiAllegatoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.ELIMINA_METADATI_ALLEGATO, new EliminaMetadatiAllegatoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CAMBIA_VISIBILITA_ALLEGATO, new CambiaVisibilitaAllegatoTaskApiImpl<>(this));
		operazioni.put(TipoApiTask.MODIFICA_TIPOLOGIE_ALLEGATO, new ModificaTipologieAllegatoTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.OPERATIVITA_RIDOTTA, new OperativitaRidottaTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.ESTRAI_VERSIONI_PRECEDENTI_ALLEGATI, new EstraiVersioniPrecedentiAllegatiTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.MODIFICA_NOTE, new ModificaNoteFascicoloTaskApiImpl<T>(this));
		operazioni.put(TipoApiTask.CREA_BOZZA, new CreaBozzaTaskApiImpl<T>(this));
	}

	@SuppressWarnings("unchecked")
	protected T getDatiTask(Assegnatario asscorrente) {
		DatiGestioneFascicoloTask.Builder builder = new DatiGestioneFascicoloTask.Builder();
		builder.setAssegnatario(asscorrente);
		return (T) builder.construct();
	}

	/* Implementazione dei task: */

	@Override
	public void agganciaPraticaAFascicolo(Pratica<?> pratica) throws PraticaException {
		checkAbilitazione(TipoApiTask.AGGANCIA_PRATICA_A_FASCICOLO);
		((AgganciaPraticaAFascicoloTaskApi) operazioni.get(TipoApiTask.AGGANCIA_PRATICA_A_FASCICOLO)).agganciaPraticaAFascicolo(pratica);
	}

	@Override
	public void aggiungiAllegato(Allegato allegato, AggiungiAllegato handler) throws ApplicationException, InvalidArgumentException {
		checkAbilitazione(TipoApiTask.AGGIUNGI_ALLEGATO);
		((AggiungiAllegatoApiTask) operazioni.get(TipoApiTask.AGGIUNGI_ALLEGATO)).aggiungiAllegato(allegato, handler);
	}

	@Override
	public void aggiungiCapofilaFromBA01(String numeroPG, Integer annoPG, String titiolo, String rubrica, String sezione, String oggetto, Date dataProtocollazione, String dataArrivo,
			String oraArrivo) throws PraticaException {
		checkAbilitazione(TipoApiTask.AGGIUNGI_PROTOCOLLAZIONE_BA01);
		((AggiungiProtocollazioneBA01TaskApi) operazioni.get(TipoApiTask.AGGIUNGI_PROTOCOLLAZIONE_BA01)).aggiungiCapofilaFromBA01(numeroPG, annoPG, titiolo, rubrica, sezione, oggetto,
				dataProtocollazione, dataArrivo, oraArrivo);
	}

	@Override
	public void aggiungiReinoltroAProtocollazione(PraticaEmailOut emailOut, String numeroProtocollo, int anno) throws PraticaException {
		checkAbilitazione(TipoApiTask.AGGIUNGI_REINOLTRO_PROTOCOLLAZIONE);
		((AggiungiReinoltroAProtocollazione) operazioni.get(TipoApiTask.AGGIUNGI_REINOLTRO_PROTOCOLLAZIONE)).aggiungiReinoltroAProtocollazione(emailOut, numeroProtocollo, anno);
	}

	@Override
	public void concludiFascicolo() throws PraticaException {
		checkAbilitazione(TipoApiTask.CONCLUDI_FASCICOLO);
		((ConcludiFascicoloTaskApi) operazioni.get(TipoApiTask.CONCLUDI_FASCICOLO)).concludiFascicolo();
	}

	@Override
	public void firmaAllegati(List<Allegato> allegati) throws PraticaException {
		checkAbilitazione(TipoApiTask.FIRMA);
		((FirmaAllegatiTaskApi) operazioni.get(TipoApiTask.FIRMA)).firmaAllegati(allegati);
	}

	@Override
	public void protocolla(ProtocollazioneBean protocollazioneBean) {

		checkAbilitazione(TipoApiTask.PROTOCOLLAZIONE);
		((ProtocollaTaskApi) operazioni.get(TipoApiTask.PROTOCOLLAZIONE)).protocolla(protocollazioneBean);
	}

	@Override
	public void protocolla(ProtocollazioneBean protocollazioneBean, String numeroPgCapofila, String annoPgCapofila) {
		checkAbilitazione(TipoApiTask.PROTOCOLLAZIONE);
		((ProtocollaTaskApi) operazioni.get(TipoApiTask.PROTOCOLLAZIONE)).protocolla(protocollazioneBean, numeroPgCapofila, annoPgCapofila);
	}

	@Override
	public void riassegna(AnagraficaRuolo nuovoAssegnatario, List<GruppoVisibilita> gruppiVisibilita, List<Pratica<?>> praticheCollegate, String operatore,
			List<String> indirizziNotifica) throws PraticaException {
		checkAbilitazione(TipoApiTask.RIASSEGNA);
		((RiassegnaFascicoloTaskApi) operazioni.get(TipoApiTask.RIASSEGNA)).riassegna(nuovoAssegnatario, gruppiVisibilita, praticheCollegate, operatore, indirizziNotifica);
	}

	@Override
	public void termina() {
		checkAbilitazione(TipoApiTask.TERMINA);
		((TerminaApiTask) operazioni.get(TipoApiTask.TERMINA)).termina();
	}

	@Override
	public void mettiInAffissione() throws PraticaException {
		checkAbilitazione(TipoApiTask.METTI_IN_AFFISSIONE);
		((MettiInAffissioneTaskApi) operazioni.get(TipoApiTask.METTI_IN_AFFISSIONE)).mettiInAffissione();
	}

	@Override
	public void cambiaStato(Stato stato) throws PraticaException {
		checkAbilitazione(TipoApiTask.CAMBIA_STATO_FASCICOLO);
		((CambiaStatoFascicoloTaskApi) operazioni.get(TipoApiTask.CAMBIA_STATO_FASCICOLO)).cambiaStato(stato);
	}

	@Override
	public void sganciaPecIn(PraticaEmailIn pecIn) throws PraticaException {
		checkAbilitazione(TipoApiTask.SGANCIA_PEC_IN);
		((SganciaPecInApiTask) operazioni.get(TipoApiTask.SGANCIA_PEC_IN)).sganciaPecIn(pecIn);

	}

	@Override
	public void eliminaCollegamento(Fascicolo fascicolo) throws PraticaException {
		checkAbilitazione(TipoApiTask.ELIMINA_COLLEGAMENTO_FASCICOLO);
		((EliminaCollegamentoFascicoloTaskApi) operazioni.get(TipoApiTask.ELIMINA_COLLEGAMENTO_FASCICOLO)).eliminaCollegamento(fascicolo);
	}

	@Override
	public void eliminaCondivisione(AnagraficaRuolo gruppo) throws PraticaException {
		checkAbilitazione(TipoApiTask.ELIMINA_CONDIVISIONE_FASCICOLO);
		((EliminaCondivisioneTaskApi) operazioni.get(TipoApiTask.ELIMINA_CONDIVISIONE_FASCICOLO)).eliminaCondivisione(gruppo);
	}

	@Override
	public void condividi(AnagraficaRuolo gruppo, List<String> oprazioni) throws PraticaException {
		checkAbilitazione(TipoApiTask.CONDIVIDI_FASCICOLO);
		((CondividiFascicoloTaskApi) operazioni.get(TipoApiTask.CONDIVIDI_FASCICOLO)).condividi(gruppo, oprazioni);
	}

	@Override
	public void rimuoviVisibilitaFascicolo(List<AnagraficaRuolo> listaRuoliVisibilita) throws PraticaException {
		checkAbilitazione(TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO);
		((CambiaVisibilitaFascicoloTaskApi) operazioni.get(TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO)).rimuoviVisibilitaFascicolo(listaRuoliVisibilita);

	}

	@Override
	public void aggiungiVisibilitaFascicolo(List<AnagraficaRuolo> listaRuoliVisibilita) throws PraticaException {
		checkAbilitazione(TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO);
		((CambiaVisibilitaFascicoloTaskApi) operazioni.get(TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO)).aggiungiVisibilitaFascicolo(listaRuoliVisibilita);

	}

	@Override
	public void aggiungiVisibilita(List<GruppoVisibilita> gruppiDaAggiungere) throws PraticaException {
		checkAbilitazione(TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO);
		((CambiaVisibilitaFascicoloTaskApi) operazioni.get(TipoApiTask.CAMBIA_VISIBILITA_FASCICOLO)).aggiungiVisibilita(gruppiDaAggiungere);
	}

	@Override
	public void removePratica(Pratica<? extends DatiPratica> pratica) throws PraticaException {
		((RimuoviPraticaTaskApi) operazioni.get(TipoApiTask.RIMUOVI_PRATICA)).removePratica(pratica);
	}

	@Override
	public void pubblicaAllegato(Allegato allegato, Date inizioPubblicazione, Date finePubblicazione) {
		checkAbilitazione(TipoApiTask.PUBBLICA);
		((PubblicaAllegatoTaskApi) operazioni.get(TipoApiTask.PUBBLICA)).pubblicaAllegato(allegato, inizioPubblicazione, finePubblicazione);
	}

	@Override
	public void rimuoviPubblicazioneAllegato(Allegato allegato) {
		checkAbilitazione(TipoApiTask.RIMUOVI_PUBBLICAZIONE);
		((RimuoviPubblicazioneAllegatoTaskApi) operazioni.get(TipoApiTask.RIMUOVI_PUBBLICAZIONE)).rimuoviPubblicazioneAllegato(allegato);
	}

	@Override
	public void aggiungiDatoAggiuntivo(DatoAggiuntivo datoAggiuntivo) {
		checkAbilitazione(TipoApiTask.AGGIUNGI_DATO_AGGIUNTIVO);
		((AggiungiDatoAggiuntivoTaskApi) operazioni.get(TipoApiTask.AGGIUNGI_DATO_AGGIUNTIVO)).aggiungiDatoAggiuntivo(datoAggiuntivo);
	}

	@Override
	public void aggiungiDatiAggiuntivi(List<DatoAggiuntivo> datiAggiuntivi) {
		checkAbilitazione(TipoApiTask.AGGIUNGI_DATO_AGGIUNTIVO);
		((AggiungiDatoAggiuntivoTaskApi) operazioni.get(TipoApiTask.AGGIUNGI_DATO_AGGIUNTIVO)).aggiungiDatiAggiuntivi(datiAggiuntivi);
	}

	@Override
	public void rimuoviDatoAggiuntivo(String nomeCampo) {
		checkAbilitazione(TipoApiTask.RIMUOVI_DATO_AGGIUNTIVO);
		((RimuoviDatoAggiuntivoTaskApi) operazioni.get(TipoApiTask.RIMUOVI_DATO_AGGIUNTIVO)).rimuoviDatoAggiuntivo(nomeCampo);
	}

	@Override
	public void rimuoviDatoAggiuntivoIfExists(String nomeCampo) {
		checkAbilitazione(TipoApiTask.RIMUOVI_DATO_AGGIUNTIVO);
		((RimuoviDatoAggiuntivoTaskApi) operazioni.get(TipoApiTask.RIMUOVI_DATO_AGGIUNTIVO)).rimuoviDatoAggiuntivoIfExists(nomeCampo);
	}

	@Override
	public void rimuoviDatiAggiuntivi() {
		checkAbilitazione(TipoApiTask.RIMUOVI_DATO_AGGIUNTIVO);
		((RimuoviDatoAggiuntivoTaskApi) operazioni.get(TipoApiTask.RIMUOVI_DATO_AGGIUNTIVO)).rimuoviDatiAggiuntivi();
	}

	@Override
	public void collegaFascicolo(Fascicolo fascicoloRemoto, List<String> ruoliUtente, List<String> operazioniRemotePerUtenteLocale) throws PraticaException {
		checkAbilitazione(TipoApiTask.COLLEGA_FASCICOLO);
		((CollegaFascicoloTaskApi) operazioni.get(TipoApiTask.COLLEGA_FASCICOLO)).collegaFascicolo(fascicoloRemoto, ruoliUtente, operazioniRemotePerUtenteLocale);
	}

	@Override
	public void collegaFascicolo(Fascicolo fascicoloRemoto, List<String> ruoliUtente, Permessi permessi) throws PraticaException {
		checkAbilitazione(TipoApiTask.COLLEGA_FASCICOLO);
		((CollegaFascicoloTaskApi) operazioni.get(TipoApiTask.COLLEGA_FASCICOLO)).collegaFascicolo(fascicoloRemoto, ruoliUtente, permessi);
	}

	@Override
	public void avviaProcedimento(GestioneProcedimentoBean avviaProcedimentoBean) {
		checkAbilitazione(TipoApiTask.AVVIA_PROCEDIMENTO);
		((AvviaProcedimentoTaskApi) operazioni.get(TipoApiTask.AVVIA_PROCEDIMENTO)).avviaProcedimento(avviaProcedimentoBean);

	}

	@Override
	public void chiudiProcedimento(ChiusuraProcedimentoInput chiudiProcedimentoBean, Integer durata) {
		checkAbilitazione(TipoApiTask.CHIUDI_PROCEDIMENTO);
		((ChiudiProcedimentoTaskApi) operazioni.get(TipoApiTask.CHIUDI_PROCEDIMENTO)).chiudiProcedimento(chiudiProcedimentoBean, durata);
	}

	@Override
	public void cambiaTipo(TipologiaPratica nuovoTipo) throws PraticaException {
		checkAbilitazione(TipoApiTask.CAMBIA_TIPO_FASCICOLO);
		((CambiaTipoFascicoloTaskApi) operazioni.get(TipoApiTask.CAMBIA_TIPO_FASCICOLO)).cambiaTipo(nuovoTipo);
	}

	@Override
	public void riportaInLettura() throws PraticaException {
		checkAbilitazione(TipoApiTask.RIPORTA_IN_LETTURA);
		((RiportaInLetturaTaskApi) operazioni.get(TipoApiTask.RIPORTA_IN_LETTURA)).riportaInLettura();
	}

	@Override
	public void assegnaEsterno(Set<String> destinatari, String testoEmail, Set<String> operazioniAbilitate) {
		checkAbilitazione(TipoApiTask.ASSEGNA_UTENTE_ESTERNO);
		((AssegnaUtenteEsternoTaskApi) operazioni.get(TipoApiTask.ASSEGNA_UTENTE_ESTERNO)).assegnaEsterno(destinatari, testoEmail, operazioniAbilitate);
	}

	@Override
	public void modificaAbilitazioni(Set<String> operazioniAbilitate) {
		checkAbilitazione(TipoApiTask.MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO);
		((ModificaAbilitazioniAssegnaUtenteEsternoTaskApi) operazioni.get(TipoApiTask.MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO)).modificaAbilitazioni(operazioniAbilitate);
	}

	@Override
	public void ritornaDaInoltrareEsterno(List<Pratica<?>> praticheCollegate) {
		checkAbilitazione(TipoApiTask.RITORNA_DA_INOLTRARE_ESTERNO);
		((RitornaDaInoltrareEsternoTaskApi) operazioni.get(TipoApiTask.RITORNA_DA_INOLTRARE_ESTERNO)).ritornaDaInoltrareEsterno(praticheCollegate);
	}

	@Override
	public void cambiaStep(String step, boolean finale, boolean iniziale, boolean creaBozza, List<String> destinatariNotifica) {
		checkAbilitazione(TipoApiTask.CAMBIA_STEP_ITER_CON_NOTIFICA);
		((CambiaStepIterTaskApi) operazioni.get(TipoApiTask.CAMBIA_STEP_ITER_CON_NOTIFICA)).cambiaStep(step, finale, iniziale, creaBozza, destinatariNotifica);
	}

	@Override
	public void cambiaStep(String step, boolean finale, boolean iniziale, boolean creaBozza) {
		checkAbilitazione(TipoApiTask.CAMBIA_STEP_ITER);
		((CambiaStepIterTaskApi) operazioni.get(TipoApiTask.CAMBIA_STEP_ITER)).cambiaStep(step, finale, iniziale, creaBozza);
	}

	@Override
	public void modificaDatoAggiuntivo(DatoAggiuntivo datoAggiuntivo) {
		checkAbilitazione(TipoApiTask.MODIFICA_DATO_AGGIUNTIVO);
		((ModificaDatoAggiuntivoTaskApi) operazioni.get(TipoApiTask.MODIFICA_DATO_AGGIUNTIVO)).modificaDatoAggiuntivo(datoAggiuntivo);
	}

	@Override
	public void modificaOperatore(String operatore) {
		checkAbilitazione(TipoApiTask.MODIFICA_OPERATORE);
		((ModificaOperatoreTaskApi) operazioni.get(TipoApiTask.MODIFICA_OPERATORE)).modificaOperatore(operatore);
	}

	@Override
	public Integer inviaInApprovazione(RichiestaApprovazioneFirmaBean bean, List<String> destinatariNotifica, String note, String fullNameUtente) {
		checkAbilitazione(TipoApiTask.RICHIESTA_APPROVAZIONE_FIRMA);
		return ((RichiestaApprovazioneFirmaTaskApi) operazioni.get(TipoApiTask.RICHIESTA_APPROVAZIONE_FIRMA)).inviaInApprovazione(bean, destinatariNotifica, note, fullNameUtente);
	}

	@Override
	public void ritira(Allegato a, ApprovazioneFirmaTask approvazioneFirmaTask, String note, List<String> destinatariNotifica, List<String> ruoli, String fullNameUtente) {
		checkAbilitazione(TipoApiTask.RITIRO_APPROVAZIONE_FIRMA);
		((RitiroApprovazioneFirmaTaskApi) operazioni.get(TipoApiTask.RITIRO_APPROVAZIONE_FIRMA)).ritira(a, approvazioneFirmaTask, note, destinatariNotifica, ruoli, fullNameUtente);
	}

	@Override
	public void ricaricaAllegatoProtocollato(Allegato allegato, AggiungiAllegato handler, Map<String, String> campi) throws InvalidArgumentException, ApplicationException {
		checkAbilitazione(TipoApiTask.RICARICA_ALLEGATO_PROTOCOLLATO);
		((RicaricaAllegatoProtocollatoTaskApi) operazioni.get(TipoApiTask.RICARICA_ALLEGATO_PROTOCOLLATO)).ricaricaAllegatoProtocollato(allegato, handler, campi);
	}

	@Override
	public boolean modifica(String titolo, TipologiaPratica tipologiaPratica) {
		checkAbilitazione(TipoApiTask.MODIFICA_FASCICOLO);
		return ((ModificaFascicoloTaskApi) operazioni.get(TipoApiTask.MODIFICA_FASCICOLO)).modifica(titolo, tipologiaPratica);
	}

	@Override
	public void aggiornaPG(AggiornaPGTaskApiDati datiDiAggiornamento) {
		checkAbilitazione(TipoApiTask.AGGIORNA_PG);
		((AggiornaPGTaskApi) operazioni.get(TipoApiTask.AGGIORNA_PG)).aggiornaPG(datiDiAggiornamento);
	}

	@Override
	public boolean aggiornamentoValido(List<IdentificativoProtocollazione> protocollazioni) {
		checkAbilitazione(TipoApiTask.AGGIORNA_PG);
		return ((AggiornaPGTaskApi) operazioni.get(TipoApiTask.AGGIORNA_PG)).aggiornamentoValido(protocollazioni);
	}

	@Override
	public void versionaAllegato(Allegato allegato, boolean protocollato, AggiungiAllegato handler) throws Exception {
		checkAbilitazione(TipoApiTask.VERSIONA_ALLEGATO_TASK_FIRMA);
		((VersionaAllegatoTaskFirmaApiTask) operazioni.get(TipoApiTask.VERSIONA_ALLEGATO_TASK_FIRMA)).versionaAllegato(allegato, protocollato, handler);
	}

	@Override
	public void cambiaTipologiaAllegato(String nomeAllegato, String tipologiaAllegato) throws PraticaException {
		checkAbilitazione(TipoApiTask.CAMBIA_TIPOLOGIA_ALLEGATO);
		((CambiaTipologiaAllegatoTaskApi) operazioni.get(TipoApiTask.CAMBIA_TIPOLOGIA_ALLEGATO)).cambiaTipologiaAllegato(nomeAllegato, tipologiaAllegato);
	}

	@Override
	public void modificaTipologiaAllegato(String nomeAllegato, String tipologiaAllegato) throws PraticaException {
		checkAbilitazione(TipoApiTask.CAMBIA_TIPOLOGIA_ALLEGATO);
		((CambiaTipologiaAllegatoTaskApi) operazioni.get(TipoApiTask.CAMBIA_TIPOLOGIA_ALLEGATO)).modificaTipologiaAllegato(nomeAllegato, tipologiaAllegato);
	}

	@Override
	public boolean collegaPraticaProcedi(String idPraticaProcedi) {
		checkAbilitazione(TipoApiTask.COLLEGA_PRATICA_PROCEDI);
		return ((CollegaPraticaProcediTaskApi) operazioni.get(TipoApiTask.COLLEGA_PRATICA_PROCEDI)).collegaPraticaProcedi(idPraticaProcedi);
	}

	@Override
	public boolean eliminaCollegaPraticaProcedi(String idPraticaProcedi) {
		checkAbilitazione(TipoApiTask.COLLEGA_PRATICA_PROCEDI);
		return ((CollegaPraticaProcediTaskApi) operazioni.get(TipoApiTask.COLLEGA_PRATICA_PROCEDI)).eliminaCollegaPraticaProcedi(idPraticaProcedi);
	}

	@Override
	public DocumentoTaskFirma estraiTaskFirma(Fascicolo fascicolo, Integer idTaskFirma) {
		checkAbilitazione(TipoApiTask.RICERCA_TASK_FIRMA);
		return ((EstrazioneTaskFirmaTaskApi) operazioni.get(TipoApiTask.RICERCA_TASK_FIRMA)).estraiTaskFirma(fascicolo, idTaskFirma);
	}

	@Override
	public void inserisciModificaMetadatiAllegato(Allegato allegato, List<DatoAggiuntivo> datiAggiuntivi) {
		checkAbilitazione(TipoApiTask.INSERISCI_O_MODIFICA_METADATI_ALLEGATO);
		((InserisciModificaMetadatiAllegatoTaskApi) operazioni.get(TipoApiTask.INSERISCI_O_MODIFICA_METADATI_ALLEGATO)).inserisciModificaMetadatiAllegato(allegato, datiAggiuntivi);
	}

	@Override
	public void eliminaMetadatiAllegato(Allegato allegato, List<String> nomiMetadati) {
		checkAbilitazione(TipoApiTask.ELIMINA_METADATI_ALLEGATO);
		((EliminaMetadatiAllegatoTaskApi) operazioni.get(TipoApiTask.ELIMINA_METADATI_ALLEGATO)).eliminaMetadatiAllegato(allegato, nomiMetadati);
	}

	@Override
	public void rimuoviAllegato(Allegato allegato) {
		checkAbilitazione(TipoApiTask.RIMUOVI_ALLEGATO);
		((RimuoviAllegatoTaskApi) operazioni.get(TipoApiTask.RIMUOVI_ALLEGATO)).rimuoviAllegato(allegato);
	}

	@Override
	public void cambiaVisibilitaAllegato(Allegato allegato, List<String> gruppi) {
		checkAbilitazione(TipoApiTask.CAMBIA_VISIBILITA_ALLEGATO);
		((CambiaVisibilitaAllegatoTaskApi) operazioni.get(TipoApiTask.CAMBIA_VISIBILITA_ALLEGATO)).cambiaVisibilitaAllegato(allegato, gruppi);
	}

	@Override
	public void modificaTipologieAllegato(Allegato allegato, List<String> tipologie) throws PraticaException {
		checkAbilitazione(TipoApiTask.MODIFICA_TIPOLOGIE_ALLEGATO);
		((ModificaTipologieAllegatoTaskApi) operazioni.get(TipoApiTask.MODIFICA_TIPOLOGIE_ALLEGATO)).modificaTipologieAllegato(allegato, tipologie);
	}

	@Override
	public void applicaOperativitaRidotta(String operazione, List<TipoAccesso> accessiConsentiti) {
		checkAbilitazione(TipoApiTask.OPERATIVITA_RIDOTTA);
		((OperativitaRidottaTaskApi) operazioni.get(TipoApiTask.OPERATIVITA_RIDOTTA)).applicaOperativitaRidotta(operazione, accessiConsentiti);
	}

	@Override
	public TagliaAllegatiOutput tagliaAllegati(List<Allegato> allegati, Pratica<?> praticaDestinataria) throws ApplicationException, InvalidArgumentException {
		TagliaAllegatiTaskApi taskApi = new TagliaAllegatiTaskApiImpl<T>(this);
		return taskApi.tagliaAllegati(allegati, praticaDestinataria);
	}

	@Override
	public void incollaAllegati(TagliaAllegatiOutput tagliaAllegatiOutput, Pratica<?> praticaSorgente, IncollaAllegatoHandler handler) throws ApplicationException, InvalidArgumentException {
		IncollaAllegatiTaskApi taskApi = new IncollaAllegatiTaskApiImpl<T>(this);
		taskApi.incollaAllegati(tagliaAllegatiOutput, praticaSorgente, handler);
	}

	@Override
	public TagliaProtocollazioniOutput tagliaProtocollazioni(List<Allegato> allegati, List<Pratica<?>> praticheCollegateProtocollate, Pratica<?> praticaDestinataria) throws Exception {
		TagliaProtocollazioniTaskApi taskApi = new TagliaProtocollazioniTaskApiImpl<T>(this);
		return taskApi.tagliaProtocollazioni(allegati, praticheCollegateProtocollate, praticaDestinataria);
	}

	@Override
	public void incollaProtocollazioni(TagliaProtocollazioniOutput tagliaProtocollazioniOutput, Pratica<?> praticaSorgente, IncollaAllegatoHandler allegatoHandler) throws Exception {
		IncollaProtocollazioniTaskApi taskApi = new IncollaProtocollazioniTaskApiImpl<T>(this);
		taskApi.incollaProtocollazioni(tagliaProtocollazioniOutput, praticaSorgente, allegatoHandler);

	}

	@Override
	public void aggiungiNote(String note) {
		checkAbilitazione(TipoApiTask.MODIFICA_NOTE);
		((ModificaNoteTaskApi) operazioni.get(TipoApiTask.MODIFICA_NOTE)).aggiungiNote(note);
	}

	@Override
	public void modificaNote(String note) {
		checkAbilitazione(TipoApiTask.MODIFICA_NOTE);
		((ModificaNoteTaskApi) operazioni.get(TipoApiTask.MODIFICA_NOTE)).modificaNote(note);
	}

	@Override
	public void inviaDaCSV(String nomeAllegato) {
		InviaDaCSVTaskApi taskApi = new InviaDaCSVTaskApiImpl<T>(this);
		taskApi.inviaDaCSV(nomeAllegato);
	}

	@Override
	public void creaBozza(PraticaEmailOut bozza) {
		checkAbilitazione(TipoApiTask.CREA_BOZZA);
		((CreaBozzaTaskApi) operazioni.get(TipoApiTask.CREA_BOZZA)).creaBozza(bozza);

	}
}
