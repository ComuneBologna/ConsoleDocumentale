package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.shared.model.RicevuteErroreDTO;

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

public class RicevuteErroreWidget extends Composite {

	private static RicevuteErroreWidgetUiBinder uiBinder = GWT.create(RicevuteErroreWidgetUiBinder.class);

	interface RicevuteErroreWidgetUiBinder extends UiBinder<Widget, RicevuteErroreWidget> {
	}

	@UiField
	HTMLPanel ricevutePanel;

	public RicevuteErroreWidget() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void initWidget(List<RicevuteErroreDTO> ricevuteErrore) {

		ricevutePanel.clear();
		Element elem = ricevutePanel.getElement();
		while (elem.hasChildNodes()) {
			elem.removeChild(elem.getFirstChild());
		}

		for (RicevuteErroreDTO ric : ricevuteErrore) {

			SpanElement dateSpan = SpanElement.as(DOM.createSpan());
			dateSpan.setClassName("label");
			dateSpan.setInnerHTML("Errore");

			HTMLPanel corpoDIV = new HTMLPanel("");
			corpoDIV.setStyleName("abstract");

			Label label = new Label(ric.getDest() + ": " + ric.getErrMsg());
			corpoDIV.add(label);

			ricevutePanel.getElement().appendChild(dateSpan);
			ricevutePanel.add(corpoDIV);
		}

	}
}
