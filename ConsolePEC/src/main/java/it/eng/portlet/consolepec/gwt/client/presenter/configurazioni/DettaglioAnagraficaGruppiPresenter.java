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

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneRuoliAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaRuoloResponse;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaRuoliApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaRuoliApiClient.AnagraficaRuoloCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;

/**
 * 
 * @author biagiot
 *
 */
public class DettaglioAnagraficaGruppiPresenter extends Presenter<DettaglioAnagraficaGruppiPresenter.MyView, DettaglioAnagraficaGruppiPresenter.MyProxy> {

	@ProxyCodeSplit
	@NameToken({ NameTokens.dettaglioanagraficagruppo, NameTokens.creaanagraficagruppo })
	public interface MyProxy extends ProxyPlace<DettaglioAnagraficaGruppiPresenter> {
		//
	}

	public interface MyView extends View {

		void mostraFormDettaglio(AnagraficaRuolo anagraficaRuolo, Settore settore, AbilitazioniRuolo abilitazioniRuolo, boolean showAction);

		void mostraFormCreazione();

		void mostraFormCreaPerCopia(AnagraficaRuolo anagraficaRuolo, Settore settore, AbilitazioniRuolo abilitazioniRuolo);

		void setSalvaRuoloCommand(Command command);

		void setChiudiCommand(Command command);

		void setCreaRuoloPerCopiaCommand(Command command);

		void setDataCreazione(Date dataCreazione);

		void setUtenteCreazione(String utente);

		void showErrors(List<String> errors);

		void resetErrors();

		AnagraficaRuolo getAnagraficaRuolo();

		List<Abilitazione> getAbilitazioniDaAggiungere();

		List<Abilitazione> getAbilitazioniDaRimuovere();

		Settore getSettore();

		List<Azione> getAzioni();

		void clearView();
	}

	private AmministrazioneAnagraficaRuoliApiClient amministrazioneGruppiApiClient;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	/*
	 * Var. stato
	 */
	private String parentPlace;
	private boolean isDettaglio;
	private AnagraficaRuolo anagraficaRuoloOriginale;
	private AbilitazioniRuolo abilitazioniRuoloOriginali;
	private Settore settoreOriginale;

