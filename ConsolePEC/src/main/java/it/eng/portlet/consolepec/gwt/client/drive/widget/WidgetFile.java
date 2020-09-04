package it.eng.portlet.consolepec.gwt.client.drive.widget;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.portlet.consolepec.gwt.client.drive.ImageSource;
import it.eng.portlet.consolepec.gwt.client.drive.event.ScaricaFileEvent;
import it.eng.portlet.consolepec.gwt.client.drive.popup.DriveDialog;
import it.eng.portlet.consolepec.gwt.client.drive.popup.FileDriveDialog;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;

/**
 * @author Giacomo F.M.
 * @since 2019-06-04
 */
public class WidgetFile extends WidgetDriveElement<File> {

	@UiField
	HTMLPanel filePanel;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetFile> {/**/}

	public WidgetFile(final File file, final EventBus eventBus, final DriveHandler driveHandler) {
		initWidget(binder.createAndBindUi(this));
		init(filePanel, file, eventBus, driveHandler);
	}

	@Override
	protected Image loadMainBtnImg(final File element) {
		return ImageSource.loadFile(element.getMimetype());
	}

	@Override
	protected GwtEvent<?> loadDoubleClickEvent(final File element) {
		return new ScaricaFileEvent(element);
	}

	@Override
	protected DriveDialog<File> loadDriveDialog(final File element, final EventBus eventBus, final DriveHandler driveHandler) {
		return new FileDriveDialog(element, eventBus, driveHandler);
	}

	@Override
	protected String loadComplexElementName(File element) {
		if (element.getVersione() != null && !element.getVersione().isEmpty()) {
			return element.getNome() + " (v." + element.getVersione() + ")";
		}
		return super.loadComplexElementName(element);
	}

}
