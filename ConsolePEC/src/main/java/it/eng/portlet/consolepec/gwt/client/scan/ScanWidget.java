package it.eng.portlet.consolepec.gwt.client.scan;

import java.util.List;

public interface ScanWidget {

	public void scanHidden(VisitableWidget visitableWidget);

	public void scanTextBox(VisitableWidget visitableWidget);

	public void scanTextArea(VisitableWidget visitableWidget);

	public void scanHTMLPanel(VisitableWidget visitableWidget);

	public void scanListWidget(List<VisitableWidget> widget);

	public void scanDateBox(VisitableWidget visitableWidget);

	public void scanListBox(VisitableWidget visitableWidget);

	public void scanSuggestBox(VisitableWidget visitableWidget);

	public void scanCustomSuggestBox(VisitableWidget visitableWidget);

	public void scanIntegerBox(VisitableWidget visitableWidget);

	public void scanDoubleBox(VisitableWidget visitableWidget);

	public void scanYesNoRadioButton(VisitableWidget visitableWidget);

	public void scanListWidget(VisitableWidget visitableWidget);

}
