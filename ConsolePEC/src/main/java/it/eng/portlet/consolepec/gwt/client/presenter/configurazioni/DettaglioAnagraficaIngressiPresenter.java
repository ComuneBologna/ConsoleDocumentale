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

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.AmministrazioneIngressiAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.LetturaIngressoAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.services.AnagraficaIngressoResponse;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaIngressoApiClient;
import it.eng.portlet.consolepec.gwt.client.operazioni.amministrazione.AmministrazioneAnagraficaIngressoApiClient.AnagraficaIngressoCallback;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;

public class DettaglioAnagraficaIngressiPresenter extends Presenter<DettaglioAnagraficaIngressiPresenter.MyView, DettaglioAnagraficaIngressiPresenter.MyProxy> {

	@ProxyCodeSplit
	@NameToken({ NameTokens.dettaglioanagraficaingresso, NameTokens.creaanagraficaingresso })
	public interface MyProxy extends ProxyPlace<DettaglioAnagraficaIngressiPresenter> {
		//
	}

	public interface MyView extends View {

		void mostraFormDettaglio(AnagraficaIngresso anagraficaIngresso, AnagraficaRuolo primoAssegnatario, List<AnagraficaRuolo> gruppiAbilitatiLettura, boolean showAction);

		void mostraFormCreazione();

		void setSalvaIngressoCommand(Command command);

		void setChiudiCommand(Command command);

		void setDataCreazione(Date dataCreazione);

		void setUtenteCreazione(String utente);

		void showErrors(List<String> errors);

		void resetErrors();

		AnagraficaIngresso getAnagraficaIngresso();

		AnagraficaRuolo getPrimoAssegnatario();

		boolean isCreaEmailOut();

		void clearView();
	}

	private AmministrazioneAnagraficaIngressoApiClient amministrazioneAnagraficaIngressoApiClient;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	/*
	 * Var. stato
	 */
	private String parentPlace;
	private boolean isDettaglio;
	private AnagraficaIngresso anagraficaIngressoOriginale;
	private AnagraficaRuolo assegnatarioOriginale;

