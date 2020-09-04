package it.eng.portlet.consolepec.gwt.client.presenter.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class GoToAssegnaFromDettaglioPraticaModulisticaEvent extends GwtEvent<GoToAssegnaFromDettaglioPraticaModulisticaEvent.GoToAssegnaFromDettaglioPraticaModulisticaHandler> {

	public static Type<GoToAssegnaFromDettaglioPraticaModulisticaHandler> TYPE = new Type<GoToAssegnaFromDettaglioPraticaModulisticaHandler>();

	public interface GoToAssegnaFromDettaglioPraticaModulisticaHandler extends EventHandler {
		void onGoToAssegnaFromDettaglioPraticaModulistica(GoToAssegnaFromDettaglioPraticaModulisticaEvent event);
	}

	private String praticaModulisticaId;

	public GoToAssegnaFromDettaglioPraticaModulisticaEvent(String praticaModulisticaId) {
		this.praticaModulisticaId = praticaModulisticaId;
	}

	@Override
	protected void dispatch(GoToAssegnaFromDettaglioPraticaModulisticaHandler handler) {
		handler.onGoToAssegnaFromDettaglioPraticaModulistica(this);
	}

	@Override
	public Type<GoToAssegnaFromDettaglioPraticaModulisticaHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<GoToAssegnaFromDettaglioPraticaModulisticaHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String praticaModulisticaId) {
		source.fireEvent(new GoToAssegnaFromDettaglioPraticaModulisticaEvent(praticaModulisticaId));
	}

	public String getPraticaModulisticaId() {
		return praticaModulisticaId;
	}
}
