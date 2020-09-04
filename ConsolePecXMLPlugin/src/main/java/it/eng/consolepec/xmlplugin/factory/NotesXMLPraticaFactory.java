package it.eng.consolepec.xmlplugin.factory;

import it.eng.consolepec.xmlplugin.exception.PraticaException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

public class NotesXMLPraticaFactory extends AbstractPraticaFactory {

	@Override
	protected <T extends Pratica<?>> Pratica<?> loadImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		try {
			String name = clazz.getName();
			List<Pratica<? extends DatiPratica>> pratiche = readClass(name);
			return pratiche.get(0);
		} catch (Throwable t) {
			throw new PraticaException("Errore in fase di load implementation pratica");
		}
	}

	@Override
	protected <T extends Pratica<?>> List<Pratica<?>> loadImplementations() {
		try {
			String name = Pratica.class.getName();
			List<Pratica<? extends DatiPratica>> pratiche = readClass(name);
			return pratiche;
		} catch (Throwable t) {
			throw new PraticaException("Errore in fase di load implementations pratica:" + t.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Pratica<?>> List<Pratica<?>> readClass(String name) throws Throwable {
		List<Pratica<? extends DatiPratica>> pratiche = new ArrayList<Pratica<? extends DatiPratica>>();
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
		if (is == null)
			throw new PraticaException("readclass: inputStream null");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String riga = "";
		while ((riga = br.readLine()) != null) {
			pratiche.add((T) Class.forName(riga).newInstance());
		}
		return pratiche;
	}

	@Override
	protected TaskFactory getTaskFactory() {
		return new NotesXMLTaskFactory();
	}

	@Override
	protected it.eng.consolepec.xmlplugin.jaxb.Pratica deserialize(String xmlPraticaString) throws JAXBException {
		Unmarshaller un = getJaxbContext().createUnmarshaller();
		it.eng.consolepec.xmlplugin.jaxb.Pratica praticaJaxb = (it.eng.consolepec.xmlplugin.jaxb.Pratica) un.unmarshal(new StringReader(xmlPraticaString));
		return praticaJaxb;
	}

	@Override
	protected void serialize(StringWriter intw, it.eng.consolepec.xmlplugin.jaxb.Pratica praticaJaxb) throws JAXBException, PropertyException {
		Marshaller mar = getJaxbContext().createMarshaller();
		mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		mar.marshal(praticaJaxb, intw);
	}
}
