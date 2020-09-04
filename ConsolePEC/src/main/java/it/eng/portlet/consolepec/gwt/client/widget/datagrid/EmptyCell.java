package it.eng.portlet.consolepec.gwt.client.widget.datagrid;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class EmptyCell extends AbstractCell<SafeHtml>{

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, SafeHtml value, SafeHtmlBuilder sb) {
		if (value != null) {
			sb.append(value);
		}
	}
}
