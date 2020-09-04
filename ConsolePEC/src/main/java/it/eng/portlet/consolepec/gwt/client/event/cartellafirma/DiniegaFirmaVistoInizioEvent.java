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
public class DiniegaFirmaVistoInizioEvent extends GwtEvent<DiniegaFirmaVistoInizioEvent.DiniegaFirmaVistoInizioHandler> {

	public static Type<DiniegaFirmaVistoInizioHandler> TYPE = new Type<DiniegaFirmaVistoInizioHandler>();
	private Set<DocumentoFirmaVistoDTO> documentiFirmaVisto;
	private OperazioneWizardTaskFirma operazioneRichiesta;
	private Boolean riassegnaEnabled;
	private Set<String> ruoliAbilitati;
	private List<FileDTO> fileDaAllegare;
	private boolean taskRuoli;
	private String motivazione;

	public interface DiniegaFirmaVistoInizioHandler extends EventHandler {
		void onDiniegaPropostaFirmaVisto(DiniegaFirmaVistoInizioEvent event);
	}

	public DiniegaFirmaVistoInizioEvent(Set<DocumentoFirmaVistoDTO> documentiFirmaVisto, OperazioneWizardTaskFirma tipoRichiesta, Boolean riassegnaEnabled, Set<String> ruoliAbilitati,
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
	public Type<DiniegaFirmaVistoInizioHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DiniegaFirmaVistoInizioHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DiniegaFirmaVistoInizioHandler handler) {
		handler.onDiniegaPropostaFirmaVisto(this);
	}

	public Set<DocumentoFirmaVistoDTO> getDocumentiFirmaVisto() {
		return documentiFirmaVisto;
	}

	public OperazioneWizardTaskFirma getOperazioneRichiesta() {
		return operazioneRichiesta;
	}

	public static void fire(HasHandlers source, Set<DocumentoFirmaVistoDTO> documentiFirmaVisto, OperazioneWizardTaskFirma tipoRichiesta, Boolean riassegnaEnabled, Set<String> ruoliAbilitati,
			List<FileDTO> fileDaAllegare, String motivazione) {
		source.fireEvent(new DiniegaFirmaVistoInizioEvent(documentiFirmaVisto, tipoRichiesta, riassegnaEnabled, ruoliAbilitati, fileDaAllegare, motivazione));
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
