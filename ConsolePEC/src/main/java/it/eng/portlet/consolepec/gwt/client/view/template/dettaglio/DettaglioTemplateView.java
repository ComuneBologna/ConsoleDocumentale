package it.eng.portlet.consolepec.gwt.client.view.template.dettaglio;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HRElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.presenter.template.dettaglio.DettaglioTemplatePresenter.MyView;
import it.eng.portlet.consolepec.gwt.client.presenter.template.dettaglio.DettaglioTemplatePresenter.UploadAllegatoCommand;
import it.eng.portlet.consolepec.gwt.client.util.VisualizzazioneElementiProtocollati;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget.SelezioneAllegato;
import it.eng.portlet.consolepec.gwt.client.widget.EventoIterFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UploadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UploadAllegatoWidget.UploadAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.widget.template.AbstractCorpoTemplateWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.BaseTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.CampoTemplateDTO;
import it.eng.portlet.consolepec.gwt.shared.model.EventoIterDTO;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;

/**
 *
 * @author biagiot
 *
 */
public class DettaglioTemplateView extends ViewImpl implements MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DettaglioTemplateView> {/**/}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@UiField
	HeadingElement titoloFascicolo;
	@UiField
	Element fieldsetPulsantiera;
	@UiField
	HTMLPanel corpoTemplatePanel;
	@UiField(provided = true)
	MessageAlertWidget messageWidget;
	@UiField(provided = true)
	DisclosurePanel iterFascicoloPanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Azioni");
	@UiField(provided = true)
	DisclosurePanel dettaglioTemplatePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Dettaglio modello");
	@UiField
	Button creaComunicazioneButton;
	@UiField
	Button chiudiButton;
	@UiField
	Button salvaButton;
	@UiField
	Button eliminaButton;
	@UiField
	Button caricaAllegatoButton;
	@UiField
	Button eliminaAllegatoButton;
	@UiField
	Button creaPerCopiaButton;
	@UiField
	HTMLPanel elencoAllegatiPanel;
	@UiField(provided = true)
	DisclosurePanel dettaglioAllegatiPanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Allegati");
	@UiField
	HRElement dettaglioAllegatiHR;
	@UiField(provided = true)
	EventoIterFascicoloWidget eventiIter;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	@UiField
	UploadAllegatoWidget uploadWidget;

	private AbstractCorpoTemplateWidget<?> corpoTemplateWidget;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> aggiungiCampoCommand;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> eliminaCampoCommand;
	private UploadAllegatoCommand uploadAllegatoCommand;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand;
	private Set<AllegatoDTO> allegatiSelezionati = new HashSet<AllegatoDTO>();

	@Inject
	public DettaglioTemplateView(final Binder binder, final EventBus eventBus) {
		eventiIter = new EventoIterFascicoloWidget();
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public void setCorpoTemplateWidget(AbstractCorpoTemplateWidget<?> corpoTemplateWidget) {
		this.corpoTemplateWidget = corpoTemplateWidget;
		this.corpoTemplateWidget.setAggiungiCampoCommand(aggiungiCampoCommand);
		this.corpoTemplateWidget.setEliminaCampoCommand(eliminaCampoCommand);
		this.corpoTemplatePanel.add(this.corpoTemplateWidget);
	}

	@Override
	public void mostraTitolo(boolean enabled) {
		titoloFascicolo.setInnerText(enabled ? ConsolePecConstants.TITOLO_DETTAGLIO_TEMPLATE : "");
	}

	@Override
	public void mostraPulsantiera(boolean enabled) {
		this.fieldsetPulsantiera.getStyle().setDisplay(enabled ? Display.BLOCK : Display.NONE);
	}

	@Override
	public void resetDisclosurePanels(boolean showActions) {
		if (showActions) {
			this.dettaglioTemplatePanel.setOpen(false);
			this.iterFascicoloPanel.setOpen(true);
		} else {
			this.dettaglioTemplatePanel.setOpen(true);
			this.iterFascicoloPanel.setOpen(false);
		}
	}

	@Override
	public void initWidgetEventiIter(List<EventoIterDTO> eventiIter) {
		this.eventiIter.initWidget(eventiIter);
	}

	@Override
	public void abilitaEliminaButton(boolean enabled) {
		this.eliminaButton.setEnabled(enabled);
	}

	@Override
	public void abilitaCreaComunicazioneButton(boolean enabled) {
		this.creaComunicazioneButton.setEnabled(enabled);
	}

	@Override
	public void abilitaCreaPerCopiaButton(boolean enabled) {
		this.creaPerCopiaButton.setEnabled(enabled);
	}

	@Override
	public void abilitaSalvaButton(boolean enabled) {
		this.salvaButton.setEnabled(enabled);
	}

	@Override
	public void abilitaCaricaAllegatiButton(boolean enabled) {
		this.caricaAllegatoButton.setEnabled(enabled);
	}

	@Override
	public void abilitaEliminaAllegatoButton(boolean enabled) {
		this.eliminaAllegatoButton.setEnabled(enabled);
	}

	@Override
	public void mostraAllegati(List<AllegatoDTO> allegati, String panelName) {

		elencoAllegatiPanel.clear();
		while (elencoAllegatiPanel.getElement().hasChildNodes()) {
			elencoAllegatiPanel.getElement().removeChild(elencoAllegatiPanel.getElement().getLastChild());
		}

		if (allegati.size() > 0) {

			HTMLPanel pannelloGruppo = VisualizzazioneElementiProtocollati.buildGruppoPanel(panelName, elencoAllegatiPanel);
			elencoAllegatiPanel.setVisible(true);

			for (final AllegatoDTO allegato : allegati) {
				ElementoAllegatoElencoWidget allgwidget = new ElementoAllegatoElencoWidget();

				if (allegatiSelezionati != null && allegatiSelezionati.contains(allegato)) {
					allgwidget.setCheckBoxVisible(true);
					eliminaAllegatoButton.setEnabled(true);
				} else {
					eliminaAllegatoButton.setEnabled(false);
				}

				allgwidget.setSelezionaAllegatoCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, ElementoAllegatoElencoWidget.SelezioneAllegato>() {

					@Override
					public Void exe(SelezioneAllegato selezionaAllegato) {
						if (selezionaAllegato.isChecked()) {
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

				allgwidget.setDownloadAllegatoCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO>() {
					@Override
					public Void exe(AllegatoDTO allegato) {
						downloadAllegatoCommand.exe(allegato);
						return null;
					}
				});
				allgwidget.setMostraDettaglioAllegatoCommand(new it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO>() {

					@Override
					public Void exe(AllegatoDTO allegato) {
						DettaglioTemplateView.this.mostraDettaglioAllegatoCommand.exe(allegato);
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
	public boolean controlloCampi(List<String> errori) {
		return corpoTemplateWidget.validateForm(errori);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BaseTemplateDTO> T getTemplate() {
		return (T) corpoTemplateWidget.getTemplate();
	}

	@Override
	public void addCampo(CampoTemplateDTO o) {
		corpoTemplateWidget.addCampo(o);
	}

	@Override
	public void removeCampo(CampoTemplateDTO o) {
		corpoTemplateWidget.removeCampo(o);
	}

	@Override
	public void setCorpoTemplateWidgetReadOnly(boolean b) {
		corpoTemplateWidget.setReadOnly(b);
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	@Override
	public void setChiudiDettaglioCommand(final Command chiudiDettaglioCommand) {
		this.chiudiButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				chiudiDettaglioCommand.execute();
			}
		});
	}

	@Override
	public void setSalvaTemplateCommand(final Command salvaTemplateCommand) {
		this.salvaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				salvaTemplateCommand.execute();
			}
		});
	}

	@Override
	public void setCreaTemplatePerCopiaCommand(final Command creaTemplatePerCopiacommand) {
		this.creaPerCopiaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				creaTemplatePerCopiacommand.execute();

			}
		});
	}

	@Override
	public void setEliminaTemplateCommand(final Command eliminaTemplateCommand) {
		this.eliminaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				eliminaTemplateCommand.execute();
			}
		});
	}

	@Override
	public void setEliminaCampoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> eliminaCampoCommand) {
		this.eliminaCampoCommand = eliminaCampoCommand;
	}

	@Override
	public void setAggiungiCampoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, CampoTemplateDTO> aggiungiCampoCommand) {
		this.aggiungiCampoCommand = aggiungiCampoCommand;

	}

	@Override
	public void setCreaComunicazioneCommand(final Command creaComunicazioneCommand) {

		this.creaComunicazioneButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				creaComunicazioneCommand.execute();
			}
		});
	}

	@Override
	public void startUpload() {
		uploadWidget.startUpload();
	}

	@Override
	public void setDownloadAllegatoButtonName(String name) {
		this.caricaAllegatoButton.setText(name);
	}

	@Override
	public void showEliminaAllegatoButton(boolean show) {
		this.eliminaAllegatoButton.setVisible(show);
	}

	@Override
	public void showCreaComunicazioneButton(boolean show) {
		this.creaComunicazioneButton.setVisible(show);
	}

	@Override
	public void cancellaListaAllegatiSelezionati() {
		allegatiSelezionati.clear();
		eliminaAllegatoButton.setEnabled(false);
	}

	@Override
	public Set<AllegatoDTO> getAllegatiSelezionati() {
		return allegatiSelezionati;
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
				DettaglioTemplateView.this.uploadAllegatoCommand.onFileUploaded(dto);
			}

			@Override
			public void onFileSelected(String fileName) {
				DettaglioTemplateView.this.uploadAllegatoCommand.onFileSelected(fileName);
			}

			@Override
			public boolean onSubmitUpload(Integer fileNumber, String[] fileNames, Long[] fileLength) {
				return true;
			}

		});
	}

	@Override
	public void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.downloadAllegatoCommand = downloadAllegatoCommand;

	}

	@Override
	public void setCancellaAllegatoCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> command) {
		eliminaAllegatoButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.exe(allegatiSelezionati);
			}
		});
	}

	@Override
	public void setMostraDettaglioAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand) {
		this.mostraDettaglioAllegatoCommand = mostraDettaglioAllegatoCommand;
	}

	@Override
	public void clearCorpoTemplatePanel() {
		this.corpoTemplatePanel.clear();
	}
}
