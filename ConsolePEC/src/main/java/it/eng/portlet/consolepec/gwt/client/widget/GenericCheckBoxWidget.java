package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

/**
 * @author GiacomoFM
 * @since 11/dic/2018
 */
public class GenericCheckBoxWidget extends Composite {

	@UiField HTMLPanel panel;
	@UiField CheckBox checkBox;
	@UiField InlineLabel label;

	private GenericCheckBoxWidgetUiBinder uiBinder = GWT.create(GenericCheckBoxWidgetUiBinder.class);

	interface GenericCheckBoxWidgetUiBinder extends UiBinder<Widget, GenericCheckBoxWidget> {/**/}

	public GenericCheckBoxWidget(String label, ClickHandler clickHandler) {
		init(label, false, clickHandler);
	}

	public GenericCheckBoxWidget(String label, Boolean value, ClickHandler clickHandler) {
		init(label, value, clickHandler);
	}

	private void init(String label, Boolean value, ClickHandler clickHandler) {
		initWidget(this.uiBinder.createAndBindUi(this));
		this.label.setText(label);
		this.checkBox.setValue(value);
		this.checkBox.addClickHandler(clickHandler);
	}

}
