package it.eng.consolepec.xmlplugin.factory;


import java.io.Reader;
import java.io.Writer;

/**
 * Entry point per ottenere una istanza concreta di factory per pratica
 * @author pluttero
 *
 */
public abstract class PraticaFactory {
	
	public abstract <T extends Pratica<?>> T loadPratica(Class<T> clazz, Reader reader);
	
	public abstract Pratica<?> loadPratica(Reader reader);
	
	public abstract <T extends Pratica<?>, Z extends DatiPratica> T newPraticaInstance(Class<T> clazz, Z bean);

	public abstract void serializePraticaInstance(Writer writer, Pratica<?> pratica);
	
}
