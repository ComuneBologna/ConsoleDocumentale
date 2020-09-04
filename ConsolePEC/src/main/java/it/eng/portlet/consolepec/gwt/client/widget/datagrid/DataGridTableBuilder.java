package it.eng.portlet.consolepec.gwt.client.widget.datagrid;

import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget.ProvidesElementoEvidenziatoInfo;
import it.eng.portlet.consolepec.gwt.client.widget.datagrid.DataGridWidget.Style;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.TableRowElement;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.DefaultCellTableBuilder;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.view.client.SelectionModel;

public class DataGridTableBuilder<T> extends DefaultCellTableBuilder<T> {

	private final String evenRowStyle;
	private final String oddRowStyle;
	private final String selectedRowStyle;
	private final String cellStyle;
	private final String evenCellStyle;
	private final String oddCellStyle;
	private final String firstColumnStyle;
	private final String lastColumnStyle;
	private final String selectedCellStyle;
	
	
	private Map<Integer, HTMLPanel> detailsMap = new HashMap<Integer, HTMLPanel>();
	private Map<Integer, HTMLPanel> operationsMap = new HashMap<Integer, HTMLPanel>();
	private String item;
	private String itemnew;
	private String itemred;
	private String itemtd;
	private String itemtr;
	private String operations;
	private String details;
	private String row;
	
