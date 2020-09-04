package it.eng.portlet.consolepec.gwt.client.event.cartellafirma;

import lombok.NoArgsConstructor;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * 
 * @author biagiot
 *
 */
@NoArgsConstructor
public class NotificaTaskFirmaInizioEvent extends GwtEvent<NotificaTaskFirmaInizioEvent.NotificaTaskFirmaInizioHandler> {

	public static Type<NotificaTaskFirmaInizioHandler> TYPE = new Type<NotificaTaskFirmaInizioHandler>();

	public interface NotificaTaskFirmaInizioHandler extends EventHandler {
		void onNotificaTaskFirmaInizio(NotificaTaskFirmaInizioEvent event);
	}
	
	@Override
	protected void dispatch(NotificaTaskFirmaInizioHandler handler) {
		handler.onNotificaTaskFirmaInizio(this);
	}

	@Override
	public Type<NotificaTaskFirmaInizioHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<NotificaTaskFirmaInizioHandler> getType() {
		return TYPE;
	}
	
	public static void fire(HasHandlers source) {
		source.fireEvent(new NotificaTaskFirmaInizioEvent());
	}
}
