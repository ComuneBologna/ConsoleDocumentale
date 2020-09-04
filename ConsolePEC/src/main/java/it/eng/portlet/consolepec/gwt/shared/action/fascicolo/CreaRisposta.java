package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;


public class CreaRisposta extends LiferayPortletUnsecureActionImpl<CreaRispostaResult> {

	private String pathFascicolo;
	private String idMailSelezionata;
	private boolean interoperabile;
	
	public CreaRisposta(){
		
	}
	
	public CreaRisposta(String path){
		this.pathFascicolo = path;
	}

	public CreaRisposta(String fascicoloPath, String idMailSelezionata) {
		this( fascicoloPath );
		this.idMailSelezionata = idMailSelezionata;
	}

	public String getPathFascicolo() {
		return pathFascicolo;
	}

	public void setPathFascicolo(String pathFascicolo) {
		this.pathFascicolo = pathFascicolo;
	}

	
	public String getIdMailSelezionata() {
		return idMailSelezionata;
	}
	
	public void setIdMailSelezionata(String idMailSelezionata) {
		this.idMailSelezionata = idMailSelezionata;
	}

	public boolean isInteroperabile() {
		return interoperabile;
	}

	public void setInteroperabile(boolean interoperabile) {
		this.interoperabile = interoperabile;
	}
	
}
