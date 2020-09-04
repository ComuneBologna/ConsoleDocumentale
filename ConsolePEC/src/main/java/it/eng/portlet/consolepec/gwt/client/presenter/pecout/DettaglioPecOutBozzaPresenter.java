package it.eng.portlet.consolepec.gwt.client.presenter.pecout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneEmailOutAbilitazione;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmailOutLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.BackFromPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.MostraDettaglioAllegatoEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione.ConfermaProtocollazionePecOutEvent;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleFineEvent;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleFineEvent.FirmaDigitaleFineHandler;
import it.eng.portlet.consolepec.gwt.client.event.firmadigitale.FirmaDigitaleInizioEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent.BackToFascicoloHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent.UploadStatus;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.IBackToFascicolo;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.ChiudiCaricaAllegatiDaPraticaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.event.ChiudiCaricaAllegatiDaPraticaEvent.ChiudiCaricaAllegatiDaPraticaHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.event.MostraBozzaReinoltroEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.event.MostraBozzaReinoltroEvent.MostraBozzaReinoltroHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.event.MostraDettaglioBozzaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.event.MostraDettaglioBozzaEvent.MostraDettaglioBozzaHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.event.MostraDettaglioPecOutInviataEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila.MostraSceltaCapofilaEmailOutEvent;
import it.eng.portlet.consolepec.gwt.client.view.pecout.DettaglioPecOutBozzaView;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.IndirizziEmailSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.TinyMCEUtils;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.FirmaAllegatoPecOutBozzaAction;
import it.eng.portlet.consolepec.gwt.shared.action.FirmaAllegatoPecOutBozzaActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CancellaAllegatoPecOut;
import it.eng.portlet.consolepec.gwt.shared.action.pec.CancellaAllegatoPecOutResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.EliminaBozza;
import it.eng.portlet.consolepec.gwt.shared.action.pec.EliminaBozzaResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InviaMailAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.InviaMailActionResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ReinoltroAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.ReinoltroResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.SalvaBozzaInvio;
import it.eng.portlet.consolepec.gwt.shared.action.pec.SalvaBozzaInvioResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaAction;
import it.eng.portlet.consolepec.gwt.shared.action.pec.UploadAllegatoPraticaResult;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoComunicazioneRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElencoVisitor;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppo;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGrupppoNonProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPECRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoPraticaModulisticaRiferimento;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.TipologiaInteroperabileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;

public class DettaglioPecOutBozzaPresenter extends Presenter<DettaglioPecOutBozzaPresenter.MyView, DettaglioPecOutBozzaPresenter.MyProxy> implements FirmaDigitaleFineHandler, ChiudiDettaglioAllegatoHandler, ChiudiCaricaAllegatiDaPraticaHandler, MostraDettaglioBozzaHandler, MostraBozzaReinoltroHandler, IBackToFascicolo, BackToFascicoloHandler {

	public interface MyView extends View {

		Button getChiudiButton();

		List<String> getDestinatari();

		List<String> getDestinatariCC();

		String getOggetto();

		String getBodyEmail();

		String getFirmaEmail();

		String getMittenteEmail();

		void setSuggestDestinatari(SuggestOracle suggestOracle);

		PecOutDTO getBozzaPecOutDto();

		void mostraBozza(PecOutDTO pec, FascicoloDTO fascicolo);

		void mostraReinoltro(PecOutDTO pec, FascicoloDTO fascicolo);

		void init();

		void setCommandFirmaAllegato(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> command);

		void setCommandCancellaAllegato(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> command);

		void setBozzaPecOutDto(PecOutDTO bozza);

		Button getEliminaBozzaButton();

		ListBox getMittentiListBox();

		void setUploadAllegatoCommand(UploadAllegatoCommand uploadAllegatoCommand);

		void startUpload();

		void setSuggestOracleDestinatari(SuggestOracle suggestBox);

		void setDestinatarioRequired(boolean b);

		void disabilitaGUI(PecOutDTO bozza, FascicoloDTO fascicolo);

		void setMostraDettaglioAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand);

