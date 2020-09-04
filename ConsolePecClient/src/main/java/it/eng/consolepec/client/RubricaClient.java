package it.eng.consolepec.client;

import java.util.Map;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.services.AnagraficaResponse;
import it.eng.cobo.consolepec.commons.services.ServiceResponse;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

public interface RubricaClient {

	ServiceResponse<AnagraficaResponse> ricerca(Map<String, Object> filtri, Integer limit, Integer offset, Utente utente) throws SpagicClientException;

	ServiceResponse<AnagraficaResponse> crea(Anagrafica anagrafica, Utente utente) throws SpagicClientException;

	ServiceResponse<AnagraficaResponse> modifica(Anagrafica anagrafica, Utente utente) throws SpagicClientException;

	ServiceResponse<AnagraficaResponse> elimina(Anagrafica anagrafica, Utente utente) throws SpagicClientException;

}
