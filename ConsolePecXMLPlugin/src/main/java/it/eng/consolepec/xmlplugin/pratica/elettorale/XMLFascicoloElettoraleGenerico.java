package it.eng.consolepec.xmlplugin.pratica.elettorale;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloElettoraleGenerico extends XMLFascicolo implements FascicoloElettoraleGenerico {

	public XMLFascicoloElettoraleGenerico() {
	}

	@Override
	public DatiFascicoloElettoraleGenerico getDati() {
		return (DatiFascicoloElettoraleGenerico) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloElettoraleGenerico.Builder();
	}
}