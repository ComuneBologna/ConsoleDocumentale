package it.eng.portlet.consolepec.gwt.shared;

public class ConsolePecConstants {

	public static final String ERROR_MESSAGE = "Servizio temporaneamente non disponibile, si prega di riprovare pi\u00F9 tardi";

	public static final int MIN_DESKTOP_WIDTH_PIXELS = 480;

	public static final String NESSUN_OGGETTO_MAIL_IN = "Nessun oggetto";

	public static final int WORKLIST_NUMERO_PER_PAGINA = 5;

	public static final int MAX_LENGTH_TITOLO = 30;

	public static final int MAX_LENGHT_TITOLO_MODULISTICA = 60;

	/**
	 * messaggio visualizzato quando al data di firma non è correttamente acquisita o è null
	 */
	public static final String DATA_FIRMA_NONVALIDA_O_ASSENTE = "Data di firma non valida oppure assente";

	/**
	 * massimo numero di caratteri, per i testi lunghi da troncare
	 */
	public static final Integer MAX_NUMERO_CARATTERI_TESTO_LUNGO = 200;

	/**
	 * numero massimo di righe per i testi lunghi da troncare
	 */
	public static final int MAX_NUMERO_RIGHE_TESTO_LUNGO = 5;

	public static final String OUT_FOLDER = "/PEC/CONSOLE/OUT/";

	public static final String PRATICA_FOLDER = "/PEC/CONSOLE/PRATICHE/";

	public static final String DOWNLOAD_ZIP_NAME = "allegati.zip";

	public enum VociRootSiteMap {
		//
		DA_LAVORARE("Scrivania"),
		//
		APERTE_ORA("Aperte"),
		//
		RICERCA_LIBERA("Cerca"),
		//
		CREA_FASCICOLO("Crea fascicolo"),
		//
		CREA_TEMPLATE("Crea modello"),
		//
		ESTRAZIONI_AMIANTO("Estrazioni amianto"),
		//
		CREA_COMUNICAZIONE("Crea comunicazione"),
		//
		LISTA_ANAGRAFICHE("Rubrica"),
		//
		CREA_ANAGRAFICA("Crea Anagrafica"),
		//
		AMMINISTRAZIONE("Amministrazione"),
		//
		ANAGRAFICA_FASCICOLI("Anagrafica fascicoli"),
		//
		ANAGRAFICA_GRUPPI("Anagrafica Gruppi"),
		//
		ANAGRAFICA_INGRESSI("Anagrafica Ingressi"),
		//
		ABILITAZIONI("Abilitazioni"),
		//
		ANGULAR("Angular"),
		//
		DRIVE("Drive");

		String label = null;

		VociRootSiteMap(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public String getId() {
			return name();
		}
	}

	public static final String TITOLO_DETTAGLIO_TEMPLATE = "Dettaglio Modello";

	public static final String TITOLO_SCELTA_CREAZIONE_TEMPLATE = "Creazione Modello";

	public static final String TITOLO_RICHIEDI_VISTO_FIRMA = "Proposta approvazione";

	public static final String TITOLO_FASCICOLO = "Modifica visibilita del fascicolo";

	public static final String TITOLO_ALLEGATO = "Modifica visibilita allegato";

	public static final String NUOVA_PUBBLICAZIONE = "Pubblica";

	public static final String MODIFICA_PUBBLICAZIONE = "Modifica pubblicazione";

	public static final String ELIMINA_PUBBLICAZIONE = "Rimuovi pubblicazione";

	public static final String NUOVA_PUBBLICAZIONE_INVIA = "Pubblica e invia";

	public static final String MODIFICA_PUBBLICAZIONE_INVIA = "Modifica pubblicazione e invia";

	public static final int MAX_COUNT_EXTENDED = 10000;

	public static final long AUTO_EXPIRE_TIME_IN_MS = 60 * 60 * 1000;

	public static final String TITOLO_ABILITA_COLLEGAMENTO = "Crea collegamento";

	public static final String TITOLO_MODIFICA_COLLEGAMENTO = "Modifica collegamento";

	public static final int HTTP_ERROR_PUBBLICAZIONE_TERMINATA = 1982;

	/* PROCEDIMENTI */

	public static final String CONFERMA_AVVIO_PROCEDIMENTO = "Avvia procedimento";

	public static final String CONFERMA_CHIUSURA_PROCEDIMENTO = "Chiudi procedimento";

	public static final String TITOLO_AVVIO_PROCEDIMENTO = "Avvio procedimento";

	public static final String TITOLO_CHIUSURA_PROCEDIMENTO = "Chiusura procedimento";

	public static final String RIEPILOGO_AVVIO_PROCEDIMENTO = "Dati del documento di avvio";

	public static final String RIEPILOGO_CHIUSURA_PROCEDIMENTO = "Dati del documento di chiusra";

	public static final String AVVISO_PROCEDIMENTI = "ATTENZIONE: esiste almeno un procedimento associato, modificare l'eventuale responsabilità e/o competenza su SPA0";

	public static final String FLAG_AVVIO_DI_UFFICIO = "U";

	public static final String FLAG_AVVIO_DI_PARTE = "P";

	public static final String DESCR_AVVIO_DI_UFFICIO = "D'UFFICIO";

	public static final String DESCR_AVVIO_DI_PARTE = "INIZIATIVA DI PARTE";

	public static final String WARN_CHIUSURA_PROCEDIMENTI_MULTIPLI = "Attenzione: non è possibile concludere per la presenza di più procedimenti avviati sul capofila. Verificare su SIPA";

	public static final String WARN_CHIUSURA_PROCEDIMENTI_ASSENTI = "Attenzione: non risulta presente alcun procedimento chiudibile avviato sul capofila";

	public static final String NON_PROTOCOLLATO = "NON PROTOCOLLATO";

	public static final String VEDI_ALLEGATI_COMPOSIZIONE_FASCICOLO = "Vedi allegati";

	public static final String CAPOFILA_LABEL = "CAPOFILA ";

	/* ESTRAZIONI */
	public static final String ESTRAZIONI_AMIANTO_FILE_NAME_XLS = "estrazioni.xlsm";
	public static final String ESTRAZIONI_AMIANTO_FILE_NAME_ODS = "estrazioni.ods";

	// lunghezze descrizioni nei widget del dettaglio delle pratiche

	public static final int DESCR_WIDGET_ELEMENTI_MAX_LEN = 97;

	public static final String FORCE_RELOAD_PARAM = "force-reload";

	public static final String WARN_CARICAMENTO_ZIP = "Attenzione: il caricamento di file zip molto grandi o con molti file al suo interno puo' richiedere diverso tempo per il caricamento. Si desidera continuare?";

	public static final Integer MAX_ZIP_FILE_LENGTH_MB = 300;

	public static interface StatiElementiComposizioneFascicolo {
		String STATO_EMAIL_IN = "EMAIL_IN";
		String STATO_EMAIL_OUT = "EMAIL_OUT";
		String STATO_PRATICA_MODULISTICA = "MODULISTICA";
		String STATO_ESTERNO = "ESTERNO";
		String STATO_BOZZA = "BOZZA";
	}

	public static final String DRIVE_OK = "ok";
	public static final String DRIVE_MESSAGE = "message";

}
