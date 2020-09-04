package it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni;

import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Ricevuta;

/**
 * @author Giacomo F.M.
 * @since 2019-09-17
 */
public interface PecConsegnataTaskApi {

	void pecConsegnata(Ricevuta ricevutaConsegna);

}
