package it.eng.portlet.consolepec.gwt.client.cell;

import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Aggiunta per modificare lo stile di tutte le checkbox della console, da sostituire alla classica {@link CheckboxCell}
 * 
 * @author GiacomoFM
 * @since 07/mar/2019
 */
public class StyledCheckboxCell extends CheckboxCell {

	// public static final String OPEN_LABEL_CONTAINER = "<label class=\"container\">";
	public static final String SPAN_POST_CHECKBOX = "<span class=\"checkmark\"></span>";

	private String[] cssClasses;

	public StyledCheckboxCell(String... additionalCssClasses) {
		this.cssClasses = additionalCssClasses;
	}

	@Override
	public void render(Context context, Boolean value, SafeHtmlBuilder shb) {
		StringBuilder sb = new StringBuilder("<label class=\"container");
		if (cssClasses != null && cssClasses.length > 0) {
			for (String cssClass : cssClasses) {
				sb.append(" ").append(cssClass);
			}
		}
		sb.append("\">");

		shb.append(SafeHtmlUtils.fromSafeConstant(sb.toString()));
		super.render(context, value, shb);
		shb.append(SafeHtmlUtils.fromSafeConstant(SPAN_POST_CHECKBOX));
		shb.append(SafeHtmlUtils.fromSafeConstant("</label>"));
	}
}
