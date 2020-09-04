package it.eng.portlet.consolepec.gwt.client.drive.widget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.portlet.consolepec.gwt.client.drive.DrivePresenter;
import it.eng.portlet.consolepec.gwt.client.drive.ImageSource;
import it.eng.portlet.consolepec.gwt.client.drive.event.ApriCartellaEvent;
import it.eng.portlet.consolepec.gwt.client.drive.popup.CartellaDriveDialog;
import it.eng.portlet.consolepec.gwt.client.drive.popup.DriveDialog;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;

/**
 * @author Giacomo F.M.
 * @since 2019-05-31
 */
public class WidgetCartella extends WidgetDriveElement<Cartella> {

	@UiField
	HTMLPanel cartellaPanel;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetCartella> {/**/}

	public WidgetCartella(final Cartella cartella, final EventBus eventBus, final DriveHandler driveHandler) {
		initWidget(binder.createAndBindUi(this));
		init(cartellaPanel, cartella, eventBus, driveHandler);
	}

	@Override
	protected Image loadMainBtnImg(final Cartella element) {
		return ImageSource.loadFolder(DrivePresenter.BACK.equals(element.getNome()));
	}

	@Override
	protected GwtEvent<?> loadDoubleClickEvent(final Cartella element) {
		return new ApriCartellaEvent(element.getId(), 1);
	}

	@Override
	protected DriveDialog<Cartella> loadDriveDialog(final Cartella element, final EventBus eventBus, final DriveHandler driveHandler) {
		return new CartellaDriveDialog(element, eventBus, driveHandler);
	}

}
