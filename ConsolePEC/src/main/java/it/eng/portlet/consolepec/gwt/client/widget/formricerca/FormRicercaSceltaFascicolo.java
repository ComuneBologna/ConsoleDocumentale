package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;

public class FormRicercaSceltaFascicolo extends FormRicercaBase {

	private static FormRicercaWorklistFascicoloWidgetUiBinder uiBinder = GWT.create(FormRicercaWorklistFascicoloWidgetUiBinder.class);

	interface FormRicercaWorklistFascicoloWidgetUiBinder extends UiBinder<Widget, FormRicercaSceltaFascicolo> {}

	private ConfigurazioniHandler configurazioniHandler;

	@UiField(provided = true)
	SuggestBox tipoPraticaSuggestBox;
	@UiField
	TextBox utenteCreazioneTextBox;

	private List<TipologiaPratica> tipologiePratiche = new ArrayList<TipologiaPratica>();

	protected FormRicercaSceltaFascicolo() {
		super();
	}

	public FormRicercaSceltaFascicolo(ConfigurazioniHandler configurazioniHandler) {
		this();

		this.configurazioniHandler = configurazioniHandler;
		tipologiePratiche = PraticaUtil.fascicoliToTipologiePratiche(configurazioniHandler.filtraFascicoloPersonale(configurazioniHandler.getAnagraficheFascicoli(true)));
		final List<String> tipiLabels = new ArrayList<String>();

		for (TipologiaPratica tp : tipologiePratiche) {
			tipiLabels.add(tp.getEtichettaTipologia());
		}

		MultiWordSuggestOracle so = new SpacebarSuggestOracle(tipiLabels);
		tipoPraticaSuggestBox = new SuggestBox(so);
		tipoPraticaSuggestBox.setVisible(true);

		initWidget(uiBinder.createAndBindUi(this));
	}

	@Override
	public void configura(Command cercaCommand) {
		super.configura(cercaCommand);
		configuraHandlerCampo(utenteCreazioneTextBox);
		setLabelCampo(utenteCreazioneTextBox, "Utente creazione");
		configuraHandlerCampo(tipoPraticaSuggestBox);
		setLabelCampo(tipoPraticaSuggestBox, "Tipologia fascicolo");
	}

	@Override
	protected void serializza(CercaPratiche dto) {
		super.serializza(dto);
		dto.setUtenteCreazione(utenteCreazioneTextBox.getText());
		TipologiaPratica tp = null;

		if (tipoPraticaSuggestBox.getValue() != null && !tipoPraticaSuggestBox.getValue().trim().isEmpty()) {
			for (TipologiaPratica t : tipologiePratiche) {
				if (t.getEtichettaTipologia().equals(tipoPraticaSuggestBox.getValue())) {
					tp = t;
					break;
				}
			}
		}

		if (tp != null) {
			dto.setTipologiePratiche(Arrays.asList(tp));
		} else {
			dto.setTipologiePratiche(PraticaUtil.fascicoliToTipologiePratiche(configurazioniHandler.filtraFascicoloPersonale(configurazioniHandler.getAnagraficheFascicoli(true))));
		}
	}

	@Override
	public void resetForm() {
		super.resetForm();
		tipoPraticaSuggestBox.setValue(null);
	}

	@Override
	protected void resetAvanzate() {
		super.resetAvanzate();
		utenteCreazioneTextBox.setValue(null);
	}

	public void setProvenienza(String provenienza) {
		this.provenienzaTextBox.setText(provenienza);
	}

	public String getProvenienza() {
		return this.provenienzaTextBox.getText();
	}
}
