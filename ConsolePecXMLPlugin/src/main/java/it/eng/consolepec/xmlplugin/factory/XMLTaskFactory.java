package it.eng.consolepec.xmlplugin.factory;

import java.util.ServiceLoader;

public class XMLTaskFactory extends AbstractXMLTaskFactory {

	protected <T extends Task<?>> Task<?> loadImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		Task<?> task = ServiceLoader.load(clazz, this.getClass().getClassLoader()).iterator().next();
		return task;
	}

}
