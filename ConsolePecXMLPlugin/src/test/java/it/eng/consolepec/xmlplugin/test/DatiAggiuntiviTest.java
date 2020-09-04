package it.eng.consolepec.xmlplugin.test;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.RigaDatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.tabella.ValoreCellaDatoAggiuntivo.ValoreCellaDatoAggiuntivoSingolo;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.TaskFactory;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.factory.FascicoliFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class DatiAggiuntiviTest {

	@Test
	public void test() {
		
		Fascicolo ser = compilaFascicoloDummy();
		XMLPraticaFactory xpf = new XMLPraticaFactory();
		StringWriter sw1 = new StringWriter();
		xpf.serializePraticaInstance(sw1, ser);
		
		Fascicolo deser = (Fascicolo) xpf.loadPratica(new StringReader(sw1.toString()));
		Assert.assertEquals(ser.getDati().getDatiAggiuntivi(), deser.getDati().getDatiAggiuntivi());
		
		StringWriter sw0 = new StringWriter();
		xpf.serializePraticaInstance(sw0, deser);
		Assert.assertEquals(sw1.toString(), sw0.toString());
		Assert.assertEquals(ser, deser);
		Assert.assertEquals(ser.getDati(), ser.getDati());
		
	}
	
	public Fascicolo compilaFascicoloDummy() {
		Fascicolo fascicolo = null;
		DatiFascicolo.Builder builder = compilaDummyBuilderDatiFascicolo();
		DatiFascicolo dati = builder.construct();
		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = pf.newPraticaInstance(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO), dati);

		DatiGestioneFascicoloTask.Builder builderTask = FascicoliFactory.getBuilderTask(TipologiaPratica.FASCICOLO);

		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloTask datiGestioneFascicoloTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		TaskFascicolo<?> fascicoloTask = tf.newTaskInstance(FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO), fascicolo, datiGestioneFascicoloTask);
		fascicoloTask.setCurrentUser("CiaoCiao");


		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());

		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		List<String> valori = new ArrayList<String>();
		valori.add("Valore 1 Multiplo");
		valori.add("Valore 2 Multiplo");
		valori.add("Valore 3 Multiplo");
		
		DatoAggiuntivoValoreSingolo valoreSingolo = new DatoAggiuntivoValoreSingolo("Campo 1", "Campo1Des", TipoDato.Testo, 10, true, false, true, new ArrayList<String>(), "Valore 1");
		DatoAggiuntivoValoreMultiplo valoreMultiplo = new DatoAggiuntivoValoreMultiplo("Campo 3", "Campo3Desc", TipoDato.MultiploTesto, 20, true, false, true, new ArrayList<String>(), valori);
		DatoAggiuntivoAnagrafica anagrafica = new DatoAggiuntivoAnagrafica("Campo 4", "Campo4Desc", TipoDato.Anagrafica, 30, true, false, true, "Valore Anagrafica", 6.0);
		
		DatoAggiuntivoTabella t = new DatoAggiuntivoTabella();
		t.setNome("tabella1");
		t.setDescrizione("Ma che bella tabella che bella che bella");
		t.setPosizione(50);
		t.setVisibile(true);
		t.setTipo(TipoDato.Tabella);
		
		t.getIntestazioni().add(valoreSingolo);
		t.getIntestazioni().add(valoreMultiplo);
		t.getIntestazioni().add(anagrafica);
		
		List<ValoreCellaDatoAggiuntivo> celle = new ArrayList<ValoreCellaDatoAggiuntivo>();
		ValoreCellaDatoAggiuntivo cv1 = new ValoreCellaDatoAggiuntivoSingolo("valore1");
		ValoreCellaDatoAggiuntivo cv2 = new ValoreCellaDatoAggiuntivoMultiplo(Arrays.asList("Valore 1 Multiplo", "Valore 2 Multiplo"));
		ValoreCellaDatoAggiuntivo ca1 = new ValoreCellaDatoAggiuntivoAnagrafica(100.0, "Biagio Placido");
		celle.add(cv1);
		celle.add(cv2);
		celle.add(ca1);
		RigaDatoAggiuntivo riga1 = new RigaDatoAggiuntivo(celle);
		
		List<ValoreCellaDatoAggiuntivo> celle2 = new ArrayList<>();
		ValoreCellaDatoAggiuntivo cv21 = new ValoreCellaDatoAggiuntivoSingolo( "valore1b");
		ValoreCellaDatoAggiuntivo cv22 = new ValoreCellaDatoAggiuntivoMultiplo( Arrays.asList("Valore 2 Multiplo"));
		ValoreCellaDatoAggiuntivo ca2 = new ValoreCellaDatoAggiuntivoAnagrafica(100.0, "Biagio Placidob");
		celle2.add(cv21);
		celle2.add(cv22);
		celle2.add(ca2);
		RigaDatoAggiuntivo riga2 = new RigaDatoAggiuntivo(celle2);
		
		t.getRighe().add(riga1);
		t.getRighe().add(riga2);
		
		fascicolo.getDati().getDatiAggiuntivi().add(t);
		
		return fascicolo;
	}
	
	public DatiFascicolo.Builder compilaDummyBuilderDatiFascicolo() {
		Builder builder = FascicoliFactory.getBuilder(TipologiaPratica.FASCICOLO);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/PERS_PG_123_2013");
		builder.setProvenienza("Biagio");
		builder.setDataCreazione(new Date());
		builder.setTitolo("Biagio - Test dati aggiuntivi");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Javier Zanetti");
		builder.setUsernameCreazione("JZanett");
		builder.setTipologiaPratica(TipologiaPratica.FASCICOLO_PERSONALE);
		return builder;
	}
}
