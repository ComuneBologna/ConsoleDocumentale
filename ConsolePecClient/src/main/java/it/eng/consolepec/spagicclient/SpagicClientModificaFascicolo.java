package it.eng.consolepec.spagicclient;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

import java.util.List;

/**
 * @author GiacomoFM
 * @since 10/lug/2017
 */
public interface SpagicClientModificaFascicolo {

	/**
	 * Aggiorna il titolo del fascicolo selezionato
	 * 
	 * @param pathFascicolo
	 * @param nuovoTitolo
	 * @param nuovoTipoFascicolo
	 * @param datiAggiuntivi
	 * @param utente
	 */
	LockedPratica modifica(String pathFascicolo, String nuovoTitolo, String nuovoTipoFascicolo, List<DatoAggiuntivo> datiAggiuntivi, Utente utente);

}
