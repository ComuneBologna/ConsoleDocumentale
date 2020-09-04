package it.eng.portlet.consolepec.gwt.shared.action.cartellafirma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaWizardApiClient.OperazioneWizardTaskFirma;
import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoRispostaParereDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FineWizardTaskFirmaAction extends LiferayPortletUnsecureActionImpl<FineWizardTaskFirmaResult> {

	private Map<String, List<AllegatoDTO>> praticaAllegatiMap;
	private OperazioneWizardTaskFirma operazioneWizard;

	private InformazioniFirmaDigitaleTaskFirmaDTO infoFirmaDigitale;
	private InformazioniNotificaTaskFirmaDTO infoNotifica;
	private TipoRispostaParereDTO tipoRisposta;
	private String ruolo;
	private List<FileDTO> fileDaAllegare = new ArrayList<FileDTO>();
	private Map<String, List<Integer>> praticaIdTaskMap = new HashMap<String, List<Integer>>();
	private String motivazione;

	public FineWizardTaskFirmaAction(Map<String, List<AllegatoDTO>> praticaAllegatiMap, OperazioneWizardTaskFirma operazioneWizardTaskFirma) {
		this.praticaAllegatiMap = praticaAllegatiMap;
		this.operazioneWizard = operazioneWizardTaskFirma;
	}

	protected FineWizardTaskFirmaAction() {
		// per la serializzazione
	}
}
