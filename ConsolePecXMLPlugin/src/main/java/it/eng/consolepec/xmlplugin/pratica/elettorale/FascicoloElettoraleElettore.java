package it.eng.consolepec.xmlplugin.pratica.elettorale;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

public interface FascicoloElettoraleElettore extends Fascicolo {
	@Override
	public DatiFascicoloElettoraleElettore getDati();
}
