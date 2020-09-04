package it.eng.cobo.consolepec.util.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import it.eng.cobo.consolepec.commons.configurazioni.AbilitazioniRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaComunicazione;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaEmailOut;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaIngresso;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaModello;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaPraticaModulistica;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaWorklist;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.*;
import it.eng.cobo.consolepec.commons.configurazioni.properties.ProprietaGenerali;
import it.eng.cobo.consolepec.commons.datigenerici.AnagraficaAmministrazione;
import it.eng.cobo.consolepec.commons.datigenerici.IndirizzoEmail;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente;
import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.cobo.consolepec.commons.drive.Nomenclatura;
import it.eng.cobo.consolepec.commons.drive.metadato.Metadato;
import it.eng.cobo.consolepec.commons.drive.metadato.MetadatoMultiplo;
import it.eng.cobo.consolepec.commons.drive.metadato.MetadatoSingolo;
import it.eng.cobo.consolepec.commons.drive.permessi.CreaElementoPermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.EliminaElementoPermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.ModificaElementoPermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.ModificaPermessiElementoPermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.PermessoDrive;
import it.eng.cobo.consolepec.commons.drive.permessi.VisualizzaElementoPermessoDrive;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoAnagrafica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoTabella;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreMultiplo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivoValoreSingolo;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.TipoDato;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione.CambiaStepIterCondizioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione.ModificaDatoAggiuntivoValoreCondizioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione.ModificaDatoAggiuntivoValoriCondizioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.CondizioneEsecuzione.RibaltaDatiAggiuntiviDaAnagraficaCondizioneEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.CambiaStepIterConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.ModificaDatoAggiuntivoValoreConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.ModificaDatoAggiuntivoValoriConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione.RibaltaDatiAggiuntiviDaAnagraficaConseguenzaEsecuzione;
import it.eng.cobo.consolepec.commons.profilazione.Settore;
import it.eng.cobo.consolepec.commons.rubrica.Anagrafica;
import it.eng.cobo.consolepec.commons.rubrica.PersonaFisica;
import it.eng.cobo.consolepec.commons.rubrica.PersonaGiuridica;
import it.eng.cobo.consolepec.commons.spagic.Message.Attachment;
import it.eng.cobo.consolepec.util.configurazioni.ConfigurazioniUtil;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.datigenerici.DatiGenericiUtil;

public class ConsolePecJsonConfiguration extends JsonConfiguration {

	@Override
	public void configure(GsonBuilder builder) {
		builder.setDateFormat(ConsoleConstants.FORMATO_ISO8601);
		builder.registerTypeAdapter(Map.class, new MapDeserializer());

		for (RuntimeTypeAdapterFactory<?> factory : factories)
			builder.registerTypeAdapterFactory(factory);

		builder.addSerializationExclusionStrategy(new NotSerializableExclusionStrategy());

	}

	private final List<RuntimeTypeAdapterFactory<?>> factories = new ArrayList<RuntimeTypeAdapterFactory<?>>();
	{
		factories.add(rubricaFactory);
		factories.add(metadatoFactory);
		factories.add(datoAggiuntivoFactory);
		factories.add(driveElementFactory);
		factories.add(condizioneEsecuzioneFactory);
		factories.add(conseguenzaEsecuzioneFactory);
		factories.add(tipologiaPraticaFactory);
		factories.add(anagraficaAmministrazioneFactory);
		factories.add(indirizzoEmailFactory);
		factories.add(preferenzeUtenteFactory);
		factories.add(proprietaGeneraliFactory);
		factories.add(settoreFactory);
		factories.add(anagraficaRuoloFactory);
		factories.add(anagraficaWorklistFactory);
		factories.add(abilitazioniRuoloFactory);
		factories.add(dizionarioFactory);
		factories.add(nomenclaturaFactory);
		factories.add(permessiDriveFactory);
		factories.add(abilitazioneFactory);
		factories.add(anagraficaIngressoFactory);
		factories.add(anagraficaFascicoloFactory);
		factories.add(anagraficaComunicazioneFactory);
		factories.add(anagraficaPraticaModulisticaFactory);
		factories.add(anagraficaEmailOutFactory);
		factories.add(anagraficaModelloFactory);
	}

