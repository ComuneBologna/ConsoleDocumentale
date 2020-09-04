package it.eng.cobo.consolepec.util.json.configuration;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo.Stato;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.util.json.JsonFactory;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CaricamentoConfigurazioneAnagraficaRuolo {

	private boolean checkAzioni(List<Azione> serializzati , List<Azione> deserializzati){
		
		for(int i=0; i< serializzati.size(); i++){
			if(!serializzati.get(i).getUsernameUtente().equals(deserializzati.get(i).getUsernameUtente()) || !serializzati.get(i).getDescrizione().equals(deserializzati.get(i).getDescrizione()) || !serializzati.get(i).getData().equals(deserializzati.get(i).getData())){
				return false;
			}
		}
		
		return true;
	}
	
	@Test
	public void loadConfigurazioni(){
		
		
		AnagraficaRuolo anagraficaRuoloSerializzazione = new AnagraficaRuolo();
		anagraficaRuoloSerializzazione.setRuolo("DOC_00183_Entrate Pubblicita");
		anagraficaRuoloSerializzazione.setEmailPredefinita("email.predefinita@email.it");
		anagraficaRuoloSerializzazione.setEtichetta("Entrate Pubblicita");
		anagraficaRuoloSerializzazione.setOperatori(Arrays.asList("Operatore Uno", "Operatore Due"));
		anagraficaRuoloSerializzazione.setStato(Stato.ATTIVA);
		
		String serializeConfiguration = JsonFactory.defaultFactory().serialize(anagraficaRuoloSerializzazione);
		
		
		AnagraficaRuolo anagraficaRuoloDeserializzazione = JsonFactory.defaultFactory().deserialize(serializeConfiguration, AnagraficaRuolo.class);
		
		Assert.assertEquals(anagraficaRuoloSerializzazione.getRuolo(), anagraficaRuoloDeserializzazione.getRuolo());
		Assert.assertEquals(anagraficaRuoloSerializzazione.getEmailPredefinita(), anagraficaRuoloDeserializzazione.getEmailPredefinita());
		Assert.assertEquals(anagraficaRuoloSerializzazione.getEtichetta(), anagraficaRuoloDeserializzazione.getEtichetta());
		Assert.assertEquals(anagraficaRuoloSerializzazione.getStato().name(), anagraficaRuoloDeserializzazione.getStato().name());
		
		for(int i=0;i<anagraficaRuoloSerializzazione.getOperatori().size();i++){
			Assert.assertEquals(anagraficaRuoloSerializzazione.getOperatori().get(i), anagraficaRuoloDeserializzazione.getOperatori().get(i));
		}
		
		Assert.assertTrue(checkAzioni(anagraficaRuoloSerializzazione.getAzioni(), anagraficaRuoloDeserializzazione.getAzioni()));
		
		
	}
}
