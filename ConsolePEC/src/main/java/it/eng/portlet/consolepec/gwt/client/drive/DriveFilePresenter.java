package it.eng.portlet.consolepec.gwt.client.drive;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
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
import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.portlet.consolepec.gwt.client.drive.event.CaricaFileEvent;
import it.eng.portlet.consolepec.gwt.client.drive.event.CaricaFileEvent.CaricaFileEventHandler;
import it.eng.portlet.consolepec.gwt.client.drive.util.JavaScriptFile;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveFileUploadWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveFileUploadWidget.SubmitInfo;
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

/**
 * @author Giacomo F.M.
 * @since 2019-06-28
 */
public class DriveFilePresenter extends Presenter<DriveFilePresenter.MyView, DriveFilePresenter.MyProxy> implements CaricaFileEventHandler, DriveFileUploadWidget.UploadHandler {

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<DriveFilePresenter> {/**/}

	public interface MyView extends View {

		DriveMessageWidget getMessageWidget();

		DriveFileUploadWidget getUploadWidget();

		void init(Cartella cartellaPadre, Command uploadCommand, Command undoCommand);

		String getRuolo();

		ModificheDizionario getModifiche();

	}

	private File file;

	private DispatchAsync dispatcher;

	@Inject
	public DriveFilePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy, final DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher = dispatcher;
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getUploadWidget().setUploadHandler(this);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	@ProxyEvent
	public void onCaricaFile(final CaricaFileEvent creaCartellaEvent) {
		revealInParent();
		ShowAppLoadingEvent.fire(DriveFilePresenter.this, true);
		file = new File();
		dispatcher.execute(new CercaElementoAction(creaCartellaEvent.getIdCartella()), new AsyncCallback<CercaElementoResult>() {
			@Override
			public void onFailure(final Throwable caught) {
				ShowAppLoadingEvent.fire(DriveFilePresenter.this, false);
				GWT.log("Qualcosa e' andato davvero male.", caught);

				Place place = new Place();
				place.setToken(NameTokens.drive);
				place.addParam(NameTokensParams.id, creaCartellaEvent.getIdCartella());
				getEventBus().fireEvent(new GoToPlaceEvent(place));
			}

			@Override
			public void onSuccess(final CercaElementoResult result) {
				ShowAppLoadingEvent.fire(DriveFilePresenter.this, false);
				if (result.isError()) {
					getView().getMessageWidget().danger(result.getMsgError());
				} else {
					file.setIdCartella(result.getElemento().getId());
					ModificheDizionario mod = getView().getModifiche();
					if (mod != null) {
						file.setDizionario(mod.getDizionario().getNome());
						file.getMetadati().addAll(mod.getMetadati());
					}
					getView().init((Cartella) result.getElemento(), //
							new Command() {
								@Override
								public void execute() {
									getView().getUploadWidget().submit();
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

	public void undo(final String idCartella) {
		Place place = new Place();
		place.setToken(NameTokens.drive);
		place.addParam(NameTokensParams.id, idCartella);
		getEventBus().fireEvent(new GoToPlaceEvent(place));
	}

	@Override
	public SubmitInfo onSubmit() {
		ShowAppLoadingEvent.fire(DriveFilePresenter.this, true);
		SubmitInfo si = new SubmitInfo();

		JavaScriptFile jsf = JavaScriptFile.build(file);
		si.setMetadati(new JSONObject(jsf).toString());

		si.setRuolo(getView().getRuolo());

		return si;
	}

	@Override
	public void onSubmitComplete(final boolean ok, final String message) {
		ShowAppLoadingEvent.fire(DriveFilePresenter.this, false);
		if (ok) {
			getView().getMessageWidget().success(message);

			Place place = new Place();
			place.setToken(NameTokens.drive);
			place.addParam(NameTokensParams.id, file.getIdCartella());
			getEventBus().fireEvent(new GoToPlaceEvent(place));

		} else {
			getView().getMessageWidget().danger(message);
		}
		ShowAppLoadingEvent.fire(DriveFilePresenter.this, false);
	}

}
