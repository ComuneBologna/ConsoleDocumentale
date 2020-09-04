package it.eng.portlet.consolepec.gwt.client.presenter.configurazioni;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gwt.user.client.Command;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneFascicoliAbilitazione;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaFascicoloApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaFascicoloApiClient.AnagraficaFascicoloCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;

/**
 * 
 * @author biagiot
 *
 */
public class DettaglioAnagraficaFascicoloPresenter extends Presenter<DettaglioAnagraficaFascicoloPresenter.MyView, DettaglioAnagraficaFascicoloPresenter.MyProxy> {

	@ProxyCodeSplit
	@NameToken({ NameTokens.dettaglioanagraficafascicolo
			// ,NameTokens.creaanagraficafascicolo
	})
	public interface MyProxy extends ProxyPlace<DettaglioAnagraficaFascicoloPresenter> {
		//
	}

	public interface MyView extends View {
		void mostraFormDettaglio(AnagraficaFascicolo anagraficaFascicolo, boolean showAction);

		void mostraFormCreazione();

		void setSalvaCommand(Command command);

		void setChiudiCommand(Command command);

		void setDataCreazione(Date dataCreazione);

		void setUtenteCreazione(String utente);

		void showErrors(List<String> errors);

		void resetErrors();

		AnagraficaFascicolo getAnagraficaFascicolo();

		void clearView();
	}

	private AmministrazioneAnagraficaFascicoloApiClient amministrazioneAnagraficaFascicoloApiClient;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	/*
	 * Var. stato
	 */
	private String parentPlace;
	private boolean isDettaglio;
	private AnagraficaFascicolo anagraficaFascicoloOriginale;

	@Inject
	public DettaglioAnagraficaFascicoloPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
			AmministrazioneAnagraficaFascicoloApiClient amministrazioneAnagraficaFascicoloApiClient, ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {

		super(eventBus, view, proxy);

		this.amministrazioneAnagraficaFascicoloApiClient = amministrazioneAnagraficaFascicoloApiClient;
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setSalvaCommand(new SalvaCommand());
		getView().setChiudiCommand(new AnnullaCommand());

	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().clearView();
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		if (profilazioneUtenteHandler.isAbilitato(AmministrazioneFascicoliAbilitazione.class)) {
			parentPlace = request.getParameter(NameTokensParams.parentPlace, null);

			if (request.getNameToken().equals(NameTokens.dettaglioanagraficafascicolo)) {
				dettaglioFascicolo(request.getParameter(NameTokensParams.tipologiaFascicolo, null), Boolean.parseBoolean(request.getParameter(NameTokensParams.showActions, null)));

			} else if (request.getNameToken().equals(NameTokens.creaanagraficafascicolo)) {
				creaFascicolo();

			} else {
				throw new IllegalArgumentException();
			}

		} else {
			throw new IllegalArgumentException("Utente non amministratore");
		}
	}

	private void creaFascicolo() {
		isDettaglio = false;
		this.anagraficaFascicoloOriginale = null;

		getView().setUtenteCreazione(profilazioneUtenteHandler.getDatiUtente().getUsername());
		getView().setDataCreazione(new Date());
		getView().mostraFormCreazione();

	}

	private void dettaglioFascicolo(String tipoFascicolo, boolean showAction) {
		AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(tipoFascicolo);
		dettaglioFascicolo(af, showAction);
	}

	private void dettaglioFascicolo(AnagraficaFascicolo anagraficaFascicolo, boolean showAction) {
		isDettaglio = true;

		if (anagraficaFascicolo != null) {
			this.anagraficaFascicoloOriginale = anagraficaFascicolo;

			getView().setUtenteCreazione(anagraficaFascicolo.getUsernameCreazione());
			getView().setDataCreazione(anagraficaFascicolo.getDataCreazione());

			getView().mostraFormDettaglio(anagraficaFascicolo, showAction);

		} else {
			getView().showErrors(Arrays.asList("L'ingresso selezionato non esiste"));
		}
	}

