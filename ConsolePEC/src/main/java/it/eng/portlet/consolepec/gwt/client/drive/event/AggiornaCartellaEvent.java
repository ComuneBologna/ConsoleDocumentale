package it.eng.portlet.consolepec.gwt.client.drive.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaCartellaEvent.AggiornaCartellaEventHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Giacomo F.M.
 * @since 2019-06-12
 */
@AllArgsConstructor
public class AggiornaCartellaEvent extends GwtEvent<AggiornaCartellaEventHandler> {

	public static final Type<AggiornaCartellaEventHandler> TYPE = new Type<>();

	public static interface AggiornaCartellaEventHandler extends EventHandler {
		void aggiornaCartella(Cartella cartella, boolean reload);
	}

	@Getter
	private Cartella cartella;
	@Getter
	private boolean reload;

	@Override
	public Type<AggiornaCartellaEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AggiornaCartellaEventHandler handler) {
		handler.aggiornaCartella(cartella, reload);
	}

}
