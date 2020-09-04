package it.eng.portlet.consolepec.gwt.client.drive.widget;

import java.util.LinkedList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.portlet.consolepec.gwt.client.drive.DriveView;
import it.eng.portlet.consolepec.gwt.client.drive.ImageSource;
import it.eng.portlet.consolepec.gwt.client.drive.event.ApriCartellaEvent;

/**
 * @author Giacomo F.M.
 * @since 2019-06-05
 */
public class WidgetHistory extends Composite {

	@UiField
	HTMLPanel historyPanel;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetHistory> {/**/}

	public WidgetHistory(final LinkedList<Cartella> history, final EventBus eventBus) {
		initWidget(binder.createAndBindUi(this));
		for (Cartella cartella : history) {
			if (history.indexOf(cartella) != 0) {
				historyPanel.add(new InlineHTML("&nbsp;>&nbsp;"));
			}
			historyPanel.add(drawHistoryBtn(cartella, eventBus));
		}
	}

	private static Button drawHistoryBtn(final Cartella cartella, final EventBus eventBus) {
		Button btn = new Button();
		btn.setTitle(cartella.getNome());
		btn.setStylePrimaryName(DriveView.BTN_HISTORY_STYLE);

		btn.getElement().appendChild(ImageSource.loadFolder(true).getElement());
		btn.getElement().appendChild(new InlineLabel(cartella.getNome()).getElement());

		btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ApriCartellaEvent(cartella.getId(), 1));
			}
		});
		return btn;
	}

}
