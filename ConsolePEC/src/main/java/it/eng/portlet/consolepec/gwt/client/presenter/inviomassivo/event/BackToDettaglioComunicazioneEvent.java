package it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class BackToDettaglioComunicazioneEvent extends GwtEvent<BackToDettaglioComunicazioneEvent.BackToDettaglioComunicazioneHandler> {

	public static Type<BackToDettaglioComunicazioneHandler> TYPE = new Type<BackToDettaglioComunicazioneHandler>();

	public interface BackToDettaglioComunicazioneHandler extends EventHandler {
		void onBackToDettaglioComunicazione(BackToDettaglioComunicazioneEvent event);
	}

	public BackToDettaglioComunicazioneEvent() {
		super();
	}
	
	@Override
	protected void dispatch(BackToDettaglioComunicazioneHandler handler) {
		handler.onBackToDettaglioComunicazione(this);
	}

	@Override
	public Type<BackToDettaglioComunicazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<BackToDettaglioComunicazioneHandler> getType() {
		return TYPE;
	}
	
	

}
