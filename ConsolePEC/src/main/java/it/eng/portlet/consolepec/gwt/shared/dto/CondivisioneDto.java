package it.eng.portlet.consolepec.gwt.shared.dto;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CondivisioneDto implements IsSerializable{

	private AnagraficaRuolo ruolo;
	private List<String> operazioni = new ArrayList<String>();

	public AnagraficaRuolo getRuolo() {
		return ruolo;
	}

	public void setRuolo(AnagraficaRuolo ruolo) {
		this.ruolo = ruolo;
	}


	public List<String> getOperazioni() {
		return operazioni;
	}

}
