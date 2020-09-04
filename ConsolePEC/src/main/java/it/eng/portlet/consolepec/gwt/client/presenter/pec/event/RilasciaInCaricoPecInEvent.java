package it.eng.portlet.consolepec.gwt.client.presenter.pec.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class RilasciaInCaricoPecInEvent extends GwtEvent<RilasciaInCaricoPecInEvent.RilasciaInCaricoPecInHandler> {

	public static Type<RilasciaInCaricoPecInHandler> TYPE = new Type<RilasciaInCaricoPecInHandler>();
	
	private String idPecIn;

	public interface RilasciaInCaricoPecInHandler extends EventHandler {
		void onRilasciaInCaricoPecIn(RilasciaInCaricoPecInEvent event);
	}

	public RilasciaInCaricoPecInEvent(String idPecIn) {
		this.idPecIn = idPecIn;
	}
	
	public String getIdPecIn() {
		return idPecIn;
	}

	@Override
	protected void dispatch(RilasciaInCaricoPecInHandler handler) {
		handler.onRilasciaInCaricoPecIn(this);
	}

	@Override
	public Type<RilasciaInCaricoPecInHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RilasciaInCaricoPecInHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String idPecIn) {
		source.fireEvent(new RilasciaInCaricoPecInEvent(idPecIn));
	}
}
