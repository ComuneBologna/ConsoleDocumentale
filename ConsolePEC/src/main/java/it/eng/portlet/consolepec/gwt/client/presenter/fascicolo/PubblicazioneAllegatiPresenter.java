package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaEmaiInlLoaded;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.ChiudiPubblicazioneAllegatiEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraPubblicazioneAllegatiEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.MostraPubblicazioneAllegatiEvent.MostraPubblicazioneAllegatiHandler;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecUtils;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.PubblicazioneAllegati;
import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.PubblicazioneAllegatiResult;
import it.eng.portlet.consolepec.gwt.shared.action.pec.TipoRiferimentoPEC;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.datepicker.client.CalendarUtil;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

public class PubblicazioneAllegatiPresenter extends Presenter<PubblicazioneAllegatiPresenter.MyView, PubblicazioneAllegatiPresenter.MyProxy> implements MostraPubblicazioneAllegatiHandler {

	private PecInPraticheDB praticheDB;
	private DispatchAsync dispatcher;
	private SitemapMenu siteMapMenu;

	private String fascicoloPath;
	private String nomeAllegato;
	private String url;
	private boolean isAllegatoPubblicato;
	
	private ConfigurazioniHandler configurazioniHandler;

	public interface MyView extends View {

		public void setAnnullaCommand(com.google.gwt.user.client.Command command);

		public void setPubblicaCommand(com.google.gwt.user.client.Command command);

		public Date getDataInizio();

		public Date getDataFine();

		public void popolaGUI(String url, Date dataInizio, Date dataFine, String confermaLabel);

		public void abilitaConfermaPubblicazione(boolean abilita);
		
		public void impostaConfermaPubblicazione(String confermaLabel);
		
		public void abilitaDestinatariEmail(boolean abilita);
		
		public void abilitaTestoEmail(boolean abilita);

		public void setSuggestOracleDestinatari(SuggestOracle suggestOracle, Command command);

		public void setDataFromValueChangeHandler(ValueChangeHandler<Date> handler);

		public void setDataToValueChangeHandler(ValueChangeHandler<Date> handler);

		public List<String> getDestinatariEmail();

		public String getTestoEmail();

		void abilitaDataInizio(boolean abilita);

	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<PubblicazioneAllegatiPresenter> {
	}

	@Inject
	public PubblicazioneAllegatiPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final PecInPraticheDB pecInDb, final DispatchAsync dispatcher, final SitemapMenu siteMap, ConfigurazioniHandler configurazioniHandler) {
		super(eventBus, view, proxy);
		this.praticheDB = pecInDb;
		this.dispatcher = dispatcher;
		this.siteMapMenu = siteMap;
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
		ShowAppLoadingEvent.fire(PubblicazioneAllegatiPresenter.this, true);
		praticheDB.getFascicoloByPath(fascicoloPath, true, new PraticaFascicoloLoaded() {

			@Override
			public void onPraticaLoaded(FascicoloDTO fascicolo) {
				ShowAppLoadingEvent.fire(PubblicazioneAllegatiPresenter.this, false);
				for (AllegatoDTO allegato : fascicolo.getAllegati())
					if (allegato.getNome().equals(nomeAllegato)) {
						try {
							isAllegatoPubblicato = allegato.isPubblicato();
							//url = AllegatiUtils.encodeURL(fascicoloPath, nomeAllegato);
							url = configurazioniHandler.getBaseURLPubblicazioneAllegato() + UriMapping.generaDownloadPubblicoServletContextPath() + UriMapping.encodeDownloadPubblicoPathInfo(fascicoloPath, nomeAllegato);
							getView().setSuggestOracleDestinatari(getSuggestBox(fascicolo), new Command() {
								@Override
								public void execute() {
									getView().abilitaTestoEmail(isInvioPossibile());
									getView().impostaConfermaPubblicazione(getConfermaLabel());
								}
							});
							if (isAllegatoPubblicato) {
								getView().popolaGUI(url, allegato.getDataInizioPubblicazione(), allegato.getDataFinePubblicazione(), getConfermaLabel());
							} else {
								getView().popolaGUI(url, new Date(), null, getConfermaLabel());
							}
							getView().abilitaConfermaPubblicazione(isAllegatoPubblicato);
							getView().abilitaDestinatariEmail(false);
							getView().abilitaTestoEmail(false);
							getView().abilitaDataInizio(!isAllegatoPubblicato);
						} catch (Exception e) {
							ShowMessageEvent event = new ShowMessageEvent();
							event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
							getEventBus().fireEvent(event);
						}

					}
			}

			@Override
			public void onPraticaError(String error) {
				ShowAppLoadingEvent.fire(PubblicazioneAllegatiPresenter.this, false);
				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
				getEventBus().fireEvent(event);
			}
		});
	}

	@Override
	public void onHide() {
		super.onHide();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		getEventBus().fireEvent(event);
	}

