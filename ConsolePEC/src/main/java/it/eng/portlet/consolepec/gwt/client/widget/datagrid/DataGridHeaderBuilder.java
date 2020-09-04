package it.eng.portlet.consolepec.gwt.client.widget.datagrid;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.ElementBuilderBase;
import com.google.gwt.dom.builder.shared.TableCellBuilder;
import com.google.gwt.dom.builder.shared.TableRowBuilder;
import com.google.gwt.user.cellview.client.AbstractCellTable;
import com.google.gwt.user.cellview.client.AbstractHeaderOrFooterBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.Header;

public class DataGridHeaderBuilder<T> extends AbstractHeaderOrFooterBuilder<T> {

	public DataGridHeaderBuilder(DataGridWidget<T> grid) {
		super(grid, false);
		setSortIconStartOfLine(false);
	}

	@Override
	protected boolean buildHeaderOrFooterImpl() {
		AbstractCellTable<T> table = getTable();
		boolean isFooter = isBuildingFooter();

		// Early exit if there aren't any columns to render.
		int columnCount = table.getColumnCount();
		if (columnCount == 0) {
			// Nothing to render;
			return false;
		}

		// Early exit if there aren't any headers in the columns to render.
		boolean hasHeader = false;
		for (int i = 0; i < columnCount; i++) {
			if (getHeader(i) != null) {
				hasHeader = true;
				break;
			}
		}
		if (hasHeader == false) {
			return false;
		}

		// Get information about the sorted column.
		ColumnSortList sortList = table.getColumnSortList();
		ColumnSortInfo sortedInfo = (sortList.size() == 0) ? null : sortList.get(0);
		Column<?, ?> sortedColumn = (sortedInfo == null) ? null : sortedInfo.getColumn();
		boolean isSortAscending = (sortedInfo == null) ? false : sortedInfo.isAscending();

		// Get the common style names.
		com.google.gwt.user.cellview.client.AbstractCellTable.Style style = getTable().getResources().style();
		String className = "";
		// String className = isBuildingFooter() ? style.footer() :
		// style.header();
		String sortableStyle = " " + style.sortableHeader();
		// (isSortAscending ? "asc":"desc");style.sortedHeaderAscending():style.sortedHeaderDescending());
		String sortedStyle = " " + (isSortAscending ? "asc" : "desc");// style.sortedHeaderAscending() : style.sortedHeaderDescending());
		// Setup the first column.
		com.google.gwt.user.cellview.client.Header<?> prevHeader = getHeader(0);
		Column<T, ?> column = getTable().getColumn(0);
		int prevColspan = 1;
		boolean isSortable = false;
		boolean isSorted = false;
		StringBuilder classesBuilder = new StringBuilder(className);
		// classesBuilder.append(" " + (isFooter ? style.firstColumnFooter() :
		// style.firstColumnHeader()));
		if (!isFooter && column.isSortable()) {
			isSortable = true;
			isSorted = (column == sortedColumn);
		}

		// Loop through all column headers.
		TableRowBuilder tr = startRow();
		TableCellBuilder thead = tr.startTH().colSpan(prevColspan);// .className(classesBuilder.toString());
		DivBuilder headDiv = thead.startDiv();
		// TODO prelevare da resources
		headDiv.className("row row-title");

		int curColumn;
		for (curColumn = 1; curColumn < columnCount; curColumn++) {
			com.google.gwt.user.cellview.client.Header<?> header = getHeader(curColumn);

			if (header != prevHeader) {
				// The header has changed, so append the previous one.
				if (isSortable) {
					classesBuilder.append(sortableStyle);
				}
				if (isSorted) {
					classesBuilder.append(sortedStyle);
				}
				appendExtraStyles(prevHeader, classesBuilder);
				DivBuilder div = headDiv.startDiv();

				div.className(classesBuilder.toString());
				// Render the header.
				enableColumnHandlers(div, column);

				if (prevHeader != null) {
					// Build the header.
					com.google.gwt.cell.client.Cell.Context context = new com.google.gwt.cell.client.Cell.Context(0, curColumn - prevColspan, prevHeader.getKey());
					renderDataGridSortableHeader(div, context, prevHeader, isSorted, isSortAscending);
				}
				div.end();
//				if (isSortable) {
//					DivBuilder fillerDiv = headDiv.startDiv();
//					fillerDiv.end();
//				}
				// Reset the previous header.
				prevHeader = header;
				prevColspan = 1;
				classesBuilder = new StringBuilder(className);
				isSortable = false;
				isSorted = false;
			} else {
				// Increment the colspan if the headers == each other.
				prevColspan++;
			}

			// Update the sorted state.
			column = table.getColumn(curColumn);
			if (!isFooter && column.isSortable()) {
				isSortable = true;
				isSorted = (column == sortedColumn);
			}
		}

		// Append the last header.
		if (isSortable) {
			classesBuilder.append(sortableStyle);
		}
		if (isSorted) {
			classesBuilder.append(sortedStyle);
		}

		// The first and last columns could be the same column.
		classesBuilder.append(" ").append(isFooter ? style.lastColumnFooter() : style.lastColumnHeader());
		appendExtraStyles(prevHeader, classesBuilder);

		// Render the last header.
		DivBuilder div = headDiv.startDiv();
		div.className(classesBuilder.toString());
		// TableCellBuilder th =
		// tr.startTH().colSpan(prevColspan).className(classesBuilder.toString());
		enableColumnHandlers(div, column);
		if (prevHeader != null) {
			com.google.gwt.cell.client.Cell.Context context = new com.google.gwt.cell.client.Cell.Context(0, curColumn - prevColspan, prevHeader.getKey());
			renderDataGridSortableHeader(div, context, prevHeader, isSorted, isSortAscending);
		}
		div.end();
//		if(isSortable){
//			DivBuilder fillerDiv = headDiv.startDiv();
//			fillerDiv.end();
//		}

		headDiv.end();
		thead.endTH();
		// End the row.
		tr.endTR();

		return true;

	}

