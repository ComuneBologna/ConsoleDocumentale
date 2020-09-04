package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraSceltaCapofilaEmailInEvent extends GwtEvent<MostraSceltaCapofilaEmailInEvent.MostraSceltaCapofilaEmailInHandler> {

	public static Type<MostraSceltaCapofilaEmailInHandler> TYPE = new Type<MostraSceltaCapofilaEmailInHandler>();

	private CreaFascicoloDTO creaFascicoloDTO;
	private int numeroAllegati;

	public int getNumeroAllegati() {
		return numeroAllegati;
	}

	public void setNumeroAllegati(int numeroAllegati) {
		this.numeroAllegati = numeroAllegati;
	}

	public CreaFascicoloDTO getCreaFascicoloDTO() {
		return creaFascicoloDTO;
	}

	public void setCreaFascicoloDTO(CreaFascicoloDTO creaFascicoloDTO) {
		this.creaFascicoloDTO = creaFascicoloDTO;
	}

	public interface MostraSceltaCapofilaEmailInHandler extends EventHandler {
		void onMostraSceltaCapofilaEmailIn(MostraSceltaCapofilaEmailInEvent event);
	}

	public MostraSceltaCapofilaEmailInEvent() {
	}

	@Override
	protected void dispatch(MostraSceltaCapofilaEmailInHandler handler) {
		handler.onMostraSceltaCapofilaEmailIn(this);
	}

	@Override
	public Type<MostraSceltaCapofilaEmailInHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaCapofilaEmailInHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraSceltaCapofilaEmailInEvent());
	}
}
