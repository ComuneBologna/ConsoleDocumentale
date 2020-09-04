package it.eng.portlet.consolepec.gwt.client.event.cartellafirma;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoRispostaParereDTO;

/**
 *
 * @author biagiot
 *
 */
public class RispondiParereFineEvent extends GwtEvent<RispondiParereFineEvent.RispondiParereFineHandler> {

	public static Type<RispondiParereFineHandler> TYPE = new Type<RispondiParereFineHandler>();

	private boolean isRiassegna;
	private boolean isAnnulla;
	private TipoRispostaParereDTO tipoRisposta;
	private String ruolo;
	private List<FileDTO> allegati;
	private String motivazione;

	public interface RispondiParereFineHandler extends EventHandler {
		void onRispondiParereFine(RispondiParereFineEvent event);
	}

	public RispondiParereFineEvent() {
		this.ruolo = null;
		this.isAnnulla = true;
		this.isRiassegna = false;
		this.tipoRisposta = null;
	}

	public RispondiParereFineEvent(String ruolo, TipoRispostaParereDTO tipoRisposta, boolean isRiassegna, List<FileDTO> allegati, String motivazione) {
		this.ruolo = ruolo;
		this.tipoRisposta = tipoRisposta;
		this.isRiassegna = isRiassegna;
		this.isAnnulla = false;
		this.allegati = allegati;
		this.motivazione = motivazione;
	}

	@Override
	protected void dispatch(RispondiParereFineHandler handler) {
		handler.onRispondiParereFine(this);
	}

	@Override
	public Type<RispondiParereFineHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RispondiParereFineHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new RispondiParereFineEvent());
	}

	public static void fire(HasHandlers source, String ruolo, TipoRispostaParereDTO tipoRisposta, boolean isRiassegna, List<FileDTO> allegati, String motivazione) {
		source.fireEvent(new RispondiParereFineEvent(ruolo, tipoRisposta, isRiassegna, allegati, motivazione));
	}

	public boolean isRiassegna() {
		return isRiassegna;
	}

	public boolean isAnnulla() {
		return isAnnulla;
	}

	public TipoRispostaParereDTO getTipoRisposta() {
		return tipoRisposta;
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