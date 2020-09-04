package it.eng.portlet.consolepec.gwt.client.widget.suggestBox;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Widget;

public class BulletList extends ComplexPanel {
	UListElement element;

	public BulletList() {
		element = Document.get().createULElement();
		setElement(element);
	}

	public void add(Widget w) {
		super.add(w, element);
	}

	public void insert(Widget w, int beforeIndex) {
		super.insert(w, element, beforeIndex, true);
	}
}
