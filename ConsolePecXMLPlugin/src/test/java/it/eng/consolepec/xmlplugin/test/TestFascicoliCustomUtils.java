package it.eng.consolepec.xmlplugin.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoFirma;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoProtocollazione;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.TaskFactory;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.factory.FascicoliFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;

public class TestFascicoliCustomUtils {
	private static Date dateFrom = new Date();
	private static Date dateTo = new Date();

	public static Fascicolo compilaFascicoloDummy(String tipo) {
		Fascicolo fascicolo = null;
		DatiFascicolo.Builder builder = compilaDummyBuilderDatiFascicolo(tipo);

		DatiFascicolo dati = builder.construct();

		PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343", TipologiaPratica.EMAIL_OUT.getNomeTipologia(), new Date());
		PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(), new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);
		dati.setProtocollazioniCapofila(protocollazioniCapofila);
		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = pf.newPraticaInstance(FascicoliFactory.getPraticaClass(tipo), dati);

		DatiGestioneFascicoloTask.Builder builderTask = FascicoliFactory.getBuilderTask(tipo);
		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloTask datiGestioneFascicoloTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		TaskFascicolo<?> fascicoloTask = tf.newTaskInstance(FascicoliFactory.getTaskClass(tipo), fascicolo, datiGestioneFascicoloTask);
		fascicoloTask.setCurrentUser("CiaoCiao");
		
		Allegato allegato1 = dati.new Allegato("allegato 1", "allegato 1", "ALLEGATI", null, null, 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null, null, null, null);
		Allegato allegato2 = dati.new Allegato("allegato 2", "allegato 2", "MESSAGGIO", null, null, 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null, null, null, null);
		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));
		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);
		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());
		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));
		DatoAggiuntivoValoreSingolo dato = new DatoAggiuntivoValoreSingolo("Nome", "Descrizione", TipoDato.Data, 25, true, false, true, new ArrayList<String>(), "Valore");
		fascicolo.getDati().getDatiAggiuntivi().add(dato);
		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	private static DatiFascicolo.Builder compilaDummyBuilderDatiFascicolo(String tipoPratica) {
		Builder builder = FascicoliFactory.getBuilder(tipoPratica);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/ENT_PG_123_2013");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("pratica molto bella");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");
		builder.setTipologiaPratica(new TipologiaPratica(tipoPratica));
		return builder;
	}

	private static List<ProtocollazioneCapofila> getProtocollazioniCapofila(DatiFascicolo dati) {
		List<ProtocollazioneCapofila> list = new ArrayList<DatiFascicolo.ProtocollazioneCapofila>();

		List<PraticaCollegata> listaPraticheCollegate = new ArrayList<DatiFascicolo.PraticaCollegata>();
		listaPraticheCollegate.add(dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(), new Date()));
		HashSet<DatiPratica.Allegato> allegati = new HashSet<DatiPratica.Allegato>();

		Allegato allegato1 = dati.new Allegato("allegato protocollato 1", "allegato protocollato 1", "ALLEGATI", null, null, 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null, null,
				null, null);
		
		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1Protocollato"));
		allegati.add(allegato1);
		dati.getAllegati().addAll(allegati);

		list.add(compilaProtocollazioneCapofila(dati, "sezione", "rubrica", "123", 2012, "provenienza", "tipologiadocumento", listaPraticheCollegate, "utenteprotocollazione", "numeroFascicolo", 2015,
				allegati, "titiolo", new Date(), "oggetto", getProtocollazioniCollegate(dati)));
		list.add(compilaProtocollazioneCapofila(dati, "sezione", "rubrica", "124", 2012, "provenienza", "tipologiadocumento", listaPraticheCollegate, "utenteprotocollazione", "numeroFascicolo", 2015,
				allegati, "titiolo", new Date(), "oggetto", getProtocollazioniCollegate(dati)));
		list.add(compilaProtocollazioneCapofila(dati, "sezione", "rubrica", "125", 2013, "provenienza", "tipologiadocumento", listaPraticheCollegate, "utenteprotocollazione", "numeroFascicolo", 2015,
				allegati, "titiolo", new Date(), "oggetto", getProtocollazioniCollegate(dati)));
		list.add(compilaProtocollazioneCapofila(dati, "sezione", "rubrica", "126", 2013, "provenienza", "tipologiadocumento", listaPraticheCollegate, "utenteprotocollazione", "numeroFascicolo", 2015,
				allegati, "titiolo", new Date(), "oggetto", getProtocollazioniCollegate(dati)));
		list.add(compilaProtocollazioneCapofila(dati, "sezione", "rubrica", "345", 2014, "provenienza", "tipologiadocumento", listaPraticheCollegate, "utenteprotocollazione", "numeroFascicolo", 2015,
				allegati, "titiolo", new Date(), "oggetto", getProtocollazioniCollegate(dati)));

		return list;
	}

	private static TreeSet<GruppoVisibilita> getGruppoVisibilita(String nomeAllegato) {
		TreeSet<GruppoVisibilita> treeSet = new TreeSet<DatiPratica.GruppoVisibilita>();
		for (int i = 0; i < 5; i++) {
			GruppoVisibilita gruppoVisibilita = new GruppoVisibilita("gruppo_" + i + "_" + nomeAllegato);
			treeSet.add(gruppoVisibilita);
		}
		return treeSet;
	}

	private static List<Protocollazione> getProtocollazioniCollegate(DatiFascicolo dati) {
		List<Protocollazione> list = new ArrayList<DatiFascicolo.Protocollazione>();
		List<PraticaCollegata> listaPraticheCollegate = new ArrayList<DatiFascicolo.PraticaCollegata>();
		listaPraticheCollegate.add(dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(), new Date()));
		HashSet<DatiPratica.Allegato> allegati = new HashSet<DatiPratica.Allegato>();
		Allegato allegato1 = dati.new Allegato("allegato protocollato collegato 1", "allegato protocollato collegato 1", "ALLEGATI", null, null, 234L, "0.9", true, TipoFirma.CADES, false, dateFrom,
				dateTo, null, null, null, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1Protocollato"));
		allegati.add(allegato1);
		dati.getAllegati().addAll(allegati);
		list.add(compilaProtocollazione(dati, "sezione", "rubrica", "234", 2014, "provenienza", "tipologiadocumento", listaPraticheCollegate, "utenteprotocollazione", "numeroFascicoloColl", 2015,
				allegati, "titiolo", new Date(), "oggetto"));
		return list;
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

}
