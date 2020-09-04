package it.eng.portlet.consolepec.gwt.client.event;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class MostraDettaglioAllegatoEvent extends GwtEvent<MostraDettaglioAllegatoEvent.MostraDettaglioAllegatoHandler> {

	public static Type<MostraDettaglioAllegatoHandler> TYPE = new Type<MostraDettaglioAllegatoHandler>();
	private String pathPraticaAllegato;
	private String pathPraticaRitornoMaschera; // può essere differente dalla pratica dell'allegato(dettaglio dell'allegato di una mail dalla maschera del dettagli fascicolo)
	private AllegatoDTO allegato;

	public interface MostraDettaglioAllegatoHandler extends EventHandler {
		void onMostraDettaglioAllegato(MostraDettaglioAllegatoEvent event);
	}

	public MostraDettaglioAllegatoEvent(String pathPraticaAllegato, String pathPraticaRitornoMaschera, AllegatoDTO allegato) {
		this.pathPraticaAllegato = pathPraticaAllegato;
		this.pathPraticaRitornoMaschera = pathPraticaRitornoMaschera;
		this.allegato = allegato;
	}

	
	public String getPathPraticaAllegato() {
		return pathPraticaAllegato;
	}


	public String getPathPraticaRitornoMaschera() {
		return pathPraticaRitornoMaschera;
	}

	public AllegatoDTO getAllegato() {
		return allegato;
	}

	@Override
	protected void dispatch(MostraDettaglioAllegatoHandler handler) {
		handler.onMostraDettaglioAllegato(this);
	}

	@Override
	public Type<MostraDettaglioAllegatoHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MostraDettaglioAllegatoHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String pathPraticaAllegato, String pathPraticaRitornoMaschera, AllegatoDTO allegato) {
		source.fireEvent(new MostraDettaglioAllegatoEvent(pathPraticaAllegato, pathPraticaRitornoMaschera, allegato));
	}
	
	public static void fire(HasHandlers source, String pathPraticaAllegato, AllegatoDTO allegato) {
		source.fireEvent(new MostraDettaglioAllegatoEvent(pathPraticaAllegato, pathPraticaAllegato, allegato));
	}
}
