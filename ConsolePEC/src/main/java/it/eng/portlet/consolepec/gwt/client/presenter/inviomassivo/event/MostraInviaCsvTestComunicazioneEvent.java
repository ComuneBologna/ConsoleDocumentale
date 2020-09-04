package it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.event;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraInviaCsvTestComunicazioneEvent extends GwtEvent<MostraInviaCsvTestComunicazioneEvent.MostraInviaCsvTestComunicazioneHandler> {

	public static Type<MostraInviaCsvTestComunicazioneHandler> TYPE = new Type<MostraInviaCsvTestComunicazioneHandler>();
	
	private ComunicazioneDTO comunicazione;
	private AllegatoDTO allegato;

	public interface MostraInviaCsvTestComunicazioneHandler extends EventHandler {
		void onMostraInviaCsvTestComunicazione(MostraInviaCsvTestComunicazioneEvent event);
	}

	public MostraInviaCsvTestComunicazioneEvent(ComunicazioneDTO comunicazione, AllegatoDTO allegato) {
		super();
		this.comunicazione = comunicazione;
		this.allegato = allegato;
	}

	@Override
	protected void dispatch(MostraInviaCsvTestComunicazioneHandler handler) {
		handler.onMostraInviaCsvTestComunicazione(this);
	}

	@Override
	public Type<MostraInviaCsvTestComunicazioneHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraInviaCsvTestComunicazioneHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, ComunicazioneDTO comunicazione, AllegatoDTO allegato) {
		source.fireEvent(new MostraInviaCsvTestComunicazioneEvent(comunicazione, allegato));
	}

	public ComunicazioneDTO getComunicazione() {
		return comunicazione;
	}

	public AllegatoDTO getAllegato() {
		return allegato;
	}
	
	
}
