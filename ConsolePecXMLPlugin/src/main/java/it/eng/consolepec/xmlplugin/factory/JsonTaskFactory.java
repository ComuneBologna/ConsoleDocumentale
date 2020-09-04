package it.eng.consolepec.xmlplugin.factory;

import java.io.Reader;
import java.util.ServiceLoader;

import javax.xml.bind.JAXBException;
import javax.xml.datatype.XMLGregorianCalendar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import it.eng.consolepec.xmlplugin.factory.AbstractPraticaFactory.CustomConverter;
import it.eng.consolepec.xmlplugin.factory.JsonPraticaFactory.XGCalConverter;
import it.eng.consolepec.xmlplugin.jaxb.DestinatarioGruppoRichiestaFirmaJaxb;
import it.eng.consolepec.xmlplugin.jaxb.DestinatarioRichiestaFirmaJaxb;
import it.eng.consolepec.xmlplugin.jaxb.DestinatarioUtenteRichiestaFirmaJaxb;
import it.eng.consolepec.xmlplugin.jaxb.Pratica;

public class JsonTaskFactory extends AbstractXMLTaskFactory {

	Gson gson = new GsonBuilder() //
			.registerTypeAdapter(XMLGregorianCalendar.class, new XGCalConverter.Serializer()) //
			.registerTypeAdapter(XMLGregorianCalendar.class, new XGCalConverter.Deserializer()) //
			.registerTypeAdapter(DestinatarioRichiestaFirmaJaxb.class, new DestinatarioRichiestaFirmaConverter()) //
			.create();

	@Override
	protected <T extends Task<?>> Task<?> loadImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Task<?> task = ServiceLoader.load(clazz, this.getClass().getClassLoader()).iterator().next();
		return task;
	}

	@Override
	protected Pratica deserialize(Reader reader) throws JAXBException {
		return gson.fromJson(reader, Pratica.class);
	}

	static class DestinatarioRichiestaFirmaConverter extends CustomConverter<DestinatarioRichiestaFirmaJaxb> {

		public DestinatarioRichiestaFirmaConverter() {
			super();
			registra(DestinatarioUtenteRichiestaFirmaJaxb.class, hasCampo("nomeUtente"));
			registra(DestinatarioGruppoRichiestaFirmaJaxb.class, hasCampo("nomeGruppo"));
		}
	}
}
