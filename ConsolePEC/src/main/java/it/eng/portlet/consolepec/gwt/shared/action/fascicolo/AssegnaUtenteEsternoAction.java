package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.List;

public class AssegnaUtenteEsternoAction extends LiferayPortletUnsecureActionImpl<AssegnaUtenteEsternoResult> {

	private String praticaPath;
	private List<String> destinatari;
	private String testoEmail;
	private List<String> operazioni;
	
	public AssegnaUtenteEsternoAction() {
	}

	public AssegnaUtenteEsternoAction(String praticaPath, List<String> destinatari, String testoEmail, List<String> operazioni) {
		super();
		this.praticaPath = praticaPath;
		this.destinatari = destinatari;
		this.testoEmail = testoEmail;
		this.operazioni = operazioni;
	}

	public String getPraticaPath() {
		return praticaPath;
	}

	public List<String> getDestinatari() {
		return destinatari;
	}

	public String getTestoEmail() {
		return testoEmail;
	}

	public List<String> getOperazioni() {
		return operazioni;
	}
	
	
}
