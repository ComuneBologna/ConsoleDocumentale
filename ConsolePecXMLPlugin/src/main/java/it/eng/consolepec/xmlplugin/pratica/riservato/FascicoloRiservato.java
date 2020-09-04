package it.eng.consolepec.xmlplugin.pratica.riservato;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

public interface FascicoloRiservato extends Fascicolo {
	
	@Override
	public DatiFascicoloRiservato getDati();

}
