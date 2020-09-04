package it.eng.portlet.consolepec.gwt.client.drive;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
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

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaCartellaEvent;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaCartellaEvent.AggiornaCartellaEventHandler;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaFileEvent;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaFileEvent.AggiornaFileEventHandler;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMessageWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMetadatiWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMetadatiWidget.ModificheDizionario;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveRuoliWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveRuoliWidget.ModificheRuoli;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.client.widget.SitemapMenu;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants.VociRootSiteMap;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaCartellaAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaCartellaResult;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaFileAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaFileResult;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaPermessiAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.AggiornaPermessiResult;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CercaElementoAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CercaElementoResult;
import it.eng.portlet.consolepec.gwt.shared.drive.action.EliminaElementoAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.EliminaElementoResult;

/**
 * @author Giacomo F.M.
 * @since 2019-06-19
 */
public class DriveDetailPresenter extends Presenter<DriveDetailPresenter.MyView, DriveDetailPresenter.MyProxy> implements AggiornaCartellaEventHandler, AggiornaFileEventHandler {

	@ProxyCodeSplit
	@NameToken(NameTokens.drivedetail)
	public interface MyProxy extends ProxyPlace<DriveDetailPresenter> {/**/}

	public interface MyView extends View {

		void bindSalvaCommand(Command command);

		void bindApriCommand(Command command);

		void bindAnnullaCommand(Command command);

		void bindEliminaCommand(Command command);

		DriveMessageWidget getMessageWidget();

		DriveRuoliWidget getRuoliWidget();

		DriveMetadatiWidget getMetadatiWidget();

		String getDescrizione();

		void clear(boolean clearMessage);

		void mostraErrore(String msgError);

		void dettaglioElemento(boolean clearMessage, DriveElement elemento);
	}

	private DriveElement elemento;

	private PlaceManager placeManager;
	private DispatchAsync dispatcher;
	private SitemapMenu sitemapMenu;

