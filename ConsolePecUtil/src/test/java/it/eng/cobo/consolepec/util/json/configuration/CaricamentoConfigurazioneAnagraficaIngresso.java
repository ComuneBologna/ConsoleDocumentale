package it.eng.cobo.consolepec.util.json.configuration;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.json.JsonFactory;

public class CaricamentoConfigurazioneAnagraficaIngresso {

	private static boolean checkAzioni(List<Azione> serializzati, List<Azione> deserializzati) {

		for (int i = 0; i < serializzati.size(); i++) {
			if (!serializzati.get(i).getUsernameUtente().equals(deserializzati.get(i).getUsernameUtente()) || !serializzati.get(i).getDescrizione().equals(deserializzati.get(i).getDescrizione())
					|| !serializzati.get(i).getData().equals(deserializzati.get(i).getData())) {
				return false;
			}
		}

		return true;
	}

	@Test
	public void loadConfigurazioni() {

		AnagraficaIngresso anagraficaIngressoSerializzazione = new AnagraficaIngresso();

		anagraficaIngressoSerializzazione.setNomeTipologia(TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia());
		anagraficaIngressoSerializzazione.setEtichettaTipologia("Fascicolo Personale");
		anagraficaIngressoSerializzazione.setIndirizzo("indirizzo@indirizzo.it");
		anagraficaIngressoSerializzazione.setPassword("psw");

		String serializeConfiguration = JsonFactory.defaultFactory().serialize(anagraficaIngressoSerializzazione);

		AnagraficaIngresso anagraficaIngressoDeserializzazione = JsonFactory.defaultFactory().deserialize(serializeConfiguration, AnagraficaIngresso.class);
		Assert.assertEquals(anagraficaIngressoSerializzazione.getNomeTipologia(), anagraficaIngressoDeserializzazione.getNomeTipologia());
		Assert.assertEquals(anagraficaIngressoSerializzazione.getIndirizzo(), anagraficaIngressoDeserializzazione.getIndirizzo());
		Assert.assertEquals(anagraficaIngressoSerializzazione.getPassword(), anagraficaIngressoDeserializzazione.getPassword());
		Assert.assertTrue(checkAzioni(anagraficaIngressoSerializzazione.getAzioni(), anagraficaIngressoDeserializzazione.getAzioni()));
	}
}
