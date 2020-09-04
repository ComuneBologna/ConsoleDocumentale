package it.eng.portlet.consolepec.gwt.client.drive;

import java.util.Arrays;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.DriveElement;
import it.eng.cobo.consolepec.commons.drive.File;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.drive.util.DrawingUtil;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMessageWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMetadatiWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveRuoliWidget;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;

/**
 * @author Giacomo F.M.
 * @since 2019-06-19
 */
public class DriveDetailView extends ViewImpl implements DriveDetailPresenter.MyView {

	protected static DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);

	public static final String DIV_STYLE = "drive-detail-div";

	@UiField
	HTMLPanel errorPanel;
	@UiField
	HTMLPanel ruoliPanel, metadatiPanel;
	@UiField
	HTMLPanel buttonPanel;

	@UiField
	HTMLPanel title, info;
	@UiField
	TextArea descArea;

	@UiField
	DriveMessageWidget message;

	@Override
	public DriveMessageWidget getMessageWidget() {
		return this.message;
	}

	private Command salvaCommand, apriCommand, annullaCommand, eliminaCommand;

	private DriveRuoliWidget ruoli;

	@Override
	public DriveRuoliWidget getRuoliWidget() {
		return this.ruoli;
	}

	private DriveMetadatiWidget metadati;

	@Override
	public DriveMetadatiWidget getMetadatiWidget() {
		return this.metadati;
	}

	public interface Binder extends UiBinder<Widget, DriveDetailView> {/**/}

	@Inject
	public DriveDetailView(final Binder binder) {
		initWidget(binder.createAndBindUi(this));
		Window.scrollTo(0, 0);
		errorPanel.setVisible(false);
	}

	@Override
	public void bindSalvaCommand(final Command command) {
		this.salvaCommand = command;
	}

	@Override
	public void bindApriCommand(final Command command) {
		this.apriCommand = command;
	}

	@Override
	public void bindAnnullaCommand(final Command command) {
		this.annullaCommand = command;
	}

	@Override
	public void bindEliminaCommand(Command command) {
		this.eliminaCommand = command;
	}

	@Override
	public void clear(final boolean clearMessage) {
		if (clearMessage) {
			message.clear();
		}
		Window.scrollTo(0, 0);
		errorPanel.setVisible(false);
		errorPanel.clear();
		title.clear();
		info.clear();
		ruoliPanel.clear();
		metadatiPanel.clear();
		buttonPanel.clear();
	}

	@Override
	public void mostraErrore(final String msgError) {
		errorPanel.setVisible(true);
		errorPanel.add(new InlineLabel(msgError));
	}

	@Inject
	private ConfigurazioniHandler configurazioniHandler;
	@Inject
	private DriveHandler driveHandler;

	@Override
	public void dettaglioElemento(final boolean clearMessage, final DriveElement elemento) {
		clear(clearMessage);

		this.ruoli = new DriveRuoliWidget(elemento, configurazioniHandler);
		this.metadati = new DriveMetadatiWidget(elemento, driveHandler);

		if (elemento.isCartella()) {
			drawCartellaBox((Cartella) elemento);
		} else {
			drawFileBox((File) elemento);
		}

		drawDescrizione(elemento, false);

		ruoliPanel.add(this.ruoli);
		metadatiPanel.add(this.metadati);

		buttonPanel.add(DrawingUtil.drawFunBtn("Salva", ImageSource.loadSave(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				salvaCommand.execute();
			}
		}));
		buttonPanel.add(DrawingUtil.drawFunBtn("Modifica", ImageSource.loadEdit(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				drawDescrizione(elemento, true);
				metadati.ridisegna(true);
			}
		}));
		buttonPanel.add(DrawingUtil.drawFunBtn("Annulla", ImageSource.loadCancel(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (metadati.isModifica()) {
					drawDescrizione(elemento, false);
					metadati.ridisegna(false);
				} else {
					annullaCommand.execute();
				}
			}
		}));
		buttonPanel.add(DrawingUtil.drawFunBtn("Apri cartella", ImageSource.loadShare(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				apriCommand.execute();
			}
		}));
		buttonPanel.add(DrawingUtil.drawFunBtn("Elimina", ImageSource.loadTrash(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (Window.confirm("Confermi di voler eliminare questo elemento?")) {
					eliminaCommand.execute();
				}
			}
		}));
	}

	private void drawCartellaBox(final Cartella cartella) {
		title.add(new InlineLabel("Info cartella"));
		info.add(drawSimpleBox("ID", cartella.getId()));
		info.add(drawSimpleBox("Cartella padre", cartella.getIdPadre()));
		info.add(drawSimpleBox("Path alfresco", cartella.getPathAlfresco()));
		info.add(drawSimpleBox("Nome", cartella.getNome()));
		info.add(drawSimpleBox("Nomenclatura", cartella.getNomenclatura()));
		info.add(drawSimpleBox("Elementi contenuti", Arrays.toString(cartella.getIdFigli().toArray())));
		info.add(drawSimpleBox("Utente creazione", cartella.getUtenteCreazione()));
		info.add(drawSimpleBox("Data creazione", DriveView.DRIVE_DATE_TIME_FORMAT.format(cartella.getDataCreazione())));
	}

	private void drawFileBox(final File file) {
		title.add(new InlineLabel("Info file"));
		info.add(drawSimpleBox("ID", file.getId()));
		info.add(drawSimpleBox("Cartella padre", file.getIdCartella()));
		info.add(drawSimpleBox("Path alfresco", file.getPathAlfresco()));
		info.add(drawSimpleBox("ID alfresco", file.getIdAlfresco()));
		info.add(drawSimpleBox("Nome", file.getNome()));
		info.add(drawSimpleBox("Versione", file.getVersione()));
		info.add(drawSimpleBox("Mimetype", file.getMimetype()));
		info.add(drawSimpleBox("Utente creazione", file.getUtenteCreazione()));
		info.add(drawSimpleBox("Data creazione", DriveView.DRIVE_DATE_TIME_FORMAT.format(file.getDataCreazione())));
	}

	private void drawDescrizione(final DriveElement e, final boolean modifica) {
		descArea.setEnabled(modifica);
		descArea.setText(e.getDescrizione());
	}

	@Override
	public String getDescrizione() {
		return descArea.getText();
	}

	private static Widget drawSimpleBox(final String label, final String value) {
		HTMLPanel div = new HTMLPanel("");
		div.setStylePrimaryName(DIV_STYLE);
		div.add(new InlineLabel(label));
		div.add(new InlineLabel(value));
		return div;
	}

}
