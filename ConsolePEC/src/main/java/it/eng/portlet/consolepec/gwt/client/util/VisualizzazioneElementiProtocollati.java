package it.eng.portlet.consolepec.gwt.client.util;

import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTMLPanel;

public class VisualizzazioneElementiProtocollati {
	public static HTMLPanel buildGruppoPanel(String label, HTMLPanel parent){
		UListElement ulNonProt = UListElement.as(DOM.createElement("ul"));
		ulNonProt.setClassName("contenitore-lista-gruppi");
		parent.getElement().appendChild(ulNonProt);

		LIElement curLi = LIElement.as(DOM.createElement("li"));
		curLi.setClassName("gruppo clearfix");

		ulNonProt.appendChild(curLi);

		SpanElement pgSpan = SpanElement.as(DOM.createSpan());
		pgSpan.setClassName("label nessun-protocollo");
		pgSpan.setInnerHTML(label);
		curLi.appendChild(pgSpan);
		/* div corpo */
		HTMLPanel corpoDIV = new HTMLPanel("");
		corpoDIV.setStylePrimaryName("corpo");
		parent.add(corpoDIV, curLi);

		HTMLPanel boxDIV = new HTMLPanel("");
		boxDIV.setStyleName("box-mail last");
		corpoDIV.add(boxDIV);
		
		return boxDIV;
	}
}
