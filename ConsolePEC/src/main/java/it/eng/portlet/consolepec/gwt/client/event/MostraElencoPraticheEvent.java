package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraElencoPraticheEvent extends GwtEvent<MostraElencoPraticheEvent.MostraElencoPraticheHandler> {

	public static Type<MostraElencoPraticheHandler> TYPE = new Type<MostraElencoPraticheHandler>();

	public interface MostraElencoPraticheHandler extends EventHandler {
		void onMostraElencoPratiche(MostraElencoPraticheEvent event);
	}

	public MostraElencoPraticheEvent() {
	}

	@Override
	protected void dispatch(MostraElencoPraticheHandler handler) {
		handler.onMostraElencoPratiche(this);
	}

	@Override
	public Type<MostraElencoPraticheHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraElencoPraticheHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraElencoPraticheEvent());
	}
}
