package it.eng.portlet.consolepec.gwt.client.presenter.event.urbanistica;

import it.eng.cobo.consolepec.commons.urbanistica.AllegatoProcedi;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

/**
 * @author GiacomoFM
 * @since 09/feb/2018
 */
@AllArgsConstructor
public class MostraDettaglioAllegatoProcediEvent extends GwtEvent<MostraDettaglioAllegatoProcediEvent.MostraDettaglioAllegatoProcediHandler> {

	private static Type<MostraDettaglioAllegatoProcediHandler> TYPE = new Type<>();

	@Getter private String idPraticaProcedi;
	@Getter private AllegatoProcedi allegatoProcedi;

	public interface MostraDettaglioAllegatoProcediHandler extends EventHandler {
		void onMostraDettaglioAllegatoProcedi(MostraDettaglioAllegatoProcediEvent event);
	}

	public static Type<MostraDettaglioAllegatoProcediHandler> getType() {
		return TYPE;
	}

	@Override
	public GwtEvent.Type<MostraDettaglioAllegatoProcediHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(MostraDettaglioAllegatoProcediHandler handler) {
		handler.onMostraDettaglioAllegatoProcedi(this);
	}

	public static void fire(HasHandlers source, String idPraticaProcedi, AllegatoProcedi allegato) {
		source.fireEvent(new MostraDettaglioAllegatoProcediEvent(idPraticaProcedi, allegato));
	}
	
	
}
