package it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma;

import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TmpFileUploadDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.DocumentoFirmaVistoDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Espone i metodi per l'intera gestione degli eventi di approvazione / diniego di documenti del task di firma;
 *
 * @author biagiot
 *
 */
public interface CartellaFirmaWizardApiClient {

	void startWizardTaskFirma(Set<DocumentoFirmaVistoDTO> documentiFirmaVisto, OperazioneWizardTaskFirma operazione);
	void ritiraDocumenti(Map<String, List<AllegatoDTO>> praticaAllegatiMap, boolean fromDettaglioFascicolo);
	void evadi(Set<DocumentoFirmaVistoDTO> documentiFirmaVisto);
	
	boolean operazioniDestinariAbilitate(Set<DocumentoFirmaVistoDTO> documenti);
	boolean operazioniDestinatarioGruppoAbilitate(Set<DocumentoFirmaVistoDTO> documenti);
	boolean checkDestinatariGruppo(Set<DocumentoFirmaVistoDTO> documenti);
	boolean isOperazioneAbilitata(Set<DocumentoFirmaVistoDTO> documenti);

	void getFileDaAllegare(List<TmpFileUploadDTO> tmpFilesUploadDTO, List<FileDTO> allegati, UploadAllegatiCallback callback);
	void eliminaFileDaAllegare(String pathFile);

	
	public interface UploadAllegatiCallback {
		void onAllegatiUploaded(List<FileDTO> files);
		void onError(String message);
	}

	interface OperazioneDestinatarioGruppoAbilitataResult {
		void onComplete(Boolean enabled, Set<String> ruoliAbilitati);
	}

	public enum OperazioneWizardTaskFirma {
		FIRMA(false),
		VISTO(false),
		DINIEGO(true),
		RITIRO(true),
		RISPOSTA_PARERE(false),
		EVADI(true);

		boolean operazioneConclusiva;

		public boolean isOperazioneConclusiva() {
			return operazioneConclusiva;
		}

		OperazioneWizardTaskFirma(boolean operazioneConclusiva) {
			this.operazioneConclusiva = operazioneConclusiva;
		}
	}
}