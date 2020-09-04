package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.editor.client.Editor.Ignore;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class YesNoRadioButton extends Composite {

	private YesNoRadioButtonCommand yesNoRadioButtonCommand;

	interface YesNoRadioButtonUiBinder extends UiBinder<Widget, YesNoRadioButton> {}

	public final class RadioButtonClickHandler implements ClickHandler {
		private Boolean value;

		public RadioButtonClickHandler(Boolean value) {
			this.value = value;
		}

		@Override
		public void onClick(ClickEvent event) {
			backingValue = value;
			if (yesNoRadioButtonCommand != null) {
				yesNoRadioButtonCommand.execute(backingValue);
			}
		}
	}

	public interface YesNoRadioButtonCommand {
		public void execute(Boolean value);
	}

	@UiField
	@Ignore
	RadioButton yes;

	@UiField
	@Ignore
	RadioButton no;

	@UiField
	SpanElement yesNoLabel;

	@UiField
	LabelElement yesNoLabelGWT;

	Boolean backingValue;

	private static YesNoRadioButtonUiBinder uiBinder = GWT.create(YesNoRadioButtonUiBinder.class);

	public YesNoRadioButton() {
		initWidget(uiBinder.createAndBindUi(this));
		setupHandlers();
		String name = DOM.createUniqueId();
		yes.setName(name);
		no.setName(name);
	}

	public YesNoRadioButton(String label) {
		initWidget(uiBinder.createAndBindUi(this));
		setupHandlers();
		yesNoLabel.setInnerText(label);
		String name = DOM.createUniqueId();
		yes.setName(name);
		no.setName(name);
	}

	public YesNoRadioButton(String label, boolean isLabelGWT) {
		initWidget(uiBinder.createAndBindUi(this));
		setupHandlers();

		if (isLabelGWT) {
			yesNoLabelGWT.setInnerText(label);
			yesNoLabel.removeFromParent();
		} else
			yesNoLabel.setInnerText(label);

		String name = DOM.createUniqueId();
		yes.setName(name);
		no.setName(name);
	}

	public void selectYes() {
		yes.setValue(true);
		no.setValue(false);
	}

	public void selectNo() {
		yes.setValue(false);
		no.setValue(true);
	}

	private void setupHandlers() {
		yes.addClickHandler(new RadioButtonClickHandler(true));
		no.addClickHandler(new RadioButtonClickHandler(false));
	}

	public void setYesNoRadioButtonCommand(YesNoRadioButtonCommand yesNoRadioButtonCommand) {
		this.yesNoRadioButtonCommand = yesNoRadioButtonCommand;
	}

	public void setValue(Boolean value) {
		this.backingValue = value;
		if (value != null) {
			if (this.backingValue) {
				yes.setValue(true);
			} else {
				no.setValue(true);
			}
			if (this.yesNoRadioButtonCommand != null) {
				this.yesNoRadioButtonCommand.execute(value);
			}
		} else {
			yes.setValue(false);
			yes.setValue(false);
		}
	}

	public void setReadOnly(Boolean readOnly) {
		yes.setEnabled(!readOnly);
		no.setEnabled(!readOnly);
	}

	public Boolean getValue() {
		return yes.getValue() == true;
	}

	public String getStringValueSiNo() {
		if (yes.getValue() == true)
			return "Si";
		else
			return "No";
	}
}
