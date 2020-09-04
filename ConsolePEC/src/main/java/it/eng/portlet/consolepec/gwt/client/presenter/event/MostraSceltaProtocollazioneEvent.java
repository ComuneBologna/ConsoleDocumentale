package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MostraSceltaProtocollazioneEvent extends GwtEvent<MostraSceltaProtocollazioneEvent.MostraSceltaProtocollazioneHandler> {

	public static Type<MostraSceltaProtocollazioneHandler> TYPE = new Type<MostraSceltaProtocollazioneHandler>();
	private String fascicoloPath;

	public interface MostraSceltaProtocollazioneHandler extends EventHandler {
		void onMostraSceltaProtocollazione(MostraSceltaProtocollazioneEvent event);
	}

	public MostraSceltaProtocollazioneEvent(String fascicoloPath) {
		this.fascicoloPath = fascicoloPath;
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

	@Override
	protected void dispatch(MostraSceltaProtocollazioneHandler handler) {
		handler.onMostraSceltaProtocollazione(this);
	}

	@Override
	public Type<MostraSceltaProtocollazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaProtocollazioneHandler> getType() {
		return TYPE;
	}
	
}
