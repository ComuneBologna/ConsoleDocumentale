package it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class BackToCreaComunicazioneEvent extends GwtEvent<BackToCreaComunicazioneEvent.BackToCreaComunicazioneHandler> {

	public static Type<BackToCreaComunicazioneHandler> TYPE = new Type<BackToCreaComunicazioneHandler>();

	public interface BackToCreaComunicazioneHandler extends EventHandler {
		void onBackToCreaComunicazione(BackToCreaComunicazioneEvent event);
	}

	public BackToCreaComunicazioneEvent() {
		super();
	}
	
	@Override
	protected void dispatch(BackToCreaComunicazioneHandler handler) {
		handler.onBackToCreaComunicazione(this);
	}

	@Override
	public Type<BackToCreaComunicazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<BackToCreaComunicazioneHandler> getType() {
		return TYPE;
	}
	
	

}
