package it.eng.portlet.consolepec.gwt.client.event.cartellafirma;

import java.util.List;
import java.util.Set;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient.OperazioneWizardTaskFirma;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;

/**
 *
 * @author biagiot
 *
 */
public class AccettaFirmaVistoInizioEvent extends GwtEvent<AccettaFirmaVistoInizioEvent.AccettaFirmaVistoInizioHandler> {

	public static Type<AccettaFirmaVistoInizioHandler> TYPE = new Type<AccettaFirmaVistoInizioEvent.AccettaFirmaVistoInizioHandler>();
	private Set<DocumentoFirmaVistoDTO> documentiFirmaVisto;
	private OperazioneWizardTaskFirma operazioneRichiesta;
	private Boolean riassegnaEnabled;
	private List<FileDTO> fileDaAllegare;
	private boolean taskRuoli;
	private Set<String> ruoliAbilitati;
	private String motivazione;

	public interface AccettaFirmaVistoInizioHandler extends EventHandler {
		void onAccettaPropostaFirmaVisto(AccettaFirmaVistoInizioEvent event);
	}

	public AccettaFirmaVistoInizioEvent(Set<DocumentoFirmaVistoDTO> documentiFirmaVisto, OperazioneWizardTaskFirma tipoRichiesta, Boolean riassegnaEnabled, Set<String> ruoliAbilitati,
			List<FileDTO> fileDaAllegare, String motivazione) {
		this.documentiFirmaVisto = documentiFirmaVisto;
		this.operazioneRichiesta = tipoRichiesta;
		this.riassegnaEnabled = riassegnaEnabled;
		this.ruoliAbilitati = ruoliAbilitati;
		this.taskRuoli = ruoliAbilitati != null;
		this.fileDaAllegare = fileDaAllegare;
		this.motivazione = motivazione;
	}

	@Override
	public Type<AccettaFirmaVistoInizioHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<AccettaFirmaVistoInizioHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AccettaFirmaVistoInizioHandler handler) {
		handler.onAccettaPropostaFirmaVisto(this);
	}

	public Set<DocumentoFirmaVistoDTO> getDocumentiFirmaVisto() {
		return documentiFirmaVisto;
	}

	public OperazioneWizardTaskFirma getOperazioneRichiesta() {
		return operazioneRichiesta;
	}

	public static void fire(HasHandlers source, Set<DocumentoFirmaVistoDTO> documentiFirmaVisto, OperazioneWizardTaskFirma tipoRichiesta, Boolean riassegnaEnabled, Set<String> ruoliAbilitati,
			List<FileDTO> fileDaAllegare, String motivazione) {
		source.fireEvent(new AccettaFirmaVistoInizioEvent(documentiFirmaVisto, tipoRichiesta, riassegnaEnabled, ruoliAbilitati, fileDaAllegare, motivazione));
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
