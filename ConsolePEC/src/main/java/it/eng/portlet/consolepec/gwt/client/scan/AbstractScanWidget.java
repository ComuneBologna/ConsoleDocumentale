package it.eng.portlet.consolepec.gwt.client.scan;

import java.util.List;

public abstract class AbstractScanWidget implements ScanWidget {

	@Override
	public void scanListWidget(List<VisitableWidget> widget) {
		for (VisitableWidget w : widget) {
			w.visit(this);
		}

	}

}
