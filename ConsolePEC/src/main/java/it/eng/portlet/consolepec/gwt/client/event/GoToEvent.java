package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class GoToEvent extends GwtEvent<GoToEvent.GoToHandler> {

	public static Type<GoToHandler> TYPE = new Type<GoToHandler>();

	public interface GoToHandler extends EventHandler {
		void onGoTo(GoToEvent event);
	}

	Object openingRequestor;
	
	public GoToEvent(Object openingRequestor) {
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
		source.fireEvent(new GoToEvent(openingRequestor));
	}

	public Object getOpeningRequestor() {
		return openingRequestor;
	}
}
