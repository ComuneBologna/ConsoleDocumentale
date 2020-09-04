package it.eng.cobo.consolepec.util.json.configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.eng.cobo.consolepec.commons.datigenerici.AnagraficaAmministrazione;
import it.eng.cobo.consolepec.commons.datigenerici.IndirizzoEmail;
import it.eng.cobo.consolepec.util.datigenerici.DatiGenericiUtil;
import it.eng.cobo.consolepec.util.json.JsonFactory;

public class DatiGenericiTest {

	JsonFactory jsonFactory;

	AnagraficaAmministrazione am;
	IndirizzoEmail ie;

	@Before
	public void before() {
		jsonFactory = JsonFactory.defaultFactory();

		am = new AnagraficaAmministrazione();
		am.setCodiceAmministrativo("CAM");
		am.setFonte("Fonte");
		am.setNome("Amministrazione");

		ie = new IndirizzoEmail();
		ie.setIndirizzoEmail("biagio.tozzi@gmail.com");
	}

	@Test
	public void testAnagraficaAmministrazione() {

		String ser = jsonFactory.serialize(am);
		JsonObject jsonObject = new JsonParser().parse(ser).getAsJsonObject();
		Assert.assertEquals(DatiGenericiUtil.NOME_TIPOLOGIA_ANAGRAFICA_AMMINISTRAZIONE, jsonObject.get("tipologiaDatoGenerico").getAsString());
		AnagraficaAmministrazione am = jsonFactory.deserialize(ser, AnagraficaAmministrazione.class);
		Assert.assertEquals(this.am, am);
		Assert.assertEquals(this.am.getCodiceAmministrativo(), am.getCodiceAmministrativo());

	}

	@Test
	public void testIndirizzoEmail() {

		String ser = jsonFactory.serialize(ie);
		JsonObject jsonObject = new JsonParser().parse(ser).getAsJsonObject();
		Assert.assertEquals(DatiGenericiUtil.NOME_TIPOLOGIA_INDIRIZZO_EMAIL, jsonObject.get("tipologiaDatoGenerico").getAsString());
		IndirizzoEmail ie = jsonFactory.deserialize(ser, IndirizzoEmail.class);
		Assert.assertEquals(this.ie, ie);
		Assert.assertEquals(this.ie.getIndirizzoEmail(), ie.getIndirizzoEmail());

	}

}
