package it.eng.portlet.consolepec.gwt.client.event.dettaglioallegato;

import it.eng.portlet.consolepec.gwt.client.event.OpeningEvent;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class DettaglioAllegatoInizioEvent extends
		GwtEvent<DettaglioAllegatoInizioEvent.DettaglioAllegatoInizioHandler> implements OpeningEvent{

	public static Type<DettaglioAllegatoInizioHandler> TYPE = new Type<DettaglioAllegatoInizioHandler>();

	private Object openingRequestor;
	private String pathPraticaAllegato;
	private AllegatoDTO allegato;

	public interface DettaglioAllegatoInizioHandler extends EventHandler {
		void onStartDettaglioAllegato(DettaglioAllegatoInizioEvent event);
	}

	public DettaglioAllegatoInizioEvent(AllegatoDTO allegato, String pathPraticaAllegato, Object openingRequestor) {
		this.allegato = allegato;
		this.pathPraticaAllegato = pathPraticaAllegato;
		this.openingRequestor = openingRequestor;
	}

	@Override
	protected void dispatch(DettaglioAllegatoInizioHandler handler) {
		handler.onStartDettaglioAllegato(this);
	}

	@Override
	public Type<DettaglioAllegatoInizioHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DettaglioAllegatoInizioHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, AllegatoDTO allegato, String pathPraticaAllegato, Object openingRequestor) {
		source.fireEvent(new DettaglioAllegatoInizioEvent(allegato, pathPraticaAllegato, openingRequestor));
	}

	@Override
	public Object getOpeningRequestor() {
		return openingRequestor;
	}


	public String getPathPraticaAllegato() {
		return pathPraticaAllegato;
	}

	public void setPathPraticaAllegato(String pathPraticaAllegato) {
		this.pathPraticaAllegato = pathPraticaAllegato;
	}

	public AllegatoDTO getAllegato() {
		return allegato;
	}

	public void setAllegato(AllegatoDTO allegato) {
		this.allegato = allegato;
	}
}
