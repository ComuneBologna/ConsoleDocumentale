package it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event;

import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraCreaComunicazioneDaDettaglioComunicazioneEvent extends GwtEvent<MostraCreaComunicazioneDaDettaglioComunicazioneEvent.MostraCreaComunicazioneDaDettaglioComunicazioneHandler> {

	public static Type<MostraCreaComunicazioneDaDettaglioComunicazioneHandler> TYPE = new Type<MostraCreaComunicazioneDaDettaglioComunicazioneHandler>();

	public interface MostraCreaComunicazioneDaDettaglioComunicazioneHandler extends EventHandler {
		void onMostraCreaComunicazioneDaDettaglioComunicazione(MostraCreaComunicazioneDaDettaglioComunicazioneEvent event);
	}

	private ComunicazioneDTO comunicazione;
	
	
	public MostraCreaComunicazioneDaDettaglioComunicazioneEvent(ComunicazioneDTO comunicazione) {
		super();
		this.comunicazione = comunicazione;
	}

	@Override
	protected void dispatch(MostraCreaComunicazioneDaDettaglioComunicazioneHandler handler) {
		handler.onMostraCreaComunicazioneDaDettaglioComunicazione(this);
	}

	@Override
	public Type<MostraCreaComunicazioneDaDettaglioComunicazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraCreaComunicazioneDaDettaglioComunicazioneHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, ComunicazioneDTO comunicazione) {
		source.fireEvent(new MostraCreaComunicazioneDaDettaglioComunicazioneEvent(comunicazione));
	}
	
	public ComunicazioneDTO getComunicazione() {
		return comunicazione;
	}

}
