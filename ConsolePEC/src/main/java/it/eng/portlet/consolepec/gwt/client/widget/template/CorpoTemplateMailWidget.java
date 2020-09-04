package it.eng.portlet.consolepec.gwt.client.widget.template;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.template.TemplateCreazioneApiClient;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.InputListWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.IndirizziEmailSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.TinyMCEUtils;
import it.eng.portlet.consolepec.gwt.shared.model.TemplateDTO;

/**
 *
 * @author biagiot
 *
 */
public class CorpoTemplateMailWidget extends AbstractCorpoTemplateWidget<TemplateDTO> {

	private static CorpoTemplateMailWidgetUiBinder uiBinder = GWT.create(CorpoTemplateMailWidgetUiBinder.class);

	interface CorpoTemplateMailWidgetUiBinder extends UiBinder<Widget, CorpoTemplateMailWidget> {/**/}

	@UiField ListBox mittenteListBox;
	@UiField HTMLPanel destinatarioPanel;
	@UiField TextBox oggettoTextBox;
	@UiField HTMLPanel inCopiaPanel;
	@UiField HTMLPanel informazioniTemplateMailPanel;

	private final String DOM_BODY_ID = "bodyTemplate";
	private final String INIT_BODY_HTML = "<p></p>";

	private String bodyText;

	InputListWidget inputListWidgetDestinatari;
	InputListWidget inputListWidgetInCopia;

	public CorpoTemplateMailWidget(ConfigurazioniHandler configurazioniHandler, TemplateCreazioneApiClient templateCreazioneApiClient) {
		super(configurazioniHandler, templateCreazioneApiClient);
		initWidget(uiBinder.createAndBindUi(this));

		dataCreazionePraticaDateBox.setEnabled(false);
		utenteTextBox.setEnabled(false);

		initDestinatariPanel();
	}

	@Override
	protected void onUnload() {
		super.onUnload();
		resetBody();
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		initBody(bodyText);
	}

	private void initDestinatariPanel() {
		SuggestOracle suggestOracle = new IndirizziEmailSuggestOracle(new ArrayList<String>());

		inputListWidgetDestinatari = new InputListWidget(suggestOracle, "destinatari", false);
		inputListWidgetInCopia = new InputListWidget(suggestOracle, "cc", false);

		inCopiaPanel.clear();
		destinatarioPanel.clear();
		RootPanel.get().add(inputListWidgetDestinatari);
		RootPanel.get().add(inputListWidgetInCopia);

		inCopiaPanel.add(inputListWidgetInCopia);
		destinatarioPanel.add(inputListWidgetDestinatari);
	}

	@Override
	public boolean validateForm(List<String> errors) {
		boolean valid = super.validateForm(errors);
		boolean campiInRossoObbligatori = false;

		if (!Strings.isNullOrEmpty(oggettoTextBox.getValue())) {
			oggettoTextBox.getElement().removeAttribute("required");

		} else {
			valid = false;
			oggettoTextBox.getElement().setAttribute("required", "required");
			campiInRossoObbligatori = true;
		}

		int index = mittenteListBox.getSelectedIndex();
		if (index >= 0 && !Strings.isNullOrEmpty(mittenteListBox.getValue(index))) {
			mittenteListBox.getElement().removeAttribute("required");

		} else {
			valid = false;
			mittenteListBox.getElement().setAttribute("required", "required");
			campiInRossoObbligatori = true;
		}

		List<String> listDestinatariInCopia = new ArrayList<String>();
		listDestinatariInCopia.addAll(inputListWidgetDestinatari.getItemSelected());
		listDestinatariInCopia.addAll(inputListWidgetInCopia.getItemSelected());

		Set<String> set = new HashSet<String>(listDestinatariInCopia);
		if (set.size() < listDestinatariInCopia.size()) {
			valid = false;
			errors.add("Ci sono duplicati tra gli indirizzi");
		}

		if (campiInRossoObbligatori) {

			if (errors.size() > 0) {
				if (!errors.get(0).equalsIgnoreCase(VALIDATE_FORM_ERROR)) errors.add(0, VALIDATE_FORM_ERROR);
			} else errors.add(0, VALIDATE_FORM_ERROR);
		}

		return valid;
	}

