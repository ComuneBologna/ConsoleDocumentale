package it.eng.portlet.consolepec.gwt.client.view;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent;
import it.eng.portlet.consolepec.gwt.client.widget.UploadMonitorWidget;

public class MainView extends ViewImpl implements MainPresenter.MyView {

	private final Widget widget;

	@UiField
	FlowPanel mainContentPanel;
	PopupPanel popUp = new PopupPanel();
	@UiField
	HTMLPanel alertPanel;
	@UiField
	UploadMonitorWidget uploadProgress;

	public interface Binder extends UiBinder<Widget, MainView> {/**/}

	@Inject
	public MainView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == MainPresenter.TYPE_SetMainContent) {
			mainContentPanel.clear();
			if (content != null)
				mainContentPanel.add(content);
		} else if (slot == MainPresenter.TYPE_SetSplashScreenContent) {
			popUp.clear();
			if (content != null) {
				popUp.add(content);
				popUp.center();
				popUp.show();
			}
		} else if (slot == MainPresenter.TYPE_SetMessageContent) {
			alertPanel.clear();
			if (content != null) {
				alertPanel.add(content);
			}
		} else {
			super.setInSlot(slot, content);
		}

		setPlaceHolder();

	}

	@Override
	public void forwardUploadEvent(UploadEvent event) {
		uploadProgress.setUploadStatus(event);
	}

	public static native void setPlaceHolder() /*-{
		$wnd.intplaceholder2();
	}-*/;

}
