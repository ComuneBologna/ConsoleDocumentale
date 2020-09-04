package it.eng.portlet.consolepec.gwt.client.event.confermaprotocollazione;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class ConfermaProtocollazionePecOutEvent extends GwtEvent<ConfermaProtocollazionePecOutEvent.ConfermaProtocollazionePecOutHandler> {

	public static Type<ConfermaProtocollazionePecOutHandler> TYPE = new Type<ConfermaProtocollazionePecOutHandler>();

	private String idFascicolo;
	private String idEmailOut;

	public interface ConfermaProtocollazionePecOutHandler extends EventHandler {
		void onConfermaProtocollazionePecOut(ConfermaProtocollazionePecOutEvent event);
	}

	public interface ConfermaProtocollazionePecOutHasHandlers extends HasHandlers {
		HandlerRegistration addConfermaProtocollazionePecOutHandler(ConfermaProtocollazionePecOutHandler handler);
	}

	public ConfermaProtocollazionePecOutEvent() {
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public void setIdFascicolo(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public String getIdEmailOut() {
		return idEmailOut;
	}

	public void setIdEmailOut(String idEmailOut) {
		this.idEmailOut = idEmailOut;
	}

	@Override
	protected void dispatch(ConfermaProtocollazionePecOutHandler handler) {
		handler.onConfermaProtocollazionePecOut(this);
	}

	@Override
	public Type<ConfermaProtocollazionePecOutHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ConfermaProtocollazionePecOutHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ConfermaProtocollazionePecOutEvent());
	}
}
