package it.eng.portlet.consolepec.gwt.client.drive;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.GestioneDriveAbilitazione;
import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.cobo.consolepec.commons.drive.permessi.PermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.VisualizzaElementoPermessoDrive;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaCartellaEvent;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaCartellaEvent.AggiornaCartellaEventHandler;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaFileEvent;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaFileEvent.AggiornaFileEventHandler;
import it.eng.portlet.consolepec.gwt.client.drive.event.ApriCartellaEvent;
import it.eng.portlet.consolepec.gwt.client.drive.event.ApriCartellaEvent.ApriCartellaEventHandler;
import it.eng.portlet.consolepec.gwt.client.drive.event.CaricaFileEvent;
import it.eng.portlet.consolepec.gwt.client.drive.event.CreaCartellaEvent;
import it.eng.portlet.consolepec.gwt.client.drive.event.ScaricaFileEvent;
import it.eng.portlet.consolepec.gwt.client.drive.event.ScaricaFileEvent.ScaricaFileEventHandler;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMessageWidget;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.Base64Utils;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaCartellaAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaCartellaResult;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaFileAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaFileResult;
import it.eng.portlet.consolepec.gwt.shared.drive.action.ApriCartellaAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.ApriCartellaResult;
import it.eng.portlet.consolepec.gwt.shared.drive.action.RicercaDriveAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.RicercaDriveResult;
import it.eng.portlet.consolepec.spring.bean.gestionepratiche.IGestioneAllegati;
import lombok.Data;

/**
 * @author Giacomo F.M.
 * @since 2019-05-30
 */
public class DrivePresenter extends Presenter<DrivePresenter.MyView, DrivePresenter.MyProxy> implements ApriCartellaEventHandler, ScaricaFileEventHandler, AggiornaCartellaEventHandler, AggiornaFileEventHandler {

	public static final Integer LIMIT = 100;

	public static final String BACK = "..";
	public static final Cartella ROOT = new Cartella();
	{
		ROOT.setId(Cartella.PRE_ID + 0);
		ROOT.setNome(BACK);
	}

	private DrivePlace currentPlace = new DrivePlace();

	private DispatchAsync dispatcher;
	private PlaceManager placeManager;
	private SitemapMenu sitemapMenu;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@ProxyCodeSplit
	@NameToken(NameTokens.drive)
	public interface MyProxy extends ProxyPlace<DrivePresenter> {/**/}

	public interface MyView extends View {

		void bindDettaglioCartella(Command command);

		void bindCreaCartella(Command command);

		void bindUploadFile(Command command);

		void bindRicerca(Command command);

		String getTextRicerca();

		DriveMessageWidget getMessageWidget();

		DownloadAllegatoWidget getDownloadWidget();

		void apriCartella(EventBus eventBus, DrivePlace place, Cartella cartella, List<DriveElement> elements);

	}

	@Inject
	public DrivePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PlaceManager placeManager, final SitemapMenu sitemapMenu,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
		this.sitemapMenu = sitemapMenu;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		eventBus.addHandler(ApriCartellaEvent.TYPE, this);
		eventBus.addHandler(ScaricaFileEvent.TYPE, this);
		eventBus.addHandler(AggiornaCartellaEvent.TYPE, this);
		eventBus.addHandler(AggiornaFileEvent.TYPE, this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().bindRicerca(new Command() {
			@Override
			public void execute() {
				ricerca();
			}
		});
		getView().bindDettaglioCartella(new Command() {
			@Override
			public void execute() {
				Place place = new Place();
				place.setToken(NameTokens.drivedetail);
				place.addParam(NameTokensParams.id, currentPlace.getHistory().getLast().getId());
				getEventBus().fireEvent(new GoToPlaceEvent(place));
			}
		});
		getView().bindCreaCartella(new Command() {
			@Override
			public void execute() {
				getEventBus().fireEvent(new CreaCartellaEvent(currentPlace.getHistory().getLast().getId()));
			}
		});
		getView().bindUploadFile(new Command() {
			@Override
			public void execute() {
				getEventBus().fireEvent(new CaricaFileEvent(currentPlace.getHistory().getLast().getId()));
			}
		});
	}

	@Override
	protected void onReveal() {
		super.onReveal();

		if (!profilazioneUtenteHandler.isAbilitato(GestioneDriveAbilitazione.class)) {
			throw new IllegalArgumentException("Utente non abilitato al Drive");
		}

		sitemapMenu.setActiveVoce(VociRootSiteMap.DRIVE.getId());
		Integer page;
		try {
			page = Integer.parseInt(placeManager.getCurrentPlaceRequest().getParameter(NameTokensParams.page, "1"));
		} catch (Exception e) {
			GWT.log("Errore nel parsing della pagina della cartella", e);
			page = 1;
		}
		apriCartella(placeManager.getCurrentPlaceRequest().getParameter(NameTokensParams.id, ROOT.getId()), page);
	}

