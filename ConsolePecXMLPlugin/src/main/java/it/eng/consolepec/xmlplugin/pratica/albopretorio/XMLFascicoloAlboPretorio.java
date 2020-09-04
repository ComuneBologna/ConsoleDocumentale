package it.eng.consolepec.xmlplugin.pratica.albopretorio;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloAlboPretorio extends XMLFascicolo implements FascicoloAlboPretorio {

	public XMLFascicoloAlboPretorio() {
	}

	@Override
	public DatiFascicoloAlboPretorio getDati() {
		return (DatiFascicoloAlboPretorio) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloAlboPretorio.Builder();
	}
}
