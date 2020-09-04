package it.eng.portlet.consolepec.gwt.client.handler.configurazioni;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.Timer;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.IngressoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.LetturaIngressoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.RuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.portlet.consolepec.gwt.client.PostLoadingAction;
import it.eng.portlet.consolepec.gwt.client.event.UpdateSiteMapEvent;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;

/**
 *
 * @author biagiot
 *
 */
public class ConfigurazioniHandler {

	private AnagrafichePraticheHandler anagrafichePraticheHandler;
	private AnagraficheRuoliHandler anagraficheRuoliHandler;
	private SettoriHandler settoriHandler;
	private ProprietaGeneraliHandler proprietaGeneraliHandler;
	private EventBus eventBus;

	@Inject
	public ConfigurazioniHandler(ProprietaGeneraliHandler proprietaGeneraliHandler, AnagrafichePraticheHandler anagraficheFascicoliHandler, AnagraficheRuoliHandler anagraficheRuoliHandler,
			SettoriHandler settoriHandler, EventBus eventBus) {
		super();
		this.proprietaGeneraliHandler = proprietaGeneraliHandler;
		this.anagrafichePraticheHandler = anagraficheFascicoliHandler;
		this.anagraficheRuoliHandler = anagraficheRuoliHandler;
		this.settoriHandler = settoriHandler;
		this.eventBus = eventBus;
	}

	public void reloadAnagrafichePratiche(final ConfigurazioniCallback callbackC, boolean ricarica) {
		ConfigurazioniCallback callback = null;

		if (callbackC != null) {
			callback = new ConfigurazioniCallback() {

				int loading = 0;

				@Override
				public void onSuccess() {
					loading++;
					if (loading == 1) {
						callbackC.onSuccess();
					}
				}

				@Override
				public void onFailure(String error) {
					callbackC.onFailure(ConsolePecConstants.ERROR_MESSAGE);
				}
			};
		}

		caricaAnagrafichePratiche(callback, ricarica);
	}

	public void reloadRuoli(final ConfigurazioniCallback callbackC, boolean ricarica) {

		ConfigurazioniCallback callback = null;

		if (callbackC != null) {
			callback = new ConfigurazioniCallback() {

				int loading = 0;

				@Override
				public void onSuccess() {
					loading++;
					if (loading == 2) {
						callbackC.onSuccess();
					}
				}

				@Override
				public void onFailure(String error) {
					callbackC.onFailure(ConsolePecConstants.ERROR_MESSAGE);
				}
			};
		}

		caricaAnagraficheRuoli(callback, ricarica);
		caricaSettori(callback, ricarica);
		caricaAbilitazioniRuoli();
	}

	public void reloadRuoli() {
		reloadRuoli(null, false);
	}

	public void reloadAnagrafichePratiche() {
		caricaAnagrafichePratiche(null, false);
	}

	// //////////////////INIT HANDLER ////////////////////
	public void init(final PostLoadingAction postLoadingAction) {
		ConfigurazioniCallback callback = new ConfigurazioniCallback() {

			int loading = 0;

			@Override
			public void onSuccess() {
				loading++;
				if (loading == 4 && postLoadingAction != null) {
					if (proprietaGeneraliHandler.getProprietaGenerali().getIntervalloAggiornamentoWorklist() != null) {
						Timer timer = new Timer() {
							@Override
							public void run() {
								eventBus.fireEvent(new UpdateSiteMapEvent(true));
							}
						};
						timer.scheduleRepeating(proprietaGeneraliHandler.getProprietaGenerali().getIntervalloAggiornamentoWorklist());
					}
					postLoadingAction.onComplete();
				}
			}

			@Override
			public void onFailure(String error) {
				throw new IllegalArgumentException(error);
			}
		};
		caricaProprietaGenerali(callback);
		caricaAnagraficheRuoli(callback, false);
		caricaAnagrafichePratiche(callback, false);
		caricaSettori(callback, false);
	}

	// ///////////////// AMMINISTRAZIONE ///////////////////
	public void caricaAbilitazioniRuoli() {
		anagraficheRuoliHandler.caricaAbilitazioniRuoli(null);
	}

	public void caricaAbilitazioniRuoli(AbilitazioniRuoloCallback abilitazioniRuoloCallback) {
		anagraficheRuoliHandler.caricaAbilitazioniRuoli(abilitazioniRuoloCallback);
	}

