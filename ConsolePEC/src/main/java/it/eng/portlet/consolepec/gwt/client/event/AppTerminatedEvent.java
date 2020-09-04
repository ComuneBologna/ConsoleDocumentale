package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Evento lanciato quando si notifica la terminazione dell'applicazione. Ciò implica che la sessione non è più utilizzabile
 * 
 * @author pluttero
 * 
 */
public class AppTerminatedEvent extends GwtEvent<AppTerminatedEvent.AppTerminatedHandler> {

	public static Type<AppTerminatedHandler> TYPE = new Type<AppTerminatedHandler>();
	private final HTMLPanel messagePanel;

	public interface AppTerminatedHandler extends EventHandler {
		void onAppTerminated(AppTerminatedEvent event);
	}

	public AppTerminatedEvent(HTMLPanel messagePanel) {
		this.messagePanel = messagePanel;
	}

	public HTMLPanel getMessagePanel() {
		return messagePanel;
	}

	@Override
	protected void dispatch(AppTerminatedHandler handler) {
		handler.onAppTerminated(this);
	}

	@Override
	public Type<AppTerminatedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AppTerminatedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, HTMLPanel messagePanel) {
		source.fireEvent(new AppTerminatedEvent(messagePanel));
	}
}
