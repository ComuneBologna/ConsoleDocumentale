package it.eng.portlet.consolepec.gwt.client.presenter.event;

import it.eng.portlet.consolepec.gwt.client.event.OpeningEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class GoToSelezionaAnagrafica extends GwtEvent<GoToSelezionaAnagrafica.GoToHandler> implements OpeningEvent {

	public static Type<GoToHandler> TYPE = new Type<GoToHandler>();

	public interface GoToHandler extends EventHandler {
		void onGoTo(GoToSelezionaAnagrafica event);
	}

	private Object openingRequestor;
	
	public GoToSelezionaAnagrafica(Object openingRequestor) {
		this.openingRequestor = openingRequestor;
	}

	@Override
	protected void dispatch(GoToHandler handler) {
		handler.onGoTo(this);
	}

	@Override
	public Type<GoToHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Object openingRequestor) {
		source.fireEvent(new GoToSelezionaAnagrafica(openingRequestor));
	}

	@Override
	public Object getOpeningRequestor() {
		return openingRequestor;
	}
}