	@Override
	public TemplateDTO getTemplate() {
		TemplateDTO templateMail = new TemplateDTO();
		templateMail.setTipologiaPratica(TipologiaPratica.MODELLO_MAIL);
		popolaBaseTemplate(templateMail);
		templateMail.setOggettoMail(oggettoTextBox.getValue());
		String corpoMail = TinyMCEUtils.getContent(DOM_BODY_ID);
		if (!Strings.isNullOrEmpty(corpoMail)) templateMail.setCorpoMail(corpoMail);
		int index = mittenteListBox.getSelectedIndex();
		if (index >= 0) {
			templateMail.setMittente(mittenteListBox.getValue(index));
		}
		templateMail.getDestinatari().addAll(inputListWidgetDestinatari.getItemSelected());
		templateMail.getDestinatariCC().addAll(inputListWidgetInCopia.getItemSelected());

		return templateMail;
	}

	@Override
	public void setTemplate(TemplateDTO template) {
		super.setTemplate(template);

		if (!Strings.isNullOrEmpty(template.getOggettoMail())) oggettoTextBox.setValue(template.getOggettoMail());

		if (!Strings.isNullOrEmpty(template.getCorpoMail())) {
			this.bodyText = TinyMCEUtils.escapeTinyMCE(template.getCorpoMail());
			// XXX Il contenuto dell'editor del body viene popolato dopo il caricamento del widget TinyMCE (vedi onLoad())
			// OLD: setBody(TinyMCEUtils.escapeTinyMCE(template.getCorpoMail()));
		}

		if (!Strings.isNullOrEmpty(template.getMittente())) {
			for (int i = 0; i < mittenteListBox.getItemCount(); i++) {
				if (template.getMittente().equals(mittenteListBox.getValue(i))) {
					mittenteListBox.setSelectedIndex(i);
				}
			}
		}

		inputListWidgetDestinatari.reset();
		for (String des : template.getDestinatari()) {
			inputListWidgetDestinatari.addValueItem(des.toString());
		}

		inputListWidgetInCopia.reset();
		for (String cc : template.getDestinatariCC()) {
			inputListWidgetInCopia.addValueItem(cc.toString());
		}
	}

	@Override
	public void clear() {
		super.clear();

		oggettoTextBox.setValue("");
		inputListWidgetDestinatari.reset();
		inputListWidgetInCopia.reset();
		oggettoTextBox.getElement().removeAttribute("required");
		mittenteListBox.getElement().removeAttribute("required");
	}

	public void addMittente(String k, String v) {
		mittenteListBox.addItem(k, v);
	}

	public void clearMittenti() {
		mittenteListBox.clear();
	}

	public void setBody(String text) {
		TinyMCEUtils.setContent(text, DOM_BODY_ID);
	}

	public void initBody(String text) {
		String body = Strings.isNullOrEmpty(text) ? INIT_BODY_HTML : text;
		TinyMCEUtils.setupTinyMCEWithPlugins(body, DOM_BODY_ID, true);
	}

	public void resetBody() {
		TinyMCEUtils.removeTinyMCE();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		nomeTextBox.setEnabled(!readOnly);
		descrizioneTextBox.setEnabled(!readOnly);
		oggettoTextBox.setEnabled(!readOnly);
		mittenteListBox.setEnabled(!readOnly);
		inputListWidgetDestinatari.setAbilitato(readOnly);
		inputListWidgetInCopia.setAbilitato(readOnly);
		gruppiSuggestBox.setEnabled(readOnly);
		
		if (!readOnly) {
			gruppiSuggestBox.setEnabled(false);
			gruppiSuggestBox.setStyleName("testo disabilitato");

		} else {
			gruppiSuggestBox.removeStyleName("disabilitato");
		}

		elencoTipiFascicoloWidget.setEnabled(!readOnly);
		listaCampiTemplateWidget.setEnabled(!readOnly);
	}

	public void headingInformazioniModelloMailVisibile(boolean visibile) {
		informazioniTemplateMailPanel.setVisible(visibile);
	}
}
