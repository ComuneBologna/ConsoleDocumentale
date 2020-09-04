package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO.StatoTemplateDTO;

public class FormRicercaSceltaTemplate extends FormRicercaBase {

	private static FormRicercaWorklistTemplateWidgetUiBinder uiBinder = GWT.create(FormRicercaWorklistTemplateWidgetUiBinder.class);

	interface FormRicercaWorklistTemplateWidgetUiBinder extends UiBinder<Widget, FormRicercaSceltaTemplate> {}

	@UiField
	TextBox utenteCreazioneTextBox;
	@UiField
	TextBox nomeTextBox;

	private ConfigurazioniHandler configurazioniHandler;

	protected FormRicercaSceltaTemplate() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	@Inject
	public FormRicercaSceltaTemplate(ConfigurazioniHandler configurazioniHandler) {
		this();
		this.configurazioniHandler = configurazioniHandler;
	}

	@Override
	public void configura(Command cercaCommand) {
		super.configura(cercaCommand);
		configuraHandlerCampo(utenteCreazioneTextBox);
		setLabelCampo(utenteCreazioneTextBox, "Utente creazione");
		configuraHandlerCampo(nomeTextBox);

	}

	@Override
	protected void serializza(CercaPratiche dto) {
		super.serializza(dto);
		dto.setUtenteCreazione(utenteCreazioneTextBox.getText());
		if (dto.getTipologiePratiche() == null || dto.getTipologiePratiche().isEmpty())
			dto.setTipologiePratiche(PraticaUtil.modelliToTipologiePratiche(configurazioniHandler.getAnagraficheModelli(true)));
		dto.setNomeTemplate(nomeTextBox.getText());
		dto.setStato(new String[] { StatoTemplateDTO.IN_GESTIONE.name() });
	}

	@Override
	public void resetForm() {
		super.resetForm();

		nomeTextBox.setText(null);

	}

	@Override
	protected void resetAvanzate() {
		super.resetAvanzate();
		utenteCreazioneTextBox.setValue(null);
	}

	@Override
	protected void applicaConfigurazionePerTipo(ConfigurazioneCampiPerTipo config) {

		super.applicaConfigurazionePerTipo(config);
		setLabelCampo(this.nomeTextBox, config.getTitoloNomeTemplate());
	}

}
