package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportSanDonatoSanVitale extends XMLFascicolo implements FascicoloSportSanDonatoSanVitale {

	public XMLFascicoloSportSanDonatoSanVitale() {
	}

	@Override
	public DatiFascicoloSportSanDonatoSanVitale getDati() {
		return (DatiFascicoloSportSanDonatoSanVitale) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportSanDonatoSanVitale.Builder();
	}
}