package it.eng.portlet.consolepec.gwt.client.drive.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import it.eng.portlet.consolepec.gwt.client.drive.event.CreaCartellaEvent.CreaCartellaEventHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Giacomo F.M.
 * @since 2019-06-24
 */
@AllArgsConstructor
public class CreaCartellaEvent extends GwtEvent<CreaCartellaEventHandler> {

	@Getter
	public static Type<CreaCartellaEventHandler> type = new Type<>();

	public static interface CreaCartellaEventHandler extends EventHandler {
		void onCreaCartella(CreaCartellaEvent creaCartellaEvent);
	}

	@Getter
	private String idCartellaPadre;

	@Override
	public Type<CreaCartellaEventHandler> getAssociatedType() {
		return type;
	}

	@Override
	protected void dispatch(CreaCartellaEventHandler handler) {
		handler.onCreaCartella(this);
	}

}
