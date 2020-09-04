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
public class DiniegaFirmaVistoFineEvent extends GwtEvent<DiniegaFirmaVistoFineEvent.DiniegaFirmaVistoFineHandler> {

	public static Type<DiniegaFirmaVistoFineHandler> TYPE = new Type<DiniegaFirmaVistoFineHandler>();

	private boolean isRiassegna;
	private boolean isAnnulla;
	private String ruolo;
	private List<FileDTO> allegati;
	private String motivazione;

	public interface DiniegaFirmaVistoFineHandler extends EventHandler {
		void onDiniegaFirmaVistoFine(DiniegaFirmaVistoFineEvent event);
	}

	public DiniegaFirmaVistoFineEvent() {
		this.isAnnulla = true;
	}

	public DiniegaFirmaVistoFineEvent(String ruolo, boolean isRiassegna, List<FileDTO> allegati, String motivazione) {
		this.ruolo = ruolo;
		this.isRiassegna = isRiassegna;
		this.isAnnulla = false;
		this.allegati = allegati;
		this.motivazione = motivazione;
	}

	@Override
	public Type<DiniegaFirmaVistoFineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DiniegaFirmaVistoFineHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DiniegaFirmaVistoFineHandler handler) {
		handler.onDiniegaFirmaVistoFine(this);
	}

	public static void fire(HasHandlers source, String ruolo, boolean isRiassegna, List<FileDTO> allegati, String motivazione) {
		source.fireEvent(new DiniegaFirmaVistoFineEvent(ruolo, isRiassegna, allegati, motivazione));
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new DiniegaFirmaVistoFineEvent());
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
