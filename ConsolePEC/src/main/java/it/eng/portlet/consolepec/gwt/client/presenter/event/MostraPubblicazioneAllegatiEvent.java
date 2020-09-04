package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraPubblicazioneAllegatiEvent extends GwtEvent<MostraPubblicazioneAllegatiEvent.MostraPubblicazioneAllegatiHandler> {

	public static Type<MostraPubblicazioneAllegatiHandler> TYPE = new Type<MostraPubblicazioneAllegatiHandler>();
	private String idFascicolo;
	private String nomeAllegato;

	public interface MostraPubblicazioneAllegatiHandler extends EventHandler {
		void onMostraPubblicazioneAllegati(MostraPubblicazioneAllegatiEvent event);
	}

	public MostraPubblicazioneAllegatiEvent(String idFascicolo, String nomeAllegato) {
		this.idFascicolo = idFascicolo;
		this.nomeAllegato = nomeAllegato;
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public String getNomeAllegato() {
		return nomeAllegato;
	}

	@Override
	protected void dispatch(MostraPubblicazioneAllegatiHandler handler) {
		handler.onMostraPubblicazioneAllegati(this);
	}

	@Override
	public Type<MostraPubblicazioneAllegatiHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraPubblicazioneAllegatiHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String idFascicolo, String nomeAllegato) {
		source.fireEvent(new MostraPubblicazioneAllegatiEvent(idFascicolo, nomeAllegato));
	}
}
