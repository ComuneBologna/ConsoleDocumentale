package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraDettaglioAllegatoDaFormProtocollazioneEvent extends GwtEvent<MostraDettaglioAllegatoDaFormProtocollazioneEvent.MostraDettaglioAllegatoDaFormProtocollazioneHandler> {

	public static Type<MostraDettaglioAllegatoDaFormProtocollazioneHandler> TYPE = new Type<MostraDettaglioAllegatoDaFormProtocollazioneHandler>();
	private AllegatoDTO allegato;

	public interface MostraDettaglioAllegatoDaFormProtocollazioneHandler extends EventHandler {
		void onMostraDettaglioAllegatoDaFormProtocollazione(MostraDettaglioAllegatoDaFormProtocollazioneEvent event);
	}

	public MostraDettaglioAllegatoDaFormProtocollazioneEvent(AllegatoDTO allegato) {
		this.allegato = allegato;
	}

	@Override
	protected void dispatch(MostraDettaglioAllegatoDaFormProtocollazioneHandler handler) {
		handler.onMostraDettaglioAllegatoDaFormProtocollazione(this);
	}

	@Override
	public Type<MostraDettaglioAllegatoDaFormProtocollazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraDettaglioAllegatoDaFormProtocollazioneHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source,AllegatoDTO allegatoDTO) {
		source.fireEvent(new MostraDettaglioAllegatoDaFormProtocollazioneEvent(allegatoDTO));
	}


	public AllegatoDTO getAllegato() {
		return allegato;
	}

	public void setAllegato(AllegatoDTO allegato) {
		this.allegato = allegato;
	}
}
