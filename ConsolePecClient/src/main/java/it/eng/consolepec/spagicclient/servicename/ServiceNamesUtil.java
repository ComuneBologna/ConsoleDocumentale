package it.eng.consolepec.spagicclient.servicename;

import java.util.HashMap;

public class ServiceNamesUtil {

	/* Nomi dei servizi invocati */
	// Alfresco
	public static final String CREATE_FOLDER_SERVICE = "alfrescocreatefolderService";
	public static final String GET_VERSIONS = "alfrescogetversionsService";
	public static final String COPY_DOCS_TO_FOLDER = "alfrescocopydocstofolderService";
	public static final String GET_DOC_BY_UUID = "alfrescogetdocbyuuidService";
	public static final String REMOVE_FOLDER = "alfrescoremovefolderService";
	public static final String REMOVE_DOCUMENT = "alfrescoremovedocumentsService";
	public static final String CREATE_DOCUMENT = "alfrescocreatedocumentService";
	public static final String CHECK_FOLDER = "alfrescocheckfolderService";
	public static final String GET_ALL_CONTENTS = "alfrescogetallcontentsService";
	public static final String GET_DOCUMENTS_BY_FOLDER = "alfrescogetdocumentsService";
	public static final String ALFRESCO_REMOVE_ASPECT = "alfrescoremoveaspectService";
	// Protocollazione
	public static final String RICERCA_CAPOFILA = "sdocricercacapofilaService";
	public static final String INTERROGA_PG = "sdocinterrogapgService";
	public static final String VERSAMENTO_PARER_PG = "sdocversamentoparerpgService";
	public static final String PROTOCOLLA = "sdocprotocollazionecompletaService";
	public static final String DETTAGLIO_PRATICA_PROTOCOLLAZIONE = "sdocdettagliopraticaprotocollazioneService";
	public static final String ELENCO_PRATICA_PROTOCOLLAZIONE = "sdocelencopraticaprotocollazioneService";
	// Firma Digitale
	public static final String VERIFICA_FIRMA_ALFRESCO = "sdocnewverificafirmaService";
	public static final String VERIFICA_FIRMA_FILE = "sdocnewverificafirmarequestService";
	public static final String FIRMA_MULTIPLA_CADES = "sdocfirmamultiplaalfrescohashService";
	public static final String FIRMA_SINGOLA_CADES_STRATIFICATA = "sdocaddpkcs7signalfrescohashService";
	public static final String FIRMA_SINGOLA_CADES = "sdocpkcs7signv2alfrescohashService";
	public static final String FIRMA_SINGOLA_PADES = "sdocpdfsignaturev2alfrescoService";
	public static final String FIRMA_MULTIPLA_PADES = "sdocfirmamultiplapadesalfrescoService";
	public static final String SEND_CREDENTIAL = "sdocsendcredentialService";
	public static final String INSERIMENTO_NOTE = "sdocinserimentonoteService";
	// Procedimenti
	public static final String AVVIO_PROCEDIMENTO = "sdocavvioprocedimentoService";
	public static final String ITER_PROCEDIMENTO = "sdociterprocedimentoService";
	public static final String CHIUSURA_PROCEDIMENTO = "sdocchiusuraprocedimentoService";
	public static final String PROPOSTA_CHIUSURA_PROCEDIMENTO = "sdocpropostachiusuraprocedimentoService";
	// Dispatch
	public static final String CONSOLE_PEC_DISPATCHER = "sdocconsolepecdispatcherService";
	/* Nomi dei servizi interni dispatch (CONSOLE_PEC_DISPATCHER) */
	public static final String AGGIORNA_PG = "aggiornapg";
	public static final String RIPORTA_IN_LETTURA = "sdriportainletturaService";
	public static final String SEARCH_PRATICHE_MONGO = "searchpratichemongo";
	@Deprecated
	public static final String ASSEGNA_UTENTE_ESTERNO = "assegnautenteesterno";
	@Deprecated
	public static final String MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO = "modificaabilitazioniassegnautenteesterno";
	public static final String MODIFICA_OPERATORE = "modificaoperatore";
	public static final String VALIDAZIONE_DATI_AGGIUNTIVI = "validazione_dati_aggiuntivi";
	public static final String FIRMASERVICE = "firmaservice";
	public static final String PROTOCOLLAZIONECOMPLETA = "protocollazionecompleta";
	public static final String INTERROGAPG = "interrogapg";
	public static final String VERSAMENTOPARERPG = "versamentoparerpg";
	@Deprecated
	public static final String CREAZIONEDRAFT = "creazionedraft";
	public static final String UPLOADALLEGATO = "uploadallegato";
	public static final String DELETEALLEGATI = "deleteallegati";
	@Deprecated
	public static final String DELETEDRAFT = "deletedraft";
	public static final String CREATEFASCICOLO = "createfascicolo";
	public static final String ESTRAZIONEAMIANTO = "estrazioneamianto";
	public static final String CREATEPRATICAMODULISTICA = "createpraticamodulistica";
	public static final String CREATE_TEMPLATE = "createtemplate";
	public static final String SAVE_TEMPLATE_MAIL = "savetemplatemail";
	public static final String SAVE_TEMPLATE_PDF = "savetemplatepdf";
	public static final String CREATE_TEMPLATE_PER_COPIA = "createtemplatepercopia";
	public static final String CREATE_TEMPLATE_PDF = "createtemplatepdf";
	public static final String UPLOADLINKALLEGATO = "uploadlinkallegato";
	public static final String GETVERSIONS = "getversions";
	public static final String COMBOEPROT = "comboeprot";
	public static final String RIATTIVASERVICE = "riattivaservice";
	public static final String ELIMINAFASCICOLOSERVICE = "eliminafascicolo";
	public static final String ELIMINAPRATICAMODULISTICASERVICE = "eliminapraticamodulistica";
	public static final String ELIMINATEMPLATE = "eliminatemplate";
	public static final String DOWNLOADMETADATILOCK = "downloadmetadatilock";
	public static final String UPLOADMETADATILOCK = "uploadmetadatilock";
	public static final String AGGIUNGIAFASCICOLO = "aggiungiafascicolo";
	@Deprecated
	public static final String PRENDIINCARICO = "prendiincarico";
	@Deprecated
	public static final String RILASCIAINCARICO = "rilasciaincarico";
	public static final String SEARCHCAPOFILA = "searchcapofila";
	public static final String PUBBLICAZIONE_ALLEGATO = "pubblicazioneallegato";
	public static final String MODIFICA_VISIBILITA_ALLEGATO = "modificavisibilitaallegato";
	public static final String MODIFICA_VISIBILITA_FASCICOLO = "modificavisibilitafascicolo";
	public static final String RIMOZIONE_PUBBLICAZIONE_ALLEGATO = "rimozionepubblicazioneallegato";
	public static final String AGGIUNTA_DATI_AGGIUNTIVI = "aggiuntadatiaggiuntivi";
	public static final String RIMOZIONE_DATI_AGGIUNTIVI = "rimozionedatiaggiuntivi";
	@Deprecated
	public static final String COLLEGA_FASCICOLO = "collegafascicolo";
	public static final String ELIMINA_COLLEGAMENTO = "eliminacollegamento";
	public static final String CONDIVIDI_FASCICOLO = "condividifascicolo";
	public static final String ELIMINA_CONDIVISIONE = "eliminacondivisione";
	public static final String RECUPERA_TIPOLOGIE_PROCEDIMENTI = "recuperatipologiapratiche";
	public static final String AVVIA_PROCEDIMENTI = "avviaprocedimenti";
	public static final String ITER_PROCEDIMENTI = "iterprocedimenti";
	public static final String CHIUDI_PROCEDIMENTI = "chiudiprocedimenti";
	public static final String PROPONI_CHIUSURA_PROCEDIMENTI = "proponichiusuraprocedimenti";
	public static final String SGANCIA_MAIL = "sganciamail";
	public static final String IMPORTA_ELETTORALE = "importaelettorare";
	public static final String ANNULLA_ELETTORALE = "annullaelettorare";
	@Deprecated
	public static final String SEARCH_METADATI_MONGO = "searchmetadatimongo";
	@Deprecated
	public static final String RITORNA_DA_INOLTRARE_ESTERNO = "ritornadainoltrareesterno";
	public static final String DOWNLOAD_STAMPE = "downloadstampe";
	public static final String CREATE_COMUNICAZIONE = "createcomunicazione";
	public static final String DETTAGLIO_COMUNICAZIONE = "dettagliocomunicazione";
	public static final String NUOVO_INVIO_COMUNICAZIONE = "nuovoinviocomunicazione";
	@Deprecated
	public static final String CREA_BOZZA_DA_TEMPLATE = "creabozzadatemplate";
	public static final String CAMBIA_TIPOLOGIA_ALLEGATO = "cambiatipologiaallegato";
	public static final String TASK_FIRMA_SERVICE = "taskfirmaservice";
	public static final String SEARCH_RICHIESTE_FIRMA_MONGO = "searchrichiestefirmamongo";
	public static final String ESTRAI_ETICHETTE_METADATI_PRATICA = "estraietichettemetadatipratica";
	public static final String TEMPLATE_PDF_OUTPUT_SERVICE = "templatepdfoutputservice";
	public static final String ESTRAI_EML_PEC = "estraiemlpec";
	public static final String MODIFICA_FASCICOLO = "modificafascicolo";
	@Deprecated
	public static final String RIASSEGNASERVICE = "riassegnaservice";
	public static final String CONCLUDI_ASSEGNAZIONE_ESTERNA = "concludiassegnazioneesterna";
	public static final String AVVIA_ASSEGNAZIONE_ESTERNA = "avviaassegnazioneesterna";
	public static final String MODIFICA_ASSEGNAZIONE_ESTERNA = "modificaoperazioniassegnazioneesterna";

