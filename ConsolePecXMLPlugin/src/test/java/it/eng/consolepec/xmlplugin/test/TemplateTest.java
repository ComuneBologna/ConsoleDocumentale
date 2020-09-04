package it.eng.consolepec.xmlplugin.test;

import java.io.StringReader;
import java.io.StringWriter;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.cobo.consolepec.commons.exception.ApplicationException;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.JsonPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.TaskObserver.AggiungiAllegato;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.XMLGestioneTemplateDocumentoPDFTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;
import it.eng.consolepec.xmlplugin.util.XmlPluginUtil;

public class TemplateTest {

	private static final String FILE_NAME = "file.txt";
	private static final String FILE_ALFRESCO_PATH = "/CONSOLE/PRATICHE/MODU_123";
	private static final String FILE_VERSION = "1.0";
	
	Logger logger = LoggerFactory.getLogger(TemplateTest.class);

	@Test
	public void testTemplateEmail() {
		logger.info("TEST TEMPLATE EMAIL");

		TemplateEmail template = TestUtils.compilaTemplateEmailDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, template);

		TemplateEmail template2 = xpf.loadPratica(TemplateEmail.class, new StringReader(sw1.toString()));
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, template2);

		Assert.assertEquals(sw1.toString(), sw2.toString());
		Assert.assertEquals(template, template2);
		Assert.assertEquals(template.getDati(), template2.getDati());

		logger.debug("fine");
	}

	@Test
	public void testTemplateEmailJson() {
		logger.info("TEST TEMPLATE EMAIL");

		TemplateEmail template = TestUtils.compilaTemplateEmailDummy();

		JsonPraticaFactory xpf = new JsonPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, template);

		TemplateEmail template2 = xpf.loadPratica(TemplateEmail.class, new StringReader(sw1.toString()));
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, template2);

		Assert.assertEquals(sw1.toString(), sw2.toString());
		Assert.assertEquals(template, template2);
		Assert.assertEquals(template.getDati(), template2.getDati());

		logger.debug("fine");
	}

	@Test
	public void testTemplateDocumentoPDF() throws Exception {

		logger.info("TEST TEMPLATE DOCPDF");

		TemplateDocumentoPDF template = TestUtils.compilaTemplateDocPDFDummy();

		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, template);

		XMLGestioneTemplateDocumentoPDFTask gestioneTaskCorrente = XmlPluginUtil.getGestioneTaskCorrente(template, XMLGestioneTemplateDocumentoPDFTask.class, "Ataulfo");

		if (gestioneTaskCorrente.controllaAbilitazione(TipoApiTaskTemplate.CARICA_ODT)) {

			final Allegato allegato = template.getDati().new Allegato(FILE_NAME, FILE_NAME, FILE_ALFRESCO_PATH, FILE_VERSION);

			gestioneTaskCorrente.caricaODT(allegato, new AggiungiAllegato() {

				@Override
				public Allegato aggiungiAllegato(String alfrescoPathAllegato, String nomeAllegato) throws ApplicationException {
					// TODO Auto-generated method stub
					return allegato;
				}

			});
		}

		Assert.assertNotNull(template.getDati().getAllegati().iterator().next());
		Assert.assertEquals(FILE_NAME, template.getDati().getAllegati().iterator().next().getNome());

		// Il workaround per evitare di serializzare le notifiche non serve piu' in quanto:
		// se non vengono specificati indirizzi di notifica in caso di mail nessuna notifica viene registrata
		// Assert.assertTrue(template.getDati().getNotifiche().size() == 0);
		// template.getDati().getNotifiche().clear();
		Assert.assertTrue(template.getDati().getNotifiche().isEmpty());

		StringWriter reloaded = new StringWriter();
		xpf.serializePraticaInstance(reloaded, template);

		TemplateDocumentoPDF template2 = xpf.loadPratica(TemplateDocumentoPDF.class, new StringReader(reloaded.toString()));
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, template2);

		Assert.assertEquals(reloaded.toString(), sw2.toString());
		Assert.assertEquals(template, template2);
		Assert.assertEquals(template.getDati(), template2.getDati());

		logger.debug("fine");
	}

	public void testTemplateDocumentoPDFJson() throws Exception {
		logger.info("TEST TEMPLATE EMAIL");

		TemplateDocumentoPDF template = TestUtils.compilaTemplateDocPDFDummy();

		JsonPraticaFactory xpf = new JsonPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, template);

		TemplateDocumentoPDF template2 = xpf.loadPratica(TemplateDocumentoPDF.class, new StringReader(sw1.toString()));
		StringWriter sw2 = new StringWriter();
		xpf.serializePraticaInstance(sw2, template2);

		Assert.assertEquals(sw1.toString(), sw2.toString());
		Assert.assertEquals(template, template2);
		Assert.assertEquals(template.getDati(), template2.getDati());

		logger.debug("fine");
	}
}
