package it.eng.portlet.consolepec.gwt.client.presenter.rubrica;

import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
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

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.GestioneRubricaAbilitazione;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.rubrica.FiltriRicerca;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraCreaAnagraficaEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.MostraListaAnagraficheEvent.MostraListaAnagraficheHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaFineEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaInizioEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica.SelezionaAnagraficaInizioEvent.SelezionaAnagraficaInizioHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.rubrica.command.DettaglioAnagraficaCommand;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.RicercaAnagrafiche;
import it.eng.portlet.consolepec.gwt.shared.action.rubrica.RicercaAnagraficheResult;

/**
 * @author GiacomoFM
 * @since 15/set/2017
 */
public class ListaAnagrafichePresenter extends Presenter<ListaAnagrafichePresenter.MyView, ListaAnagrafichePresenter.MyProxy> implements MostraListaAnagraficheHandler, SelezionaAnagraficaInizioHandler {

	@ProxyCodeSplit
	@NameToken(NameTokens.listaanagrafiche)
	public interface MyProxy extends ProxyPlace<ListaAnagrafichePresenter> {
		//
	}

	public interface MyView extends View {
		void resetFiltriRicerca();

		void setRicercaCommand(final Command command);

		void setCreaCommand(CreaCommand command);

		void setDettaglioAnagraficaCommand(final DettaglioAnagraficaCommand command);

		void mostraAnagrafiche(final List<Anagrafica> anagrafiche);

		String getFiltroIdentificativo();

		String getFiltroDenominazione();

		String getFiltroStatoDocumentale();

		void mostraColonnaSelezione(boolean b);

		void setAvantiCommand(AvantiCommand avantiCommand);

		void setAnnullaCommand(AnnullaCommand annullaCommand);

		Anagrafica getAnagrafica();

	}

	private final DispatchAsync dispatcher;
	private final SitemapMenu sitemapMenu;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public ListaAnagrafichePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final SitemapMenu sitemapMenu,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		this.sitemapMenu = sitemapMenu;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		sitemapMenu.setActiveVoce(VociRootSiteMap.LISTA_ANAGRAFICHE.getId());
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	@ProxyEvent
	public void onMostraListaAnagrafiche(MostraListaAnagraficheEvent event) {
		if (event.getOpeningRequestor() == null || this.equals(event.getOpeningRequestor())) {
			getView().mostraColonnaSelezione(false);
			revealInParent();
		}
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setDettaglioAnagraficaCommand(new DettaglioAnagraficaCommand(this));
		getView().setRicercaCommand(new Command() {
			@Override
			public void execute() {
				ricerca();
			}
		});
	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().resetFiltriRicerca();
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		revealInParent();
		ricerca();
		sitemapMenu.setActiveVoce(VociRootSiteMap.LISTA_ANAGRAFICHE.getId());
		Window.scrollTo(0, 0);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);

		if (!profilazioneUtenteHandler.isAbilitato(GestioneRubricaAbilitazione.class)) {
			throw new IllegalArgumentException("Utente non abilitato");

		} else {
			getView().resetFiltriRicerca();
			getView().mostraColonnaSelezione(false);
			ricerca();
		}
	}

	private void ricerca() {
		ShowAppLoadingEvent.fire(ListaAnagrafichePresenter.this, true);
		RicercaAnagrafiche ricercaAnagrafiche = new RicercaAnagrafiche();

		ricercaAnagrafiche.getFiltri().put(FiltriRicerca.CODFISCALE_PIVA, getView().getFiltroIdentificativo());
		ricercaAnagrafiche.getFiltri().put(FiltriRicerca.DENOMINAZIONE, getView().getFiltroDenominazione());
		ricercaAnagrafiche.getFiltri().put(FiltriRicerca.STATO_DOCUMENTALE, getView().getFiltroStatoDocumentale());

		dispatcher.execute(ricercaAnagrafiche, new AsyncCallback<RicercaAnagraficheResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(ListaAnagrafichePresenter.this, false);
				fireErrorMessageEvent(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(RicercaAnagraficheResult result) {
				ShowAppLoadingEvent.fire(ListaAnagrafichePresenter.this, false);
				if (result.isError()) {
					fireErrorMessageEvent(result.getMsgError());
				} else {
					getView().mostraAnagrafiche(result.getAnagrafiche());
				}
			}
		});
	}

	private void fireErrorMessageEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

	@Override
	@ProxyEvent
	public void onMostraListaAnagraficheWidget(SelezionaAnagraficaInizioEvent event) {
		getView().mostraColonnaSelezione(true);
		AvantiCommand avantiCommand = new AvantiCommand(event.getOpeningRequestor());
		avantiCommand.setNomeDatoAggiuntivo(event.getNomeDatoAggiuntivo());
		AnnullaCommand annullaCommand = new AnnullaCommand(event.getOpeningRequestor());
		annullaCommand.setNomeDatoAggiuntivo(event.getNomeDatoAggiuntivo());
		getView().setAvantiCommand(avantiCommand);
		getView().setAnnullaCommand(annullaCommand);

		CreaCommand creaCommand = new CreaCommand(event.getOpeningRequestor(), event.getNomeDatoAggiuntivo());
		getView().setCreaCommand(creaCommand);

		revealInParent();
	}

	public class CreaCommand implements Command {
		Object openingRequestor;
		String nomeDatoAggiuntivo;

		CreaCommand(Object openingRequestor, String nomeDatoAggiuntivo) {
			this.openingRequestor = openingRequestor;
			this.nomeDatoAggiuntivo = nomeDatoAggiuntivo;
		}

		@Override
		public void execute() {
			MostraCreaAnagraficaEvent event = new MostraCreaAnagraficaEvent(openingRequestor, nomeDatoAggiuntivo);
			getEventBus().fireEvent(event);
		}
	}

	public class AvantiCommand implements Command {

		Object openingRequestor;

		String nomeDatoAggiuntivo;

		AvantiCommand(Object openingRequestor) {
			this.openingRequestor = openingRequestor;
		}

		@Override
		public void execute() {
			SelezionaAnagraficaFineEvent event = new SelezionaAnagraficaFineEvent(openingRequestor, getView().getAnagrafica());
			event.setNomeDatoAggiuntivo(nomeDatoAggiuntivo);
			getEventBus().fireEvent(event);
		}

		public void setNomeDatoAggiuntivo(String nomeDatoAggiuntivo) {
			this.nomeDatoAggiuntivo = nomeDatoAggiuntivo;
		}

	}

	public class AnnullaCommand implements Command {

		Object openingRequestor;

		String nomeDatoAggiuntivo;

		AnnullaCommand(Object openingRequestor) {
			this.openingRequestor = openingRequestor;
		}

		public void setNomeDatoAggiuntivo(String nomeDatoAggiuntivo) {
			this.nomeDatoAggiuntivo = nomeDatoAggiuntivo;
		}

		@Override
		public void execute() {
			// getEventBus().fireEvent(new SelezionaAnagraficaFineEvent(openingRequestor));
			SelezionaAnagraficaFineEvent event = new SelezionaAnagraficaFineEvent(openingRequestor);
			event.setNomeDatoAggiuntivo(nomeDatoAggiuntivo);
			getEventBus().fireEvent(event);
		}
	}

}
