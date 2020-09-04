package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraSceltaCapofilaPraticaModulisticaEvent extends GwtEvent<MostraSceltaCapofilaPraticaModulisticaEvent.MostraSceltaCapofilaPraticaModulisticaHandler> {

	public static Type<MostraSceltaCapofilaPraticaModulisticaHandler> TYPE = new Type<MostraSceltaCapofilaPraticaModulisticaHandler>();

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

	public interface MostraSceltaCapofilaPraticaModulisticaHandler extends EventHandler {
		void onMostraSceltaCapofilaPraticaModulistica(MostraSceltaCapofilaPraticaModulisticaEvent event);
	}

	public MostraSceltaCapofilaPraticaModulisticaEvent() {
	}

	@Override
	protected void dispatch(MostraSceltaCapofilaPraticaModulisticaHandler handler) {
		handler.onMostraSceltaCapofilaPraticaModulistica(this);
	}

	@Override
	public Type<MostraSceltaCapofilaPraticaModulisticaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaCapofilaPraticaModulisticaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraSceltaCapofilaPraticaModulisticaEvent());
	}
}
