package it.eng.portlet.consolepec.gwt.client.angular;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * @author Giacomo F.M.
 * @since 2019-05-14
 */
public class AngularView extends ViewImpl implements AngularPresenter.MyView {

	private static final String URL = "http://localhost:8080/ionoi2/it/group/console#drive";

	@UiField
	HTMLPanel panel;
	@UiField
	Button showBtn;

	public interface Binder extends UiBinder<Widget, AngularView> {/**/}

	@Inject
	public AngularView(final Binder binder) {
		initWidget(binder.createAndBindUi(this));
		final CustomBox popup = new CustomBox(URL);
		panel.add(popup);
		showBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				popup.center();
			}
		});
		popup.hide();
	}

	public static class CustomBox extends DialogBox {

		public CustomBox(final String url) {
			HTMLPanel panel = new HTMLPanel("");
			panel.setStylePrimaryName("custom-box");

			Frame frame = new Frame(url);
			frame.setHeight("700px");
			frame.setWidth("700px");

			Button close = new Button("Nascondi");
			close.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					hide();
				}
			});

			panel.add(close);
			panel.add(frame);

			setWidget(panel);
			setAnimationEnabled(true);
			hide();
		}

	}

}
