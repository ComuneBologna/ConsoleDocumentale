package it.eng.consolepec.spagicclient;

import java.util.List;

import it.eng.cobo.consolepec.commons.dto.TaskFirmaDocumentale;
import it.eng.consolepec.client.RicercaPraticheClient;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;
import it.eng.consolepec.spagicclient.search.SearchObjectResult;
import it.eng.consolepec.spagicclient.search.SearchPraticheFilter;
import it.eng.consolepec.spagicclient.search.SearchPraticheSort;

/**
 * Per utilizzo interno. Per ricerche esterne {@link RicercaPraticheClient}
 */
public interface SpagicClientNewSearchPratiche {

	public List<SearchObjectResult> search(SearchPraticheFilter filter, SearchPraticheSort searchPraticheSort, int limit, int offset, Utente utente) throws SpagicClientException;

	public List<SearchObjectResult> search(SearchPraticheFilter filter, int limit, int offset, Utente utente) throws SpagicClientException;

	public int count(SearchPraticheFilter filter, Utente utente) throws SpagicClientException;

	public List<TaskFirmaDocumentale> searchRichiesteFirma(SearchPraticheFilter filter, SearchPraticheSort searchPraticheSort, int limit, int offset, Utente utente) throws SpagicClientException;

	public int countRichiesteFirma(SearchPraticheFilter filter, Utente utente) throws SpagicClientException;

}
