package it.eng.consolepec.xmlplugin.pratica.personale;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

public interface FascicoloPersonale extends Fascicolo {
	@Override
	public DatiFascicoloPersonale getDati();
}
