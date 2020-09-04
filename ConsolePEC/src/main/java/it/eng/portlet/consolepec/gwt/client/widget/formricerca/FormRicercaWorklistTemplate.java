package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.StatoTemplateDTO;

public class FormRicercaWorklistTemplate extends FormRicercaBase {

	private static FormRicercaWorklistTemplateWidgetUiBinder uiBinder = GWT.create(FormRicercaWorklistTemplateWidgetUiBinder.class);

	interface FormRicercaWorklistTemplateWidgetUiBinder extends UiBinder<Widget, FormRicercaWorklistTemplate> {}

	@UiField
	TextBox nomeTextBox;

	@UiField(provided = true)
	SuggestBox tipoTemplateSuggestBox = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));

	@UiField(provided = true)
	SuggestBox statoTemplateSuggestBox = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));

	private ConfigurazioniHandler configurazioniHandler;

	protected FormRicercaWorklistTemplate() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public FormRicercaWorklistTemplate(ConfigurazioniHandler configurazioniHandler) {
		this();
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	public void configura(Command cercaCommand) {
		super.configura(cercaCommand);

		configuraHandlerCampo(nomeTextBox);
		configuraHandlerCampo(tipoTemplateSuggestBox);
		configuraHandlerCampo(statoTemplateSuggestBox);

	}

	@Override
	protected void serializza(CercaPratiche dto) {
		super.serializza(dto);
		dto.setNomeTemplate(nomeTextBox.getText());

		if (PraticaUtil.toTipologiaPratica(configurazioniHandler.getAnagraficaModelloByEtichetta(tipoTemplateSuggestBox.getValue())) != null) {
			dto.setTipologiePratiche(Arrays.asList(PraticaUtil.toTipologiaPratica(configurazioniHandler.getAnagraficaModelloByEtichetta(tipoTemplateSuggestBox.getValue()))));
		}

		StatoTemplateDTO st = StatoTemplateDTO.fromLabel(statoTemplateSuggestBox.getValue());
		if (st != null)
			dto.setStato(new String[] { st.name() });
	}

	@Override
	public void addGruppoFiltriFissi(AbstractGruppoFiltriWidget widget) {
		super.addGruppoFiltriFissi(widget);
		gruppoFiltriFissiPanel.setVisible(false);
	}

	@Override
	protected void resetAvanzate() {
		super.resetAvanzate();
	}

	@Override
	public void resetForm() {
		super.resetForm();
		nomeTextBox.setText(null);
		tipoTemplateSuggestBox.setValue(null);
		statoTemplateSuggestBox.setValue(null);
	}

	@Override
	protected void applicaConfigurazionePerTipo(ConfigurazioneCampiPerTipo config) {
		super.applicaConfigurazionePerTipo(config);
		setLabelCampo(this.nomeTextBox, config.getTitoloNomeTemplate());
		setLabelCampo(tipoTemplateSuggestBox, "Tipologia modello");
		setLabelCampo(statoTemplateSuggestBox, "Stato modello");
	}

	@Override
	public void setElencoTipoPratiche(List<TipologiaPratica> tipiPratiche) {
		tipoTemplateSuggestBox.setVisible(false);
		SpacebarSuggestOracle so = (SpacebarSuggestOracle) tipoTemplateSuggestBox.getSuggestOracle();

		List<String> suggestions = Lists.transform(tipiPratiche, new Function<TipologiaPratica, String>() {

			@Override
			public String apply(TipologiaPratica input) {
				return configurazioniHandler.getAnagraficaModello(input.getNomeTipologia()).getEtichettaTipologia();
			}
		});

		so.setSuggestions(suggestions);
		tipoTemplateSuggestBox.setVisible(true);
	}

	public void setElencoStatiTemplate(List<String> suggestions) {
		statoTemplateSuggestBox.setVisible(false);
		SpacebarSuggestOracle so = (SpacebarSuggestOracle) statoTemplateSuggestBox.getSuggestOracle();
		so.setSuggestions(new ArrayList<String>(new TreeSet<String>(suggestions)));
		statoTemplateSuggestBox.setVisible(true);
		statoTemplateSuggestBox.setValue(StatoTemplateDTO.IN_GESTIONE.getDescrizioneStato());
	}
}
