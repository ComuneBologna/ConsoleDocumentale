package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Evento lanciato alla notifica di sessione scaduta
 * 
 * @author pluttero
 * 
 */
public class SessionTimeoutEvent extends GwtEvent<SessionTimeoutEvent.SessionTimeoutHandler> {

	public static Type<SessionTimeoutHandler> TYPE = new Type<SessionTimeoutHandler>();

	public interface SessionTimeoutHandler extends EventHandler {
		void onSessionTimeout(SessionTimeoutEvent event);
	}

	public SessionTimeoutEvent() {
	}

	@Override
	protected void dispatch(SessionTimeoutHandler handler) {
		handler.onSessionTimeout(this);
	}

	@Override
	public Type<SessionTimeoutHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SessionTimeoutHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new SessionTimeoutEvent());
	}
}
