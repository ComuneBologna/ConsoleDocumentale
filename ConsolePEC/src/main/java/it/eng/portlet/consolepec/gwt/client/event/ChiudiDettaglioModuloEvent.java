package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ChiudiDettaglioModuloEvent extends GwtEvent<ChiudiDettaglioModuloEvent.ChiudiDettaglioModuloHandler> {

	public static Type<ChiudiDettaglioModuloHandler> TYPE = new Type<ChiudiDettaglioModuloHandler>();

	public interface ChiudiDettaglioModuloHandler extends EventHandler {
		void onChiudiDettaglioModulo(ChiudiDettaglioModuloEvent event);
	}
	
	@Override
	protected void dispatch(ChiudiDettaglioModuloHandler handler) {
		handler.onChiudiDettaglioModulo(this);
	}

	@Override
	public Type<ChiudiDettaglioModuloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ChiudiDettaglioModuloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ChiudiDettaglioModuloEvent());
	}
}