	// ===================================================================================================== Nuovi Servizi
	// Rubrica
	public static final String RICERCA_ANAGRAFICA = "ricercaAnagrafica";
	public static final String CREA_ANAGRAFICA = "creaAnagrafica";
	public static final String MODIFICA_ANAGRAFICA = "modificaAnagrafica";
	public static final String ELIMINA_ANAGRAFICA = "eliminaAnagrafica";
	// Pratica Procedi
	public static final String RICERCA_PRATICA_PROCEDI = "ricercapraticaprocedi";
	public static final String COUNT_PRATICA_PROCEDI = "countpraticaprocedi";
	public static final String DETTAGLIO_PRATICA_PROCEDI = "dettagliopraticaprocedi";
	public static final String COLLEGA_PRATICA_PROCEDI = "collegapraticaprocedi";
	public static final String ELIMINA_COLLEGA_PRATICA_PROCEDI = "eliminacollegapraticaprocedi";
	// Generici pratiche
	public static final String ARCHIVIA_PRATICA = "archiviapratica";
	public static final String GENERA_TITOLO_FASCICOLO_DA_TEMPLATE = "generatitolofascicolodatemplate";
	public static final String RIASSEGNA_PRATICA = "riassegnapratica";
	public static final String COLLEGA_PRATICA = "collegapratica";
	public static final String COLLEGA_PRATICA_GRUPPO = "collegapraticagruppo";
	public static final String APPLICA_OPERATIVITA_RIDOTTA = "applicaOperativitaRidotta";
	public static final String RICERCA_PRATICHE_EXT = "ricercaPraticheExt";
	public static final String SPOSTA_ALLEGATI = "spostaAllegati";
	public static final String SPOSTA_PROTOCOLLAZIONI = "spostaProtocollazioni";
	public static final String AGGIUNGI_NOTE = "aggiungiNote";
	public static final String MODIFICA_NOTE = "modificaNote";
	public static final String CAMBIA_STEP_ITER = "cambiastepiter";
	public static final String INVIA_BOZZA = "inviaBozza";
	public static final String ELIMINA_BOZZA = "eliminaBozza";
	public static final String MODIFICA_BOZZA = "modificaBozza";
	public static final String CREA_BOZZA_DA_TEMPLATE_2 = "creaBozzaDaTemplate2";
	public static final String CREA_BOZZA = "creaBozza";
	public static final String RILASCIA_IN_CARICO = "rilascioInCarico";
	public static final String PRENDI_IN_CARICO = "presaInCarico";

