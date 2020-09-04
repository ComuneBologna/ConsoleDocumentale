package it.eng.portlet.consolepec.gwt.client.drive.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.portlet.consolepec.gwt.client.drive.DrivePresenter;
import it.eng.portlet.consolepec.gwt.client.drive.DriveView;
import it.eng.portlet.consolepec.gwt.client.drive.ImageSource;
import it.eng.portlet.consolepec.gwt.client.drive.popup.DriveDialog;
import it.eng.portlet.consolepec.gwt.client.event.GoToPlaceEvent;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;
import it.eng.portlet.consolepec.gwt.client.place.NameTokens;
import it.eng.portlet.consolepec.gwt.client.place.NameTokensParams;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter.Place;

/**
 * @author Giacomo F.M.
 * @since 2019-06-19
 */
public abstract class WidgetDriveElement<T extends DriveElement> extends Composite {

	protected abstract Image loadMainBtnImg(final T element);

	protected abstract GwtEvent<?> loadDoubleClickEvent(final T element);

	protected abstract DriveDialog<T> loadDriveDialog(final T element, final EventBus eventBus, final DriveHandler driveHandler);

	protected void init(final HTMLPanel panel, final T element, final EventBus eventBus, final DriveHandler driveHandler) {
		panel.setStylePrimaryName(DriveView.DIV_BTN_STYLE);

		final Button openBtn = drawBtn(element, null, DriveView.LEFT_SUFFIX, ImageSource.loadServices());
		final Button mainBtn = drawBtn(element, loadComplexElementName(element), DriveView.MIDDLE_SUFFIX, loadMainBtnImg(element));
		final Button infoBtn = drawBtn(element, null, DriveView.RIGHT_SUFFIX, ImageSource.loadInfo());

		// toggle(openBtn);
		if (!DrivePresenter.BACK.equals(element.getNome())) {
			openBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Place place = new Place();
					place.setToken(NameTokens.drivedetail);
					place.addParam(NameTokensParams.id, element.getId());
					eventBus.fireEvent(new GoToPlaceEvent(place));
				}
			});
			panel.add(openBtn);
		}

		// mainBtn.addClickHandler(new ClickHandler() {
		// @Override
		// public void onClick(ClickEvent event) {
		// toggle(openBtn);
		// }
		// });
		mainBtn.addDoubleClickHandler(new DoubleClickHandler() {
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				eventBus.fireEvent(loadDoubleClickEvent(element));
			}
		});
		panel.add(mainBtn);

		if (!DrivePresenter.BACK.equals(element.getNome())) {
			final DriveDialog<T> metaDialog = loadDriveDialog(element, eventBus, driveHandler);
			infoBtn.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					metaDialog.open();
				}
			});
			panel.add(infoBtn);
		}
	}

	private static Button drawBtn(final DriveElement element, final String btnText, final String styleSuffix, final Image img) {
		Button btn = new Button();
		btn.setTitle(element.getNome());
		btn.setStylePrimaryName(DriveView.BTN_STYLE);
		btn.addStyleDependentName(styleSuffix);

		btn.getElement().appendChild(img.getElement());
		if (btnText != null) {
			HTML div = new HTML("<span>" + btnText + "</span>" + getDescrizione(element));
			btn.getElement().appendChild(div.getElement());
		}
		return btn;
	}

	private static String getDescrizione(DriveElement element) {
		if (element.getDescrizione() != null && !element.getDescrizione().isEmpty()) {
			return "<span><i>" + element.getDescrizione() + "</i></span>";
		}
		return "";
	}

	protected String loadComplexElementName(final T element) {
		return element.getNome();
	}

	// private static void toggle(Button... buttons) {
	// for (Button button : buttons) {
	// if (button.getStyleName().contains(DriveView.HIDE_SUFFIX)) {
	// button.removeStyleDependentName(DriveView.HIDE_SUFFIX);
	// button.setEnabled(true);
	// } else {
	// button.addStyleDependentName(DriveView.HIDE_SUFFIX);
	// button.setEnabled(false);
	// }
	// }
	// }

}
