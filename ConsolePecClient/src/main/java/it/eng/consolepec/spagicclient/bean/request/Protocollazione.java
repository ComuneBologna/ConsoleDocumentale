package it.eng.consolepec.spagicclient.bean.request;

public class Protocollazione {
	private String numPG;
	private Integer annoPG;
	public Protocollazione(String numPG, Integer annoPG) {
		super();
		this.numPG = numPG;
		this.annoPG = annoPG;
	}
	public String getNumPG() {
		return numPG;
	}
	public Integer getAnnoPG() {
		return annoPG;
	}
	
	
}
