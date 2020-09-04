package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportSavena extends XMLFascicolo implements FascicoloSportSavena {

	public XMLFascicoloSportSavena() {
	}

	@Override
	public DatiFascicoloSportSavena getDati() {
		return (DatiFascicoloSportSavena) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportSavena.Builder();
	}
}