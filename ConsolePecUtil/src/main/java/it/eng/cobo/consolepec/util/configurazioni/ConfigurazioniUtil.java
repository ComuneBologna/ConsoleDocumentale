package it.eng.cobo.consolepec.util.configurazioni;

public class ConfigurazioniUtil {

	/*
	 * Nomi tipologie
	 */
	public static final String NOME_TIPOLOGIA_ANAGRAFICA_INGRESSO = "ANAGRAFICA_INGRESSO";
	public static final String NOME_TIPOLOGIA_ANAGRAFICA_FASCICOLO = "ANAGRAFICA_FASCICOLO";
	public static final String NOME_TIPOLOGIA_ANAGRAFICA_COMUNICAZIONE = "ANAGRAFICA_COMUNICAZIONE";
	public static final String NOME_TIPOLOGIA_ANAGRAFICA_PRATICA_MODULISTICA = "ANAGRAFICA_PRATICA_MODULISTICA";
	public static final String NOME_TIPOLOGIA_ANAGRAFICA_MAIL_OUT = "ANAGRAFICA_MAIL_OUT";
	public static final String NOME_TIPOLOGIA_ANAGRAFICA_MODELLO = "ANAGRAFICA_MODELLO";
	public static final String NOME_TIPOLOGIA_ANAGRAFICA_WORKLIST = "ANAGRAFICA_WORKLIST";
	public static final String NOME_TIPOLOGIA_ANAGRAFICA_RUOLO = "ANAGRAFICA_RUOLO";
	public static final String NOME_TIPOLOGIA_SETTORE = "SETTORE";
	public static final String NOME_TIPOLOGIA_PREFERENZE_UTENTE = "PREFERENZE_UTENTE";
	public static final String NOME_TIPOLOGIA_PROPERIETA_GENERALI = "PROPRIETA_GENERALI";
	public static final String NOME_TIPOLOGIA_ABILITAZIONE_RUOLO = "ABILITAZIONE_RUOLO";
	/*
	 * Filtri proiettabili
	 */
	public static final String FILTRO_PROJECTION_PASSWORD = "password";

	/*
	 * Filtri ricerca
	 */
	// Comuni
	public static final String FILTRO_RICERCA_CONFIGURAZIONE_TIPO_PRATICA = "nomeTipologia";
	public static final String FILTRO_RICERCA_CONFIGURAZIONE_ETICHETTA_PRATICA = "etichettaTipologia";
	public static final String FILTRO_RICERCA_CONFIGURAZIONE_STATO = "stato";
	public static final String FITRO_RICERCA_CONFIGURAZIONE_TIPOLOGIA = "tipologiaConfigurazione";
	public static final String FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE_DA = "dataCreazioneDa";
	public static final String FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE_A = "dataCreazioneA";
	public static final String FILTRO_RICERCA_CONFIGURAZIONE_DATA_CREAZIONE = "dataCreazione";

	// Ruoli
	public static final String FILTRO_RICERCA_RUOLI_ETICHETTA = "etichetta";
	public static final String FILTRO_RICERCA_RUOLI_NOME = "ruolo";

	// Ingressi
	public static final String FILTRO_RICERCA_INGRESSI_SERVER = "server";
	public static final String FILTRO_RICERCA_INGRESSI_INDIRIZZO = "indirizzo";

	// Fascicoli
	public static final String FILTRO_RICERCA_FASCICOLI_PROTOCOLLABILE = "protocollabile";

	// Abilitazioni
	public static final String FILTRO_RICERCA_ABILITAZIONI_RUOLO_ABILITAZIONI = "abilitazioni";
	public static final String FILTRO_RICERCA_ABILITAZIONI_RUOLO_RUOLO = "ruolo";
	public static final String FILTRO_RICERCA_ABILITAZIONI_RUOLO_TIPO_ABILITAZIONE = "abilitazioni.tipoAbilitazione";
	public static final String FILTRO_RICERCA_ABILITAZIONI_RUOLO_ABILITAZIONE_RUOLO = "abilitazioni.ruolo";
	public static final String FILTRO_RICERCA_ABILITAZIONI_RUOLO_ABILITAZIONE_PRATICA_TIPO = "abilitazioni.tipo";
	public static final String FILTRO_RICERCA_ABILITAZIONI_DATA_CREAZIONE_DA = "abilitazioni.dataCreazioneDa";
	public static final String FILTRO_RICERCA_ABILITAZIONI_DATA_CREAZIONE_A = "abilitazioni.dataCreazioneA";
	public static final String FILTRO_RICERCA_ABILITAZIONI_DATA_CREAZIONE = "abilitazioni.dataCreazione";
}
