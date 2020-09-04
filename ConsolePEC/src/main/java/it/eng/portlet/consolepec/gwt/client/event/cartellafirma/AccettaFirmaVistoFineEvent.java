package it.eng.portlet.consolepec.gwt.client.event.cartellafirma;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;

/**
 *
 * @author biagiot
 *
 */
public class AccettaFirmaVistoFineEvent extends GwtEvent<AccettaFirmaVistoFineEvent.AccettaFirmaVistoFineHandler> {

	public static Type<AccettaFirmaVistoFineHandler> TYPE = new Type<AccettaFirmaVistoFineEvent.AccettaFirmaVistoFineHandler>();

	private boolean isRiassegna;
	private boolean isAnnulla;
	private String ruolo;
	private List<FileDTO> allegati;
	private String motivazione;

	public interface AccettaFirmaVistoFineHandler extends EventHandler {
		void onAccettaFirmaVistoFine(AccettaFirmaVistoFineEvent event);
	}

	public AccettaFirmaVistoFineEvent(String ruolo, boolean isRiassegna, List<FileDTO> allegati, String motivazione) {
		this.ruolo = ruolo;
		this.isAnnulla = false;
		this.isRiassegna = isRiassegna;
		this.allegati = allegati;
		this.motivazione = motivazione;
	}

	public AccettaFirmaVistoFineEvent() {
		this.isAnnulla = true;
	}

	@Override
	public Type<AccettaFirmaVistoFineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AccettaFirmaVistoFineHandler> getType() {
		return TYPE;

	}

	@Override
	protected void dispatch(AccettaFirmaVistoFineHandler handler) {
		handler.onAccettaFirmaVistoFine(this);
	}

	public static void fire(HasHandlers source, String ruolo, boolean isRiassegna, List<FileDTO> allegati, String motivazione) {
		source.fireEvent(new AccettaFirmaVistoFineEvent(ruolo, isRiassegna, allegati, motivazione));
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new AccettaFirmaVistoFineEvent());
	}

	public boolean isRiassegna() {
		return isRiassegna;
	}

	public boolean isAnnulla() {
		return isAnnulla;
	}

	public String getRuolo() {
		return ruolo;
	}

	public List<FileDTO> getAllegati() {
		return allegati;
	}

	public String getMotivazione() {
		return motivazione;
	}

}