	public AbilitazioniRuolo getAbilitazioniRuolo(String ruolo) {
		for (AbilitazioniRuolo abr : anagraficheRuoliHandler.getAbilitazioniRuolo()) {
			if (abr.getRuolo().equals(ruolo)) {
				return abr;
			}
		}
		return null;
	}

	public List<String> controlloCongruenzaAbilitazioni(AbilitazioniRuolo abilitazioniRuolo) {
		List<String> errors = new ArrayList<>();

		for (Abilitazione a : abilitazioniRuolo.getAbilitazioni()) {

			if (a.getClass().equals(LetturaIngressoAbilitazione.class) && ((LetturaIngressoAbilitazione) a).isPrimoAssegnatario()) {
				LetturaIngressoAbilitazione lia = (LetturaIngressoAbilitazione) a;
				String ruolo = esistePrimoAssegnatario(lia.getTipo(), lia.getIndirizzo(), abilitazioniRuolo.getRuolo());
				if (ruolo != null) {
					errors.add("Esiste gia' un primo assegnatario per l'anagrafica " + lia.getIndirizzo() + " : " + ruolo);
					break;
				}

			} else if (a.getClass().equals(ModificaRuoloAbilitazione.class)
					&& isDipendenzaCircolare(abilitazioniRuolo.getRuolo(), ((ModificaRuoloAbilitazione) a).getRuolo(), ModificaRuoloAbilitazione.class)) {

				errors.add("Dipendenza circolare: il gruppo " + ((ModificaRuoloAbilitazione) a).getRuolo() + " e' supervisore di " + abilitazioniRuolo.getRuolo());
				break;
			}
		}

		return errors;
	}

	public void cleanAbilitazioni(List<Abilitazione> ab) {

		List<Abilitazione> abilitazioni = new ArrayList<Abilitazione>();

		for (Abilitazione a : ab) {

			if (a.getClass().equals(LetturaIngressoAbilitazione.class) && ((LetturaIngressoAbilitazione) a).isPrimoAssegnatario()) {
				LetturaIngressoAbilitazione lia = (LetturaIngressoAbilitazione) a;
				if (esistePrimoAssegnatario(lia.getTipo(), lia.getIndirizzo())) {
					lia.setPrimoAssegnatario(false);
				}

				abilitazioni.add(lia);

			} else {
				abilitazioni.add(a);
			}
		}

		ab.clear();
		ab.addAll(abilitazioni);
	}

	private String esistePrimoAssegnatario(String nomeTipologia, String indirizzo, String ruolo) {
		for (AbilitazioniRuolo abr : anagraficheRuoliHandler.getAbilitazioniRuolo()) {
			if ((ruolo != null && !abr.getRuolo().equals(ruolo)) || ruolo == null) {
				for (Abilitazione a : abr.getAbilitazioni()) {
					if (a.getClass().equals(LetturaIngressoAbilitazione.class)) {
						LetturaIngressoAbilitazione via = (LetturaIngressoAbilitazione) a;
						if (via.getTipo().equals(nomeTipologia) && via.getIndirizzo().equalsIgnoreCase(indirizzo) && via.isPrimoAssegnatario()) {
							return abr.getRuolo();
						}
					}
				}
			}
		}

		return null;
	}

	public boolean esistePrimoAssegnatario(String nomeTipologia, String indirizzo) {
		return esistePrimoAssegnatario(nomeTipologia, indirizzo, null) != null;
	}

