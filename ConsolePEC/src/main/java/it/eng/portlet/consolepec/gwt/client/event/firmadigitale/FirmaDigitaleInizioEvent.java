package it.eng.portlet.consolepec.gwt.client.event.firmadigitale;

import it.eng.portlet.consolepec.gwt.client.event.OpeningEvent;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 *
 * @author biagiot
 *
 */
public class FirmaDigitaleInizioEvent extends GwtEvent<FirmaDigitaleInizioEvent.FirmaDigitaleInizioHandler> implements OpeningEvent {

	public static Type<FirmaDigitaleInizioHandler> TYPE = new Type<FirmaDigitaleInizioEvent.FirmaDigitaleInizioHandler>();

	private Object openingRequestor;
	private Set<AllegatoDTO> allegati;

	public interface FirmaDigitaleInizioHandler extends EventHandler {
		void onFirmaDigitaleStart(FirmaDigitaleInizioEvent event);
	}

	public FirmaDigitaleInizioEvent (Object openingRequestor, Set<AllegatoDTO> allegati) {
		this.openingRequestor = openingRequestor;
		this.allegati = allegati;
	}

	@Override
	public Type<FirmaDigitaleInizioHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<FirmaDigitaleInizioHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(FirmaDigitaleInizioHandler handler) {
		handler.onFirmaDigitaleStart(this);
	}

	public static void fire(HasHandlers source, Object openingRequestor, Set<AllegatoDTO> allegati) {
		source.fireEvent(new FirmaDigitaleInizioEvent(openingRequestor, allegati));
	}

	public Set<AllegatoDTO> getAllegati() {
		return allegati;
	}

	@Override
	public Object getOpeningRequestor() {
		return openingRequestor;
	}
}
