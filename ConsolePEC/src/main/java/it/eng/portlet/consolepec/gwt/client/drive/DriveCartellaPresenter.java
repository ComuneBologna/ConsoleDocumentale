package it.eng.portlet.consolepec.gwt.client.drive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.ProxyEvent;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.Nomenclatura;
import it.eng.portlet.consolepec.gwt.client.drive.event.CreaCartellaEvent;
import it.eng.portlet.consolepec.gwt.client.drive.event.CreaCartellaEvent.CreaCartellaEventHandler;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMessageWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMetadatiWidget.ModificheDizionario;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.event.ShowAppLoadingEvent;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CercaElementoAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CercaElementoResult;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CreaCartellaAction;
import it.eng.portlet.consolepec.gwt.shared.drive.action.CreaCartellaResult;

/**
 * @author Giacomo F.M.
 * @since 2019-06-24
 */
public class DriveCartellaPresenter extends Presenter<DriveCartellaPresenter.MyView, DriveCartellaPresenter.MyProxy> implements CreaCartellaEventHandler {

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<DriveCartellaPresenter> {/**/}

	public interface MyView extends View {

		DriveMessageWidget getMessageWidget();

		void init(Cartella cartellaPadre, Command saveCommand, Command undoCommand);

		String getNomeCartella();

		Nomenclatura getNomenclatura();

		String getRuolo();

		ModificheDizionario getModifiche();

	}

	private DispatchAsync dispatcher;

	@Inject
	public DriveCartellaPresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	@ProxyEvent
	public void onCreaCartella(final CreaCartellaEvent creaCartellaEvent) {
		revealInParent();
		ShowAppLoadingEvent.fire(DriveCartellaPresenter.this, true);
		dispatcher.execute(new CercaElementoAction(creaCartellaEvent.getIdCartellaPadre()), new AsyncCallback<CercaElementoResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				ShowAppLoadingEvent.fire(DriveCartellaPresenter.this, false);
				GWT.log("Qualcosa e' andato davvero male.", caught);

				Place place = new Place();
				place.setToken(NameTokens.drive);
				place.addParam(NameTokensParams.id, creaCartellaEvent.getIdCartellaPadre());
				getEventBus().fireEvent(new GoToPlaceEvent(place));
			}

			@Override
			public void onSuccess(final CercaElementoResult result) {
				ShowAppLoadingEvent.fire(DriveCartellaPresenter.this, false);
				if (result.isError()) {
					getView().getMessageWidget().danger(result.getMsgError());
				} else {
					getView().init((Cartella) result.getElemento(), //
							new Command() {
								@Override
								public void execute() {
									save(result.getElemento().getId());
								}
							}, //
							new Command() {
								@Override
								public void execute() {
									undo(result.getElemento().getId());
								}
							});
				}
			}
		});
	}

	public void save(final String idPadre) {
		ShowAppLoadingEvent.fire(DriveCartellaPresenter.this, true);
		Cartella cartella = new Cartella();
		cartella.setIdPadre(idPadre);
		ModificheDizionario mod = getView().getModifiche();
		if (mod != null) {
			cartella.setDizionario(mod.getDizionario().getNome());
			cartella.getMetadati().addAll(mod.getMetadati());
		}

		if (getView().getNomenclatura() != null && !getView().getNomenclatura().getNomenclatura().contains(getView().getNomeCartella())) {
			ShowAppLoadingEvent.fire(DriveCartellaPresenter.this, false);
			getView().getMessageWidget().danger("Il nome della cartella non appartiene alla nomenclatura selezionata.");

		} else {
			cartella.setNome(getView().getNomeCartella());

			if (getView().getNomenclatura() != null)
				cartella.setNomenclatura(getView().getNomenclatura().getNome());

			dispatcher.execute(new CreaCartellaAction(cartella, getView().getRuolo()), new AsyncCallback<CreaCartellaResult>() {
				@Override
				public void onFailure(Throwable caught) {
					ShowAppLoadingEvent.fire(DriveCartellaPresenter.this, false);
					getView().getMessageWidget().danger("Grave errore nel salvataggio della cartella.");
				}

				@Override
				public void onSuccess(CreaCartellaResult result) {
					ShowAppLoadingEvent.fire(DriveCartellaPresenter.this, false);
					if (result.isError()) {
						getView().getMessageWidget().danger(result.getMsgError());
					} else {
						Place place = new Place();
						place.setToken(NameTokens.drivedetail);
						place.addParam(NameTokensParams.id, result.getCartella().getId());
						getEventBus().fireEvent(new GoToPlaceEvent(place));
					}
				}
			});
		}
	}

	public void undo(final String idPadre) {
		Place place = new Place();
		place.setToken(NameTokens.drive);
		place.addParam(NameTokensParams.id, idPadre);
		getEventBus().fireEvent(new GoToPlaceEvent(place));
	}

}
