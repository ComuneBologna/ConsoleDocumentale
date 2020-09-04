package it.eng.portlet.consolepec.gwt.client.presenter.event.richiedifirma;

import it.eng.portlet.consolepec.gwt.client.event.ClosingEvent;
import it.eng.portlet.consolepec.gwt.shared.model.richiedifirma.RichiediFirmaVistoDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 *
 * @author biagiot
 *
 */
public class RichiediFirmaVistoFineEvent extends GwtEvent<RichiediFirmaVistoFineEvent.RichiediFirmaVistoFineHandler> implements ClosingEvent {

	public static Type<RichiediFirmaVistoFineHandler> TYPE = new Type<RichiediFirmaVistoFineHandler>();

	private Object openingRequestor;
	private RichiediFirmaVistoDTO richiediFirmaVisto;
	private boolean isAnnulla;

	public interface RichiediFirmaVistoFineHandler extends EventHandler {
		void onRichiediFirmaVistoFine(RichiediFirmaVistoFineEvent event);
	}

	public RichiediFirmaVistoFineEvent(RichiediFirmaVistoDTO richiediFirmaVisto, Object openingRequestor) {
		this.richiediFirmaVisto = richiediFirmaVisto;
		this.openingRequestor = openingRequestor;
		this.isAnnulla = false;
	}

	public RichiediFirmaVistoFineEvent(Object openingRequestor) {
		this.openingRequestor = openingRequestor;
		this.isAnnulla = true;
	}

	@Override
	protected void dispatch(RichiediFirmaVistoFineHandler handler) {
		handler.onRichiediFirmaVistoFine(this);
	}

	@Override
	public Type<RichiediFirmaVistoFineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RichiediFirmaVistoFineHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, RichiediFirmaVistoDTO richiediFirmaVisto, Object openingRequestor) {
		source.fireEvent(new RichiediFirmaVistoFineEvent(richiediFirmaVisto, openingRequestor));
	}

	public static void fire(HasHandlers source, Object openingRequestor) {
		source.fireEvent(new RichiediFirmaVistoFineEvent(openingRequestor));
	}

	@Override
	public Object getOpeningRequestor() {
		return openingRequestor;
	}

	public RichiediFirmaVistoDTO getRichiediFirmaVisto() {
		return richiediFirmaVisto;
	}

	public boolean isAnnulla() {
		return isAnnulla;
	}
}
