package it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.shared.proxy.PlaceRequest;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneComunicazioneAbilitazione;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.BackToCreaComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.BackToCreaComunicazioneEvent.BackToCreaComunicazioneHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.BackToDettaglioComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraCreaComunicazioneDaDettaglioComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraCreaComunicazioneDaDettaglioComunicazioneEvent.MostraCreaComunicazioneDaDettaglioComunicazioneHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraCreaComunicazioneDaDettaglioTemplateEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraCreaComunicazioneDaDettaglioTemplateEvent.MostraCreaComunicazioneDaDettaglioTemplateHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event.MostraSceltaTemplateDaCreaComunicazioneEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.template.event.BackToDettaglioTemplateEvent;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.CreaComunicazioneAction;
import it.eng.portlet.consolepec.gwt.shared.action.inviomassivo.CreaComunicazioneActionResult;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;

public class CreaComunicazionePresenter extends Presenter<CreaComunicazionePresenter.MyView, CreaComunicazionePresenter.MyProxy> implements BackToCreaComunicazioneHandler, MostraCreaComunicazioneDaDettaglioTemplateHandler, MostraCreaComunicazioneDaDettaglioComunicazioneHandler {

	private final EventBus eventBus;
	private final DispatchAsync dispatcher;
	private final PecInPraticheDB praticheDB;
	private final SitemapMenu siteMapMenu;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	public interface MyView extends View {

		public void svuotaCampi();

		public void setDataCreazionePratica(Date date);

		public void setUtente(String utente);

		public void init();

		public void hideButtonAnnulla();

		public void showButtonAnnulla();

		public void setAnnullaCommand(Command annullaCommand);

		public void setAvantiCommand(Command avantCommand);

		public void setGruppiSuggestBox(List<AnagraficaRuolo> ruoli);

		public void setAvantiEnabled(boolean b);

		public ComunicazioneDTO getComunicazione();

		public void setComunicazione(ComunicazioneDTO comunicazione);

		public boolean controlloCampi(List<String> errori);

	}

	@ProxyCodeSplit
	@NameToken(NameTokens.creacomunicazione)
	public interface MyProxy extends ProxyPlace<CreaComunicazionePresenter> {}

	@Inject
	public CreaComunicazionePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, PecInPraticheDB db, final SitemapMenu siteMapMenu,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {

		super(eventBus, view, proxy);
		this.eventBus = eventBus;
		this.dispatcher = dispatcher;
		this.praticheDB = db;
		this.siteMapMenu = siteMapMenu;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

	}