	private static RuntimeTypeAdapterFactory<Anagrafica> rubricaFactory = RuntimeTypeAdapterFactory.of(Anagrafica.class) //
			.registerPredicate(PersonaFisica.class, RuntimeTypeAdapterFactory.hasFields("codiceFiscale")) //
			.registerPredicate(PersonaGiuridica.class, RuntimeTypeAdapterFactory.hasFields("partitaIva"));

	private static RuntimeTypeAdapterFactory<Metadato> metadatoFactory = RuntimeTypeAdapterFactory.of(Metadato.class) //
			.registerPredicate(MetadatoSingolo.class, RuntimeTypeAdapterFactory.hasFields("valore")) //
			.registerPredicate(MetadatoMultiplo.class, RuntimeTypeAdapterFactory.hasFields("valori"));

	private static RuntimeTypeAdapterFactory<DatoAggiuntivo> datoAggiuntivoFactory = RuntimeTypeAdapterFactory.of(DatoAggiuntivo.class) //
			.registerPredicate(DatoAggiuntivoTabella.class, RuntimeTypeAdapterFactory.fieldEquals("tipo", TipoDato.Tabella.name())) //
			.registerPredicate(DatoAggiuntivoValoreMultiplo.class, RuntimeTypeAdapterFactory.fieldEquals("tipo", TipoDato.MultiploTesto.name(), TipoDato.MultiploRicerca.name())) //
			.registerPredicate(DatoAggiuntivoAnagrafica.class, RuntimeTypeAdapterFactory.fieldEquals("tipo", TipoDato.Anagrafica.name())) //
			.registerPredicate(DatoAggiuntivoValoreSingolo.class, RuntimeTypeAdapterFactory.deserDefault()); //

	private static RuntimeTypeAdapterFactory<DriveElement> driveElementFactory = RuntimeTypeAdapterFactory.of(DriveElement.class) //
			.registerPredicate(Cartella.class, RuntimeTypeAdapterFactory.fieldEquals("cartella", Boolean.toString(true))) //
			.registerPredicate(File.class, RuntimeTypeAdapterFactory.fieldEquals("cartella", Boolean.toString(false)));

	private static RuntimeTypeAdapterFactory<CondizioneEsecuzione> condizioneEsecuzioneFactory = RuntimeTypeAdapterFactory.of(CondizioneEsecuzione.class, "tipo") //
			.registerSubtype(CambiaStepIterCondizioneEsecuzione.class, true) //
			.registerSubtype(ModificaDatoAggiuntivoValoreCondizioneEsecuzione.class, true) //
			.registerSubtype(ModificaDatoAggiuntivoValoriCondizioneEsecuzione.class, true) //
			.registerSubtype(RibaltaDatiAggiuntiviDaAnagraficaCondizioneEsecuzione.class, true);

	private static RuntimeTypeAdapterFactory<ConseguenzaEsecuzione> conseguenzaEsecuzioneFactory = RuntimeTypeAdapterFactory.of(ConseguenzaEsecuzione.class, "tipo") //
			.registerSubtype(CambiaStepIterConseguenzaEsecuzione.class, true) //
			.registerSubtype(ModificaDatoAggiuntivoValoreConseguenzaEsecuzione.class, true) //
			.registerSubtype(ModificaDatoAggiuntivoValoriConseguenzaEsecuzione.class, true) //
			.registerSubtype(RibaltaDatiAggiuntiviDaAnagraficaConseguenzaEsecuzione.class, true);

	private static RuntimeTypeAdapterFactory<TipologiaPratica> tipologiaPraticaFactory = RuntimeTypeAdapterFactory.of(TipologiaPratica.class, "tipologiaConfigurazione") //
			.registerSubtype(AnagraficaFascicolo.class, ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_FASCICOLO, false) //
			.registerSubtype(AnagraficaIngresso.class, ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_INGRESSO, false) //
			.registerSubtype(AnagraficaComunicazione.class, ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_COMUNICAZIONE, false) //
			.registerSubtype(AnagraficaPraticaModulistica.class, ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_PRATICA_MODULISTICA, false) //
			.registerSubtype(AnagraficaEmailOut.class, ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_MAIL_OUT, false) //
			.registerSubtype(AnagraficaModello.class, ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_MODELLO, false);

