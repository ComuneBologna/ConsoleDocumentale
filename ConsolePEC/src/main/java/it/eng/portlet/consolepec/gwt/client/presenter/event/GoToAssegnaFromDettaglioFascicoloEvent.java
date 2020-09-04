package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class GoToAssegnaFromDettaglioFascicoloEvent extends GwtEvent<GoToAssegnaFromDettaglioFascicoloEvent.GoToAssegnaFromDettaglioFascicoloHandler> {

	public static Type<GoToAssegnaFromDettaglioFascicoloHandler> TYPE = new Type<GoToAssegnaFromDettaglioFascicoloHandler>();
	private String idFascicolo;

	public interface GoToAssegnaFromDettaglioFascicoloHandler extends EventHandler {
		void onGoToAssegnaFromDettaglioFascicolo(GoToAssegnaFromDettaglioFascicoloEvent event);
	}

	public interface GoToAssegnaFromDettaglioFascicoloHasHandlers extends HasHandlers {
		HandlerRegistration addGoToAssegnaFromDettaglioFascicoloHandler(GoToAssegnaFromDettaglioFascicoloHandler handler);
	}

	public GoToAssegnaFromDettaglioFascicoloEvent(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	@Override
	protected void dispatch(GoToAssegnaFromDettaglioFascicoloHandler handler) {
		handler.onGoToAssegnaFromDettaglioFascicolo(this);
	}

	@Override
	public Type<GoToAssegnaFromDettaglioFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToAssegnaFromDettaglioFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String idFascicolo) {
		source.fireEvent(new GoToAssegnaFromDettaglioFascicoloEvent(idFascicolo));
	}
}
