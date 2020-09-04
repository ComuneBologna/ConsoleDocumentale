package it.eng.portlet.consolepec.gwt.client.handler.profilazione;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.Operazione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneFascicoliAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneIngressiAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneRuoliAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.EmailOutAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.FascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.FiltroDatoAggiuntivoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModelloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.RuoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeCartellaAttivita;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeFirmaDigitale;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeRiassegnazione;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.CondizioneAbilitazione;
import it.eng.cobo.consolepec.security.handler.AutorizzazioneHandler.QueryAbilitazione;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.PostLoadingAction;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.WorklistHandler.WorklistHandlerCallback;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;

/**
 *
 * @author biagiot
 *
 */
public class ProfilazioneUtenteHandler {

	private DatiUtenteHandler utenteHandler;
	private PreferenzeUtenteHandler preferenzeUtenteHandler;
	private ConfigurazioniHandler configurazioniHandler;
	private AbilitazioniUtenteHandler abilitazioniUtenteHandler;
	private WorklistHandler worklistHandler;

	@Inject
	public ProfilazioneUtenteHandler(DatiUtenteHandler utenteHandler, PreferenzeUtenteHandler preferenzeUtenteHandler, AbilitazioniUtenteHandler abilitazioniUtenteHandler,
			ConfigurazioniHandler configurazioniHandler, WorklistHandler worklistHandler) {
		super();
		this.utenteHandler = utenteHandler;
		this.preferenzeUtenteHandler = preferenzeUtenteHandler;
		this.abilitazioniUtenteHandler = abilitazioniUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
		this.worklistHandler = worklistHandler;
	}

	///////////////////////////////////////////////////////// INIT HANDLER /////////////////////////////////////////////////////////
	public void init(final PostLoadingAction postLoadingAction) {

		ProfilazioneUtenteCallback callback = new ProfilazioneUtenteCallback() {

			int loading = 0;

			@Override
			public void onSuccess() {
				loading++;

				if (loading == 5 && postLoadingAction != null) {

					if (isAmministratore()) {
						configurazioniHandler.caricaAbilitazioniRuoli();
					}

					postLoadingAction.onComplete();
				}
			}

			@Override
			public void onFailure(String error) {
				throw new IllegalArgumentException(error);
			}
		};

		caricaProfilazioneUtente(callback);
		caricaAutorizzazioniUtente(callback);
		caricaAnagraficheUtentiSupervisori(callback);
		caricaPreferenzeUtente(callback);
		caricaWorklist(callback);
	}

	///////////////////////////////////////////////////////// PROFILAZIONE UTENTE /////////////////////////////////////////////////////////
	private void caricaProfilazioneUtente(ProfilazioneUtenteCallback callback) {
		utenteHandler.caricaDatiUtente(callback);
	}

	public Utente getDatiUtente() {
		return utenteHandler.getUtente();
	}

	public List<AnagraficaRuolo> getAnagraficheRuoloUtente() {
		return utenteHandler.getUtente().getAnagraficheRuoli();
	}

	public AnagraficaRuolo getAnagraficaRuoloUtente(String ruolo) {
		for (AnagraficaRuolo ar : getAnagraficheRuoloUtente()) {
			if (ar.getRuolo().equals(ruolo)) {
				return ar;
			}
		}

		return null;
	}

	public AnagraficaRuolo getAnagraficaRuoloUtenteByEtichetta(String etichetta) {
		for (AnagraficaRuolo ar : getAnagraficheRuoloUtente()) {
			if (ar.getEtichetta().equals(etichetta)) {
				return ar;
			}
		}

		return null;
	}

	///////////////////////////////////////////////////////// ABILITAZIONI /////////////////////////////////////////////////////////////
	private void caricaAnagraficheUtentiSupervisori(ProfilazioneUtenteCallback callback) {
		abilitazioniUtenteHandler.caricaAnagraficheUtentiSupervisori(callback);
	}

	private void caricaAutorizzazioniUtente(ProfilazioneUtenteCallback callback) {
		abilitazioniUtenteHandler.caricaAutorizzazioniUtente(callback);
	}

	public <T extends Abilitazione> boolean isAbilitato(Class<T> tipoAbilitazione) {
		return abilitazioniUtenteHandler.getAutorizzazioneHandler().isAbilitato(tipoAbilitazione);
	}

	public <T extends Abilitazione> boolean isAbilitato(Class<T> tipoAbilitazione, QueryAbilitazione<T> qab) {
		return abilitazioniUtenteHandler.getAutorizzazioneHandler().isAbilitato(tipoAbilitazione, qab);
	}

