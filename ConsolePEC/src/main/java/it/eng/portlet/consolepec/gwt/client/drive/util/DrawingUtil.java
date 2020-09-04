package it.eng.portlet.consolepec.gwt.client.drive.util;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;

/**
 * @author Giacomo F.M.
 * @since 2019-06-24
 */
public class DrawingUtil {

	public static final String FUN_BTN_STYLE = "drive-fun-btn";

	public static Button drawFunBtn(final String text, final Image img, final ClickHandler clickHandler) {
		Button btn = new Button();
		btn.setStylePrimaryName(FUN_BTN_STYLE);
		if (img != null) {
			btn.getElement().appendChild(img.getElement());
		}
		if (text != null) {
			btn.getElement().appendChild(new HTML(text).getElement());
		}
		if (clickHandler != null) {
			btn.addClickHandler(clickHandler);
		}
		return btn;
	}

}
