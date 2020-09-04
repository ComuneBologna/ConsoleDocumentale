package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.shared.model.EventoIterDTO;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class EventoIterFascicoloWidget extends Composite {

	private static EventoIterFascicoloWidgetUiBinder uiBinder = GWT.create(EventoIterFascicoloWidgetUiBinder.class);

	interface EventoIterFascicoloWidgetUiBinder extends UiBinder<Widget, EventoIterFascicoloWidget> {
	}

	@UiField
	HTMLPanel iterPanel;

	public EventoIterFascicoloWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initWidget(List<EventoIterDTO> eventiIterDTO) {

		iterPanel.clear();
		Element elem = iterPanel.getElement();
		while (elem.hasChildNodes()) {
			elem.removeChild(elem.getFirstChild());
		}

		for (EventoIterDTO eventoIterDTO : eventiIterDTO) {

			SpanElement dateSpan = SpanElement.as(DOM.createSpan());
			dateSpan.setClassName("label");
			dateSpan.setInnerHTML(eventoIterDTO.getDateTimeEvento());

			HTMLPanel corpoDIV = new HTMLPanel("");
			corpoDIV.setStyleName("abstract");

			Label label = new Label(eventoIterDTO.getNotaEvento());
			corpoDIV.add(label);

			iterPanel.getElement().appendChild(dateSpan);
			iterPanel.add(corpoDIV);
		}

	}
	
	public void clear() {
		iterPanel.clear();
	}
}
