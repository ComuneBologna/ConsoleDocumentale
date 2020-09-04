package it.eng.consolepec.xmlplugin.test;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Notifica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipologiaNotifica;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.jaxb.Documento;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.RiassegnaFascicoloTaskApi;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class NotificheTest {

	Logger logger = LoggerFactory.getLogger(NotificheTest.class);

	@Test
	public void testNotifiche() {

		Fascicolo fascicolo = TestUtils.compilaFascicoloGenericoDummy();

		TaskFascicolo<?> task = getTaskCorrente(fascicolo);
		task.setCurrentUser("CiaoCiao");

		boolean controllaAbilitazione = task.controllaAbilitazione(TipoApiTask.RIASSEGNA);
		Assert.assertTrue("Non Abilitato", controllaAbilitazione);

		AnagraficaRuolo ar = new AnagraficaRuolo();
		ar.setRuolo("Nuovo Utente");
		ar.setEtichetta("Nuovo Utente");
		((RiassegnaFascicoloTaskApi) task).riassegna(ar, new ArrayList<GruppoVisibilita>(), new ArrayList<Pratica<?>>(), "", Arrays.asList("indirizzo@aaa.com"));

		List<String> indNotifica = new ArrayList<>();
		for (Notifica not : fascicolo.getDati().getNotifiche()) {
			if (not.getParametriExtra().getIndirizziEmail() != null)
				indNotifica.addAll(not.getParametriExtra().getIndirizziEmail());
		}

		Assert.assertEquals(fascicolo.getDati().getNotifiche().get(0).getTipologia(), TipologiaNotifica.EMAIL);
		Assert.assertTrue(indNotifica.contains("indirizzo@aaa.com"));

		Documento documento = new Documento();
		documento.setNome("MARCARORE INVIA");
		fascicolo.getDati().getNotifiche().get(0).getParametriExtra().getAllegatiDaCaricare().add(XmlPluginUtil.getAllegatoFromDocumento(documento, fascicolo.getDati()));

		// controllo la serializzazione delle notifiche:

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, fascicolo);
		logger.debug("Originale serializzato {}", sw1.toString());
		Fascicolo fascicolo2 = xpf.loadPratica(Fascicolo.class, new StringReader(sw1.toString()));
		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, fascicolo2);
		logger.debug("Copia deserializzata riserializzata: {}", sw0.toString());

		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(fascicolo, fascicolo2);
		// Assert.assertEquals(fascicolo.getDati(), fascicolo2.getDati());

	}

	private TaskFascicolo<?> getTaskCorrente(Fascicolo fascicolo) {
		Set<Task<?>> tasks = fascicolo.getTasks();
		for (Task<?> t : tasks) {
			if (t instanceof TaskFascicolo && t.isAttivo())
				return (TaskFascicolo<?>) t;
		}
		return null;
	}

}
