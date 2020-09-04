package it.eng.portlet.consolepec.gwt.client.widget.template;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

public class ElementoElencoTipiFascicoloWidget extends Composite {

	private static ElementoElencoTipiFascicoliWidgetUiBinder uiBinder = GWT.create(ElementoElencoTipiFascicoliWidgetUiBinder.class);

	interface ElementoElencoTipiFascicoliWidgetUiBinder extends UiBinder<Widget, ElementoElencoTipiFascicoloWidget> {
	}
	
	@UiField
	InlineLabel tipoFascicoloLabel;
	@UiField
	Button eliminaButton;
	
	private Command onEliminaCommand;

	public ElementoElencoTipiFascicoloWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void init(String etichetta) {
		tipoFascicoloLabel.setText(etichetta);
		eliminaButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				onEliminaCommand.execute();
			}
		});
	}
	
	public void setOnEliminaCommand(Command onEliminaCommand) {
		this.onEliminaCommand = onEliminaCommand;
	}

	public void setEnabled(boolean enabled) {
		eliminaButton.setEnabled(enabled);
	}
	
	

}
