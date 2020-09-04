package it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi;

import com.google.gwt.dom.client.Document;
import com.google.gwt.text.client.LongParser;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.user.client.ui.ValueBox;

public class DatiAggiuntiviNumberBox extends ValueBox<Long> {
	
	public DatiAggiuntiviNumberBox() {
		super(Document.get().createTextInputElement(), new AbstractRenderer<Long>() {
			@Override
			public String render(Long l) {
				return l == null ? "" : l.toString();
			}
		}, LongParser.instance());
	}
}