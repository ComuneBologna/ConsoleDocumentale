package it.eng.portlet.consolepec.gwt.client.event.collegamento;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ChiudiCollegaFascicoloDirettoEvent extends GwtEvent<ChiudiCollegaFascicoloDirettoEvent.ChiudiCollegaFascicoloDirettoHandler> {

	public static Type<ChiudiCollegaFascicoloDirettoHandler> TYPE = new Type<ChiudiCollegaFascicoloDirettoHandler>();

	public interface ChiudiCollegaFascicoloDirettoHandler extends EventHandler {
		void onChiudiCollegaFascicoloDiretto(ChiudiCollegaFascicoloDirettoEvent event);
	}
	
	public ChiudiCollegaFascicoloDirettoEvent() {
	}

	@Override
	protected void dispatch(ChiudiCollegaFascicoloDirettoHandler handler) {
		handler.onChiudiCollegaFascicoloDiretto(this);
	}

	@Override
	public Type<ChiudiCollegaFascicoloDirettoHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ChiudiCollegaFascicoloDirettoHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ChiudiCollegaFascicoloDirettoEvent());
	}

}
