package it.eng.portlet.consolepec.gwt.client.presenter.event.rubrica;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author GiacomoFM
 * @since 15/set/2017
 */
@AllArgsConstructor
public class MostraDettaglioAnagraficaEvent extends GwtEvent<MostraDettaglioAnagraficaEvent.MostraDettaglioAnagraficaHandler> {

	private static Type<MostraDettaglioAnagraficaHandler> TYPE = new Type<MostraDettaglioAnagraficaHandler>();

	@Getter private final Anagrafica anagrafica;
	@Getter private final Object openingRequestor;

	public interface MostraDettaglioAnagraficaHandler extends EventHandler {
		void onMostraDettaglioAnagrafica(MostraDettaglioAnagraficaEvent event);
	}

	@Override
	public Type<MostraDettaglioAnagraficaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraDettaglioAnagraficaHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MostraDettaglioAnagraficaHandler handler) {
		handler.onMostraDettaglioAnagrafica(this);
	}

}
