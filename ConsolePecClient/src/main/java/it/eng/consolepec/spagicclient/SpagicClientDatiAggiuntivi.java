package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.bean.request.datiaggiuntivi.DatoAggiuntivo;
import it.eng.consolepec.spagicclient.bean.response.validazione.ValidazioneEdErroriDatiAggiuntivi;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

import java.util.List;

public interface SpagicClientDatiAggiuntivi {

	public LockedPratica aggiungiDatiAggiuntivi(String pathPratica, List<DatoAggiuntivo> valoriAggiuntivi, Utente utente) throws SpagicClientException;
	public LockedPratica rimuoviDatiAggiuntivi(String pathPratica, List<String> nomiCampi, Utente utente) throws SpagicClientException;
	public ValidazioneEdErroriDatiAggiuntivi validaDatiAggiuntivi(List<DatoAggiuntivo> valoriAggiuntivi, Utente utente) throws SpagicClientException;
}
