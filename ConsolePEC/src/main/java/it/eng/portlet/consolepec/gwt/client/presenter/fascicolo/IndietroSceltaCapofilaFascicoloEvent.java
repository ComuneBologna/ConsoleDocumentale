package it.eng.portlet.consolepec.gwt.client.presenter.fascicolo;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class IndietroSceltaCapofilaFascicoloEvent extends GwtEvent<IndietroSceltaCapofilaFascicoloEvent.IndietroSceltaCapofilaFascicoloHandler> {

	public static Type<IndietroSceltaCapofilaFascicoloHandler> TYPE = new Type<IndietroSceltaCapofilaFascicoloHandler>();

	public interface IndietroSceltaCapofilaFascicoloHandler extends EventHandler {
		void onIndietroSceltaCapofilaFascicolo(IndietroSceltaCapofilaFascicoloEvent event);
	}

	private String idFascicolo;
	
	
	public IndietroSceltaCapofilaFascicoloEvent() {
	}

	@Override
	protected void dispatch(IndietroSceltaCapofilaFascicoloHandler handler) {
		handler.onIndietroSceltaCapofilaFascicolo(this);
	}

	@Override
	public Type<IndietroSceltaCapofilaFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<IndietroSceltaCapofilaFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new IndietroSceltaCapofilaFascicoloEvent());
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public void setIdFascicolo(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}
}
