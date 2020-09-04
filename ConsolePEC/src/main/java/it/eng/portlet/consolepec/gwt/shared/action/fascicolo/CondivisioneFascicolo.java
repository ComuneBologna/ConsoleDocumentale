package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import it.eng.portlet.consolepec.gwt.shared.action.LiferayPortletUnsecureActionImpl;

import java.util.ArrayList;
import java.util.List;

public class CondivisioneFascicolo extends LiferayPortletUnsecureActionImpl<CondivisioneFascicoloResult> {
	
	public static String SHARE_MERGE = "merge";
	public static String SHARE_DELETE = "delete";

	private String share;
	private String nomeGruppo; 
	private String pathFascicolo; 
	private List<String> operazioniCondivise = new ArrayList<String>();
	
	
	@SuppressWarnings("unused")
	private CondivisioneFascicolo() {
		// For serialization only
	}
	
	public CondivisioneFascicolo(String share, String nomeGruppo,String pathFascicolo, List<String> operazioniCondivise){
		this.share = share;
		this.nomeGruppo = nomeGruppo; 
		this.pathFascicolo = pathFascicolo; 
		this.operazioniCondivise.addAll(operazioniCondivise);
	}

	public String getShare() {
		return share;
	}

	public String getNomeGruppo() {
		return nomeGruppo;
	}

	public String getPathFascicolo() {
		return pathFascicolo;
	}

	public List<String> getOperazioniCondivise() {
		return operazioniCondivise;
	}

}
