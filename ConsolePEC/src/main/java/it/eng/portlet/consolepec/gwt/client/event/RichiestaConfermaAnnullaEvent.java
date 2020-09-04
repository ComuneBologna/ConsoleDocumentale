package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class RichiestaConfermaAnnullaEvent extends GwtEvent<RichiestaConfermaAnnullaEvent.RichiestaConfermaAnnullaHandler> {

	public static Type<RichiestaConfermaAnnullaHandler> TYPE = new Type<RichiestaConfermaAnnullaHandler>();

	private String message;
	private String eventId;

	public interface RichiestaConfermaAnnullaHandler extends EventHandler {
		void onRichiestaConfermaAnnulla(RichiestaConfermaAnnullaEvent event);
	}

	public interface RichiestaConfermaAnnullaHasHandlers extends HasHandlers {
		HandlerRegistration addRichiestaConfermaAnnullaHandler(RichiestaConfermaAnnullaHandler handler);
	}

	public RichiestaConfermaAnnullaEvent(String message, String eventId) {
		this.message = message;
		this.eventId = eventId;
	}

	@Override
	protected void dispatch(RichiestaConfermaAnnullaHandler handler) {
		handler.onRichiestaConfermaAnnulla(this);
	}

	@Override
	public Type<RichiestaConfermaAnnullaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RichiestaConfermaAnnullaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String message, String eventId) {
		source.fireEvent(new RichiestaConfermaAnnullaEvent(message, eventId));
	}

	public String getMessage() {
		return message;
	}

	public String getEventId() {
		return eventId;
	}

}