		void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand);

		void setInviaBozza(Command inviaBozzaCommand);

		void sendDownload(SafeUri uri);

		void cancellaListaAllegatiSelezionati();

		void setCommandMostraCaricaAllegatiDaPratica(Command mostraCaricaAllegatiDaPraticaCommand);

		void scrollDown();

		Set<AllegatoDTO> getAllegatiSelezionati();

		void scrollUp();

		void resetFocus();

		void setButtonSalvaLabel(boolean isSalvaDisabilitato);

		TreeSet<Destinatario> getNuoviDestinatari();

		TreeSet<Destinatario> getNuoviDestinatariCC();

		boolean isAllegatoPrincipaleSelezionato();

		TipologiaInteroperabileDTO getTipologiaInteroperabile();

		void aggiornaTabellaAllegati(PecOutDTO bozzaPecOut, FascicoloDTO fascicoloCollegato, boolean checkBoxVisibile);

		PecOutDTO getPecOutDTO();

		void setSalvaFirma(boolean salvaFirma);

		boolean isSalvaFirma();

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<DettaglioPecOutBozzaPresenter> {}

	private String idPratica; // email corrente
	private String idFascicolo; // fascicolo contenente l'email corrente
	private boolean interoperabile;

	private final DispatchAsync dispatcher;
	private final PecInPraticheDB pecInDb;
	private final SitemapMenu siteMapMenu;
	private final PlaceManager placeManager;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;
	private Command chiudiCommand;
	private Command revealCommand;
	private Command inviaCommand;

	boolean isInviata = false;// se la bozza è inviata viene impostata nell'
								// onReveal
	private boolean scrollDown = false;
	private PecOutDTO pecOutDto;

	@Inject
	public DettaglioPecOutBozzaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PecInPraticheDB pecInDb, final SitemapMenu siteMap,
			final PlaceManager placeManager, ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		this.pecInDb = pecInDb;
		this.siteMapMenu = siteMap;
		this.placeManager = placeManager;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
	}

	@Override
	protected void revealInParent() {

		if (!profilazioneUtenteHandler.isAbilitato(CreazioneEmailOutAbilitazione.class)) {
			throw new IllegalArgumentException("Utente non abilitato alla creazione di email in uscita");
		}

		RevealContentEvent.fire(this, DettaglioPecOutPresenter.TYPE_SetDettaglio, this);
	}

	@Override
	protected void onHide() {
		getView().resetFocus();
		TinyMCEUtils.removeTinyMCE();
		super.onHide();
		dropMessage();
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().init();

		impostaMittenti();

		getView().setCommandMostraCaricaAllegatiDaPratica(new MostraCaricaAllegatiDaPraticaCommand());
		getView().setCommandFirmaAllegato(new FirmaAllegatoCommand());
		getView().setCommandCancellaAllegato(new CommandCancellaAllegato());
		getView().setInviaBozza(new Command() {

			@Override
			public void execute() {
				inviaCommand.execute();
			}
		});
		getView().setDownloadAllegatoCommand(new DownloadAllegatoCommand());
		getView().getChiudiButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				if (!isInviata) {// se è gia stata inviata il salvataggio non
									// occorre
					/* reset dei messaggi di errore */
					ShowMessageEvent event2 = new ShowMessageEvent();
					event2.setMessageDropped(true);
					getEventBus().fireEvent(event2);
					salvaBozza(new AsyncCallback<SalvaBozzaInvioResult>() {

						@Override
						public void onFailure(Throwable caught) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							getEventBus().fireEvent(event);
						}

						@Override
						public void onSuccess(SalvaBozzaInvioResult result) {
							if (result.getError()) {
								writeErrorMessage(result.getMessError());
								return;
							}

							chiudiCommand.execute();
						}
					});

				} else {
					chiudiCommand.execute();
				}

			}
		});

		getView().getEliminaBozzaButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eliminaBozza();

			}
		});

		getView().setUploadAllegatoCommand(new UploadAllegatoCommand());
		getView().setMostraDettaglioAllegatoCommand(new MostraDettaglioAllegatoCommand());

	}

	protected boolean controllaForm() {

		if (getView().getDestinatari().size() == 0) {
			getView().setDestinatarioRequired(true);
			ShowMessageEvent event = new ShowMessageEvent();
			event.setErrorMessage("Aggiungere almeno un destinatario valido");
			getEventBus().fireEvent(event);
			return true;
		}

		String mittente = getView().getMittenteEmail();
		if (Strings.isNullOrEmpty(mittente)) {

			getView().setDestinatarioRequired(true);
			ShowMessageEvent event = new ShowMessageEvent();
			event.setErrorMessage("Mittente non selezionato");
			getEventBus().fireEvent(event);
			return true;
		}
		String oggetto = getView().getOggetto();
		if (Strings.isNullOrEmpty(oggetto)) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setErrorMessage("Oggetto non inserito.");
			getEventBus().fireEvent(event);
			return true;
		}

		List<String> destinatari = getView().getDestinatari();
		List<String> destinatariCC = getView().getDestinatariCC();

		for (String dest : destinatari) {
			if (!ValidationUtilities.validateEmailAddress(dest)) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage("L'indirizzo email \"" + dest + "\" non è valido.");
				getEventBus().fireEvent(event);
				return true;
			}
		}

		List<String> unionDestDestCC = new ArrayList<String>();
		unionDestDestCC.addAll(destinatari);
		unionDestDestCC.addAll(destinatariCC);

		Set<String> set = new HashSet<String>(unionDestDestCC);
		if (set.size() < unionDestDestCC.size()) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setErrorMessage("Ci sono duplicati tra i destinatari");
			getEventBus().fireEvent(event);
			return true;
		}

		for (String dest : destinatariCC) {
			if (!ValidationUtilities.validateEmailAddress(dest)) {
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage("L'indirizzo email \"" + dest + "\" non è valido.");
				getEventBus().fireEvent(event);
				return true;
			}
		}

		if (interoperabile && getView().getTipologiaInteroperabile() == TipologiaInteroperabileDTO.ALLEGATI && !getView().isAllegatoPrincipaleSelezionato()) {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setErrorMessage("Selezionare almeno un allegato principale");
			getEventBus().fireEvent(event);
			return true;
		}

		return false;
	}

	private void impostaMittenti() {
		final ListBox mittentiListBox = getView().getMittentiListBox();

		List<String> mittentiAbilitati = profilazioneUtenteHandler.getIndirizziEmailInUscitaAbilitati(CreazioneEmailOutAbilitazione.class);
		Map<String, String> mittMap = new HashMap<String, String>();
		for (String mitt : mittentiAbilitati) {
			mittMap.put(mitt, mitt);
		}

		mittentiListBox.clear();
		for (Entry<String, String> e : mittMap.entrySet()) {
			String k = e.getKey();
			String v = e.getValue();
			mittentiListBox.addItem(k, v);
		}

		if (getView().getPecOutDTO() != null) {
			if (getView().getPecOutDTO().getMittente() != null) {
				for (int i = 0; i < mittentiListBox.getItemCount(); i++) {
					String m = mittentiListBox.getValue(i);
					if (getView().getPecOutDTO().getMittente().equals(m)) {
						mittentiListBox.setSelectedIndex(i);
					}
				}
			}
		}
	}

	private void eliminaBozza() {
		ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, true);

		getView().cancellaListaAllegatiSelezionati();

		EliminaBozza eliminaBozzaAction = new EliminaBozza(idPratica, idFascicolo);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
		this.dispatcher.execute(eliminaBozzaAction, new AsyncCallback<EliminaBozzaResult>() {

			@Override
			public void onSuccess(EliminaBozzaResult result) {
				ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);

				if (!result.getError()) {
					pecInDb.remove(idPratica);
					pecInDb.update(result.getFascicolo().getClientID(), result.getFascicolo(), siteMapMenu.containsLink(idPratica));
					pecInDb.remove(idPratica);
					DettaglioPecOutBozzaPresenter.this.siteMapMenu.removeVoice(idPratica);
					getEventBus().fireEvent(new BackFromPlaceEvent(idPratica));

				} else {
					writeErrorMessage(result.getMessError());
				}

			}

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}
		});
	}

	/**
	 * Cancella il file, previo salvataggio
	 *
	 * @param nomeAlleato
	 * @param bozza
	 */
	private void cancellaFile(final Set<AllegatoDTO> allegati) {
		ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, true);
		/* reset dei messaggi di errore */
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
		this.salvaBozza(new AsyncCallback<SalvaBozzaInvioResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(SalvaBozzaInvioResult result) {

				CancellaAllegatoPecOut action = new CancellaAllegatoPecOut(idPratica, allegati);
				/* reset dei messaggi di errore */
				ShowMessageEvent event = new ShowMessageEvent();
				event.setMessageDropped(true);
				getEventBus().fireEvent(event);
				DettaglioPecOutBozzaPresenter.this.dispatcher.execute(action, new AsyncCallback<CancellaAllegatoPecOutResult>() {

					@Override
					public void onSuccess(final CancellaAllegatoPecOutResult result) {
						getView().setBozzaPecOutDto(result.getPecOut());
						ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);

						if (result.getError()) {
							writeErrorMessage(result.getMessError());
						} else {
							dropMessage();
							getView().cancellaListaAllegatiSelezionati();

							pecInDb.getFascicoloByPath(idFascicolo, siteMapMenu.containsLink(idFascicolo), new PraticaFascicoloLoaded() {

								@Override
								public void onPraticaLoaded(FascicoloDTO fascicolo) {
									getView().aggiornaTabellaAllegati(result.getPecOut(), fascicolo, true);
								}

								@Override
								public void onPraticaError(String error) {
									ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
									ShowMessageEvent event = new ShowMessageEvent();
									event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
									getEventBus().fireEvent(event);
								}
							});
						}
					}

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}
				});

			}

		});

	}

	private void salvaBozza(final AsyncCallback<SalvaBozzaInvioResult> extHandler) {
		ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, true);
		PecOutDTO pecOutDto = getView().getBozzaPecOutDto();
		final SalvaBozzaInvio salvaBozzaInvio = new SalvaBozzaInvio(pecOutDto, idFascicolo, idPratica);

		dropMessage();

		/**
		 * Salvataggio firma
		 */
		if (getView().isSalvaFirma()) {
			profilazioneUtenteHandler.aggiornaPreferenzeUtente(pecOutDto.getFirma(), null, null, null);
		}

		this.dispatcher.execute(salvaBozzaInvio, new AsyncCallback<SalvaBozzaInvioResult>() {

			@Override
			public void onSuccess(SalvaBozzaInvioResult res) {
				ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);

				if (!res.getError()) {
					pecInDb.update(res.getBozzaPecOut().getClientID(), res.getBozzaPecOut(), siteMapMenu.containsLink(idPratica));

				} else {
					writeErrorMessage(res.getMessError());
				}
				if (extHandler != null) {
					extHandler.onSuccess(res);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
				if (extHandler != null) {
					extHandler.onFailure(caught);
				} else {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}
			}
		});

	}

	private SuggestOracle getSuggestBox(FascicoloDTO fascicoloDTO) {

		final List<String> mittenti = new ArrayList<String>();

		for (ElementoElenco elementFascicolo : fascicoloDTO.getElenco()) {
			elementFascicolo.accept(new ElementoElencoVisitor() {

				@Override
				public void visit(ElementoGruppoProtocollato subProt) {
					for (ElementoElenco e : subProt.getElementi()) {
						e.accept(this);
					}

				}

				@Override
				public void visit(ElementoGruppoProtocollatoCapofila capofila) {
					for (ElementoElenco e : capofila.getElementi()) {
						e.accept(this);
					}

				}

				@Override
				public void visit(ElementoGruppo noProt) {
					for (ElementoElenco e : noProt.getElementi()) {
						e.accept(this);
					}

				}

				@Override
				public void visit(ElementoPECRiferimento pec) {

					String path = pec.getRiferimento();
					if (pec.getTipo().equals(TipoRiferimentoPEC.IN)) {
						pecInDb.getPecInByPath(path, siteMapMenu.containsLink(path), new PraticaEmaiInlLoaded() {

							@Override
							public void onPraticaLoaded(PecInDTO pec) {

								String mittente = pec.getMittente();
								mittenti.add(mittente);
							}

							@Override
							public void onPraticaError(String error) {
								ShowMessageEvent event = new ShowMessageEvent();
								event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
								getEventBus().fireEvent(event);
							}
						});
					}

				}

				@Override
				public void visit(ElementoAllegato allegato) {

				}

				@Override
				public void visit(ElementoGrupppoNonProtocollato nonProt) {
					for (ElementoElenco e : nonProt.getElementi()) {
						e.accept(this);
					}
				}

				@Override
				public void visit(ElementoPraticaModulisticaRiferimento elementoPraticaModulisticaRiferimento) {
					// NOP
				}

				@Override
				public void visit(ElementoComunicazioneRiferimento elementoComunicazioneRiferimento) {
					// NOP
				}

			});
		}

		IndirizziEmailSuggestOracle oracle = new IndirizziEmailSuggestOracle(mittenti);

		return oracle;

	}

	/**
	 * Avvia il wizard di invio mail, previo salvataggio
	 */
	private void inviaMail() {
		ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, true);
		this.salvaBozza(new AsyncCallback<SalvaBozzaInvioResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}

			@Override
			public void onSuccess(final SalvaBozzaInvioResult result) {
				pecInDb.update(idPratica, result.getBozzaPecOut(), siteMapMenu.containsLink(idPratica));
				pecInDb.getFascicoloByPath(idFascicolo, siteMapMenu.containsLink(idFascicolo), new PraticaFascicoloLoaded() {

					@Override
					public void onPraticaLoaded(FascicoloDTO fascicolo) {
						dropMessage();
						ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);

						if (fascicolo.isProtocolla()) {

							pecInDb.getPecOutByPath(idPratica, siteMapMenu.containsLink(idPratica), new PraticaEmailOutLoaded() {

								@Override
								public void onPraticaLoaded(final PecOutDTO pecOutDto) {
									if (pecOutDto.isInteroperabile() && pecOutDto.getTipologiaInteroperabile() == TipologiaInteroperabileDTO.EMAIL) {

										MostraSceltaCapofilaEmailOutEvent event = new MostraSceltaCapofilaEmailOutEvent();
										event.setIdFascicolo(idFascicolo);
										event.setIdPecOut(idPratica);
										event.setInteroperabile(pecOutDto.isInteroperabile());
										getEventBus().fireEvent(event);

									} else {
										ConfermaProtocollazionePecOutEvent event = new ConfermaProtocollazionePecOutEvent();
										event.setIdFascicolo(idFascicolo);
										event.setIdEmailOut(idPratica);
										getEventBus().fireEvent(event);
									}
								}

								@Override
								public void onPraticaError(String error) {
									ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
									writeErrorMessage(error);
								}
							});

						} else {
							InviaMailAction action = new InviaMailAction(idPratica);

							ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, true);
							/* reset dei messaggi di errore */
							ShowMessageEvent event = new ShowMessageEvent();
							event.setMessageDropped(true);
							getEventBus().fireEvent(event);
							dispatcher.execute(action, new AsyncCallback<InviaMailActionResult>() {

								@Override
								public void onSuccess(InviaMailActionResult imresult) {

									ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
									pecInDb.remove(idFascicolo);
									pecInDb.remove(idPratica);
									if (imresult.getError()) {
										writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
									} else {
										pecInDb.getFascicoloByPath(idFascicolo, true, new PraticaFascicoloLoaded() {

											@Override
											public void onPraticaLoaded(FascicoloDTO fascicolo) {

												Place place = new Place();
												place.setToken(NameTokens.dettagliofascicolo);
												place.addParam(NameTokensParams.idPratica, fascicolo.getClientID());
												getEventBus().fireEvent(new GoToPlaceEvent(place));
											}

											@Override
											public void onPraticaError(String error) {
												writeErrorMessage(error);
											}
										});
									}

								}

								@Override
								public void onFailure(Throwable caught) {
									writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);

								}
							});

						}

					}

					@Override
					public void onPraticaError(String error) {
						writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					}
				});
			}
		});

	}

	@Override
	protected void onReveal() {
		super.onReveal();
		revealCommand.execute();
	}

	public class RevealBozza implements Command {

		@Override
		public void execute() {
			caricaPecOutBozza();
		}
	}

	public class RevealReinoltra implements Command {

		@Override
		public void execute() {
			caricaPecOutReinoltro();
		}
	}

	private void caricaPecOutBozza() {
		dropMessage();
		ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, true);
		pecInDb.getPecOutByPath(idPratica, siteMapMenu.containsLink(idPratica), new PraticaEmailOutLoaded() {

			@Override
			public void onPraticaLoaded(final PecOutDTO pecOutDto) {
				ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
				caricaPecOutBozza(pecOutDto);

			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
				writeErrorMessage(error);
			}
		});
	}

	private void caricaPecOutBozza(final PecOutDTO pecOutDto) {
		idFascicolo = pecOutDto.getIdPraticheCollegate().first();
		pecInDb.getFascicoloByPath(idFascicolo, siteMapMenu.containsLink(idFascicolo), new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(final FascicoloDTO fascicoloDTO) {

				/*
				 * TODO portare in mostraBozza
				 */
				getView().setSuggestOracleDestinatari(getSuggestBox(fascicoloDTO));

				String body = null;
				String firma = null;

				if (pecOutDto != null && !Strings.isNullOrEmpty(pecOutDto.getBody())) {
					body = TinyMCEUtils.escapeTinyMCE(pecOutDto.getBody());
				} else {
					body = "<p></p>";
				}

				if (profilazioneUtenteHandler.getPreferenzeUtente().getFirmaEmail() != null) {
					firma = TinyMCEUtils.escapeTinyMCE(profilazioneUtenteHandler.getPreferenzeUtente().getFirmaEmail());
				} else {
					firma = "<p></p>";
				}

				TinyMCEUtils.setupTinyMCE(body, DettaglioPecOutBozzaView.DOM_BODY_ID, !pecOutDto.isInviata());
				TinyMCEUtils.setupTinyMCE(firma, DettaglioPecOutBozzaView.DOM_FIRMA_ID, !pecOutDto.isInviata());

				/*
				 *
				 */

				isInviata = pecOutDto.isInviata() || pecOutDto.isAzioniDisabilitate();
				getView().setButtonSalvaLabel(isInviata);

				getView().mostraBozza(pecOutDto, fascicoloDTO);
				if (scrollDown) {
					getView().scrollDown();
				} else {
					getView().scrollUp();
				}
				scrollDown = false;
			}

			@Override
			public void onPraticaError(String error) {
				writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			}
		});
	}

	private void caricaPecOutReinoltro() {
		dropMessage();

		pecInDb.getPecOutByPath(idPratica, siteMapMenu.containsLink(idPratica), new PraticaEmailOutLoaded() {

			@Override
			public void onPraticaLoaded(final PecOutDTO pecOutDto) {
				caricaPecOutReinoltro(pecOutDto);

			}

			@Override
			public void onPraticaError(String error) {
				writeErrorMessage(error);
			}
		});
	}

	private void caricaPecOutReinoltro(final PecOutDTO pecOutDto) {
		this.pecOutDto = pecOutDto;
		idFascicolo = pecOutDto.getIdPraticheCollegate().first();
		pecInDb.getFascicoloByPath(idFascicolo, siteMapMenu.containsLink(idFascicolo), new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(final FascicoloDTO fascicoloDTO) {
				/*
				 * TODO portare in mostraReinoltro
				 */
				getView().setSuggestOracleDestinatari(getSuggestBox(fascicoloDTO));

				String body = "";
				String firma = "";

				if (!Strings.isNullOrEmpty(pecOutDto.getBody())) {
					body = pecOutDto.getBody();
				} else {
					body = "<p></p>";
				}

				if (!Strings.isNullOrEmpty(pecOutDto.getFirma())) {
					firma = pecOutDto.getFirma();
				} else {
					firma = "<p></p>";
				}

				TinyMCEUtils.setupTinyMCE(body, DettaglioPecOutBozzaView.DOM_BODY_ID, false);
				TinyMCEUtils.setupTinyMCE(firma, DettaglioPecOutBozzaView.DOM_FIRMA_ID, false);

				/*
				 *
				 */

				getView().setButtonSalvaLabel(true);

				isInviata = pecOutDto.isInviata() || pecOutDto.isAzioniDisabilitate();
				getView().mostraReinoltro(pecOutDto, fascicoloDTO);
				pecInDb.remove(fascicoloDTO.getClientID());
				if (scrollDown) {
					getView().scrollDown();
				} else {
					getView().scrollUp();
				}
				scrollDown = false;
			}

			@Override
			public void onPraticaError(String error) {
				writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			}
		});
	}

	/**
	 * richiama la view, dopo aver scaricato anche il fascicolo
	 *
	 * @param pec
	 */
	private void mostraDettaglioBozza(final PecOutDTO pec) {

		String idFascicolo = pec.getIdPraticheCollegate().first();
		pecInDb.getFascicoloByPath(idFascicolo, siteMapMenu.containsLink(idFascicolo), new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				getView().mostraBozza(pec, fascicolo);
			}

			@Override
			public void onPraticaError(String error) {
				writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			}
		});
	}

	@Override
	@ProxyEvent
	public void onFirmaDigitaleEnd(FirmaDigitaleFineEvent event) {

		if (DettaglioPecOutBozzaPresenter.this.equals(event.getOpeningRequestor())) {
			if (!event.isAnnulla()) {
				ShowAppLoadingEvent.fire(this, true);
				Set<AllegatoDTO> allegatiSelezionati = getView().getAllegatiSelezionati();
				AllegatoDTO[] allegati = allegatiSelezionati.toArray(new AllegatoDTO[0]);
				FirmaAllegatoPecOutBozzaAction action = new FirmaAllegatoPecOutBozzaAction(idPratica, allegati);
				action.setCredenzialiFirma(event.getCredenzialiFirma());
				action.setTipologiaFirma(event.getTipologiaFirma());

				if (event.getCredenzialiFirma() != null) {
					profilazioneUtenteHandler.aggiornaPreferenzeFirmaDigitale(event.getCredenzialiFirma().isSalvaCredenziali(), event.getCredenzialiFirma().getUsername(),
							event.getCredenzialiFirma().getPassword(), null);
				}

				/* reset dei messaggi di errore */
				ShowMessageEvent event2 = new ShowMessageEvent();
				event2.setMessageDropped(true);
				getEventBus().fireEvent(event2);
				dispatcher.execute(action, new AsyncCallback<FirmaAllegatoPecOutBozzaActionResult>() {

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

					@Override
					public void onSuccess(FirmaAllegatoPecOutBozzaActionResult result) {
						if (!result.getError()) {
							if (getView().getAllegatiSelezionati() != null) {
								getView().getAllegatiSelezionati().clear();
							}
							pecInDb.update(result.getPecOutDto().getClientID(), result.getPecOutDto(), siteMapMenu.containsLink(idPratica));
							placeManager.revealCurrentPlace();

						} else {
							writeErrorMessage(result.getMessageError());
						}
						ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
					}

				});

			} else {
				placeManager.revealCurrentPlace();
			}
		}
	}

	@Override
	@ProxyEvent
	public void onChiudiCaricaAllegatiDaPratica(ChiudiCaricaAllegatiDaPraticaEvent event) {
		/* verifico se sia interessato all'evento */
		if (event.getClientIdBozza() != null && event.getClientIdBozza().equals(idPratica)) {

			ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, true);
			/* reset dei messaggi di errore */
			ShowMessageEvent event2 = new ShowMessageEvent();
			event2.setMessageDropped(true);
			getEventBus().fireEvent(event2);
			// upload allegati selezionati
			UploadAllegatoPraticaAction action = new UploadAllegatoPraticaAction(event.getAllegati(), idPratica);
			dispatcher.execute(action, new AsyncCallback<UploadAllegatoPraticaResult>() {

				@Override
				public void onSuccess(UploadAllegatoPraticaResult result) {
					ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
					if (!result.getError()) {
						scrollDown = true;
						PecOutDTO bozza = (PecOutDTO) result.getPratica();
						pecInDb.update(result.getPratica().getClientID(), bozza, siteMapMenu.containsLink(idPratica));
						getEventBus().fireEvent(new BackFromPlaceEvent());
					} else {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

				}

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}
			});

		} else {
			getEventBus().fireEvent(new BackFromPlaceEvent());
		}
	}

	/* classi interne */

	/* upload di allegato già caricato su FS del server */
	public class UploadAllegatoCommand {

		public void onFileSelected(String fileName) {
			/* reset dei messaggi di errore */
			ShowMessageEvent event2 = new ShowMessageEvent();
			event2.setMessageDropped(true);
			getEventBus().fireEvent(event2);
			// prima di avviare l'upload eseguo il salvataggio
			salvaBozza(new AsyncCallback<SalvaBozzaInvioResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(SalvaBozzaInvioResult result) {
					getEventBus().fireEvent(new UploadEvent(idPratica, UploadStatus.START));
					DettaglioPecOutBozzaPresenter.this.getView().startUpload();
				}
			});
		}

		public void onFileUploaded(RispostaFileUploaderDTO dto) {
			/* reset dei messaggi di errore */
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);
			if (!dto.isError()) {
				UploadAllegatoPraticaAction action = new UploadAllegatoPraticaAction(dto.getTmpFiles(), DettaglioPecOutBozzaPresenter.this.idPratica);
				dispatcher.execute(action, new AsyncCallback<UploadAllegatoPraticaResult>() {

					@Override
					public void onFailure(Throwable caught) {
						getEventBus().fireEvent(new UploadEvent(idPratica, UploadStatus.ERROR));
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

					@Override
					public void onSuccess(UploadAllegatoPraticaResult result) {
						getEventBus().fireEvent(new UploadEvent(idPratica, UploadStatus.DONE));
						PecOutDTO bozza = (PecOutDTO) result.getPratica();
						getView().setBozzaPecOutDto(bozza);
						if (!result.getError()) {
							dropMessage();
							pecInDb.insertOrUpdate(idPratica, bozza, true);
							mostraDettaglioBozza(bozza);
						} else {
							getEventBus().fireEvent(new UploadEvent(idPratica, UploadStatus.ERROR));
							writeErrorMessage(result.getMessError());
						}
					}
				});
			} else {
				getEventBus().fireEvent(new UploadEvent(idPratica, UploadStatus.ERROR));
				writeErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
			}
		}
	}

	private class MostraDettaglioAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> {

		@Override
		public Object exe(AllegatoDTO allegato) {
			MostraDettaglioAllegatoEvent.fire(DettaglioPecOutBozzaPresenter.this, idPratica, allegato);
			return null;
		}

	}

	private class FirmaAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> {

		@Override
		public Object exe(final Set<AllegatoDTO> allegati) {
			/* reset dei messaggi di errore */
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);
			salvaBozza(new AsyncCallback<SalvaBozzaInvioResult>() {

				@Override
				public void onSuccess(SalvaBozzaInvioResult result) {
					Set<AllegatoDTO> allegati = getView().getAllegatiSelezionati();
					FirmaDigitaleInizioEvent event = new FirmaDigitaleInizioEvent(DettaglioPecOutBozzaPresenter.this, allegati);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onFailure(Throwable caught) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}
			});

			return null;
		}

	}

	private class CommandCancellaAllegato implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> {

		@Override
		public Object exe(Set<AllegatoDTO> allegati) {

			cancellaFile(allegati);

			return null;
		}

	}

	private class MostraCaricaAllegatiDaPraticaCommand implements Command {

		@Override
		public void execute() {
			/* reset dei messaggi di errore */
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);
			/* poi salvo la bozza e vado alla selezione degli allegati da importare */
			salvaBozza(new AsyncCallback<SalvaBozzaInvioResult>() {

				@Override
				public void onSuccess(SalvaBozzaInvioResult result) {
					Place place = new Place();
					place.setToken(NameTokens.caricaallegati);
					place.addParam(NameTokensParams.idPratica, idFascicolo); // fascicolo contenente l'email corrente
					place.addParam(NameTokensParams.idEmail, idPratica); // email corrente
					getEventBus().fireEvent(new GoToPlaceEvent(place));

				}

				@Override
				public void onFailure(Throwable caught) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}
			});

		}

	}

	private class DownloadAllegatoCommand implements it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> {

		@Override
		public Void exe(final AllegatoDTO allegato) {
			SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(allegato.getClientID(), allegato);
			getView().sendDownload(uri);
			return null;
		}
	}

	private class ChiudiBozzaCommand implements Command {

		@Override
		public void execute() {
			pecInDb.remove(idPratica);
			DettaglioPecOutBozzaPresenter.this.siteMapMenu.removeVoice(idPratica);
			getEventBus().fireEvent(new BackFromPlaceEvent(idPratica, true));
		}
	}

	private class ChiudiReinoltroCommand implements Command {

		@Override
		public void execute() {
			MostraDettaglioPecOutInviataEvent mostraDettaglioPecOutInviataEvent = new MostraDettaglioPecOutInviataEvent();
			mostraDettaglioPecOutInviataEvent.setIdPratica(idPratica);
			mostraDettaglioPecOutInviataEvent.setOnChiudiToken(NameTokens.worklistfascicolo);
			getEventBus().fireEvent(mostraDettaglioPecOutInviataEvent);
		}
	}

	@Override
	@ProxyEvent
	public void onChiudiDettaglioAllegato(ChiudiDettaglioAllegatoEvent event) {
		if (event.getClientID().equals(idPratica) && placeManager.getCurrentPlaceRequest().getNameToken().equals(NameTokens.dettagliopecout)) {
			ShowAppLoadingEvent.fire(this, false);
			placeManager.revealCurrentPlace();
		}
	}

	@Override
	@ProxyEvent
	public void onMostraDettaglioBozza(MostraDettaglioBozzaEvent event) {
		idPratica = event.getIdPratica();
		interoperabile = event.isInteroperabile();
		chiudiCommand = new ChiudiBozzaCommand();
		revealCommand = new RevealBozza();
		inviaCommand = new InviaBozzaCommand();
		revealInParent();
		if (isVisible()) {
			caricaPecOutBozza();
		}

	}

	private void dropMessage() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
	}

	private void writeErrorMessage(String errorMessage) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(errorMessage == null ? ConsolePecConstants.ERROR_MESSAGE : errorMessage);
		getEventBus().fireEvent(event);
	}

	@Override
	@ProxyEvent
	public void onMostraBozzaReinoltro(MostraBozzaReinoltroEvent event) {
		idPratica = event.getPecOutDto().getClientID();
		chiudiCommand = new ChiudiReinoltroCommand();
		revealCommand = new RevealReinoltra();
		inviaCommand = new InviaReinoltroCommand();
		revealInParent();
		caricaPecOutReinoltro(event.getPecOutDto());
	}

	private class InviaBozzaCommand implements Command {
		@Override
		public void execute() {
			boolean error = controllaForm();
			if (!error) {
				inviaMail();
			}
		}
	}

	private class InviaReinoltroCommand implements Command {
		@Override
		public void execute() {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);
			boolean error = controllaForm();
			if (!error) {
				// prendo dalla view i destinatari, che potrebbero essere diversi da quelli impostati precedentemente
				ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, true);
				ReinoltroAction reinoltroAction = new ReinoltroAction();
				for (Destinatario dest : getView().getNuoviDestinatari()) {
					reinoltroAction.getDestinatari().add(dest.getDestinatario());
				}
				for (Destinatario dest : getView().getNuoviDestinatariCC()) {
					reinoltroAction.getCopia().add(dest.getDestinatario());
				}
				reinoltroAction.setIdMailOriginale(pecOutDto.getClientID());

				dispatcher.execute(reinoltroAction, new AsyncCallback<ReinoltroResult>() {

					@Override
					public void onFailure(Throwable caught) {
						ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}

					@Override
					public void onSuccess(ReinoltroResult result) {
						ShowAppLoadingEvent.fire(DettaglioPecOutBozzaPresenter.this, false);
						if (result.isError()) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(result.getMessageError());
							getEventBus().fireEvent(event);
						} else {

							pecInDb.remove(pecOutDto.getClientID());

							PecOutDTO dto = result.getPecOutDTO();
							MainPresenter.Place p = new Place();
							p.setToken(NameTokens.dettagliopecout);
							p.addParam(NameTokensParams.idPratica, dto.getClientID());
							GoToPlaceEvent e = new GoToPlaceEvent(p);
							getEventBus().fireEvent(e);
						}
					}

				});
			}
		}
	}

	@Override
	public EventBus _getEventBus() {
		return getEventBus();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatcher;
	}

	@Override
	public PlaceManager getPlaceManager() {
		return placeManager;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return pecInDb;
	}

	@Override
	public String getFascicoloPath() {
		return idFascicolo;
	}

	@Override
	@ProxyEvent
	public void onBackToFascicolo(BackToFascicoloEvent event) {
		placeManager.revealCurrentPlace();
	}
}
