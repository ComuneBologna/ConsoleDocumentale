package it.eng.consolepec.xmlplugin.factory;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import it.eng.consolepec.xmlplugin.factory.JsonTaskFactory.DestinatarioRichiestaFirmaConverter;
import it.eng.consolepec.xmlplugin.jaxb.DestinatarioRichiestaFirmaJaxb;
import it.eng.consolepec.xmlplugin.versioning.JsonVersionUpdater;
import it.eng.consolepec.xmlplugin.versioning.JsonVersionUpdaterFactory;
import it.eng.consolepec.xmlplugin.versioning.XMLVersionUpdater.ValidationResult;

public class JsonPraticaFactory extends AbstractPraticaFactory {

	Gson gson = new GsonBuilder() //
			.registerTypeAdapter(XMLGregorianCalendar.class, new XGCalConverter.Serializer()) //
			.registerTypeAdapter(XMLGregorianCalendar.class, new XGCalConverter.Deserializer()) //
			.registerTypeAdapter(DestinatarioRichiestaFirmaJaxb.class, new DestinatarioRichiestaFirmaConverter()) //
			.create();

	@Override
	protected <T extends Pratica<?>> Pratica<?> loadImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		T pratica = ServiceLoader.load(clazz, this.getClass().getClassLoader()).iterator().next();
		return pratica;
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected List<Pratica<?>> loadImplementations() {
		Iterator<Pratica> iterator = ServiceLoader.load(Pratica.class, this.getClass().getClassLoader()).iterator();
		List<Pratica<? extends DatiPratica>> pratiche = new ArrayList<Pratica<? extends DatiPratica>>();
		while (iterator.hasNext()) {
			pratiche.add(iterator.next());
		}
		return pratiche;
	}

	@Override
	protected TaskFactory getTaskFactory() {
		return new JsonTaskFactory();
	}

	public <T extends Pratica<?>> Pratica<?> getImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return loadImplementation(clazz);
	}

	@Override
	protected void serialize(StringWriter intw, it.eng.consolepec.xmlplugin.jaxb.Pratica praticaJaxb) throws JAXBException, PropertyException {
		gson.toJson(praticaJaxb, intw);
	}

	@Override
	protected it.eng.consolepec.xmlplugin.jaxb.Pratica deserialize(String xmlPraticaString) throws JAXBException {
		return gson.fromJson(xmlPraticaString, it.eng.consolepec.xmlplugin.jaxb.Pratica.class);
	}

	@Override
	protected Reader checkAndUpdateVersion(Reader reader) throws Exception {
		JsonVersionUpdaterFactory jsonVersionUpdaterFactory = new JsonVersionUpdaterFactory();
		JsonVersionUpdater jsonVersionUpdater = jsonVersionUpdaterFactory.newVersionUpdaterInstance();
		JsonElement jElement = new JsonParser().parse(reader);
		JsonElement jElementUpdated = jsonVersionUpdater.updateVersion(jElement);
		return new StringReader(jElementUpdated.toString());
	}

	@Override
	protected ValidationResult validateAgainstXSD(Reader reader) throws Exception {
		return null;
	}

	static class XGCalConverter {
		static class Serializer implements JsonSerializer<XMLGregorianCalendar> {
			public Serializer() {
				super();
			}

			@Override
			public JsonElement serialize(XMLGregorianCalendar t, Type type, JsonSerializationContext jsonSerializationContext) {
				return new JsonPrimitive(t.toXMLFormat());
			}
		}

		static class Deserializer implements JsonDeserializer<XMLGregorianCalendar> {

			Logger logger = LoggerFactory.getLogger(Deserializer.class);

			@Override
			public XMLGregorianCalendar deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
				try {
					return DatatypeFactory.newInstance().newXMLGregorianCalendar(jsonElement.getAsString());
				} catch (DatatypeConfigurationException ex) {
					logger.error("DatatypeConfigurationException", ex);
					return null;
				}
			}
		}
	}

}
