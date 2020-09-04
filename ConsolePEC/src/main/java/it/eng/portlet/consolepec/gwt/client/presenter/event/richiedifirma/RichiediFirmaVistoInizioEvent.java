package it.eng.portlet.consolepec.gwt.client.presenter.event.richiedifirma;

import it.eng.portlet.consolepec.gwt.client.event.OpeningEvent;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 *
 * @author biagiot
 *
 */
public class RichiediFirmaVistoInizioEvent extends GwtEvent<RichiediFirmaVistoInizioEvent.RichiediFirmaVistoInizioHandler> implements OpeningEvent{

	public static Type<RichiediFirmaVistoInizioHandler> TYPE = new Type<RichiediFirmaVistoInizioHandler>();
	private Object openingRequestor;
	private String assegnatario;
	private boolean allegatoProtocollato;

	public interface RichiediFirmaVistoInizioHandler extends EventHandler {
		void onRichiediFirmaFascicolo(RichiediFirmaVistoInizioEvent event);
	}

	public RichiediFirmaVistoInizioEvent(Object openingRequestor, String assegnatario, boolean allegatoProtocollato) {
		this.openingRequestor = openingRequestor;
		this.assegnatario = assegnatario;
		this.allegatoProtocollato = allegatoProtocollato;
	}

	@Override
	protected void dispatch(RichiediFirmaVistoInizioHandler handler) {
		handler.onRichiediFirmaFascicolo(this);
	}

	@Override
	public Type<RichiediFirmaVistoInizioHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RichiediFirmaVistoInizioHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Object openingRequestor, String assegnatario, boolean allegatoProtocollato) {
		source.fireEvent(new RichiediFirmaVistoInizioEvent(openingRequestor, assegnatario, allegatoProtocollato));
	}

	@Override
	public Object getOpeningRequestor() {
		return openingRequestor;
	}

	public String getAssegnatario() {
		return assegnatario;
	}

	public boolean isAllegatoProtocollato() {
		return allegatoProtocollato;
	}
}
