package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.SelezioneElementoEvent.SelezioneElementoEventHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author GiacomoFM
 * @since 31/gen/2019
 */
@AllArgsConstructor
public class SelezioneElementoEvent extends GwtEvent<SelezioneElementoEventHandler> {

	public static interface SelezioneElementoEventHandler extends EventHandler {

		void onSelezionaElemento(ElementoComposizione elementoSelezionato);

		void onDeselezionaElemento(ElementoComposizione elementoDeselezionato);
	}

	public static final Type<SelezioneElementoEventHandler> TYPE = new Type<>();

	private boolean check;
	@Getter
	private ElementoComposizione elementoComposizione;

	@Override
	public Type<SelezioneElementoEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(SelezioneElementoEventHandler handler) {
		if (check) {
			handler.onSelezionaElemento(elementoComposizione);
		} else {
			handler.onDeselezionaElemento(elementoComposizione);
		}
	}

}
