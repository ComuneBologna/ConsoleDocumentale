package it.eng.consolepec.xmlplugin.test;

import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetadatiTest {
	Logger logger = LoggerFactory.getLogger(MetadatiTest.class);
	
	@Test
	public void testElencoMetadati(){
		logger.debug("prefisso metadato: {}", MetadatiPratica.getPrefisso());
		for(MetadatiPratica metas : MetadatiPratica.values()){
			logger.debug("metadato-email: {}",metas.getNome());
		}
	}
}
