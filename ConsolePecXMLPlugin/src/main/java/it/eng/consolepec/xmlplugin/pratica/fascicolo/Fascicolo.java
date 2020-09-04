package it.eng.consolepec.xmlplugin.pratica.fascicolo;

import java.util.Map;

import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.Pratica;

public interface Fascicolo extends Pratica<DatiFascicolo> {

	public Map<MetadatiPratica, Object> getMetadata();

	public void accessoUtenteEsterno(String utente) throws PraticaException;
	
}
