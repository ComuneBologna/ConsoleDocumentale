package it.eng.consolepec.xmlplugin.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;

public class FascicoliCustomTest {

	Logger logger = LoggerFactory.getLogger(FascicoliCustomTest.class);

	@Test
	public void test() {

		testSerializzazioneFascicoli("FASCICOLO_GENERICO", "FASCICOLO_CONTRASSEGNO");
		testSerializzazioneFascicoli(
				Arrays.asList(TipologiaPratica.FASCICOLO_SPORT_PORTO_SARAGOZZA, TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE_RENO, TipologiaPratica.FASCICOLO_SPORT_SANDONATO_SANVITALE));

	}

	private void testSerializzazioneFascicoli(List<TipologiaPratica> tipi) {
		List<String> tipiList = Lists.transform(tipi, new Function<TipologiaPratica, String>() {

			@Override
			public String apply(TipologiaPratica input) {
				return input.getNomeTipologia();
			}

		});

		testSerializzazioneFascicoli(tipiList.toArray(new String[tipiList.size()]));
	}

	private void testSerializzazioneFascicoli(String... tipi) {
		for (String tipo : tipi) {
			try {
				logger.info("TEST FASCICOLO: " + tipo);
				Fascicolo fascicolo = TestFascicoliCustomUtils.compilaFascicoloDummy(tipo);
				XMLPraticaFactory xpf = new XMLPraticaFactory();
				StringWriter sw1 = new StringWriter();
				xpf.serializePraticaInstance(sw1, fascicolo);
				logger.debug("Originale serializzato: {}", sw1.toString());
				Fascicolo fascicolo2 = (Fascicolo) xpf.loadPratica(new StringReader(sw1.toString()));
				StringWriter sw0 = new StringWriter();
				xpf.serializePraticaInstance(sw0, fascicolo2);
				logger.debug("Copia deserializzata riserializzata: {}", sw0.toString());
				Assert.assertEquals(sw1.toString(), sw0.toString());
				Assert.assertEquals(fascicolo2.getDati().getTipo().getNomeTipologia(), tipo);
				Assert.assertEquals(fascicolo, fascicolo2);
				logger.debug(fascicolo.getDati().toString());
				logger.debug(fascicolo2.getDati().toString());
				Assert.assertEquals(fascicolo.getDati(), fascicolo2.getDati());
				StringWriter sw2 = new StringWriter();
				xpf.serializePraticaInstance(sw2, fascicolo2);
				Assert.assertEquals(sw1.toString(), sw2.toString());

			} catch (Throwable t) {
				throw new PraticaException(t, "Errore per pratica " + tipo + ": " + t.getMessage());
			}
		}
	}
}
