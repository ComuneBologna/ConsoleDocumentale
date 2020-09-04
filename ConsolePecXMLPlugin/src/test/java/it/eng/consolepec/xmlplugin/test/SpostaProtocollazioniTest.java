package it.eng.consolepec.xmlplugin.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoProtocollazione;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.TaskFactory;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.IncollaAllegatoHandler;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.factory.FascicoliFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Procedimento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaProtocollazioniTaskApi.TagliaProtocollazioniOutput;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.protocollo.GestioneFascicoloGenericoTask;

/**
 * 
 * @author biagiot
 *
 */
public class SpostaProtocollazioniTest {

	/**
	 * Dati: <br>
	 * - capofila1: 2 allegati + 1 pratica <br>
	 * 
	 * Test: <br>
	 * - sposto capofila1 <br>
	 * 
	 * @throws Exception
	 */
	@Test
	public void spostaProtocollazioni1() throws Exception {
		Map<String, List<String>> allegatiCapofila = new HashMap<>();
		Map<String, List<String>> praticheCapofila = new HashMap<>();
		Map<String, List<String>> collegati = new HashMap<>();
		Map<String, List<String>> praticheProtCollegate = new HashMap<>();

		allegatiCapofila.put("capofila1", Arrays.asList("allegato1", "allegato2"));
		praticheCapofila.put("capofila1", Arrays.asList("/PEC/CONSOLE/IN/EE12/metadati.xml"));

		Fascicolo fascicoloSorgente = getFascicolo("FS1234", allegatiCapofila, praticheCapofila, collegati, praticheProtCollegate);
		Fascicolo fascicoloDestinatario = getFascicolo("FS567", new HashMap<String, List<String>>(), new HashMap<String, List<String>>(), new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>());

		Assert.assertFalse(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());

		TaskFascicolo<?> taskSorgente = getTaskCorrente(fascicoloSorgente);
		TaskFascicolo<?> taskDestinatario = getTaskCorrente(fascicoloDestinatario);

		List<Allegato> allegatiDaSpostare = new ArrayList<>();
		List<Pratica<?>> praticheCollProtDaSpostare = new ArrayList<>();

		for (Allegato allegato : fascicoloSorgente.getDati().getAllegati())
			if (allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato2"))
				allegatiDaSpostare.add(allegato);

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE12",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		TagliaProtocollazioniOutput output = taskSorgente.tagliaProtocollazioni(allegatiDaSpostare, praticheCollProtDaSpostare, fascicoloDestinatario);
		taskDestinatario.incollaProtocollazioni(output, fascicoloSorgente, new IncollaAllegatoHandler() {

			@Override
			public Allegato incollaAllegato(String nomeAllegato, Allegato allegatoOriginale) throws ApplicationException, InvalidArgumentException {
				return allegatoOriginale;
			}
		});

		fascicoloSorgente = serDeserFascicolo(fascicoloSorgente);
		fascicoloDestinatario = serDeserFascicolo(fascicoloDestinatario);

		Assert.assertTrue(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertTrue(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());

		Assert.assertFalse(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		for (Allegato allegato : fascicoloDestinatario.getDati().getAllegati()) {
			Assert.assertTrue(allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato2"));
		}
		Assert.assertFalse(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertEquals(2, fascicoloDestinatario.getDati().getAllegati().size());
		Assert.assertEquals(1, fascicoloDestinatario.getDati().getProtocollazioniCapofila().size());
		Assert.assertEquals(2, fascicoloDestinatario.getDati().getProtocollazioniCapofila().get(0).getAllegatiProtocollati().size());
		Assert.assertEquals(1, fascicoloDestinatario.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().size());
	}

	/**
	 * Dati: <br>
	 * - capofila1: 2 allegati + 1 pratica <br>
	 * - capofila2: 2 allegati + 1 pratica <br>
	 * 
	 * Test: <br>
	 * - sposto capofila1 <br>
	 * 
	 * @throws Exception
	 */
	@Test
	public void spostaProtocollazioni2() throws Exception {
		Map<String, List<String>> allegatiCapofila = new HashMap<>();
		Map<String, List<String>> praticheCapofila = new HashMap<>();
		Map<String, List<String>> collegati = new HashMap<>();
		Map<String, List<String>> praticheProtCollegate = new HashMap<>();

		allegatiCapofila.put("capofila1", Arrays.asList("allegato1", "allegato2"));
		praticheCapofila.put("capofila1", Arrays.asList("/PEC/CONSOLE/IN/EE12/metadati.xml"));

		allegatiCapofila.put("capofila2", Arrays.asList("allegato3", "allegato4"));
		praticheCapofila.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE34/metadati.xml"));

		Fascicolo fascicoloSorgente = getFascicolo("FS1234", allegatiCapofila, praticheCapofila, collegati, praticheProtCollegate);
		Fascicolo fascicoloDestinatario = getFascicolo("FS567", new HashMap<String, List<String>>(), new HashMap<String, List<String>>(), new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>());

		Assert.assertFalse(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());

		TaskFascicolo<?> taskSorgente = getTaskCorrente(fascicoloSorgente);
		TaskFascicolo<?> taskDestinatario = getTaskCorrente(fascicoloDestinatario);

		List<Allegato> allegatiDaSpostare = new ArrayList<>();
		List<Pratica<?>> praticheCollProtDaSpostare = new ArrayList<>();

		for (Allegato allegato : fascicoloSorgente.getDati().getAllegati())
			if (allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato2"))
				allegatiDaSpostare.add(allegato);

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE12",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		TagliaProtocollazioniOutput output = taskSorgente.tagliaProtocollazioni(allegatiDaSpostare, praticheCollProtDaSpostare, fascicoloDestinatario);
		taskDestinatario.incollaProtocollazioni(output, fascicoloSorgente, new IncollaAllegatoHandler() {

			@Override
			public Allegato incollaAllegato(String nomeAllegato, Allegato allegatoOriginale) throws ApplicationException, InvalidArgumentException {
				return allegatoOriginale;
			}
		});

		fascicoloSorgente = serDeserFascicolo(fascicoloSorgente);
		fascicoloDestinatario = serDeserFascicolo(fascicoloDestinatario);

		Assert.assertFalse(fascicoloSorgente.getDati().getAllegati().isEmpty());
		for (Allegato allegato : fascicoloSorgente.getDati().getAllegati()) {
			Assert.assertTrue(allegato.getNome().equals("allegato3") || allegato.getNome().equals("allegato4"));
		}
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertEquals(2, fascicoloSorgente.getDati().getAllegati().size());
		Assert.assertEquals(1, fascicoloSorgente.getDati().getProtocollazioniCapofila().size());
		for (Allegato allegato : fascicoloSorgente.getDati().getProtocollazioniCapofila().get(0).getAllegatiProtocollati()) {
			Assert.assertTrue(allegato.getNome().equals("allegato3") || allegato.getNome().equals("allegato4"));
		}

		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().isEmpty());
		Assert.assertEquals(1, fascicoloSorgente.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().size());
		Assert.assertEquals("/PEC/CONSOLE/IN/EE34/metadati.xml", fascicoloSorgente.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().get(0).getAlfrescoPath());

		Assert.assertFalse(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		for (Allegato allegato : fascicoloDestinatario.getDati().getAllegati()) {
			Assert.assertTrue(allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato2"));
		}
		Assert.assertFalse(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertEquals(2, fascicoloDestinatario.getDati().getAllegati().size());
		Assert.assertEquals(1, fascicoloDestinatario.getDati().getProtocollazioniCapofila().size());
		Assert.assertEquals(2, fascicoloDestinatario.getDati().getProtocollazioniCapofila().get(0).getAllegatiProtocollati().size());
		Assert.assertEquals(1, fascicoloDestinatario.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().size());
		for (Allegato allegato : fascicoloDestinatario.getDati().getProtocollazioniCapofila().get(0).getAllegatiProtocollati()) {
			Assert.assertTrue(allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato2"));
		}

		Assert.assertFalse(fascicoloDestinatario.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().isEmpty());
		Assert.assertEquals(1, fascicoloDestinatario.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().size());
		Assert.assertEquals("/PEC/CONSOLE/IN/EE12/metadati.xml", fascicoloDestinatario.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().get(0).getAlfrescoPath());
	}

	/**
	 * Dati: <br>
	 * - capofila1: 2 allegati + 1 pratica <br>
	 * - capofila2: 2 allegati + 1 pratica - collegati: 1 allegato + 1 pratica<br>
	 * 
	 * Test: <br>
	 * - sposto capofila1 <br>
	 * - sposto collegati capofila2 <br>
	 * 
	 * @throws Exception
	 */
	@Test
	public void spostaProtocollazioni3() throws Exception {
		Map<String, List<String>> allegatiCapofila = new HashMap<>();
		Map<String, List<String>> praticheCapofila = new HashMap<>();
		Map<String, List<String>> collegati = new HashMap<>();
		Map<String, List<String>> praticheProtCollegate = new HashMap<>();

		allegatiCapofila.put("capofila1", Arrays.asList("allegato1", "allegato2"));
		praticheCapofila.put("capofila1", Arrays.asList("/PEC/CONSOLE/IN/EE12/metadati.xml"));

		allegatiCapofila.put("capofila2", Arrays.asList("allegato3", "allegato4"));
		praticheCapofila.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE34/metadati.xml"));

		collegati.put("capofila2", Arrays.asList("allegato5"));
		praticheProtCollegate.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE56/metadati.xml"));

		Fascicolo fascicoloSorgente = getFascicolo("FS1234", allegatiCapofila, praticheCapofila, collegati, praticheProtCollegate);
		Fascicolo fascicoloDestinatario = getFascicolo("FS567", new HashMap<String, List<String>>(), new HashMap<String, List<String>>(), new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>());

		Assert.assertFalse(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());

		TaskFascicolo<?> taskSorgente = getTaskCorrente(fascicoloSorgente);
		TaskFascicolo<?> taskDestinatario = getTaskCorrente(fascicoloDestinatario);

		List<Allegato> allegatiDaSpostare = new ArrayList<>();
		List<Pratica<?>> praticheCollProtDaSpostare = new ArrayList<>();

		for (Allegato allegato : fascicoloSorgente.getDati().getAllegati())
			if (allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato2") || allegato.getNome().equals("allegato5"))
				allegatiDaSpostare.add(allegato);

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE12",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));
		praticheCollProtDaSpostare.add(getEmailInCollegata("EE56",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		TagliaProtocollazioniOutput output = taskSorgente.tagliaProtocollazioni(allegatiDaSpostare, praticheCollProtDaSpostare, fascicoloDestinatario);
		taskDestinatario.incollaProtocollazioni(output, fascicoloSorgente, new IncollaAllegatoHandler() {

			@Override
			public Allegato incollaAllegato(String nomeAllegato, Allegato allegatoOriginale) throws ApplicationException, InvalidArgumentException {
				return allegatoOriginale;
			}
		});

		fascicoloSorgente = serDeserFascicolo(fascicoloSorgente);
		fascicoloDestinatario = serDeserFascicolo(fascicoloDestinatario);

		Assert.assertFalse(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertEquals(2, fascicoloSorgente.getDati().getAllegati().size());
		Assert.assertEquals(1, fascicoloSorgente.getDati().getProtocollazioniCapofila().size());
		for (Allegato allegato : fascicoloSorgente.getDati().getProtocollazioniCapofila().get(0).getAllegatiProtocollati()) {
			Assert.assertTrue(allegato.getNome().equals("allegato3") || allegato.getNome().equals("allegato4"));
		}
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().isEmpty());
		Assert.assertEquals(1, fascicoloSorgente.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().size());
		Assert.assertEquals("/PEC/CONSOLE/IN/EE34/metadati.xml", fascicoloSorgente.getDati().getProtocollazioniCapofila().get(0).getPraticheCollegateProtocollate().get(0).getAlfrescoPath());

		Assert.assertFalse(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		for (Allegato allegato : fascicoloDestinatario.getDati().getAllegati()) {
			Assert.assertTrue(allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato2") || allegato.getNome().equals("allegato5"));
		}
		Assert.assertFalse(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertEquals(3, fascicoloDestinatario.getDati().getAllegati().size());
		Assert.assertEquals(2, fascicoloDestinatario.getDati().getProtocollazioniCapofila().size());
		for (ProtocollazioneCapofila pc : fascicoloDestinatario.getDati().getProtocollazioniCapofila()) {
			if (pc.getAllegatiProtocollati().isEmpty() && pc.getPraticheCollegateProtocollate().isEmpty()) {
				Assert.assertFalse(pc.getProtocollazioniCollegate().isEmpty());
				Assert.assertEquals(1, pc.getProtocollazioniCollegate().size());
				Assert.assertFalse(pc.getProtocollazioniCollegate().get(0).getAllegatiProtocollati().isEmpty());
				Assert.assertEquals(1, pc.getProtocollazioniCollegate().get(0).getAllegatiProtocollati().size());
				Assert.assertEquals("allegato5", pc.getProtocollazioniCollegate().get(0).getAllegatiProtocollati().get(0).getNome());
				Assert.assertFalse(pc.getProtocollazioniCollegate().get(0).getPraticheCollegateProtocollate().isEmpty());
				Assert.assertEquals(1, pc.getProtocollazioniCollegate().get(0).getPraticheCollegateProtocollate().size());
				Assert.assertEquals("/PEC/CONSOLE/IN/EE56/metadati.xml", pc.getProtocollazioniCollegate().get(0).getPraticheCollegateProtocollate().get(0).getAlfrescoPath());
			} else {
				Assert.assertEquals(1, pc.getPraticheCollegateProtocollate().size());
				Assert.assertEquals("/PEC/CONSOLE/IN/EE12/metadati.xml", pc.getPraticheCollegateProtocollate().get(0).getAlfrescoPath());
				Assert.assertEquals(2, pc.getAllegatiProtocollati().size());
				for (Allegato allegato : pc.getAllegatiProtocollati()) {
					Assert.assertTrue(allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato2"));
				}
			}
		}

	}

	/**
	 * Dati: <br>
	 * Fascicolo Sorgente <br>
	 * - capofila1: 2 allegati + 1 pratica <br>
	 * - capofila2: collegati: 1 allegato + 1 pratica<br>
	 * 
	 * Fascicolo destinatario <br>
	 * : - capofila2: 1 allegato <br>
	 * - allegato con stesso nome di un allegato di capofila1 <br>
	 * - allegato con stesso nome di un allegato di capofila1 protocollato <br>
	 * - allegato con stesso nome di un allegato di capofila2 bloccato (lock) in task di firma <br>
	 * 
	 * Test: <br>
	 * - sposto capofila1 <br>
	 * - sposto collegati capofila2 <br>
	 * 
	 * Risultati: Spostamento di capofila1 e collegati capofila2 con: <br>
	 * - versionamento di un allegato di capofila1 <br>
	 * - cambio nome di due allegati (uno di capofila1 e uno di capofila2 in quanto già presenti allegati con lo stesso nome non versionabili) <br>
	 * 
	 * @throws Exception
	 */
	@Test
	public void spostaProtocollazioni4() throws Exception {
		Map<String, List<String>> allegatiCapofila = new HashMap<>();
		Map<String, List<String>> praticheCapofila = new HashMap<>();
		Map<String, List<String>> collegati = new HashMap<>();
		Map<String, List<String>> praticheProtCollegate = new HashMap<>();

		allegatiCapofila.put("capofila1", Arrays.asList("allegato1", "allegato2"));
		praticheCapofila.put("capofila1", Arrays.asList("/PEC/CONSOLE/IN/EE12/metadati.xml"));
		collegati.put("capofila2", Arrays.asList("allegato5"));
		praticheProtCollegate.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE56/metadati.xml"));
		Fascicolo fascicoloSorgente = getFascicolo("FS1234", allegatiCapofila, praticheCapofila, collegati, praticheProtCollegate);

		allegatiCapofila.clear();
		praticheCapofila.clear();
		collegati.clear();
		praticheProtCollegate.clear();

		allegatiCapofila.put("capofila2", Arrays.asList("allegato3", "allegato4"));
		praticheCapofila.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE34/metadati.xml"));
		allegatiCapofila.put("capofila3", Arrays.asList("allegato2"));
		final Fascicolo fascicoloDestinatario = getFascicolo("FS567", allegatiCapofila, praticheCapofila, new HashMap<String, List<String>>(), new HashMap<String, List<String>>());
		fascicoloDestinatario.getDati().getAllegati().add(buildAllegato(fascicoloDestinatario.getDati(), "allegato1"));
		Allegato allegatoTaskFirma = buildAllegato(fascicoloDestinatario.getDati(), "allegato5");
		allegatoTaskFirma.setLock(true);
		allegatoTaskFirma.setLockedBy(1);
		fascicoloDestinatario.getDati().getAllegati().add(allegatoTaskFirma);

		ProtocollazioneCapofila protoCapofilaSorg = null;
		for (ProtocollazioneCapofila pc : fascicoloSorgente.getDati().getProtocollazioniCapofila()) {
			for (Protocollazione p : pc.getProtocollazioniCollegate()) {
				for (Allegato allegato : p.getAllegatiProtocollati()) {
					if (allegato.getNome().equals("allegato5")) {
						protoCapofilaSorg = pc.clona();
						break;
					}
				}
			}
		}

		ProtocollazioneCapofila daEliminare = null;
		ProtocollazioneCapofila nuovo = null;
		for (ProtocollazioneCapofila pc : fascicoloDestinatario.getDati().getProtocollazioniCapofila()) {
			for (Allegato allegato : pc.getAllegatiProtocollati()) {
				if (allegato.getNome().equals("allegato3") || allegato.getNome().equals("allegato4")) {
					daEliminare = pc;
					ProtocollazioneCapofila pcn = protoCapofilaSorg;
					pcn.getProtocollazioniCollegate().clear();
					pcn.getAllegatiProtocollati().clear();
					pcn.getPraticheCollegateProtocollate().clear();
					pcn.getAllegatiProtocollati().addAll(pc.getAllegatiProtocollati());
					pcn.getPraticheCollegateProtocollate().addAll(pc.getPraticheCollegateProtocollate());
					nuovo = pcn;
					break;
				}
			}
		}
		fascicoloDestinatario.getDati().getProtocollazioniCapofila().remove(daEliminare);
		fascicoloDestinatario.getDati().getProtocollazioniCapofila().add(nuovo);

		Assert.assertFalse(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertEquals(2, fascicoloSorgente.getDati().getProtocollazioniCapofila().size());
		Assert.assertEquals(3, fascicoloSorgente.getDati().getAllegati().size());

		Assert.assertFalse(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertEquals(2, fascicoloDestinatario.getDati().getProtocollazioniCapofila().size());
		Assert.assertEquals(5, fascicoloDestinatario.getDati().getAllegati().size());

		TaskFascicolo<?> taskSorgente = getTaskCorrente(fascicoloSorgente);
		TaskFascicolo<?> taskDestinatario = getTaskCorrente(fascicoloDestinatario);

		List<Allegato> allegatiDaSpostare = new ArrayList<>();
		List<Pratica<?>> praticheCollProtDaSpostare = new ArrayList<>();

		for (Allegato allegato : fascicoloSorgente.getDati().getAllegati())
			if (allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato2") || allegato.getNome().equals("allegato5"))
				allegatiDaSpostare.add(allegato);

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE12",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));
		praticheCollProtDaSpostare.add(getEmailInCollegata("EE56",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		TagliaProtocollazioniOutput output = taskSorgente.tagliaProtocollazioni(allegatiDaSpostare, praticheCollProtDaSpostare, fascicoloDestinatario);
		taskDestinatario.incollaProtocollazioni(output, fascicoloSorgente, new IncollaAllegatoHandler() {

			@Override
			public Allegato incollaAllegato(String nomeAllegato, Allegato allegatoOriginale) throws ApplicationException, InvalidArgumentException {
				// Cerco di replicare l'handler del servizio SpostaProtocollazioniService senza l'aiuto di Alfresco per il versionamento;
				Allegato allegato = allegatoOriginale;

				if (!nomeAllegato.equals(allegatoOriginale.getNome())) {
					allegato = buildAllegato(fascicoloDestinatario.getDati(), nomeAllegato);

				} else {
					boolean vers = false;
					Allegato all = null;
					for (Allegato a : fascicoloDestinatario.getDati().getAllegati()) {
						if (a.getNome().equals(nomeAllegato)) {
							all = a;
							vers = true;
							break;
						}
					}

					if (vers) {
						Float versione = Float.parseFloat(allegatoOriginale.getCurrentVersion());
						versione++;
						allegato.setCurrentVersion(Float.toString(versione));
						all.setCurrentVersion(Float.toString(versione));
					}

				}

				return allegato;
			}
		});

		fascicoloSorgente = serDeserFascicolo(fascicoloSorgente);
		Fascicolo fascicoloDestinatario2 = serDeserFascicolo(fascicoloDestinatario);

		Assert.assertTrue(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertTrue(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());

		Assert.assertFalse(fascicoloDestinatario2.getDati().getAllegati().isEmpty());
		Assert.assertEquals(7, fascicoloDestinatario2.getDati().getAllegati().size());
		Assert.assertFalse(fascicoloDestinatario2.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertEquals(3, fascicoloDestinatario2.getDati().getProtocollazioniCapofila().size());
		int versionati = 0;
		int nonVersionati = 0;
		for (Allegato allegato : fascicoloDestinatario2.getDati().getAllegati()) {

			if (allegato.getNome().equals("allegato1")) {
				Assert.assertEquals("2.0", allegato.getCurrentVersion());
				versionati++;

			} else {
				Assert.assertEquals("1.0", allegato.getCurrentVersion());
				nonVersionati++;
			}
		}
		Assert.assertEquals(6, nonVersionati);
		Assert.assertEquals(1, versionati);
		for (ProtocollazioneCapofila pc : fascicoloDestinatario2.getDati().getProtocollazioniCapofila()) {
			Assert.assertFalse(pc.getAllegatiProtocollati().isEmpty());

			if (pc.getAllegatiProtocollati().size() == 1) {
				// capofila3
				Assert.assertEquals(1, pc.getAllegatiProtocollati().size());
				Assert.assertEquals("allegato2", pc.getAllegatiProtocollati().get(0).getNome());
				Assert.assertEquals("1.0", pc.getAllegatiProtocollati().get(0).getCurrentVersion());
				Assert.assertTrue(pc.getPraticheCollegateProtocollate().isEmpty());
				Assert.assertTrue(pc.getProtocollazioniCollegate().isEmpty());
			} else {
				if (pc.getProtocollazioniCollegate().isEmpty()) {
					// capofila1
					Assert.assertEquals(2, pc.getAllegatiProtocollati().size());
					for (Allegato ap : pc.getAllegatiProtocollati()) {
						Assert.assertTrue(ap.getNome().equals("allegato1") || ap.getNome().startsWith("allegato2"));
						if (ap.getNome().equals("allegato1")) {
							Assert.assertEquals("2.0", ap.getCurrentVersion());
						} else {
							Assert.assertNotEquals("allegato2", ap.getNome());
						}
					}
					Assert.assertFalse(pc.getPraticheCollegateProtocollate().isEmpty());
					Assert.assertEquals(1, pc.getPraticheCollegateProtocollate().size());
					Assert.assertEquals("/PEC/CONSOLE/IN/EE12/metadati.xml", pc.getPraticheCollegateProtocollate().get(0).getAlfrescoPath());
					Assert.assertTrue(pc.getProtocollazioniCollegate().isEmpty());
				} else {
					// capofila2
					Assert.assertEquals(2, pc.getAllegatiProtocollati().size());
					for (Allegato ap : pc.getAllegatiProtocollati()) {
						Assert.assertTrue(ap.getNome().equals("allegato3") || ap.getNome().startsWith("allegato4"));
						Assert.assertEquals("1.0", ap.getCurrentVersion());
					}
					Assert.assertFalse(pc.getPraticheCollegateProtocollate().isEmpty());
					Assert.assertEquals(1, pc.getPraticheCollegateProtocollate().size());
					Assert.assertEquals("/PEC/CONSOLE/IN/EE34/metadati.xml", pc.getPraticheCollegateProtocollate().get(0).getAlfrescoPath());
					Assert.assertFalse(pc.getProtocollazioniCollegate().isEmpty());
					Assert.assertEquals(1, pc.getProtocollazioniCollegate().size());
					Assert.assertFalse(pc.getProtocollazioniCollegate().get(0).getAllegatiProtocollati().isEmpty());
					Assert.assertEquals(1, pc.getProtocollazioniCollegate().get(0).getAllegatiProtocollati().size());
					Assert.assertTrue(pc.getProtocollazioniCollegate().get(0).getAllegatiProtocollati().get(0).getNome().startsWith("allegato5"));
					Assert.assertEquals("1.0", pc.getProtocollazioniCollegate().get(0).getAllegatiProtocollati().get(0).getCurrentVersion());
					Assert.assertFalse(pc.getProtocollazioniCollegate().get(0).getPraticheCollegateProtocollate().isEmpty());
					Assert.assertEquals(1, pc.getProtocollazioniCollegate().get(0).getPraticheCollegateProtocollate().size());
					Assert.assertEquals("/PEC/CONSOLE/IN/EE56/metadati.xml", pc.getProtocollazioniCollegate().get(0).getPraticheCollegateProtocollate().get(0).getAlfrescoPath());
				}
			}
		}
	}

	/**
	 * Dati: <br>
	 * - capofila1: 2 allegati + 1 pratica <br>
	 * - capofila2: 2 allegati + 1 pratica - collegati: 1 allegato + 1 pratica<br>
	 * 
	 * Test: <br>
	 * - sposto 2 allegati del capofila1 <br>
	 * - sposto collegati capofila2 <br>
	 * 
	 * Risultato: <br>
	 * - Errore: non posso spostare un capofila 'spezzandolo' <br>
	 * 
	 * @throws Exception
	 */
	@Test(expected = PraticaException.class)
	public void spostaProtocollazioniException1() throws Exception {
		Map<String, List<String>> allegatiCapofila = new HashMap<>();
		Map<String, List<String>> praticheCapofila = new HashMap<>();
		Map<String, List<String>> collegati = new HashMap<>();
		Map<String, List<String>> praticheProtCollegate = new HashMap<>();

		allegatiCapofila.put("capofila1", Arrays.asList("allegato1", "allegato2"));
		praticheCapofila.put("capofila1", Arrays.asList("/PEC/CONSOLE/IN/EE12/metadati.xml"));

		allegatiCapofila.put("capofila2", Arrays.asList("allegato3", "allegato4"));
		praticheCapofila.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE34/metadati.xml"));

		collegati.put("capofila2", Arrays.asList("allegato5"));
		praticheProtCollegate.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE56/metadati.xml"));

		Fascicolo fascicoloSorgente = getFascicolo("FS1234", allegatiCapofila, praticheCapofila, collegati, praticheProtCollegate);
		Fascicolo fascicoloDestinatario = getFascicolo("FS567", new HashMap<String, List<String>>(), new HashMap<String, List<String>>(), new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>());

		Assert.assertFalse(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());

		TaskFascicolo<?> taskSorgente = getTaskCorrente(fascicoloSorgente);

		List<Allegato> allegatiDaSpostare = new ArrayList<>();
		List<Pratica<?>> praticheCollProtDaSpostare = new ArrayList<>();

		for (Allegato allegato : fascicoloSorgente.getDati().getAllegati())
			if (allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato2") || allegato.getNome().equals("allegato5"))
				allegatiDaSpostare.add(allegato);

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE56",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		taskSorgente.tagliaProtocollazioni(allegatiDaSpostare, praticheCollProtDaSpostare, fascicoloDestinatario);
	}

	/**
	 * Dati: <br>
	 * - capofila1: 2 allegati + 1 pratica <br>
	 * - capofila2: 2 allegati + 1 pratica - collegati: 1 allegato + 1 pratica<br>
	 * 
	 * Test: <br>
	 * - sposto 1 allegato e la pratica prot. del capofila1 <br>
	 * - sposto collegati capofila2 <br>
	 * 
	 * Risultato: <br>
	 * - Errore: non posso spostare un capofila 'spezzandolo' <br>
	 * 
	 * @throws Exception
	 */
	@Test(expected = PraticaException.class)
	public void spostaProtocollazioniException2() throws Exception {
		Map<String, List<String>> allegatiCapofila = new HashMap<>();
		Map<String, List<String>> praticheCapofila = new HashMap<>();
		Map<String, List<String>> collegati = new HashMap<>();
		Map<String, List<String>> praticheProtCollegate = new HashMap<>();

		allegatiCapofila.put("capofila1", Arrays.asList("allegato1", "allegato2"));
		praticheCapofila.put("capofila1", Arrays.asList("/PEC/CONSOLE/IN/EE12/metadati.xml"));

		allegatiCapofila.put("capofila2", Arrays.asList("allegato3", "allegato4"));
		praticheCapofila.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE34/metadati.xml"));

		collegati.put("capofila2", Arrays.asList("allegato5"));
		praticheProtCollegate.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE56/metadati.xml"));

		Fascicolo fascicoloSorgente = getFascicolo("FS1234", allegatiCapofila, praticheCapofila, collegati, praticheProtCollegate);
		Fascicolo fascicoloDestinatario = getFascicolo("FS567", new HashMap<String, List<String>>(), new HashMap<String, List<String>>(), new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>());

		Assert.assertFalse(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());

		TaskFascicolo<?> taskSorgente = getTaskCorrente(fascicoloSorgente);

		List<Allegato> allegatiDaSpostare = new ArrayList<>();
		List<Pratica<?>> praticheCollProtDaSpostare = new ArrayList<>();

		for (Allegato allegato : fascicoloSorgente.getDati().getAllegati())
			if (allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato5"))
				allegatiDaSpostare.add(allegato);

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE12",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE56",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		taskSorgente.tagliaProtocollazioni(allegatiDaSpostare, praticheCollProtDaSpostare, fascicoloDestinatario);
	}

	/**
	 * Dati: <br>
	 * - capofila1: 2 allegati + 1 pratica <br>
	 * - capofila2: 2 allegati + 1 pratica - collegati: 1 allegato + 1 pratica<br>
	 * - procedimento avviato per capofila1 <br>
	 * 
	 * Test: <br>
	 * - sposto capofila1 <br>
	 * - sposto collegati capofila2 <br>
	 * 
	 * Risultato: <br>
	 * - Errore: non posso spostare un capofila se c'è un procedimento avviato <br>
	 * 
	 * @throws Exception
	 */
	@Test(expected = PraticaException.class)
	public void spostaProtocollazioniException3() throws Exception {
		Map<String, List<String>> allegatiCapofila = new HashMap<>();
		Map<String, List<String>> praticheCapofila = new HashMap<>();
		Map<String, List<String>> collegati = new HashMap<>();
		Map<String, List<String>> praticheProtCollegate = new HashMap<>();

		allegatiCapofila.put("capofila1", Arrays.asList("allegato1", "allegato2"));
		praticheCapofila.put("capofila1", Arrays.asList("/PEC/CONSOLE/IN/EE12/metadati.xml"));

		allegatiCapofila.put("capofila2", Arrays.asList("allegato3", "allegato4"));
		praticheCapofila.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE34/metadati.xml"));

		collegati.put("capofila2", Arrays.asList("allegato5"));
		praticheProtCollegate.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE56/metadati.xml"));

		Fascicolo fascicoloSorgente = getFascicolo("FS1234", allegatiCapofila, praticheCapofila, collegati, praticheProtCollegate);
		Fascicolo fascicoloDestinatario = getFascicolo("FS567", new HashMap<String, List<String>>(), new HashMap<String, List<String>>(), new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>());

		String numeroPG = "";
		Integer annoPG = null;

		for (ProtocollazioneCapofila pc : fascicoloSorgente.getDati().getProtocollazioniCapofila()) {
			if (!pc.getAllegatiProtocollati().isEmpty()) {
				numeroPG = pc.getNumeroPG();
				annoPG = pc.getAnnoPG();
			}
		}

		fascicoloSorgente.getDati().getProcedimenti().add(new Procedimento("", "", "", numeroPG, "", "", "", "", null, null, null, null, null, annoPG, null, null, null, null));

		Assert.assertFalse(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());

		TaskFascicolo<?> taskSorgente = getTaskCorrente(fascicoloSorgente);

		List<Allegato> allegatiDaSpostare = new ArrayList<>();
		List<Pratica<?>> praticheCollProtDaSpostare = new ArrayList<>();

		for (Allegato allegato : fascicoloSorgente.getDati().getAllegati())
			if (allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato5"))
				allegatiDaSpostare.add(allegato);

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE12",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE56",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		taskSorgente.tagliaProtocollazioni(allegatiDaSpostare, praticheCollProtDaSpostare, fascicoloDestinatario);
	}

	/**
	 * Dati: <br>
	 * - capofila1: 2 allegati + 1 pratica <br>
	 * - capofila2: 2 allegati + 1 pratica - collegati: 1 allegato + 1 pratica<br>
	 * - 1 allegato del capofila1 è in lock <br>
	 * 
	 * Test: <br>
	 * - sposto capofila1 <br>
	 * - sposto collegati capofila2 <br>
	 * 
	 * Risultato: <br>
	 * - Errore: non posso spostare un allegato in lock <br>
	 * 
	 * @throws Exception
	 */
	@Test(expected = PraticaException.class)
	public void spostaProtocollazioniException4() throws Exception {
		Map<String, List<String>> allegatiCapofila = new HashMap<>();
		Map<String, List<String>> praticheCapofila = new HashMap<>();
		Map<String, List<String>> collegati = new HashMap<>();
		Map<String, List<String>> praticheProtCollegate = new HashMap<>();

		allegatiCapofila.put("capofila1", Arrays.asList("allegato1", "allegato2"));
		praticheCapofila.put("capofila1", Arrays.asList("/PEC/CONSOLE/IN/EE12/metadati.xml"));

		allegatiCapofila.put("capofila2", Arrays.asList("allegato3", "allegato4"));
		praticheCapofila.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE34/metadati.xml"));

		collegati.put("capofila2", Arrays.asList("allegato5"));
		praticheProtCollegate.put("capofila2", Arrays.asList("/PEC/CONSOLE/IN/EE56/metadati.xml"));

		Fascicolo fascicoloSorgente = getFascicolo("FS1234", allegatiCapofila, praticheCapofila, collegati, praticheProtCollegate);
		Fascicolo fascicoloDestinatario = getFascicolo("FS567", new HashMap<String, List<String>>(), new HashMap<String, List<String>>(), new HashMap<String, List<String>>(),
				new HashMap<String, List<String>>());

		for (Allegato a : fascicoloSorgente.getDati().getAllegati()) {
			if (a.getNome().equals("allegato2")) {
				a.setLock(true);
				a.setLockedBy(1);
				break;
			}
		}

		String numeroPG = "";
		Integer annoPG = null;

		for (ProtocollazioneCapofila pc : fascicoloSorgente.getDati().getProtocollazioniCapofila()) {
			if (!pc.getAllegatiProtocollati().isEmpty()) {
				numeroPG = pc.getNumeroPG();
				annoPG = pc.getAnnoPG();
			}
		}

		fascicoloSorgente.getDati().getProcedimenti().add(new Procedimento("", "", "", numeroPG, "", "", "", "", null, null, null, null, null, annoPG, null, null, null, null));

		Assert.assertFalse(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloSorgente.getDati().getProtocollazioniCapofila().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		Assert.assertTrue(fascicoloDestinatario.getDati().getProtocollazioniCapofila().isEmpty());

		TaskFascicolo<?> taskSorgente = getTaskCorrente(fascicoloSorgente);

		List<Allegato> allegatiDaSpostare = new ArrayList<>();
		List<Pratica<?>> praticheCollProtDaSpostare = new ArrayList<>();

		for (Allegato allegato : fascicoloSorgente.getDati().getAllegati())
			if (allegato.getNome().equals("allegato1") || allegato.getNome().equals("allegato5"))
				allegatiDaSpostare.add(allegato);

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE12",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		praticheCollProtDaSpostare.add(getEmailInCollegata("EE56",
				fascicoloSorgente.getDati().new PraticaCollegata(fascicoloSorgente.getAlfrescoPath(), fascicoloSorgente.getDati().getTipo().getNomeTipologia(), new Date())));

		taskSorgente.tagliaProtocollazioni(allegatiDaSpostare, praticheCollProtDaSpostare, fascicoloDestinatario);
	}

	/*
	 * Utils
	 */

	private static PraticaEmailIn getEmailInCollegata(String idDocumentale, PraticaCollegata pc) {
		PraticaFactory pf = new XMLPraticaFactory();
		it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Builder builder = new DatiEmail.Builder();
		builder.setOggetto("Test Taglia/Incolla " + idDocumentale);
		builder.setTitolo("Test Taglia/Incolla " + idDocumentale);
		builder.setIdDocumentale(idDocumentale);
		builder.setFolderPath("/PEC/CONSOLE/IN/" + idDocumentale);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setMessageID(idDocumentale);
		DatiEmail datiEmail = builder.construct();
		PraticaEmailIn pratica = pf.newPraticaInstance(PraticaEmailIn.class, datiEmail);
		pratica.addPraticaCollegata(pc);
		return pratica;
	}

	private static Fascicolo getFascicolo(String idDocumentale, Map<String, List<String>> allegatiCapofila, Map<String, List<String>> praticheCapofila, Map<String, List<String>> collegati,
			Map<String, List<String>> praticheProtCollegate) {

		PraticaFactory pf = new XMLPraticaFactory();
		Builder builder = FascicoliFactory.getBuilder("FASCICOLO_GENERICO");
		builder.setFolderPath("/PEC/CONSOLE/PRATICHE/" + idDocumentale);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setDataCreazione(new Date());
		builder.setTitolo("Test Taglia/Incolla " + idDocumentale);
		builder.setIdDocumentale(idDocumentale);
		builder.setTipologiaPratica(new TipologiaPratica("FASCICOLO_GENERICO"));
		DatiFascicolo dati = builder.construct();

		List<ProtocollazioneCapofila> protocollazioniCapofila = new ArrayList<ProtocollazioneCapofila>();

		/*
		 * Alleg. capofila
		 */

		for (Entry<String, List<String>> entry : allegatiCapofila.entrySet()) {
			List<Allegato> allCapofila = new ArrayList<Allegato>();
			List<Allegato> allegatiCollegati = new ArrayList<Allegato>();
			List<PraticaCollegata> prProtCapofila = new ArrayList<>();
			List<PraticaCollegata> prProtCollegate = new ArrayList<>();

			for (String nome : entry.getValue()) {
				allCapofila.add(buildAllegato(dati, nome));
			}

			if (collegati.containsKey(entry.getKey())) {
				for (String nomeColl : collegati.get(entry.getKey())) {
					allegatiCollegati.add(buildAllegato(dati, nomeColl));
				}
			}

			if (praticheProtCollegate.containsKey(entry.getKey())) {
				for (String iddoc : praticheProtCollegate.get(entry.getKey()))
					prProtCollegate.add(dati.new PraticaCollegata(iddoc, "EMAIL_IN", new Date()));
			}

			if (praticheCapofila.containsKey(entry.getKey())) {
				for (String iddoc : praticheCapofila.get(entry.getKey()))
					prProtCapofila.add(dati.new PraticaCollegata(iddoc, "EMAIL_IN", new Date()));
			}

			protocollazioniCapofila.add(getProtocollazioneCapofila(dati, allCapofila, prProtCapofila, allegatiCollegati.isEmpty() ? null : allegatiCollegati, prProtCollegate));
		}

		/*
		 * Prat.capofila
		 */

		for (Entry<String, List<String>> entry : praticheCapofila.entrySet()) {

			if (!allegatiCapofila.containsKey(entry.getKey())) {
				List<Allegato> allCapofila = new ArrayList<Allegato>();
				List<Allegato> allegatiCollegati = new ArrayList<Allegato>();
				List<PraticaCollegata> prProtCapofila = new ArrayList<>();
				List<PraticaCollegata> prProtCollegate = new ArrayList<>();

				for (String iddoc : entry.getValue()) {
					prProtCapofila.add(dati.new PraticaCollegata(iddoc, "EMAIL_IN", new Date()));
				}

				if (collegati.containsKey(entry.getKey())) {
					for (String nomeColl : collegati.get(entry.getKey())) {
						allegatiCollegati.add(buildAllegato(dati, nomeColl));
					}
				}

				if (praticheProtCollegate.containsKey(entry.getKey())) {
					for (String iddoc : praticheProtCollegate.get(entry.getKey()))
						prProtCollegate.add(dati.new PraticaCollegata(iddoc, "EMAIL_IN", new Date()));
				}

				if (allegatiCapofila.containsKey(entry.getKey())) {
					for (String nome : allegatiCapofila.get(entry.getKey()))
						allCapofila.add(buildAllegato(dati, nome));
				}

				protocollazioniCapofila.add(getProtocollazioneCapofila(dati, allCapofila, prProtCapofila, allegatiCollegati.isEmpty() ? null : allegatiCollegati, prProtCollegate));
			}
		}

		/*
		 * Alleg. prot. non capofila
		 */

		for (Entry<String, List<String>> entry : collegati.entrySet()) {
			if ((allegatiCapofila.containsKey(entry.getKey())) //
					|| (praticheCapofila.containsKey(entry.getKey()))) { //
				// Già inserito

			} else {
				List<Allegato> allegatiCollegati = new ArrayList<Allegato>();
				List<PraticaCollegata> prProtCollegate = new ArrayList<>();

				for (String nome : entry.getValue()) {
					allegatiCollegati.add(buildAllegato(dati, nome));
				}

				if (praticheProtCollegate.containsKey(entry.getKey())) {
					for (String iddoc : praticheProtCollegate.get(entry.getKey()))
						prProtCollegate.add(dati.new PraticaCollegata(iddoc, "EMAIL_IN", new Date()));
				}

				protocollazioniCapofila.add(getProtocollazioneCapofila(dati, new ArrayList<Allegato>(), new ArrayList<PraticaCollegata>(), allegatiCollegati, prProtCollegate));
			}
		}

		/*
		 * Pratiche prot. non capofila
		 */

		for (Entry<String, List<String>> entry : praticheProtCollegate.entrySet()) {
			if ((allegatiCapofila.containsKey(entry.getKey())) //
					|| (praticheCapofila.containsKey(entry.getKey()))) { //
				// Già inserito

			} else if ((!allegatiCapofila.containsKey(entry.getKey()) //
					&& (!praticheCapofila.containsKey(entry.getKey()))) && collegati.containsKey(entry.getKey())) {
				// Già inserito

			} else {
				List<Allegato> allegatiCollegati = new ArrayList<Allegato>();
				List<PraticaCollegata> prProtCollegate = new ArrayList<>();

				for (String nome : entry.getValue()) {
					prProtCollegate.add(dati.new PraticaCollegata(nome, "EMAIL_IN", new Date()));
				}

				if (collegati.containsKey(entry.getKey())) {
					for (String nome : praticheProtCollegate.get(entry.getKey()))
						allegatiCollegati.add(buildAllegato(dati, nome));
				}

				protocollazioniCapofila.add(getProtocollazioneCapofila(dati, new ArrayList<Allegato>(), new ArrayList<PraticaCollegata>(), allegatiCollegati, prProtCollegate));
			}
		}

		dati.getProtocollazioniCapofila().addAll(protocollazioniCapofila);

		Fascicolo fascicolo = pf.newPraticaInstance(FascicoliFactory.getPraticaClass("FASCICOLO_GENERICO"), dati);
		DatiGestioneFascicoloTask.Builder builderTask = FascicoliFactory.getBuilderTask("FASCICOLO_GENERICO");
		Assegnatario assegnatario = new Assegnatario("DOC_0000_Biagio Tozzi", "Biagio Tozzi", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloTask datiGestioneFascicoloTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloGenericoTask fascicoloTask = (GestioneFascicoloGenericoTask) tf.newTaskInstance(FascicoliFactory.getTaskClass("FASCICOLO_GENERICO"), fascicolo, datiGestioneFascicoloTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("btozzi");
		for (TipoApiTask taskApi : TipoApiTask.values())
			fascicoloTask.getOperazioniAbilitate().add(taskApi.name());
		return fascicolo;

	}

	private static Allegato buildAllegato(DatiFascicolo dati, String nome) {
		return buildAllegato(dati, nome, "1.0");
	}

	private static Allegato buildAllegato(DatiFascicolo dati, String nome, String versione) {
		return dati.new Allegato(nome, nome, "ALLEGATI", 0L, versione, false, null, false, null, null, new Date());
	}

	private static TaskFascicolo<?> getTaskCorrente(Fascicolo fascicolo) {
		Set<Task<?>> tasks = fascicolo.getTasks();

		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo())
				return (TaskFascicolo<?>) t;
		}

		return null;
	}

	private static ProtocollazioneCapofila getProtocollazioneCapofila(DatiFascicolo dati, List<Allegato> allegatiCapofila, List<PraticaCollegata> praticheProtCapofila,
			List<Allegato> allegatiProtocollatiCollegati, List<PraticaCollegata> praticheProtCollegate) {

		HashSet<DatiPratica.Allegato> allegati = new HashSet<DatiPratica.Allegato>();

		if (allegatiCapofila != null) {
			allegati.addAll(allegatiCapofila);
			dati.getAllegati().addAll(allegati);
		}

		List<Protocollazione> collegate = new ArrayList<Protocollazione>();
		if (allegatiProtocollatiCollegati != null) {
			collegate.add(getProtocollazioneCollegata(dati, allegatiProtocollatiCollegati, praticheProtCollegate));
		}

		Random rand = new Random();
		return compilaProtocollazioneCapofila(dati, "sezione", "rubrica", Integer.toString(rand.nextInt(1000)), 2019, "provenienza", "tipologiadocumento", praticheProtCapofila,
				"utenteprotocollazione", "numeroFascicolo", 2015, allegati, "titiolo", new Date(), "oggetto", collegate);
	}

	private static ProtocollazioneCapofila compilaProtocollazioneCapofila(DatiFascicolo dati, String sezione, String rubrica, String numeroPG, Integer annoPG, String provenienza,
			String tipologiadocumento, List<PraticaCollegata> praticheCollegateProtocollate, String utenteprotocollazione, String numeroFascicolo, Integer annoFascicolo,
			HashSet<Allegato> allegatiProtocollati, String titiolo, Date dataprotocollazione, String oggetto, List<Protocollazione> protocollazioniCollegate) {
		DatiFascicolo.ProtocollazioneCapofilaBuilder builder = new DatiFascicolo.ProtocollazioneCapofilaBuilder(dati);
		builder.setAllegatiProtocollati(new ArrayList<DatiPratica.Allegato>(allegatiProtocollati));
		builder.setAnnoPG(annoPG);
		builder.setDataprotocollazione(dataprotocollazione);
		builder.setNumeroFascicolo(numeroFascicolo);
		builder.setAnnoFascicolo(annoFascicolo);
		builder.setNumeroPG(numeroPG);
		builder.setOggetto(oggetto);
		builder.setPraticheCollegateProtocollate(praticheCollegateProtocollate);
		builder.setProtocollazioniCollegate(protocollazioniCollegate);
		builder.setProvenienza(provenienza);
		builder.setRubrica(rubrica);
		builder.setSezione(sezione);
		builder.setTipologiadocumento(tipologiadocumento);
		builder.setTitolo(titiolo);
		builder.setUtenteprotocollazione(utenteprotocollazione);
		builder.setTipoProtocollazione(TipoProtocollazione.INTERNA);
		return builder.construct();
	}

	private static Protocollazione getProtocollazioneCollegata(DatiFascicolo dati, List<Allegato> allegatiCollegati, List<PraticaCollegata> praticheProtCollegate) {
		HashSet<DatiPratica.Allegato> allegati = new HashSet<DatiPratica.Allegato>();
		allegati.addAll(allegatiCollegati);
		dati.getAllegati().addAll(allegati);

		Random rand = new Random();
		return compilaProtocollazione(dati, "sezione", "rubrica", Integer.toString(rand.nextInt(1000)), 2014, "provenienza", "tipologiadocumento", praticheProtCollegate, "utenteprotocollazione",
				"numeroFascicoloColl", 2015, allegati, "titiolo", new Date(), "oggetto");
	}

	private static Protocollazione compilaProtocollazione(DatiFascicolo dati, String sezione, String rubrica, String numeroPG, Integer annoPG, String provenienza, String tipologiadocumento,
			List<PraticaCollegata> praticheCollegateProtocollate, String utenteprotocollazione, String numeroFascicolo, Integer annoFascicolo, HashSet<Allegato> allegatiProtocollati, String titiolo,
			Date dataprotocollazione, String oggetto) {
		DatiFascicolo.ProtocollazioneBuilder builder = new DatiFascicolo.ProtocollazioneBuilder(dati);
		builder.setAllegatiProtocollati(new ArrayList<DatiPratica.Allegato>(allegatiProtocollati));
		builder.setAnnoPG(annoPG);
		builder.setDataprotocollazione(dataprotocollazione);
		builder.setNumeroFascicolo(numeroFascicolo);
		builder.setAnnoFascicolo(annoFascicolo);
		builder.setNumeroPG(numeroPG);
		builder.setOggetto(oggetto);
		builder.setPraticheCollegateProtocollate(praticheCollegateProtocollate);
		builder.setProvenienza(provenienza);
		builder.setRubrica(rubrica);
		builder.setSezione(sezione);
		builder.setTipologiadocumento(tipologiadocumento);
		builder.setTitolo(titiolo);
		builder.setUtenteprotocollazione(utenteprotocollazione);
		builder.setTipoProtocollazione(TipoProtocollazione.INTERNA);
		return builder.construct();
	}

	private static Fascicolo serDeserFascicolo(Fascicolo fascicolo) {
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicolo);
		return xpf.loadPratica(Fascicolo.class, new StringReader(sw1.toString()));
	}

}
