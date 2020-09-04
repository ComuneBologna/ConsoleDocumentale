package it.eng.portlet.consolepec.gwt.client.drive;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.GestioneDriveAbilitazione;
import it.eng.cobo.consolepec.commons.drive.Cartella;
import it.eng.cobo.consolepec.commons.drive.Nomenclatura;
import it.eng.portlet.consolepec.gwt.client.drive.util.DrawingUtil;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMessageWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMetadatiWidget;
import it.eng.portlet.consolepec.gwt.client.drive.widget.DriveMetadatiWidget.ModificheDizionario;
import it.eng.portlet.consolepec.gwt.client.handler.drive.DriveHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;

/**
 * @author Giacomo F.M.
 * @since 2019-06-24
 */
public class DriveCartellaView extends ViewImpl implements DriveCartellaPresenter.MyView {

	private DriveMetadatiWidget metadati;

	@UiField
	DriveMessageWidget message;

	@Override
	public DriveMessageWidget getMessageWidget() {
		return this.message;
	}

	@UiField
	InlineLabel cartellaPadreLabel;
	@UiField
	InlineLabel pathAlfrescoLabel;
	@UiField(provided = true)
	SuggestBox nomeCartella;
	@UiField(provided = true)
	SuggestBox nomenclatura;
	@UiField
	ListBox listBoxRuoli;
	@UiField
	CheckBox checkMetadati;
	@UiField
	HTMLPanel ruoliBox, metadatiPanel, buttonPanel;

	public interface Binder extends UiBinder<Widget, DriveCartellaView> {/**/}

	private DriveHandler driveHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public DriveCartellaView(final Binder binder, final DriveHandler driveHandler, final ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		this.driveHandler = driveHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		List<String> nomenclature = new ArrayList<>();
		for (Nomenclatura nom : driveHandler.getNomenclature()) {
			nomenclature.add(nom.getNome());
		}
		this.nomenclatura = new SuggestBox(new SpacebarSuggestOracle(nomenclature));
		this.nomeCartella = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));

		this.nomenclatura.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				nomeCartella.setValue(null);
				SpacebarSuggestOracle so = (SpacebarSuggestOracle) nomeCartella.getSuggestOracle();
				so.setSuggestions(new ArrayList<String>());

				Nomenclatura nom = driveHandler.getNomenclatura(event.getSelectedItem().getDisplayString());
				if (nom != null)
					so.setSuggestions(nom.getNomenclatura());
			}
		});

		this.nomenclatura.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				nomeCartella.setValue(null);
				SpacebarSuggestOracle so = (SpacebarSuggestOracle) nomeCartella.getSuggestOracle();
				so.setSuggestions(new ArrayList<String>());

				Nomenclatura nom = driveHandler.getNomenclatura(event.getValue());
				if (nom != null)
					so.setSuggestions(nom.getNomenclatura());
			}
		});

		initWidget(binder.createAndBindUi(this));
	}

	public void clear() {
		if (message != null)
			message.clear();
		if (nomeCartella != null)
			nomeCartella.setText("");
		if (checkMetadati != null)
			checkMetadati.setValue(false);
		if (listBoxRuoli != null)
			listBoxRuoli.clear();
		if (metadatiPanel != null)
			metadatiPanel.clear();
		if (buttonPanel != null)
			buttonPanel.clear();
	}

	@Override
	public void init(final Cartella cartellaPadre, final Command saveCommand, final Command undoCommand) {
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
				saveCommand.execute();
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
	public String getNomeCartella() {
		String text = nomeCartella.getText();
		if (text != null) {
			return nomeCartella.getText().trim();
		}
		return "";
	}

	@Override
	public Nomenclatura getNomenclatura() {
		return driveHandler.getNomenclatura(nomenclatura.getValue());
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
