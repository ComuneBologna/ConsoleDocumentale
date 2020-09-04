package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class BackToFascicoloEvent extends GwtEvent<BackToFascicoloEvent.BackToFascicoloHandler> {

	public static Type<BackToFascicoloHandler> TYPE = new Type<BackToFascicoloHandler>();
	private String fascicoloID;

	public interface BackToFascicoloHandler extends EventHandler {
		void onBackToFascicolo(BackToFascicoloEvent event);
	}

	public BackToFascicoloEvent(String fascicoloID) {
		this.fascicoloID = fascicoloID;
	}

	public String getFascicoloID() {
		return fascicoloID;
	}

	@Override
	protected void dispatch(BackToFascicoloHandler handler) {
		handler.onBackToFascicolo(this);
	}

	@Override
	public Type<BackToFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<BackToFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String fascicoloID) {
		source.fireEvent(new BackToFascicoloEvent(fascicoloID));
	}
}
