package it.eng.portlet.consolepec.spring.bean.configurazioni;

import java.util.List;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.consolepec.spagicclient.Utente;
import it.eng.consolepec.spagicclient.remoteproxy.exception.SpagicClientException;

/**
 *
 * @author biagiot
 *
 */
public interface GestioneConfigurazioni {

	void init() throws SpagicClientException;

	void reload(Utente utente) throws SpagicClientException;

	/*
	 * Utility configurazioni
	 */

	List<AnagraficaFascicolo> getAnagraficheFascicoli();

	List<AnagraficaIngresso> getAnagraficheIngressi();

	List<AnagraficaModello> getAnagraficheModelli();

	List<AnagraficaEmailOut> getAnagraficheMailInUscita();

	List<AnagraficaComunicazione> getAnagraficheComunicazioni();

	List<AnagraficaPraticaModulistica> getAnagrafichePraticaModulistica();

	List<Settore> getSettori();

	ProprietaGenerali getProprietaGenerali();

	List<AnagraficaRuolo> getAnagraficheRuoliPersonali();

	List<AnagraficaRuolo> getAnagraficheRuoli();

	List<AbilitazioniRuolo> getAbilitazioniRuoli();

	AnagraficaFascicolo getAnagraficaFascicolo(String nomeTipologia);

	AnagraficaFascicolo getAnagraficaFascicoloByEtichetta(String etichetta);

	AnagraficaIngresso getAnagraficaIngresso(String tipologia, String indirizzo);

	AnagraficaModello getAnagraficaModello(String tipologia);

	AnagraficaEmailOut getAnagraficaMailInUscita(String tipologia, String indirizzo);

	AnagraficaComunicazione getAnagraficaComunicazione(String nomeTipologia);

	AnagraficaPraticaModulistica getAnagraficaPraticaModulistica(String nomeTipologia);

	AnagraficaRuolo getAnagraficaRuoloByEtichetta(String etichetta);

	AnagraficaRuolo getAnagraficaRuolo(String ruolo);

	AnagraficaRuolo getAnagraficaRuolo(String ruolo, boolean checkRuoliPersonali);

	List<AnagraficaRuolo> getAnagraficheRuoli(Utente utente, boolean checkAttivo);

	List<String> controlloValiditaAbilitazioni(AbilitazioniRuolo abilitazioniRuolo);

	String getBaseUrlPubblicazioneAllegati();

	List<String> getEmailAssegnaEsterno();

	void addRuoloPersonale(Utente utente);

	/*
	 * Caricamento/Reload configurazioni (Amministratori)
	 */
	void reloadRuoli(Utente utente) throws SpagicClientException;

	void reloadAbilitazioniRuoli(Utente utente) throws SpagicClientException;

	void reloadAnagraficheFascicoli(Utente utente) throws SpagicClientException;

	void reloadAnagraficheIngressi(Utente utente) throws SpagicClientException;

	/*
	 * Altro
	 */
	boolean isCartellaFirmaRiassegnabile(AnagraficaRuolo anagraficaRuolo);

	List<AnagraficaRuolo> getRuoliVisibilita(TipologiaPratica tipoPratica) throws InvalidArgumentException;

	AbilitazioniRuolo getAbilitazioniRuolo(String ruolo);

	List<AnagraficaRuolo> getRuoliSuperutentiModifica(String ruolo);

	List<AnagraficaRuolo> getRuoliSuperutentiMatriceVisibilita(String ruolo);

	List<AnagraficaRuolo> getRuoliSuperutentiMatriceVisibilita(TipologiaPratica tipoPratica);

	void reloadSettori(Utente utenteSpagic) throws SpagicClientException;

}
