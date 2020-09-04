package it.eng.consolepec.xmlplugin.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.cobo.consolepec.commons.exception.InvalidArgumentException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.TaskFactory;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.IncollaAllegatoHandler;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.factory.FascicoliFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TagliaAllegatiTaskApi.TagliaAllegatiOutput;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.protocollo.GestioneFascicoloGenericoTask;

/**
 * 
 * @author biagiot
 *
 */
public class SpostaAllegatiTest {

	@Test
	public void sposta() throws Exception {
		List<String> allegati = new ArrayList<String>();
		allegati.add("allegato_1.txt");
		allegati.add("allegato_2.txt");
		Fascicolo fascicoloSorgente = getFascicolo("FS123", allegati);
		Fascicolo fascicoloDestinatario = getFascicolo("FS456", null);

		TaskFascicolo<?> taskFascicoloSorgente = getTaskCorrente(fascicoloSorgente);
		TaskFascicolo<?> taskFascicoloDestinatario = getTaskCorrente(fascicoloDestinatario);

		TagliaAllegatiOutput tagliaOutput = taskFascicoloSorgente.tagliaAllegati(new ArrayList<Allegato>(fascicoloSorgente.getDati().getAllegati()), fascicoloDestinatario);
		taskFascicoloDestinatario.incollaAllegati(tagliaOutput, fascicoloSorgente, new IncollaAllegatoHandler() {

			@Override
			public Allegato incollaAllegato(String nomeAllegato, Allegato allegatoOriginale) throws ApplicationException, InvalidArgumentException {
				return allegatoOriginale;
			}
		});

		fascicoloSorgente = serDeserFascicolo(fascicoloSorgente);
		fascicoloDestinatario = serDeserFascicolo(fascicoloDestinatario);

		Assert.assertTrue(fascicoloSorgente.getDati().getAllegati().isEmpty());
		Assert.assertFalse(fascicoloDestinatario.getDati().getAllegati().isEmpty());
		Assert.assertEquals(fascicoloDestinatario.getDati().getAllegati().size(), 2);
	}

	@Test(expected = InvalidArgumentException.class)
	public void spostaConErrore() throws Exception {
		List<String> allegati = new ArrayList<String>();
		allegati.add("allegato_1.txt");
		allegati.add("allegato_2.txt");
		Fascicolo fascicoloSorgente = getFascicolo("FS123", allegati);
		Fascicolo fascicoloDestinatario = getFascicolo("FS456", null);

		fascicoloSorgente.getDati().getAllegati().iterator().next().setLock(true);
		TaskFascicolo<?> taskFascicoloSorgente = getTaskCorrente(fascicoloSorgente);
		taskFascicoloSorgente.tagliaAllegati(new ArrayList<Allegato>(fascicoloSorgente.getDati().getAllegati()), fascicoloDestinatario);
	}

	/*
	 * Utils
	 */

	private static Fascicolo getFascicolo(String idDocumentale, List<String> allegati) {
		PraticaFactory pf = new XMLPraticaFactory();
		Builder builder = FascicoliFactory.getBuilder("FASCICOLO_GENERICO");
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setDataCreazione(new Date());
		builder.setTitolo("Test Taglia/Incolla " + idDocumentale);
		builder.setIdDocumentale(idDocumentale);
		builder.setTipologiaPratica(new TipologiaPratica("FASCICOLO_GENERICO"));
		DatiFascicolo dati = builder.construct();

		if (allegati != null) {
			for (String nome : allegati) {
				dati.getAllegati().add(buildAllegato(dati, nome));
			}
		}

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
		return dati.new Allegato(nome, nome, "ALLEGATI", 0L, "1.0", false, null, false, null, null, new Date());
	}

	private static TaskFascicolo<?> getTaskCorrente(Fascicolo fascicolo) {
		Set<Task<?>> tasks = fascicolo.getTasks();

		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo())
				return (TaskFascicolo<?>) t;
		}

		return null;
	}

	private static Fascicolo serDeserFascicolo(Fascicolo fascicolo) {
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicolo);
		return xpf.loadPratica(Fascicolo.class, new StringReader(sw1.toString()));
	}
}
