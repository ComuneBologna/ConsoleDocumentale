package it.eng.portlet.consolepec.gwt.client.presenter.urbanistica;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
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

import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.BackToFascicoloEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica.MostraListaPraticaProcediEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica.MostraListaPraticaProcediEvent.MostraListaPraticaProcedHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.BackToFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.IBackToFascicolo;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.CollegaPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.CollegaPraticaProcediResult;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.RicercaPraticaProcediAction;
import it.eng.portlet.consolepec.gwt.shared.action.urbanistica.RicercaPraticaProcediResult;

/**
 * @author GiacomoFM
 * @since 07/nov/2017
 */
public class ListaPraticaProcediPresenter extends Presenter<ListaPraticaProcediPresenter.MyView, ListaPraticaProcediPresenter.MyProxy> implements IBackToFascicolo, MostraListaPraticaProcedHandler {

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<ListaPraticaProcediPresenter> {
		//
	}

	public interface MyView extends View {
		Map<String, Object> getFiltriRicerca();

		PraticaProcedi getSelectedPraticaProcedi();

		void setRicercaCommand(final Command command);

		void resetFiltriRicerca();

		void setAnnullaCommand(final Command command);

		void setConfermaCommand(final Command command);

		void mostraListaPraticaProcedi(List<PraticaProcedi> listaPraticaProcedi, Integer start, Integer maxSize);

		void setDataProvider(PraticaProcediDataProvider dataProvider);

		Map<String, Boolean> getOrdinamento();
	}

	private DispatchAsync dispatchAsync;
	private PlaceManager placeManager;
	private PecInPraticheDB praticheDB;
	private String pathFascicolo;

	@Inject
	public ListaPraticaProcediPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatchAsync, final PlaceManager placeManager,
			final PecInPraticheDB praticheDB) {
		super(eventBus, view, proxy);
		this.dispatchAsync = dispatchAsync;
		this.placeManager = placeManager;
		this.praticheDB = praticheDB;
	}

	@Override
	public EventBus _getEventBus() {
		return getEventBus();
	}

	@Override
	public DispatchAsync getDispatchAsync() {
		return dispatchAsync;
	}

	@Override
	public PlaceManager getPlaceManager() {
		return placeManager;
	}

	@Override
	public PecInPraticheDB getPecInPraticheDB() {
		return praticheDB;
	}

	@Override
	public String getFascicoloPath() {
		return pathFascicolo;
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().setAnnullaCommand(new BackToFascicoloCommand<ListaPraticaProcediPresenter>(this));
		getView().setConfermaCommand(new CollegaPraticaProcediCommand());
		getView().setRicercaCommand(new Command() {
			@Override
			public void execute() {
				ricerca(getView().getFiltriRicerca(), getView().getOrdinamento(), ConsolePecConstants.MAX_NUMERO_RIGHE_TESTO_LUNGO, 0);
			}
		});
		getView().setDataProvider(new PraticaProcediDataProvider());
	}

	private class CollegaPraticaProcediCommand implements Command {
		@Override
		public void execute() {
			ShowAppLoadingEvent.fire(ListaPraticaProcediPresenter.this, true);
			dispatchAsync.execute(new CollegaPraticaProcediAction(pathFascicolo, getView().getSelectedPraticaProcedi()), new AsyncCallback<CollegaPraticaProcediResult>() {
				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(ListaPraticaProcediPresenter.this, false);
					fireErrorMessageEvent(ConsolePecConstants.ERROR_MESSAGE);
				}

				@Override
				public void onSuccess(CollegaPraticaProcediResult result) {
					ShowAppLoadingEvent.fire(ListaPraticaProcediPresenter.this, false);
					if (result.isError()) {
						fireErrorMessageEvent(result.getMsgError());
					} else {
						getEventBus().fireEvent(new BackToFascicoloEvent(pathFascicolo));
					}
				}
			});
		}
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
	@ProxyEvent
	public void onMostraListaPraticaProced(MostraListaPraticaProcediEvent event) {
		if (event.getNameClass().equals(ListaPraticaProcediPresenter.class.getName())) {

			revealInParent();
			if (!Strings.isNullOrEmpty(event.getPathFascicolo())) {
				pathFascicolo = event.getPathFascicolo();
			}
			if (event.getFiltri() != null) {
				ricerca(event.getFiltri(), event.getOrdinamento(), ConsolePecConstants.MAX_NUMERO_RIGHE_TESTO_LUNGO, 0);
			}
		}
	}

	public void ricerca(final Map<String, Object> filtri, final Map<String, Boolean> ordinamento, final Integer limit, final Integer offset) {
		ShowAppLoadingEvent.fire(ListaPraticaProcediPresenter.this, true);
		dispatchAsync.execute(new RicercaPraticaProcediAction(filtri, ordinamento, limit, offset), new AsyncCallback<RicercaPraticaProcediResult>() {
			@Override
			public void onFailure(Throwable caught) {
				ShowAppLoadingEvent.fire(ListaPraticaProcediPresenter.this, false);
				fireErrorMessageEvent(ConsolePecConstants.ERROR_MESSAGE);
			}

			@Override
			public void onSuccess(RicercaPraticaProcediResult result) {
				ShowAppLoadingEvent.fire(ListaPraticaProcediPresenter.this, false);
				if (result.isError()) {
					fireErrorMessageEvent(result.getMsgError());
				} else {
					getView().mostraListaPraticaProcedi(result.getListaPraticaProcedi(), offset, (int) result.getMaxResult());
				}
			}
		});
	}

	private void fireErrorMessageEvent(String msg) {
		ShowMessageEvent event = new ShowMessageEvent();
		event.setErrorMessage(msg);
		getEventBus().fireEvent(event);
	}

	public class PraticaProcediDataProvider extends AsyncDataProvider<PraticaProcedi> {

		@Override
		protected void onRangeChanged(HasData<PraticaProcedi> display) {
			int start = display.getVisibleRange().getStart();
			int length = display.getVisibleRange().getLength();
			ricerca(getView().getFiltriRicerca(), getView().getOrdinamento(), length, start);
		}
	}

}