	public <T extends Abilitazione> List<AnagraficaRuolo> findAnagraficheRuoli(Class<T> tipoAbilitazione) {
		return abilitazioniUtenteHandler.getAutorizzazioneHandler().findAnagraficheRuoli(tipoAbilitazione);
	}

	public List<AnagraficaRuolo> getAnagraficheRuoliSuperutenti(String ruolo) {
		return abilitazioniUtenteHandler.getAnagraficheRuoliSuperutenti().get(ruolo) != null ? abilitazioniUtenteHandler.getAnagraficheRuoliSuperutenti().get(ruolo) : new ArrayList<AnagraficaRuolo>();
	}

	public List<AnagraficaRuolo> getAnagraficheRuoliSuperutentiMatriceVisibilita(String ruolo) {
		return abilitazioniUtenteHandler.getAnagraficheRuoliSuperutentiMatriceVisibilita().get(ruolo) != null ? abilitazioniUtenteHandler.getAnagraficheRuoliSuperutentiMatriceVisibilita().get(ruolo)
				: new ArrayList<AnagraficaRuolo>();
	}

	public List<TipologiaPratica> getTipologieFascicoloInModifica(final String operazione) {

		QueryAbilitazione<ModificaFascicoloAbilitazione> qab = new QueryAbilitazione<ModificaFascicoloAbilitazione>();
		qab.addCondition(new CondizioneAbilitazione<ModificaFascicoloAbilitazione>() {

			@Override
			protected boolean valutaCondizione(ModificaFascicoloAbilitazione abilitazione) {

				for (Operazione o : abilitazione.getOperazioni()) {
					if (operazione.equals(o.getNome())) {
						return true;
					}
				}

				return false;
			}
		});

		List<ModificaFascicoloAbilitazione> abilitazioni = abilitazioniUtenteHandler.getAutorizzazioneHandler().findAbilitazioni(ModificaFascicoloAbilitazione.class, qab);

		List<TipologiaPratica> res = Lists.newArrayList(Lists.transform(abilitazioni, new Function<ModificaFascicoloAbilitazione, TipologiaPratica>() {

			@Override
			public TipologiaPratica apply(ModificaFascicoloAbilitazione input) {
				return new TipologiaPratica(input.getTipo());
			}
		}));

		if (res.contains(TipologiaPratica.FASCICOLO)) {
			return PraticaUtil.fascicoliToTipologiePratiche(configurazioniHandler.filtraFascicoloPersonale(configurazioniHandler.getAnagraficheFascicoli(true)));
		}

		return res;
	}

	/*
	 * Ammin.
	 */
	public boolean isAmministratore() {
		boolean b1 = isAbilitato(AmministrazioneRuoliAbilitazione.class);
		boolean b2 = isAbilitato(AmministrazioneFascicoliAbilitazione.class);
		boolean b3 = isAbilitato(AmministrazioneIngressiAbilitazione.class);
		boolean amministrazioneAbilitata = b1 || b2 || b3;

		return amministrazioneAbilitata;
	}

	/*
	 * Ruoli
	 */
	public <T extends Abilitazione> List<AnagraficaRuolo> getAnagraficheRuoliAbilitati(Class<T> clazz) {
		return abilitazioniUtenteHandler.getAutorizzazioneHandler().findAnagraficheRuoli(clazz);
	}

	public <T extends Abilitazione> List<AnagraficaRuolo> getAnagraficheRuoliAbilitati(Class<T> clazz, QueryAbilitazione<T> qab) {
		return abilitazioniUtenteHandler.getAutorizzazioneHandler().findAnagraficheRuoli(clazz, qab);
	}

	public <T extends RuoloAbilitazione> List<AnagraficaRuolo> getAnagraficheRuoliSubordinati(Class<T> clazz) {
		List<T> abilitazioni = abilitazioniUtenteHandler.getAutorizzazioneHandler().findAbilitazioni(clazz);

		List<AnagraficaRuolo> res = Lists.newArrayList(Lists.transform(abilitazioni, new Function<T, AnagraficaRuolo>() {

			@Override
			public AnagraficaRuolo apply(T input) {
				return configurazioniHandler.getAnagraficaRuolo(input.getRuolo());
			}
		}));

		res.removeAll(Collections.singleton(null));
		return res;
	}

