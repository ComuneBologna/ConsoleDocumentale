package it.eng.cobo.consolepec.util.json;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.drive.permessi.PermessoDrive;

/**
 * @author Giacomo F.M.
 * @since 2020-04-10
 */
public class JsonFactoryTest {

	private static final JsonFactory jf = JsonFactory.defaultFactory();

	@Test
	public void permessiDrive() {
		String pdJson = "{\"tipoPermesso\":\"VisualizzaElementoPermessoDrive\",\"ruolo\":\"DOC_00947_ProtocolloGenerale\"}";
		PermessoDrive pd = jf.deserialize(pdJson, PermessoDrive.class);
		assertEquals(pdJson, jf.serialize(pd).replaceAll("\n", "").replaceAll(" ", ""));
	}

	@Test
	public void abilitazioni() {
		String aJson = "{\"tipoAbilitazione\":\"RicercaLiberaAbilitazione\",\"dataCreazione\":\"2020-04-10T12:00:00.000+0200\",\"usernameCreazione\":\"giacomino\"}";
		Abilitazione a = jf.deserialize(aJson, Abilitazione.class);
		assertEquals(aJson, jf.serialize(a).replaceAll("\n", "").replaceAll(" ", ""));
	}

}