	// SARA
	public static final String EMISSIONE_PERMESSO = "emettiPermessoEmissionePermesso";
	// Amministrazione - Anagrafiche fascicoli
	public static final String RICERCA_ANAGRAFICA_FASCICOLO = "ricercaAnagraficaFascicolo";
	public static final String CREA_ANAGRAFICA_FASCICOLO = "creaAnagraficaFascicolo";
	public static final String MODIFICA_ANAGRAFICA_FASCICOLO = "modificaAnagraficaFascicolo";
	public static final String COUNT_ANAGRAFICA_FASCICOLO = "countAnagraficaFascicolo";
	// Amministrazione - Anagrafiche ruoli
	public static final String RICERCA_ANAGRAFICA_GRUPPI = "ricercaAnagraficaGruppi";
	public static final String CREA_ANAGRAFICA_GRUPPI = "creaAnagraficaGruppi";
	public static final String MODIFICA_ANAGRAFICA_GRUPPI = "modificaAnagraficaGruppi";
	public static final String COUNT_ANAGRAFICA_GRUPPI = "countAnagraficaGruppi";
	// Amministrazione - Anagrafiche abilitazioni
	public static final String CARICA_ABILITAZIONI_RUOLI = "caricaabilitazioniruoli";
	public static final String AGGREGATE_ABILITAZIONI_RUOLI = "aggregateabilitazioniruoli";
	public static final String COUNT_AGGREGATE_ABILITAZIONI_RUOLI = "countaggregateabilitazioniruoli";
	// Amministrazione - Anagrafiche ingressi
	public static final String RICERCA_ANAGRAFICA_INGRESSI = "ricercaAnagraficaIngressi";
	public static final String CREA_ANAGRAFICA_INGRESSI = "creaAnagraficaIngressi";
	public static final String MODIFICA_ANAGRAFICA_INGRESSI = "modificaAnagraficaIngressi";
	public static final String COUNT_ANAGRAFICA_INGRESSI = "countanagraficaingressi";
	public static final String AGGIORNA_PRIMO_ASSEGNATARIO = "aggiornaprimoassegnatario";
	// Profilazione utente
	public static final String PROFILAZIONE_UTENTE_CARICA_ANAGRAFICHE_RUOLI = "caricaanagraficheruoliutente";
	public static final String PROFILAZIONE_UTENTE_CARICA_PREFERENZE = "caricapreferenzeutente";
	public static final String PROFILAZIONE_UTENTE_AGGIORNA_PREFERENZE = "aggiornapreferenzeutente";
	public static final String PROFILAZIONE_UTENTE_CARICA_AUTORIZZAZIONI = "caricaautorizzazioniutente";
	public static final String PROFILAZIONE_UTENTE_CARICA_WORKLIST = "caricaworklistutente";
	// Configurazioni
	public static final String CONFIGURAZIONI_ANGRAFICHE_RUOLI = "caricaanagraficheruoli";
	public static final String CONFIGURAZIONI_SETTORI = "caricasettori";
	public static final String CONFIGURAZIONI_PROPRIETA_GENERALI = "caricaproprietagenerali";
	public static final String CONFIGURAZIONI_ANAGRAFICHE_FASCICOLI = "caricaanagrafichefascicoli";
	public static final String CONFIGURAZIONI_ANAGRAFICHE_COMUNICAZIONI = "caricaanagrafichecomunicazioni";
	public static final String CONFIGURAZIONI_ANAGRAFICHE_MODELLI = "caricaanagrafichemodelli";
	public static final String CONFIGURAZIONI_ANAGRAFICHE_PRATICHE_MODULISTICA = "caricaanagrafichepratichemodulistica";
	public static final String CONFIGURAZIONI_ANAGRAFICHE_MAIL_IN_USCITA = "caricaanagrafichemailinuscita";
	public static final String CONFIGURAZIONI_ANAGRAFICHE_INGRESSI = "caricaanagraficheingressi";
	public static final String CONFIGURAZIONI_ANAGRAFICHE_RUOLI_PERSONALI = "caricaanagraficheruolipersonali";
	public static final String CONFIGURAZIONI_ABILITAZIONI_RUOLI = "caricaabilitazioniruoli";
	public static final String CONFIGURAZIONI_LISTA_EMAIL_ASSEGNA_ESTERNO = "listaemailassegnaesterno";
	public static final String CONFIGURAZIONI_ABILITAZIONI_RUOLO_CARTELLA_FIRMA = "abilitazioniruolocartellafirma";
	public static final String CONFIGURAZIONI_ABILITAZIONI_VISIBILITA_TIPO_PRATICA = "abilitazionivisibilitatipopratica";
	public static final String CONFIGURAZIONI_ABILITAZIONI_MODIFICA_RUOLO = "abilitazionimodificaruolo";
	public static final String CONFIGURAZIONI_ABILITAZIONI_MATRICE_VISIBILITA_RUOLO = "abilitazionimatricevisibilitaruolo";
	public static final String CONFIGURAZIONI_ABILITAZIONI_MATRICE_VISIBILITA_TIPO_PRATICA = "abilitazionimatricevisibilitatipopratica";
	public static final String CONFIGURAZIONI_GET_LAST_EDIT = "configurazionilastedit";

