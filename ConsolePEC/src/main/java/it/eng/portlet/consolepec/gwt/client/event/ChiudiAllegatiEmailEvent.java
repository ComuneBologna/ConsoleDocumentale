package it.eng.portlet.consolepec.gwt.client.event;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ChiudiAllegatiEmailEvent extends GwtEvent<ChiudiAllegatiEmailEvent.ChiudiAllegatiEmailHandler> {

	public static Type<ChiudiAllegatiEmailHandler> TYPE = new Type<ChiudiAllegatiEmailHandler>();
	
	private FascicoloDTO fascicolo = null;

	public interface ChiudiAllegatiEmailHandler extends EventHandler {
		void onAnnullaImportaAllegatiEmail(ChiudiAllegatiEmailEvent event);
	}

	public ChiudiAllegatiEmailEvent() {
	}

	@Override
	protected void dispatch(ChiudiAllegatiEmailHandler handler) {
		handler.onAnnullaImportaAllegatiEmail(this);
	}

	@Override
	public Type<ChiudiAllegatiEmailHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ChiudiAllegatiEmailHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ChiudiAllegatiEmailEvent());
	}

	public FascicoloDTO getFascicolo() {
		return fascicolo;
	}

	public void setFascicolo(FascicoloDTO fascicolo) {
		this.fascicolo = fascicolo;
	}
}
