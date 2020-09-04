package it.eng.consolepec.spagicclient;

import it.eng.consolepec.spagicclient.bean.request.inviomassivo.ComunicazioneRequest;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.remoteproxy.result.LockedPratica;

public interface SpagicClientGestioneComunicazioniMassive {

	LockedPratica creaInvio(String pathComunciazione, String csv, Utente utente) throws SpagicClientException;

	LockedPratica creaInvioTest(String pathComunciazione, String csv, String destinatarioTest, int numeroTest, Utente utente) throws SpagicClientException;

	LockedPratica createComunicazione(ComunicazioneRequest crequest, Utente utente) throws SpagicClientException;

}
