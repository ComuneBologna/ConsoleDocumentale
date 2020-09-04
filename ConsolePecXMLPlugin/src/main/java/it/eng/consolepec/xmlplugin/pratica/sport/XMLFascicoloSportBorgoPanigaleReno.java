package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportBorgoPanigaleReno extends XMLFascicolo implements FascicoloSportBorgoPanigaleReno {

	public XMLFascicoloSportBorgoPanigaleReno() {
	}


	@Override
	public DatiFascicoloSportBorgoPanigaleReno getDati() {
		return (DatiFascicoloSportBorgoPanigaleReno) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportBorgoPanigaleReno.Builder();
	}
}