	@Override
	protected void onBind() {
		super.onBind();
		DateValueChangeHandler handler = new DateValueChangeHandler();
		getView().setDataFromValueChangeHandler(handler);
		getView().setDataToValueChangeHandler(handler);
		getView().setAnnullaCommand(new com.google.gwt.user.client.Command() {

			@Override
			public void execute() {
				getEventBus().fireEvent(new ChiudiPubblicazioneAllegatiEvent(fascicoloPath));
			}
		});
		getView().setPubblicaCommand(new PubblicaAllegatiCommand());

	}

	@Override
	@ProxyEvent
	public void onMostraPubblicazioneAllegati(final MostraPubblicazioneAllegatiEvent event) {
		this.fascicoloPath = event.getIdFascicolo();
		this.nomeAllegato = event.getNomeAllegato();
		revealInParent();
	}

	public class PubblicaAllegatiCommand implements Command {

		@Override
		public void execute() {
			ShowMessageEvent event = new ShowMessageEvent();
			event.setMessageDropped(true);
			getEventBus().fireEvent(event);
			Date inizio = getView().getDataInizio();
			Date fine = getView().getDataFine();
			CalendarUtil.resetTime(inizio);
			CalendarUtil.resetTime(fine);
			String testoEmail = null;
			List<String> destinatariEmail = null;
			if(isInvioPossibile()){
				testoEmail = getView().getTestoEmail() + "\n\n" + ConsolePecUtils.getTestoPubblicazioneAllegato(url, inizio, fine);
				destinatariEmail = getView().getDestinatariEmail();
			}
			PubblicazioneAllegati action;
			action = new PubblicazioneAllegati(fascicoloPath, nomeAllegato, inizio, fine, destinatariEmail, testoEmail);
			ShowAppLoadingEvent.fire(PubblicazioneAllegatiPresenter.this, true);
			dispatcher.execute(action, new AsyncCallback<PubblicazioneAllegatiResult>() {

				@Override
				public void onFailure(Throwable arg0) {
					ShowAppLoadingEvent.fire(PubblicazioneAllegatiPresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(PubblicazioneAllegatiResult result) {
					ShowAppLoadingEvent.fire(PubblicazioneAllegatiPresenter.this, false);
					if (result.isError()) {
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(result.getErrorMsg());
						getEventBus().fireEvent(event);
					} else {
						// rimuovo la pratica dalla cache locale per farla ricaricare dopo le modifiche
						praticheDB.remove(result.getFascicolo().getClientID());
						getEventBus().fireEvent(new ChiudiPubblicazioneAllegatiEvent(result.getFascicolo().getClientID()));
					}
				}

			});
		}

		
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
						praticheDB.getPecInByPath(path, siteMapMenu.containsLink(path), new PraticaEmaiInlLoaded() {

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
		SpacebarSuggestOracle oracle = new SpacebarSuggestOracle(mittenti);
		return oracle;
	}

	public class DateValueChangeHandler implements ValueChangeHandler<Date> {

		@Override
		public void onValueChange(ValueChangeEvent<Date> paramValueChangeEvent) {
			if(isPeriodoDiPubblicazioneValido()){
				getView().abilitaConfermaPubblicazione(true);
				getView().abilitaDestinatariEmail(true);
			} else {
				getView().abilitaConfermaPubblicazione(false);
				getView().abilitaDestinatariEmail(false);
				getView().abilitaTestoEmail(false);
			
			}
			getView().impostaConfermaPubblicazione(getConfermaLabel());
		}
	}
	
	private boolean isInvioPossibile() {
		return isPeriodoDiPubblicazioneValido() && getView().getDestinatariEmail().size() > 0;
	}
	
	private boolean isPeriodoDiPubblicazioneValido(){
		Date inizio = getView().getDataInizio();
		Date fine = getView().getDataFine();
		Date oggi = new Date();
		if (inizio != null && fine != null) {
			CalendarUtil.resetTime(inizio);
			CalendarUtil.resetTime(fine);
			CalendarUtil.resetTime(oggi);
			boolean abilitazione;
			if(isAllegatoPubblicato)
				abilitazione = (fine.equals(oggi) || fine.after(oggi));
			else
				abilitazione = (inizio.equals(oggi) || inizio.after(oggi)) && (fine.equals(inizio) || fine.after(inizio));
			return abilitazione;
		} else {
			return false;
		}
	}
	
	private String getConfermaLabel(){
		if(isAllegatoPubblicato){
			return (isInvioPossibile() ? ConsolePecConstants.MODIFICA_PUBBLICAZIONE_INVIA : ConsolePecConstants.MODIFICA_PUBBLICAZIONE);
		} else {
			return (isInvioPossibile() ? ConsolePecConstants.NUOVA_PUBBLICAZIONE_INVIA : ConsolePecConstants.NUOVA_PUBBLICAZIONE);
		}
	}

}
