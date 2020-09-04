package it.eng.portlet.consolepec.gwt.shared.action;


public class RiversamentoCartaceo extends Stampa<RiversamentoCartaceoResult> {

	private String fascicoloPath;
	private String annoPG;
	private String numeroPG;

	public RiversamentoCartaceo(){
		
	}
	
	public RiversamentoCartaceo(String fascicoloPath, String annoPG, String numeroPG) {
		super();
		this.fascicoloPath = fascicoloPath;
		this.annoPG = annoPG;
		this.numeroPG = numeroPG;
	}

	public String getFascicoloPath() {
		return fascicoloPath;
	}

	public String getAnnoPG() {
		return annoPG;
	}

	public String getNumeroPG() {
		return numeroPG;
	}
}
