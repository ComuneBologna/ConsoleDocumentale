package it.eng.consolepec.xmlplugin.factory;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.google.common.base.Predicate;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.consolepec.xmlplugin.exception.PraticaException;
import it.eng.consolepec.xmlplugin.jaxb.Pratica.Tasks;
import it.eng.consolepec.xmlplugin.pratica.albopretorio.XMLFascicoloAlboPretorio;
import it.eng.consolepec.xmlplugin.pratica.comunicazione.XMLComunicazione;
import it.eng.consolepec.xmlplugin.pratica.elettorale.XMLFascicoloElettoraleComunicazioni;
import it.eng.consolepec.xmlplugin.pratica.elettorale.XMLFascicoloElettoraleElettore;
import it.eng.consolepec.xmlplugin.pratica.elettorale.XMLFascicoloElettoraleGenerico;
import it.eng.consolepec.xmlplugin.pratica.email.XMLPraticaEmailDaEprotocollo;
import it.eng.consolepec.xmlplugin.pratica.email.XMLPraticaEmailIn;
import it.eng.consolepec.xmlplugin.pratica.email.XMLPraticaEmailOut;
import it.eng.consolepec.xmlplugin.pratica.fascicolo.XMLFascicolo;
import it.eng.consolepec.xmlplugin.pratica.fatturazione.XMLFascicoloFatturazione;
import it.eng.consolepec.xmlplugin.pratica.modulistica.XMLFascicoloModulistica;
import it.eng.consolepec.xmlplugin.pratica.modulistica.XMLPraticaModulistica;
import it.eng.consolepec.xmlplugin.pratica.personale.XMLFascicoloPersonale;
import it.eng.consolepec.xmlplugin.pratica.riservato.XMLFascicoloRiservato;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSaluteSport;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportBorgoPanigale;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportBorgoPanigaleReno;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportNavile;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportPorto;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportPortoSaragozza;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportReno;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportSanDonato;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportSanDonatoSanVitale;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportSantoStefano;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportSaragozza;
import it.eng.consolepec.xmlplugin.pratica.sport.XMLFascicoloSportSavena;
import it.eng.consolepec.xmlplugin.pratica.template.XMLTemplateDocumentoPDF;
import it.eng.consolepec.xmlplugin.pratica.template.XMLTemplateEmail;
import it.eng.consolepec.xmlplugin.versioning.XMLVersionUpdater;
import it.eng.consolepec.xmlplugin.versioning.XMLVersionUpdaterFactory;

public abstract class AbstractPraticaFactory extends PraticaFactory {
	private Logger logger = LoggerFactory.getLogger(XMLPraticaFactory.class);

	public AbstractPraticaFactory() {
		getJaxbContext();
	}

	private static JAXBContext jaxbContext = null;

