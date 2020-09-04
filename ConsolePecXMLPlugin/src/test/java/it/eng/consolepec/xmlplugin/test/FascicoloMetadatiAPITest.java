package it.eng.consolepec.xmlplugin.test;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.MetadatiPratica;
import it.eng.consolepec.xmlplugin.factory.PraticaObserver.FileDownload;
import it.eng.consolepec.xmlplugin.factory.TaskFactory;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.factory.FascicoliFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;

import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FascicoloMetadatiAPITest {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void downloadAllegatoTest() {
		/* Richiedo la verifica firma di un allegato */
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		final Fascicolo fascicolo = xpf.loadPratica(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO), TestUtils.getFascicoloStream());
		final Allegato allg = fascicolo.getDati().getAllegati().first();

		fascicolo.downloadAllegato(allg, new FileDownload() {

			@Override
			public void onDownloadRequest(String alfrescoPath) {
				String path = fascicolo.getDati().getFolderPath() + "/" + allg.getFolderRelativePath() + "/" + allg.getNome();
				Assert.assertNotNull(alfrescoPath);
				Assert.assertEquals(alfrescoPath, path);
			}
		});
	}


	@Test
	public void metadatiFascicoloTest() {
		final Fascicolo fascicolo = TestUtils.compilaFascicoloDummy();
		// inizializzazione del task
		TaskFactory tf = new XMLTaskFactory();
		DatiGestioneFascicoloTask datiTask = TestUtils.compilaDatiTaskGestioneFascicolo();
		tf.newTaskInstance(FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO), fascicolo, datiTask);

		Map<MetadatiPratica, Object> metadata = fascicolo.getMetadata();

		Set<MetadatiPratica> keySet = metadata.keySet();
		for (MetadatiPratica metadatiPratica : keySet) {
			logger.info("Metadato: {} -> {}", metadatiPratica.toString(), metadata.get(metadatiPratica) == null ? null : metadata.get(metadatiPratica).toString());
		}

	}

}