	public List<AnagraficaRuolo> getAllAnagraficheRuoliSubordinati() {
		Set<AnagraficaRuolo> all = new HashSet<>();
		all.addAll(getAnagraficheRuoloUtente());
		all.addAll(getAnagraficheRuoliSubordinati(VisibilitaRuoloAbilitazione.class));
		all.addAll(getAnagraficheRuoliSubordinati(ModificaRuoloAbilitazione.class));
		return Lists.newArrayList(all);
	}

	/*
	 * Fascicoli
	 */
	public <T extends FascicoloAbilitazione> List<AnagraficaFascicolo> getAnagraficheFascicoliAbilitati(final Class<T> clazz) {

		return Lists.newArrayList(Iterables.filter(configurazioniHandler.getAnagraficheFascicoli(true), new Predicate<AnagraficaFascicolo>() {

			@Override
			public boolean apply(AnagraficaFascicolo input) {
				for (T cfa : abilitazioniUtenteHandler.getAutorizzazioneHandler().findAbilitazioni(clazz)) {
					if (cfa.getTipo().equals(input.getNomeTipologia())) {
						return true;
					}
				}
				return false;
			}
		}));
	}

	/*
	 * Modelli
	 */
	public <T extends ModelloAbilitazione> List<AnagraficaModello> getAnagraficheModelliAbilitati(final Class<T> clazz) {

		return Lists.newArrayList(Iterables.filter(configurazioniHandler.getAnagraficheModelli(true), new Predicate<AnagraficaModello>() {

			@Override
			public boolean apply(AnagraficaModello input) {
				for (T cfa : abilitazioniUtenteHandler.getAutorizzazioneHandler().findAbilitazioni(clazz)) {
					if (cfa.getTipo().equals(input.getNomeTipologia())) {
						return true;
					}
				}
				return false;
			}
		}));
	}

	/*
	 * Email out
	 */
	public <T extends EmailOutAbilitazione> List<AnagraficaEmailOut> getAnagraficheEmailInUscitaAbilitate(final Class<T> clazz) {

		return Lists.newArrayList(Iterables.filter(configurazioniHandler.getAnagraficheMailInUscita(true), new Predicate<AnagraficaEmailOut>() {

			@Override
			public boolean apply(AnagraficaEmailOut input) {

				if (getDatiUtente().isUtenteEsterno()) {
					return true;
				}

				for (T cfa : abilitazioniUtenteHandler.getAutorizzazioneHandler().findAbilitazioni(clazz)) {
					if (cfa.getTipo().equals(input.getNomeTipologia()) && cfa.getIndirizzo().equalsIgnoreCase(input.getIndirizzo())) {
						return true;
					}
				}

				return false;
			}
		}));
	}

	public <T extends EmailOutAbilitazione> List<String> getIndirizziEmailInUscitaAbilitati(Class<T> clazz) {

		return Lists.transform(getAnagraficheEmailInUscitaAbilitate(clazz), new Function<AnagraficaEmailOut, String>() {

			@Override
			public String apply(AnagraficaEmailOut input) {
				return input.getIndirizzo();
			}
		});
	}

	/*
	 * Filtri di ricerca
	 */
	public List<DatoAggiuntivo> getFiltriRicercaAggiuntivi() {
		List<DatoAggiuntivo> result = new ArrayList<>();

		for (FiltroDatoAggiuntivoAbilitazione abilitazione : abilitazioniUtenteHandler.getAutorizzazioneHandler().findAbilitazioni(FiltroDatoAggiuntivoAbilitazione.class)) {
			result.add(abilitazione.getFiltroDatoAggiuntivo());
		}

		return result;
	}

	/*
	 * Worklist
	 */
	private void caricaWorklist(ProfilazioneUtenteCallback callback) {
		worklistHandler.caricaWorklist(callback);
	}

	public void getWorklist(boolean reload, WorklistHandlerCallback callback) {
		worklistHandler.caricaWorklist(reload, callback);
	}

	public AnagraficaWorklist getWorklist(String nome) {
		nome = Base64Utils.URLdecodeAlfrescoPath(nome);
		AnagraficaWorklist res = worklistHandler.getDefaultWorklist();

		if (nome != null) {
			for (AnagraficaWorklist aw : worklistHandler.getWorklistAbilitate().keySet()) {
				if (aw.getNome().equals(nome)) {
					res = aw;
					break;
				}
			}
		}

		return res != null ? res : worklistHandler.getWorklistAbilitate().keySet().iterator().next();
	}

