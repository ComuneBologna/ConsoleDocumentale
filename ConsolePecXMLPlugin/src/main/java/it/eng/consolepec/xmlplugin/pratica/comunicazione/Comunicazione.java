package it.eng.consolepec.xmlplugin.pratica.comunicazione;

import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;

import java.util.Map;

public interface Comunicazione extends Pratica<DatiComunicazione> {
	
	public Map<MetadatiPratica, Object> getMetadata();
	
	
	
}
