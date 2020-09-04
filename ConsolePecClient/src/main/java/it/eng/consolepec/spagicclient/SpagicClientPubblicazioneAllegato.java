package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

import java.util.Date;
import java.util.List;

public interface SpagicClientPubblicazioneAllegato {
	
	public LockedPratica pubblicaAllegato(String praticaPath, String allegatoNome, Date dataInizio, Date dataFine, List<String> destinatari, String testo, Utente utente) throws SpagicClientException;
	
	public LockedPratica rimuoviPubblicazioneAllegato(String praticaPath, String allegatoNome, Utente utente) throws SpagicClientException;

}
