package it.eng.portlet.consolepec.gwt.client.worklist;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.worklist.WorklistStrategy.EspandiRigaEventListener;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Disegna la riga espansa, secondo il tipo della pratica passata
 * 
 * @author pluttero
 * 
 */
public class RigaEspansaPerTipoStrategy extends AbstractRigaEspansaStrategy {
	Map<TipologiaPratica, AbstractRigaEspansaStrategy> strategiesMap = new HashMap<TipologiaPratica, AbstractRigaEspansaStrategy>();

	public void addStrategy(TipologiaPratica tipo, AbstractRigaEspansaStrategy strategy) {
		if (!strategiesMap.containsKey(tipo))
			strategiesMap.put(tipo, strategy);
		else
			throw new IllegalArgumentException("Il tipo pratica " + tipo + " è già contenuto");
	}

	@Override
	public void disegnaDettaglio(HTMLPanel destination, PraticaDTO pratica) {
		AbstractRigaEspansaStrategy strategy = strategiesMap.get(pratica.getTipologiaPratica());
		strategy.disegnaDettaglio(destination, pratica);
	}

	@Override
	public void disegnaOperazioni(HTMLPanel operations, PraticaDTO pratica, EspandiRigaEventListener listener) {
		AbstractRigaEspansaStrategy strategy = strategiesMap.get(pratica.getTipologiaPratica());
		strategy.disegnaOperazioni(operations, pratica, listener);

	}

}
