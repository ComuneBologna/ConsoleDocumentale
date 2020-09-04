package it.eng.consolepec.xmlplugin.pratica.fatturazione;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

public interface FascicoloFatturazione extends Fascicolo {

	@Override
	public DatiFascicoloFatturazione getDati();

}
