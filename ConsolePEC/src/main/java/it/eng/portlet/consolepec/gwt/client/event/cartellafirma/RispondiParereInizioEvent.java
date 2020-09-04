package it.eng.portlet.consolepec.gwt.client.event.cartellafirma;

import java.util.List;
import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;

/**
 *
 * @author biagiot
 *
 */
public class RispondiParereInizioEvent extends GwtEvent<RispondiParereInizioEvent.RispondiParereInizioHandler> {

	public static Type<RispondiParereInizioHandler> TYPE = new Type<RispondiParereInizioHandler>();
	private Set<DocumentoFirmaVistoDTO> documentiFirmaVisto;
	private Boolean riassegnaEnabled;
	private List<FileDTO> fileDaAllegare;
	private boolean taskRuoli;
	private Set<String> ruoliAbilitati;
	private String motivazione;

	public interface RispondiParereInizioHandler extends EventHandler {
		void onRispondiParereInizio(RispondiParereInizioEvent event);
	}

	public RispondiParereInizioEvent(Set<DocumentoFirmaVistoDTO> documentiFirmaVisto, Boolean riassegnaEnabled, Set<String> ruoliAbilitati, List<FileDTO> fileDaAllegare, String motivazione) {
		this.documentiFirmaVisto = documentiFirmaVisto;
		this.riassegnaEnabled = riassegnaEnabled;
		this.ruoliAbilitati = ruoliAbilitati;
		this.taskRuoli = ruoliAbilitati != null;
		this.fileDaAllegare = fileDaAllegare;
		this.motivazione = motivazione;
	}

	@Override
	protected void dispatch(RispondiParereInizioHandler handler) {
		handler.onRispondiParereInizio(this);
	}

	@Override
	public Type<RispondiParereInizioHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<RispondiParereInizioHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Set<DocumentoFirmaVistoDTO> documentiFirmaVisto, Boolean riassegnaEnabled, Set<String> ruoliAbilitati, List<FileDTO> fileDaAllegare,
			String motivazione) {
		source.fireEvent(new RispondiParereInizioEvent(documentiFirmaVisto, riassegnaEnabled, ruoliAbilitati, fileDaAllegare, motivazione));
	}

	public Set<DocumentoFirmaVistoDTO> getDocumentiFirmaVisto() {
		return documentiFirmaVisto;
	}

	public Boolean isRiassegnaEnabled() {
		return riassegnaEnabled;
	}

	public boolean isTaskRuoli() {
		return taskRuoli;
	}

	public Set<String> getRuoliAbilitati() {
		return ruoliAbilitati;
	}

	public List<FileDTO> getFileDaAllegare() {
		return fileDaAllegare;
	}

	public String getMotivazione() {
		return motivazione;
	}
}
