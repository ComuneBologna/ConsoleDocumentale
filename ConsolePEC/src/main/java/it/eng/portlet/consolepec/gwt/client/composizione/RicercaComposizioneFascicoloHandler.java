package it.eng.portlet.consolepec.gwt.client.composizione;

import java.util.Set;

import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public interface RicercaComposizioneFascicoloHandler {

	void index(FascicoloDTO fascicolo);

	Set<String> autocomplete(String prefix);

	Set<Object> search(String key);

	void clear();

}