	private static RuntimeTypeAdapterFactory<AnagraficaIngresso> anagraficaIngressoFactory = RuntimeTypeAdapterFactory.of(AnagraficaIngresso.class, "tipologiaConfigurazione",
			ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_INGRESSO);

	private static RuntimeTypeAdapterFactory<AnagraficaFascicolo> anagraficaFascicoloFactory = RuntimeTypeAdapterFactory.of(AnagraficaFascicolo.class, "tipologiaConfigurazione",
			ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_FASCICOLO);

	private static RuntimeTypeAdapterFactory<AnagraficaComunicazione> anagraficaComunicazioneFactory = RuntimeTypeAdapterFactory.of(AnagraficaComunicazione.class, "tipologiaConfigurazione",
			ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_COMUNICAZIONE);

	private static RuntimeTypeAdapterFactory<AnagraficaPraticaModulistica> anagraficaPraticaModulisticaFactory = RuntimeTypeAdapterFactory.of(AnagraficaPraticaModulistica.class,
			"tipologiaConfigurazione", ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_PRATICA_MODULISTICA);

	private static RuntimeTypeAdapterFactory<AnagraficaEmailOut> anagraficaEmailOutFactory = RuntimeTypeAdapterFactory.of(AnagraficaEmailOut.class, "tipologiaConfigurazione",
			ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_MAIL_OUT);

	private static RuntimeTypeAdapterFactory<AnagraficaModello> anagraficaModelloFactory = RuntimeTypeAdapterFactory.of(AnagraficaModello.class, "tipologiaConfigurazione",
			ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_MODELLO);

	public static RuntimeTypeAdapterFactory<AnagraficaAmministrazione> anagraficaAmministrazioneFactory = RuntimeTypeAdapterFactory.of(AnagraficaAmministrazione.class, "tipologiaDatoGenerico",
			DatiGenericiUtil.NOME_TIPOLOGIA_ANAGRAFICA_AMMINISTRAZIONE);

	public static RuntimeTypeAdapterFactory<IndirizzoEmail> indirizzoEmailFactory = RuntimeTypeAdapterFactory.of(IndirizzoEmail.class, "tipologiaDatoGenerico",
			DatiGenericiUtil.NOME_TIPOLOGIA_INDIRIZZO_EMAIL); //

	private static RuntimeTypeAdapterFactory<ProprietaGenerali> proprietaGeneraliFactory = RuntimeTypeAdapterFactory.of(ProprietaGenerali.class, "tipologiaConfigurazione",
			ConfigurazioniUtil.NOME_TIPOLOGIA_PROPERIETA_GENERALI); //

	private static RuntimeTypeAdapterFactory<PreferenzeUtente> preferenzeUtenteFactory = RuntimeTypeAdapterFactory.of(PreferenzeUtente.class, "tipologiaConfigurazione",
			ConfigurazioniUtil.NOME_TIPOLOGIA_PREFERENZE_UTENTE); //

	private static RuntimeTypeAdapterFactory<Settore> settoreFactory = RuntimeTypeAdapterFactory.of(Settore.class, "tipologiaConfigurazione", ConfigurazioniUtil.NOME_TIPOLOGIA_SETTORE); //

	private static RuntimeTypeAdapterFactory<AnagraficaRuolo> anagraficaRuoloFactory = RuntimeTypeAdapterFactory.of(AnagraficaRuolo.class, "tipologiaConfigurazione",
			ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_RUOLO); //

	private static RuntimeTypeAdapterFactory<AnagraficaWorklist> anagraficaWorklistFactory = RuntimeTypeAdapterFactory.of(AnagraficaWorklist.class, "tipologiaConfigurazione",
			ConfigurazioniUtil.NOME_TIPOLOGIA_ANAGRAFICA_WORKLIST); //

	private static RuntimeTypeAdapterFactory<AbilitazioniRuolo> abilitazioniRuoloFactory = RuntimeTypeAdapterFactory.of(AbilitazioniRuolo.class, "tipologiaConfigurazione",
			ConfigurazioniUtil.NOME_TIPOLOGIA_ABILITAZIONE_RUOLO); //