	@Inject
	public DettaglioAnagraficaIngressiPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, AmministrazioneAnagraficaIngressoApiClient amministrazioneAnagraficaIngressoApiClient,
			ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler) {

		super(eventBus, view, proxy);

		this.amministrazioneAnagraficaIngressoApiClient = amministrazioneAnagraficaIngressoApiClient;
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setSalvaIngressoCommand(new SalvaCommand());
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
		if (profilazioneUtenteHandler.isAbilitato(AmministrazioneIngressiAbilitazione.class)) {
			parentPlace = request.getParameter(NameTokensParams.parentPlace, null);

			if (request.getNameToken().equals(NameTokens.dettaglioanagraficaingresso)) {
				dettaglioIngresso(request.getParameter(NameTokensParams.indirizzoIngresso, null), Boolean.parseBoolean(request.getParameter(NameTokensParams.showActions, null)));

			} else if (request.getNameToken().equals(NameTokens.creaanagraficaingresso)) {
				creaIngresso();

			} else {
				throw new IllegalArgumentException();
			}

		} else {
			throw new IllegalArgumentException("Utente non amministratore");
		}
	}

	private void creaIngresso() {
		isDettaglio = false;
		this.anagraficaIngressoOriginale = null;
		this.assegnatarioOriginale = null;

		getView().setUtenteCreazione(profilazioneUtenteHandler.getDatiUtente().getUsername());
		getView().setDataCreazione(new Date());
		getView().mostraFormCreazione();

	}

	private void dettaglioIngresso(String indirizzo, boolean showRuoli) {
		AnagraficaIngresso ai = configurazioniHandler.getAnagraficaIngresso(TipologiaPratica.EMAIL_IN.getNomeTipologia(), indirizzo);
		dettaglioIngresso(ai, showRuoli);
	}

	private void dettaglioIngresso(AnagraficaIngresso anagraficaIngresso, boolean showRuoli) {
		isDettaglio = true;

		if (anagraficaIngresso != null) {
			this.anagraficaIngressoOriginale = anagraficaIngresso;
			this.assegnatarioOriginale = configurazioniHandler.getPrimoAssegnatario(anagraficaIngresso.getIndirizzo(), anagraficaIngresso.getNomeTipologia());

			getView().setUtenteCreazione(anagraficaIngresso.getUsernameCreazione());
			getView().setDataCreazione(anagraficaIngresso.getDataCreazione());

			getView().mostraFormDettaglio(anagraficaIngresso, assegnatarioOriginale,
					configurazioniHandler.getGruppiAbilitati(LetturaIngressoAbilitazione.class, anagraficaIngresso.getIndirizzo(), anagraficaIngresso.getNomeTipologia()), showRuoli);

		} else {
			getView().showErrors(Arrays.asList("L'ingresso selezionato non esiste"));
		}

	}

	private class SalvaCommand implements Command {

		@Override
		public void execute() {
			getView().resetErrors();
			ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, true);

			final List<String> errors = controllaForm(isDettaglio);
			final boolean aggiornaPrimoAssegnatario = aggiornaPrimoAssegnatario();

			if (!aggiornaPrimoAssegnatario && !errors.isEmpty()) {
				ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);
				getView().showErrors(errors);

			} else if (isDettaglio) {
				final AnagraficaIngresso ai = getAnagraficaIngresso();

				if (errors.isEmpty()) {
					amministrazioneAnagraficaIngressoApiClient.modifica(ai, new AnagraficaIngressoCallback() {

						@Override
						public void onSuccess(final AnagraficaIngressoResponse a) {
							ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);

							if (aggiornaPrimoAssegnatario) {
								amministrazioneAnagraficaIngressoApiClient.aggiornaPrimoAssegnatario(ai.getNomeTipologia(), ai.getIndirizzo(),

										getView().getPrimoAssegnatario() != null ? getView().getPrimoAssegnatario().getRuolo() : null, new AnagraficaIngressoCallback() {

											@Override
											public void onSuccess(List<AnagraficaIngresso> a1, int count) {}

											@Override
											public void onSuccess(AnagraficaIngressoResponse a1) {
												ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);

												List<String> errors1 = new ArrayList<>();
												if (!a1.getErrors().isEmpty()) {
													errors1.addAll(a1.getErrors());
												}

												if (!a.getErrors().isEmpty()) {
													errors1.addAll(a.getErrors());
												}

												if (!errors1.isEmpty()) {
													getView().showErrors(errors1);
												}

												if (a1.getAnagraficaIngresso() != null) {
													dettaglioIngresso(a1.getAnagraficaIngresso(), false);

												} else if (a.getAnagraficaIngresso() != null) {
													dettaglioIngresso(a.getAnagraficaIngresso(), false);
												}
											}

											@Override
											public void onError(String error) {
												ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);

												List<String> errors1 = new ArrayList<>();
												errors1.addAll(Arrays.asList(error));

												if (!a.getErrors().isEmpty()) {
													errors1.addAll(a.getErrors());
												}

												getView().showErrors(errors1);
											}
										});

							} else {
								List<String> errors1 = new ArrayList<>();
								if (!a.getErrors().isEmpty()) {
									errors1.addAll(a.getErrors());
								}

								if (!errors1.isEmpty()) {
									getView().showErrors(errors1);
								}

								if (a.getAnagraficaIngresso() != null) {
									dettaglioIngresso(a.getAnagraficaIngresso(), false);
								}
							}
						}

						@Override
						public void onSuccess(List<AnagraficaIngresso> a, int count) {}

						@Override
						public void onError(String error) {
							ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);
							getView().showErrors(Arrays.asList(error));
						}
					});

				} else {
					amministrazioneAnagraficaIngressoApiClient.aggiornaPrimoAssegnatario(ai.getNomeTipologia(), ai.getIndirizzo(),

							getView().getPrimoAssegnatario() != null ? getView().getPrimoAssegnatario().getRuolo() : null, new AnagraficaIngressoCallback() {

								@Override
								public void onSuccess(List<AnagraficaIngresso> a1, int count) {}

								@Override
								public void onSuccess(AnagraficaIngressoResponse a1) {
									ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);
									List<String> errors1 = new ArrayList<>();
									errors1.addAll(errors);

									if (!a1.getErrors().isEmpty()) {
										errors1.addAll(a1.getErrors());
									}

									getView().showErrors(errors1);

									if (a1.getAnagraficaIngresso() != null) {
										dettaglioIngresso(a1.getAnagraficaIngresso(), false);
									}
								}

								@Override
								public void onError(String error) {
									ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);
									List<String> errors1 = new ArrayList<>();
									errors1.addAll(Arrays.asList(error));
									errors1.addAll(errors);
									getView().showErrors(errors1);
								}
							});
				}

			} else {
				final AnagraficaIngresso ai = getAnagraficaIngresso();

				if (errors.isEmpty()) {
					amministrazioneAnagraficaIngressoApiClient.inserisci(ai, getView().isCreaEmailOut(), new AnagraficaIngressoCallback() {

						@Override
						public void onSuccess(final AnagraficaIngressoResponse a) {
							ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);

							if (aggiornaPrimoAssegnatario && a.getAnagraficaIngresso() != null) {
								amministrazioneAnagraficaIngressoApiClient.aggiornaPrimoAssegnatario(ai.getNomeTipologia(), ai.getIndirizzo(),

										getView().getPrimoAssegnatario() != null ? getView().getPrimoAssegnatario().getRuolo() : null, new AnagraficaIngressoCallback() {

											@Override
											public void onSuccess(List<AnagraficaIngresso> a1, int count) {}

											@Override
											public void onSuccess(AnagraficaIngressoResponse a1) {
												ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);

												List<String> errors1 = new ArrayList<>();
												if (!a1.getErrors().isEmpty()) {
													errors1.addAll(a1.getErrors());
												}

												if (!a.getErrors().isEmpty()) {
													errors1.addAll(a.getErrors());
												}

												if (!errors1.isEmpty()) {
													getView().showErrors(errors1);
												}

												if (a1.getAnagraficaIngresso() != null) {
													dettaglioIngresso(a1.getAnagraficaIngresso(), false);

												} else if (a.getAnagraficaIngresso() != null) {
													dettaglioIngresso(a.getAnagraficaIngresso(), false);
												}
											}

											@Override
											public void onError(String error) {
												ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);

												List<String> errors1 = new ArrayList<>();
												errors1.addAll(Arrays.asList(error));

												if (!a.getErrors().isEmpty()) {
													errors1.addAll(a.getErrors());
												}

												getView().showErrors(errors1);
											}
										});

							} else if (!a.getErrors().isEmpty()) {
								getView().showErrors(a.getErrors());

							} else {
								getView().showErrors(Arrays.asList(ConsolePecConstants.ERROR_MESSAGE));
							}
						}

						@Override
						public void onSuccess(List<AnagraficaIngresso> a, int count) {}

						@Override
						public void onError(String error) {
							ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);
							getView().showErrors(Arrays.asList(error));
						}
					});

				} else {
					ShowAppLoadingEvent.fire(DettaglioAnagraficaIngressiPresenter.this, false);
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

	private boolean aggiornaPrimoAssegnatario() {
		return (assegnatarioOriginale == null && getView().getPrimoAssegnatario() != null) || (assegnatarioOriginale != null && !assegnatarioOriginale.equals(getView().getPrimoAssegnatario()));
	}

	private List<String> controllaForm(boolean controllaModifiche) {
		List<String> errors = new ArrayList<String>();
		AnagraficaIngresso ai = getAnagraficaIngresso();

		if (ai == null) {
			errors.add("Ingresso non valido");
			return errors;
		}

		if (ai.getIndirizzo() == null || ai.getIndirizzo().trim().isEmpty()) {
			errors.add("Nome casella obbligatoria");

		} else if (!ValidationUtilities.validateEmailAddress(ai.getIndirizzo())) {
			errors.add("Indirizzo casella non valido");
		}

		if (ai.getServer() == null) {
			errors.add("Server obbligatorio");
		}

		if (ai.getUtenza() == null || ai.getUtenza().trim().isEmpty()) {
			errors.add("Utenza obbligatoria");
		}

		if (ai.getFolderIn() == null || ai.getFolderIn().trim().isEmpty()) {
			errors.add("Cartella di lettura obbligatoria");
		}

		if (ai.getFolderTo() == null || ai.getFolderTo().trim().isEmpty()) {
			errors.add("Cartella di scarico obbligatoria");
		}

		if (getView().getPrimoAssegnatario() == null) {
			errors.add("Primo assegnatario obbligatorio");
		}

		if (ai.isCancellazioneAutomatica() && ai.getGiorniCancellazione() == null) {
			errors.add("Indicare i giorni di cancellazione automatica se si desidera attivarla");
		}

		if (errors.isEmpty() && controllaModifiche) {
			boolean edited = false;

			if (!ai.getNomeTipologia().equals(anagraficaIngressoOriginale.getNomeTipologia()) || !ai.getEtichettaTipologia().equals(anagraficaIngressoOriginale.getEtichettaTipologia())
					|| !ai.getDettaglioNameToken().equals(anagraficaIngressoOriginale.getDettaglioNameToken()) || ai.isAzioniDettaglio() != anagraficaIngressoOriginale.isAzioniDettaglio()) {
				errors.add("Ingresso non valido");
			}

			if (!ai.getIndirizzo().equalsIgnoreCase(anagraficaIngressoOriginale.getIndirizzo())) {
				errors.add("Non è possibile modificare l'indirizzo della casella");
			}

			if (!ai.getServer().equals(anagraficaIngressoOriginale.getServer())) {
				errors.add("Non è possibile modificare il server");
			}

			if (!ai.getPassword().equals(anagraficaIngressoOriginale.getPassword())) {
				edited = true;
			}

			if (!ai.getUtenza().equals(anagraficaIngressoOriginale.getUtenza())) {
				edited = true;
			}

			if (ai.isCancellazioneAutomatica() != anagraficaIngressoOriginale.isCancellazioneAutomatica()) {
				edited = true;
			}

			if (ai.getGiorniCancellazione() != anagraficaIngressoOriginale.getGiorniCancellazione()) {
				edited = true;
			}

			if (!ai.getFolderIn().equals(anagraficaIngressoOriginale.getFolderIn())) {
				edited = true;
			}

			if (!ai.getFolderTo().equals(anagraficaIngressoOriginale.getFolderTo())) {
				edited = true;
			}

			if (ai.isScaricoRicevute() != anagraficaIngressoOriginale.isScaricoRicevute()) {
				edited = true;
			}

			if (!ai.getStato().equals(anagraficaIngressoOriginale.getStato())) {
				edited = true;
			}

			if (!edited) {
				errors.add("Nessuna modifica effettuata sui parametri dell'ingresso");
			}
		}

		return errors;
	}

	private AnagraficaIngresso getAnagraficaIngresso() {
		AnagraficaIngresso ai = getView().getAnagraficaIngresso();

		if (isDettaglio) {
			TipologiaPratica tp = PraticaUtil.toTipologiaPratica(anagraficaIngressoOriginale);
			ai.setNomeTipologia(tp.getNomeTipologia());
			ai.setEtichettaTipologia(tp.getEtichettaTipologia());
			ai.setDettaglioNameToken(tp.getDettaglioNameToken());
			ai.setAzioniDettaglio(tp.isAzioniDettaglio());
			ai.getAzioni().addAll(anagraficaIngressoOriginale.getAzioni());
			ai.setProtocollabile(tp.isProtocollabile());

		} else {
			ai.setNomeTipologia(TipologiaPratica.EMAIL_IN.getNomeTipologia());
			ai.setEtichettaTipologia(TipologiaPratica.INGRESSO_EMAIL_IN_ETICHETTA_DEFAULT);
			ai.setDettaglioNameToken(TipologiaPratica.INGRESSO_DETTAGLIO_NAME_TOKEN_DEFAULT);
			ai.setProtocollabile(true);
			ai.setAzioniDettaglio(true);
		}

		return ai;
	}
}
