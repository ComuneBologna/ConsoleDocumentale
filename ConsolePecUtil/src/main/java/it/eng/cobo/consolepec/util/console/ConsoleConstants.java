package it.eng.cobo.consolepec.util.console;

import lombok.Getter;

public interface ConsoleConstants {

	String BASE_PATH_IN_ALFRESCO = "/PEC/CONSOLE/IN";
	String BASE_PATH_OUT_ALFRESCO = "/PEC/CONSOLE/OUT";
	String PATH_ALLEGATI_EMAIL_ALFRESCO = "MESSAGGIO";

	String RICEVUTA_CONSEGNA_NAME = "RicevuteDiConsegna.pdf";
	String RIVERSAMENTO_CARTACEO_NAME = "RiversamentoCartaceo.pdf";
	String ESTRAZIONI_ODS = "estrazione_globale.ods";
	String ESTRAZIONE_RESULT_ODS = "estrazioni.ods";
	int NUMERO_MASSIMO_SITI = 40;
	String DATA_SOURCE_SARA = "sara";
	String DATA_SOURCE_PEC = "dspec";

	String FORMATO_TIME = "HH:mm";
	String FORMATO_TIMESEC = "HH:mm:ss";
	String FORMATO_DATA = "dd/MM/yyyy";
	String FORMATO_US_DATA = "yyyy-MM-dd";
	String FORMATO_DATA_1 = "dd-MM-yyyy";

	String FORMATO_DATAORA = "dd/MM/yyyy HH:mm";
	String FORMATO_DATAORA_1 = "yyyy-MM-dd HH:mm";
	String FORMATO_DATAORA_2 = "dd-MM-yyyy HH:mm";

	String FORMATO_DATAORASEC_1 = "dd/MM/yyyy HH:mm:ss";
	String FORMATO_DATAORASEC_2 = "yyyy-MM-dd HH:mm:ss";
	String FORMATO_DATAORASEC_3 = "dd-MM-yyyy hh:mm:ss";

	String FORMATO_DATAORAMILLIS = "dd/MM/yyyy HH:mm:ss.SSS";
	String FORMATO_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	String FORMATO_ISO8601_2 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
	String FORMATO_ISO8601_3 = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
	String FORMATO_GLOBAL = "yyyy-MM-dd HH:mm:ss z";

	String CODICE_TIPO_PROTOCOLLAZIONE = "PG";

	interface Procedimenti {
		String FLAG_AVVIO_PROCEDIMENTI = "5";
		String FLAG_CHIUSURA_PROCEDIMENTI = "9";
		String TIPO_PROTOCOLLO_PROCEDIMENTI = "PG";
		int COD_COMUNE_PROCEDIMENTI = 37006;
		String COD_UTENTE_PROCEDIMENTI = "8888873";
	}

	enum TipoFiltro {
		SINGOLO, LISTA, MAPPA;
	}

	enum TipoDato {
		STRINGA, BOOLEANO, DATA, NUMERICO;
	}

