package it.eng.consolepec.xmlplugin.pratica.modulistica;

import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;

import java.util.Map;

public interface PraticaModulistica extends Pratica<DatiModulistica> {
	public Map<MetadatiPratica, Object> getMetadata();

}
