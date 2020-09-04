package it.eng.portlet.consolepec.gwt.client.presenter.pratica.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class TornaADettaglioRidottoEvent extends GwtEvent<TornaADettaglioRidottoEvent.TornaADettaglioRidottoHandler> {

	public static Type<TornaADettaglioRidottoHandler> TYPE = new Type<TornaADettaglioRidottoHandler>();
	
	public interface TornaADettaglioRidottoHandler extends EventHandler {
		void onTornaADettaglioRidotto(TornaADettaglioRidottoEvent event);
	}

	public TornaADettaglioRidottoEvent() {
		super();
	}

	@Override
	protected void dispatch(TornaADettaglioRidottoHandler handler) {
		handler.onTornaADettaglioRidotto(this);
	}

	@Override
	public Type<TornaADettaglioRidottoHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<TornaADettaglioRidottoHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new TornaADettaglioRidottoEvent());
	}

	
}
