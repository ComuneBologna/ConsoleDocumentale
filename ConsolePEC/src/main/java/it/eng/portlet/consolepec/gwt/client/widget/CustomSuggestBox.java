package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

public class CustomSuggestBox extends SuggestBox {

	private String identificativo;

	public CustomSuggestBox(SuggestOracle oracleRubricaSuggest) {
		super(oracleRubricaSuggest);
		this.getValueBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (getValue() == null || getValue().trim().isEmpty())
					setIdentificativo(null);
			}
		});
	}

	public String getIdentificativo() {
		return identificativo;
	}

	public void setIdentificativo(String identificativo) {
		this.identificativo = identificativo;
	}

	@Override
	public void setValue(String newValue) {

		super.setValue(newValue);
		if (newValue == null || newValue.isEmpty())
			this.setIdentificativo(newValue);
	}

}
