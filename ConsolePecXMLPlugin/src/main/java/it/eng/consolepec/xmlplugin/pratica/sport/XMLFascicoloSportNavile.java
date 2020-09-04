package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportNavile extends XMLFascicolo implements FascicoloSportNavile {

	public XMLFascicoloSportNavile() {
	}

	@Override
	public DatiFascicoloSportNavile getDati() {
		return (DatiFascicoloSportNavile) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportNavile.Builder();
	}
}