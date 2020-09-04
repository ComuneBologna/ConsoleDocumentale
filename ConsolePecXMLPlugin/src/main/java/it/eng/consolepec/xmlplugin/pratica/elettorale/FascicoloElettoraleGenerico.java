package it.eng.consolepec.xmlplugin.pratica.elettorale;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

public interface FascicoloElettoraleGenerico extends Fascicolo {
	@Override
	public DatiFascicoloElettoraleGenerico getDati();
}
