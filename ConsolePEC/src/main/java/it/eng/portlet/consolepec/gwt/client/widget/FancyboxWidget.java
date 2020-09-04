package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.widget.images.ResImg;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class FancyboxWidget extends Composite {
	private static FancyboxUiBinder uiBinder = GWT.create(FancyboxUiBinder.class);

	interface FancyboxUiBinder extends UiBinder<Widget, FancyboxWidget> {
	}

	@UiField
	HTMLPanel data;

	@UiField
	HTMLPanel wrapData;

	@UiField
	HTMLPanel linkApriPanel;

	public FancyboxWidget() {
		/*
		 * Anchor linkApri = new Anchor(img.getElement().getInnerHTML()); linkApri.setStyleName("fancybox"); linkApri.setHref("#data"); linkApri.setText("help");
		 */

		initWidget(uiBinder.createAndBindUi(this));

		Image img = new Image(ResImg.IMG.helpImg());
		Element a = DOM.createAnchor();
		a.setAttribute("href", "#data");
		a.appendChild(img.getElement());
		a.addClassName("fancybox");
		linkApriPanel.getElement().appendChild(a);
		// linkApriPanle.add(img);
		// wrapData.getElement().setAttribute("style", "display:none");
		data.getElement().setId("data");
		HTML content = new HTML("<h3>Titolo</h3>" + "  <p>Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>");
		data.add(content);
		inizializza();
	}

	public void setContent(HTML content) {
		data.clear();
		data.add(content);
	}

	private static native void inizializza() /*-{
												$wnd.$('.fancybox').fancybox();
												}-*/;
}
