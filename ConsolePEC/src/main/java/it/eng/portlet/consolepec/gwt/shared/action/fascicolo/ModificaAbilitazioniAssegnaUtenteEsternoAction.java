package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.List;

public class ModificaAbilitazioniAssegnaUtenteEsternoAction extends LiferayPortletUnsecureActionImpl<ModificaAbilitazioniAssegnaUtenteEsternoResult> {

	private String praticaPath;
	private List<String> operazioni;
	
	public ModificaAbilitazioniAssegnaUtenteEsternoAction() {
	}

	public ModificaAbilitazioniAssegnaUtenteEsternoAction(String praticaPath, List<String> operazioni) {
		super();
		this.praticaPath = praticaPath;
		this.operazioni = operazioni;
	}

	public String getPraticaPath() {
		return praticaPath;
	}

	public List<String> getOperazioni() {
		return operazioni;
	}
	
	
}
