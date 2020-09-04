package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.mostraform;

import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class MostraFormProtocollazionePecOutEvent extends GwtEvent<MostraFormProtocollazionePecOutEvent.MostraFormProtocollazionePecOutHandler> {

	public static Type<MostraFormProtocollazionePecOutHandler> TYPE = new Type<MostraFormProtocollazionePecOutHandler>();

	private PecOutDTO pecOutDTO;
	private FascicoloDTO fascicoloDTO;
	private DatiDefaultProtocollazione datiDefaultProtocollazione;
	private boolean emailInteroperabile;

	public interface MostraFormProtocollazionePecOutHandler extends EventHandler {
		void onMostraFormProtocollazionePecOut(MostraFormProtocollazionePecOutEvent event);
	}

	public interface MostraFormProtocollazionePecOutHasHandlers extends HasHandlers {
		HandlerRegistration addMostraFormProtocollazionePecOutHandler(MostraFormProtocollazionePecOutHandler handler);
	}

	public MostraFormProtocollazionePecOutEvent() {
	}

	@Override
	protected void dispatch(MostraFormProtocollazionePecOutHandler handler) {
		handler.onMostraFormProtocollazionePecOut(this);
	}

	@Override
	public Type<MostraFormProtocollazionePecOutHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraFormProtocollazionePecOutHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraFormProtocollazionePecOutEvent());
	}

	public DatiDefaultProtocollazione getDatiDefaultProtocollazione() {
		return datiDefaultProtocollazione;
	}

	public void setDatiDefaultProtocollazione(DatiDefaultProtocollazione datiDefaultProtocollazione) {
		this.datiDefaultProtocollazione = datiDefaultProtocollazione;
	}

	public boolean isEmailInteroperabile() {
		return emailInteroperabile;
	}

	public void setEmailInteroperabile(boolean emailInteroperabile) {
		this.emailInteroperabile = emailInteroperabile;
	}

	public PecOutDTO getPecOutDTO() {
		return pecOutDTO;
	}

	public void setPecOutDTO(PecOutDTO pecOutDTO) {
		this.pecOutDTO = pecOutDTO;
	}

	public FascicoloDTO getFascicoloDTO() {
		return fascicoloDTO;
	}

	public void setFascicoloDTO(FascicoloDTO fascicoloDTO) {
		this.fascicoloDTO = fascicoloDTO;
	}

}
