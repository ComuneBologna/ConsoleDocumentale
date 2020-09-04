package it.eng.consolepec.xmlplugin.pratica.elettorale;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloElettoraleElettore extends XMLFascicolo implements FascicoloElettoraleElettore {

	public XMLFascicoloElettoraleElettore() {
	}

	@Override
	public DatiFascicoloElettoraleElettore getDati() {
		return (DatiFascicoloElettoraleElettore) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloElettoraleElettore.Builder();
	}
}