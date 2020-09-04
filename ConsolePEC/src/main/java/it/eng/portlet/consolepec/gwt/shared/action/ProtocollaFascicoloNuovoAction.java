package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.action.fascicolo.CreaFascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.dto.Element;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;

import java.util.Map;

public class ProtocollaFascicoloNuovoAction extends LiferayPortletUnsecureActionImpl<ProtocollaFascicoloNuovoActionResult> {

	private Map<String, Map<String, Element>> valueMap;
	private DatiDefaultProtocollazione datiPerProtocollazione;
	private CreaFascicoloDTO creaFascicoloDTO;

	public ProtocollaFascicoloNuovoAction() {
	}

	public Map<String, Map<String, Element>> getValueMap() {
		return valueMap;
	}

	public void setValueMap(Map<String, Map<String, Element>> valueMap) {
		this.valueMap = valueMap;
	}

	public DatiDefaultProtocollazione getDatiPerProtocollazione() {
		return datiPerProtocollazione;
	}

	public void setDatiPerProtocollazione(DatiDefaultProtocollazione datiPerProtocollazione) {
		this.datiPerProtocollazione = datiPerProtocollazione;
	}

	public CreaFascicoloDTO getCreaFascicoloDTO() {
		return creaFascicoloDTO;
	}

	public void setCreaFascicoloDTO(CreaFascicoloDTO creaFascicoloDTO) {
		this.creaFascicoloDTO = creaFascicoloDTO;
	}
}
