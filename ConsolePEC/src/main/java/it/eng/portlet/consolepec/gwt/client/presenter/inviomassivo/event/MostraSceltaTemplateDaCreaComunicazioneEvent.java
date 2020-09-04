package it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event;

import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MostraSceltaTemplateDaCreaComunicazioneEvent extends GwtEvent<MostraSceltaTemplateDaCreaComunicazioneEvent.MostraSceltaTemplateDaCreaComunicazioneHandler> {

	public static Type<MostraSceltaTemplateDaCreaComunicazioneHandler> TYPE = new Type<MostraSceltaTemplateDaCreaComunicazioneHandler>();

	public interface MostraSceltaTemplateDaCreaComunicazioneHandler extends EventHandler {
		void onMostraSceltaTemplateDaCreaComunicazione(MostraSceltaTemplateDaCreaComunicazioneEvent event);
	}

	private ComunicazioneDTO comuncazione;
	
	public MostraSceltaTemplateDaCreaComunicazioneEvent(ComunicazioneDTO comuncazione) {
		super();
		this.comuncazione = comuncazione;
	}

	@Override
	protected void dispatch(MostraSceltaTemplateDaCreaComunicazioneHandler handler) {
		handler.onMostraSceltaTemplateDaCreaComunicazione(this);
	}

	@Override
	public Type<MostraSceltaTemplateDaCreaComunicazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaTemplateDaCreaComunicazioneHandler> getType() {
		return TYPE;
	}

	public ComunicazioneDTO getComuncazione() {
		return comuncazione;
	}
	

}
