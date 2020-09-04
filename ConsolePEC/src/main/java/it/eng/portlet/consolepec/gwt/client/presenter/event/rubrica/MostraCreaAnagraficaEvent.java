package it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.client.event.OpeningEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author GiacomoFM
 * @since 16/ott/2017
 */
@AllArgsConstructor
public class MostraCreaAnagraficaEvent extends GwtEvent<MostraCreaAnagraficaEvent.MostraCreaAnagraficaHandler> implements OpeningEvent {

	private static Type<MostraCreaAnagraficaHandler> TYPE = new Type<MostraCreaAnagraficaHandler>();

	@Getter private Object openingRequestor;
	@Getter private String nomeDatoAggiuntivo;

	public interface MostraCreaAnagraficaHandler extends EventHandler {
		void onMostraCreaAnagrafica(MostraCreaAnagraficaEvent event);
	}

	@Override
	public Type<MostraCreaAnagraficaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraCreaAnagraficaHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MostraCreaAnagraficaHandler handler) {
		handler.onMostraCreaAnagrafica(this);
	}
}
