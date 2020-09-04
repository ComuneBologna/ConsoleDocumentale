package it.eng.portlet.consolepec.gwt.client.drive;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.GestioneDriveAbilitazione;
import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.portlet.consolepec.gwt.client.drive.util.DrawingUtil;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveFileUploadWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMessageWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMetadatiWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMetadatiWidget.ModificheDizionario;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;

/**
 * @author Giacomo F.M.
 * @since 2019-06-28
 */
public class DriveFileView extends ViewImpl implements DriveFilePresenter.MyView {

	@UiField
	DriveMessageWidget message;

	@Override
	public DriveMessageWidget getMessageWidget() {
		return this.message;
	}

	@UiField
	DriveFileUploadWidget uploadWidget;

	@Override
	public DriveFileUploadWidget getUploadWidget() {
		return this.uploadWidget;
	}

	private DriveMetadatiWidget metadati;

	@UiField
	InlineLabel cartellaPadreLabel;
	@UiField
	InlineLabel pathAlfrescoLabel;
	@UiField
	CheckBox checkMetadati;
	@UiField
	HTMLPanel ruoliBox, metadatiPanel, buttonPanel;
	@UiField
	ListBox listBoxRuoli;

	public interface Binder extends UiBinder<Widget, DriveFileView> {/**/}

	@Inject
	public DriveFileView(final Binder binder) {
		initWidget(binder.createAndBindUi(this));
	}

	public void clear() {
		if (message != null)
			message.clear();
		if (checkMetadati != null)
			checkMetadati.setValue(false);
		if (listBoxRuoli != null)
			listBoxRuoli.clear();
		if (metadatiPanel != null)
			metadatiPanel.clear();
		if (buttonPanel != null)
			buttonPanel.clear();
		if (uploadWidget != null)
			uploadWidget.clear();
	}

	@Inject
	private DriveHandler driveHandler;
	@Inject
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Override
	public void init(final Cartella cartellaPadre, final Command uploadCommand, final Command undoCommand) {
		clear();
		cartellaPadreLabel.setText("(" + cartellaPadre.getId() + ") " + cartellaPadre.getNome());
		pathAlfrescoLabel.setText(cartellaPadre.getPathAlfresco());

		if (cartellaPadre.getId().equals(DrivePresenter.ROOT.getId())) {
			ruoliBox.setVisible(true);
			for (AnagraficaRuolo ruolo : profilazioneUtenteHandler.findAnagraficheRuoli(GestioneDriveAbilitazione.class)) {
				listBoxRuoli.addItem(ruolo.getEtichetta(), ruolo.getRuolo());
			}
		} else {
			ruoliBox.setVisible(false);
		}

		metadati = new DriveMetadatiWidget(new Cartella(), driveHandler);
		metadati.ridisegna(true);
		metadatiPanel.setVisible(checkMetadati.getValue());
		metadatiPanel.add(metadati);

		buttonPanel.add(DrawingUtil.drawFunBtn("Salva", ImageSource.loadOk(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				uploadCommand.execute();
			}
		}));

		buttonPanel.add(DrawingUtil.drawFunBtn("Annulla", ImageSource.loadCancel(), new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				undoCommand.execute();
			}
		}));

		checkMetadati.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				metadatiPanel.setVisible(event.getValue());
			}
		});
	}

	@Override
	public String getRuolo() {
		return listBoxRuoli.getSelectedValue();
	}

	@Override
	public ModificheDizionario getModifiche() {
		if (checkMetadati.getValue()) {
			return metadati.getModifiche();
		}
		return null;
	}

}