	public DataGridTableBuilder(DataGridWidget<T> grid) {
		super(grid);

		com.google.gwt.user.cellview.client.AbstractCellTable.Style style = cellTable.getResources().style();
		evenRowStyle = style.evenRow();
		oddRowStyle = style.oddRow();
		selectedRowStyle = " " + style.selectedRow();
		cellStyle = style.cell();
		evenCellStyle = " " + style.evenRowCell();
		oddCellStyle = " " + style.oddRowCell();
		firstColumnStyle = " " + style.firstColumn();
		lastColumnStyle = " " + style.lastColumn();
		selectedCellStyle = " " + style.selectedRowCell();
		Style gridStyle = grid.getDataGridResources().cellTableStyle();
		item = gridStyle.item();
		itemnew = gridStyle.itemnew();
		itemred = gridStyle.itemred();
		itemtd = gridStyle.itemtd();
		itemtr = gridStyle.itemtr();
		operations = gridStyle.operations();
		details = gridStyle.details();
		row = gridStyle.row();
	}
	/**
	 * 
	 * @param rowIndex
	 * @return - elem[0] item elem[1] details elem[2] operations
	 */
	private Element[] getElementRiga(int rowIndex){
		TableRowElement tre = cellTable.getRowElement(rowIndex);
		NodeList<Element> desc = tre.getElementsByTagName("div");
		Element[] divs = new Element[3];
		for(int i=0; i<desc.getLength();i++){
			Element ele = desc.getItem(i);
			if("details".equals(ele.getClassName())){
				divs[1] = ele;
			}
			else if("operations".equals(ele.getClassName())){
				divs[2] = ele;
			}
			else if(ele.getClassName().contains("item")){
				divs[0] = ele;
			}
		}
		return divs;
	}
	/**
	 * Apre o aggiorna una riga espansa.
	 * @param rowIndex - l'indice della riga (valore tra 0 e pageSize-1)
	 * @param content
	 * @param operations
	 */
	public void espandiRiga(int rowIndex, HTMLPanel content, HTMLPanel operations){
		Element[] divs = getElementRiga(rowIndex);
		Element detailsDIV = divs[1];
		Element operationsDIV = divs[2];
		Element rowDIV = divs[0];
		
		if(rowDIV.getClassName().contains("open")){
			/*si tratta di una update*/
			RootPanel.get().remove(detailsMap.remove(rowIndex));
			RootPanel.get().remove(operationsMap.remove(rowIndex));
		}
		else
			rowDIV.addClassName("open");
		RootPanel.get().add(content);
		RootPanel.get().add(operations);
		detailsDIV.appendChild(content.getElement());
		operationsDIV.appendChild(operations.getElement());
	
		detailsMap.put(rowIndex, content);
		operationsMap.put(rowIndex, operations);
	}
	/**
	 * Chiude una riga espansa. Se già chiusa, il metodo ritorna senza errore
	 * @param rowIndex - l'indice della riga (valore tra 0 e pageSize-1)
	 */
	public void chiudiRiga(int rowIndex){
		Element[] divs = getElementRiga(rowIndex);
		Element rowDIV = divs[0];
		if(rowDIV.getClassName().contains("open")){
			rowDIV.removeClassName("open");
			RootPanel.get().remove(detailsMap.remove(rowIndex));
			RootPanel.get().remove(operationsMap.remove(rowIndex));
		}
	}
	/**
	 * Verifica se una riga sia espansa
	 * @param rowIndex - l'indice della riga (valore tra 0 e pageSize-1)
	 * @return
	 */
	public boolean isRigaEspansa(int rowIndex){
		TableRowElement tre = cellTable.getRowElement(rowIndex);
		NodeList<Element> desc = tre.getElementsByTagName("div");
		for(int i=0; i<desc.getLength();i++){
			Element ele = desc.getItem(i);
			if(ele.getClassName().contains("item")){
				return ele.getClassName().contains("open");
			}
		}	
		return false;
	}
	/**
	 * 
	 * @param rowValue
	 * @param rowIndex - l'indice della riga (valore tra 0 e pageSize-1)
	 */
	public void updateEvidenziato(T rowValue, int rowIndex){
		Element item = getElementRiga(rowIndex)[0];
		updateEvidenziato(rowValue, item);
	}
	
	
	@Override
	public void buildRowImpl(T rowValue, int absRowIndex) {
		
		
		// Calculate the row styles.
		SelectionModel<? super T> selectionModel = cellTable.getSelectionModel();
		boolean isSelected = (selectionModel == null || rowValue == null) ? false : selectionModel.isSelected(rowValue);
		boolean isEven = absRowIndex % 2 == 0;
		StringBuilder trClasses = new StringBuilder(isEven ? evenRowStyle : oddRowStyle);
		if (isSelected) {
			trClasses.append(selectedRowStyle);
		}

		// Add custom row styles.
		RowStyles<T> rowStyles = cellTable.getRowStyles();
		if (rowStyles != null) {
			String extraRowStyles = rowStyles.getStyleNames(rowValue, absRowIndex);
			if (extraRowStyles != null) {
				trClasses.append(" ").append(extraRowStyles);
			}
		}

		// Build the row.
		TableRowBuilder tr = startRow();
		tr.className(itemtr);
		tr.className(trClasses.toString());
		
		// Build the columns.
		int columnCount = cellTable.getColumnCount();
		TableCellBuilder tdRow = tr.startTD();
		tdRow.className(itemtd);
		DivBuilder rowOuterDIV = tdRow.startDiv();
		/*PRIMO SET DELLO STATO EVIDENZIATO*/
		ProvidesElementoEvidenziatoInfo<T> evProvider = ((DataGridWidget<T>)cellTable).getEvidenziatoInfoProvider();
		if(evProvider!=null && evProvider.isEvidenziato(rowValue))
			rowOuterDIV.className(item+" "+itemnew);
		else
			rowOuterDIV.className(item+" "+itemred);
		DivBuilder rowInnerDIV = rowOuterDIV.startDiv();
		// TODO gestione stili
		rowInnerDIV.className(row);
		for (int curColumn = 0; curColumn < columnCount; curColumn++) {
			Column<T, ?> column = cellTable.getColumn(curColumn);
			// Create the cell styles.
			StringBuilder tdClasses = new StringBuilder(cellStyle);
			tdClasses.append(isEven ? evenCellStyle : oddCellStyle);
			if (curColumn == 0) {
				tdClasses.append(firstColumnStyle);
			}
			if (isSelected) {
				tdClasses.append(selectedCellStyle);
			}
			// The first and last column could be the same column.
			if (curColumn == columnCount - 1) {
				tdClasses.append(lastColumnStyle);
			}

			// Add class names specific to the cell.
			Context context = new Context(absRowIndex, curColumn, cellTable.getValueKey(rowValue));
			String cellStyles = column.getCellStyleNames(context, rowValue);
			if (cellStyles != null) {
				tdClasses.append(" " + cellStyles);
			}

			// Build the cell.
//			HorizontalAlignmentConstant hAlign = column.getHorizontalAlignment();
//			VerticalAlignmentConstant vAlign = column.getVerticalAlignment();
			// Add the inner div.
			DivBuilder div = rowInnerDIV.startDiv();
			
			div.className(tdClasses.toString());
			//TODO gestire possibilità di allineamento
			//			if (hAlign != null) {
//				div.align(hAlign.getTextAlignString());
//			}
//			if (vAlign != null) {
//				td.vAlign(vAlign.getVerticalAlignString());
//			}

			
//			div.style().outlineStyle(OutlineStyle.NONE).endStyle();

			// Render the cell into the div.
			renderCell(div, context, column, rowValue);

			// End the cell.
			div.endDiv();
		}

		// End the row.
		rowInnerDIV.end();
		DivBuilder rowDetailsDIV = rowOuterDIV.startDiv();
		//TODO stili
		rowDetailsDIV.className(details);
		rowDetailsDIV.end();
		DivBuilder rowOperationsDIV = rowOuterDIV.startDiv();
		rowOperationsDIV.className(operations);
		rowOperationsDIV.end();
		rowOuterDIV.end();
		tdRow.endTD();
		tr.endTR();

	}
	
	/*metodi interni*/
	
	private void updateEvidenziato(T rowValue, Element item){
		ProvidesElementoEvidenziatoInfo<T> evProvider = ((DataGridWidget<T>)cellTable).getEvidenziatoInfoProvider();
		String newClass = item.getClassName().replaceAll(itemnew, "").replaceAll(itemred, "").trim();
		if(evProvider!=null && evProvider.isEvidenziato(rowValue)){
			item.setClassName(newClass+" "+itemnew);
		}
		else{
			item.setClassName(newClass+" "+itemred);
		}
	}
	
}
