package it.eng.consolepec.client;

import java.util.List;

import it.eng.cobo.consolepec.commons.datigenerici.IndirizzoEmail;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface IndirizziEmailRubricaClient {

	public boolean inserisci(IndirizzoEmail indirizzoEmail, Utente utente) throws SpagicClientException;

	public List<IndirizzoEmail> getIndirizziEmail(Utente utente) throws SpagicClientException;
}
