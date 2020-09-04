package it.eng.portlet.consolepec.gwt.client.presenter.pec.event;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ChiudiCaricaAllegatiDaPraticaEvent
	extends GwtEvent<ChiudiCaricaAllegatiDaPraticaEvent.ChiudiCaricaAllegatiDaPraticaHandler> {

	public static Type<ChiudiCaricaAllegatiDaPraticaHandler> TYPE = new Type<ChiudiCaricaAllegatiDaPraticaHandler>();

	public interface ChiudiCaricaAllegatiDaPraticaHandler extends EventHandler {
	
		void onChiudiCaricaAllegatiDaPratica(ChiudiCaricaAllegatiDaPraticaEvent event);
	}

	private String idFascicolo;
	private String clientIdBozza;
	private Map<String, List<AllegatoDTO>> allegati = new HashMap<String, List<AllegatoDTO>>();
		
	public ChiudiCaricaAllegatiDaPraticaEvent(String idFascicolo, String clientIdBozza) {
		this.idFascicolo = idFascicolo;
		this.clientIdBozza = clientIdBozza;
	}

	@Override
	protected void dispatch(ChiudiCaricaAllegatiDaPraticaHandler handler) {
	
		handler.onChiudiCaricaAllegatiDaPratica(this);
	}

	@Override
	public Type<ChiudiCaricaAllegatiDaPraticaHandler> getAssociatedType() {
	
		return TYPE;
	}

	public static Type<ChiudiCaricaAllegatiDaPraticaHandler> getType() {
	
		return TYPE;
	}

	public static void fire(HasHandlers source, String idFascicolo, String clientIdBozza) {

		source.fireEvent(new ChiudiCaricaAllegatiDaPraticaEvent(idFascicolo, clientIdBozza));
	}
	
	public String getIdFascicolo() {
		return idFascicolo;
	}

	public String getClientIdBozza() {
		return clientIdBozza;
	}

	public Map<String, List<AllegatoDTO>> getAllegati() {
		return allegati;
	}

	
}
