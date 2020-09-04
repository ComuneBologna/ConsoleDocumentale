package it.eng.portlet.consolepec.gwt.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ProgressWidget extends Composite {

	private static ProgressWidgetUiBinder uiBinder = GWT.create(ProgressWidgetUiBinder.class);

	interface ProgressWidgetUiBinder extends UiBinder<Widget, ProgressWidget> {
	}

	Timer simulationTimer;
	ProgressBar progressBar;
	@UiField
	Button nascondiButton;

	@UiField
	Label progressText;
	@UiField
	HTMLPanel progressBarPanel;

	public ProgressWidget() {
		super();
		initWidget(uiBinder.createAndBindUi(this));
		progressBar = new ProgressBar();
		//progressBar.setTextVisible(false);
		progressBarPanel.add(progressBar);
		simulationTimer = new Timer() {
			@Override
			public void run() {
				progressBar.setProgress((progressBar.getPercent() >= 1.0) ? 0.0 : progressBar.getProgress() + 10.0);
			}
		};
	}
	
	public ProgressWidget(String testoIniziale) {
		this();
		progressText.setText(testoIniziale);
		progressBar.setProgress(0.0);
	}

	public void start() {
		progressBar.setProgress(0.0);
		simulationTimer.scheduleRepeating(500);
	}

	public void done() {
		simulationTimer.cancel();
		progressBar.setProgress(100.0);
	}

	public void error() {
		simulationTimer.cancel();
		progressBar.setProgress(0.0);
	}

	public void setText(String text) {
		progressText.setText(text);
	}
	
	public void setNascondiCommand(final Command nascondiCommand){
		nascondiButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				nascondiCommand.execute();
			}
		});
	}

}