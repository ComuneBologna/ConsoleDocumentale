package it.eng.consolepec.xmlplugin.pratica.elettorale;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

public interface FascicoloElettoraleComunicazioni extends Fascicolo {
	@Override
	public DatiFascicoloElettoraleComunicazioni getDati();
}
