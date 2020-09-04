package it.eng.portlet.consolepec.gwt.client.widget.css;



import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.cellview.client.CellTable;

public interface  Res extends CellTable.Resources {
	public static final CellTable.Resources	CSS =  GWT.create(Res.class);

	@Source({CellTable.Style.DEFAULT_CSS, "table.css"})
	
	@CssResource.NotStrict
	TableStyle cellTableStyle();
	 
	interface TableStyle extends CellTable.Style {}
}