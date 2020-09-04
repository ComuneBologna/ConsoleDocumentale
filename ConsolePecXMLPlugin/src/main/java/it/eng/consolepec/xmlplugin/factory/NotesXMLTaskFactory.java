package it.eng.consolepec.xmlplugin.factory;

import it.eng.consolepec.xmlplugin.exception.PraticaException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class NotesXMLTaskFactory extends AbstractXMLTaskFactory {

	@SuppressWarnings("unchecked")
	@Override
	protected <T extends Task<?>> Task<?> loadImplementation(Class<T> clazz) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		try {
			InputStream is = this.getClass().getResourceAsStream(clazz.getName());
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String riga = "";
			StringBuilder sb = new StringBuilder();
			while ((riga = br.readLine()) != null) {
				sb.append(riga);
			}
			Task<?> task = (T)Class.forName(sb.toString()).newInstance();
			return task;
		} catch (Throwable t) {
			throw new PraticaException("Errore in fase di load implementation pratica");
		}
	}

}
