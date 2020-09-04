package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ChiudiDettaglioAllegatoEvent extends GwtEvent<ChiudiDettaglioAllegatoEvent.ChiudiDettaglioAllegatoHandler> {

	public static Type<ChiudiDettaglioAllegatoHandler> TYPE = new Type<ChiudiDettaglioAllegatoHandler>();
	private String clientID;

	public interface ChiudiDettaglioAllegatoHandler extends EventHandler {
		void onChiudiDettaglioAllegato(ChiudiDettaglioAllegatoEvent event);
	}

	public ChiudiDettaglioAllegatoEvent(String clientID) {
		this.clientID = clientID;
	}

	public String getClientID() {
		return clientID;
	}

	@Override
	protected void dispatch(ChiudiDettaglioAllegatoHandler handler) {
		handler.onChiudiDettaglioAllegato(this);
	}

	@Override
	public Type<ChiudiDettaglioAllegatoHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ChiudiDettaglioAllegatoHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String clientID) {
		source.fireEvent(new ChiudiDettaglioAllegatoEvent(clientID));
	}
}
