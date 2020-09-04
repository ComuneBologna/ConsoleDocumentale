package it.eng.portlet.consolepec.gwt.client.presenter.configurazioni;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneFascicoliAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneIngressiAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneRuoliAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaRuoloResponse;
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
public class DettaglioAbilitazionePresenter extends Presenter<DettaglioAbilitazionePresenter.MyView, DettaglioAbilitazionePresenter.MyProxy> {

	@ProxyCodeSplit
	@NameToken({ NameTokens.dettaglioabilitazione, NameTokens.creaabilitazione })
	public interface MyProxy extends ProxyPlace<DettaglioAbilitazionePresenter> {
		//
	}

	public interface MyView extends View {
		void mostraFormDettaglio(AnagraficaRuolo anagraficaRuolo, AbilitazioniRuolo abilitazioniRuolo);

		void mostraFormCreazione();

		void setSalvaRuoloCommand(Command command);

		void setChiudiCommand(Command command);

		void setDataCreazione(Date dataCreazione);

		void setUtenteCreazione(String utente);

		void showErrors(List<String> errors);

		void resetErrors();

		AnagraficaRuolo getRuoloSelezionato();

		boolean isAmministratore();

		List<Abilitazione> getAbilitazioniDaAggiungere();

		List<Abilitazione> getAbilitazioniDaRimuovere();

		List<String> getOperatori();

		void setOnSelectRuoloCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AnagraficaRuolo> command);

		void clearView();

