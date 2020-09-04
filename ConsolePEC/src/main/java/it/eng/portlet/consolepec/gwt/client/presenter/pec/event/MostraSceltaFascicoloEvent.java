package it.eng.portlet.consolepec.gwt.client.presenter.pec.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MostraSceltaFascicoloEvent extends GwtEvent<MostraSceltaFascicoloEvent.MostraSceltaFascicoloHandler> {

	public static Type<MostraSceltaFascicoloHandler> TYPE = new Type<MostraSceltaFascicoloHandler>();

	public interface MostraSceltaFascicoloHandler extends EventHandler {
		void onMostraSceltaFascicolo(MostraSceltaFascicoloEvent event);
	}

	public MostraSceltaFascicoloEvent() {}

	@Override
	protected void dispatch(MostraSceltaFascicoloHandler handler) {
		handler.onMostraSceltaFascicolo(this);
	}

	@Override
	public Type<MostraSceltaFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaFascicoloHandler> getType() {
		return TYPE;
	}

}
