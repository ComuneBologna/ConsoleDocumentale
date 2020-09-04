package it.eng.cobo.consolepec.util.json.configuration;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.json.JsonFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CaricamentoConfigurazioneAnagraficaModello {

	private boolean checkAzioni(List<Azione> serializzati , List<Azione> deserializzati){
		
		for(int i=0; i< serializzati.size(); i++){
			if(!serializzati.get(i).getUsernameUtente().equals(deserializzati.get(i).getUsernameUtente()) || !serializzati.get(i).getDescrizione().equals(deserializzati.get(i).getDescrizione()) || !serializzati.get(i).getData().toString().equals(deserializzati.get(i).getData().toString())){
				return false;
			}
		}
		
		return true;
	}
	
	@Test
	public void loadConfigurazioni(){
		
		
		AnagraficaModello anagraficaModelloSerializzazione = new AnagraficaModello();
		
		anagraficaModelloSerializzazione.setNomeTipologia(TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia());
		
		Azione azione = new Azione("Username" , new Date(), "descrizione Azione");
		
		anagraficaModelloSerializzazione.setAzioni(Arrays.asList(azione));
		
		
		String serializeConfiguration = JsonFactory.defaultFactory().serialize(anagraficaModelloSerializzazione);
		
		
		AnagraficaModello anagraficaModelloDeserializzazione = JsonFactory.defaultFactory().deserialize(serializeConfiguration, AnagraficaModello.class);
		
		Assert.assertEquals(anagraficaModelloSerializzazione.getNomeTipologia(), anagraficaModelloDeserializzazione.getNomeTipologia());
		Assert.assertTrue(checkAzioni(anagraficaModelloSerializzazione.getAzioni(), anagraficaModelloDeserializzazione.getAzioni()));
		
		
	}
}
