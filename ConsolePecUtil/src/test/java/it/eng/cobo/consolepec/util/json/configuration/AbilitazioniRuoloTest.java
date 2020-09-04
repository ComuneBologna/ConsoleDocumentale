package it.eng.cobo.consolepec.util.json.configuration;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.Abilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.CreazioneFascicoloAbilitazione;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.ModificaFascicoloAbilitazione;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.cobo.consolepec.util.json.JsonFactory;

public class AbilitazioniRuoloTest {

	JsonFactory jsonFactory;

	@Before
	public void before() {
		jsonFactory = JsonFactory.defaultFactory();
	}

	@Test
	public void test() {

		ModificaFascicoloAbilitazione mfa = new ModificaFascicoloAbilitazione("FASCICOLO_GENERICO");
		Abilitazione cfa = new CreazioneFascicoloAbilitazione("FASCICOLO_PERSONALE");

		List<Abilitazione> abilitazioni = Arrays.asList(mfa, cfa);
		AbilitazioniRuolo ar = new AbilitazioniRuolo();
		ar.setRuolo("DOC_0000_Test");
		ar.getAbilitazioni().addAll(abilitazioni);

		String json = jsonFactory.serialize(ar);
		JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
		Assert.assertEquals(jsonObject.get("tipologiaConfigurazione").getAsString(), ConfigurazioniUtil.NOME_TIPOLOGIA_ABILITAZIONE_RUOLO);

		JsonArray jsonArrayAbilitazioni = jsonObject.get("abilitazioni").getAsJsonArray();
		JsonObject jsonObjModificaFascicoloAbilitazione = jsonArrayAbilitazioni.get(0).getAsJsonObject();
		JsonObject jsonObjCreazioneFascicoloAbilitazione = jsonArrayAbilitazioni.get(1).getAsJsonObject();

		Assert.assertEquals(jsonObjModificaFascicoloAbilitazione.get("tipoAbilitazione").getAsString(), ModificaFascicoloAbilitazione.class.getSimpleName());
		Assert.assertEquals(jsonObjCreazioneFascicoloAbilitazione.get("tipoAbilitazione").getAsString(), CreazioneFascicoloAbilitazione.class.getSimpleName());

	}

}
