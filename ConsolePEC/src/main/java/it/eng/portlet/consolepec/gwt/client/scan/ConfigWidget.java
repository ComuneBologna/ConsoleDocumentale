package it.eng.portlet.consolepec.gwt.client.scan;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.datepicker.client.DateBox;

import it.eng.portlet.consolepec.gwt.client.widget.CustomSuggestBox;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.SimpleInputListWidget;

public class ConfigWidget extends AbstractScanWidget {

	// private DatiDefaultProtocollazione datiPerProtocollazione;

	public ConfigWidget() {}

	@Override
	public void scanTextArea(VisitableWidget visitableWidget) {
		TextArea textarea = (TextArea) visitableWidget.getWidget();
		textarea.setEnabled(visitableWidget.isModificabile());
		if (!Strings.isNullOrEmpty(visitableWidget.getValore())) {
			textarea.setValue(visitableWidget.getValore());
			textarea.setText(visitableWidget.getValore());
		}

		if (visitableWidget.isObbligatorio()) {
			textarea.getElement().removeAttribute("required");
		}

		if (visitableWidget.getLunghezza() > 0) {
			textarea.getElement().setAttribute("maxlength", Integer.toString(visitableWidget.getLunghezza()));
		}
	}

	@Override
	public void scanHTMLPanel(VisitableWidget visitableWidget) {
		// TODO Auto-generated method stub
	}

	@Override
	public void scanDateBox(VisitableWidget visitableWidget) {
		DateBox dateBox = (DateBox) visitableWidget.getWidget();
		if (visitableWidget.isModificabile()) {
			dateBox.setEnabled(true);
			dateBox.setStyleName("testo");
		} else {
			dateBox.setEnabled(false);
			dateBox.setStyleName("testo disabilitato");
		}
	}

	@Override
	public void scanListBox(final VisitableWidget visitableWidget) {
		if (visitableWidget.getValoriListbox().isEmpty() == false) {
			ListBox listBox = (ListBox) visitableWidget.getWidget();
			for (String val : visitableWidget.getValoriListbox())
				listBox.addItem(val);
		}

		if (visitableWidget.getOnChangeCommand() != null) {
			final ListBox listBox = (ListBox) visitableWidget.getWidget();
			listBox.addChangeHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent paramChangeEvent) {
					visitableWidget.getOnChangeCommand().exe(listBox.getSelectedItemText());
				}
			});
		}

		if (visitableWidget.getSelectedItem() != null) {
			final ListBox listBox = (ListBox) visitableWidget.getWidget();
			for (int i = 0; i < listBox.getItemCount(); i++) {
				String m = listBox.getValue(i);
				if (visitableWidget.getSelectedItem().equals(m)) {
					listBox.setSelectedIndex(i);
				}
			}
		}
	}

	@Override
	public void scanSuggestBox(VisitableWidget visitableWidget) {
		// TODO Auto-generated method stub
	}

	@Override
	public void scanCustomSuggestBox(VisitableWidget visitableWidget) {
		CustomSuggestBox customSuggetBox = (CustomSuggestBox) visitableWidget.getWidget();
		if (visitableWidget.isModificabile()) {
			customSuggetBox.setEnabled(true);
			customSuggetBox.setStyleName("testo");
		} else {
			customSuggetBox.setEnabled(false);
			customSuggetBox.setStyleName("testo disabilitato");
		}
	}

	@Override
	public void scanHidden(VisitableWidget visitableWidget) {
		Hidden hidden = (Hidden) visitableWidget.getWidget();
		hidden.setValue(visitableWidget.getValore());
	}

	@Override
	public void scanTextBox(VisitableWidget visitableWidget) {
		TextBox textBox = (TextBox) visitableWidget.getWidget();
		textBox.setEnabled(visitableWidget.isModificabile());
		if (visitableWidget.isModificabile()) {
			textBox.setEnabled(true);
			textBox.setStyleName("testo");
		} else {
			textBox.setEnabled(false);
			textBox.setStyleName("testo disabilitato");
		}

		if (!Strings.isNullOrEmpty(visitableWidget.getValore())) {
			textBox.setValue(visitableWidget.getValore());
			textBox.setText(visitableWidget.getValore());
		}

		if (visitableWidget.isObbligatorio()) {
			textBox.getElement().removeAttribute("required");
		}

	}

	@Override
	public void scanIntegerBox(VisitableWidget visitableWidget) {
		IntegerBox integerBox = (IntegerBox) visitableWidget.getWidget();
		integerBox.setEnabled(visitableWidget.isModificabile());
		if (visitableWidget.isModificabile()) {
			integerBox.setEnabled(true);
			integerBox.setStyleName("testo");
		} else {
			integerBox.setEnabled(false);
			integerBox.setStyleName("testo disabilitato");
		}

		if (!Strings.isNullOrEmpty(visitableWidget.getValore())) {
			integerBox.setValue(Integer.valueOf(visitableWidget.getValore()));
			integerBox.setText(visitableWidget.getValore());
		}

		if (visitableWidget.isObbligatorio()) {
			integerBox.getElement().removeAttribute("required");
		}
	}

	@Override
	public void scanDoubleBox(VisitableWidget visitableWidget) {
		DoubleBox doubleBox = (DoubleBox) visitableWidget.getWidget();
		doubleBox.setEnabled(visitableWidget.isModificabile());
		if (visitableWidget.isModificabile()) {
			doubleBox.setEnabled(true);
			doubleBox.setStyleName("testo");
		} else {
			doubleBox.setEnabled(false);
			doubleBox.setStyleName("testo disabilitato");
		}

		if (!Strings.isNullOrEmpty(visitableWidget.getValore())) {
			doubleBox.setValue(Double.valueOf(visitableWidget.getValore()));
			doubleBox.setText(visitableWidget.getValore());
		}

		if (visitableWidget.isObbligatorio()) {
			doubleBox.getElement().removeAttribute("required");
		}
	}

	@Override
	public void scanYesNoRadioButton(VisitableWidget visitableWidget) {
		YesNoRadioButton yesNoRadioButton = (YesNoRadioButton) visitableWidget.getWidget();
		if (visitableWidget.isModificabile()) {
			yesNoRadioButton.setReadOnly(false);
		} else {
			yesNoRadioButton.setReadOnly(true);
		}

		if (!Strings.isNullOrEmpty(visitableWidget.getValore())) {
			yesNoRadioButton.setValue(Boolean.valueOf(visitableWidget.getValore()));
		}

	}

	@Override
	public void scanListWidget(VisitableWidget visitableWidget) {
		SimpleInputListWidget listWidget = (SimpleInputListWidget) visitableWidget.getWidget();
		listWidget.setAbilitato(visitableWidget.isModificabile());

	}

}
