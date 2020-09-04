package it.eng.consolepec.xmlplugin.pratica.modulistica;

import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;

public class XMLFascicoloModulistica extends XMLFascicolo implements FascicoloModulistica {

	@Override
	protected Builder getBuilder() {
		return new DatiFascicoloModulistica.Builder();
	}
}