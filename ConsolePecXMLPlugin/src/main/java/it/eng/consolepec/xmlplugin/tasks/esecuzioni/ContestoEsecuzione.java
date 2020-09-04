package it.eng.consolepec.xmlplugin.tasks.esecuzioni;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.ConseguenzaEsecuzione;
import it.eng.consolepec.xmlplugin.factory.Task;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Supplier;

/**
 *
 * @author biagiot
 *
 */
public class ContestoEsecuzione<T extends Task<?>> {

	private Map<Class<? extends ConseguenzaEsecuzione>, Supplier<? extends Esecuzione<T, ? extends ConseguenzaEsecuzione>>> contesto = new HashMap<>();

	public ContestoEsecuzione() {
		super();
	}

	public <CE extends ConseguenzaEsecuzione, E extends Esecuzione<T, CE>> void add(Class<CE> clazz, Supplier<E> factory){
		contesto.put(clazz, factory);
	}

	@SuppressWarnings("unchecked")
	public Esecuzione<T, ConseguenzaEsecuzione> calcola(Class<? extends ConseguenzaEsecuzione> clazz) {
		if (contesto.get(clazz) != null)
			return (Esecuzione<T, ConseguenzaEsecuzione>) contesto.get(clazz).get();

		return null;
	}
}