	// Atti
	public static final String CARICA_DOCUMENTO_TASK_FIRMA = "caricadocumentotaskfirma";
	// Allegati
	public static final String DOWNLOAD_ALLEGATO_VERSIONATO = "downloadallegatoversionato";
	public static final String DOWNLOAD_ALLEGATO_VERSIONATO_SBUSTATO = "downloadallegatoversionatosbustato";
	public static final String DOWNLOAD_ALLEGATO_VERSIONATO_PRATICA = "downloadallegatoversionatopratica";
	public static final String MODIFICA_METADATI_ALLEGATO = "modificametadatiallegato";
	public static final String ELIMINA_METADATI_ALLEGATO = "eliminametadatiallegato";
	public static final String MODIFICA_TIPOLOGIE_ALLEGATI = "modificatipologieallegati";
	public static final String UPLOAD_FILE_ZIP = "uploadfilezip";
	// Lag
	public static final String LAG_IMPORTA_CODICE_FISCALE = "lagimportacodicefiscale";
	// Dati generici
	public static final String INDIRIZZI_EMAIL_RUBRICA_GET = "getindirizziemailrubrica";
	public static final String INDIRIZZI_EMAIL_RUBRICA_INSERT = "insertindirizziemailrubrica";

	// =====================================================================================================

