package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.UIObject;

/**
 *
 * @author biagiot
 *
 */
public class WidgetUtils extends UIObject {

	public static SpanElement creaLabel(HTMLPanel dettaglioContent, String value) {
		SpanElement span = Document.get().createSpanElement();
		span.addClassName("label");
		span.setInnerText(value);
		dettaglioContent.getElement().appendChild(span);
		return span;
	}

	public static HTMLPanel creaAbstract(HTMLPanel dettaglioContent, String value, boolean tooltip) {
		HTMLPanel panel = new HTMLPanel(value);
		panel.addStyleName("abstract_note");

		if (tooltip)
			panel.setTitle(value);

		dettaglioContent.add(panel);
		return panel;
	}

	public static void setEnabled(Element element, boolean enabled) {
		element.setPropertyBoolean("disabled", !enabled);
		setStyleName(element, "disabled", !enabled);
	}

	/**
	 * Returns true if the element has the disabled attribute.
	 */
	public static boolean isEnabled(Element element) {
		return element.getPropertyBoolean("disabled");
	}

}
