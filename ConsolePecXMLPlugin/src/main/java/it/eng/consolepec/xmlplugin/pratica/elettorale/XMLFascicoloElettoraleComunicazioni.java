package it.eng.consolepec.xmlplugin.pratica.elettorale;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloElettoraleComunicazioni extends XMLFascicolo implements FascicoloElettoraleComunicazioni {

	public XMLFascicoloElettoraleComunicazioni() {
	}

	@Override
	public DatiFascicoloElettoraleComunicazioni getDati() {
		return (DatiFascicoloElettoraleComunicazioni) super.getDati();
	}

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloElettoraleComunicazioni.Builder();
	}
}