	/* Nomi delle classi dei servizi interni dispatch */
	private static final String FIRMASERVICE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.firma.FirmaService";
	private static final String PROTOCOLLAZIONECOMPLETA_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.protocollazione.NewProtocollazioneService";
	private static final String INTERROGAPG_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.interrogapg.InterrogaPGService";
	private static final String VERSAMENTOPARERPG_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.versamentoparerpg.VersamentoParerPGService";
	@Deprecated
	private static final String CREAZIONEDRAFT_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.draft.CreazioneDraftService";
	private static final String UPLOADALLEGATO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.upload.UploadAllegatoPraticaService";
	private static final String DELETEALLEGATI_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.delete.RimuoviAllegatoFascicoloService";
	@Deprecated
	private static final String DELETEDRAFT_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.draft.EliminazioneDraftService";
	private static final String CREATEPRATICAMODULISTICA_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.modulistica.CreatePraticaModulisticaService";
	private static final String CREATE_TEMPLATE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.template.CreateTemplateEmailService";
	private static final String SAVE_TEMPLATE_MAIL_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.template.SaveTemplateMailService";
	private static final String SAVE_TEMPLATE_PDF_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.template.SaveTemplatePDFService";
	private static final String CREATE_TEMPLATE_PER_COPIA_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.template.CreaTemplatePerCopiaService";
	private static final String CREATE_TEMPLATE_PDF_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.template.CreateTemplatePdfService";
	private static final String CREATEFASCICOLO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.fascicolo.CreateFascicoloService";
	private static final String GETVERSIONS_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.version.GetAllVersionsService";
	private static final String UPLOADLINKALLEGATO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.upload.UploadAllegatoLinkPraticaService";
	private static final String COMBOEPROT_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.comboeprot.ComboEprotService";
	private static final String RIATTIVASERVICE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.riattiva.RiattivaService";
	private static final String ELIMINAFASCICOLOSERVICE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.fascicolo.EliminaFascicoloService";
	private static final String ELIMINAPRATICAMODULISTICASERVICE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.modulistica.EliminaPraticaModulisticaService";
	private static final String ELIMINATEMPLATE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.template.EliminaTemplateService";
	private static final String SEARCH_PRATICHE_MONGO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.ricercamongodb.RicercaPraticheMongoDbService";

	private static final String DOWNLOADMETADATILOCK_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.lockedservices.DownloadMetadatiService";
	private static final String UPLOADMETADATILOCK_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.lockedservices.UploadMetadatiService";
	private static final String AGGIUNGIAFASCICOLO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.fascicolo.AgganciaPraticaAFascicoloService";
	@Deprecated
	private static final String PRENDIINCARICO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.lockedservices.GetLock";
	@Deprecated
	private static final String RILASCIAINCARICO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.lockedservices.ReleaseLock";
	private static final String SEARCHCAPOFILA_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.ricercacapofila.RicercaCapofilaService";
	private static final String PUBBLICAZIONE_ALLEGATO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.allegatopubblico.PubblicazioneAllegatoService";
	private static final String RIMOZIONE_PUBBLICAZIONE_ALLEGATO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.allegatopubblico.RimozionePubblicazioneAllegatoService";
	private static final String AGGIUNTA_DATI_AGGIUNTIVI_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.datiaggiuntivi.AggiuntaDatiAggiuntiviService";
	private static final String RIMOZIONE_DATI_AGGIUNTIVI_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.datiaggiuntivi.RimozioneDatiAggiuntiviService";
	@Deprecated
	private static final String COLLEGA_FASCICOLO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.iperfascicolo.collegamento.CreaCollegamentoService";
	private static final String ELIMINA_COLLEGAMENTO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.iperfascicolo.collegamento.EliminaCollegamentoService";
	private static final String CONDIVIDI_FASCICOLO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.iperfascicolo.condivisione.CreaCondivisioneService";
	private static final String ELIMINA_CONDIVISIONE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.iperfascicolo.condivisione.EliminaCondivisioneService";
	private static final String RECUPERA_TIPOLOGIE_PROCEDIMENTI_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.tipologiaprocedimenti.RecuperaTipologieProcedimentiService";
	private static final String AVVIA_PROCEDIMENTI_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.procedimenti.AvviaProcedimentiService";
	private static final String ITER_PROCEDIMENTI_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.procedimenti.IterProcedimentiService";
	private static final String SGANCIA_MAIL_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.sganciamail.SganciaMailService";
	private static final String RIPORTA_IN_LETTURA_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.riportainlettura.RiportaInLetturaService";
	@Deprecated
	private static final String SEARCH_METADATI_MONGO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.ricercamongodb.RicercaMetadatiMongoDbService";
	private static final String ESTRAZIONEAMIANTO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.estrazioniamianto.EstrazioneAmiantoService";
	private static final String IMPORTA_ELETTORALE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.elettorale.ImportaElettoraleService";
	private static final String ANNULLA_ELETTORALE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.elettorale.AnnullaElettoraleService";
	@Deprecated
	private static final String ASSEGNA_UTENTE_ESTERNO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.assegna.AssegnaUtenteEsternoService";
	@Deprecated
	private static final String MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.assegna.ModificaAbilitazioniAssegnaUtenteEsternoService";
	@Deprecated
	private static final String RITORNA_DA_INOLTRARE_ESTERNO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.assegna.RitornaDaInoltrareEsternoService";
	private static final String DOWNLOAD_STAMPE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.stampe.DownloadStampeService";
	private static final String CREATE_COMUNICAZIONE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.inviomassivo.CreateComunicazioneService";
	private static final String DETTAGLIO_COMUNICAZIONE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.inviomassivo.DettaglioComunicazioneService";
	private static final String NUOVO_INVIO_COMUNICAZIONE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.inviomassivo.NuovoInvioComunicazioneService";
	@Deprecated
	private static final String CREA_BOZZA_DA_TEMPLATE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.template.CreateBozzaDaTemplateService";
	private static final String CAMBIA_TIPOLOGIA_ALLEGATO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.tipologiaallegato.CambiaTipologiaAllegatoService";
	private static final String MODIFICA_OPERATORE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.operatore.ModificaOperatoreService";
	private static final String VALIDAZIONE_DATI_AGGIUNTIVI_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.validazione.ValidazioneDatiAggiuntiviService";
	private static final String TASK_FIRMA_SERVICE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.taskfirma.TaskFirmaService";
	private static final String SEARCH_RICHIESTE_FIRMA_MONGO_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.ricercamongodb.RicercaTaskFirmaMongoDbService";
	private static final String ESTRAI_ETICHETTE_METADATI_PRATICA_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.fascicolo.EstraiEtichetteMetadatiPraticaService";
	private static final String TEMPLATE_PDF_OUTPUT_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.template.TemplatePDFOutputService";
	private static final String AGGIORNA_PG_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.fascicolo.AggiornaPGService";
	private static final String MODIFICA_FASCICOLO_SERVICE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.fascicolo.ModificaFascicoloService";
	@Deprecated
	private static final String RIASSEGNASERVICE_CLASS = "it.eng.cobo.consolepec.transactional.service.impl.riassegna.RiassegnaService";