	@Override
	protected void revealInParent() {

		if (profilazioneUtenteHandler.isAbilitato(CreazioneComunicazioneAbilitazione.class)) {
			RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);

		} else {
			throw new IllegalArgumentException("Utente non abilitato alla creazione di comunicazioni");
		}

	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().init();

	}

	@Override
	protected void onHide() {
		super.onHide();
		dropMessage();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);

		siteMapMenu.setActiveVoce(VociRootSiteMap.CREA_COMUNICAZIONE.getId());

		mostraFormCreaFascicolo(profilazioneUtenteHandler.getDatiUtente().getNomeCompleto());
		getView().setGruppiSuggestBox(profilazioneUtenteHandler.getAnagraficheRuoliAbilitati(CreazioneComunicazioneAbilitazione.class));
		ShowAppLoadingEvent.fire(CreaComunicazionePresenter.this, false);

		revealInParent();
	}

	protected void mostraFormCreaFascicolo(String utente) {
		getView().setUtente(utente);
		getView().setDataCreazionePratica(new Date());
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);

	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		getView().setAnnullaCommand(new AnnullaDaMenuCommand());
		getView().setAvantiCommand(new AvantiDaMenuCommand());

		getView().svuotaCampi();
		getView().hideButtonAnnulla();
	}

	private void dropMessage() {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setMessageDropped(true);
		eventBus.fireEvent(event);
	}

	private class AvantiDaMenuCommand implements Command {

		@Override
		public void execute() {

			if (!controllaCampi()) {
				// nessun errore
				ComunicazioneDTO c = getView().getComunicazione();
				eventBus.fireEvent(new MostraSceltaTemplateDaCreaComunicazioneEvent(c));
			}

		}

	}

	private class AnnullaDaMenuCommand implements Command {

		@Override
		public void execute() {
			// do nothing
		}

	}

	@Override
	@ProxyEvent
	public void onBackToCreaComunicazione(BackToCreaComunicazioneEvent event) {

		revealInParent();
	}

	@Override
	@ProxyEvent
	public void onMostraCreaComunicazioneDaDettaglioTemplate(MostraCreaComunicazioneDaDettaglioTemplateEvent event) {

		getView().setAnnullaCommand(new AnnullaDaDettaglioTemplateCommand());
		getView().setAvantiCommand(new AvantiDaDettaglioTemplateCommand(event.getTemplate()));

		getView().svuotaCampi();
		getView().showButtonAnnulla();

		revealInParent();

	}

	@Override
	@ProxyEvent
	public void onMostraCreaComunicazioneDaDettaglioComunicazione(MostraCreaComunicazioneDaDettaglioComunicazioneEvent event) {
		getView().setAnnullaCommand(new AnnullaDaDettaglioComunicazioneCommand());
		getView().setAvantiCommand(new AvantiDaDettaglioComunicazioneCommand());

		getView().showButtonAnnulla();
		getView().setComunicazione(event.getComunicazione());

		revealInParent();
	}

	private class AnnullaDaDettaglioTemplateCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new BackToDettaglioTemplateEvent());
		}

	}

	private class AvantiDaDettaglioTemplateCommand implements Command {

		private TemplateDTO template;

		public AvantiDaDettaglioTemplateCommand(TemplateDTO template) {
			super();
			this.template = template;
		}

		@Override
		public void execute() {

			if (controllaCampi()) {
				return; // errore
			}

			ComunicazioneDTO comunicazione = getView().getComunicazione();
			comunicazione.setIdDocumentaleTemplate(template.getNumeroRepertorio());

			CreaComunicazioneAction creaComunicazioneAction = new CreaComunicazioneAction(comunicazione);

			dispatcher.execute(creaComunicazioneAction, new AsyncCallback<CreaComunicazioneActionResult>() {

				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(CreaComunicazionePresenter.this, false);
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					getEventBus().fireEvent(event);
				}

				@Override
				public void onSuccess(CreaComunicazioneActionResult result) {
					if (!result.getError()) {

						praticheDB.insertOrUpdate(result.getComunicazione().getClientID(), result.getComunicazione(), siteMapMenu.containsLink(result.getComunicazione().getClientID()));

						Place place = new Place();
						place.setToken(NameTokens.dettagliocomunicazione);
						place.addParam(NameTokensParams.idPratica, result.getComunicazione().getClientID());

						getEventBus().fireEvent(new GoToPlaceEvent(place));
						ShowAppLoadingEvent.fire(CreaComunicazionePresenter.this, false);

					} else {
						ShowAppLoadingEvent.fire(CreaComunicazionePresenter.this, false);
						ShowMessageEvent event = new ShowMessageEvent();
						event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
						getEventBus().fireEvent(event);
					}
				}
			});
		}

	}

	private boolean controllaCampi() {
		List<String> errori = new ArrayList<String>();
		if (getView().controlloCampi(errori) == false) {

			if (!errori.isEmpty()) {
				StringBuilder errore = new StringBuilder();
				errore.append("<br/>");
				for (String e : errori) {
					errore.append(e + "<br/>");
				}

				ShowMessageEvent event = new ShowMessageEvent();
				event.setErrorMessage(errore);
				eventBus.fireEvent(event);

			}

			return true; // errore
		} else {
			return false; // nessun errore
		}
	}

	private class AvantiDaDettaglioComunicazioneCommand implements Command {

		@Override
		public void execute() {

			if (!controllaCampi()) {
				// nessun errore
				ComunicazioneDTO c = getView().getComunicazione();
				eventBus.fireEvent(new MostraSceltaTemplateDaCreaComunicazioneEvent(c));
			}

		}

	}

	private class AnnullaDaDettaglioComunicazioneCommand implements Command {

		@Override
		public void execute() {
			eventBus.fireEvent(new BackToDettaglioComunicazioneEvent());
		}

	}

}
