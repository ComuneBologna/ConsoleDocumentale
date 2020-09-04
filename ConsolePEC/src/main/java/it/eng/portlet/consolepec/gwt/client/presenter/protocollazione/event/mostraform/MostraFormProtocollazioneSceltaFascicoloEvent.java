package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform;

import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class MostraFormProtocollazioneSceltaFascicoloEvent extends GwtEvent<MostraFormProtocollazioneSceltaFascicoloEvent.MostraFormProtocollazioneSceltaFascicoloHandler> {

	public static Type<MostraFormProtocollazioneSceltaFascicoloHandler> TYPE = new Type<MostraFormProtocollazioneSceltaFascicoloHandler>();

	private FascicoloDTO fascicoloDTO;
	private PraticaDTO praticaDaProtocollare;
	private DatiDefaultProtocollazione datiPerFormProtocollazione;

	public interface MostraFormProtocollazioneSceltaFascicoloHandler extends EventHandler {
		void onMostraFormProtocollazioneSceltaFascicolo(MostraFormProtocollazioneSceltaFascicoloEvent event);
	}

	public interface MostraFormProtocollazioneSceltaFascicoloHasHandlers extends HasHandlers {
		HandlerRegistration addMostraFormProtocollazioneSceltaFascicoloHandler(MostraFormProtocollazioneSceltaFascicoloHandler handler);
	}

	public MostraFormProtocollazioneSceltaFascicoloEvent() {
	}

	@Override
	protected void dispatch(MostraFormProtocollazioneSceltaFascicoloHandler handler) {
		handler.onMostraFormProtocollazioneSceltaFascicolo(this);
	}

	@Override
	public Type<MostraFormProtocollazioneSceltaFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraFormProtocollazioneSceltaFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraFormProtocollazioneSceltaFascicoloEvent());
	}

	public FascicoloDTO getFascicoloDTO() {
		return fascicoloDTO;
	}

	public void setFascicoloDTO(FascicoloDTO fascicoloDTO) {
		this.fascicoloDTO = fascicoloDTO;
	}

	public DatiDefaultProtocollazione getDatiPerFormProtocollazione() {
		return datiPerFormProtocollazione;
	}

	public void setDatiPerFormProtocollazione(DatiDefaultProtocollazione datiPerFormProtocollazione) {
		this.datiPerFormProtocollazione = datiPerFormProtocollazione;
	}

	public PraticaDTO getPraticaDaProtocollare() {
		return praticaDaProtocollare;
	}

	public void setPraticaDaProtocollare(PraticaDTO praticaDaProtocollare) {
		this.praticaDaProtocollare = praticaDaProtocollare;
	}
}
