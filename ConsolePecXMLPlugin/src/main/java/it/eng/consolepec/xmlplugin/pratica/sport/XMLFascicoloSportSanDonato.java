package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportSanDonato extends XMLFascicolo implements FascicoloSportSanDonato {

	public XMLFascicoloSportSanDonato() {
	}

	@Override
	public DatiFascicoloSportSanDonato getDati() {
		return (DatiFascicoloSportSanDonato) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportSanDonato.Builder();
	}
}