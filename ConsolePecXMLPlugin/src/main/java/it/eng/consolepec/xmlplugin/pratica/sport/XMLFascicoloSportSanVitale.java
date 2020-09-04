package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportSanVitale extends XMLFascicolo implements FascicoloSportSanVitale {

	public XMLFascicoloSportSanVitale() {
	}

	@Override
	public DatiFascicoloSportSanVitale getDati() {
		return (DatiFascicoloSportSanVitale) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportSanVitale.Builder();
	}
}