	// TODO utilizzare le image resources, che al momento causano problemi di
	// allineamento CSS
	protected final void renderDataGridSortableHeader(ElementBuilderBase<?> out, Context context, Header<?> header, boolean isSorted, boolean isSortAscending) {
		ElementBuilderBase<?> headerContainer = out;
		boolean isFooter = isBuildingFooter();
		// Wrap the header in a sort icon if sorted.
		isSorted = isSorted && !isFooter;
		if (isSorted) {
			// Determine the position of the sort icon.
			// boolean posRight =
			// LocaleInfo.getCurrentLocale().isRTL() ? isSortIconStartOfLine :
			// !isSortIconStartOfLine;

			// Create an outer container to hold the icon and the header.
			// int iconWidth = isSortAscending ? sortAscIconWidth :
			// sortDescIconWidth;
			// int halfHeight = isSortAscending ? sortAscIconHalfHeight :
			// sortDescIconHalfHeight;
			// DivBuilder outerDiv = out.startDiv();
			// StylesBuilder style =
			// outerDiv.style().position(Position.RELATIVE).trustedProperty("zoom",
			// "1");
			// if (posRight) {
			// style.paddingRight(iconWidth, Unit.PX);
			// } else {
			// style.paddingLeft(iconWidth, Unit.PX);
			// }
			// style.endStyle();
			//
			// // Add the icon.
			// DivBuilder imageHolder = outerDiv.startDiv();
			// style =
			// outerDiv.style().position(Position.ABSOLUTE).top(50.0,
			// Unit.PCT).lineHeight(0.0, Unit.PX)
			// .marginTop(-halfHeight, Unit.PX);
			// if (posRight) {
			// style.right(0, Unit.PX);
			// } else {
			// style.left(0, Unit.PX);
			// }
			//
			// style.endStyle();
			// imageHolder.html(getSortIcon(isSortAscending));
			// imageHolder.endDiv();

			// Create the header wrapper.
			// headerContainer = outerDiv.startDiv();
			// if(isSortAscending){
			// headerContainer.style().backgroundImage((getTable().getResources().sortAscending().getSafeUri().asString());
			// }
			// else{
			// headerContainer.style().backgroundImage(getTable().getResources().sortDescending().getSafeUri());
			// }

		}

		// Build the header.
		renderHeader(headerContainer, context, header);

		// Close the elements used for the sort icon.
		// if (isSorted) {
		// headerContainer.endDiv(); // headerContainer.
		// headerContainer.endDiv(); // outerDiv
		// }
	}

	private <H> void appendExtraStyles(com.google.gwt.user.cellview.client.Header<H> header, StringBuilder classesBuilder) {
		if (header == null) {
			return;
		}
		String headerStyleNames = header.getHeaderStyleNames();
		if (headerStyleNames != null) {
			classesBuilder.append(" ");
			classesBuilder.append(headerStyleNames);
		}
	}
}