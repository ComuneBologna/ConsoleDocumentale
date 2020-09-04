package it.eng.portlet.consolepec.gwt.shared.action;

import it.eng.portlet.consolepec.gwt.shared.dto.Element;
import it.eng.portlet.consolepec.gwt.shared.model.DatiDefaultProtocollazione;

import java.util.Map;

public class ProtocollaAction extends LiferayPortletUnsecureActionImpl<ProtocollaResult> {

	private String idFascicolo;
	private Map<String, Map<String, Element>> valueMap;
	private DatiDefaultProtocollazione datiPerProtocollazione;
	private String tipoPratica;

	@SuppressWarnings("unused")
	private ProtocollaAction() {
		// For serialization only
	}

	public ProtocollaAction(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public void setValueMap(Map<String, Map<String, Element>> map) {
		this.valueMap = map;
	}

	public Map<String, Map<String, Element>> getValueMap() {
		return this.valueMap;
	}

	public void setDatiPerProtocollazione(DatiDefaultProtocollazione datiPerProtocollazione) {
		this.datiPerProtocollazione = datiPerProtocollazione;
	}

	public DatiDefaultProtocollazione getDatiPerProtocollazione() {
		return this.datiPerProtocollazione;
	}

	public String getIdFascicolo() {
		return idFascicolo;
	}

	public void setIdFascicolo(String idFascicolo) {
		this.idFascicolo = idFascicolo;
	}

	public String getTipoPratica() {
		return tipoPratica;
	}

	public void setTipoPratica(String tipoPratica) {
		this.tipoPratica = tipoPratica;
	}

}
