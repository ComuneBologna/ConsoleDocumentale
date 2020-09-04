package it.eng.consolepec.xmlplugin.pratica.personale;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloPersonale extends XMLFascicolo implements FascicoloPersonale {

	public XMLFascicoloPersonale() {
	}

	@Override
	public DatiFascicoloPersonale getDati() {
		return (DatiFascicoloPersonale) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloPersonale.Builder();
	}
}
