package it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni;

import java.util.List;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.collegamenti.Permessi;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

public interface CollegaFascicoloTaskApi extends ITaskApi {

	/**
	 *
	 * @param fascicoloRemoto fascicolo con cui condividere
	 * @param operazioniRemotePerUtenteLocale operazioni che sono consentite sul fascicolo locale dal fascicolo remoto
	 * @throws PraticaException
	 */
	public void collegaFascicolo(Fascicolo fascicoloRemoto, List<String> ruoliUtente, List<String> operazioniRemotePerUtenteLocale) throws PraticaException;

	/**
	 *
	 * @param fascicoloRemoto fascicolo con cui condividere
	 * @param permessi contiene le operazioni che sono consentite sul fascicolo locale dal fascicolo remoto e i permessi di lettura
	 * @throws PraticaException
	 */
	public void collegaFascicolo(Fascicolo fascicoloRemoto, List<String> ruoliUtente, Permessi permessi) throws PraticaException;

}
