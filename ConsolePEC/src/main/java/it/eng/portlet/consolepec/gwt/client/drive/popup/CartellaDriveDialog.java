package it.eng.portlet.consolepec.gwt.client.drive.popup;

import java.util.Arrays;
import java.util.Set;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.Dizionario;
import it.eng.cobo.consolepec.commons.drive.metadato.Metadato;
import it.eng.portlet.consolepec.gwt.client.drive.DriveView;
import it.eng.portlet.consolepec.gwt.client.drive.event.AggiornaCartellaEvent;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;

/**
 * @author Giacomo F.M.
 * @since 2019-06-07
 */
public class CartellaDriveDialog extends DriveDialog<Cartella> {

	public CartellaDriveDialog(final Cartella cartella, final EventBus eventBus, final DriveHandler driveHandler) {
		super(cartella.getNome(), cartella, eventBus, driveHandler);
	}

	@Override
	protected Widget drawInfoPanel(final Cartella cartella) {
		HTMLPanel content = new HTMLPanel("");
		content.setStylePrimaryName(POPUP_CONTENT_STYLE);

		HTMLPanel title = new HTMLPanel("Info");
		title.setStylePrimaryName(POPUP_CONTENT_TITLE_STYLE);
		content.add(title);

		content.add(drawSimpleBox("ID", cartella.getId()));
		content.add(drawSimpleBox("Path alfresco", cartella.getPathAlfresco()));
		content.add(drawSimpleBox("ID figli", Arrays.toString(cartella.getIdFigli().toArray())));
		content.add(drawSimpleBox("Utente creazione", cartella.getUtenteCreazione()));
		content.add(drawSimpleBox("Data creazione", DriveView.DRIVE_DATE_TIME_FORMAT.format(cartella.getDataCreazione())));
		content.add(drawSimpleBox("Descrizione", cartella.getDescrizione()));
		return content;
	}

	@Override
	protected void update(Dizionario dizionario, Set<Metadato> metadati) {
		Cartella cartella = new Cartella();
		cartella.setId(element.getId());
		cartella.setDizionario(dizionario.getNome());
		cartella.getMetadati().addAll(metadati);
		eventBus.fireEvent(new AggiornaCartellaEvent(cartella, true));
	}

}
