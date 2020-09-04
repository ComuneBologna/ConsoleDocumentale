package it.eng.portlet.consolepec.gwt.client.view.cartellafirma;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.presenter.cartellafirma.StepOperazioneWizardTaskFirmaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.cartellafirma.StepOperazioneWizardTaskFirmaPresenter.UploadAllegatoCommand;
import it.eng.portlet.consolepec.gwt.client.util.VisualizzazioneElementiProtocollati;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget.SelezioneFile;
import it.eng.portlet.consolepec.gwt.client.widget.GruppoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UploadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UploadAllegatoWidget.UploadAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.ProponenteDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoRispostaParereDTO;

/**
 *
 * @author biagiot
 *
 */
public class StepOperazioneWizardTaskFirmaView extends ViewImpl implements StepOperazioneWizardTaskFirmaPresenter.MyView {

	private final Widget widget;

	@UiField
	HTMLPanel proponentiAllegatiPanel;
	@UiField
	YesNoRadioButton riassegnaRadioButton;
	@UiField
	Button annullaButton;
	@UiField
	Button confermaButton;
	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;
	@UiField
	HeadingElement mainTitle;
	@UiField
	HTMLPanel tipoRispostaPanel;
	@UiField
	ListBox tipoRispostaListBox;
	@UiField
	HTMLPanel sceltaRuoloContainer;
	@UiField
	HTMLPanel sceltaRuoloPanel;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	@UiField
	UploadAllegatoWidget uploadWidget;
	@UiField
	Button caricaAllegatoButton;
	@UiField
	Button eliminaAllegatoButton;
	@UiField
	HTMLPanel elencoAllegatiPanel;
	@UiField
	TextArea motivazioneTextArea;

	private UploadAllegatoCommand uploadAllegatoCommand;
	private Set<FileDTO> allegatiSelezionati = new HashSet<FileDTO>();
	private HandlerRegistration handlerRegistration1;
	private HandlerRegistration handlerRegistration2;

	public interface Binder extends UiBinder<Widget, StepOperazioneWizardTaskFirmaView> {/**/}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Inject
	public StepOperazioneWizardTaskFirmaView(Binder binder, EventBus eventBus) {
		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		mainTitle.scrollIntoView();
	}

	@Override
	public boolean isRiassegna() {
		return riassegnaRadioButton.getValue();
	}

	@Override
	public void setAnnullaCommand(final Command command) {
		this.handlerRegistration1 = this.annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setConfermaCommand(final Command command) {

		this.handlerRegistration2 = this.confermaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void mostraPannelloTipoRisposta() {
		initTipoRispostaListBox();
		tipoRispostaPanel.setVisible(true);
	}

	private void initTipoRispostaListBox() {
		for (TipoRispostaParereDTO trp : TipoRispostaParereDTO.values()) {
			this.tipoRispostaListBox.addItem(trp.getLabel());
		}
	}

	@Override
	public TipoRispostaParereDTO getTipoRisposta() {
		return TipoRispostaParereDTO.fromLabel(tipoRispostaListBox.getItemText(tipoRispostaListBox.getSelectedIndex()));
	}

	@Override
	public void configuraSceltaRuolo(Set<String> ruoliAbiltiati, it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, String> selectCommand) {

		for (String ruolo : ruoliAbiltiati) {
			GruppoWidget widget = new GruppoWidget();
			widget.setSelectCommand(selectCommand);
			sceltaRuoloPanel.add(widget);
			widget.showWidgetWithRadioButton(ruolo);
		}

		sceltaRuoloContainer.setVisible(true);
	}

	@Override
	public void configuraPannelloProponentiGruppi(Map<ProponenteDTO, Set<AllegatoDTO>> map) {
		HTMLPanel panel = new HTMLPanel("");
		DivElement corpoDiv = Document.get().createDivElement();
		corpoDiv.setClassName("corpo");
		panel.getElement().appendChild(corpoDiv);
		DivElement boxMailDiv = Document.get().createDivElement();
		boxMailDiv.setClassName("box-mail");
		corpoDiv.appendChild(boxMailDiv);

		for (Entry<ProponenteDTO, Set<AllegatoDTO>> entry : map.entrySet()) {

			DivElement divDocumentiMailProponente = Document.get().createDivElement();
			divDocumentiMailProponente.setClassName("documenti-mail");

			DivElement divContainerProponente = Document.get().createDivElement();
			divContainerProponente.setClassName("documento-container");
			divDocumentiMailProponente.appendChild(divContainerProponente);

			SpanElement span = Document.get().createSpanElement();
			span.setClassName("documento-semplice");
			divContainerProponente.appendChild(span);

			Image icona = new Image(ConsolePECIcons._instance.gruppo());
			span.appendChild(icona.getElement());

			LabelElement label = Document.get().createLabelElement();
			label.setInnerText(" " + entry.getKey().getNomeGruppo());
			span.appendChild(label);

			for (AllegatoDTO allegato : entry.getValue()) {
				SpanElement spanDocumento = Document.get().createSpanElement();
				spanDocumento.setClassName("documento");

				Image imageDocumento = (allegato.isFirmato() || allegato.isFirmatoHash()) ? new Image(ConsolePECIcons._instance.firmato()) : new Image(ConsolePECIcons._instance.nonfirmato());
				spanDocumento.appendChild(imageDocumento.getElement());

				LabelElement labelDocumento = Document.get().createLabelElement();
				labelDocumento.setInnerText(" " + allegato.getNome());
				spanDocumento.appendChild(labelDocumento);
				divContainerProponente.appendChild(spanDocumento);
			}

			boxMailDiv.appendChild(divDocumentiMailProponente);
		}

		proponentiAllegatiPanel.add(panel);
	}

	@Override
	public void setTitolo(String titolo) {
		this.mainTitle.setInnerText(titolo);
	}

	@Override
	public void reset() {
		Window.scrollTo(0, 0);
		this.proponentiAllegatiPanel.clear();
		this.tipoRispostaPanel.setVisible(false);
		this.sceltaRuoloPanel.clear();
		this.sceltaRuoloContainer.setVisible(false);
		this.tipoRispostaListBox.clear();
		this.elencoAllegatiPanel.clear();
		this.riassegnaRadioButton.setReadOnly(false);
		cancellaListaAllegatiSelezionati();
		this.motivazioneTextArea.setValue("");
		if (this.handlerRegistration1 != null) {
			this.handlerRegistration1.removeHandler();
		}
		if (this.handlerRegistration2 != null) {
			this.handlerRegistration2.removeHandler();
		}
	}

	@Override
	public void impostaRiassegnazioneValore(boolean value) {
		if (value) {
			riassegnaRadioButton.selectYes();
		} else {
			riassegnaRadioButton.selectNo();
		}
	}

	@Override
	public void disabilitaRiassegnazione() {
		riassegnaRadioButton.selectNo();
		riassegnaRadioButton.setReadOnly(true);
	}

	@Override
	public void startUpload() {
		uploadWidget.startUpload();
	}

	@Override
	public void setUploadAllegatoCommand(UploadAllegatoCommand uploadAllegatoCommand) {
		this.uploadAllegatoCommand = uploadAllegatoCommand;

		caricaAllegatoButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				uploadWidget.sfoglia(UriMapping.generaUploadAllegatoServletContextPath());
			}
		});

