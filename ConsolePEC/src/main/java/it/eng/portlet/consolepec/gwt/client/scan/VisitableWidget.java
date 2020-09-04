package it.eng.portlet.consolepec.gwt.client.scan;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.widget.CustomSuggestBox;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.SimpleInputListWidget;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo;
import it.eng.portlet.consolepec.gwt.shared.dto.Campo.TipoWidget;

import java.util.List;

import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;

public class VisitableWidget {

	private String nome, valore, tipoRecord, descrizione;
	private boolean visibile, modificabile, obbligatorio;
	private int maxOccurs = 1, lunghezza;
	
	private List<String> valoriListbox;
	private Command<Void, String> onChangeCommand;
	private String selectedItem;
	
	private Widget widget;

	public VisitableWidget(Campo campo) {
		this.nome = campo.getNome();
		this.tipoRecord = campo.getTipoRecord();
		this.widget = creaWidget(campo.getTipoWidget());		
		this.lunghezza = campo.getLunghezza();
		this.descrizione = campo.getDescrizione();
		this.maxOccurs = campo.getMaxOccurs();
		this.valore = campo.getValore();
		this.visibile = campo.isVisibile();
		this.modificabile = campo.isModificabile();
		this.obbligatorio = campo.isObbligatorio();
		this.valoriListbox = campo.getValoriListbox();
		this.selectedItem = campo.getSelectedItem();
	}

	private Widget creaWidget(TipoWidget tipoWidget) {
		switch (tipoWidget) {
			case TEXTBOX:
				return new TextBox();
			case INTEGERBOX:
				return new IntegerBox();
			case YESNORADIOBUTTON:
				return new YesNoRadioButton();
			case DOUBLEBOX:
				return new DoubleBox();
			case TEXTAREA:
				return new TextArea();
			case DATE:
				return new DateBox();
			case LISTBOX:
				return new ListBox();
			case SUGGESTBOX:
				return new HTMLPanel("");
			case VALORIBOX:
				return new SimpleInputListWidget(null);
		}
		throw new IllegalArgumentException();
	}

	public void visit(ScanWidget scan) {

		if (widget instanceof Hidden) {
			scan.scanHidden(this);
		} else if (widget instanceof TextBox) {
			scan.scanTextBox(this);
		} else if (widget instanceof IntegerBox) {
			scan.scanIntegerBox(this);
		} else if (widget instanceof DoubleBox) {
			scan.scanDoubleBox(this);
		} else if (widget instanceof YesNoRadioButton) {
			scan.scanYesNoRadioButton(this);
		} else if (widget instanceof TextArea) {
			scan.scanTextArea(this);
		} else if (widget instanceof HTMLPanel) {
			scan.scanHTMLPanel(this);
		} else if (widget instanceof ListBox) {
			scan.scanListBox(this);
		} else if (widget instanceof DateBox) {
			scan.scanDateBox(this);
		} else if (widget instanceof CustomSuggestBox) {
			scan.scanCustomSuggestBox(this);
		} else if (widget instanceof SuggestBox) {
			scan.scanSuggestBox(this);
		} else if (widget instanceof SimpleInputListWidget) {
			scan.scanListWidget(this);
		}
	}

	public String getNome() {
		return nome;
	}

	public String getValore() {
		return valore;
	}

	public String getTipoRecord() {
		return tipoRecord;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public boolean isVisibile() {
		return visibile;
	}

	public boolean isModificabile() {
		return modificabile;
	}

	public boolean isObbligatorio() {
		return obbligatorio;
	}

	public int getMaxOccurs() {
		return maxOccurs;
	}

	public int getLunghezza() {
		return lunghezza;
	}

	public Widget getWidget() {
		return widget;
	}

	public void setWidget(Widget widget) {
		this.widget = widget;
	}

	public List<String> getValoriListbox() {
		return valoriListbox;
	}
	
	public Command<Void, String> getOnChangeCommand() {
		return onChangeCommand;
	}
	
	public void setOnChangeCommand(Command<Void, String> onChangeCommand) {
		this.onChangeCommand = onChangeCommand;
	}
	
	public String getSelectedItem() {
		return selectedItem;
	}

}