	private class AnnullaCommand implements Command {

		@Override
		public void execute() {
			getView().resetErrors();

			Place place = new Place();
			place.setToken(parentPlace != null ? parentPlace : NameTokens.listaanagraficagruppi);
			getEventBus().fireEvent(new GoToPlaceEvent(place));
		}
	}

	private class SalvaCommand implements Command {

		@Override
		public void execute() {
			getView().resetErrors();

			ShowAppLoadingEvent.fire(DettaglioAnagraficaFascicoloPresenter.this, true);
			Map<String, Object> modifiche = new HashMap<String, Object>();

			if (isDettaglio) {

				List<String> errors = controllaForm(true, modifiche);
				if (errors.isEmpty()) {
					AnagraficaFascicolo af = getAnagraficaFascicolo();

					StringBuilder descrizioneAzione = new StringBuilder("Modifica anagrafica fascicolo");
					if (!modifiche.isEmpty()) {
						descrizioneAzione.append("- Campi aggiornati: ");
						int start = 0;
						for (Entry<String, Object> entry : modifiche.entrySet()) {

							if (start > 0) {
								descrizioneAzione.append(", ");
							}

							descrizioneAzione.append(entry.getKey() + ":" + entry.getValue().toString());

							start++;
						}
					}

					Azione a = new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), new Date(), descrizioneAzione.toString());
					List<Azione> azioni = new ArrayList<Azione>();
					azioni.add(a);

					amministrazioneAnagraficaFascicoloApiClient.modifica(af, azioni, new AnagraficaFascicoloCallback() {

						@Override
						public void onSuccess(AnagraficaFascicolo a) {
							ShowAppLoadingEvent.fire(DettaglioAnagraficaFascicoloPresenter.this, false);
							dettaglioFascicolo(a, false);
						}

						@Override
						public void onSuccess(List<AnagraficaFascicolo> a, int count) {/**/}

						@Override
						public void onError(String error) {
							ShowAppLoadingEvent.fire(DettaglioAnagraficaFascicoloPresenter.this, false);
							getView().showErrors(Arrays.asList(error));
						}
					});

				} else {
					ShowAppLoadingEvent.fire(DettaglioAnagraficaFascicoloPresenter.this, false);
					getView().showErrors(errors);
				}

			} else {

				List<String> errors = controllaForm(false, modifiche);
				if (errors.isEmpty()) {
					AnagraficaFascicolo af = getAnagraficaFascicolo();

					Azione a = new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), af.getDataCreazione(), "Inserimento anagrafica fascicolo");
					List<Azione> azioni = new ArrayList<Azione>();
					azioni.add(a);

