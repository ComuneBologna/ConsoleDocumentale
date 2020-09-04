package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class HideAppLoadingEvent extends GwtEvent<HideAppLoadingEvent.HideAppLoadingHandler> {

	public static Type<HideAppLoadingHandler> TYPE = new Type<HideAppLoadingHandler>();

	public interface HideAppLoadingHandler extends EventHandler {

		void onHideAppLoading(HideAppLoadingEvent event);
	}

	public HideAppLoadingEvent() {

	}

	@Override
	protected void dispatch(HideAppLoadingHandler handler) {

		handler.onHideAppLoading(this);
	}

	@Override
	public Type<HideAppLoadingHandler> getAssociatedType() {

		return TYPE;
	}

	public static Type<HideAppLoadingHandler> getType() {

		return TYPE;
	}

	public static void fire(HasHandlers source) {

		source.fireEvent(new HideAppLoadingEvent());
	}
}
