package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;

public enum TipoApiTaskPEC implements ITipoApiTask {

	ARCHIVIA,
	//
	ELIMINA,
	//
	ELIMINA_BOZZA,
	//
	RIASSEGNA,
	//
	AGGANCIA_A_FASCICOLO,
	//
	AGGANCIA_A_PEC,
	//
	AGGANCIA_PRATICA,
	//
	CREA_FASCICOLO,
	//
	FIRMA,
	//
	AGGIUNGI_ALLEGATO,
	//
	RIMUOVI_ALLEGATO,
	//
	RICONSEGNA,
	//
	RIPORTA_IN_LETTURA,
	//
	IMPORTA_ELETTORALE,
	//
	ANNULLA_ELETTORALE,
	//
	SCARTA,
	//
	NOTIFICA,
	//
	RISPOSTA_INTEROPERABILE,
	//
	MODIFICA_OPERATORE,
	//
	MANCATA_CONSEGNA_REINOLTRO,
	//
	INVIA_PEC,
	//
	PEC_IN_ATTESA_PRESA_IN_CARICO,
	//
	PEC_INVIATA,
	//
	PEC_NON_INVIATA,
	//
	MODIFICA_BOZZA,
	// Gestione Ricevute
	PEC_ACCETTATA, PEC_NON_ACCETTATA, PEC_CONSEGNATA, PEC_PREAVVISO_MANCATA_CONSEGNA, PEC_NON_CONSEGNATA,
	//
	MODIFICA_NOTE;

}