	protected JAXBContext getJaxbContext() {
		if (jaxbContext == null) {
			synchronized (this) {
				if (jaxbContext == null) {
					try {
						jaxbContext = JAXBContext.newInstance(it.eng.consolepec.xmlplugin.jaxb.Pratica.class);
					} catch (JAXBException e) {
						logger.error("Errore nella creazione del contesto Jaxb.", e);
					}
				}
			}
		}
		return jaxbContext;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Pratica<?> loadPratica(Reader reader) {
		try {

			String xmlPraticaString = readerToString(reader);
			reader = checkAndUpdateVersion(new StringReader(xmlPraticaString));
			xmlPraticaString = readerToString(reader);

			logger.debug("loadDatiPratica");

			it.eng.consolepec.xmlplugin.jaxb.Pratica praticaJaxb = deserialize(xmlPraticaString);
			logger.debug("Identificato tipo {} da file xml", praticaJaxb.getTipo());

			XMLPratica<?> pratica = null;

			if (PraticaUtil.isFascicolo(praticaJaxb.getTipo())) {
				if (TipologiaPratica.FASCICOLO.getNomeTipologia().equals(praticaJaxb.getTipo()) || TipologiaPratica.FASCICOLO_PERSONALE.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloPersonale();

				} else if (TipologiaPratica.FASCICOLO_ALBO_PRETORIO.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloAlboPretorio();

				} else if (TipologiaPratica.FASCICOLO_RISERVATO.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloRiservato();

				} else if (TipologiaPratica.FASCICOLO_FATTURAZIONE_ELETTRONICA.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloFatturazione();

				} else if (TipologiaPratica.FASCICOLO_ELETTORALE_ELETTORE.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloElettoraleElettore();

				} else if (TipologiaPratica.FASCICOLO_ELETTORALE_COMUNICAZIONI.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloElettoraleComunicazioni();

				} else if (TipologiaPratica.FASCICOLO_ELETTORALE_GENERICO.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloElettoraleGenerico();

				} else if (TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportBorgoPanigale();

				} else if (TipologiaPratica.FASCICOLO_SPORT_NAVILE.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportNavile();

				} else if (TipologiaPratica.FASCICOLO_SPORT_PORTO.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportPorto();

				} else if (TipologiaPratica.FASCICOLO_SPORT_RENO.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportReno();

				} else if (TipologiaPratica.FASCICOLO_SPORT_SANDONATO.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportSanDonato();

				} else if (TipologiaPratica.FASCICOLO_SPORT_SANTOSTEFANO.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportSantoStefano();

				} else if (TipologiaPratica.FASCICOLO_SPORT_SANVITALE.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportNavile();

				} else if (TipologiaPratica.FASCICOLO_SPORT_SARAGOZZA.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportSaragozza();

				} else if (TipologiaPratica.FASCICOLO_SPORT_SAVENA.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportSavena();

				} else if (TipologiaPratica.FASCICOLO_SPORT_PORTO_SARAGOZZA.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportPortoSaragozza();

				} else if (TipologiaPratica.FASCICOLO_SPORT_BORGOPANIGALE_RENO.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportBorgoPanigaleReno();

				} else if (TipologiaPratica.FASCICOLO_SPORT_SANDONATO_SANVITALE.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSportSanDonatoSanVitale();

				} else if (TipologiaPratica.FASCICOLO_SALUTE_SPORT.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloSaluteSport();

				} else if (TipologiaPratica.FASCICOLO_MODULISTICA.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLFascicoloModulistica();

				} else {
					pratica = new XMLFascicolo(); // TODO
				}

			} else if (PraticaUtil.isComunicazione(praticaJaxb.getTipo())) {
				pratica = new XMLComunicazione();

			} else if (PraticaUtil.isPraticaModulistica(praticaJaxb.getTipo())) {
				pratica = new XMLPraticaModulistica();

			} else if (PraticaUtil.isEmailOut(praticaJaxb.getTipo())) {
				pratica = new XMLPraticaEmailOut();

			} else if (PraticaUtil.isModello(praticaJaxb.getTipo())) {

				if (TipologiaPratica.MODELLO_MAIL.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLTemplateEmail();

				} else if (TipologiaPratica.MODELLO_PDF.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLTemplateDocumentoPDF();
				}

			} else if (PraticaUtil.isIngresso(praticaJaxb.getTipo())) {

				if (TipologiaPratica.EMAIL_IN.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLPraticaEmailIn();

				} else if (TipologiaPratica.EMAIL_EPROTOCOLLO.getNomeTipologia().equals(praticaJaxb.getTipo())) {
					pratica = new XMLPraticaEmailDaEprotocollo();
				}

			}

			if (pratica != null)
				return loadPratica((XMLPratica<DatiPratica>) pratica, new StringReader(xmlPraticaString));

			throw new PraticaException("Nessun tipo pratica trovato, per l'xml passato: " + praticaJaxb.getTipo());

		} catch (Throwable t) {
			logger.error("Errore in fase di caricamento del file XML", t);
			throw new PraticaException(t, "Errore in fase di caricamento del file XML");
		}

	}

	protected <T extends Pratica<?>> T loadPratica(XMLPratica<DatiPratica> pratica, Reader reader) throws Exception {

		String xmlPraticaString = readerToString(reader);
		reader = checkAndUpdateVersion(new StringReader(xmlPraticaString));
		xmlPraticaString = readerToString(reader);

		XMLVersionUpdater.ValidationResult valRes = null;
		try {
			valRes = validateAgainstXSD(new StringReader(xmlPraticaString.toString()));
		} catch (Throwable t) {
			throw new PraticaException("Errore in fase di controllo validazione dell'XML. " + t.getLocalizedMessage());
		}

		// carico la pratica
		logger.debug("loadDatiPratica");
		it.eng.consolepec.xmlplugin.jaxb.Pratica praticaJaxb = deserialize(xmlPraticaString);

		pratica.loadDati(praticaJaxb);
		pratica.getDati().set_version(praticaJaxb.getVersion());
		pratica.getDati().set_sync(praticaJaxb.isSync());

		// se la validazione KO setto il FLAG
		if (valRes != null && !valRes.isValid()) {
			// logger.warn("L'XML serializzato non ha superato la validazione. Messaggio: {}", valRes.getErrorMsg());
			pratica.setXmlValid(false);
		} else {
			pratica.setXmlValid(true);
		}

		/**
		 * caricamento dei task
		 */
		TaskFactory tf = getTaskFactory();
		Set<Task<?>> tasks = tf.loadPraticaTasks(new StringReader(xmlPraticaString), pratica);
		for (Task<?> task : tasks) {
			pratica.putTask((XMLTask<?>) task);// safe cast, la factory è
												// di tipo xml
		}
		@SuppressWarnings("unchecked")
		// implicitamente safe, altrimenti sarebbe fallito il classloading
		T res = (T) pratica;
		return res;
	}

	@Override
	public <T extends Pratica<?>> T loadPratica(Class<T> clazz, Reader reader) {
		try {
			// detect della pratica impl
			@SuppressWarnings("unchecked") XMLPratica<DatiPratica> pratica = (XMLPratica<DatiPratica>) loadImplementation(clazz); // TODO
			return loadPratica(pratica, reader);

		} catch (Throwable t) {
			throw new PraticaException(t, "Errore in fase di caricamento del file XML");
		}
	}

	@Override
	public <T extends Pratica<?>, Z extends DatiPratica> T newPraticaInstance(Class<T> clazz, Z bean) {
		// detect della pratica impl
		try {
			@SuppressWarnings("unchecked") XMLPratica<DatiPratica> pratica = (XMLPratica<DatiPratica>) loadImplementation(clazz); // TODO
																																	// check
																																	// cast
			pratica.initPratica(bean);
			@SuppressWarnings("unchecked")
			// implicitamente safe, altrimenti sarebbe fallito il classloading
			T res = (T) pratica;
			return res;

		} catch (Throwable e) {
			throw new PraticaException(e, "Errore nella ricerca di una implementazione dell'interfaccia pratica passata" + ((clazz != null) ? ": " + clazz.getSimpleName() : ""));
		}
	}

	@Override
	public void serializePraticaInstance(Writer w, Pratica<?> pratica) {
		if (pratica instanceof XMLPratica<?>) {
			logger.debug("Init serializzazione");
			StringWriter intw = new StringWriter();
			try {
				XMLPratica<?> xmlPratica = (XMLPratica<?>) pratica;
				it.eng.consolepec.xmlplugin.jaxb.Pratica praticaJaxb = new it.eng.consolepec.xmlplugin.jaxb.Pratica();

				logger.debug("Serializzazione dati pratica");
				xmlPratica.serializeDati(praticaJaxb);
				logger.debug("Serializzazione tasks");
				praticaJaxb.setTasks(new Tasks());
				for (XMLTask<?> task : xmlPratica.getXMLTasks()) {
					logger.debug("Serializzazione task: {}", task.toString());
					task.serializeDati(praticaJaxb);
				}
				serialize(intw, praticaJaxb);
				logger.debug(intw.toString());

			} catch (Throwable e) {
				throw new PraticaException(e, "Errore in fase di serializzazione del file xml");
			}
			XMLVersionUpdater.ValidationResult valRes = null;
			try {
				valRes = validateAgainstXSD(new StringReader(intw.toString()));
			} catch (Throwable t) {
				throw new PraticaException("Errore in fase di controllo validazione dell'XML. " + t.getLocalizedMessage());
			}
			if (valRes != null && !valRes.isValid()) {
				// logger.warn("L'XML serializzato non ha superato la validazione. Messaggio: {}", valRes.getErrorMsg());
			}
			// validazione ok, popolo il writer in uscita
			try {
				w.write(intw.toString());
			} catch (Throwable t) {
				throw new PraticaException(t, "Errore in fase di scrittura del Writer passato");
			}

		} else {
			throw new PraticaException("La factory non supporta il tipo di pratica passato");
		}
	}

	protected XMLVersionUpdater.ValidationResult validateAgainstXSD(Reader reader) throws Exception {
		XMLVersionUpdaterFactory vuf = new XMLVersionUpdaterFactory();
		XMLVersionUpdater upd = vuf.newVersionUpdaterInstance();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(reader));

		XMLVersionUpdater.ValidationResult result = upd.validateDocument(doc);

		return result;
	}