	private static final String CONSOLE_PEC_SERVICE_GENERICO_CLASS = "it.eng.cobo.consolepec.transactional.service.ConsolePecGenericService";

	private static HashMap<String, String> _map = new HashMap<String, String>();

	static {
		_map.put(FIRMASERVICE, FIRMASERVICE_CLASS);
		_map.put(PROTOCOLLAZIONECOMPLETA, PROTOCOLLAZIONECOMPLETA_CLASS);
		_map.put(INTERROGAPG, INTERROGAPG_CLASS);
		_map.put(VERSAMENTOPARERPG, VERSAMENTOPARERPG_CLASS);
		_map.put(CREAZIONEDRAFT, CREAZIONEDRAFT_CLASS);
		_map.put(UPLOADALLEGATO, UPLOADALLEGATO_CLASS);
		_map.put(DELETEALLEGATI, DELETEALLEGATI_CLASS);
		_map.put(DELETEDRAFT, DELETEDRAFT_CLASS);
		_map.put(CREATEFASCICOLO, CREATEFASCICOLO_CLASS);
		_map.put(CREATEPRATICAMODULISTICA, CREATEPRATICAMODULISTICA_CLASS);
		_map.put(GETVERSIONS, GETVERSIONS_CLASS);
		_map.put(COMBOEPROT, COMBOEPROT_CLASS);
		_map.put(RIATTIVASERVICE, RIATTIVASERVICE_CLASS);
		_map.put(ELIMINAFASCICOLOSERVICE, ELIMINAFASCICOLOSERVICE_CLASS);
		_map.put(ELIMINAPRATICAMODULISTICASERVICE, ELIMINAPRATICAMODULISTICASERVICE_CLASS);
		_map.put(DOWNLOADMETADATILOCK, DOWNLOADMETADATILOCK_CLASS);
		_map.put(UPLOADMETADATILOCK, UPLOADMETADATILOCK_CLASS);
		_map.put(AGGIUNGIAFASCICOLO, AGGIUNGIAFASCICOLO_CLASS);
		_map.put(PRENDIINCARICO, PRENDIINCARICO_CLASS);
		_map.put(RILASCIAINCARICO, RILASCIAINCARICO_CLASS);
		_map.put(SEARCHCAPOFILA, SEARCHCAPOFILA_CLASS);
		_map.put(UPLOADLINKALLEGATO, UPLOADLINKALLEGATO_CLASS);
		_map.put(PUBBLICAZIONE_ALLEGATO, PUBBLICAZIONE_ALLEGATO_CLASS);
		_map.put(RIMOZIONE_PUBBLICAZIONE_ALLEGATO, RIMOZIONE_PUBBLICAZIONE_ALLEGATO_CLASS);
		_map.put(AGGIUNTA_DATI_AGGIUNTIVI, AGGIUNTA_DATI_AGGIUNTIVI_CLASS);
		_map.put(RIMOZIONE_DATI_AGGIUNTIVI, RIMOZIONE_DATI_AGGIUNTIVI_CLASS);
		_map.put(COLLEGA_FASCICOLO, COLLEGA_FASCICOLO_CLASS);
		_map.put(ELIMINA_COLLEGAMENTO, ELIMINA_COLLEGAMENTO_CLASS);
		_map.put(CONDIVIDI_FASCICOLO, CONDIVIDI_FASCICOLO_CLASS);
		_map.put(ELIMINA_CONDIVISIONE, ELIMINA_CONDIVISIONE_CLASS);
		_map.put(RECUPERA_TIPOLOGIE_PROCEDIMENTI, RECUPERA_TIPOLOGIE_PROCEDIMENTI_CLASS);
		_map.put(AVVIA_PROCEDIMENTI, AVVIA_PROCEDIMENTI_CLASS);
		_map.put(ITER_PROCEDIMENTI, ITER_PROCEDIMENTI_CLASS);
		_map.put(SGANCIA_MAIL, SGANCIA_MAIL_CLASS);
		_map.put(RIPORTA_IN_LETTURA, RIPORTA_IN_LETTURA_CLASS);
		_map.put(SEARCH_METADATI_MONGO, SEARCH_METADATI_MONGO_CLASS);
		_map.put(CREATE_TEMPLATE, CREATE_TEMPLATE_CLASS);
		_map.put(CREATE_TEMPLATE_PDF, CREATE_TEMPLATE_PDF_CLASS);
		_map.put(CREATE_TEMPLATE_PER_COPIA, CREATE_TEMPLATE_PER_COPIA_CLASS);
		_map.put(ELIMINATEMPLATE, ELIMINATEMPLATE_CLASS);
		_map.put(ESTRAZIONEAMIANTO, ESTRAZIONEAMIANTO_CLASS);
		_map.put(IMPORTA_ELETTORALE, IMPORTA_ELETTORALE_CLASS);
		_map.put(ANNULLA_ELETTORALE, ANNULLA_ELETTORALE_CLASS);
		_map.put(ASSEGNA_UTENTE_ESTERNO, ASSEGNA_UTENTE_ESTERNO_CLASS);
		_map.put(MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO, MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO_CLASS);
		_map.put(RITORNA_DA_INOLTRARE_ESTERNO, RITORNA_DA_INOLTRARE_ESTERNO_CLASS);
		_map.put(DOWNLOAD_STAMPE, DOWNLOAD_STAMPE_CLASS);
		_map.put(SEARCH_PRATICHE_MONGO, SEARCH_PRATICHE_MONGO_CLASS);
		_map.put(CREATE_COMUNICAZIONE, CREATE_COMUNICAZIONE_CLASS);
		_map.put(DETTAGLIO_COMUNICAZIONE, DETTAGLIO_COMUNICAZIONE_CLASS);
		_map.put(NUOVO_INVIO_COMUNICAZIONE, NUOVO_INVIO_COMUNICAZIONE_CLASS);
		_map.put(CREA_BOZZA_DA_TEMPLATE, CREA_BOZZA_DA_TEMPLATE_CLASS);
		_map.put(MODIFICA_OPERATORE, MODIFICA_OPERATORE_CLASS);
		_map.put(VALIDAZIONE_DATI_AGGIUNTIVI, VALIDAZIONE_DATI_AGGIUNTIVI_CLASS);
		_map.put(TASK_FIRMA_SERVICE, TASK_FIRMA_SERVICE_CLASS);
		_map.put(SEARCH_RICHIESTE_FIRMA_MONGO, SEARCH_RICHIESTE_FIRMA_MONGO_CLASS);
		_map.put(ESTRAI_ETICHETTE_METADATI_PRATICA, ESTRAI_ETICHETTE_METADATI_PRATICA_CLASS);
		_map.put(TEMPLATE_PDF_OUTPUT_SERVICE, TEMPLATE_PDF_OUTPUT_CLASS);
		_map.put(SAVE_TEMPLATE_MAIL, SAVE_TEMPLATE_MAIL_CLASS);
		_map.put(SAVE_TEMPLATE_PDF, SAVE_TEMPLATE_PDF_CLASS);
		_map.put(MODIFICA_FASCICOLO, MODIFICA_FASCICOLO_SERVICE_CLASS);
		_map.put(AGGIORNA_PG, AGGIORNA_PG_CLASS);
		_map.put(CAMBIA_TIPOLOGIA_ALLEGATO, CAMBIA_TIPOLOGIA_ALLEGATO_CLASS);
		_map.put(RIASSEGNASERVICE, RIASSEGNASERVICE_CLASS);

		// ===================================================================================================== Nuovi Servizi
		// Generici pratiche
		_map.put(ARCHIVIA_PRATICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(GENERA_TITOLO_FASCICOLO_DA_TEMPLATE, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(RIASSEGNA_PRATICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(COLLEGA_PRATICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(COLLEGA_PRATICA_GRUPPO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_VISIBILITA_ALLEGATO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_VISIBILITA_FASCICOLO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(RICERCA_PRATICHE_EXT, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(SPOSTA_ALLEGATI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(APPLICA_OPERATIVITA_RIDOTTA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(SPOSTA_PROTOCOLLAZIONI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_NOTE, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(AGGIUNGI_NOTE, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CAMBIA_STEP_ITER, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(INVIA_BOZZA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(ELIMINA_BOZZA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_BOZZA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CREA_BOZZA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CREA_BOZZA_DA_TEMPLATE_2, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(ESTRAI_EML_PEC, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(RILASCIA_IN_CARICO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(PRENDI_IN_CARICO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONCLUDI_ASSEGNAZIONE_ESTERNA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(AVVIA_ASSEGNAZIONE_ESTERNA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_ASSEGNAZIONE_ESTERNA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Rubrica
		_map.put(RICERCA_ANAGRAFICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CREA_ANAGRAFICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_ANAGRAFICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(ELIMINA_ANAGRAFICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Pratica procedi
		_map.put(RICERCA_PRATICA_PROCEDI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(COUNT_PRATICA_PROCEDI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(DETTAGLIO_PRATICA_PROCEDI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(COLLEGA_PRATICA_PROCEDI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(ELIMINA_COLLEGA_PRATICA_PROCEDI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// SARA
		_map.put(EMISSIONE_PERMESSO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Amministrazione - fascicoli
		_map.put(RICERCA_ANAGRAFICA_FASCICOLO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CREA_ANAGRAFICA_FASCICOLO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_ANAGRAFICA_FASCICOLO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(COUNT_ANAGRAFICA_FASCICOLO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Amministrazione - ruoli
		_map.put(RICERCA_ANAGRAFICA_GRUPPI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CREA_ANAGRAFICA_GRUPPI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_ANAGRAFICA_GRUPPI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(COUNT_ANAGRAFICA_GRUPPI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Amministrazione - ingressi
		_map.put(RICERCA_ANAGRAFICA_INGRESSI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CREA_ANAGRAFICA_INGRESSI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_ANAGRAFICA_INGRESSI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(COUNT_ANAGRAFICA_INGRESSI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(AGGIORNA_PRIMO_ASSEGNATARIO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Amministrazione - abilitazioni
		_map.put(CARICA_ABILITAZIONI_RUOLI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(AGGREGATE_ABILITAZIONI_RUOLI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(COUNT_AGGREGATE_ABILITAZIONI_RUOLI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Profilazione Utente
		_map.put(PROFILAZIONE_UTENTE_CARICA_ANAGRAFICHE_RUOLI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(PROFILAZIONE_UTENTE_CARICA_PREFERENZE, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(PROFILAZIONE_UTENTE_AGGIORNA_PREFERENZE, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(PROFILAZIONE_UTENTE_CARICA_AUTORIZZAZIONI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(PROFILAZIONE_UTENTE_CARICA_WORKLIST, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Configurazioni
		_map.put(CONFIGURAZIONI_ANGRAFICHE_RUOLI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_SETTORI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_PROPRIETA_GENERALI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ANAGRAFICHE_FASCICOLI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ANAGRAFICHE_COMUNICAZIONI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ANAGRAFICHE_MODELLI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ANAGRAFICHE_PRATICHE_MODULISTICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ANAGRAFICHE_MAIL_IN_USCITA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ANAGRAFICHE_INGRESSI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ANAGRAFICHE_RUOLI_PERSONALI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ABILITAZIONI_RUOLI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_LISTA_EMAIL_ASSEGNA_ESTERNO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ABILITAZIONI_RUOLO_CARTELLA_FIRMA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ABILITAZIONI_VISIBILITA_TIPO_PRATICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ABILITAZIONI_MODIFICA_RUOLO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ABILITAZIONI_MATRICE_VISIBILITA_RUOLO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_ABILITAZIONI_MATRICE_VISIBILITA_TIPO_PRATICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(CONFIGURAZIONI_GET_LAST_EDIT, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Atti
		_map.put(CARICA_DOCUMENTO_TASK_FIRMA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Allegati
		_map.put(DOWNLOAD_ALLEGATO_VERSIONATO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(DOWNLOAD_ALLEGATO_VERSIONATO_SBUSTATO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_METADATI_ALLEGATO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(DOWNLOAD_ALLEGATO_VERSIONATO_PRATICA, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(ELIMINA_METADATI_ALLEGATO, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(MODIFICA_TIPOLOGIE_ALLEGATI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(UPLOAD_FILE_ZIP, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		// Lag
		_map.put(LAG_IMPORTA_CODICE_FISCALE, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		//
		_map.put(INDIRIZZI_EMAIL_RUBRICA_GET, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(INDIRIZZI_EMAIL_RUBRICA_INSERT, CONSOLE_PEC_SERVICE_GENERICO_CLASS);

		// Procedimenti
		_map.put(CHIUDI_PROCEDIMENTI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
		_map.put(PROPONI_CHIUSURA_PROCEDIMENTI, CONSOLE_PEC_SERVICE_GENERICO_CLASS);
	}

	public static String getService(String name) {
		return _map.get(name);
	}

}
