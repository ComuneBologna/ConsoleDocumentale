package it.eng.portlet.consolepec.gwt.client.widget;

import it.eng.portlet.consolepec.gwt.client.presenter.event.UploadEvent;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class UploadMonitorWidget extends Composite {

	private static UploadMonitorWidgetUiBinder uiBinder = GWT.create(UploadMonitorWidgetUiBinder.class);

	interface UploadMonitorWidgetUiBinder extends UiBinder<Widget, UploadMonitorWidget> {
	}

	@UiField
	HTMLPanel panelUploadMonitor;
	@UiField
	HTMLPanel panelProgress; // contenitore del disclosure panel
	
	private ProgressWidget progress;
	private int uploadTotali = 0;
	private int uploadCompletati = 0;
	private List<String> errors = new ArrayList<String>();

	public UploadMonitorWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		progress = new ProgressWidget("Nessun upload in corso");
		progress.setNascondiCommand(new Command(){

			@Override
			public void execute() {
				nascondimi();
			}
		});
		panelProgress.add(progress);
		panelUploadMonitor.setVisible(false);
	}

	public void setUploadStatus(UploadEvent event) {
		switch (event.getUploadStatus()) {
		case START:
			uploadTotali++;
			if (uploadTotali == 1) {
				progress.start();
				mostrami();
			}
			updateText();
			break;
		case DONE:
			uploadCompletati++;
			if (uploadCompletati == uploadTotali) {
				progress.done();
				uploadCompletati = 0;
				uploadTotali = 0;
				progress.setText("Tutti gli upload sono stati completati con successo");
				mostrami();
			} else {
				updateText();
			}
			break;
		case ERROR:
			uploadCompletati++;
			errors.add(event.getClientID());
			if (uploadCompletati == uploadTotali) {
				progress.error();
				uploadCompletati = 0;
				uploadTotali = 0;
				progress.setText("Upload completati con errori (numero di upload falliti: " + errors.size() + ")");
				mostrami();
			} else {
				updateText();
			}
			break;
		}
	}

	private void mostrami() {
		if (!panelUploadMonitor.isVisible())
			panelUploadMonitor.setVisible(true);
	}
	
	private void nascondimi() {
		panelUploadMonitor.setVisible(false);
	}

	private void updateText() {
		progress.setText("Upload in corso (" + uploadCompletati + " di " + uploadTotali + " completati)");
	}

}