		uploadWidget.setUploadAllegatoHandler(new UploadAllegatoHandler() {

			@Override
			public void onUploadDone(RispostaFileUploaderDTO dto) {
				StepOperazioneWizardTaskFirmaView.this.uploadAllegatoCommand.onFileUploaded(dto);
			}

			@Override
			public void onFileSelected(String fileName) {
				StepOperazioneWizardTaskFirmaView.this.uploadAllegatoCommand.onFileSelected(fileName);
			}

			@Override
			public boolean onSubmitUpload(Integer fileNumber, String[] fileNames, Long[] fileLength) {
				return true;
			}
		});
	}

	@Override
	public void setEliminaAllegatoCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Set<FileDTO>> command) {
		eliminaAllegatoButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.exe(allegatiSelezionati);
			}
		});
	}

	@Override
	public void mostraAllegati(List<FileDTO> allegati) {
		cancellaListaAllegatiSelezionati();
		elencoAllegatiPanel.clear();

		while (elencoAllegatiPanel.getElement().hasChildNodes()) {
			elencoAllegatiPanel.getElement().removeChild(elencoAllegatiPanel.getElement().getLastChild());
		}

		if (allegati.size() > 0) {
			HTMLPanel pannelloGruppo = VisualizzazioneElementiProtocollati.buildGruppoPanel("Allegati", elencoAllegatiPanel);
			elencoAllegatiPanel.setVisible(true);

			for (final FileDTO allegato : allegati) {
				ElementoAllegatoElencoWidget allgwidget = new ElementoAllegatoElencoWidget();

				if (allegatiSelezionati != null && allegatiSelezionati.contains(allegato)) {
					allgwidget.setCheckBoxVisible(true);
					eliminaAllegatoButton.setEnabled(true);
				} else {
					eliminaAllegatoButton.setEnabled(false);
				}

				allgwidget.setSelezionaFileCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, ElementoAllegatoElencoWidget.SelezioneFile>() {

					@Override
					public Void exe(SelezioneFile t) {
						if (t.isChecked()) {
							allegatiSelezionati.add(allegato);

						} else {
							allegatiSelezionati.remove(allegato);

						}
						if (allegatiSelezionati.size() == 0) {
							eliminaAllegatoButton.setEnabled(false);

						} else {
							eliminaAllegatoButton.setEnabled(true);

						}

						return null;
					}
				});

				pannelloGruppo.add(allgwidget);
				allgwidget.mostraDettaglio(allegato);
			}
		} else {
			elencoAllegatiPanel.setVisible(false);
		}
	}

	@Override
	public void cancellaListaAllegatiSelezionati() {
		allegatiSelezionati.clear();
		eliminaAllegatoButton.setEnabled(false);
	}

	@Override
	public Set<FileDTO> getAllegatiSelezionati() {
		return allegatiSelezionati;
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	@Override
	public void showErrors(List<String> errors) {
		if (errors.size() > 0) {
			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			sb.appendHtmlConstant("<ul>");
			for (String error : errors) {
				sb.appendHtmlConstant("<li>");
				sb.appendEscaped(error);
				sb.appendHtmlConstant("</li>");
			}
			sb.appendHtmlConstant("</ul>");
			HTML w = new HTML(sb.toSafeHtml());
			messaggioAlertWidget.showWarningMessage(w.getHTML());

		} else {
			messaggioAlertWidget.reset();
		}
	}

	@Override
	public String getMotivazione() {
		return this.motivazioneTextArea.getText() != null && !Strings.isNullOrEmpty(this.motivazioneTextArea.getText().trim()) ? this.motivazioneTextArea.getText() : null;
	}

	@Override
	public void setMotivazione(String motivazione) {
		this.motivazioneTextArea.setText(motivazione != null ? motivazione : "");
	}
}
