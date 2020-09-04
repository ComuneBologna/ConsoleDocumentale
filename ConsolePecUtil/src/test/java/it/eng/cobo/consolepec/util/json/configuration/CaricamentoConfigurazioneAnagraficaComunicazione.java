package it.eng.cobo.consolepec.util.json.configuration;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.Azione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.json.JsonFactory;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class CaricamentoConfigurazioneAnagraficaComunicazione {
	
	private boolean checkAzioni(List<Azione> serializzati, List<Azione> deserializzati) {

		for (int i = 0; i < serializzati.size(); i++) {
			if (!serializzati.get(i).getUsernameUtente().equals(deserializzati.get(i).getUsernameUtente()) || !serializzati.get(i).getDescrizione().equals(deserializzati.get(i).getDescrizione())) {
				return false;
			}
		}

		return true;
	}

	@Test
	public void loadConfigurazioni() {

		AnagraficaComunicazione anagraficaComunicazioneSerializzazione = new AnagraficaComunicazione();

		anagraficaComunicazioneSerializzazione.setNomeTipologia(TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia());
		Azione azione = new Azione("Username", new Date(), "descrizione Azione");

		anagraficaComunicazioneSerializzazione.setAzioni(Arrays.asList(azione));

		String serializeConfiguration = JsonFactory.defaultFactory().serialize(anagraficaComunicazioneSerializzazione);

		AnagraficaComunicazione anagraficaComunicazioneDeserializzazione = JsonFactory.defaultFactory().deserialize(serializeConfiguration, AnagraficaComunicazione.class);

		Assert.assertEquals(anagraficaComunicazioneSerializzazione.getNomeTipologia(), anagraficaComunicazioneDeserializzazione.getNomeTipologia());
		Assert.assertTrue(checkAzioni(anagraficaComunicazioneSerializzazione.getAzioni(), anagraficaComunicazioneDeserializzazione.getAzioni()));
		
	}
	
	/*@Test
	public void genera() {

		JsonFactory jsonFactory = new JsonFactory();
		
		AnagraficaRuolo anagrafica = new AnagraficaRuolo();
		anagrafica.setRuolo("RR");
		anagrafica.setEtichetta("ETICHETTA");
		anagrafica.setStato(StatoRuolo.ATTIVO);
		
		System.out.println(jsonFactory.serialize(anagrafica));
		
		AbilitazioniRuolo abilitazione = new AbilitazioniRuolo();
		WorklistAbilitazione worklistAbilitazione = new WorklistAbilitazione("NOME_WORKLIST", 1);
		abilitazione.getAbilitazioni().add(worklistAbilitazione );
		
		CreazioneFascicoloAbilitazione cfa = new CreazioneFascicoloAbilitazione("FASCICOLO_GENERICO");
		abilitazione.getAbilitazioni().add( cfa );
		
		System.out.println(jsonFactory.serialize(abilitazione));
		
		
	}*/
}
