package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliSuggestOracle;

public class FormRicercaWorklistBase extends FormRicercaBase {

	private List<String> assegnatari = new ArrayList<String>();

	public FormRicercaWorklistBase() {
		super();
	}

	@SuppressWarnings("unchecked")
	public void setParametriFissiWorklist(Map<String, Object> parametriFissiWorklist) {
		// questo deve sempre essere invocato prima del settaggio dei gruppi
		assegnatari.clear();

		if (parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.ASSEGNATARIO_SINGOLO.getFiltro()) != null) {
			for (String ass : (List<String>) parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.ASSEGNATARIO_SINGOLO.getFiltro())) {
				assegnatari.add(ass);
			}
		}
	}

	@Override
	public void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati) {
		List<AnagraficaRuolo> gruppiAssegnatariAbilitati = new ArrayList<AnagraficaRuolo>();

		if (!assegnatari.isEmpty()) {
			for (AnagraficaRuolo ruoloAbilitato : gruppiAbilitati) {
				if (assegnatari.contains(ruoloAbilitato.getRuolo())) {
					gruppiAssegnatariAbilitati.add(ruoloAbilitato);
				}
			}

		} else {
			gruppiAssegnatariAbilitati.addAll(gruppiAbilitati); // se non specifico i parametri fissi ho tutti i gruppi abilitati
		}

		AnagraficheRuoliSuggestOracle so = (AnagraficheRuoliSuggestOracle) this.assegnatarioSuggestBox.getSuggestOracle();
		so.setAnagraficheRuoli(gruppiAssegnatariAbilitati);

		if (gruppiAssegnatariAbilitati.size() == 1) {
			assegnatarioSuggestBox.setValue(gruppiAssegnatariAbilitati.iterator().next().getEtichetta());

			if (assegnatarioSuggestBox.isEnabled()) {
				assegnatarioSuggestBox.setEnabled(false);
			}
			if (!"testo disabilitato".equalsIgnoreCase(assegnatarioSuggestBox.getStyleName())) {
				assegnatarioSuggestBox.setStyleName("testo disabilitato");
			}

		} else {
			assegnatarioSuggestBox.removeStyleName("disabilitato");
			assegnatarioSuggestBox.setEnabled(true);
		}

	}
}
