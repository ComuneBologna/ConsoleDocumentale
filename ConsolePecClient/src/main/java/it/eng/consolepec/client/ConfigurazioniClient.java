package it.eng.consolepec.client;

import java.util.Date;
import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 *
 * @author biagiot
 *
 */
public interface ConfigurazioniClient {

	List<AnagraficaFascicolo> getAnagraficheFascicoli(Utente utente) throws SpagicClientException;

	List<AnagraficaIngresso> getAnagraficheIngressi(Utente utente) throws SpagicClientException;

	List<AnagraficaModello> getAnagraficheModelli(Utente utente) throws SpagicClientException;

	List<AnagraficaEmailOut> getAnagraficheMailInUscita(Utente utente) throws SpagicClientException;

	List<AnagraficaComunicazione> getAnagraficheComunicazioni(Utente utente) throws SpagicClientException;

	List<AnagraficaPraticaModulistica> getAnagrafichePraticaModulistica(Utente utente) throws SpagicClientException;

	List<AnagraficaRuolo> getAnagraficheRuoli(Utente utente) throws SpagicClientException;

	List<Settore> getSettori(Utente utente) throws SpagicClientException;

	ProprietaGenerali getProprietaGenerali(Utente utente) throws SpagicClientException;

	List<AnagraficaRuolo> getRuoliPersonali(Utente utente) throws SpagicClientException;

	List<AbilitazioniRuolo> getAbilitazioniRuoli(Utente utente) throws SpagicClientException;

	List<String> getEmailAssegnaEsterno(Utente utente) throws SpagicClientException;

	List<Abilitazione> getAbilitazioniRuoloCartellaDiFirma(AnagraficaRuolo ruolo, Utente utente) throws SpagicClientException;

	List<String> getRuoliVisibilita(String tipologiaPratica, Utente utente) throws SpagicClientException;

	List<AnagraficaRuolo> getRuoliSuperutentiModifica(AnagraficaRuolo ar, Utente utente) throws SpagicClientException;

	List<AnagraficaRuolo> getRuoliSuperutentiMatriceVisibilita(AnagraficaRuolo ar, Utente utente) throws SpagicClientException;

	List<AnagraficaRuolo> getRuoliSuperutentiMatriceVisibilita(String tipologiaPratica, Utente utente) throws SpagicClientException;

	Date getLastEditDate(Utente utente) throws SpagicClientException;

}