	/*
	 * Settori
	 */
	public List<Settore> getSettoriUtente(boolean subordinati) {

		List<Settore> res = new ArrayList<>();

		for (AnagraficaRuolo ar : getDatiUtente().getAnagraficheRuoli()) {
			Settore settore = configurazioniHandler.getSettore(ar);

			if (settore != null) {
				res.add(settore);
			}
		}

		List<Settore> subRes = new ArrayList<>();
		if (subordinati) {
			for (Settore settore : res) {
				for (String sub : settore.getSettoriSubordinati()) {
					Settore settoreSub = configurazioniHandler.getSettore(sub);
					if (settoreSub != null && !res.contains(settoreSub) && !subRes.contains(settoreSub)) {
						subRes.add(settoreSub);
					}
				}

			}
		}

		res.addAll(subRes);
		return res;
	}

	public Map<Settore, List<AnagraficaRuolo>> getSettori(boolean filtroUtente, boolean filtroRiservato, boolean subordinati) {
		Map<Settore, List<AnagraficaRuolo>> res = new HashMap<Settore, List<AnagraficaRuolo>>();

		for (Settore settore : configurazioniHandler.getSettori()) {
			if (filtroUtente && Collections.disjoint(settore.getRuoli(), getDatiUtente().getRuoli())) {
				continue;
			}

			if (settore.isRiservato() && (filtroRiservato && !getSettoriUtente(true).contains(settore))) {
				continue;
			}

			if (!res.containsKey(settore)) {
				res.put(settore, new ArrayList<AnagraficaRuolo>());
			}

			for (AnagraficaRuolo ar : configurazioniHandler.getAnagraficheRuoli(settore)) {
				if (!res.get(settore).contains(ar)) {
					res.get(settore).add(ar);
				}
			}

			if (subordinati) {
				for (String settoreSubordinato : settore.getSettoriSubordinati()) {
					Settore settoreSub = configurazioniHandler.getSettore(settoreSubordinato);
					if (settoreSub != null) {

						if (settoreSub.isRiservato() && filtroRiservato && !getSettoriUtente(true).contains(settoreSub)) {
							continue;
						}

						for (AnagraficaRuolo ar : configurazioniHandler.getAnagraficheRuoli(settoreSub)) {
							if (!res.get(settore).contains(ar)) {
								res.get(settore).add(ar);
							}
						}
					}
				}
			}
		}

		for (List<AnagraficaRuolo> ar : res.values()) {
			Collections.sort(ar, new Comparator<AnagraficaRuolo>() {

				@Override
				public int compare(AnagraficaRuolo o1, AnagraficaRuolo o2) {
					return o1.getEtichetta().compareToIgnoreCase(o2.getEtichetta());
				}

			});
		}

		return res;
	}

	///////////////////////////////////////////////////////// PREFERENZE UTENTE /////////////////////////////////////////////////////////
	private void caricaPreferenzeUtente(ProfilazioneUtenteCallback callback) {
		preferenzeUtenteHandler.caricaPreferenze(callback);
	}

	public PreferenzeUtente getPreferenzeUtente() {
		return preferenzeUtenteHandler.getPreferenzeUtente();
	}

	public void aggiornaPreferenzeUtente(String firmaEmail, String fascicoloDefault, PreferenzeCartellaAttivita preferenzeCartellaAttivita, ProfilazioneUtenteCallback callback) {
		preferenzeUtenteHandler.aggiornaPreferenze(firmaEmail, fascicoloDefault, preferenzeCartellaAttivita, callback);
	}

	public void aggiornaPreferenzeFirmaDigitale(boolean ricorda, String username, String password, ProfilazioneUtenteCallback callback) {
		preferenzeUtenteHandler.aggiornaPreferenzeFirmaDigitale(ricorda, username, password, callback);
	}

	public void aggiornaPreferenzeRiassegnazione(boolean ricorda, String settore, String ruolo, List<String> indirizziNotifica, ProfilazioneUtenteCallback callback) {
		preferenzeUtenteHandler.aggiornaPreferenzeRiassegnazione(ricorda, settore, ruolo, indirizziNotifica, callback);
	}

	public PreferenzeFirmaDigitale getPreferenzeFirmaDigitaleUtente() {
		return preferenzeUtenteHandler.getPreferenzeFirmaDigitale();
	}

	public PreferenzeRiassegnazione getPreferenzeRiassegnazioneUtente() {
		return preferenzeUtenteHandler.getPreferenzeRiassegnazione();
	}

	///////////////////////////////////////////////////////// CALLBACK /////////////////////////////////////////////////////////
	public static abstract class ProfilazioneUtenteCallback {
		public abstract void onSuccess();

		public abstract void onFailure(String error);
	}
}