	private static RuntimeTypeAdapterFactory<Dizionario> dizionarioFactory = RuntimeTypeAdapterFactory.of(Dizionario.class, "tipologiaConfigurazioneDrive", "DIZIONARIO");

	private static RuntimeTypeAdapterFactory<Nomenclatura> nomenclaturaFactory = RuntimeTypeAdapterFactory.of(Nomenclatura.class, "tipologiaConfigurazioneDrive", "NOMENCLATURA");

	public static RuntimeTypeAdapterFactory<PermessoDrive> permessiDriveFactory = RuntimeTypeAdapterFactory.of(PermessoDrive.class, "tipoPermesso") //
			.registerSubtype(CreaElementoPermessoDrive.class, true) //
			.registerSubtype(ModificaElementoPermessoDrive.class, true) //
			.registerSubtype(EliminaElementoPermessoDrive.class, true) //
			.registerSubtype(ModificaPermessiElementoPermessoDrive.class, true) //
			.registerSubtype(VisualizzaElementoPermessoDrive.class, true);

	private static RuntimeTypeAdapterFactory<Abilitazione> abilitazioneFactory = RuntimeTypeAdapterFactory.of(Abilitazione.class, "tipoAbilitazione") //
			.registerSubtype(AmministrazioneFascicoliAbilitazione.class, true) //
			.registerSubtype(AmministrazioneIngressiAbilitazione.class, true) //
			.registerSubtype(AmministrazioneRuoliAbilitazione.class, true) //
			.registerSubtype(CreazioneComunicazioneAbilitazione.class, true) //
			.registerSubtype(CreazioneEmailOutAbilitazione.class, true) //
			.registerSubtype(CreazioneFascicoloAbilitazione.class, true) //
			.registerSubtype(CreazioneModelloAbilitazione.class, true) //
			.registerSubtype(CreazionePraticaModulisticaAbilitazione.class, true) //
			.registerSubtype(GestioneRubricaAbilitazione.class, true) //
			.registerSubtype(EstrazioneAmiantoAbilitazione.class, true) //
			.registerSubtype(FiltroDatoAggiuntivoAbilitazione.class, true) //
			.registerSubtype(ModificaComunicazioneAbilitazione.class, true) //
			.registerSubtype(ModificaEmailOutAbilitazione.class, true) //
			.registerSubtype(ModificaFascicoloAbilitazione.class, true) //
			.registerSubtype(ModificaIngressoAbilitazione.class, true) //
			.registerSubtype(ModificaModelloAbilitazione.class, true) //
			.registerSubtype(ModificaPraticaModulisticaAbilitazione.class, true) //
			.registerSubtype(ModificaRuoloAbilitazione.class, true) //
			.registerSubtype(VisibilitaRuoloAbilitazione.class, true) //
			.registerSubtype(RicercaLiberaAbilitazione.class, true) //
			.registerSubtype(VisibilitaComunicazioneAbilitazione.class, true) //
			.registerSubtype(VisibilitaFascicoloAbilitazione.class, true) //
			.registerSubtype(VisibilitaModelloAbilitazione.class, true) //
			.registerSubtype(VisibilitaPraticaModulisticaAbilitazione.class, true) //
			.registerSubtype(WorklistAbilitazione.class, true) //
			.registerSubtype(LetturaIngressoAbilitazione.class, true) //
			.registerSubtype(CartellaFirmaAbilitazione.class, true) //
			.registerSubtype(ImportazioneLagAbilitazione.class, true) //
			.registerSubtype(GestioneDriveAbilitazione.class, true);

	public static class SpagicAttachmentSerializer implements JsonSerializer<Attachment>, JsonDeserializer<Attachment> {

		@Override
		public JsonElement serialize(Attachment isw, Type type, JsonSerializationContext context) {

			if (isw == null || isw.getInputStream() == null) {
				return context.serialize(null);
			}

			try {
				String encoded = Base64.encodeBase64String(IOUtils.toByteArray(isw.getInputStream())); // UTF-8
				return context.serialize(encoded, String.class);

			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		}

		@Override
		public Attachment deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {

			if (jsonElement.isJsonNull()) {
				return null;
			}

			return new Attachment(new ByteArrayInputStream(Base64.decodeBase64(jsonElement.getAsString())));
		}
	}

}
