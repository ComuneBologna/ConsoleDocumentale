package it.eng.cobo.consolepec.util.validation;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.rubrica.Indirizzo;
import it.eng.cobo.consolepec.commons.rubrica.PersonaFisica;
import it.eng.cobo.consolepec.commons.rubrica.PersonaGiuridica;

/**
 * @author GiacomoFM
 * @since 24/ott/2017
 */
public class AnagraficaValidatorTest {

	@Test
	public void nullo() {
		Anagrafica n = null;
		assertEquals("Controllo in caso di null", 1, ValidationUtilities.valida(n).size());
	}

	@Test
	public void personaFisica() {
		PersonaFisica pf = new PersonaFisica();
		pf.setCodiceFiscale("TEST");
		assertEquals("Controllo in caso di CF errato", 1, ValidationUtilities.valida(pf).size());

		pf.setCodiceFiscale("FRRGCM94E04M172Y");
		assertEquals("Controllo in caso di CF valido", 0, ValidationUtilities.valida(pf).size());
	}

	@Test
	public void personaGiuridica() {
		PersonaGiuridica pg = new PersonaGiuridica();
		pg.setPartitaIva("12345678901");
		assertEquals("Controllo in caso di PIVA errata", 1, ValidationUtilities.valida(pg).size());

		pg.setPartitaIva("01232710374");
		assertEquals("Controllo in caso di PIVA valida", 0, ValidationUtilities.valida(pg).size());
	}

	@Test
	public void validazioneIndirizzi() {
		PersonaGiuridica pg = new PersonaGiuridica();
		pg.setPartitaIva("01232710374");

		Indirizzo i1 = new Indirizzo();
		i1.setVia("VIA MARCONI");
		i1.setPiano("1");
		pg.getIndirizzi().add(i1);
		assertEquals("Controllo in caso di Indirizzo non valido", 1, ValidationUtilities.valida(pg).size());

		i1.setCivico("30");
		i1.setTipologia(Indirizzo.TIPOLOGIA_INDIRIZZO_LAG);
		assertEquals("Controllo in caso di Indirizzo corretto", 0, ValidationUtilities.valida(pg).size());

		Indirizzo i2 = new Indirizzo();
		i2.setVia("VIA LAME");
		i2.setPiano("1");
		i2.setCivico("30");
		i2.setTipologia(Indirizzo.TIPOLOGIA_INDIRIZZO_LAG);
		pg.getIndirizzi().add(i2);
		assertEquals("Controllo in caso di Indirizzo non valido", 1, ValidationUtilities.valida(pg).size());
	}
}
