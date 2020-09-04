package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSaluteSport extends XMLFascicolo implements FascicoloSaluteSport {

	public XMLFascicoloSaluteSport() {
	}

	@Override
	public DatiFascicoloSaluteSport getDati() {
		return (DatiFascicoloSaluteSport) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSaluteSport.Builder();
	}
}