	/* metodi interni */

	protected Reader checkAndUpdateVersion(Reader reader) throws Exception {
		XMLVersionUpdaterFactory vuf = new XMLVersionUpdaterFactory();
		XMLVersionUpdater upd = vuf.newVersionUpdaterInstance();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new InputSource(reader));

		doc = upd.updateVersion(doc);

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StringWriter out = new StringWriter();
		StreamResult result = new StreamResult(out);
		transformer.transform(source, result);

		return new StringReader(out.toString());
	}

	private static String readerToString(Reader reader) throws IOException {
		char[] arr = new char[8 * 1024]; // 8K at a time
		StringBuffer buf = new StringBuffer();
		int numChars;

		while ((numChars = reader.read(arr, 0, arr.length)) > 0) {
			buf.append(arr, 0, numChars);
		}

		return buf.toString();
	}

	protected abstract void serialize(StringWriter intw, it.eng.consolepec.xmlplugin.jaxb.Pratica praticaJaxb) throws Exception;

	protected abstract it.eng.consolepec.xmlplugin.jaxb.Pratica deserialize(String xmlPraticaString) throws Exception;

	/*
	 * Carica le implementazioni di una data pratica
	 */
	protected abstract <T extends Pratica<?>> Pratica<?> loadImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException;

	/*
	 * Carica tutte le implementazioni di Pratica
	 */
	protected abstract <T extends Pratica<?>> List<Pratica<?>> loadImplementations();

	protected abstract TaskFactory getTaskFactory();

	/*
	 * custom deserializer per gestire il polimorfismo definito dall'xsd del xmlplugin
	 */
	static abstract class CustomConverter<T> implements JsonDeserializer<T>, JsonSerializer<T> {

		Logger logger = LoggerFactory.getLogger(CustomConverter.class);

		private List<Class<? extends T>> classes = new ArrayList<Class<? extends T>>();
		private List<Predicate<JsonObject>> predicati = new ArrayList<Predicate<JsonObject>>();

		protected void registra(Class<? extends T> clazz, Predicate<JsonObject> predicato) {
			classes.add(clazz);
			predicati.add(predicato);
		}

		protected Predicate<JsonObject> hasCampo(final String campo) {
			return new Predicate<JsonObject>() {

				@Override
				public boolean apply(JsonObject json) {
					return json.has(campo);
				}
			};
		}

		protected Predicate<JsonObject> campoEquals(final String campo, final Object obj) {
			return new Predicate<JsonObject>() {

				@Override
				public boolean apply(JsonObject json) {
					return json.has(campo) && json.get(campo).equals(obj);
				}
			};
		}

		protected Predicate<JsonObject> deserDefault() {
			return new Predicate<JsonObject>() {

				@Override
				public boolean apply(JsonObject json) {
					return true;
				}
			};
		}

		@Override
		public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

			for (int i = 0; i < classes.size(); i++) {
				if (predicati.get(i).apply(json.getAsJsonObject())) {
					return context.deserialize(json, classes.get(i));
				}
			}

			throw new IllegalStateException("Non posso deserializzare il campo: " + typeOfT);
		}

		@Override
		public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {

			if (src == null) {
				return context.serialize(src);
			}

			for (int i = 0; i < classes.size(); i++) {

				if (classes.get(i).isInstance(src)) {
					return context.serialize(src, classes.get(i));
				}
			}

			throw new IllegalStateException("Non posso serializzare l'oggetto: " + src);
		}
	}
}