	@Inject
	public DettaglioAnagraficaGruppiPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, AmministrazioneAnagraficaRuoliApiClient amministrazioneGruppiApiClient,
			ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);

		this.amministrazioneGruppiApiClient = amministrazioneGruppiApiClient;
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setSalvaRuoloCommand(new SalvaCommand());
		getView().setChiudiCommand(new AnnullaCommand());
		getView().setCreaRuoloPerCopiaCommand(new CreaPerCopiaCommand());
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
		super.prepareFromRequest(request);

		if (profilazioneUtenteHandler.isAbilitato(AmministrazioneRuoliAbilitazione.class)) {
			parentPlace = request.getParameter(NameTokensParams.parentPlace, null);

			if (request.getNameToken().equals(NameTokens.dettaglioanagraficagruppo)) {
				dettaglioRuolo(request.getParameter(NameTokensParams.ruolo, null), Boolean.parseBoolean(request.getParameter(NameTokensParams.showActions, null)));

			} else if (request.getNameToken().equals(NameTokens.creaanagraficagruppo)) {
				creaRuolo();

			} else {
				throw new IllegalArgumentException();
			}

		} else {
			throw new IllegalArgumentException("Utente non amministratore");
		}
	}

	private void creaRuolo() {
		isDettaglio = false;
		this.anagraficaRuoloOriginale = null;
		this.abilitazioniRuoloOriginali = null;
		this.settoreOriginale = null;

		getView().setUtenteCreazione(profilazioneUtenteHandler.getDatiUtente().getUsername());
		getView().setDataCreazione(new Date());
		getView().mostraFormCreazione();
	}

	private void dettaglioRuoloPerCopia(AnagraficaRuolo anagraficaRuolo, Settore settore, AbilitazioniRuolo abilitazioniRuolo) {
		isDettaglio = false;
		this.anagraficaRuoloOriginale = null;
		this.abilitazioniRuoloOriginali = null;
		this.settoreOriginale = null;

		getView().setUtenteCreazione(profilazioneUtenteHandler.getDatiUtente().getUsername());
		getView().setDataCreazione(new Date());
		getView().mostraFormCreaPerCopia(anagraficaRuolo, settore, abilitazioniRuolo);
	}

	private void dettaglioRuolo(String ruolo, boolean showAction) {
		AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuolo(ruolo);
		AbilitazioniRuolo abilitazioniRuolo = configurazioniHandler.getAbilitazioniRuolo(ruolo);
		Settore settore = configurazioniHandler.getSettore(ar);
		dettaglioRuolo(ar, settore, abilitazioniRuolo, showAction);
	}

	private void dettaglioRuolo(AnagraficaRuolo anagraficaRuolo, Settore settore, AbilitazioniRuolo abilitazioniRuolo, boolean showAction) {
		isDettaglio = true;

		if (anagraficaRuolo != null) {
			this.anagraficaRuoloOriginale = anagraficaRuolo;
			this.abilitazioniRuoloOriginali = abilitazioniRuolo;
			this.settoreOriginale = settore;

			getView().setUtenteCreazione(anagraficaRuolo.getUsernameCreazione());
			getView().setDataCreazione(anagraficaRuolo.getDataCreazione());
			getView().mostraFormDettaglio(anagraficaRuolo, settore, abilitazioniRuolo, showAction);

		} else {
			getView().showErrors(Arrays.asList("Il ruolo selezionato non esiste"));
		}
	}

	private class CreaPerCopiaCommand implements Command {

		@Override
		public void execute() {

			getView().resetErrors();

			if (isDettaglio) {
				dettaglioRuoloPerCopia(anagraficaRuoloOriginale, settoreOriginale, abilitazioniRuoloOriginali);
			}
		}

	}

	private class SalvaCommand implements Command {

		@Override
		public void execute() {
			getView().resetErrors();

			ShowAppLoadingEvent.fire(DettaglioAnagraficaGruppiPresenter.this, true);
			Map<String, Object> modifiche = new HashMap<String, Object>();

			if (isDettaglio) {

				List<String> errors = controllaForm(true, modifiche);
				if (errors.isEmpty()) {
					AnagraficaRuolo ar = getAnagraficaRuolo();

					StringBuilder descrizioneAzione = new StringBuilder("Modifica anagrafica gruppo");
					if (!modifiche.isEmpty()) {
						descrizioneAzione.append(" - Campi aggiornati: ");
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
					List<Azione> azioniRuolo = new ArrayList<Azione>();
					azioniRuolo.add(a);

					amministrazioneGruppiApiClient.modifica(ar, getView().getSettore().getNome(), getView().getAbilitazioniDaAggiungere(), getView().getAbilitazioniDaRimuovere(), azioniRuolo,
							getView().getAzioni(), new AnagraficaRuoloCallback() {

								@Override
								public void onSuccess(AnagraficaRuoloResponse a) {
									ShowAppLoadingEvent.fire(DettaglioAnagraficaGruppiPresenter.this, false);

									if (a.isError()) {
										List<String> errors = new ArrayList<>();
										errors.add("Gruppo modificato con i seguenti errori:");
										errors.addAll(a.getErrors());
										getView().showErrors(errors);
									}

									dettaglioRuolo(a.getAnagraficaRuolo(), a.getSettoreRuolo(), a.getAbilitazioniRuolo(), false);
								}

								@Override
								public void onSuccess(List<AnagraficaRuolo> a, int count) {/**/}

								@Override
								public void onError(String error) {
									ShowAppLoadingEvent.fire(DettaglioAnagraficaGruppiPresenter.this, false);
									getView().showErrors(Arrays.asList(error));
								}
							});

				} else {
					ShowAppLoadingEvent.fire(DettaglioAnagraficaGruppiPresenter.this, false);
					getView().showErrors(errors);
				}

			} else {

				List<String> errors = controllaForm(false, modifiche);
				if (errors.isEmpty()) {
					AnagraficaRuolo ar = getAnagraficaRuolo();
					Azione a = new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), ar.getDataCreazione(), "Inserimento anagrafica gruppo");
					List<Azione> azioniRuolo = new ArrayList<Azione>();
					azioniRuolo.add(a);

					amministrazioneGruppiApiClient.inserisci(ar, getView().getSettore().getNome(), getView().getAbilitazioniDaAggiungere(), azioniRuolo, getView().getAzioni(),
							new AnagraficaRuoloCallback() {

								@Override
								public void onSuccess(AnagraficaRuoloResponse a) {
									ShowAppLoadingEvent.fire(DettaglioAnagraficaGruppiPresenter.this, false);

									if (a.isError()) {
										List<String> errors = new ArrayList<>();
										errors.add("Gruppo creato con i seguenti errori:");
										errors.addAll(a.getErrors());
										getView().showErrors(errors);
									}

									dettaglioRuolo(a.getAnagraficaRuolo(), a.getSettoreRuolo(), a.getAbilitazioniRuolo(), false);
								}

								@Override
								public void onSuccess(List<AnagraficaRuolo> a, int count) {/**/}

								@Override
								public void onError(String error) {
									ShowAppLoadingEvent.fire(DettaglioAnagraficaGruppiPresenter.this, false);
									getView().showErrors(Arrays.asList(error));
								}
							});

				} else {
					ShowAppLoadingEvent.fire(DettaglioAnagraficaGruppiPresenter.this, false);
					getView().showErrors(errors);
				}
			}
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

	private List<String> controllaForm(boolean controllaModifiche, Map<String, Object> modifiche) {

		List<String> errors = new ArrayList<String>();
		AnagraficaRuolo ar = getAnagraficaRuolo();

		if (ar == null) {
			errors.add("Gruppo non valido");
			return errors;
		}

		if (ar.getRuolo() == null || ar.getRuolo().trim().isEmpty()) {
			errors.add("Il ruolo LDAP è obbligatorio");
		}

		if (ar.getEtichetta() == null || ar.getEtichetta().trim().isEmpty()) {
			errors.add("Il nome del gruppo è obbligatorio");
		}

		if (ar.getStato() == null) {
			errors.add("Lo stato è obbligatorio");
		}

		if (ar.getEmailPredefinita() != null && !ar.getEmailPredefinita().isEmpty() && !ValidationUtilities.validateEmailAddress(ar.getEmailPredefinita())) {
			errors.add("Indirizzo email non valido");
		}

		if (getView().getSettore() == null) {
			errors.add("Settore non valido");
		}

		if (errors.isEmpty() && !controllaModifiche) {
			if (configurazioniHandler.getAnagraficaRuolo(ar.getRuolo()) != null) {
				errors.add("Il gruppo LDAP esiste già");
			}

			if (configurazioniHandler.getAnagraficaRuoloByEtichetta(ar.getEtichetta()) != null) {
				errors.add("Il gruppo esiste già");
			}
		}

		if (errors.isEmpty() && controllaModifiche) {

			if (!ar.getRuolo().equals(anagraficaRuoloOriginale.getRuolo())) {
				errors.add("Non è possibile modificare il gruppo LDAP");
			}
		}

		if (controllaModifiche && errors.isEmpty()) {

			if (!ar.getEtichetta().equals(anagraficaRuoloOriginale.getEtichetta())) {
				modifiche.put("nome", ar.getEtichetta());
			}

			if (!ar.getStato().equals(anagraficaRuoloOriginale.getStato())) {
				modifiche.put("stato", Stato.ATTIVA.equals(ar.getStato()) ? "attivo" : "disattivo");
			}

			if ((ar.getEmailPredefinita() != null && !ar.getEmailPredefinita().equalsIgnoreCase(anagraficaRuoloOriginale.getEmailPredefinita()))
					|| (anagraficaRuoloOriginale.getEmailPredefinita() != null && !anagraficaRuoloOriginale.getEmailPredefinita().equalsIgnoreCase(ar.getEmailPredefinita()))) {

				modifiche.put("mail di assegnazione", ar.getEmailPredefinita() != null ? ar.getEmailPredefinita() : "");
			}

			if ((getView().getSettore() != null && !getView().getSettore().equals(settoreOriginale)) || (settoreOriginale != null && !settoreOriginale.equals(getView().getSettore()))) {
				modifiche.put("settore", getView().getSettore() != null ? getView().getSettore().getNome() : "");
			}
		}

		return errors;
	}

	private AnagraficaRuolo getAnagraficaRuolo() {
		AnagraficaRuolo ar = getView().getAnagraficaRuolo();

		if (isDettaglio) {
			ar.getOperatori().addAll(anagraficaRuoloOriginale.getOperatori());
			ar.getAzioni().addAll(anagraficaRuoloOriginale.getAzioni());
		}

		return ar;
	}
}
