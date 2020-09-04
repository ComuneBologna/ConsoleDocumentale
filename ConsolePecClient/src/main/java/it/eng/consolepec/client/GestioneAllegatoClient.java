package it.eng.consolepec.client;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

/**
 * @author GiacomoFM
 * @since 15/mar/2018
 */
public interface GestioneAllegatoClient {

	void inserisciModificaDatiAggiuntivi(final Utente utente, final String idDocumentale, final String nomeAllegato, final List<DatoAggiuntivo> datiAggiuntivi) throws SpagicClientException;

	void eliminaDatiAggiuntivi(final Utente utente, final String idDocumentale, final String nomeAllegato, final List<String> nomiDatiAggiuntivi) throws SpagicClientException;

	LockedPratica modificaTipologieAllegati(final Utente utente, final String idDocumentale, final Map<String, List<String>> mappaAllegatoTipologie) throws SpagicClientException;

}