		List<Azione> getAzioni();
	}

	private AmministrazioneAnagraficaRuoliApiClient amministrazioneGruppiApiClient;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	/*
	 * Var. stato
	 */
	private String parentPlace;
	private AnagraficaRuolo anagraficaRuoloOriginale;
	private AbilitazioniRuolo abilitazioniRuoloOriginali;

	@Inject
	public DettaglioAbilitazionePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, AmministrazioneAnagraficaRuoliApiClient amministrazioneGruppiApiClient,
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
		getView().setOnSelectRuoloCommand(new OnChangeRuoloCommand());
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

			if (request.getNameToken().equals(NameTokens.dettaglioabilitazione)) {
				dettaglioAbilitazioni(request.getParameter(NameTokensParams.ruolo, null));

			} else if (request.getNameToken().equals(NameTokens.creaabilitazione)) {
				creaAbilitazioni();

			} else {
				throw new IllegalArgumentException();
			}

		} else {
			throw new IllegalArgumentException("Utente non amministratore");
		}
	}

	private void creaAbilitazioni() {
		this.anagraficaRuoloOriginale = null;
		this.abilitazioniRuoloOriginali = null;

		getView().setUtenteCreazione(profilazioneUtenteHandler.getDatiUtente().getUsername());
		getView().setDataCreazione(new Date());
		getView().mostraFormCreazione();
	}

	private void dettaglioAbilitazioni(String ruolo) {
		AnagraficaRuolo ar = configurazioniHandler.getAnagraficaRuolo(ruolo);
		AbilitazioniRuolo abilitazioniRuolo = configurazioniHandler.getAbilitazioniRuolo(ruolo);
		dettaglioAbilitazioni(ar, abilitazioniRuolo);
	}

	private void dettaglioAbilitazioni(AnagraficaRuolo anagraficaRuolo, AbilitazioniRuolo abilitazioniRuolo) {

		if (anagraficaRuolo != null) {
			this.anagraficaRuoloOriginale = anagraficaRuolo;
			this.abilitazioniRuoloOriginali = abilitazioniRuolo;

			getView().setUtenteCreazione(anagraficaRuolo.getUsernameCreazione());
			getView().setDataCreazione(anagraficaRuolo.getDataCreazione());
			getView().mostraFormDettaglio(anagraficaRuolo, abilitazioniRuolo);

		} else {
			getView().showErrors(Arrays.asList("Il gruppo selezionato non esiste"));
		}
	}

	private class SalvaCommand implements Command {

		@Override
		public void execute() {
			getView().resetErrors();

			ShowAppLoadingEvent.fire(DettaglioAbilitazionePresenter.this, true);

			List<String> errors = controllaForm();

			if (errors.isEmpty()) {
				AnagraficaRuolo ar = getAnagraficaRuolo();
				Azione a = new Azione(profilazioneUtenteHandler.getDatiUtente().getUsername(), new Date(), "Modifica abilitazioni");
				List<Azione> azioniRuolo = new ArrayList<Azione>();
				azioniRuolo.add(a);

				amministrazioneGruppiApiClient.modifica(ar, null, getAbilitazioniDaAggiungere(), getAbilitazioniDaRimuovere(), azioniRuolo, getView().getAzioni(), new AnagraficaRuoloCallback() {

					@Override
					public void onSuccess(AnagraficaRuoloResponse a) {
						ShowAppLoadingEvent.fire(DettaglioAbilitazionePresenter.this, false);

						if (a.isError()) {
							a.getErrors().add("Abilitazioni modificate con i seguenti errori:");
							getView().showErrors(a.getErrors());
						}

						dettaglioAbilitazioni(a.getAnagraficaRuolo(), a.getAbilitazioniRuolo());
					}

					@Override
					public void onSuccess(List<AnagraficaRuolo> a, int count) {/**/}

					@Override
					public void onError(String error) {
						ShowAppLoadingEvent.fire(DettaglioAbilitazionePresenter.this, false);
						getView().showErrors(Arrays.asList(error));
					}
				});

			} else {
				ShowAppLoadingEvent.fire(DettaglioAbilitazionePresenter.this, false);
				getView().showErrors(errors);
			}
		}
	}

	private class AnnullaCommand implements Command {

		@Override
		public void execute() {
			getView().resetErrors();
			Place place = new Place();
			place.setToken(parentPlace != null ? parentPlace : NameTokens.listaabilitazioni);
			getEventBus().fireEvent(new GoToPlaceEvent(place));
		}
	}

	private AnagraficaRuolo getAnagraficaRuolo() {
		AnagraficaRuolo ar = getView().getRuoloSelezionato();

		if (ar != null) {
			ar.getOperatori().clear();
			ar.getOperatori().addAll(getView().getOperatori());
		}

		return ar;
	}

	private List<Abilitazione> getAbilitazioniDaAggiungere() {
		List<Abilitazione> abilitazioniDaAggiungere = getView().getAbilitazioniDaAggiungere();

		if (getView().isAmministratore()) {
			for (Abilitazione a : getAbilitazioniDaAmministratore()) {
				if (abilitazioniRuoloOriginali != null) {
					if (!abilitazioniRuoloOriginali.getAbilitazioni().contains(a)) {
						abilitazioniDaAggiungere.add(a);
					}
				} else {
					abilitazioniDaAggiungere.add(a);
				}

			}
		}

		return abilitazioniDaAggiungere;
	}

	private List<Abilitazione> getAbilitazioniDaRimuovere() {
		List<Abilitazione> abilitazioniDaRimuovere = getView().getAbilitazioniDaRimuovere();

		if (!getView().isAmministratore()) {
			for (Abilitazione a : getAbilitazioniDaAmministratore()) {
				if (abilitazioniRuoloOriginali != null) {
					if (!abilitazioniRuoloOriginali.getAbilitazioni().contains(a)) {
						abilitazioniDaRimuovere.add(a);
					}
				} else {
					abilitazioniDaRimuovere.add(a);
				}

			}
		}

		return abilitazioniDaRimuovere;
	}

	private List<Abilitazione> getAbilitazioniDaAmministratore() {
		List<Abilitazione> res = new ArrayList<Abilitazione>();

		Abilitazione a1 = new AmministrazioneRuoliAbilitazione();
		a1.setDataCreazione(new Date());
		a1.setUsernameCreazione(profilazioneUtenteHandler.getDatiUtente().getUsername());
		Abilitazione a2 = new AmministrazioneFascicoliAbilitazione();
		a2.setDataCreazione(new Date());
		a2.setUsernameCreazione(profilazioneUtenteHandler.getDatiUtente().getUsername());
		Abilitazione a3 = new AmministrazioneIngressiAbilitazione();
		a3.setDataCreazione(new Date());
		a3.setUsernameCreazione(profilazioneUtenteHandler.getDatiUtente().getUsername());

		res.add(a1);
		res.add(a2);
		res.add(a3);

		return res;
	}

	private List<String> controllaForm() {
		List<String> errors = new ArrayList<String>();
		AnagraficaRuolo ar = getAnagraficaRuolo();

		if (ar == null) {
			errors.add("Gruppo non valido");
			return errors;
		}

		if (!ar.getRuolo().equals(anagraficaRuoloOriginale.getRuolo())) {
			errors.add("Gruppo non valido");
			return errors;
		}

		return errors;
	}

	private class OnChangeRuoloCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AnagraficaRuolo> {

		@Override
		public Void exe(AnagraficaRuolo t) {
			AbilitazioniRuolo ar = configurazioniHandler.getAbilitazioniRuolo(t.getRuolo());
			dettaglioAbilitazioni(t, ar);
			return null;
		}

	}
}
