package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ChiudiPubblicazioneAllegatiEvent extends GwtEvent<ChiudiPubblicazioneAllegatiEvent.ChiudiPubblicazioneAllegatiHandler> {

	public static Type<ChiudiPubblicazioneAllegatiHandler> TYPE = new Type<ChiudiPubblicazioneAllegatiHandler>();
	private String idFascicolo;

	public interface ChiudiPubblicazioneAllegatiHandler extends EventHandler {
		void onChiudiPubblicazioneAllegati(ChiudiPubblicazioneAllegatiEvent event);
	}

	public ChiudiPubblicazioneAllegatiEvent(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	@Override
	protected void dispatch(ChiudiPubblicazioneAllegatiHandler handler) {
		handler.onChiudiPubblicazioneAllegati(this);
	}

	@Override
	public Type<ChiudiPubblicazioneAllegatiHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ChiudiPubblicazioneAllegatiHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String idFascicolo) {
		source.fireEvent(new ChiudiPubblicazioneAllegatiEvent(idFascicolo));
	}
}
