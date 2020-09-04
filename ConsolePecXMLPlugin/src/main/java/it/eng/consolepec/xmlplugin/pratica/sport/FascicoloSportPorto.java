package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

public interface FascicoloSportPorto extends Fascicolo{

	@Override
	public DatiFascicoloSportPorto getDati();
}
