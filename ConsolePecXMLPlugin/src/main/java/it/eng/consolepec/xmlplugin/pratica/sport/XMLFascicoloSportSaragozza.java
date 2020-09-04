package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportSaragozza extends XMLFascicolo implements FascicoloSportSaragozza {

	public XMLFascicoloSportSaragozza() {
	}

	@Override
	public DatiFascicoloSportSaragozza getDati() {
		return (DatiFascicoloSportSaragozza) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportSaragozza.Builder();
	}
}