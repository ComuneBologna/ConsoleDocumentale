package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Stato;
import it.eng.consolepec.xmlplugin.tasks.eventiiter.EventiIter;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.ITaskApi;
import it.eng.consolepec.xmlplugin.tasks.operazioni.AbstractTaskApi;

public abstract class AbstractTaskApiImpl<T extends DatiGestioneFascicoloTask> extends AbstractTaskApi implements ITaskApi {

	protected Logger log = LoggerFactory.getLogger(AbstractTaskApiImpl.class);

	protected XMLTaskFascicolo<T> task;

	public AbstractTaskApiImpl(XMLTaskFascicolo<T> task) {
		super(task);
		this.task = task;
	}

	@Override
	protected boolean controllaAbilitazioneGenerica() {

		// abilitazione generica per tutti i fascicoli

		if (task.isUtenteEsterno() == false && getDatiFascicolo().getStato().equals(Stato.DA_INOLTRARE_ESTERNO)) {
			return false;
		}
		return true;
	}

	protected DatiFascicolo getDatiFascicolo() {
		return (DatiFascicolo) task.getEnclosingPratica().getDati();
	}

	public static enum EventiIterFascicolo implements EventiIter {

		RIASSEGNA(true, "L''utente {0} ha riassegnato il fascicolo da {1} a {2}.{3, choice, 0#|1# Ha attribuito il fascicolo all''''operatore {4}}"),
		//
		RIASSEGNA_CON_NOTIFICA(true, "L''utente {0} ha riassegnato il fascicolo da {1} a {2}. La notifica verra'' inviata a: {3}.{4, choice, 0#|1# Ha attribuito il fascicolo all''''operatore {5}}"),
		//
		PROTOCOLLA(true, "Il fascicolo assegnato a {0} e'' stato protocollato dall''utente {1} con PG {2}/{3}."),
		//
		IN_AFFISSIONE(true, "L''utente {0} ha messo il fascicolo in affissione."),
		//
		CARICA_ALLEGATO(false, "L''utente {0} ha caricato l''allegato {1}."),
		//
		FIRMA_ALLEGATO(false, "L''utente {0} ha firmato l''allegato {1}."),
		//
		ELIMINA_ALLEGATO(false, "L''utente {0} ha eliminato l''allegato {1}."),
		//
		PRENDI_IN_CARICO(false, "L''utente {0} ha preso in carico il fascicolo."),
		//
		RILASCIA_IN_CARICO(false, "L''utente {0} ha rilasciato il fascicolo."),
		//
		RIPORTA_IN_GESTIONE(false, "L''utente {0} ha riportato in gestione il fascicolo."),
		//
		AGGANCIA_A_PRATICA(false, "L''utente {0} ha agganciato il fascicolo alla {1} {2}."),
		//
		CONCLUDI(false, "L''utente {0} ha concluso il fascicolo."),
		//
		ANNULLA(false, "L''utente {0} ha annullato il fascicolo."),
		//
		PROTOCOLLA_CON_CAPOFILA(true, "L''utente {0} ha protocollato il fascicolo con PG {1}/{2} e capofila {3}/{4}."),
		//
		COLLEGA_FASCICOLO(true, "L''utente {0} ha collegato il fascicolo al fascicolo {1}."),
		//
		ELIMINA_COLLEGAMENTO_FASCICOLO(true, "L''utente {0} ha eliminato il collegamento con il fascicolo: {1}."),
		//
		CONDIVIDI_FASCICOLO(true, "L''utente {0} ha condiviso il fascicolo con il gruppo: {1}."),
		//
		AGGIUNTA_VISIBILITA_FASCICOLO(true, "L''utente {0} ha esteso la visibilita'' del fascicolo ai gruppi: {1}."),
		//
		RIMOZIONE_VISIBILITA_FASCICOLO(true, "L''utente {0} ha rimosso la visibilita'' del fascicolo ai gruppi: {1}."),
		//
		MODIFICA_CONDIVIDI_FASCICOLO(true, "L''utente {0} ha modificato la condivisione del fascicolo con il gruppo: {1}."),
		//
		ELIMINA_CONDIVISIONE_FASCICOLO(true, "L''utente {0} ha eliminato la condivisone con il gruppo: {1}."),
		//
		AVVIA_PROCEDIMENTO(true, "L''utente {0} ha avviato il procedimento con codice {1} per il capofila {2}/{3}."),
		//
		CHIUDI_PROCEDIMENTO(true, "L''utente {0} ha concluso il procedimento con codice {1} per il capofila {2}/{3}."),
		//
		CAMBIA_TIPO_FASCICOLO(true, "L''utente {0} ha cambiato il tipo fascicolo da {1} a {2}."),
		//
		SGANCIA_PEC_IN(false, "L''utente {0} ha eliminato l''e-mail {1} dal fascicolo {2}."),
		//
		CAMBIA_STATO_FASCICOLO(false, "L''utente {0} ha cambiato lo stato del fascicolo {1} in {2}."),
		//
		AGGIUNGI_INFO(false, "L''utente {0} ha aggiunto delle informazioni."),
		//
		RIMUOVI_INFO(false, "L''utente {0} ha rimosso delle informazioni."),
		//
		ELIMINA(false, "L''utente {0} ha portato il fascicolo in stato di eliminazione."),
		//
		ASSEGNA_UTENTE_ESTERNO(true, "L''utente {0} ha assegnato il fascicolo ad un gruppo esterno. La notifica verra'' inviata a: {1}."),
		//
		RITORNA_DA_INOLTRARE_ESTERNO(false, "L''utente {0} ha riportato il fascicolo in gestione da inoltro a esterno"),
		//
		MODIFICA_ABILITAZIONI_ASSEGNA_UTENTE_ESTERNO(false, "L''utente {0} ha modificato le abilitazioni per l''accesso da utente esterno"),
		//
		ACCESSO_UTENTE_ESTERNO(false, "L''utente esterno {0} ha visualizzato il fascicolo"),
		//
		ELIMINA_PRATICA_COLLEGATA(false, "L''utente {0} ha eliminato una mail in bozza dal fascicolo"),
		//
		MODIFICA_DATO_AGGIUNTIVO(false, "L''utente {0} ha modificato il dato aggiuntivo {1} da {2} a {3}"),
		//
		MODIFICA_DATO_AGGIUNTIVO_TABELLA(false, "L''utente {0} ha modificato il dato aggiuntivo {1}"),
		//
		MODIFICA_OPERATORE(true, "L''utente {0} ha attribuito il fascicolo all''operatore {1}."),
		//
		CAMBIA_STEP_ITER(false, "L''utente {0} ha cambiato l''iter del fascicolo in {1}"),
		//
		CAMBIA_STEP_ITER_CON_NOTIFICA(false, "L''utente {0} ha cambiato l''iter del fascicolo in {1}. La notifica verra'' inviata a: {2}."),
		//
		RICHIEDI_APPROVAZIONE_FIRMA(false, "L''utente {0} ha effettuato la proposta di approvazione dell''allegato dell''allegato {1}."),
		//
		RICHIEDI_APPROVAZIONE_FIRMA_CON_NOTIFICA(false, "L''utente {0} ha effettuato la proposta di approvazione dell''allegato {1}. La notifica verra'' inviata a: {2}."),
		//
		RITIRA_APPROVAZIONE_FIRMA(false, "L''utente {0} ha ritirato la proposta di approvazione dell''allegato {1}."),
		//
		RITIRA_APPROVAZIONE_FIRMA_CON_NOTIFICA(false, "L''utente {0} ha ritirato la proposta di approvazione dell''allegato {1}. La notifica verra'' inviata a: {2}."),
		//
		RICARICA_ALLEGATO_PROTOCOLLATO(false, "L''utente {0} ha protocollato il modello di documento \"{1}\" aggiungendo i campi: {2}."),
		//
		MODIFICA_TITOLO(false, "L''utente {0} ha modificato il fascicolo cambiando il titolo da \"{1}\" a \"{2}\"."),
		//
		AGGIORNA_PG_OGGETTO(false, "L''utente {0} ha aggiornato la protocollazione {1}/{2} cambiando " //
				+ "{3,choice,0#l''''oggetto da {4} a {5} |1#}" //
				+ "{6,choice,0#la provenienza da {7} a {8} |1#}" //
				+ "{9,choice,0#il titolo da {10} a {11} |1#}" //
				+ "{12,choice,0#la rubrica da {13} a {14} |1#}" //
				+ "{15,choice,0#la sezione da {16} a {17} |1#}" //
				+ "{18,choice,0#la data di protocollazione da {19,date,dd/MM/yyyy} a {20,date,dd/MM/yyyy}|1#}"),
		//
		AGGIORNA_PG_SENZA_MODIFICHE(false, "L''utente {0} ha richiesto l''allineato dei dati di protcollazione per il PG {1}/{2} - nessuna modifica riscontrata"),
		//
		AGGIUNTA_DATI_AGGIUNTIVI(false, "L''utente {0} ha modificato il fascicolo aggiungendo i seguenti dati aggiuntivi: {1}"),
		//
		MODIFICA_DATI_AGGIUNTIVI(false, "L''utente {0} ha modificato il fascicolo modificando i seguenti dati aggiuntivi: {1}"),
		//
		RIMUOVI_DATI_AGGIUNTIVI(false, "L''utente {0} ha modificato il fascicolo rimuovendo i seguenti dati aggiuntivi: {1}"),
		//
		AGGIUNTA_TIPOLOGIA_ALLEGATO(false, "L''utente {0} ha aggiunto la tipologia {1} all''allegato {2}."),
		//
		COLLEGA_PRATICA_PROCEDI(false, "L''utente {0} ha collegato il fascicolo alla pratica procedi con id: {1}"),
		//
		ELIMINA_COLLEGA_PRATICA_PROCEDI(false, "L''utente {0} ha eliminato il collegamento del fascicolo alla pratica procedi con id: {1}"),
		//
		MODIFICA_TIPOLOGIE_ALLEGATO(false, "L''utente {0} ha modificato le tipologie dell''allegato {1} impostandole su: {2}"),
		//
		APPLICA_OPERATIVITA_RIDOTTA(false, "L''utente {0} ha applicato la operativita'' ridotta sul fascicolo. Operazione {1} consentita solo per {2}"),
		//
		TAGLIA_ALLEGATO(false, "L''utente {0} ha spostato l''allegato {1} nella pratica {2}."),
		//
		TAGLIA_ALLEGATO_PROTOCOLLATO(false, "L''utente {0} ha spostato l''allegato protocollato {1} con PG {2}/{3} nella pratica {4}."),
		//
		INCOLLA_ALLEGATO(false, "L''utente {0} spostato l''allegato {1} dalla pratica {2}."),
		//
		INCOLLA_ALLEGATO_PROTOCOLLATO(false, "L''utente {0} ha spostato l''allegato protocollato {1} con PG {2}/{3} dalla pratica {4}."),
		//
		TAGLIA_PRATICA_PROTOCOLLATA(false, "L''utente {0} ha spostato la pratica protocollata {1} con PG {2}/{3} nella pratica {4}."),
		//
		INCOLLA_PRATICA_PROTOCOLLATA(false, "L''utente {0} ha spostato la pratica protocollata {1} con PG {2}/{3} dalla pratica {4}."),
		//
		MODIFICA_NOTE(false, "L''utente {0} ha modificato le note del fascicolo."),
		//
		AGGIUNTA_NOTE(false, "L''utente {0} ha aggiunto delle note al fascicolo."),
		//
		INVIA_MAIL_DA_CSV(false, "L''utente {0} ha generato un invio multiplo di mail/PEC a partire dall''allegato {1}."),
		//
		CREA_BOZZA(false, "L''utente {0} ha creato una mail in bozza con identificativo documentale {1} nel fascicolo.");

		private EventiIterFascicolo(boolean isSerializzazioneAbilitata, String testoDaSerializzare) {
			this.isSerializzazioneAbilitata = isSerializzazioneAbilitata;
			this.testoDaSerializzare = testoDaSerializzare;
		}

		private boolean isSerializzazioneAbilitata;
		private String testoDaSerializzare;

		@Override
		public boolean isSerializzazioneAbilitata() {
			return isSerializzazioneAbilitata;
		}

		@Override
		public String getTestoDaSerializzare() {
			return testoDaSerializzare;
		}

	}

	@Override
	protected abstract boolean controllaAbilitazioneInterna();

}
