package it.eng.consolepec.xmlplugin.factory;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

/**
 * La factory si occupa di istanziare i vari componenti della pratica. L (de)serializzazione è delegata ai singoli componenti
 * 
 * @author pluttero
 * 
 */
public class XMLPraticaFactory extends AbstractPraticaFactory {

	protected <T extends Pratica<?>> Pratica<?> loadImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		T pratica = ServiceLoader.load(clazz, this.getClass().getClassLoader()).iterator().next();
		return pratica;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected <T extends Pratica<?>> List<Pratica<?>> loadImplementations() {
		Iterator<Pratica> iterator = ServiceLoader.load(Pratica.class, this.getClass().getClassLoader()).iterator();
		List<Pratica<? extends DatiPratica>> pratiche = new ArrayList<Pratica<? extends DatiPratica>>();
		while (iterator.hasNext())
			pratiche.add(iterator.next());
		return pratiche;
	}

	@Override
	protected TaskFactory getTaskFactory() {
		return new XMLTaskFactory();
	}
	
	public <T extends Pratica<?>> Pratica<?> getImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		return loadImplementation(clazz);
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