	public boolean isDipendenzaCircolare(String ruoloSupervisore, String ruoloSubordinato, Class<? extends RuoloAbilitazione> clazz) {
		for (AbilitazioniRuolo abr : anagraficheRuoliHandler.getAbilitazioniRuolo()) {
			if (abr.getRuolo().equals(ruoloSubordinato)) {
				for (Abilitazione a : abr.getAbilitazioni()) {
					if (a.getClass().equals(clazz)) {
						RuoloAbilitazione via = (RuoloAbilitazione) a;
						if (via.getRuolo().equals(ruoloSupervisore)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public AnagraficaRuolo getPrimoAssegnatario(String indirizzoEmail, String tipologiaIngresso) {
		for (AbilitazioniRuolo ar : anagraficheRuoliHandler.getAbilitazioniRuolo()) {
			for (Abilitazione a : ar.getAbilitazioni()) {
				if (a.getClass().equals(LetturaIngressoAbilitazione.class)) {
					LetturaIngressoAbilitazione visIngAblt = (LetturaIngressoAbilitazione) a;
					if (visIngAblt.getIndirizzo().equalsIgnoreCase(indirizzoEmail) && visIngAblt.getTipo().equals(tipologiaIngresso)) {
						if (visIngAblt.isPrimoAssegnatario()) {
							return getAnagraficaRuolo(ar.getRuolo());
						}
					}
				}
			}
		}
		return null;
	}

	public <T extends IngressoAbilitazione> List<AnagraficaRuolo> getGruppiAbilitati(Class<T> tipoAbilitazione, String indirizzo, String tipologiaIngresso) {
		List<AnagraficaRuolo> ruoli = new ArrayList<AnagraficaRuolo>();
		for (AbilitazioniRuolo ar : anagraficheRuoliHandler.getAbilitazioniRuolo()) {
			for (Abilitazione a : ar.getAbilitazioni()) {
				if (a.getClass().equals(tipoAbilitazione)) {
					IngressoAbilitazione ia = (IngressoAbilitazione) a;
					if (ia.getIndirizzo().equalsIgnoreCase(indirizzo) && ia.getTipo().equals(tipologiaIngresso)) {
						AnagraficaRuolo ruolo = getAnagraficaRuolo(ar.getRuolo());
						if (ruolo != null && !ruoli.contains(ruolo)) {
							ruoli.add(ruolo);
						}
					}
				}
			}
		}
		return ruoli;
	}

	// ////////////////// SETTORI ////////////////////
	private void caricaSettori(ConfigurazioniCallback callback, boolean ricarica) {
		settoriHandler.caricaSettori(callback, ricarica);
	}

	public List<Settore> getSettori() {
		return settoriHandler.getSettori();
	}

	public Settore getSettore(AnagraficaRuolo anagraficaRuolo) {
		for (Settore settore : settoriHandler.getSettori()) {
			if (settore.getRuoli().contains(anagraficaRuolo.getRuolo())) {
				return settore;
			}
		}
		return null;
	}

	public Settore getSettore(String nomeSettore) {
		for (Settore settore : settoriHandler.getSettori()) {
			if (settore.getNome().equals(nomeSettore)) {
				return settore;
			}
		}
		return null;
	}

	// ////////////////// ANAGRAFICHE PRATICHE ////////////////////
	private void caricaAnagrafichePratiche(ConfigurazioniCallback callback, boolean ricarica) {
		anagrafichePraticheHandler.caricaAnagrafichePratiche(callback, ricarica);
	}

	/*
	 * Fascicoli
	 */
	public List<AnagraficaFascicolo> getAnagraficheFascicoli(boolean checkAttivo) {
		List<AnagraficaFascicolo> res = anagrafichePraticheHandler.getAnagraficheFascicoli();
		if (checkAttivo) {
			res = Lists.newArrayList(Iterables.filter(anagrafichePraticheHandler.getAnagraficheFascicoli(), new Predicate<AnagraficaFascicolo>() {
				@Override
				public boolean apply(AnagraficaFascicolo input) {
					return input.getStato().equals(Stato.ATTIVA);
				}
			}));
		}
		return res;
	}

	public AnagraficaFascicolo getAnagraficaFascicoloPersonale() {
		for (AnagraficaFascicolo af : getAnagraficheFascicoli(false))
			if (af.getNomeTipologia().equals(TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia())) {
				return af;
			}
		return null;
	}

	public List<AnagraficaFascicolo> filtraFascicoloPersonale(List<AnagraficaFascicolo> anagraficheFascicoli) {
		List<AnagraficaFascicolo> res = new ArrayList<AnagraficaFascicolo>();
		res = Lists.newArrayList(Iterables.filter(anagraficheFascicoli, new Predicate<AnagraficaFascicolo>() {
			@Override
			public boolean apply(AnagraficaFascicolo input) {
				return !input.getNomeTipologia().equals(TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia()) && !input.getNomeTipologia().equals(TipologiaPratica.FASCICOLO.getNomeTipologia());
			}
		}));
		return res;
	}

	public AnagraficaFascicolo getAnagraficaFascicolo(String nomeTipologia) {
		for (AnagraficaFascicolo af : getAnagraficheFascicoli(false)) {
			if (af.getNomeTipologia().equals(nomeTipologia)) {
				return af;
			}
		}
		return null;
	}

	public AnagraficaFascicolo getAnagraficaFascicoloByEtichetta(String etichetta) {
		for (AnagraficaFascicolo af : getAnagraficheFascicoli(false)) {
			if (af.getEtichettaTipologia().equals(etichetta)) {
				return af;
			}
		}
		return null;
	}

	/*
	 * Modelli
	 */
	public List<AnagraficaModello> getAnagraficheModelli(boolean checkAttivo) {
		List<AnagraficaModello> res = anagrafichePraticheHandler.getAnagraficheModelli();
		if (checkAttivo) {
			res = Lists.newArrayList(Iterables.filter(anagrafichePraticheHandler.getAnagraficheModelli(), new Predicate<AnagraficaModello>() {
				@Override
				public boolean apply(AnagraficaModello input) {
					return input.getStato().equals(Stato.ATTIVA);
				}
			}));
		}
		return res;
	}

	public AnagraficaModello getAnagraficaModello(String nomeTipologia) {
		for (AnagraficaModello af : getAnagraficheModelli(false)) {
			if (af.getNomeTipologia().equals(nomeTipologia)) {
				return af;
			}
		}
		return null;
	}

	public AnagraficaModello getAnagraficaModelloByEtichetta(String etichetta) {
		for (AnagraficaModello af : getAnagraficheModelli(false)) {
			if (af.getEtichettaTipologia().equals(etichetta)) {
				return af;
			}
		}
		return null;
	}

	/*
	 * Ingressi
	 */
	public List<AnagraficaIngresso> getAnagraficheIngressi(boolean checkAttivo) {
		List<AnagraficaIngresso> res = anagrafichePraticheHandler.getAnagraficheIngressi();
		if (checkAttivo) {
			res = Lists.newArrayList(Iterables.filter(anagrafichePraticheHandler.getAnagraficheIngressi(), new Predicate<AnagraficaIngresso>() {
				@Override
				public boolean apply(AnagraficaIngresso input) {
					return input.getStato().equals(Stato.ATTIVA);
				}
			}));
		}
		return res;
	}

	public AnagraficaIngresso getAnagraficaIngresso(String nomeTipologia, String indirizzo) {
		for (AnagraficaIngresso af : getAnagraficheIngressi(false)) {
			if (af.getIndirizzo().equalsIgnoreCase(indirizzo) && nomeTipologia.equals(af.getNomeTipologia())) {
				return af;
			}
		}
		return null;
	}

	/*
	 * Comunicazioni
	 */
	public List<AnagraficaComunicazione> getAnagraficheComunicazioni(boolean checkAttivo) {
		List<AnagraficaComunicazione> res = anagrafichePraticheHandler.getAnagraficheComunicazioni();
		if (checkAttivo) {
			res = Lists.newArrayList(Iterables.filter(anagrafichePraticheHandler.getAnagraficheComunicazioni(), new Predicate<AnagraficaComunicazione>() {
				@Override
				public boolean apply(AnagraficaComunicazione input) {
					return input.getStato().equals(Stato.ATTIVA);
				}
			}));
		}
		return res;
	}

	public AnagraficaComunicazione getAnagraficaComunicazione(String nomeTipologia) {
		for (AnagraficaComunicazione af : getAnagraficheComunicazioni(false)) {
			if (af.getNomeTipologia().equals(nomeTipologia)) {
				return af;
			}
		}
		return null;
	}

	public AnagraficaComunicazione getAnagraficaComunicazioneByEtichetta(String etichetta) {
		for (AnagraficaComunicazione af : getAnagraficheComunicazioni(false)) {
			if (af.getEtichettaTipologia().equals(etichetta)) {
				return af;
			}
		}
		return null;
	}

	/*
	 * Email out
	 */
	public List<AnagraficaEmailOut> getAnagraficheMailInUscita(boolean checkAttivo) {
		List<AnagraficaEmailOut> res = anagrafichePraticheHandler.getAnagraficheMailInUscita();
		if (checkAttivo) {
			res = Lists.newArrayList(Iterables.filter(anagrafichePraticheHandler.getAnagraficheMailInUscita(), new Predicate<AnagraficaEmailOut>() {
				@Override
				public boolean apply(AnagraficaEmailOut input) {
					return input.getStato().equals(Stato.ATTIVA);
				}
			}));
		}
		return res;
	}

	public AnagraficaEmailOut getAnagraficaMailInUscita(String tipologia, String indirizzo) {
		for (AnagraficaEmailOut af : getAnagraficheMailInUscita(false))
			if (af.getNomeTipologia().equals(tipologia) && af.getIndirizzo().equalsIgnoreCase(indirizzo)) {
				return af;
			}
		return null;
	}

	/*
	 * Pratica modulistica
	 */
	public List<AnagraficaPraticaModulistica> getAnagrafichePraticaModulistica(boolean checkAttivo) {
		List<AnagraficaPraticaModulistica> res = anagrafichePraticheHandler.getAnagrafichePraticaModulistica();
		if (checkAttivo) {
			res = Lists.newArrayList(Iterables.filter(anagrafichePraticheHandler.getAnagrafichePraticaModulistica(), new Predicate<AnagraficaPraticaModulistica>() {
				@Override
				public boolean apply(AnagraficaPraticaModulistica input) {
					return input.getStato().equals(Stato.ATTIVA);
				}
			}));
		}
		return res;
	}

	public AnagraficaPraticaModulistica getAnagraficaPraticaModulistica(String tipologia) {
		for (AnagraficaPraticaModulistica apm : getAnagrafichePraticaModulistica(false))
			if (apm.getNomeTipologia().equals(tipologia)) {
				return apm;
			}
		return null;
	}

	// ////////////////// ANAGRAFICHE RUOLI ////////////////////
	private void caricaAnagraficheRuoli(ConfigurazioniCallback callback, boolean ricarica) {
		anagraficheRuoliHandler.caricaAnagraficheRuoli(callback, ricarica);
	}

	public List<AnagraficaRuolo> getAnagraficheRuoli() {
		return anagraficheRuoliHandler.getAnagraficheRuoli();
	}

	public List<AnagraficaRuolo> getAnagraficheRuoliPersonali() {
		return anagraficheRuoliHandler.getRuoliPersonali();
	}

	public AnagraficaRuolo getAnagraficaRuolo(String ruolo) {

		for (AnagraficaRuolo ar : anagraficheRuoliHandler.getAnagraficheRuoli()) {
			if (ar.getRuolo().equals(ruolo)) {
				return ar;
			}
		}
		for (AnagraficaRuolo ar : anagraficheRuoliHandler.getRuoliPersonali()) {
			if (ar.getRuolo().equals(ruolo)) {
				return ar;
			}
		}
		return null;
	}

	public AnagraficaRuolo getAnagraficaRuoloByEtichetta(String etichetta) {
		for (AnagraficaRuolo ar : anagraficheRuoliHandler.getAnagraficheRuoli()) {
			if (ar.getEtichetta().equals(etichetta)) {
				return ar;
			}
		}
		for (AnagraficaRuolo ar : anagraficheRuoliHandler.getRuoliPersonali()) {
			if (ar.getEtichetta().equals(etichetta)) {
				return ar;
			}
		}
		return null;
	}

	public List<AnagraficaRuolo> getAnagraficheRuoli(Settore settore) {
		List<AnagraficaRuolo> res = new ArrayList<>();
		for (String ruolo : settore.getRuoli()) {
			AnagraficaRuolo ar = getAnagraficaRuolo(ruolo);
			if (ar != null)
				res.add(ar);
		}
		return res;
	}

	public List<String> getOperatori(String ruolo) {
		return getOperatori(getAnagraficaRuolo(ruolo));
	}

	public List<String> getOperatori(AnagraficaRuolo anagraficaRuolo) {
		return anagraficaRuolo != null ? anagraficaRuolo.getOperatori() : new ArrayList<String>();
	}

	// ////////////////// PROPRIETA' GENERALI ////////////////////
	private void caricaProprietaGenerali(ConfigurazioniCallback callback) {
		proprietaGeneraliHandler.caricaProprietaGenerali(callback);
	}

	public ProprietaGenerali getProprietaGenerali() {
		return proprietaGeneraliHandler.getProprietaGenerali();
	}

	public String getBaseURLPubblicazioneAllegato() {
		return proprietaGeneraliHandler.getBaseUrlPubblicazioneAllegato();
	}

	// ////////////////// CALLBACK ////////////////////
	public static abstract class ConfigurazioniCallback {
		public abstract void onSuccess();

		public abstract void onFailure(String error);
	}

	public interface AbilitazioniRuoloCallback {
		public void onError();

		public void onSuccess();
	}
}
