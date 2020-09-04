package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.view.client.HasRows;
import com.google.gwt.view.client.Range;

public class CustomPager extends com.google.gwt.user.cellview.client.SimplePager {

	private Integer maxRows;

	public CustomPager(TextLocation center, Resources css, boolean showFastForwardButton, int fastForwardRows, boolean showLastPageButton, Integer maxRows) {
		super(center, css, showFastForwardButton, fastForwardRows, showLastPageButton);
		this.setRangeLimited(true);
		this.maxRows = maxRows;
	}

	/**
	 * Get the text to display in the pager that reflects the state of the pager.
	 *
	 * @return the text
	 */
	@Override
	protected String createText() {
		// Default text is 1 based.
		NumberFormat formatter = NumberFormat.getFormat("#,###");
		HasRows display = getDisplay();
		Range range = display.getVisibleRange();
		int pageStart = range.getStart() + 1;
		int pageSize = range.getLength();
		int dataSize = display.getRowCount();
		int endIndex = Math.min(dataSize, pageStart + pageSize - 1);
		endIndex = Math.max(pageStart, endIndex);
		boolean exact = display.isRowCountExact();

		return formatter.format(pageStart) + "-" + formatter.format(endIndex) +
				((this.maxRows == dataSize) ? " di molte " : ( exact ? " di " : " di oltre ") + formatter.format(dataSize) );
	}

	/**
	 * Vedi linkplain http://stackoverflow.com/questions/6057141/simplepager-row-count-is-working-incorrectly
	 */
	public void setPageStart(int index) {

		if (this.getDisplay() != null) {
			Range range = getDisplay().getVisibleRange();
			int pageSize = range.getLength();
			if (!isRangeLimited() && getDisplay().isRowCountExact()) {
				index = Math.min(index, getDisplay().getRowCount() - pageSize);
			}
			index = Math.max(0, index);
			if (index != range.getStart()) {
				getDisplay().setVisibleRange(index, pageSize);
			}
		}
	}

}
