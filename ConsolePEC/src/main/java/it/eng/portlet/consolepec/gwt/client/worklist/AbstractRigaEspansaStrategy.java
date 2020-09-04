package it.eng.portlet.consolepec.gwt.client.worklist;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;

public abstract class AbstractRigaEspansaStrategy implements RigaEspansaStrategy {

	protected String sanitizeNull(String input) {
		if (input == null || input.trim().length() == 0)
			return "-";
		else
			return input;
	}

	protected void addButtonToOperations(Button btn, HTMLPanel operations) {
		SpanElement span = Document.get().createSpanElement();
		operations.getElement().appendChild(span);
		operations.add(btn, span);
		operations.add(new InlineHTML(" "));
	}

}
