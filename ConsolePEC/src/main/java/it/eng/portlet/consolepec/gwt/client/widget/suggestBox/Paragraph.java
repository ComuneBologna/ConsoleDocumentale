package it.eng.portlet.consolepec.gwt.client.widget.suggestBox;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Widget;

/**
 * This widget is to create <p> elements in a page.
 */
public class Paragraph extends Widget implements HasText {

    public Paragraph() {
        setElement(Document.get().createElement("p"));
    }

    public Paragraph(String text) {
        this();
        setText(text);
    }

    public String getText() {
        return getElement().getInnerText();
    }

    public void setText(String text) {
        getElement().setInnerText(text);
    }
}
