package it.eng.portlet.consolepec.gwt.client.drive.popup;

import java.util.Set;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.cobo.consolepec.commons.drive.metadato.Metadato;
import it.eng.portlet.consolepec.gwt.client.drive.DriveView;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaFileEvent;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;

/**
 * @author Giacomo F.M.
 * @since 2019-06-06
 */
public class FileDriveDialog extends DriveDialog<File> {

	public FileDriveDialog(final File file, final EventBus eventBus, final DriveHandler driveHandler) {
		super(file.getNome(), file, eventBus, driveHandler);
	}

	@Override
	protected Widget drawInfoPanel(final File file) {
		HTMLPanel content = new HTMLPanel("");
		content.setStylePrimaryName(POPUP_CONTENT_STYLE);

		HTMLPanel title = new HTMLPanel("INFO");
		title.setStylePrimaryName(POPUP_CONTENT_TITLE_STYLE);
		content.add(title);

		content.add(drawSimpleBox("ID", file.getId()));
		content.add(drawSimpleBox("ID alfresco", file.getIdAlfresco()));
		content.add(drawSimpleBox("Path alfresco", file.getPathAlfresco()));
		content.add(drawSimpleBox("Versione", file.getVersione()));
		content.add(drawSimpleBox("Utente creazione", file.getUtenteCreazione()));
		content.add(drawSimpleBox("Data creazione", DriveView.DRIVE_DATE_TIME_FORMAT.format(file.getDataCreazione())));
		content.add(drawSimpleBox("Descrizione", file.getDescrizione()));
		return content;
	}

	@Override
	protected void update(Dizionario dizionario, Set<Metadato> metadati) {
		File file = new File();
		file.setId(element.getId());
		file.setDizionario(dizionario.getNome());
		file.getMetadati().addAll(metadati);
		eventBus.fireEvent(new AggiornaFileEvent(file, true));
	}

}
