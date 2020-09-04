package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO.StatoDTO;

public class FormRicercaWorklistComunicazione extends FormRicercaBase {

	private static FormRicercaWorklistTemplateWidgetUiBinder uiBinder = GWT.create(FormRicercaWorklistTemplateWidgetUiBinder.class);

	interface FormRicercaWorklistTemplateWidgetUiBinder extends UiBinder<Widget, FormRicercaWorklistComunicazione> {}

	@UiField
	TextBox codiceTextBox;

	@UiField
	TextBox idTemplateTextBox;

	@UiField(provided = true)
	SuggestBox statoSuggestBox = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));

	public FormRicercaWorklistComunicazione() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void configura(Command cercaCommand) {
		super.configura(cercaCommand);
		configuraHandlerCampo(codiceTextBox);
		configuraHandlerCampo(idTemplateTextBox);
		configuraHandlerCampo(statoSuggestBox);
	}

	@Override
	protected void serializza(CercaPratiche dto) {
		super.serializza(dto);
		dto.setCodiceComunicazione(codiceTextBox.getText());
		dto.setIdTemplateComunicazione(idTemplateTextBox.getText());

		StatoDTO st = StatoDTO.fromLabel(statoSuggestBox.getValue());
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
		codiceTextBox.setText(null);
		idTemplateTextBox.setText(null);
		statoSuggestBox.setValue(null);
	}

	@Override
	protected void applicaConfigurazionePerTipo(ConfigurazioneCampiPerTipo config) {

		super.applicaConfigurazionePerTipo(config);
		setLabelCampo(this.codiceTextBox, "Codice");
		setLabelCampo(this.idTemplateTextBox, "Id template");
		setLabelCampo(this.statoSuggestBox, "Stato comunicazione");
	}

	public void setElencoStatiComunicazione(List<String> suggestions) {
		statoSuggestBox.setVisible(false);
		SpacebarSuggestOracle so = (SpacebarSuggestOracle) statoSuggestBox.getSuggestOracle();
		so.setSuggestions(new ArrayList<String>(new TreeSet<String>(suggestions)));
		statoSuggestBox.setVisible(true);
		statoSuggestBox.setValue(StatoDTO.IN_GESTIONE.getLabel());
	}
}
