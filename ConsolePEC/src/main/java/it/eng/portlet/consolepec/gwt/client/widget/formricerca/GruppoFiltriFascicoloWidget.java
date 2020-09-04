package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo.StepIter;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;

public class GruppoFiltriFascicoloWidget extends AbstractGruppoFiltriWidget {

	private static FormRicercaFascicoloWidgetUiBinder uiBinder = GWT.create(FormRicercaFascicoloWidgetUiBinder.class);

	interface FormRicercaFascicoloWidgetUiBinder extends UiBinder<Widget, GruppoFiltriFascicoloWidget> {
		//
	}

	@UiField(provided = true)
	SuggestBox statoSuggestBox;
	@UiField
	TextBox utenteCreazioneTextBox;
	@UiField
	TextBox destinatarioAssegnaUtenteEsternoTextBox;
	@UiField(provided = true)
	SuggestBox stepIterSuggestBox;
	@UiField
	TextBox operatoreTextBox;

	public GruppoFiltriFascicoloWidget(ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
		initSuggestBoxStati();
		SpacebarSuggestOracle oracle = new SpacebarSuggestOracle(new ArrayList<String>());
		stepIterSuggestBox = new SuggestBox(oracle);
		initWidget(uiBinder.createAndBindUi(this));
	}

	/**
	 * Il metodo viene invocato dalla View su cui è utilizzata la form. Deve essere chiamato una volta sola
	 *
	 * @param cercaCommand - Command invocato quando si preme il pulsante Cerca, oppure invio su di una widget
	 */
	@Override
	public void configura(final com.google.gwt.user.client.Command cercaCommand) {
		super.configura(cercaCommand);
		configuraCampo(statoSuggestBox, "Stato");
		configuraCampo(utenteCreazioneTextBox, "Utente creazione");
		configuraCampo(destinatarioAssegnaUtenteEsternoTextBox, "Destinatario Inoltro");
		configuraCampo(stepIterSuggestBox, "Step Iter");
		configuraCampo(operatoreTextBox, "Operatore");
	}

	@Override
	public void serializza(CercaPratiche dto) {
		StatoDTO stato = StatoDTO.fromLabel(statoSuggestBox.getText().trim());
		if (stato != null)
			dto.setStato(new String[] { stato.name() });
		dto.setUtenteCreazione(utenteCreazioneTextBox.getText());
		dto.setDestinatarioAssegnaUtenteEsterno(destinatarioAssegnaUtenteEsternoTextBox.getText());
		dto.setStepIter(stepIterSuggestBox.getText().trim());
		dto.setOperatore(operatoreTextBox.getText());

	}

	@Override
	public void reset() {
		statoSuggestBox.setText(null);
		utenteCreazioneTextBox.setText(null);
		destinatarioAssegnaUtenteEsternoTextBox.setText(null);
		stepIterSuggestBox.setText(null);
		operatoreTextBox.setText(null);
	}

	@Override
	public List<TipologiaPratica> getTipiPraticheGestite() {
		return PraticaUtil.fascicoliToTipologiePratiche(configurazioniHandler.getAnagraficheFascicoli(true));
	}

	@Override
	public String getDescrizione() {
		return "Ricerca per dati fascicolo";
	}

	/* metodi interni */

	// public void setDefaultStatoRicerca(String stato) {
	// statoSuggestBox.setText(stato);
	// statoSuggestBox.setEnabled(false);
	// statoSuggestBox.setStyleName("testo disabilitato");
	// }

	private void initSuggestBoxStati() {
		List<String> suggestions = new ArrayList<String>();
		for (StatoDTO stato : StatoDTO.values()) {
			suggestions.add(stato.getLabel());
		}
		SpacebarSuggestOracle oracle = new SpacebarSuggestOracle(suggestions);
		statoSuggestBox = new SuggestBox(oracle);
		statoSuggestBox.setValue(StatoDTO.IN_GESTIONE.getLabel());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setParametriFissiWorklist(Map<String, Object> parametriFissiWorklist) {
		super.setParametriFissiWorklist(parametriFissiWorklist);

		// Stato
		// Stato
		List<String> statiWorklist = new ArrayList<String>();
		if (parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.STATO_PRATICA.getFiltro()) != null) {
			Object statoParFisso = parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.STATO_PRATICA.getFiltro());

			if (statoParFisso instanceof String) {
				statiWorklist.add((String) statoParFisso);

			} else if (statoParFisso instanceof List) {
				statiWorklist.addAll((List<String>) statoParFisso);
			}
		}

		StatoDTO stato = StatoDTO.IN_GESTIONE;

		if (!statiWorklist.isEmpty() && statiWorklist.size() == 1) {

			try {
				stato = StatoDTO.valueOf(statiWorklist.get(0));

			} catch (Exception e) {}

		}

		statoSuggestBox.setValue(stato.getLabel());

		// tipo pratica
		List<String> tipiFascicoloWorklist = new ArrayList<String>();

		if (parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.TIPO_PRATICA.getFiltro()) != null) {
			// destinatario
			Object tipiFascicoloWorklistDaParametroFisso = parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.TIPO_PRATICA.getFiltro());

			if (tipiFascicoloWorklistDaParametroFisso instanceof String) {
				tipiFascicoloWorklist.add((String) tipiFascicoloWorklistDaParametroFisso);

			} else if (tipiFascicoloWorklistDaParametroFisso instanceof List) {
				tipiFascicoloWorklist.addAll((List<String>) tipiFascicoloWorklistDaParametroFisso);
			}
		}

		if (!tipiFascicoloWorklist.isEmpty()) {
			if (tipiFascicoloWorklist.size() == 1 && !PraticaUtil.isConfigurazioneGenericaFascicolo(tipiFascicoloWorklist.get(0))) {
				AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicolo(tipiFascicoloWorklist.get(0));

				if (af != null && af.getStepIterAbilitati() != null && !af.getStepIterAbilitati().isEmpty()) {
					List<String> suggestions = getStepIterNames(af.getStepIterAbilitati());
					SpacebarSuggestOracle oracle = (SpacebarSuggestOracle) stepIterSuggestBox.getSuggestOracle();
					oracle.setSuggestions(suggestions);
					stepIterSuggestBox.setEnabled(true);

				} else {
					stepIterSuggestBox.setEnabled(false);
				}
			}
		}
	}

	private static List<String> getStepIterNames(List<StepIter> stepIterAbilitatiList) {
		List<String> stepIterName = new ArrayList<String>();
		for (StepIter stepIterDto : stepIterAbilitatiList) {
			stepIterName.add(stepIterDto.getNome());
		}
		return stepIterName;
	}

	protected void onTipoPraticaSelection(AnagraficaFascicolo tp) {
		List<StepIter> stepIterAbilitatiList = tp.getStepIterAbilitati();
		if (stepIterAbilitatiList != null && stepIterAbilitatiList.size() > 0) {
			List<String> suggestions = getStepIterNames(stepIterAbilitatiList);
			SpacebarSuggestOracle oracle = (SpacebarSuggestOracle) stepIterSuggestBox.getSuggestOracle();
			oracle.setSuggestions(suggestions);
			stepIterSuggestBox.setEnabled(true);
		} else {
			stepIterSuggestBox.setEnabled(false);
		}
	}

	// serve per forzare il valore del campo di ricerca relativo allo stato dei fascicoli
	public void refreshSuggestBoxStati() {
		statoSuggestBox.setText(StatoDTO.IN_GESTIONE.getLabel());
	}

}
