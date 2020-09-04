package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportPortoSaragozza extends XMLFascicolo implements FascicoloSportPortoSaragozza {

	public XMLFascicoloSportPortoSaragozza() {
	}

	@Override
	public DatiFascicoloSportPortoSaragozza getDati() {
		return (DatiFascicoloSportPortoSaragozza) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportPortoSaragozza.Builder();
	}
}