package it.eng.consolepec.xmlplugin.test;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Allegato;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.GruppoVisibilita;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.PraticaCollegata;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoFirma;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.TipoProtocollazione;
import it.eng.consolepec.xmlplugin.factory.DatiPratica.Utente;
import it.eng.consolepec.xmlplugin.factory.DatiTask.Assegnatario;
import it.eng.consolepec.xmlplugin.factory.Pratica;
import it.eng.consolepec.xmlplugin.factory.PraticaFactory;
import it.eng.consolepec.xmlplugin.factory.Task;
import it.eng.consolepec.xmlplugin.factory.TaskFactory;
import it.eng.consolepec.xmlplugin.factory.XMLPraticaFactory;
import it.eng.consolepec.xmlplugin.factory.XMLTaskFactory;
import it.eng.consolepec.xmlplugin.pratica.albopretorio.DatiFascicoloAlboPretorio;
import it.eng.consolepec.xmlplugin.pratica.albopretorio.FascicoloAlboPretorio;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.Comunicazione;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.DatiComunicazione.Invio;
import it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleComunicazioni;
import it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleElettore;
import it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleGenerico;
import it.eng.consolepec.xmlplugin.pratica.elettorale.FascicoloElettoraleComunicazioni;
import it.eng.consolepec.xmlplugin.pratica.elettorale.FascicoloElettoraleElettore;
import it.eng.consolepec.xmlplugin.pratica.elettorale.FascicoloElettoraleGenerico;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Stato;
import it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.TipoEmail;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.PraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.factory.FascicoliFactory;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Builder;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.Protocollazione;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.ProtocollazioneCapofila;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.Fascicolo;
import it.eng.consolepec.xmlplugin.pratica.fatturazione.DatiFascicoloFatturazione;
import it.eng.consolepec.xmlplugin.pratica.fatturazione.FascicoloFatturazione;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiFascicoloModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.Sezione;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.TabellaModulo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.TabellaModulo.Riga;
import it.eng.consolepec.xmlplugin.pratica.modulistica.DatiModulistica.ValoreModulo;
import it.eng.consolepec.xmlplugin.pratica.modulistica.FascicoloModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.PraticaModulistica;
import it.eng.consolepec.xmlplugin.pratica.personale.DatiFascicoloPersonale;
import it.eng.consolepec.xmlplugin.pratica.personale.FascicoloPersonale;
import it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato;
import it.eng.consolepec.xmlplugin.pratica.riservato.FascicoloRiservato;
import it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportBorgoPanigale;
import it.eng.consolepec.xmlplugin.pratica.sport.FascicoloSportBorgoPanigale;
import it.eng.consolepec.xmlplugin.pratica.template.CampoTemplate;
import it.eng.consolepec.xmlplugin.pratica.template.CampoTemplate.TipoCampoTemplate;
import it.eng.consolepec.xmlplugin.pratica.template.DatiTemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.pratica.template.DatiTemplateEmail;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.pratica.template.TemplateEmail;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.DatiGestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.GestioneComunicazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionecomunicazione.operazioni.TipoApiTaskComunicazione;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.TaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.XMLTaskFascicolo;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.albopretorio.DatiGestioneFascicoloAlboPretorioTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.albopretorio.GestioneFascicoloAlboPretorioTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.DatiGestioneFascicoloElettoraleComunicazioniTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.DatiGestioneFascicoloElettoraleElettoreTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.DatiGestioneFascicoloElettoraleGenericoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.GestioneFascicoloElettoraleComunicazioniTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.GestioneFascicoloElettoraleElettoreTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.elettorale.GestioneFascicoloElettoraleGenericoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fascicolo.DatiGestioneFascicoloTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fatturazione.DatiGestioneFascicoloFatturazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.fatturazione.GestioneFascicoloFatturazioneTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.modulistica.DatiGestioneFascicoloModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.modulistica.GestioneFascicoloModulisticaTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.operazioni.TipoApiTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.personale.DatiGestioneFascicoloPersonaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.personale.GestioneFascicoloPersonaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.protocollo.GestioneFascicoloGenericoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.riservato.DatiGestioneFascicoloRiservatoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.riservato.GestioneFascicoloRiservatoTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.DatiGestioneFascicoloSportBorgoPanigaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionefascicolo.sport.GestioneFascicoloSportBorgoPanigaleTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.DatiGestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECInTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECOutTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.GestionePECTask;
import it.eng.consolepec.xmlplugin.tasks.gestionepec.operazioni.TipoApiTaskPEC;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.DatiGestioneTemplateDocumentoPDFTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.GestioneTemplateDocumentoPDFTask;
import it.eng.consolepec.xmlplugin.tasks.gestionetemplate.operazioni.TipoApiTaskTemplate;

public class TestUtils {

	static int counter = 0;

	private static Date dateFrom = new Date();
	private static Date dateTo = new Date();

	public static DatiEmail.Builder compilaDummyBuilderDatiEmail() {
		return compilaDummyBuilderDatiEmail("pec123", "MessageId:UAAAAAAAAAAAAAA");
	}

	public static DatiEmail.Builder compilaDummyBuilderDatiEmail(String path, String messageId) {
		DatiEmail.Builder builder = new DatiEmail.Builder();
		builder.setBody("body lalalala");
		builder.setFolderPath("/CONSOLE/PEC/" + path);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setDataCreazione(new Date());
		builder.setDataRicezione(new Date(System.currentTimeMillis() - 60 * 1000));
		DatiEmail.Destinatario[] altridest = {
				new DatiEmail.Destinatario("dest2@bla.com", "errore errore", it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno, false, false),
				new DatiEmail.Destinatario("dest3@bla.com", "errore errore", it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno, false, false) };
		builder.setDestinatari(altridest);
		builder.setDestinatarioPrincipale(
				new Destinatario("destinatario@test.com", "errore errore", it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno, false, Boolean.FALSE));
		builder.setMittente("test@mittente.com");
		builder.setOggetto("Oggetto <bello> ]]> ! & bello");
		builder.setTipoEmail(TipoEmail.PEC);
		builder.setMessageID(messageId);
		Destinatario[] destCC = { new Destinatario("111@aa.com", null, it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno, false, false),
				new Destinatario("tsc@aa.com", null, it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Destinatario.TipoEmail.esterno, false, false) };
		builder.setDestinatariCC(destCC);
		builder.setIdDocumentale("" + 123);
		builder.setTitolo("Titolo Mana");

		List<GruppoVisibilita> gruppi = new ArrayList<DatiPratica.GruppoVisibilita>();
		gruppi.add(new GruppoVisibilita("GruppoAbilitato1"));
		gruppi.add(new GruppoVisibilita("GruppoAbilitato2"));
		gruppi.add(new GruppoVisibilita("GruppoAbilitato3"));
		gruppi.add(new GruppoVisibilita("GruppoAbilitato4"));
		gruppi.add(new GruppoVisibilita("GruppoAbilitato5"));

		builder.setGruppiVisibilita(gruppi);
		return builder;
	}

