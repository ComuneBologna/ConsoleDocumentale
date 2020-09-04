package it.eng.portlet.consolepec.gwt.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ChiudiProtocollazionePageEvent extends GwtEvent<ChiudiProtocollazionePageEvent.ChiudiTipoProtocollazionePageHandler> {

	public static Type<ChiudiTipoProtocollazionePageHandler> TYPE = new Type<ChiudiTipoProtocollazionePageHandler>();

	public interface ChiudiTipoProtocollazionePageHandler extends EventHandler {
		void onChiudiTipoProtocollazionePage(ChiudiProtocollazionePageEvent event);
	}

	private String clientID;

	public ChiudiProtocollazionePageEvent(String clientID) {
		if (clientID == null)
			throw new IllegalArgumentException("ChiudiProtocollazionePageEvent Id è null");
		this.clientID = clientID;
	}

	@Override
	protected void dispatch(ChiudiTipoProtocollazionePageHandler handler) {
		handler.onChiudiTipoProtocollazionePage(this);
	}

	@Override
	public Type<ChiudiTipoProtocollazionePageHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ChiudiTipoProtocollazionePageHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String id) {
		source.fireEvent(new ChiudiProtocollazionePageEvent(id));
	}

	public String getId() {
		return clientID;
	}

}
