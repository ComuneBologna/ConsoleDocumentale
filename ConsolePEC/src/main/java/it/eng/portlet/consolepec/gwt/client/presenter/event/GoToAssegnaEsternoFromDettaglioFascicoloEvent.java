package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class GoToAssegnaEsternoFromDettaglioFascicoloEvent extends GwtEvent<GoToAssegnaEsternoFromDettaglioFascicoloEvent.GoToAssegnaEsternoFromDettaglioFascicoloHandler> {

	public static Type<GoToAssegnaEsternoFromDettaglioFascicoloHandler> TYPE = new Type<GoToAssegnaEsternoFromDettaglioFascicoloHandler>();
	private String idFascicolo;

	public interface GoToAssegnaEsternoFromDettaglioFascicoloHandler extends EventHandler {
		void onGoToAssegnaEsternoFromDettaglioFascicolo(GoToAssegnaEsternoFromDettaglioFascicoloEvent event);
	}

	public interface GoToAssegnaFromDettaglioFascicoloHasHandlers extends HasHandlers {
		HandlerRegistration addGoToAssegnaFromDettaglioFascicoloHandler(GoToAssegnaEsternoFromDettaglioFascicoloHandler handler);
	}

	public GoToAssegnaEsternoFromDettaglioFascicoloEvent(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	@Override
	protected void dispatch(GoToAssegnaEsternoFromDettaglioFascicoloHandler handler) {
		handler.onGoToAssegnaEsternoFromDettaglioFascicolo(this);
	}

	@Override
	public Type<GoToAssegnaEsternoFromDettaglioFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToAssegnaEsternoFromDettaglioFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String idFascicolo) {
		source.fireEvent(new GoToAssegnaEsternoFromDettaglioFascicoloEvent(idFascicolo));
	}
}
