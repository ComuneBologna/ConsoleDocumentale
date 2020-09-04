package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class TornaAFormDiProtocollazioneEvent extends GwtEvent<TornaAFormDiProtocollazioneEvent.TornaAFormDiProtocollazioneHandler> {

	public static Type<TornaAFormDiProtocollazioneHandler> TYPE = new Type<TornaAFormDiProtocollazioneHandler>();

	public interface TornaAFormDiProtocollazioneHandler extends EventHandler {
		void onTornaAFormDiProtocollazione(TornaAFormDiProtocollazioneEvent event);
	}

	public TornaAFormDiProtocollazioneEvent() {
	}

	@Override
	protected void dispatch(TornaAFormDiProtocollazioneHandler handler) {
		handler.onTornaAFormDiProtocollazione(this);
	}

	@Override
	public Type<TornaAFormDiProtocollazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<TornaAFormDiProtocollazioneHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new TornaAFormDiProtocollazioneEvent());
	}
}
