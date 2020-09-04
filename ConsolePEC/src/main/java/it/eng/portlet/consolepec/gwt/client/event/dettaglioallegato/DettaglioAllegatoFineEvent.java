package it.eng.portlet.consolepec.gwt.client.event.dettaglioallegato;

import it.eng.portlet.consolepec.gwt.client.event.ClosingEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class DettaglioAllegatoFineEvent extends GwtEvent<DettaglioAllegatoFineEvent.DettaglioAllegatoFineHandler> implements ClosingEvent{

	public static Type<DettaglioAllegatoFineHandler> TYPE = new Type<DettaglioAllegatoFineHandler>();

	private Object openingRequestor;

	public interface DettaglioAllegatoFineHandler extends EventHandler {
		void onEndDettaglioAllegato(DettaglioAllegatoFineEvent event);
	}

	public DettaglioAllegatoFineEvent(Object openingRequestor) {
		this.openingRequestor = openingRequestor;
	}

	@Override
	protected void dispatch(DettaglioAllegatoFineHandler handler) {
		handler.onEndDettaglioAllegato(this);
	}

	@Override
	public Type<DettaglioAllegatoFineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DettaglioAllegatoFineHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Object openingRequestor) {
		source.fireEvent(new DettaglioAllegatoFineEvent(openingRequestor));
	}

	@Override
	public Object getOpeningRequestor() {
		return openingRequestor;
	}
}
