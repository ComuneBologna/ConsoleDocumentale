package it.eng.consolepec.xmlplugin.test;

import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.PraticaObserver;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECOutTask;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PraticaEmailAPITest {
	private Reader source;
	private Logger logger = LoggerFactory.getLogger(PraticaEmailAPITest.class);
	private PraticaEmailIn mail = null;

	@Before
	public void initPratica() {
		source = TestUtils.getPraticaPECStream();
		/* ottengo una pratica da una sorgente Reader */
		PraticaFactory pf = new XMLPraticaFactory();
		mail = pf.loadPratica(PraticaEmailIn.class, source);
	}

	@Test
	public void verificaFirmaAPITest() {
		Assert.assertNotNull(source);

		/* Richiedo la verifica firma di un allegato */
		final Allegato allg = mail.getDati().getAllegati().first();

		logger.debug("Richiedo verifica firma allegato {} ", allg);

		mail.verificaFirma(allg, new PraticaObserver.VerificaFirma() {

			@Override
			public void onVerificaFirma(String alfrescoPath) {
				logger.debug("Ricevuta richiesta di verifica firma di file con path: {}", alfrescoPath);
				String path = mail.getDati().getFolderPath() + "/" + allg.getFolderRelativePath() + "/" + allg.getNome();
				Assert.assertNotNull(alfrescoPath);
				Assert.assertEquals(alfrescoPath, path);

			}
		});

	}

	@Test
	public void downloadAllegatoTest() {
		/* Richiedo la verifica firma di un allegato */
		final Allegato allg = mail.getDati().getAllegati().first();

		mail.downloadAllegato(allg, new PraticaObserver.FileDownload() {

			@Override
			public void onDownloadRequest(String alfrescoPath) {
				logger.debug("Richiesta di download con path {}", alfrescoPath);
				String path = mail.getDati().getFolderPath() + "/" + allg.getFolderRelativePath() + "/" + allg.getNome();
				Assert.assertNotNull(alfrescoPath);
				Assert.assertEquals(alfrescoPath, path);
			}
		});
	}
	
	@Test
	public void agganciaPraticaAPITest() {
		
		PraticaFactory pf = new XMLPraticaFactory();
		InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("metadati_out.xml");
		final PraticaEmailOut emailOut = pf.loadPratica(PraticaEmailOut.class, new InputStreamReader(resourceAsStream));
		GestionePECOutTask taskPecOut = TestUtils.recuperaTaskGestionePECOut(emailOut);
		taskPecOut.setCurrentUser("Ataulfo");
		taskPecOut.agganciaPraticaAPEC(mail);
		taskPecOut.setOperazioniAbilitate(TestUtils.getOperazioniAbilitate());
		
		Assert.assertTrue(mail.hasPraticaCollegata(emailOut));
		Assert.assertTrue(mail.getEmailOutCollegate().size()==1);
		Assert.assertTrue(emailOut.hasPraticaCollegata(mail));
		Assert.assertTrue(emailOut.getEmailInCollegate().size()==1);
	}
	

}
