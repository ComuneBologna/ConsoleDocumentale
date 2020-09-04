package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportSantoStefano extends XMLFascicolo implements FascicoloSportSantoStefano {

	public XMLFascicoloSportSantoStefano() {
	}

	@Override
	public DatiFascicoloSportSantoStefano getDati() {
		return (DatiFascicoloSportSantoStefano) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportSantoStefano.Builder();
	}
}