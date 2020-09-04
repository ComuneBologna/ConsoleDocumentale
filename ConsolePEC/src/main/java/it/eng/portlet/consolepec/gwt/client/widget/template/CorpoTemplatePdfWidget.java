package it.eng.portlet.consolepec.gwt.client.widget.template;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateCreazioneApiClient;
import it.eng.portlet.consolepec.gwt.shared.model.TemplatePdfDTO;

/**
 *
 * @author biagiot
 *
 */
public class CorpoTemplatePdfWidget extends AbstractCorpoTemplateWidget<TemplatePdfDTO> {

	private static CorpoTemplatePdfWidgetBinder uiBinder = GWT.create(CorpoTemplatePdfWidgetBinder.class);

	@UiField
	TextBox titoloFileTextBox;

	interface CorpoTemplatePdfWidgetBinder extends UiBinder<Widget, CorpoTemplatePdfWidget> {/**/}

	public CorpoTemplatePdfWidget(ConfigurazioniHandler configurazioniHandler, TemplateCreazioneApiClient templateCreazioneApiClient) {
		super(configurazioniHandler, templateCreazioneApiClient);
		initWidget(uiBinder.createAndBindUi(this));

		dataCreazionePraticaDateBox.setEnabled(false);
		utenteTextBox.setEnabled(false);
	}

	@Override
	public TemplatePdfDTO getTemplate() {
		TemplatePdfDTO templatePDF = new TemplatePdfDTO();
		templatePDF.setTipologiaPratica(TipologiaPratica.MODELLO_PDF);
		popolaBaseTemplate(templatePDF);
		if (titoloFileTextBox.getValue() != null && !titoloFileTextBox.getValue().trim().isEmpty()) {
			templatePDF.setTitoloFile(titoloFileTextBox.getValue());
		}
		return templatePDF;
	}

	@Override
	public void setTemplate(TemplatePdfDTO template) {
		super.setTemplate(template);
		if (template.getTitoloFile() != null && !template.getTitoloFile().trim().isEmpty()) {
			titoloFileTextBox.setValue(template.getTitoloFile());
		}
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		nomeTextBox.setEnabled(!readOnly);
		descrizioneTextBox.setEnabled(!readOnly);
		gruppiSuggestBox.setEnabled(readOnly);
		titoloFileTextBox.setEnabled(!readOnly);

		if (!readOnly) {
			gruppiSuggestBox.setEnabled(false);
			gruppiSuggestBox.setStyleName("testo disabilitato");

		} else {
			gruppiSuggestBox.removeStyleName("disabilitato");
		}

		elencoTipiFascicoloWidget.setEnabled(!readOnly);
		listaCampiTemplateWidget.setEnabled(!readOnly);
	}
}
