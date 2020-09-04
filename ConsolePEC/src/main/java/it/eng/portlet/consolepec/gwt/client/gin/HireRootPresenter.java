package it.eng.portlet.consolepec.gwt.client.gin;

import it.eng.portlet.consolepec.gwt.client.listener.SessionTimeoutListener;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.RootPresenter;

/**
 * This RootPresenter should insert GWTP ui into div with id= gwt_ui
 * 
 * 
 * @author Konstantin Zolotarev <konstantin.zolotarev@gmail.com>
 * 
 */
public class HireRootPresenter extends RootPresenter {

	public static final class HireRootView extends RootView {
		@Override
		public void setInSlot(Object slot, IsWidget widget) {
			RootPanel.get("gwtdiv").add(widget);
		}
	}

	@Inject
	public HireRootPresenter(EventBus eventBus, HireRootView view, SessionTimeoutListener list) {
		super(eventBus, view);
	}

}
