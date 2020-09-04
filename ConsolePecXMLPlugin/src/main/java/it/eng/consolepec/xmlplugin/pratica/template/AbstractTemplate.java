package it.eng.consolepec.xmlplugin.pratica.template;

import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;

import java.util.Map;

public interface AbstractTemplate<T extends DatiAbstractTemplate> extends Pratica<T>  {
	
	public Map<MetadatiPratica, Object> getMetadata();

}
