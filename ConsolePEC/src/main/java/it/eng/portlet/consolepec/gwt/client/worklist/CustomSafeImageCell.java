package it.eng.portlet.consolepec.gwt.client.worklist;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.gwt.cell.client.SafeImageCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;

public class CustomSafeImageCell extends SafeImageCell {

	private String title;
	private boolean hasLink;

	public CustomSafeImageCell(boolean hasLink) {
		this.hasLink = hasLink;
	}

	@Override
	public Set<String> getConsumedEvents() {
		Set<String> consumedEvents = new HashSet<String>();
		if (hasLink) {
	        consumedEvents.add("click");
		}
        return consumedEvents;
	}


	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context, SafeUri value, SafeHtmlBuilder sb) {
		if (value != null) {
			StringBuilder sb1 = new StringBuilder();

			if (hasLink)
				sb1.append("<a href=\"javascript:;\">");

			sb1.append("<img src=\"");
			sb1.append(value.asString());
			sb1.append("\" ");
			if (!Strings.isNullOrEmpty(title)) {
				sb1.append("title=\"");
				sb1.append(title);
				sb1.append("\"");
			}
			sb1.append(" />");

			if (hasLink)
				sb1.append("</a>");

			sb.appendHtmlConstant(sb1.toString());
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setHasLink(boolean bln) {
		this.hasLink = true;
	}
}
