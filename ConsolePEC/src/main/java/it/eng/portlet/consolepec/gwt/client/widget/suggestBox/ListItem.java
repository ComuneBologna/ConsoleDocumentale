package it.eng.portlet.consolepec.gwt.client.widget.suggestBox;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasBlurHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

public class ListItem extends ComplexPanel implements HasText, HasHTML, HasClickHandlers, HasKeyDownHandlers, HasBlurHandlers {
	HandlerRegistration clickHandler;
	Element element;

	public ListItem() {
		element = Document.get().createElement("LI");
		setElement(element);
	}

	public void add(Widget w) {
		super.add(w, element);
	}

	public void insert(Widget w, int beforeIndex) {
		super.insert(w, element, beforeIndex, true);
	}

	public String getText() {
		return element.getInnerText();
	}

	public void setText(String text) {
		element.setInnerText((text == null) ? "" : text);
	}

	public void setId(String id) {
		element.setId(id);
	}

	public String getHTML() {
		return element.getInnerHTML();
	}

	public void setHTML(String html) {
		element.setInnerHTML((html == null) ? "" : html);
	}

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addDomHandler(handler, ClickEvent.getType());
	}

	public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
		return addDomHandler(handler, KeyDownEvent.getType());
	}

	public HandlerRegistration addBlurHandler(BlurHandler handler) {
		return addDomHandler(handler, BlurEvent.getType());
	}
}