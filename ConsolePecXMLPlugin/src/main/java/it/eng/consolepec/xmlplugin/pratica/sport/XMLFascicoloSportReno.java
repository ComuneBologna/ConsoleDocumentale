package it.eng.consolepec.xmlplugin.pratica.sport;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloSportReno extends XMLFascicolo implements FascicoloSportReno {

	public XMLFascicoloSportReno() {
	}

	@Override
	public DatiFascicoloSportReno getDati() {
		return (DatiFascicoloSportReno) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloSportReno.Builder();
	}

	protected String getTipologiaDocumento() {
		return "94";
	}
}