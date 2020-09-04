package it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle;

import java.util.ArrayList;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;

/**
 * 
 * @author biagiot
 *
 */
public class AnagraficheRuoliAbilitatiSuggestOracle<T extends Abilitazione> extends AnagraficheRuoliSuggestOracle {
	
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	
	public AnagraficheRuoliAbilitatiSuggestOracle(Class<T> tipoAbilitazione, QueryAbilitazione<T> qab, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(profilazioneUtenteHandler.getAnagraficheRuoliAbilitati(tipoAbilitazione, qab));
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}
	
	public void filtra(Class<T> tipoAbilitazione, QueryAbilitazione<T> qab) {
		this.anagraficheRuoli = new ArrayList<AnagraficaRuolo>(profilazioneUtenteHandler.getAnagraficheRuoliAbilitati(tipoAbilitazione, qab));
	}
	
	
}
