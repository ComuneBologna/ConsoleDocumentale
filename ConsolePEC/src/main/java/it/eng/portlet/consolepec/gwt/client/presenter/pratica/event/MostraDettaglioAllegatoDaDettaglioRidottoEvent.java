package it.eng.portlet.consolepec.gwt.client.presenter.pratica.event;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraDettaglioAllegatoDaDettaglioRidottoEvent extends GwtEvent<MostraDettaglioAllegatoDaDettaglioRidottoEvent.MostraDettaglioAllegatoDaDettaglioRidottoHandler> {

	public static Type<MostraDettaglioAllegatoDaDettaglioRidottoHandler> TYPE = new Type<MostraDettaglioAllegatoDaDettaglioRidottoHandler>();
	private AllegatoDTO allegato;

	public interface MostraDettaglioAllegatoDaDettaglioRidottoHandler extends EventHandler {
		void onMostraDettaglioAllegatoDaDettaglioRidotto(MostraDettaglioAllegatoDaDettaglioRidottoEvent event);
	}

	public MostraDettaglioAllegatoDaDettaglioRidottoEvent(AllegatoDTO allegato) {
		super();
		this.allegato = allegato;
	}

	@Override
	protected void dispatch(MostraDettaglioAllegatoDaDettaglioRidottoHandler handler) {
		handler.onMostraDettaglioAllegatoDaDettaglioRidotto(this);
	}

	@Override
	public Type<MostraDettaglioAllegatoDaDettaglioRidottoHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraDettaglioAllegatoDaDettaglioRidottoHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source,String clientID, AllegatoDTO allegatoDTO) {
		source.fireEvent(new MostraDettaglioAllegatoDaDettaglioRidottoEvent(allegatoDTO));
	}

	public AllegatoDTO getAllegato() {
		return allegato;
	}

	public void setAllegato(AllegatoDTO allegato) {
		this.allegato = allegato;
	}
}