	@Override
	public void apriCartella(final String idCartella, final Integer page) {
		ShowAppLoadingEvent.fire(DrivePresenter.this, true);
		currentPlace.setPage(page);
		currentPlace.setRicerca(false);
		dispatcher.execute(new ApriCartellaAction(idCartella, currentPlace.getPage(), LIMIT), new AsyncCallback<ApriCartellaResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DrivePresenter.this, false);
				getView().getMessageWidget().danger("Errore nella ricerca della cartella. " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(ApriCartellaResult result) {
				ShowAppLoadingEvent.fire(DrivePresenter.this, false);
				if (result == null || result.isError()) {
					getView().getMessageWidget().warning("Errore nella ricerca della cartella: " + result == null ? "nessun risultato" : result.getMsgError());
				} else {
					storicizza(result.getCartella());
					if (!ROOT.getId().equals(result.getCartella().getId())) {
						result.getElement().addFirst(caricaCartellaPadre(result.getCartella()));
					}
					getView().apriCartella(getEventBus(), currentPlace, result.getCartella(), result.getElement());
				}
			}
		});
	}

	private static Cartella caricaCartellaPadre(Cartella cartella) {
		Cartella padre = new Cartella();
		padre.setId(cartella.getIdPadre());
		padre.setNome(BACK);
		return padre;
	}

	private void storicizza(final Cartella cartella) {
		if (ROOT.getId().equals(cartella.getId())) {
			currentPlace.getHistory().clear();
		}
		if (currentPlace.getHistory().contains(cartella)) {
			currentPlace.getHistory().subList(currentPlace.getHistory().indexOf(cartella), currentPlace.getHistory().size()).clear();
		}
		currentPlace.getHistory().add(cartella);
	}

	public void ricerca() {
		ShowAppLoadingEvent.fire(DrivePresenter.this, true);
		dispatcher.execute(new RicercaDriveAction(getView().getTextRicerca()), new AsyncCallback<RicercaDriveResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DrivePresenter.this, false);
				getView().getMessageWidget().danger("Errore durante la ricerca. " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(RicercaDriveResult result) {
				ShowAppLoadingEvent.fire(DrivePresenter.this, false);
				if (result.isError()) {
					getView().getMessageWidget().warning("Errore durante la ricerca. " + result.getMsgError());
				} else {
					currentPlace = new DrivePlace();
					currentPlace.setRicerca(true);
					getView().apriCartella(getEventBus(), currentPlace, ROOT, result.getElements());
				}
			}
		});
	}

	@Override
	public void scaricaFile(File file) {
		if (Iterables.any(file.getPermessi(), new Predicate<PermessoDrive>() {
			@Override
			public boolean apply(PermessoDrive input) {
				return VisualizzaElementoPermessoDrive.class.equals(input.getClass()) && profilazioneUtenteHandler.getDatiUtente().getRuoli().contains(input.getRuolo());
			}
		})) {
			// SafeUri uri = UriMapping.generaDownloadAllegatoServletURL(file.getIdAlfresco());
			String path = Base64Utils.URLencodeAlfrescoPath(file.getPathAlfresco().replace("/" + file.getNome(), ""));
			SafeUri uri = UriMapping.generaDownloadAllegatoVersionato(path, file.getNome(), IGestioneAllegati.FLOAT_PREFIX + file.getVersione());
			getView().getDownloadWidget().sendDownload(uri);
		} else {
			getView().getMessageWidget().warning("Utente non abilitato al download del file " + file.getNome());
		}
	}

	@Override
	public void aggiornaCartella(final Cartella cartella, final boolean reload) {
		ShowAppLoadingEvent.fire(DrivePresenter.this, true);
		dispatcher.execute(new AggiornaCartellaAction(cartella), new AsyncCallback<AggiornaCartellaResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DrivePresenter.this, false);
				getView().getMessageWidget().danger("Errore nella ricerca della cartella. " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(AggiornaCartellaResult result) {
				ShowAppLoadingEvent.fire(DrivePresenter.this, false);
				if (result == null || result.isError()) {
					getView().getMessageWidget().warning("Errore durante l'aggiornamento della cartella: " + result == null ? "nessun risultato" : result.getMsgError());
				} else {
					if (reload)
						apriCartella(result.getCartella().getIdPadre(), currentPlace.getPage());
					getView().getMessageWidget().success("Aggiornamento effettuato.");
				}
			}
		});
	}

	@Override
	public void aggiornaFile(final File file, final boolean reload) {
		ShowAppLoadingEvent.fire(DrivePresenter.this, true);
		dispatcher.execute(new AggiornaFileAction(file), new AsyncCallback<AggiornaFileResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DrivePresenter.this, false);
				getView().getMessageWidget().danger("Errore nella ricerca della cartella. " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(AggiornaFileResult result) {
				ShowAppLoadingEvent.fire(DrivePresenter.this, false);
				if (result == null || result.isError()) {
					getView().getMessageWidget().warning("Errore durante l'aggiornamento del file: " + result == null ? "nessun risultato" : result.getMsgError());
				} else {
					if (reload)
						apriCartella(result.getFile().getIdCartella(), currentPlace.getPage());
					getView().getMessageWidget().success("Aggiornamento effettuato.");
				}
			}
		});
	}

	@Data
	public static class DrivePlace {
		private Integer page = 1;
		private boolean ricerca = false;
		private LinkedList<Cartella> history = new LinkedList<>(Arrays.asList(ROOT));

		public void setPage(Integer page) {
			if (page < 1) {
				this.page = 1;
			} else {
				this.page = page;
			}
		}
	}

}
