package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class TerminaAttesaEvent extends GwtEvent<TerminaAttesaEvent.TerminaAttesaHandler> {

	public static Type<TerminaAttesaHandler> TYPE = new Type<TerminaAttesaHandler>();

	public interface TerminaAttesaHandler extends EventHandler {
		void onTerminaAttesa(TerminaAttesaEvent event);
	}

	public interface TerminaAttesaHasHandlers extends HasHandlers {
		HandlerRegistration addTerminaAttesaHandler(TerminaAttesaHandler handler);
	}

	public TerminaAttesaEvent() {
	}

	@Override
	protected void dispatch(TerminaAttesaHandler handler) {
		handler.onTerminaAttesa(this);
	}

	@Override
	public Type<TerminaAttesaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<TerminaAttesaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new TerminaAttesaEvent());
	}
}
