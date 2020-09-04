package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class DettaglioPraticaFromProtocollazioneEvent extends GwtEvent<DettaglioPraticaFromProtocollazioneEvent.DettaglioPraticaFromProtocollazioneHandler> {

	public static Type<DettaglioPraticaFromProtocollazioneHandler> TYPE = new Type<DettaglioPraticaFromProtocollazioneHandler>();
	private String idPratica;

	public interface DettaglioPraticaFromProtocollazioneHandler extends EventHandler {
		void onDettaglioPraticaFromProtocollazione(DettaglioPraticaFromProtocollazioneEvent event);
	}

	public DettaglioPraticaFromProtocollazioneEvent(String idPratica) {
		super();
		this.idPratica = idPratica;
	}

	public String getIdPratica() {
		return idPratica;
	}

	@Override
	protected void dispatch(DettaglioPraticaFromProtocollazioneHandler handler) {
		handler.onDettaglioPraticaFromProtocollazione(this);
	}

	@Override
	public Type<DettaglioPraticaFromProtocollazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DettaglioPraticaFromProtocollazioneHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String idPratica) {
		source.fireEvent(new DettaglioPraticaFromProtocollazioneEvent(idPratica));
	}
}
