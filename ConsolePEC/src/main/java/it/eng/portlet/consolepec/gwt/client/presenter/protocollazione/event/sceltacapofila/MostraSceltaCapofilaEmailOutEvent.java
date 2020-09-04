package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class MostraSceltaCapofilaEmailOutEvent extends GwtEvent<MostraSceltaCapofilaEmailOutEvent.MostraSceltaCapofilaEmailOutHandler> {

	public static Type<MostraSceltaCapofilaEmailOutHandler> TYPE = new Type<MostraSceltaCapofilaEmailOutHandler>();

	private String idFascicolo;
	private String idPecOut;
	private boolean interoperabile;

	public interface MostraSceltaCapofilaEmailOutHandler extends EventHandler {
		void onMostraSceltaCapofilaEmailOut(MostraSceltaCapofilaEmailOutEvent event);
	}

	public interface MostraSceltaCapofilaEmailOutHasHandlers extends HasHandlers {
		HandlerRegistration addMostraSceltaCapofilaEmailOutHandler(MostraSceltaCapofilaEmailOutHandler handler);
	}

	public MostraSceltaCapofilaEmailOutEvent() {
	}

	@Override
	protected void dispatch(MostraSceltaCapofilaEmailOutHandler handler) {
		handler.onMostraSceltaCapofilaEmailOut(this);
	}

	@Override
	public Type<MostraSceltaCapofilaEmailOutHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaCapofilaEmailOutHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraSceltaCapofilaEmailOutEvent());
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public void setIdFascicolo(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public String getIdPecOut() {
		return idPecOut;
	}

	public void setIdPecOut(String idPecOut) {
		this.idPecOut = idPecOut;
	}

	public boolean isInteroperabile() {
		return interoperabile;
	}

	public void setInteroperabile(boolean interoperabile) {
		this.interoperabile = interoperabile;
	}
	
	
}
