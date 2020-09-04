package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.AggiornaPostSelezioneEvent.AggiornaPostSelezioneEventHandler;

/**
 * @author GiacomoFM
 * @since 06/feb/2019
 */
public class AggiornaPostSelezioneEvent extends GwtEvent<AggiornaPostSelezioneEventHandler> {

	public static interface AggiornaPostSelezioneEventHandler extends EventHandler {
		void update();
	}

	public static final Type<AggiornaPostSelezioneEventHandler> TYPE = new Type<>();

	@Override
	public Type<AggiornaPostSelezioneEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AggiornaPostSelezioneEventHandler handler) {
		handler.update();
	}

}
