package it.eng.portlet.consolepec.spring.bean.profilazione;

import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist.Counter;
import it.eng.cobo.consolepec.commons.configurazioni.Operazione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.RuoloAbilitazione;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeFirmaDigitale;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import it.eng.cobo.consolepec.commons.pratica.OperativitaRidotta;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler;
import it.eng.consolepec.xmlplugin.factory.ITipoApiTask;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Collegamento;
import it.eng.portlet.consolepec.spring.bean.session.user.UserException;
import lombok.NonNull;

/**
 * 
 * @author biagiot
 *
 */
public interface GestioneProfilazioneUtente {

	Utente getDatiUtente();

	AutorizzazioneHandler getAutorizzazioniUtente();

	PreferenzeUtente getPreferenzeUtente();

	PreferenzeFirmaDigitale getPreferenzeFirmaDigitale();

	PreferenzeRiassegnazione getPreferenzeRiassegnazione();

	void aggiornaPreferenzeUtente(PreferenzeUtente preferenzeUtente);

	void aggiornaPreferenzeRiassegnazione(PreferenzeRiassegnazione preferenzeRiassegnazione) throws UserException;

	void aggiornaPreferenzeFirmaDigitale(PreferenzeFirmaDigitale preferenzeFirmaDigitale) throws UserException;

	void eliminaPreferenzeRiassegnazione() throws UserException;

	void eliminaPreferenzeFirmaDigitale() throws UserException;

	Map<AnagraficaWorklist, Counter> getWorklistAbilitate(boolean reload);

	<T extends RuoloAbilitazione> boolean isSuperutenteAbilitato(@NonNull String ruoloSubordinato, @NonNull Class<T> clazz);

	List<Operazione> getOperazioniAbilitate(TipologiaPratica tipologiaPratica);

	boolean isOperazioneAbilitata(TipologiaPratica tipologiaPratica, Task<?> task, ITipoApiTask operazione, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniSuperutenti,
			List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniAssegnazioneEsterna, List<Collegamento> collegamenti, OperativitaRidotta operativitaRidotta,
			String indirizzoEmail);

	boolean isOperazioneAbilitata(TipologiaPratica tipologiaPratica, Task<?> task, ITipoApiTask operazione, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniSuperutenti,
			List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniAssegnazioneEsterna, List<Collegamento> collegamenti, OperativitaRidotta operativitaRidotta,
			String indirizzoEmail, boolean checkAbilitazioneLettura);

	boolean checkOperazioneVisibilita(TipologiaPratica tipologiaPratica, AnagraficaRuolo assegnatario, ITipoApiTask operazione, OperativitaRidotta operativitaRidotta);

	boolean isOperazioneSuperutenteAbilitata(String ruoloAssegnatario, ITipoApiTask tipoApiTask, List<it.eng.consolepec.xmlplugin.factory.DatiPratica.Operazione> operazioniSuperutente,
			TipologiaPratica tipologiaPratica, String indirizzoEmail);

	boolean isRuoloUtenteAbilitato(AnagraficaRuolo ruoloAssegnatario);
}