	public static DatiFascicoloAlboPretorio.Builder compilaDummyBuilderDatiFascicoloAlboPretorio() {
		Builder builder = FascicoliFactory.getBuilder(TipologiaPratica.FASCICOLO_ALBO_PRETORIO);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/ALBO_PG_123_2013");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("pratica molto bella");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");

		return (it.eng.consolepec.xmlplugin.pratica.albopretorio.DatiFascicoloAlboPretorio.Builder) builder;
	}

	public static DatiFascicolo.Builder compilaDummyBuilderDatiFascicolo(String nomeTipologia) {
		Builder builder = FascicoliFactory.getBuilder(nomeTipologia);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/PERS_PG_123_2013");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("pratica molto bella");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");
		builder.setTipologiaPratica(TipologiaPratica.FASCICOLO_PERSONALE);
		ArrayList<GruppoVisibilita> list = new ArrayList<GruppoVisibilita>();
		list.add(new GruppoVisibilita("Raffaello"));
		list.add(new GruppoVisibilita("Michelangelo"));
		list.add(new GruppoVisibilita("Leonardo"));
		list.add(new GruppoVisibilita("Inzaghi"));

		builder.setGruppiVisibilita(list);

		return builder;
	}

	public static DatiFascicoloPersonale.Builder compilaDummyBuilderDatiFascicoloPersonale() {
		DatiFascicoloPersonale.Builder builder = (DatiFascicoloPersonale.Builder) FascicoliFactory.getBuilder(TipologiaPratica.FASCICOLO_PERSONALE);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/PERS_PG_123_2013");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("pratica molto bella");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");

		return builder;
	}

	public static DatiFascicoloRiservato.Builder compilaDummyBuilderDatiFascicoloRiservato() {
		Builder builder = FascicoliFactory.getBuilder(TipologiaPratica.FASCICOLO_RISERVATO);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/RIS_PG_123_2013");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("pratica molto bella");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setTipologiaPratica(TipologiaPratica.FASCICOLO_RISERVATO);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");

		return (it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.Builder) builder;
	}

	public static DatiFascicoloFatturazione.Builder compilaDummyBuilderDatiFascicoloFatturazione() {
		DatiFascicoloFatturazione.Builder builder = (it.eng.consolepec.xmlplugin.pratica.fatturazione.DatiFascicoloFatturazione.Builder) FascicoliFactory.getBuilder(
				TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/FATT_PG_123_2013");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("pratica molto bella");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");
		builder.setNumeroFattura("1243/2015");
		builder.setCodicePIva("dsfsdf9sk03");
		builder.setPIva("234234234234");
		builder.setRagioneSociale("Mario Rossi");

		return builder;
	}

	public static it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleElettore.Builder compilaDummyBuilderDatiFascicoloElettoraleElettore() {
		DatiFascicoloElettoraleElettore.Builder builder = (DatiFascicoloElettoraleElettore.Builder) FascicoliFactory.getBuilder(TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/CONT_PG_123_2013");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("Elettorale elettore");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");
		return builder;
	}

	public static it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleComunicazioni.Builder compilaDummyBuilderDatiFascicoloElettoraleComunicazioni() {
		DatiFascicoloElettoraleComunicazioni.Builder builder = (DatiFascicoloElettoraleComunicazioni.Builder) FascicoliFactory.getBuilder(TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/CONT_PG_123_2013");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("Elettorale comunicazioni");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");
		return builder;
	}

	public static it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleGenerico.Builder compilaDummyBuilderDatiFascicoloElettoraleGenerico() {
		DatiFascicoloElettoraleGenerico.Builder builder = (DatiFascicoloElettoraleGenerico.Builder) FascicoliFactory.getBuilder(TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/CONT_PG_123_2013");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("Elettorale generico");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");
		return builder;
	}

	public static DatiFascicoloModulistica.Builder compilaDummyBuilderDatiFascicoloModulistica() {

		DatiFascicoloModulistica.Builder builder = (it.eng.consolepec.xmlplugin.pratica.modulistica.DatiFascicoloModulistica.Builder) FascicoliFactory.getBuilder(
				TipologiaPratica.FASCICOLO_MODULISTICA);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/MODU_123");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("pratica molto bella");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");

		return builder;
	}

	private static DatiFascicoloSportBorgoPanigale.Builder compilaDummyBuilderDatiFascicoloSport() {
		DatiFascicoloSportBorgoPanigale.Builder builder = (it.eng.consolepec.xmlplugin.pratica.sport.DatiFascicoloSportBorgoPanigale.Builder) FascicoliFactory.getBuilder(
				TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE);
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/MODU_123");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("pratica molto bella");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");

		return builder;
	}

	public static PraticaEmailIn compilaPraticaDummyCompleta() {
		PraticaFactory pf = new XMLPraticaFactory();

		DatiEmail.Builder builder = compilaDummyBuilderDatiEmail();
		DatiEmail email = builder.construct();

		PraticaEmailIn praticaEmailIn = pf.newPraticaInstance(PraticaEmailIn.class, email);

		email.setStato(Stato.IN_GESTIONE);
		email.setLetto(false);

		praticaEmailIn.getDati().getAllegati().add(buildAllegato(praticaEmailIn.getDati(), "pec23294actalis3434_filettino.pdf", "filettino.pdf", praticaEmailIn.getAlfrescoPath(), 183843l, "0.9", true,
				TipoFirma.CADES, false, dateFrom, dateTo, null));

		return praticaEmailIn;
	}

	public static Set<String> getOperazioniAbilitate() {
		Set<String> operazioniAbilitate = new HashSet<String>();
		for (TipoApiTask taskApi : TipoApiTask.values())
			operazioniAbilitate.add(taskApi.name());
		for (TipoApiTaskPEC taskApi : TipoApiTaskPEC.values())
			operazioniAbilitate.add(taskApi.name());
		for (TipoApiTaskTemplate taskApi : TipoApiTaskTemplate.values())
			operazioniAbilitate.add(taskApi.name());
		for (TipoApiTaskComunicazione taskApi : TipoApiTaskComunicazione.values())
			operazioniAbilitate.add(taskApi.name());

		return operazioniAbilitate;
	}

	public static Fascicolo compilaFascicoloDummy() {
		Fascicolo fascicolo = null;
		DatiFascicolo.Builder builder = compilaDummyBuilderDatiFascicolo(TipologiaPratica.FASCICOLO.getNomeTipologia());
		DatiFascicolo dati = builder.construct();
		it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343", TipologiaPratica.EMAIL_OUT.getNomeTipologia(), new Date());
		it.eng.consolepec.xmlplugin.pratica.fascicolo.DatiFascicolo.PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(), new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);

		dati.setProtocollazioniCapofila(protocollazioniCapofila);

		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = pf.newPraticaInstance(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO), dati);

		DatiGestioneFascicoloTask.Builder builderTask = FascicoliFactory.getBuilderTask(TipologiaPratica.FASCICOLO);

		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloTask datiGestioneFascicoloTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		TaskFascicolo<?> fascicoloTask = tf.newTaskInstance(FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO), fascicolo, datiGestioneFascicoloTask);
		fascicoloTask.setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));

		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);

		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());

		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		DatoAggiuntivoValoreSingolo valoreSingolo = new DatoAggiuntivoValoreSingolo("Campo 1", "Campo1Des", TipoDato.Testo, 10, true, false, true, new ArrayList<String>(), "Valore 1");
		DatoAggiuntivoValoreSingolo valoreSingolo2 = new DatoAggiuntivoValoreSingolo("Campo 2", "Campo2Des", TipoDato.Numerico, 10, false, true, true, new ArrayList<String>(), "10");

		List<String> valori = new ArrayList<String>();
		valori.add("Valore 1 Multiplo");
		valori.add("Valore 2 Multiplo");

		DatoAggiuntivoValoreMultiplo valoreMultiplo = new DatoAggiuntivoValoreMultiplo("Campo 3", "Campo3Desc", TipoDato.MultiploTesto, 20, true, false, true, new ArrayList<String>(), valori);
		DatoAggiuntivoAnagrafica anagrafica = new DatoAggiuntivoAnagrafica("Campo 4", "Campo4Desc", TipoDato.Anagrafica, 30, true, false, true, "Valore Anagrafica", 6.0);

		fascicolo.getDati().getDatiAggiuntivi().add(valoreSingolo);
		fascicolo.getDati().getDatiAggiuntivi().add(valoreSingolo2);
		fascicolo.getDati().getDatiAggiuntivi().add(valoreMultiplo);
		fascicolo.getDati().getDatiAggiuntivi().add(anagrafica);

		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	public static TreeSet<GruppoVisibilita> getGruppoVisibilita(String nomeAllegato) {
		TreeSet<GruppoVisibilita> treeSet = new TreeSet<DatiPratica.GruppoVisibilita>();
		for (int i = 0; i < 5; i++) {
			GruppoVisibilita gruppoVisibilita = new GruppoVisibilita("gruppo_" + i + "_" + nomeAllegato);
			treeSet.add(gruppoVisibilita);
		}
		return treeSet;
	}

	public static FascicoloRiservato compilaFascicoloRiservatoDummy() {
		FascicoloRiservato fascicolo = null;
		DatiFascicoloRiservato.Builder builder = compilaDummyBuilderDatiFascicoloRiservato();
		DatiFascicoloRiservato dati = builder.construct();
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343", TipologiaPratica.EMAIL_OUT.getNomeTipologia(),
				new Date());
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(),
				new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);

		dati.setProtocollazioniCapofila(protocollazioniCapofila);

		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = (FascicoloRiservato) pf.newPraticaInstance(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO_RISERVATO), dati);

		DatiGestioneFascicoloRiservatoTask.Builder builderTask = (DatiGestioneFascicoloRiservatoTask.Builder) FascicoliFactory.getBuilderTask(TipologiaPratica.FASCICOLO_RISERVATO);

		Assegnatario assegnatario = new Assegnatario("Ruolo di prova per riscossione", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloRiservatoTask datiGestioneFascicoloRiservatoTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloRiservatoTask fascicoloTask = (GestioneFascicoloRiservatoTask) tf.newTaskInstance(FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO_RISERVATO), fascicolo,
				datiGestioneFascicoloRiservatoTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));

		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);
		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());

		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	private static List<ProtocollazioneCapofila> getProtocollazioniCapofila(DatiFascicolo dati) {
		List<ProtocollazioneCapofila> list = new ArrayList<DatiFascicolo.ProtocollazioneCapofila>();

		List<PraticaCollegata> listaPraticheCollegate = new ArrayList<DatiFascicolo.PraticaCollegata>();
		listaPraticheCollegate.add(dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(), new Date()));
		HashSet<DatiPratica.Allegato> allegati = new HashSet<DatiPratica.Allegato>();

		Allegato allegato1 = buildAllegato(dati, "allegato nome manaP", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

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

	private static List<Protocollazione> getProtocollazioniCollegate(DatiFascicolo dati) {
		List<Protocollazione> list = new ArrayList<DatiFascicolo.Protocollazione>();
		List<PraticaCollegata> listaPraticheCollegate = new ArrayList<DatiFascicolo.PraticaCollegata>();
		listaPraticheCollegate.add(dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(), new Date()));
		HashSet<DatiPratica.Allegato> allegati = new HashSet<DatiPratica.Allegato>();
		Allegato allegato1 = buildAllegato(dati, "allegato nome manaPC", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1Protocollato"));
		allegati.add(allegato1);
		dati.getAllegati().addAll(allegati);
		list.add(compilaProtocollazione(dati, "sezione", "rubrica", "234", 2014, "provenienza", "tipologiadocumento", listaPraticheCollegate, "utenteprotocollazione", "numeroFascicoloColl", 2015,
				allegati, "titiolo", new Date(), "oggetto"));
		return list;
	}

	public static void compilaTaskGestionePEC(GestionePECTask task) {
		task.setCurrentUser("UtenteTestPEC");
		task.setOperazioniAbilitate(getOperazioniAbilitate());
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

	public static DatiGestionePECTask compilaDatiTaskGestionePEC() {
		DatiGestionePECTask.Builder builder = new DatiGestionePECTask.Builder();
		builder.setAssegnatario(new DatiGestionePECTask.Assegnatario("UtentiPortale", "", new Date(), new Date((long) (System.currentTimeMillis() + Math.random() * 3600000))));
		builder.setAttivo(true);
		builder.setId(counter++);
		DatiGestionePECTask dati = builder.construct();
		dati.getAssegnatariPassati().add(new Assegnatario("UtentiProtocollo", "", new Date((long) (System.currentTimeMillis() - Math.random() * 3600000)),
				new Date((long) (System.currentTimeMillis() + Math.random() * 1800000))));

		return dati;
	}

	public static DatiGestioneFascicoloTask compilaDatiTaskGestioneFascicolo() {
		DatiGestioneFascicoloTask.Builder builder = FascicoliFactory.getBuilderTask(TipologiaPratica.FASCICOLO_PERSONALE);
		builder.setAssegnatario(new DatiGestioneFascicoloTask.Assegnatario("UtentiPortale", "", new Date(), new Date((long) (System.currentTimeMillis() + Math.random() * 3600000))));
		builder.setAttivo(true);
		builder.setId(counter++);
		DatiGestioneFascicoloTask dati = builder.construct();
		dati.getAssegnatariPassati().add(new Assegnatario("UtentiProtocollo", "", new Date((long) (System.currentTimeMillis() - Math.random() * 3600000)),
				new Date((long) (System.currentTimeMillis() + Math.random() * 1800000))));

		return dati;
	}

	public static Reader getPraticaPECStream() {
		StringReader sr = null;
		TaskFactory tf = new XMLTaskFactory();
		PraticaFactory pf = new XMLPraticaFactory();
		PraticaEmailIn pratica = compilaPraticaDummyCompleta();
		DatiGestionePECTask dati = compilaDatiTaskGestionePEC();
		GestionePECInTask task = tf.newTaskInstance(GestionePECInTask.class, pratica, dati);
		compilaTaskGestionePEC(task);
		StringWriter sw = new StringWriter();
		pf.serializePraticaInstance(sw, pratica);
		sr = new StringReader(sw.toString());
		return sr;
	}

	public static Reader getFascicoloStream() {
		StringReader sr = null;
		PraticaFactory pf = new XMLPraticaFactory();
		Fascicolo fascicolo = compilaFascicoloDummy();
		TreeSet<Allegato> allegati = fascicolo.getDati().getAllegati();
		allegati.add(buildAllegato(fascicolo.getDati(), "Allegato1", "Allegato1", fascicolo.getSubFolderPath(), 134L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null));
		allegati.add(buildAllegato(fascicolo.getDati(), "Allegato2", "Allegato2", fascicolo.getSubFolderPath(), 431L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null));

		StringWriter sw = new StringWriter();
		pf.serializePraticaInstance(sw, fascicolo);
		sr = new StringReader(sw.toString());
		return sr;
	}

	public static List<Pratica<?>> getPraticheDaProtocollare(int quantita) throws InterruptedException {

		List<Pratica<?>> pratiche = new ArrayList<Pratica<?>>();

		for (int i = 0; i <= quantita - 1; i++) {
			String path = String.valueOf(System.currentTimeMillis());
			it.eng.consolepec.xmlplugin.pratica.email.DatiEmail.Builder compilaDummyBuilderDatiEmail = compilaDummyBuilderDatiEmail(path, path);

			DatiEmail datiEmail = compilaDummyBuilderDatiEmail.construct();
			TreeSet<Allegato> allegati = datiEmail.getAllegati();
			allegati.add(buildAllegato(datiEmail, "Allegato", "Label", "ALLEGATI", 459L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null));
			PraticaEmailIn pratica = new XMLPraticaFactory().newPraticaInstance(PraticaEmailIn.class, datiEmail);
			pratiche.add(pratica);

			Thread.sleep(163);
		}

		return pratiche;
	}

	public static FascicoloAlboPretorio compilaFascicoloGareSubAppaltoDummy() {
		FascicoloAlboPretorio fascicolo = null;
		DatiFascicoloAlboPretorio.Builder builder = compilaDummyBuilderDatiFascicoloAlboPretorio();
		DatiFascicoloAlboPretorio dati = builder.construct();
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343", TipologiaPratica.EMAIL_OUT.getNomeTipologia(),
				new Date());
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(),
				new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);

		dati.setProtocollazioniCapofila(protocollazioniCapofila);

		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = pf.newPraticaInstance(FascicoloAlboPretorio.class, dati);

		DatiGestioneFascicoloAlboPretorioTask.Builder builderTask = (DatiGestioneFascicoloAlboPretorioTask.Builder) FascicoliFactory.getBuilderTask(TipologiaPratica.FASCICOLO_ALBO_PRETORIO);

		Assegnatario assegnatario = new Assegnatario("Ruolo di prova per albo pretorio", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloAlboPretorioTask datiGestioneFascicoloTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloAlboPretorioTask fascicoloTask = (GestioneFascicoloAlboPretorioTask) tf.newTaskInstance(FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO_ALBO_PRETORIO), fascicolo,
				datiGestioneFascicoloTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));

		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);

		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());

		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	public static FascicoloAlboPretorio compilaFascicoloAlboPretorioDummy() {
		FascicoloAlboPretorio fascicolo = null;
		DatiFascicoloAlboPretorio.Builder builder = compilaDummyBuilderDatiFascicoloAlboPretorio();
		DatiFascicoloAlboPretorio dati = builder.construct();
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343", TipologiaPratica.EMAIL_OUT.getNomeTipologia(),
				new Date());
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(),
				new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);

		dati.setProtocollazioniCapofila(protocollazioniCapofila);

		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = pf.newPraticaInstance(FascicoloAlboPretorio.class, dati);

		DatiGestioneFascicoloAlboPretorioTask.Builder builderTask = (DatiGestioneFascicoloAlboPretorioTask.Builder) FascicoliFactory.getBuilderTask(TipologiaPratica.FASCICOLO_ALBO_PRETORIO);

		Assegnatario assegnatario = new Assegnatario("Ruolo di prova per albo pretorio", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloAlboPretorioTask datiGestioneFascicoloTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloAlboPretorioTask fascicoloTask = (GestioneFascicoloAlboPretorioTask) tf.newTaskInstance(FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO_ALBO_PRETORIO), fascicolo,
				datiGestioneFascicoloTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));

		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);

		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());

		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	public static FascicoloPersonale compilaFascicoloPersonaleDummy() {
		FascicoloPersonale fascicolo = null;
		DatiFascicoloPersonale.Builder builder = compilaDummyBuilderDatiFascicoloPersonale();
		DatiFascicoloPersonale dati = builder.construct();

		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = (FascicoloPersonale) pf.newPraticaInstance(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO_PERSONALE), dati);

		DatiGestioneFascicoloPersonaleTask.Builder builderTask = (DatiGestioneFascicoloPersonaleTask.Builder) FascicoliFactory.getBuilderTask(TipologiaPratica.FASCICOLO_PERSONALE);

		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloPersonaleTask datiGestioneFascicoloTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloPersonaleTask fascicoloTask = (GestioneFascicoloPersonaleTask) tf.newTaskInstance(FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO_PERSONALE), fascicolo,
				datiGestioneFascicoloTask);
		fascicoloTask.setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));

		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);

		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());

		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		return fascicolo;
	}

	public static FascicoloFatturazione compilaFascicoloFatturazioneDummy() {
		FascicoloFatturazione fascicolo = null;
		DatiFascicoloFatturazione.Builder builder = compilaDummyBuilderDatiFascicoloFatturazione();
		DatiFascicoloFatturazione dati = builder.construct();
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343", TipologiaPratica.EMAIL_OUT.getNomeTipologia(),
				new Date());
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(),
				new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);

		dati.setProtocollazioniCapofila(protocollazioniCapofila);

		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = (FascicoloFatturazione) pf.newPraticaInstance(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA), dati);

		DatiGestioneFascicoloFatturazioneTask.Builder builderTask = (DatiGestioneFascicoloFatturazioneTask.Builder) FascicoliFactory.getBuilderTask(
				TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA);

		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloFatturazioneTask datiGestioneFascicoloFAtturazioneTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloFatturazioneTask fascicoloTask = (GestioneFascicoloFatturazioneTask) tf.newTaskInstance(FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA),
				fascicolo, datiGestioneFascicoloFAtturazioneTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));

		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);
		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());

		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	public static FascicoloModulistica compilaFascicoloModulisticaDummy() {
		FascicoloModulistica fascicolo = null;

		DatiFascicoloModulistica.Builder builder = compilaDummyBuilderDatiFascicoloModulistica();
		DatiFascicoloModulistica dati = builder.construct();
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343", TipologiaPratica.EMAIL_OUT.getNomeTipologia(),
				new Date());
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(),
				new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);

		dati.setProtocollazioniCapofila(protocollazioniCapofila);

		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = (FascicoloModulistica) pf.newPraticaInstance(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO_MODULISTICA), dati);

		DatiGestioneFascicoloModulisticaTask.Builder builderTask = (DatiGestioneFascicoloModulisticaTask.Builder) FascicoliFactory.getBuilderTask(TipologiaPratica.FASCICOLO_MODULISTICA);

		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloModulisticaTask datiGestioneFascicoloModulisticaTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloModulisticaTask fascicoloTask = (GestioneFascicoloModulisticaTask) tf.newTaskInstance(FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO_MODULISTICA), fascicolo,
				datiGestioneFascicoloModulisticaTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));

		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);
		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());

		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	public static PraticaModulistica compilaPraticaModulisticaDummy() {

		DatiModulistica.Builder builder = new DatiModulistica.Builder();
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/MODU_123");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("pratica molto bella");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");

		DatiModulistica dati = builder.construct();

		PraticaFactory pf = new XMLPraticaFactory();
		PraticaModulistica modulistica = pf.newPraticaInstance(PraticaModulistica.class, dati);

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));

		modulistica.getDati().getAllegati().add(allegato1);
		modulistica.getDati().getAllegati().add(allegato2);

		modulistica.getDati().setNome("Test");
		modulistica.getDati().getValori().add(new ValoreModulo("n1", "n1", "v1", "v1", true));
		modulistica.getDati().getValori().add(new ValoreModulo("n2", "n2", "v2", "v2", true));

		TabellaModulo tabella = new TabellaModulo();
		tabella.getRighe().add(new Riga(colonna("a"), colonna("b"), colonna("c")));
		tabella.getRighe().add(new Riga(colonna("a1"), colonna("b1"), colonna("c1")));
		tabella.getRighe().add(new Riga(colonna("a2"), colonna("b2"), colonna("c2")));
		modulistica.getDati().getValori().add(new ValoreModulo("n2", "n2", tabella, true));

		Sezione sezione = new Sezione();
		sezione.setTitolo("titolo");
		sezione.getNodi().add(new ValoreModulo("n1", "n1", "v1", "v1", true));
		sezione.getNodi().add(new ValoreModulo("n2", "n2", "v2", "v2", true));
		modulistica.getDati().getValori().add(sezione);

		return modulistica;
	}

	private static ValoreModulo colonna(String s) {
		return new ValoreModulo("COL_" + s, "COL_" + s, s, s, true);
	}

	public static FascicoloSportBorgoPanigale compilaFascicoloSportDummy() {
		FascicoloSportBorgoPanigale fascicolo = null;

		DatiFascicoloSportBorgoPanigale.Builder builder = compilaDummyBuilderDatiFascicoloSport();
		DatiFascicoloSportBorgoPanigale dati = builder.construct();
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343", TipologiaPratica.EMAIL_OUT.getNomeTipologia(),
				new Date());
		it.eng.consolepec.xmlplugin.pratica.riservato.DatiFascicoloRiservato.PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(),
				new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);

		dati.setProtocollazioniCapofila(protocollazioniCapofila);

		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = (FascicoloSportBorgoPanigale) pf.newPraticaInstance(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE), dati);

		DatiGestioneFascicoloSportBorgoPanigaleTask.Builder builderTask = (DatiGestioneFascicoloSportBorgoPanigaleTask.Builder) FascicoliFactory.getBuilderTask(
				TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE);

		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloSportBorgoPanigaleTask datiGestioneFascicoloModulisticaTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloSportBorgoPanigaleTask fascicoloTask = (GestioneFascicoloSportBorgoPanigaleTask) tf.newTaskInstance(
				FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE), fascicolo, datiGestioneFascicoloModulisticaTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);

		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));

		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);
		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());

		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	public static FascicoloElettoraleElettore compilaFascicoloElettoraleElettoreDummy() {
		FascicoloElettoraleElettore fascicolo = null;
		DatiFascicoloElettoraleElettore.Builder builder = compilaDummyBuilderDatiFascicoloElettoraleElettore();
		DatiFascicoloElettoraleElettore dati = builder.construct();
		it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleElettore.PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343",
				TipologiaPratica.EMAIL_OUT.getNomeTipologia(), new Date());
		it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleElettore.PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA",
				TipologiaPratica.EMAIL_IN.getNomeTipologia(), new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);
		dati.setProtocollazioniCapofila(protocollazioniCapofila);
		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = (FascicoloElettoraleElettore) pf.newPraticaInstance(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE), dati);
		DatiGestioneFascicoloElettoraleElettoreTask.Builder builderTask = (DatiGestioneFascicoloElettoraleElettoreTask.Builder) FascicoliFactory.getBuilderTask(
				TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE);
		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloElettoraleElettoreTask datiGestioneFascicoloElettoraleElettoreTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloElettoraleElettoreTask fascicoloTask = (GestioneFascicoloElettoraleElettoreTask) tf.newTaskInstance(
				FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE), fascicolo, datiGestioneFascicoloElettoraleElettoreTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));
		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);
		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());
		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		List<String> valori = new ArrayList<String>();
		valori.add("Valore 1");
		DatoAggiuntivoValoreSingolo valoreSingolo = new DatoAggiuntivoValoreSingolo("Singolo", "Descrizione", TipoDato.Data, 25, true, false, true, new ArrayList<String>(), "Valore");
		DatoAggiuntivoValoreMultiplo valoreMultiplo = new DatoAggiuntivoValoreMultiplo("Multiplo", "Descrizione", TipoDato.MultiploTesto, 35, true, false, true, new ArrayList<String>(), valori);
		DatoAggiuntivoAnagrafica anagrafica = new DatoAggiuntivoAnagrafica("Anagrafica", "Descrizione", TipoDato.Anagrafica, 45, true, false, true, "Valore Anagrafica", 6.0);
		fascicolo.getDati().getDatiAggiuntivi().add(valoreSingolo);
		fascicolo.getDati().getDatiAggiuntivi().add(valoreMultiplo);
		fascicolo.getDati().getDatiAggiuntivi().add(anagrafica);
		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	public static FascicoloElettoraleComunicazioni compilaFascicoloElettoraleComunicazioniDummy() {
		FascicoloElettoraleComunicazioni fascicolo = null;
		DatiFascicoloElettoraleComunicazioni.Builder builder = compilaDummyBuilderDatiFascicoloElettoraleComunicazioni();
		DatiFascicoloElettoraleComunicazioni dati = builder.construct();
		it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleComunicazioni.PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343",
				TipologiaPratica.EMAIL_OUT.getNomeTipologia(), new Date());
		it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleComunicazioni.PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA",
				TipologiaPratica.EMAIL_IN.getNomeTipologia(), new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);
		dati.setProtocollazioniCapofila(protocollazioniCapofila);
		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = (FascicoloElettoraleComunicazioni) pf.newPraticaInstance(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI), dati);
		DatiGestioneFascicoloElettoraleComunicazioniTask.Builder builderTask = (DatiGestioneFascicoloElettoraleComunicazioniTask.Builder) FascicoliFactory.getBuilderTask(
				TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI);
		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloElettoraleComunicazioniTask datiGestioneFascicoloElettoraleComunicazioniTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloElettoraleComunicazioniTask fascicoloTask = (GestioneFascicoloElettoraleComunicazioniTask) tf.newTaskInstance(
				FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI), fascicolo, datiGestioneFascicoloElettoraleComunicazioniTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));
		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);
		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());
		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		List<String> valori = new ArrayList<String>();
		valori.add("Valore 1");
		valori.add("Valore 2");
		DatoAggiuntivoValoreSingolo valoreSingolo = new DatoAggiuntivoValoreSingolo("Singolo", "Descrizione", TipoDato.Data, 25, true, false, true, new ArrayList<String>(), "Valore");
		DatoAggiuntivoValoreMultiplo valoreMultiplo = new DatoAggiuntivoValoreMultiplo("Multiplo", "Descrizione", TipoDato.MultiploTesto, 35, true, false, true, new ArrayList<String>(), valori);
		DatoAggiuntivoAnagrafica anagrafica = new DatoAggiuntivoAnagrafica("Anagrafica", "Descrizione", TipoDato.Anagrafica, 45, true, false, true, "Valore Anagrafica", 6.0);
		fascicolo.getDati().getDatiAggiuntivi().add(valoreSingolo);
		fascicolo.getDati().getDatiAggiuntivi().add(valoreMultiplo);
		fascicolo.getDati().getDatiAggiuntivi().add(anagrafica);
		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	public static FascicoloElettoraleGenerico compilaFascicoloElettoraleGenericoDummy() {
		FascicoloElettoraleGenerico fascicolo = null;
		DatiFascicoloElettoraleGenerico.Builder builder = compilaDummyBuilderDatiFascicoloElettoraleGenerico();
		DatiFascicoloElettoraleGenerico dati = builder.construct();
		it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleGenerico.PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343",
				TipologiaPratica.EMAIL_OUT.getNomeTipologia(), new Date());
		it.eng.consolepec.xmlplugin.pratica.elettorale.DatiFascicoloElettoraleGenerico.PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA",
				TipologiaPratica.EMAIL_IN.getNomeTipologia(), new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);
		dati.setProtocollazioniCapofila(protocollazioniCapofila);
		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = (FascicoloElettoraleGenerico) pf.newPraticaInstance(FascicoliFactory.getPraticaClass(TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO), dati);
		DatiGestioneFascicoloElettoraleGenericoTask.Builder builderTask = (DatiGestioneFascicoloElettoraleGenericoTask.Builder) FascicoliFactory.getBuilderTask(
				TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO);
		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloElettoraleGenericoTask datiGestioneFascicoloElettoraleGenericoTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloElettoraleGenericoTask fascicoloTask = (GestioneFascicoloElettoraleGenericoTask) tf.newTaskInstance(
				FascicoliFactory.getTaskClass(TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO), fascicolo, datiGestioneFascicoloElettoraleGenericoTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));
		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);
		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());
		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		List<String> valori = new ArrayList<String>();
		valori.add("Valore 1");
		DatoAggiuntivoValoreSingolo valoreSingolo = new DatoAggiuntivoValoreSingolo("Singolo", "Descrizione", TipoDato.Data, 25, true, false, true, new ArrayList<String>(), "Valore");
		DatoAggiuntivoValoreMultiplo valoreMultiplo = new DatoAggiuntivoValoreMultiplo("Multiplo", "Descrizione", TipoDato.MultiploTesto, 35, true, false, true, new ArrayList<String>(), valori);
		DatoAggiuntivoAnagrafica anagrafica = new DatoAggiuntivoAnagrafica("Anagrafica", "Descrizione", TipoDato.Anagrafica, 45, true, false, true, "Valore Anagrafica", 6.0);
		fascicolo.getDati().getDatiAggiuntivi().add(valoreSingolo);
		fascicolo.getDati().getDatiAggiuntivi().add(valoreMultiplo);
		fascicolo.getDati().getDatiAggiuntivi().add(anagrafica);
		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	public static TemplateEmail compilaTemplateEmailDummy() {

		DatiTemplateEmail.Builder builder = new DatiTemplateEmail.Builder();
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/MODU_123");
		builder.setDataCreazione(new Date());
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");

		builder.setNome("Nome");
		builder.setDescrizione("Descrizione");
		builder.setOggettoMail("Oggetto");
		builder.setCorpoMail("Corpo");
		builder.setMittente("Mittente");

		builder.setDestinatari(Arrays.asList("Destinatario"));

		builder.setDestinatariCC(Arrays.asList("DestinatarioCC_1", "DestinatarioCC_2", "DestinatarioCC_3"));

		List<CampoTemplate> campi = new ArrayList<CampoTemplate>();
		campi.add(new CampoTemplate("campo1", TipoCampoTemplate.DOUBLE, "formato1", "regex1", 5, new ArrayList<String>(), null));
		campi.add(new CampoTemplate("campo2", TipoCampoTemplate.DATE, "formato2", "regex2", 10, new ArrayList<String>(), null));

		builder.setCampi(campi);

		builder.setFascicoliAbilitati(Arrays.asList("FASCICOLO_CONTRASSEGNO", "FASCICOLO_TITOLARE"));

		builder.setGruppiVisibilita(Arrays.asList(new GruppoVisibilita("DOC_00211_Mobilita Coordinatori"), new GruppoVisibilita("DOC_00211_Mobilita Supervisori")));

		Utente utente = new Utente("rugull", "Ruud", "Gullit", "XXXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXXX", new Date());
		builder.setInCaricoA(utente);

		DatiTemplateEmail dati = builder.construct();

		PraticaFactory pf = new XMLPraticaFactory();
		TemplateEmail template = pf.newPraticaInstance(TemplateEmail.class, dati);

		return template;
	}

	public static TemplateDocumentoPDF compilaTemplateDocPDFDummy() throws Exception {

		DatiTemplateDocumentoPDF.Builder builder = new DatiTemplateDocumentoPDF.Builder();
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/MODU_123");
		builder.setDataCreazione(new Date());
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");

		builder.setNome("Nome");
		builder.setDescrizione("Descrizione");

		// builder.setDocumentoODT( DatiPratica.new Allegato("file.txt", "/CONSOLE/PRATICHE/MODU_123", "1.0")); // NON VA BENE CI VUOLE IL TASK

		List<CampoTemplate> campi = new ArrayList<CampoTemplate>();
		campi.add(new CampoTemplate("campo1", TipoCampoTemplate.DOUBLE, "formato1", "regex1", 5, new ArrayList<String>(), null));
		campi.add(new CampoTemplate("campo2", TipoCampoTemplate.DATE, "formato2", "regex2", 10, new ArrayList<String>(), null));

		builder.setCampi(campi);

		builder.setFascicoliAbilitati(Arrays.asList("FASCICOLO_CONTRASSEGNO", "FASCICOLO_TITOLARE"));

		builder.setGruppiVisibilita(Arrays.asList(new GruppoVisibilita("DOC_00211_Mobilita Coordinatori"), new GruppoVisibilita("DOC_00211_Mobilita Supervisori")));

		Utente utente = new Utente("rugull", "Ruud", "Gullit", "XXXXXXXXXXXXXXXX", "XXXXXXXXXXXXXXXX", new Date());
		builder.setInCaricoA(utente);

		DatiTemplateDocumentoPDF dati = builder.construct();

		PraticaFactory pf = new XMLPraticaFactory();
		TemplateDocumentoPDF template = pf.newPraticaInstance(TemplateDocumentoPDF.class, dati);

		DatiGestioneTemplateDocumentoPDFTask.Builder builderTask = new DatiGestioneTemplateDocumentoPDFTask.Builder();

		Assegnatario assegnatario = new Assegnatario("DOC_00211_Mobilita Coordinatori", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);

		TaskFactory factory = new XMLTaskFactory();
		GestioneTemplateDocumentoPDFTask newTaskInstance = factory.newTaskInstance(GestioneTemplateDocumentoPDFTask.class, template, builderTask.construct());
		newTaskInstance.setCurrentUser("Ataulfo");

		return template;
	}

	public static Comunicazione compilaComunicazioneDummy() {
		DatiComunicazione.Builder builder = new DatiComunicazione.Builder();

		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/MODU_123");
		builder.setDataCreazione(new Date());
		builder.setIdDocumentale("" + 123);
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");

		builder.setIdComunicazione(1);
		builder.setCodComunicazione("Codice");
		builder.setDescComunicazione("Descrizione");
		builder.setStato(DatiComunicazione.Stato.IN_GESTIONE);

		DatiComunicazione dati = builder.construct();
		Invio invio = new Invio();
		invio.setCodEsito("OK");
		invio.setFlgTestProd("T");
		invio.setNumRecordTest(10);
		invio.setPecDestinazioneTest("d@c.c");

		dati.getInvii().add(invio);

		PraticaFactory pf = new XMLPraticaFactory();
		Comunicazione comunicazione = pf.newPraticaInstance(Comunicazione.class, dati);

		DatiGestioneComunicazioneTask.Builder builderTask = new DatiGestioneComunicazioneTask.Builder();
		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		builderTask.setAttivo(true);
		DatiGestioneComunicazioneTask datiGestioneComunicazione = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneComunicazioneTask task = tf.newTaskInstance(GestioneComunicazioneTask.class, comunicazione, datiGestioneComunicazione);
		task.setOperazioniAbilitate(getOperazioniAbilitate());

		return comunicazione;
	}

	public static Fascicolo compilaFascicoloGenericoDummy() {
		Fascicolo fascicolo = null;
		DatiFascicolo.Builder builder = compilaDummyBuilderDatiFascicoloGenerico();
		DatiFascicolo dati = builder.construct();
		PraticaCollegata pc = dati.new PraticaCollegata("/CONSOLE/PEC/IN/343", TipologiaPratica.EMAIL_OUT.getNomeTipologia(), new Date());
		PraticaCollegata pc2 = dati.new PraticaCollegata("PATH/DI/UNA/PRATICA", TipologiaPratica.EMAIL_IN.getNomeTipologia(), new Date());

		List<ProtocollazioneCapofila> protocollazioniCapofila = getProtocollazioniCapofila(dati);
		dati.setProtocollazioniCapofila(protocollazioniCapofila);
		PraticaFactory pf = new XMLPraticaFactory();
		fascicolo = pf.newPraticaInstance(FascicoliFactory.getPraticaClass("FASCICOLO_GENERICO"), dati);
		DatiGestioneFascicoloTask.Builder builderTask = FascicoliFactory.getBuilderTask("FASCICOLO_GENERICO");
		Assegnatario assegnatario = new Assegnatario("Ruolo di prova", "", new Date(), new Date(System.currentTimeMillis() + 1000));
		builderTask.setAssegnatario(assegnatario);
		DatiGestioneFascicoloTask datiGestioneFascicoloTask = builderTask.construct();
		TaskFactory tf = new XMLTaskFactory();
		GestioneFascicoloGenericoTask fascicoloTask = (GestioneFascicoloGenericoTask) tf.newTaskInstance(FascicoliFactory.getTaskClass("FASCICOLO_GENERICO"), fascicolo, datiGestioneFascicoloTask);
		((XMLTaskFascicolo<?>) fascicoloTask).setCurrentUser("CiaoCiao");
		fascicoloTask.setOperazioniAbilitate(getOperazioniAbilitate());

		Allegato allegato1 = buildAllegato(dati, "allegato nome mana", "allegato label mana", "ALLEGATI", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		Allegato allegato2 = buildAllegato(dati, "allegato nome", "allegato label", "MESSAGGIO", 234L, "0.9", true, TipoFirma.CADES, false, dateFrom, dateTo, null);
		allegato1.setGruppiVisibilita(getGruppoVisibilita("Allegato1"));
		allegato2.setGruppiVisibilita(getGruppoVisibilita("Allegato2"));
		fascicolo.getDati().getAllegati().add(allegato1);
		fascicolo.getDati().getAllegati().add(allegato2);
		Set<Allegato> set = new HashSet<Allegato>(fascicolo.getDati().getAllegati());
		fascicolo.getDati().getAllegati().clear();
		fascicolo.getDati().getAllegati().addAll(new ArrayList<Allegato>(set));

		List<String> valori = new ArrayList<String>();

		DatoAggiuntivoValoreSingolo valoreSingolo = new DatoAggiuntivoValoreSingolo("Singolo", "Descrizione", TipoDato.Data, 25, true, false, true, new ArrayList<String>(), "Valore");

		DatoAggiuntivoValoreMultiplo valoreMultiplo = new DatoAggiuntivoValoreMultiplo("Multiplo", "Descrizione", TipoDato.MultiploTesto, 35, true, false, true, new ArrayList<String>(), valori);

		DatoAggiuntivoAnagrafica anagrafica = new DatoAggiuntivoAnagrafica("Anagrafica", "Descrizione", TipoDato.Anagrafica, 45, true, false, true, "Valore Anagrafica", 6.0);

		fascicolo.getDati().getDatiAggiuntivi().add(valoreSingolo);
		fascicolo.getDati().getDatiAggiuntivi().add(valoreMultiplo);
		fascicolo.getDati().getDatiAggiuntivi().add(anagrafica);
		fascicolo.addPraticaCollegata(pc);
		fascicolo.addPraticaCollegata(pc2);
		return fascicolo;
	}

	private static DatiFascicolo.Builder compilaDummyBuilderDatiFascicoloGenerico() {
		Builder builder = FascicoliFactory.getBuilder("FASCICOLO_GENERICO");
		builder.setConsoleFileName(Pratica.CONSOLE_XML_FILE_NAME);
		builder.setFolderPath("/CONSOLE/PRATICHE/ENT_PG_123_2013");
		builder.setProvenienza("settore bim bum bam");
		builder.setDataCreazione(new Date());
		builder.setTitolo("pratica molto bella");
		builder.setNote("Note di creazione editabili");
		builder.setIdDocumentale("" + 123);
		builder.setTipologiaPratica(new TipologiaPratica("FASCICOLO_GENERICO"));
		builder.setUtenteCreazione("Ruud Gullit");
		builder.setUsernameCreazione("rugull");
		return builder;
	}

	public static GestionePECOutTask recuperaTaskGestionePECOut(PraticaEmailOut mail) {
		Set<Task<?>> tasks = mail.getTasks();
		GestionePECOutTask task = null;
		for (Task<?> t : tasks) {
			if (t instanceof GestionePECOutTask && t.isAttivo()) {
				task = (GestionePECOutTask) t;
				task.setCurrentUser("Test user");
				task.setOperazioniAbilitate(TestUtils.getOperazioniAbilitate());
				return task;
			}
		}

		return null;
	}

	public static Allegato buildAllegato(DatiPratica dati, String nome, String label, String folderRelativePath, Long dimensioneByte, String currentVersion, boolean firmato, TipoFirma tipoFirma,
			boolean firmatoHash, Date dataInizioPubblicazione, Date dataFinePubblicazione, Date dataCaricamento) {

		return dati.new Allegato(nome, label, folderRelativePath, null, null, dimensioneByte, currentVersion, firmato, tipoFirma, firmatoHash, dataInizioPubblicazione, dataFinePubblicazione,
				dataCaricamento, null, null, null);
	}
}
