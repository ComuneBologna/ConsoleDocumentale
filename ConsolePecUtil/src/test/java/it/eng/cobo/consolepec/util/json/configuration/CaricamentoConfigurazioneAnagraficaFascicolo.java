package it.eng.cobo.consolepec.util.json.configuration;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.ConfigurazioneEsecuzione;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.DatiProcedimento;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.Titolazione;
import it.eng.cobo.consolepec.util.json.JsonFactory;

import java.util.Arrays;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class CaricamentoConfigurazioneAnagraficaFascicolo {

	private static boolean checkEqualsDatiAggiuntivi(DatoAggiuntivo serializzato, DatoAggiuntivo deserializzato) {

		if (serializzato.getNome().equals(deserializzato.getNome()) && serializzato.getDescrizione().equals(deserializzato.getDescrizione()) && serializzato.getPosizione().equals(deserializzato.getPosizione()) && serializzato.getTipo().name().equals(deserializzato.getTipo().name())) {
			return true;
		}
		return false;
	}

	private static boolean checkEqualsStepIter(AnagraficaFascicolo.StepIter serializzato, AnagraficaFascicolo.StepIter deserializzato) {
		if (serializzato.getNome().equals(deserializzato.getNome()) && serializzato.getDataAggiornamento().toString().equals(deserializzato.getDataAggiornamento().toString())) {
			return true;
		}
		return false;
	}

	
	@Test
	public void loadConfigurazioni(){
		
		AnagraficaFascicolo anagraficaFascicoloSerializzazione = new AnagraficaFascicolo();
		anagraficaFascicoloSerializzazione.setNomeTipologia(TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia());
		anagraficaFascicoloSerializzazione.setTemplateTitolo("template Titolo");
		anagraficaFascicoloSerializzazione.setTipologieAllegato(Arrays.asList("TIPOLOGIA ALEEGATO UNO", "TIPOLOGIA ALLEGATO DUE"));
		anagraficaFascicoloSerializzazione.setProtocollabile(true);
		anagraficaFascicoloSerializzazione.setProtocollazioneRiservata(false);
		
		DatiProcedimento datiProcedimento = new DatiProcedimento();
		datiProcedimento.setDataAvviamentoProcedimento(new Date());
		datiProcedimento.setCodiceProcedimento(111);
		datiProcedimento.setQuartiere("Quartiere");
		datiProcedimento.setEmail("email.email@email.com");
		
		anagraficaFascicoloSerializzazione.setDatiProcedimento(datiProcedimento);
		anagraficaFascicoloSerializzazione.setTitolazione(new Titolazione());
		
		ConfigurazioneEsecuzione conf = new ConfigurazioneEsecuzione();
		anagraficaFascicoloSerializzazione.setConfigurazioneEsecuzioni(Arrays.asList(conf));
		
		AnagraficaFascicolo.StepIter stepIter = new AnagraficaFascicolo.StepIter();
		stepIter.setNome("STEP ITER TEST");
		stepIter.setIniziale(true);
		stepIter.setDataAggiornamento(new Date());
		
		anagraficaFascicoloSerializzazione.setStepIterAbilitati(Arrays.asList(stepIter));
		
		
		DatoAggiuntivo datoAggiuntivo = new DatoAggiuntivoValoreSingolo("nome", "descrizione", TipoDato.Testo, 10, true, false, true, null, "valore");
		anagraficaFascicoloSerializzazione.setDatiAggiuntivi(Arrays.asList(datoAggiuntivo));
		
		String serializeConfiguration = JsonFactory.defaultFactory().serialize(anagraficaFascicoloSerializzazione);
		
		
		AnagraficaFascicolo anagraficaFascicoloDeserializzazione = JsonFactory.defaultFactory().deserialize(serializeConfiguration, AnagraficaFascicolo.class);
		
		Assert.assertEquals(anagraficaFascicoloSerializzazione.getNomeTipologia(), anagraficaFascicoloDeserializzazione.getNomeTipologia());
		Assert.assertEquals(anagraficaFascicoloSerializzazione.getTemplateTitolo(), anagraficaFascicoloDeserializzazione.getTemplateTitolo());
		Assert.assertEquals(anagraficaFascicoloSerializzazione.isProtocollabile(), anagraficaFascicoloDeserializzazione.isProtocollabile());
		Assert.assertNotNull(anagraficaFascicoloDeserializzazione.getTemplateTitolo());
		
		for(int i=0; i< anagraficaFascicoloSerializzazione.getDatiAggiuntivi().size(); i++){
			Assert.assertTrue(checkEqualsDatiAggiuntivi(anagraficaFascicoloSerializzazione.getDatiAggiuntivi().get(i), anagraficaFascicoloDeserializzazione.getDatiAggiuntivi().get(i)));
		}
		
		for(int i=0; i< anagraficaFascicoloSerializzazione.getStepIterAbilitati().size(); i++){			
			Assert.assertTrue(checkEqualsStepIter(anagraficaFascicoloSerializzazione.getStepIterAbilitati().get(i), anagraficaFascicoloDeserializzazione.getStepIterAbilitati().get(i)));
		}
		
		for(int i=0; i< anagraficaFascicoloSerializzazione.getTipologieAllegato().size(); i++){
			Assert.assertEquals(anagraficaFascicoloSerializzazione.getTipologieAllegato().get(i), anagraficaFascicoloDeserializzazione.getTipologieAllegato().get(i));
		}
	
		Assert.assertEquals(anagraficaFascicoloSerializzazione.getDatiProcedimento().getEmail(), anagraficaFascicoloDeserializzazione.getDatiProcedimento().getEmail());
		Assert.assertEquals(anagraficaFascicoloSerializzazione.getDatiProcedimento().getQuartiere(), anagraficaFascicoloDeserializzazione.getDatiProcedimento().getQuartiere());
		Assert.assertEquals(anagraficaFascicoloSerializzazione.getDatiProcedimento().getCodiceProcedimento(), anagraficaFascicoloDeserializzazione.getDatiProcedimento().getCodiceProcedimento());
		Assert.assertEquals(anagraficaFascicoloSerializzazione.getDatiProcedimento().getDataAvviamentoProcedimento().toString(), anagraficaFascicoloDeserializzazione.getDatiProcedimento().getDataAvviamentoProcedimento().toString());
		
		Assert.assertEquals(anagraficaFascicoloSerializzazione.isProtocollazioneRiservata(), anagraficaFascicoloDeserializzazione.isProtocollazioneRiservata());
		
		Assert.assertNotNull(anagraficaFascicoloSerializzazione.getConfigurazioneEsecuzioni());
		
	}
}
