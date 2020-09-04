package it.eng.portlet.consolepec.gwt.client.presenter.modulistica.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class RilasciaInCaricoPraticaModulisticaEvent extends GwtEvent<RilasciaInCaricoPraticaModulisticaEvent.RilasciaInCaricoPraticaModulisticaHandler> {

	public static Type<RilasciaInCaricoPraticaModulisticaHandler> TYPE = new Type<RilasciaInCaricoPraticaModulisticaHandler>();
	
	private String idPraticaModulistica;

	public interface RilasciaInCaricoPraticaModulisticaHandler extends EventHandler {
		void onRilasciaInCaricoPraticaModulistica(RilasciaInCaricoPraticaModulisticaEvent event);
	}

	public RilasciaInCaricoPraticaModulisticaEvent(String idPecIn) {
		this.idPraticaModulistica = idPecIn;
	}
	
	public String getIdPraticaModulistica() {
		return idPraticaModulistica;
	}

	@Override
	protected void dispatch(RilasciaInCaricoPraticaModulisticaHandler handler) {
		handler.onRilasciaInCaricoPraticaModulistica(this);
	}

	@Override
	public Type<RilasciaInCaricoPraticaModulisticaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RilasciaInCaricoPraticaModulisticaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String idPecIn) {
		source.fireEvent(new RilasciaInCaricoPraticaModulisticaEvent(idPecIn));
	}
}
