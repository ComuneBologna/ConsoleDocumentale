package it.eng.consolepec.xmlplugin.pratica.riservato;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloRiservato extends XMLFascicolo implements FascicoloRiservato {

	public XMLFascicoloRiservato() {
	}
	
	@Override
	public DatiFascicoloRiservato getDati() {
		return (DatiFascicoloRiservato) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloRiservato.Builder();
	}
}
