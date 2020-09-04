package it.eng.portlet.consolepec.gwt.client.scan;

import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.widget.CustomSuggestBox;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo;
import it.eng.portlet.consolepec.gwt.shared.dto.Element;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class CompileRequestScan extends AbstractScanWidget {

	private static final String FORMATO_DATA_ARRIVO = "yyyy-MM-ddZZZ";
	private static final String FORMATO_TIME_ARRIVO = "HH:mm:ss.SSSZZZ";

	private boolean error;
	private String messageError;
	private boolean warning;
	private String messageWarning;

	private List<String> campiDiRicercaBa01 = Arrays.asList("i1_provenienza", "i1_destinatario", "i2_nominativo");
	private List<String> campiDaValidarePartitaIvaCF = Arrays.asList("i1_cf_provenienza", "i1_cf_destinatario", "i2_cf_piva_nominativo");

	private Map<String, Map<String, Element>> mapChiaveValore;

	public CompileRequestScan(Map<String, Campo> campi) {
		mapChiaveValore = new HashMap<String, Map<String, Element>>();
	}

	/* GETTER AND SETTER */

	public Map<String, Map<String, Element>> getMapChiaveValore() {
		return mapChiaveValore;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getMessageError() {
		return messageError;
	}

	public void setMessageError(String messageError) {
		this.messageError = messageError;
	}

	public boolean isWarning() {
		return warning;
	}

	public void setWarning(boolean warning) {
		this.warning = warning;
	}

	public String getMessageWarning() {
		return messageWarning;
	}

	public void setMessageWarning(String messageWarning) {
		this.messageWarning = messageWarning;
	}

	@Override
	public void scanTextBox(VisitableWidget visitableWidget) {

		String value = ((TextBox) visitableWidget.getWidget()).getValue();

		value = Strings.isNullOrEmpty(value) ? visitableWidget.getValore() : value;

		if (campiDiRicercaBa01.contains(visitableWidget.getNome())) {
			if (!Strings.isNullOrEmpty(value) && !value.contains("/"))
				value = value + "/";
		}

		checkCharset(visitableWidget,value);
		checkLength(visitableWidget);
		checkObbligatorieta(visitableWidget, value);
		validateValore(visitableWidget, value);
		aggiornaValoreForm(visitableWidget, value);

	}

	@Override
	public void scanTextArea(VisitableWidget visitableWidget) {
		String value = ((TextArea) visitableWidget.getWidget()).getValue();
		value = Strings.isNullOrEmpty(value) ? visitableWidget.getValore() : value;
		if (campiDiRicercaBa01.contains(visitableWidget.getNome())) {
			if (!Strings.isNullOrEmpty(value) && !value.contains("/"))
				value = value + "/";
		}
		checkCharset(visitableWidget, value);
		checkLength(visitableWidget);
		checkObbligatorieta(visitableWidget, value);
		validateValore(visitableWidget, value);
		aggiornaValoreForm(visitableWidget, value);

	}

	@Override
	public void scanHTMLPanel(VisitableWidget visitableWidget) {
		Widget internalWidget = ((HTMLPanel) visitableWidget.getWidget()).getWidget(0);
		if (internalWidget instanceof CustomSuggestBox)
			scanCustomSuggestBox(visitableWidget);
	}

	@Override
	public void scanListBox(VisitableWidget visitableWidget) {
		int index = ((ListBox) visitableWidget.getWidget()).getSelectedIndex();
		String value = null;
		if (index >= 0) {
			value = ((ListBox) visitableWidget.getWidget()).getValue(index);
			aggiornaValoreForm(visitableWidget, value);
		}
		else{
			value = Strings.isNullOrEmpty(value) ? visitableWidget.getValore() : value;
			aggiornaValoreForm(visitableWidget, value);
		}
	}

	private void checkObbligatorieta(VisitableWidget visitableWidget, String value) {
		if (visitableWidget.isObbligatorio() && Strings.isNullOrEmpty(value)) {
			setRequiredAttribute(visitableWidget.getWidget());
			messageError = "I campi in rosso sono obbligatori";
			error = true;
		} else {
			visitableWidget.getWidget().getElement().removeAttribute("required");
		}
	}

	private void setRequiredAttribute(Widget widget) {
		if(widget instanceof HTMLPanel){
			(((HTMLPanel)widget).getWidget(0)).getElement().setAttribute("required", "required");
		}else{
			widget.getElement().setAttribute("required", "required");
		}
		
	}

	@Override
	public void scanDateBox(VisitableWidget visitableWidget) {
		DateTimeFormat dateFormat;
		if (visitableWidget.getNome().equals("i1_data_arrivo"))
			dateFormat = DateTimeFormat.getFormat(FORMATO_DATA_ARRIVO);
		else
			dateFormat = DateTimeFormat.getFormat(FORMATO_TIME_ARRIVO);
		Date data = ((DateBox) visitableWidget.getWidget()).getValue();

		if (data != null) {
			String dataFormattata = dateFormat.format(data);
			checkObbligatorieta(visitableWidget, dataFormattata);
			aggiornaValoreForm(visitableWidget, dataFormattata);
		}
	}

	@Override
	public void scanSuggestBox(VisitableWidget visitableWidget) {

	}

	@Override
	public void scanHidden(VisitableWidget visitableWidget) {
	}

	@Override
	public void scanCustomSuggestBox(VisitableWidget visitableWidget) {
		String value = ((CustomSuggestBox) (((HTMLPanel) visitableWidget.getWidget()).getWidget(0))).getValue();
		checkObbligatorieta(visitableWidget, value);
		checkLength(visitableWidget);
		value = checkValoreSuggestBox(value);
		aggiornaValoreForm(visitableWidget, value);
	}

	private String checkValoreSuggestBox(String value) {
		return value.split("-")[0].trim();
	}

	private void checkCharset(VisitableWidget visitableWidget, String valueToCheck) {	
		if (!ValidationUtilities.isBA01CharsCompatibility(valueToCheck)) {
			messageWarning = new StringBuilder("Il campo \"").append(visitableWidget.getDescrizione()).append("\" contiene dei caratteri non consentiti.").toString();
			warning = true;
			visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
		} else {
			visitableWidget.getWidget().getElement().getStyle().clearBorderColor();
		}
	}

	private void checkLength(VisitableWidget visitableWidget) {
		if (visitableWidget.getWidget() instanceof TextBoxBase) {
			String value = ((TextBoxBase) visitableWidget.getWidget()).getValue();
			if (!Strings.isNullOrEmpty(value) && (visitableWidget.getLunghezza() != -1)) {
				if (value.length() > visitableWidget.getLunghezza()) {
					messageWarning = new StringBuilder("Il campo \"").append(visitableWidget.getDescrizione()).append("\" è maggiore di ").append(visitableWidget.getLunghezza()).append(" caratteri.").toString();
					warning = true;
					visitableWidget.getWidget().getElement().getStyle().setBorderColor("#FF0000");
				} else {
					visitableWidget.getWidget().getElement().getStyle().clearBorderColor();
				}
			}
		}
	}

	private String validateValore(VisitableWidget visitableWidget, String valore) {
		if (visitableWidget.getNome().equals("i1_provenienza") && valore != null && valore.trim().equals("/")) {
			valore = "";
		} else if (campiDaValidarePartitaIvaCF.contains(visitableWidget.getNome()) && !Strings.isNullOrEmpty(valore)) {
			if (!ValidationUtilities.isCodiceFiscaleValid(valore) && !ValidationUtilities.isPartitaIvaValid(valore)) {
				messageWarning = new StringBuilder().append("Il codice fiscale o partita iva \"").append(valore).append("\" non è valido.").toString();
				warning = true;
			}
		} else if (visitableWidget.getNome().equals("i1_numero_allegati") && !Strings.isNullOrEmpty(valore)) {
			if (!ValidationUtilities.isNumeric(valore)) {
				messageWarning = "Il campo numero allegati non ha un valore numerico";
				warning = true;
			}
		}
		return valore;
	}

	/**/
	private void aggiornaValoreForm(VisitableWidget visitableWidget, String value) {
		Map<String, Element> chiaveValore = initMapChiaveValore(visitableWidget);
		chiaveValore.put(visitableWidget.getNome(), new it.eng.portlet.consolepec.gwt.shared.dto.Row(visitableWidget.getNome(), value));
	}

	private Map<String, Element> initMapChiaveValore(VisitableWidget visitableWidget) {
		Map<String, Element> tipoRecord = mapChiaveValore.get(visitableWidget.getTipoRecord());
		if (tipoRecord == null)
			mapChiaveValore.put(visitableWidget.getTipoRecord(), new HashMap<String, Element>());
		return mapChiaveValore.get(visitableWidget.getTipoRecord());
	}

	@Override
	public void scanIntegerBox(VisitableWidget visitableWidget) {
		
	}

	@Override
	public void scanDoubleBox(VisitableWidget visitableWidget) {
		
	}

	@Override
	public void scanYesNoRadioButton(VisitableWidget visitableWidget) {
		
	}

	@Override
	public void scanListWidget(VisitableWidget visitableWidget) {
		
	}
}
