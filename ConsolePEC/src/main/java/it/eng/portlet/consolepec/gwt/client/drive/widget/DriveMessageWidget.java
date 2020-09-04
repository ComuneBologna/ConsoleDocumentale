package it.eng.portlet.consolepec.gwt.client.drive.widget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;

import it.eng.portlet.consolepec.gwt.client.drive.ImageSource;

/**
 * @author Giacomo F.M.
 * @since 2019-06-17
 */
public class DriveMessageWidget extends Composite {

	public static final Integer TIMER_TIME_INFO = 5_000;
	public static final Integer TIMER_TIME_SUCCESS = TIMER_TIME_INFO;
	public static final Integer TIMER_TIME_WARNING = 30_000;
	public static final Integer TIMER_TIME_DANGER = 0;

	public static final String MSG_STYLE = "drive-message";
	public static final String INFO = "info"; // info: #17a2b8
	public static final String SUCCESS = "success"; // success: #28a745
	public static final String WARNING = "warning"; // warning: #ffc107
	public static final String DANGER = "danger"; // danger: #dc3545

	@UiField
	HTMLPanel panel;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, DriveMessageWidget> {/**/}

	public DriveMessageWidget() {
		initWidget(binder.createAndBindUi(this));
		panel.setStylePrimaryName(MSG_STYLE);
	}

	public void info(final String text) {
		add(text, INFO, TIMER_TIME_INFO);
	}

	public void success(final String text) {
		add(text, SUCCESS, TIMER_TIME_SUCCESS);
	}

	public void warning(final String text) {
		add(text, WARNING, TIMER_TIME_WARNING);
	}

	public void danger(final String text) {
		add(text, DANGER, TIMER_TIME_DANGER);
	}

	public void clear() {
		panel.clear();
	}

	private void add(final String text, final String style, final Integer timeDelayMillis) {
		HTMLPanel div = new HTMLPanel("");
		drawMessage(div, text, style, timeDelayMillis);
		panel.add(div);
		Window.scrollTo(0, 0);
	}

	private static void drawMessage(final HTMLPanel div, final String text, final String style, final Integer timeDelayMillis) {
		div.setStylePrimaryName(style);
		div.add(new InlineLabel(text));
		Button close = new Button();
		close.setTitle("Chiudi");
		close.getElement().appendChild(ImageSource.loadClose().getElement());
		close.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				div.setVisible(false);
			}
		});
		div.add(close);

		if (timeDelayMillis > 0) {
			Timer t = new Timer() {
				@Override
				public void run() {
					div.setVisible(false);
				}
			};
			t.schedule(timeDelayMillis);
		}
	}

}
