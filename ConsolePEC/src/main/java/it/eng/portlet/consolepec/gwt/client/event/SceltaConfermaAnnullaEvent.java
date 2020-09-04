package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class SceltaConfermaAnnullaEvent extends GwtEvent<SceltaConfermaAnnullaEvent.SceltaConfermaAnnullaHandler> {

	public static Type<SceltaConfermaAnnullaHandler> TYPE = new Type<SceltaConfermaAnnullaHandler>();

	private boolean confermato;
	private String eventId;

	public interface SceltaConfermaAnnullaHandler extends EventHandler {
		void onSceltaConfermaAnnulla(SceltaConfermaAnnullaEvent event);
	}

	public interface SceltaConfermaAnnullaHasHandlers extends HasHandlers {
		HandlerRegistration addSceltaConfermaAnnullaHandler(SceltaConfermaAnnullaHandler handler);
	}

	public SceltaConfermaAnnullaEvent(boolean confermato, String eventId) {
		this.confermato = confermato;
		this.eventId = eventId;
	}

	@Override
	protected void dispatch(SceltaConfermaAnnullaHandler handler) {
		handler.onSceltaConfermaAnnulla(this);
	}

	@Override
	public Type<SceltaConfermaAnnullaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SceltaConfermaAnnullaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, boolean confermato, String eventId) {
		source.fireEvent(new SceltaConfermaAnnullaEvent(confermato, eventId));
	}

	public boolean isConfermato() {
		return confermato;
	}

	public String getEventId() {
		return eventId;
	}
}