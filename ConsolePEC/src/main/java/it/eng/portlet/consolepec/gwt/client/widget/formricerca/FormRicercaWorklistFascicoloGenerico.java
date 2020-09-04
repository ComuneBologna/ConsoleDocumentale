package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

public class FormRicercaWorklistFascicoloGenerico extends FormRicercaWorklistBase {

	private static FormRicercaWorklistFascicoloGenericoWidgetUiBinder uiBinder = GWT.create(FormRicercaWorklistFascicoloGenericoWidgetUiBinder.class);

	interface FormRicercaWorklistFascicoloGenericoWidgetUiBinder extends UiBinder<Widget, FormRicercaWorklistFascicoloGenerico> {
		//
	}

	@UiField(provided = true)
	SuggestBox tipoPraticaSuggestBox;
	@UiField(provided = true)
	GruppoFiltriFascicoloWidget avanzateFascicolo;

	private ConfigurazioniHandler configurazioniHandler;
	private boolean resetTipologiaPratica = true;

	public FormRicercaWorklistFascicoloGenerico(ProfilazioneUtenteHandler profilazioneUtenteHandler, final ConfigurazioniHandler configurazioniHandler) {
		this.configurazioniHandler = configurazioniHandler;
		avanzateFascicolo = new GruppoFiltriFascicoloWidget(profilazioneUtenteHandler, configurazioniHandler);
		final List<String> tipiLabels = new ArrayList<String>();
		MultiWordSuggestOracle so = new SpacebarSuggestOracle(tipiLabels);
		tipoPraticaSuggestBox = new SuggestBox(so);
		tipoPraticaSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String tipo = event.getSelectedItem().getDisplayString();
				AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicoloByEtichetta(tipo);
				setFiltriFissi(PraticaUtil.toTipologiaPratica(af));

			}

		});

		tipoPraticaSuggestBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (event.getValue() == null || event.getValue().trim().length() == 0 || !tipiLabels.contains(event.getValue())) {
					applicaConfigurazionePerTipo(CONFIG_CAMPI_BASE);
				}
			}
		});

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void configura(Command cercaCommand) {
		super.configura(cercaCommand);
		configuraHandlerCampo(tipoPraticaSuggestBox);
		setLabelCampo(tipoPraticaSuggestBox, "Tipologia fascicolo");

		avanzateFascicolo.configura(cercaCommand);
	}

	@Override
	protected void serializza(CercaPratiche dto) {
		super.serializza(dto);
		avanzateFascicolo.serializza(dto);
		AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicoloByEtichetta(tipoPraticaSuggestBox.getValue());
		if (af != null) {
			dto.setTipologiePratiche(PraticaUtil.fascicoliToTipologiePratiche(Arrays.asList(af)));
		}
	}

	public void setTipiPratica(TipologiaPratica tipologiaPratica) {
		gruppoFiltriFissiPanel.setVisible(!PraticaUtil.isFascicoloPersonale(tipologiaPratica));
	}

	@Override
	public void addGruppoFiltriFissi(AbstractGruppoFiltriWidget widget) {
		super.addGruppoFiltriFissi(widget);
		gruppoFiltriFissiPanel.setVisible(false);
	}

	@Override
	protected void resetAvanzate() {
		super.resetAvanzate();
		avanzateFascicolo.reset();
	}

	/**
	 * resetta i campi di base e avanzati della form ad eccezione del campo relativo allo stato del fascicolo
	 */
	public void resetParziale() {
		resetForm(); // chiama anche resetAvanzate()
		avanzateFascicolo.refreshSuggestBoxStati();
	}

	@Override
	public void resetForm() {
		super.resetForm();
		if (resetTipologiaPratica) {
			tipoPraticaSuggestBox.setValue(null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setParametriFissiWorklist(Map<String, Object> parametriFissiWorklist) {
		avanzateFascicolo.setParametriFissiWorklist(parametriFissiWorklist);
		super.setParametriFissiWorklist(parametriFissiWorklist);
		resetTipologiaPratica = true;

		List<AnagraficaFascicolo> tipiFascicoloWorklist = new ArrayList<AnagraficaFascicolo>();

		if (parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.TIPO_PRATICA.getFiltro()) != null) {
			Object tipiFascicoloWorklistDaParametroFisso = parametriFissiWorklist.get(ConsoleConstants.FiltriRicerca.TIPO_PRATICA.getFiltro());

			if (tipiFascicoloWorklistDaParametroFisso instanceof String) {
				String tipo = (String) tipiFascicoloWorklistDaParametroFisso;

				if (PraticaUtil.isConfigurazioneGenericaFascicolo(tipo)) {
					tipiFascicoloWorklist.addAll(configurazioniHandler.getAnagraficheFascicoli(true));

				} else {
					tipiFascicoloWorklist.add(configurazioniHandler.getAnagraficaFascicolo(tipo));
				}

			} else if (tipiFascicoloWorklistDaParametroFisso instanceof List) {

				List<String> params = (List<String>) tipiFascicoloWorklistDaParametroFisso;
				boolean all = false;

				for (String s : params) {
					if (PraticaUtil.isConfigurazioneGenericaFascicolo(s)) {
						all = true;
						break;
					}
				}

				if (all) {
					tipiFascicoloWorklist.addAll(configurazioniHandler.getAnagraficheFascicoli(true));

				} else {
					tipiFascicoloWorklist.addAll(Lists.newArrayList(Lists.transform((List<String>) tipiFascicoloWorklistDaParametroFisso, new Function<String, AnagraficaFascicolo>() {

						@Override
						public AnagraficaFascicolo apply(String input) {
							return configurazioniHandler.getAnagraficaFascicolo(input);
						}
					})));
				}
			}
		}

		tipiFascicoloWorklist.removeAll(Collections.singleton(null));

		// tipo pratica
		if (!tipiFascicoloWorklist.isEmpty()) {
			List<String> etichette = labels(tipiFascicoloWorklist); // estraggo le labels

			SpacebarSuggestOracle so = (SpacebarSuggestOracle) tipoPraticaSuggestBox.getSuggestOracle();
			so.setSuggestions(etichette);
			tipoPraticaSuggestBox.setVisible(true);

			if (tipiFascicoloWorklist.size() == 1) {
				tipoPraticaSuggestBox.setValue(tipiFascicoloWorklist.get(0).getEtichettaTipologia());
				tipoPraticaSuggestBox.setEnabled(false);
				setFiltriFissi(PraticaUtil.toTipologiaPratica(tipiFascicoloWorklist.get(0)));
				setTipiPratica(tipiFascicoloWorklist.get(0));

				if (tipoPraticaSuggestBox.isEnabled()) {
					tipoPraticaSuggestBox.setEnabled(false);
				}

				if (!"testo disabilitato".equalsIgnoreCase(tipoPraticaSuggestBox.getStyleName())) {
					tipoPraticaSuggestBox.setStyleName("testo disabilitato");
				}

				resetTipologiaPratica = false; // solo un fascicolo: campo bloccato

			} else {
				tipoPraticaSuggestBox.removeStyleName("disabilitato");
				tipoPraticaSuggestBox.setEnabled(true);
			}

		} else {
			SpacebarSuggestOracle so = (SpacebarSuggestOracle) tipoPraticaSuggestBox.getSuggestOracle();

			List<String> suggestions = Lists.transform(tipoPraticheAbilitate, new Function<TipologiaPratica, String>() {

				@Override
				public String apply(TipologiaPratica input) {
					return configurazioniHandler.getAnagraficaFascicolo(input.getNomeTipologia()).getEtichettaTipologia();
				}
			});

			so.setSuggestions(suggestions);
			tipoPraticaSuggestBox.setVisible(true);
			tipoPraticaSuggestBox.removeStyleName("disabilitato");
			tipoPraticaSuggestBox.setEnabled(true);
		}
	}

	private static List<String> labels(List<AnagraficaFascicolo> anagraficheFascicoli) {
		List<String> labels = new ArrayList<String>();
		for (AnagraficaFascicolo af : anagraficheFascicoli) {
			labels.add(af.getEtichettaTipologia());
		}
		return labels;
	}

	private void setFiltriFissi(TipologiaPratica tipo) {
		for (AbstractGruppoFiltriWidget w : listFiltri) {
			w.onTipoPraticaSelection(tipo);
		}
	}
}
