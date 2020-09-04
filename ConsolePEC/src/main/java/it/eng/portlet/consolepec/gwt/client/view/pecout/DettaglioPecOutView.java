package it.eng.portlet.consolepec.gwt.client.view.pecout;

import it.eng.portlet.consolepec.gwt.client.presenter.pecout.DettaglioPecOutPresenter;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class DettaglioPecOutView extends ViewImpl
	implements DettaglioPecOutPresenter.MyView {

	private final Widget widget;
	@UiField
	FlowPanel mainContentPanel;

	public interface Binder extends UiBinder<Widget, DettaglioPecOutView> {
		//
	}

	@Inject
	public DettaglioPecOutView(final Binder binder) {
	
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
	
		return widget;
	}
	@Override
	public void setInSlot(Object slot, IsWidget content) {
		if (slot == DettaglioPecOutPresenter.TYPE_SetDettaglio){
			mainContentPanel.clear();
			if (content != null){
				mainContentPanel.add(content);
			}
		}
	}
}
