package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class MostraFormProtocollazionePecInEvent extends GwtEvent<MostraFormProtocollazionePecInEvent.MostraFormProtocollazionePecInHandler> {

	public static Type<MostraFormProtocollazionePecInHandler> TYPE = new Type<MostraFormProtocollazionePecInHandler>();

	private DatiDefaultProtocollazione datiDefaultProtocollazione;
	private CreaFascicoloDTO creaFascicoloDTO;
	private PecInDTO pecInDTO;

	public interface MostraFormProtocollazionePecInHandler extends EventHandler {
		void onMostraFormProtocollazionePecIn(MostraFormProtocollazionePecInEvent event);
	}

	public interface MostraFormProtocollazionePecInHasHandlers extends HasHandlers {
		HandlerRegistration addMostraFormProtocollazionePecInHandler(MostraFormProtocollazionePecInHandler handler);
	}

	public MostraFormProtocollazionePecInEvent() {
	}

	@Override
	protected void dispatch(MostraFormProtocollazionePecInHandler handler) {
		handler.onMostraFormProtocollazionePecIn(this);
	}

	@Override
	public Type<MostraFormProtocollazionePecInHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraFormProtocollazionePecInHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraFormProtocollazionePecInEvent());
	}

	public DatiDefaultProtocollazione getDatiDefaultProtocollazione() {
		return datiDefaultProtocollazione;
	}

	public void setDatiDefaultProtocollazione(DatiDefaultProtocollazione datiDefaultProtocollazione) {
		this.datiDefaultProtocollazione = datiDefaultProtocollazione;
	}

	public CreaFascicoloDTO getCreaFascicoloDTO() {
		return creaFascicoloDTO;
	}

	public void setCreaFascicoloDTO(CreaFascicoloDTO creaFascicoloDTO) {
		this.creaFascicoloDTO = creaFascicoloDTO;
	}

	public PecInDTO getPecInDTO() {
		return pecInDTO;
	}

	public void setPecInDTO(PecInDTO pecInDTO) {
		this.pecInDTO = pecInDTO;
	}
}
