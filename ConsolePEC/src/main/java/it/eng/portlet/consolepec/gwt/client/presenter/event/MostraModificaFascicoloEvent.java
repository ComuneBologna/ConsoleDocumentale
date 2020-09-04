package it.eng.portlet.consolepec.gwt.client.presenter.event;

import lombok.Getter;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * @author GiacomoFM
 * @since 17/lug/2017
 */
public class MostraModificaFascicoloEvent extends GwtEvent<MostraModificaFascicoloEvent.MostraModificaFascicoloHandler> {
	
	private static Type<MostraModificaFascicoloHandler> TYPE = new Type<MostraModificaFascicoloHandler>();

	public interface MostraModificaFascicoloHandler extends EventHandler {
		void onMostraModificaFascicolo(MostraModificaFascicoloEvent event);
	}
	
	@Getter
	private String pathFascicolo;
	
	public MostraModificaFascicoloEvent(String pathFascicolo) {
		this.pathFascicolo = pathFascicolo;
	}

	@Override
	protected void dispatch(MostraModificaFascicoloHandler handler) {
		handler.onMostraModificaFascicolo(this);
	}

	@Override
	public Type<MostraModificaFascicoloHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static Type<MostraModificaFascicoloHandler> getType() {
		return TYPE;
	}
}
