package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class CustomButtonCell extends ButtonCell {

	private String styleName;
	private boolean visible = true;

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public CustomButtonCell() {

	}

	public CustomButtonCell(String styleName) {
		this.styleName = styleName;
	}

	@Override
	public void render(final Context context, final SafeHtml data, final SafeHtmlBuilder sb) {
		if (visible) {
			sb.appendHtmlConstant("<button type=\"button\" class=\"" + styleName + "\" tabindex=\"-1\">");
			if (data != null) {
				sb.append(data);
			}
			sb.appendHtmlConstant("</button>");
		}
	}
}
