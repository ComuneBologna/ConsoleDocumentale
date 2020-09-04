package it.eng.portlet.consolepec.gwt.shared.action.fascicolo;

import java.util.Date;

import com.gwtplatform.dispatch.shared.Result;

public interface GestioneProcedimentoResult extends Result {
	
	public abstract int getCodiceProcedimento();

	public abstract String getDescrizioneProcedimento();
		
	public abstract Date getDataInizioProcedimento();
		
	public abstract Date getDataChiusuraProcedimento();
		
	public abstract Integer getTempoNormatoInGiorni();

	public abstract Integer getDurataProcedimento();


}