	@Getter
	enum FiltriRicerca {
		ID_DOCUMENTALE("idDocumentale", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		TITOLO("titolo", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		TIPO_PRATICA("tipo", TipoFiltro.LISTA, TipoDato.STRINGA), //

		// ~ Specificati per la ricerca pratica
		DESTINATARIO_CC("destinatarioCC", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		DATA_CREAZIONE_FROM("dataCreazioneFrom", TipoFiltro.SINGOLO, TipoDato.DATA), //
		DATA_CREAZIONE_TO("dataCreazioneTo", TipoFiltro.SINGOLO, TipoDato.DATA), //
		DATA_RICEZIONE_PEC_FROM("dataRicezioneFrom", TipoFiltro.SINGOLO, TipoDato.DATA), //
		DATA_RICEZIONE_PEC_TO("dataRicezioneTo", TipoFiltro.SINGOLO, TipoDato.DATA), //
		DESTINATARIO("destinatario", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		DESTINATARIO_PRINCIPALE("destinatarioPrincipale", TipoFiltro.LISTA, TipoDato.STRINGA), //
		DESTINATARI("destinatariList", TipoFiltro.LISTA, TipoDato.STRINGA), //
		PEC_MESSAGE_ID("pecMessageID", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		PEC_ID_EMAIL_SERVER("idEmailServer", TipoFiltro.SINGOLO, TipoDato.NUMERICO), //
		LETTO("letto", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //
		ACCETTAZIONE("accettazione", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //
		CONSEGNA("consegna", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //
		STATO_DESTINATARIO("statoDestinatario", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		PROVENIENZA("provenienza", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		STATO_PRATICA("stato", TipoFiltro.LISTA, TipoDato.STRINGA), //
		STEP_ITER("stepIter", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		STEP_ITER_LIST("stepIterList", TipoFiltro.LISTA, TipoDato.STRINGA), //
		OPERATORE("operatore", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		TIPO_EMAIL("tipoEmail", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		UTENTE_CREAZIONE("utenteCreazione", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		USERNAME_CREAZIONE("usernameCreazione", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		DATI_AGGIUNTIVI_MAP("datiAggiuntivi", TipoFiltro.MAPPA, TipoDato.BOOLEANO, TipoDato.STRINGA), //
		DATI_AGGIUNTIVI_EQ_MAP("datiAggiuntiviEq", TipoFiltro.MAPPA, TipoDato.BOOLEANO, TipoDato.STRINGA), //
		DATI_AGGIUNTIVI_OR_MAP("datiAggiuntiviOr", TipoFiltro.MAPPA, TipoDato.NUMERICO, TipoDato.BOOLEANO, TipoDato.STRINGA), //
		CONDIVISIONI_ID_DOCUMENTALE("condivisioniIdDocumentale", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		NOME_MODULO("nomeModulo", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		VALORI_MODULO("valoriModulo", TipoFiltro.MAPPA, TipoDato.DATA, TipoDato.BOOLEANO, TipoDato.STRINGA), //
		NOME_TEMPLATE("nomeTemplate", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		TEMPLATE_FASCICOLI_ABILITATI("templateFascicoliAbilitati", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		DESTINATARI_ASSEGNAZIONE_ESTERNA("destinatariAssegnazioneEsterna", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		ID_TEMPLATE_COMUNICAZIONE("idTemplateComunicazione", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		CODICE_COMUNICAZIONE("codiceComunicazione", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		PROTOCOLLAZIONE("protocollazione", TipoFiltro.MAPPA, TipoDato.DATA, TipoDato.STRINGA), //

		ASSEGNATARI_SINGOLI_WORKLIST("assegnatari", TipoFiltro.LISTA, TipoDato.STRINGA), //
		FILTRO_WORKLIST("filtroWorklist", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //
		FILTRO_PROVENIENZA("filtroProvenienza", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //
		FILTRO_ASSEGNATARI_PASSATI("filtroAssegnatariPassati", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //

		ASSEGNATARIO_SINGOLO("assegnatario", TipoFiltro.LISTA, TipoDato.STRINGA), //
		GRUPPI_ASSEGNATARIO("gruppiAssegnatario", TipoFiltro.LISTA, TipoDato.STRINGA), //
		GRUPPI_CONDIVISIONI("gruppiCondivisioni", TipoFiltro.LISTA, TipoDato.STRINGA), //
		GRUPPI_VISIBILITA("gruppiVisibilita", TipoFiltro.LISTA, TipoDato.STRINGA), //
		GRUPPI_MATR_VISIBILITA("gruppiMatriceVisibilita", TipoFiltro.LISTA, TipoDato.STRINGA), //
		TIPI_PRATICHE_MATR_VISIBILITA("tipiMatriceVisibilita", TipoFiltro.LISTA, TipoDato.STRINGA), //

		FILTRO_ASSEGNATARIO("filtroAssegnatario", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //
		FILTRO_MATRICE_VISIBILITA("filtroMatriceVisibilita", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //
		FILTRO_SUPERVISORE("filtroSuperutente", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //
		FILTRO_VISIBILITA("filtroVisibilita", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //
		ESCLUDI_ASSEGNAZIONI("escludiAssegnazioni", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //

		// ~ Specificati per la ricerca task
		DATA_RICHIESTA_FROM("dataRichiestaFrom", TipoFiltro.SINGOLO, TipoDato.DATA), //
		DATA_RICHIESTA_TO("dataRichiestaTo", TipoFiltro.SINGOLO, TipoDato.DATA), //
		DATA_SCADENZA_FROM("dataScadenzaFrom", TipoFiltro.SINGOLO, TipoDato.DATA), //
		DATA_SCADENZA_TO("dataScadenzaTo", TipoFiltro.SINGOLO, TipoDato.DATA), //
		TIPO_PROPOSTA("tipoProposta", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		TIPO_STATO("tipoStato", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		STATO_TASK("statoTask", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		PROPONENTI("proponenti", TipoFiltro.LISTA, TipoDato.STRINGA), //
		RICERCA_DA_DESTINATARIO("ricercaDaDestinatario", TipoFiltro.SINGOLO, TipoDato.BOOLEANO), //
		DESTINATARIO_UTENTE("destinatarioUtente", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		DESTINATARIO_GRUPPO("destinatarioGruppo", TipoFiltro.LISTA, TipoDato.STRINGA), //
		STATO_RICHIESTA_DESTINATARIO("statoRichiestaDestinatario", TipoFiltro.LISTA, TipoDato.STRINGA), //
		MITTENTE_ORIGINALE("mittenteOriginale", TipoFiltro.SINGOLO, TipoDato.STRINGA), //
		OGGETTO_RICHIESTA("oggettoRichiesta", TipoFiltro.SINGOLO, TipoDato.STRINGA);

		private String filtro;
		private TipoFiltro tipoFiltro;
		private TipoDato[] tipiDato;

		private FiltriRicerca(String filtro, TipoFiltro tipoFiltro, TipoDato... tipiDato) {
			this.filtro = filtro;
			this.tipoFiltro = tipoFiltro;
			this.tipiDato = tipiDato;
		}
	}

}
