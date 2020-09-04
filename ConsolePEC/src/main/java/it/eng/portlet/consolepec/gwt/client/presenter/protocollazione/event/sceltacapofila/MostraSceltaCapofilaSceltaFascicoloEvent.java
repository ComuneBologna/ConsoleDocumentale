package it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.event.sceltacapofila;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public class MostraSceltaCapofilaSceltaFascicoloEvent extends GwtEvent<MostraSceltaCapofilaSceltaFascicoloEvent.MostraSceltaCapofilaSceltaFascicoloHandler> {

	public static Type<MostraSceltaCapofilaSceltaFascicoloHandler> TYPE = new Type<MostraSceltaCapofilaSceltaFascicoloHandler>();

	private String idFascicolo;
	private String idEmailIn;
	private String idPraticaModulistica;

	public interface MostraSceltaCapofilaSceltaFascicoloHandler extends EventHandler {
		void onMostraSceltaCapofilaSceltaFascicolo(MostraSceltaCapofilaSceltaFascicoloEvent event);
	}

	public interface MostraSceltaCapofilaSceltaFascicoloHasHandlers extends HasHandlers {
		HandlerRegistration addMostraSceltaCapofilaSceltaFascicoloHandler(MostraSceltaCapofilaSceltaFascicoloHandler handler);
	}

	public MostraSceltaCapofilaSceltaFascicoloEvent() {
	}

	@Override
	protected void dispatch(MostraSceltaCapofilaSceltaFascicoloHandler handler) {
		handler.onMostraSceltaCapofilaSceltaFascicolo(this);
	}

	@Override
	public Type<MostraSceltaCapofilaSceltaFascicoloHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraSceltaCapofilaSceltaFascicoloHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new MostraSceltaCapofilaSceltaFascicoloEvent());
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public void setIdFascicolo(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public String getIdEmailIn() {
		return idEmailIn;
	}

	public void setIdEmailIn(String idEmailIn) {
		this.idEmailIn = idEmailIn;
	}

	public String getIdPraticaModulistica() {
		return idPraticaModulistica;
	}

	public void setIdPraticaModulistica(String idPraticaModulistica) {
		this.idPraticaModulistica = idPraticaModulistica;
	}
	
	
}
