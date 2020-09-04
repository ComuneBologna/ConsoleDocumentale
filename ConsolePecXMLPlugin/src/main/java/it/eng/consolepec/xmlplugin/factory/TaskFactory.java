package it.eng.consolepec.xmlplugin.factory;


import java.io.Reader;
import java.util.Set;

public abstract class TaskFactory {

	public abstract Set<Task<?>> loadPraticaTasks(Reader reader, Pratica<? extends DatiPratica> pratica);
	
	public abstract <T extends Task<?>, Z extends DatiTask> T newTaskInstance(Class<T> clazz, Pratica<?> pratica, Z bean);
	
}
