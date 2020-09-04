package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportPorto extends XMLFascicolo implements FascicoloSportPorto {

	public XMLFascicoloSportPorto() {
	}

	@Override
	public DatiFascicoloSportPorto getDati() {
		return (DatiFascicoloSportPorto) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportPorto.Builder();
	}
}