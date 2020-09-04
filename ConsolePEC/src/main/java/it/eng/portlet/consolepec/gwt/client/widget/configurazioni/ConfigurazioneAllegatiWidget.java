package it.eng.portlet.consolepec.gwt.client.widget.configurazioni;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.InputListWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.ConsoleMultiWordSuggestOracle;

public class ConfigurazioneAllegatiWidget extends Composite {

	interface ConfigurazioneAllegatiWidgetBinder extends UiBinder<Widget, ConfigurazioneAllegatiWidget> {/**/}

	private static ConfigurazioneAllegatiWidgetBinder uiBinder = GWT.create(ConfigurazioneAllegatiWidgetBinder.class);

	@UiField HTMLPanel tipologiePanel;

	private InputListWidget listTipologie;

	public ConfigurazioneAllegatiWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		listTipologie = new InputListWidget(new ConsoleMultiWordSuggestOracle(), "listaTipologie", false);
		tipologiePanel.add(listTipologie);
	}

	public void clear() {
		listTipologie.clear();
		listTipologie.reset();
	}

	public void setTipologieAllegati(List<String> tipologieAllegati) {
		clear();
		for (String tipologia : tipologieAllegati) {
			listTipologie.addValueItem(tipologia);
		}
	}

	public List<String> getTipologieAllegati() {
		if (listTipologie != null) {
			return listTipologie.getItemSelected();
		}
		return new ArrayList<>();
	}

}
