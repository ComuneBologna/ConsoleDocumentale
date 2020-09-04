package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ImpostaPulsantieraFascicoloGenericoEvent extends GwtEvent<ImpostaPulsantieraFascicoloGenericoEvent.ImpostaPulsantieraFascicoloGenericoEventHandler> {

	public static Type<ImpostaPulsantieraFascicoloGenericoEventHandler> TYPE = new Type<ImpostaPulsantieraFascicoloGenericoEventHandler>();
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ImpostaPulsantieraFascicoloGenericoEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ImpostaPulsantieraFascicoloGenericoEventHandler handler) {
		handler.onShowMessage(this);
	}
	
	
	public interface ImpostaPulsantieraFascicoloGenericoEventHandler extends EventHandler {
		void onShowMessage(ImpostaPulsantieraFascicoloGenericoEvent event);
	}

}