	@Inject
	public DriveDetailPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher, final PlaceManager placeManager, final SitemapMenu sitemapMenu) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
		this.placeManager = placeManager;
		this.sitemapMenu = sitemapMenu;

		eventBus.addHandler(AggiornaCartellaEvent.TYPE, this);
		eventBus.addHandler(AggiornaFileEvent.TYPE, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().bindSalvaCommand(new Command() {
			@Override
			public void execute() {
				boolean modDesc = elemento.getDescrizione() == null ? !"".equals(getView().getDescrizione()) : !elemento.getDescrizione().equals(getView().getDescrizione());
				ModificheRuoli modRuoli = getView().getRuoliWidget().getModifiche();
				ModificheDizionario modDizionario = getView().getMetadatiWidget().getModifiche();
				if (modDesc || modDizionario != null || modRuoli != null) {
					if (modDesc && modDizionario == null) {
						modificaDescrizione(getView().getDescrizione(), modRuoli == null);
					}
					modificaDizionario(modDizionario, modRuoli == null);
					modificaRuoli(modRuoli);
				} else {
					getView().getMessageWidget().warning("Nessuna modifica apportata");
				}
			}
		});
		getView().bindApriCommand(new Command() {
			@Override
			public void execute() {
				Place place = new Place();
				place.setToken(NameTokens.drive);
				if (elemento.isCartella()) {
					place.addParam(NameTokensParams.id, elemento.getId());
				} else {
					place.addParam(NameTokensParams.id, ((File) elemento).getIdCartella());
				}
				getEventBus().fireEvent(new GoToPlaceEvent(place));
			}
		});
		getView().bindAnnullaCommand(new Command() {
			@Override
			public void execute() {
				apriPadre();
			}
		});
		getView().bindEliminaCommand(new Command() {
			@Override
			public void execute() {
				eliminaElemento();
			}
		});
	}

	private void apriPadre() {
		Place place = new Place();
		place.setToken(NameTokens.drive);
		if (elemento.isCartella()) {
			place.addParam(NameTokensParams.id, ((Cartella) elemento).getIdPadre());
		} else {
			place.addParam(NameTokensParams.id, ((File) elemento).getIdCartella());
		}
		getEventBus().fireEvent(new GoToPlaceEvent(place));
	}

	private void modificaDescrizione(String descrizione, boolean reload) {
		if (elemento.isCartella()) {
			Cartella cartella = new Cartella();
			cartella.setId(elemento.getId());
			cartella.setDescrizione(descrizione);
			getEventBus().fireEvent(new AggiornaCartellaEvent(cartella, reload));
		} else {
			File file = new File();
			file.setId(elemento.getId());
			file.setDescrizione(descrizione);
			getEventBus().fireEvent(new AggiornaFileEvent(file, reload));
		}
	}

	private void modificaDizionario(ModificheDizionario mod, boolean reload) {
		if (mod != null) {
			if (elemento.isCartella()) {
				Cartella cartella = new Cartella();
				cartella.setId(elemento.getId());
				cartella.setDescrizione(getView().getDescrizione());
				cartella.setDizionario(mod.getDizionario().getNome());
				cartella.getMetadati().addAll(mod.getMetadati());
				getEventBus().fireEvent(new AggiornaCartellaEvent(cartella, reload));
			} else {
				File file = new File();
				file.setId(elemento.getId());
				file.setDescrizione(getView().getDescrizione());
				file.setDizionario(mod.getDizionario().getNome());
				file.getMetadati().addAll(mod.getMetadati());
				getEventBus().fireEvent(new AggiornaFileEvent(file, reload));
			}
		}
	}

	private void modificaRuoli(ModificheRuoli mod) {
		if (mod != null) {
			ShowAppLoadingEvent.fire(DriveDetailPresenter.this, true);
			dispatcher.execute(new AggiornaPermessiAction(elemento.getId(), mod.isRicorsivo(), mod.getAggiunti(), mod.getRimossi()), new AsyncCallback<AggiornaPermessiResult>() {
				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DriveDetailPresenter.this, false);
					getView().getMessageWidget().danger("Errore durante l'aggiornamento dei permessi. " + caught.getLocalizedMessage());
				}

				@Override
				public void onSuccess(AggiornaPermessiResult result) {
					ShowAppLoadingEvent.fire(DriveDetailPresenter.this, false);
					if (result == null || result.isError()) {
						getView().getMessageWidget().warning("Errore durante l'aggiornamento del file: " + result == null ? "nessun risultato" : result.getMsgError());
					} else {
						getView().getMessageWidget().success("Aggiornamento file effettuato.");
					}
					loadElement(elemento.getId(), false);
				}
			});
		}
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onHide() {
		super.onHide();
		getView().clear(true);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
		sitemapMenu.setActiveVoce(VociRootSiteMap.DRIVE.getId());
		loadElement(placeManager.getCurrentPlaceRequest().getParameter(NameTokensParams.id, null), true);
	}

	private void loadElement(final String id, final boolean clearMessage) {
		ShowAppLoadingEvent.fire(DriveDetailPresenter.this, true);
		dispatcher.execute(new CercaElementoAction(id), new AsyncCallback<CercaElementoResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				ShowAppLoadingEvent.fire(DriveDetailPresenter.this, false);
				getView().mostraErrore("Errore nella ricerca dell'elemento. " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(final CercaElementoResult result) {
				ShowAppLoadingEvent.fire(DriveDetailPresenter.this, false);
				if (result.isError()) {
					getView().mostraErrore(result.getMsgError());
				} else {
					getView().dettaglioElemento(clearMessage, result.getElemento());
					elemento = result.getElemento();
				}
			}
		});
	}

	@Override
	public void aggiornaCartella(final Cartella cartella, final boolean reload) {
		ShowAppLoadingEvent.fire(DriveDetailPresenter.this, true);
		dispatcher.execute(new AggiornaCartellaAction(cartella), new AsyncCallback<AggiornaCartellaResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DriveDetailPresenter.this, false);
				getView().getMessageWidget().danger("Errore durante l'aggiornamento della cartella. " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(AggiornaCartellaResult result) {
				ShowAppLoadingEvent.fire(DriveDetailPresenter.this, false);
				if (result == null || result.isError()) {
					getView().getMessageWidget().warning("Errore durante l'aggiornamento della cartella: " + result == null ? "nessun risultato" : result.getMsgError());
				} else {
					if (reload)
						loadElement(result.getCartella().getId(), false);
					getView().getMessageWidget().success("Aggiornamento cartella effettuato.");
				}
			}
		});
	}

	@Override
	public void aggiornaFile(final File file, final boolean reload) {
		ShowAppLoadingEvent.fire(DriveDetailPresenter.this, true);
		dispatcher.execute(new AggiornaFileAction(file), new AsyncCallback<AggiornaFileResult>() {

			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DriveDetailPresenter.this, false);
				getView().getMessageWidget().danger("Errore durante l'aggiornamento del file. " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(AggiornaFileResult result) {
				ShowAppLoadingEvent.fire(DriveDetailPresenter.this, false);
				if (result == null || result.isError()) {
					getView().getMessageWidget().warning("Errore durante l'aggiornamento del file: " + result == null ? "nessun risultato" : result.getMsgError());
				} else {
					if (reload)
						loadElement(result.getFile().getId(), false);
					getView().getMessageWidget().success("Aggiornamento file effettuato.");
				}
			}
		});
	}

	private void eliminaElemento() {
		ShowAppLoadingEvent.fire(DriveDetailPresenter.this, true);
		dispatcher.execute(new EliminaElementoAction(elemento), new AsyncCallback<EliminaElementoResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(DriveDetailPresenter.this, false);
				getView().getMessageWidget().danger("Errore durante la cancellazione dell'elemento.");
			}

			@Override
			public void onSuccess(EliminaElementoResult result) {
				ShowAppLoadingEvent.fire(DriveDetailPresenter.this, false);
				if (result.isError()) {
					getView().getMessageWidget().warning(result.getMsgError());
				} else {
					getView().getMessageWidget().success("Cancellazione avvenuta correttamente, a breve sarai reindirizzato alla cartella padre." //
							+ " (In caso contrario premi sul pulsante Annulla)");
					Timer t = new Timer() {
						@Override
						public void run() {
							apriPadre();
						}
					};
					t.schedule(DriveMessageWidget.TIMER_TIME_INFO);
				}
			}
		});
	}

}