					amministrazioneAnagraficaFascicoloApiClient.inserisci(af, azioni, new AnagraficaFascicoloCallback() {

						@Override
						public void onSuccess(AnagraficaFascicolo a) {
							ShowAppLoadingEvent.fire(DettaglioAnagraficaFascicoloPresenter.this, false);
							dettaglioFascicolo(a, false);
						}

						@Override
						public void onSuccess(List<AnagraficaFascicolo> a, int count) {/**/}

						@Override
						public void onError(String error) {
							ShowAppLoadingEvent.fire(DettaglioAnagraficaFascicoloPresenter.this, false);
							getView().showErrors(Arrays.asList(error));
						}
					});

				} else {
					ShowAppLoadingEvent.fire(DettaglioAnagraficaFascicoloPresenter.this, false);
					getView().showErrors(errors);
				}
			}
		}
	}

	private AnagraficaFascicolo getAnagraficaFascicolo() {
		AnagraficaFascicolo af = getView().getAnagraficaFascicolo();

		if (isDettaglio) {
			TipologiaPratica tp = PraticaUtil.toTipologiaPratica(anagraficaFascicoloOriginale);
			af.setNomeTipologia(tp.getNomeTipologia());
			af.setEtichettaTipologia(tp.getEtichettaTipologia());
			af.setDettaglioNameToken(tp.getDettaglioNameToken());
			af.setAzioniDettaglio(tp.isAzioniDettaglio());
			af.getAzioni().addAll(anagraficaFascicoloOriginale.getAzioni());
			af.setTemplateTitolo(anagraficaFascicoloOriginale.getTemplateTitolo());
			af.getConfigurazioneEsecuzioni().addAll(anagraficaFascicoloOriginale.getConfigurazioneEsecuzioni());
			// af.getTipologieAllegato().addAll(anagraficaFascicoloOriginale.getTipologieAllegato());
			af.setDatiProcedimento(anagraficaFascicoloOriginale.getDatiProcedimento());
			af.setProtocollazioneRiservata(anagraficaFascicoloOriginale.isProtocollazioneRiservata());

			// XXX Fase 2: Dati di protocollazione rimuovere codice seguente:
			af.setTitolazione(anagraficaFascicoloOriginale.getTitolazione());

		} else {
			// ETICHETTA impostata dalla view
			if (PraticaUtil.validaEtichettaFascicolo(af.getEtichettaTipologia())) {
				String etichetta = af.getEtichettaTipologia();
				String tipologia = etichetta.replaceAll(" ", "_").toUpperCase();
				af.setNomeTipologia(tipologia);
			}

			af.setDettaglioNameToken(TipologiaPratica.FASCICOLO_DETTAGLIO_NAME_TOKEN_DEFAULT);
		}

		return af;
	}

	private List<String> controllaForm(boolean controllaModifiche, Map<String, Object> modifiche) {
		List<String> errors = new ArrayList<String>();
		AnagraficaFascicolo af = getAnagraficaFascicolo();

		if (af == null) {
			errors.add("Fascicolo non valido");
			return errors;
		}

		if (!PraticaUtil.validaEtichettaFascicolo(af.getEtichettaTipologia()) || !PraticaUtil.validaNomeTipologiaFascicolo(af.getNomeTipologia())) {
			errors.add("Nome tipologia fascicolo non valida");
		}

		if (af.getStato() == null) {
			errors.add("Stato non valido");
		}

		if (errors.isEmpty() && controllaModifiche) {
			boolean edited = false;

			if (!af.getNomeTipologia().equals(anagraficaFascicoloOriginale.getNomeTipologia()) || !af.getEtichettaTipologia().equals(anagraficaFascicoloOriginale.getEtichettaTipologia())) {
				errors.add("Non è possibile modificare la tipologia");
			}

			// if (anagraficaFascicoloOriginale.getTitolazione() != null) {
			//
			// if (!anagraficaFascicoloOriginale.getTitolazione().equals(af.getTitolazione())) {
			// StringBuilder sb = new StringBuilder();
			//
			// if (af.getTitolazione().getTitolo() != null && !af.getTitolazione().getTitolo().trim().isEmpty()) {
			// sb.append("titolo:").append(af.getTitolazione().getTitolo());
			// }
			//
			// if (af.getTitolazione().getRubrica() != null && !af.getTitolazione().getRubrica().trim().isEmpty()) {
			// if (sb.toString().isEmpty()) {
			// sb.append(";");
			// }
			//
			// sb.append("rubrica").append(af.getTitolazione().getRubrica());
			// }
			//
			// if (af.getTitolazione().getSezione() != null && !af.getTitolazione().getSezione().trim().isEmpty()) {
			// if (sb.toString().isEmpty()) {
			// sb.append(";");
			// }
			//
			// sb.append("sezione").append(af.getTitolazione().getSezione());
			// }
			//
			// if (!sb.toString().isEmpty())
			// modifiche.put("titolazione", sb.toString());
			//
			// edited = true;
			// }
			//
			// } else if (af.getTitolazione().getTitolo() != null && !af.getTitolazione().getTitolo().trim().isEmpty()
			// || af.getTitolazione().getRubrica() != null && !af.getTitolazione().getRubrica().trim().isEmpty()
			// || af.getTitolazione().getSezione() != null && !af.getTitolazione().getSezione().trim().isEmpty()) {
			//
			// StringBuilder sb = new StringBuilder();
			//
			// if (af.getTitolazione().getTitolo() != null && !af.getTitolazione().getTitolo().trim().isEmpty()) {
			// sb.append("titolo:").append(af.getTitolazione().getTitolo());
			// }
			//
			// if (af.getTitolazione().getRubrica() != null && !af.getTitolazione().getRubrica().trim().isEmpty()) {
			// if (sb.toString().isEmpty()) {
			// sb.append(";");
			// }
			//
			// sb.append("rubrica").append(af.getTitolazione().getRubrica());
			// }
			//
			// if (af.getTitolazione().getSezione() != null && !af.getTitolazione().getSezione().trim().isEmpty()) {
			// if (sb.toString().isEmpty()) {
			// sb.append(";");
			// }
			//
			// sb.append("sezione").append(af.getTitolazione().getSezione());
			// }
			//
			// modifiche.put("titolazione", sb.toString());
			// edited = true;
			// }

			List<String> modificheDati = new ArrayList<>();
			for (DatoAggiuntivo dag : af.getDatiAggiuntivi()) {
				if (!anagraficaFascicoloOriginale.getDatiAggiuntivi().contains(dag)) {
					edited = true;
					modificheDati.add("aggiunto dato: " + dag.getNome());
				}
			}

			for (DatoAggiuntivo dag : anagraficaFascicoloOriginale.getDatiAggiuntivi()) {
				if (!af.getDatiAggiuntivi().contains(dag)) {
					edited = true;
					modificheDati.add("rimosso dato: " + dag.getNome());
				}
			}

			if (!modificheDati.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (String s : modificheDati) {
					sb.append(s).append(";");

				}
				modifiche.put("dati aggiuntivi", sb.toString());
			}

			List<String> modificheStepIter = new ArrayList<>();
			for (StepIter dag : af.getStepIterAbilitati()) {
				if (!anagraficaFascicoloOriginale.getStepIterAbilitati().contains(dag)) {
					edited = true;
					modificheStepIter.add("aggiunto step: " + dag.getNome());
				}
			}

			for (StepIter dag : anagraficaFascicoloOriginale.getStepIterAbilitati()) {
				if (!af.getStepIterAbilitati().contains(dag)) {
					edited = true;
					modificheStepIter.add("rimosso step: " + dag.getNome());
				}
			}

			if (!modificheStepIter.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (String s : modificheStepIter) {
					sb.append(s).append(";");

				}
				modifiche.put("step iter", sb.toString());
			}

			// modifiche tipologia allegato
			List<String> modTipoAlleg = new ArrayList<>();
			for (String tipologia : af.getTipologieAllegato()) {
				if (!anagraficaFascicoloOriginale.getTipologieAllegato().contains(tipologia)) {
					edited = true;
					modTipoAlleg.add(" aggiunta tipologia: " + tipologia);
				}
			}
			for (String tipologia : anagraficaFascicoloOriginale.getTipologieAllegato()) {
				if (!af.getTipologieAllegato().contains(tipologia)) {
					edited = true;
					modTipoAlleg.add(" rimossa tipologia: " + tipologia);
				}
			}
			if (!modTipoAlleg.isEmpty()) {
				StringBuilder sb = new StringBuilder();
				for (String s : modTipoAlleg) {
					sb.append(s).append("; ");
				}
				modifiche.put("tipologie allegato", sb.toString());
			}

			if (anagraficaFascicoloOriginale.isProtocollabile() != af.isProtocollabile()) {
				edited = true;
				modifiche.put("protocollabile", af.isProtocollabile() ? "si" : "no");
			}

			if (!anagraficaFascicoloOriginale.getStato().equals(af.getStato())) {
				edited = true;
				modifiche.put("stato", Stato.ATTIVA.equals(af.getStato()) ? "attivo" : "disattivo");
			}

			if (!edited) {
				errors.add("Nessuna modifica effettuata");
			}
		}

		return errors;
	}
}
