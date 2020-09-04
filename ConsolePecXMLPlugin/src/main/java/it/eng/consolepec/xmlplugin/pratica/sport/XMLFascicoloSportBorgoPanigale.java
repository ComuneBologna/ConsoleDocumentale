package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportBorgoPanigale extends XMLFascicolo implements FascicoloSportBorgoPanigale {

	public XMLFascicoloSportBorgoPanigale() {
	}

	@Override
	public DatiFascicoloSportBorgoPanigale getDati() {
		return (DatiFascicoloSportBorgoPanigale) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportBorgoPanigale.Builder();
	}
}