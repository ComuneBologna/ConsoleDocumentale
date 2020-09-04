package it.eng.consolepec.xmlplugin.pratica.fatturazione;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloFatturazione extends XMLFascicolo implements FascicoloFatturazione {

	public XMLFascicoloFatturazione() {
	}

	@Override
	public DatiFascicoloFatturazione getDati() {
		return (DatiFascicoloFatturazione) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloFatturazione.Builder();
	}
}