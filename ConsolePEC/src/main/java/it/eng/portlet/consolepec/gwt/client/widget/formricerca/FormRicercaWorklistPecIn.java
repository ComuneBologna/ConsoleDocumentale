package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.TimePickerSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

public class FormRicercaWorklistPecIn extends FormRicercaWorklistBase {

	@UiField(provided = true)
	SuggestBox timeFrom = new SuggestBox(new TimePickerSuggestOracle());

	@UiField(provided = true)
	SuggestBox timeTo = new SuggestBox(new TimePickerSuggestOracle());

	final long SECONDS = 1000;
	final long MINUTES = 60 * SECONDS;
	final long HOURS = 60 * MINUTES;

	private static FormRicercaPecInWidgetUiBinder uiBinder = GWT.create(FormRicercaPecInWidgetUiBinder.class);

	interface FormRicercaPecInWidgetUiBinder extends UiBinder<Widget, FormRicercaWorklistPecIn> {}

	@UiField
	GruppoFiltriPecInWidget avanzatePecIn;

	public FormRicercaWorklistPecIn() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		setLabelCampo(timeFrom, "Ora da");
		setLabelCampo(timeTo, "Ora a");
	}

	@Override
	public void configura(Command cercaCommand) {
		super.configura(cercaCommand);
		applicaConfigurazionePerTipo(ConfigurazioneCampiFactory.getConfigurazione(TipologiaPratica.EMAIL_IN));
		avanzatePecIn.configura(cercaCommand);
	}

	@Override
	protected void serializza(CercaPratiche dto) {
		super.serializza(dto);
		avanzatePecIn.serializza(dto);

		if (timeFrom.getValue() != null && !timeFrom.getValue().trim().equals("")) {
			dto.setDataRicezioneFromMinute(TimePickerSuggestOracle.getMinute(timeFrom.getValue()));
			dto.setDataRicezioneFromHour(TimePickerSuggestOracle.getHour(timeFrom.getValue()));
		}
		if (timeTo.getValue() != null && !timeTo.getValue().trim().equals("")) {
			dto.setDataRicezioneToMinute(TimePickerSuggestOracle.getMinute(timeTo.getValue()));
			dto.setDataRicezioneToHour(TimePickerSuggestOracle.getHour(timeTo.getValue()));
		}

	}

	@Override
	protected void resetAvanzate() {
		super.resetAvanzate();
		avanzatePecIn.reset();
		timeFrom.setText(null);
		timeTo.setText(null);

	}

	public void resetParziale() {
		resetForm(); // chiama anche resetAvanzate()
		avanzatePecIn.refreshSuggestBoxStati();
	}

	@Override
	public void setParametriFissiWorklist(Map<String, Object> parametriFissiWorklist) {
		super.setParametriFissiWorklist(parametriFissiWorklist);
		avanzatePecIn.setParametriFissiWorklist(parametriFissiWorklist);
	}

}
