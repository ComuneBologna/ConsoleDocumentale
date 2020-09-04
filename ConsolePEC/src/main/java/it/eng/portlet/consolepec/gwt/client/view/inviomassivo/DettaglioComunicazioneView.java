package it.eng.portlet.consolepec.gwt.client.view.inviomassivo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HRElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ConsoleDisclosurePanel;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB.PraticaFascicoloLoaded;
import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.DettaglioComunicazionePresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.DettaglioComunicazionePresenter.GoToFascicoloCollegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.DettaglioComunicazionePresenter.UploadAllegatoCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.command.InviaCsvCommand;
import it.eng.portlet.consolepec.gwt.client.util.VisualizzazioneElementiProtocollati;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget.SelezioneAllegato;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoFascicoloCollegato;
import it.eng.portlet.consolepec.gwt.client.widget.EventoIterFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UploadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UploadAllegatoWidget.UploadAllegatoHandler;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.dto.CollegamentoDto;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;

public class DettaglioComunicazioneView extends ViewImpl implements DettaglioComunicazionePresenter.MyView {

	private final Widget widget;

	@UiField
	HeadingElement titoloComunicazione;
	@UiField
	Element fildsetPulsantiera;
	@UiField
	Label idDocumentale;
	@UiField
	Label codice;
	@UiField
	Label idDocumentaleTemplate;
	@UiField
	Label dataInserimento;
	@UiField
	Label stato;
	@UiField
	HTMLPanel descrizionePanel;
	@UiField
	Button chiudiButton;
	@UiField
	Button creaComunicazionePerCopiaButton;

	@UiField(provided = true)
	DisclosurePanel iterFascicoloPanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Azioni");
	@UiField(provided = true)
	DisclosurePanel dettaglioComunicazionePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Dettaglio comunicazione");
	@UiField(provided = true)
	ConsoleDisclosurePanel fascicoliCollegatiPanel = new ConsoleDisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Fascicoli collegati");

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
	@UiField
	Button inviaCsvButton;
	@UiField
	Button inviaCsvTestButton;
	@UiField
	Button caricaAllegatoButton;
	@UiField
	Button eliminaAllegatoButton;

	@UiField
	HTMLPanel elencoFascicoliCollegatiPanel;

	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	private Set<AllegatoDTO> allegatiSelezionati = new HashSet<AllegatoDTO>();

	private UploadAllegatoCommand uploadAllegatoCommand;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand;

	private GoToFascicoloCollegatoCommand goToFascicoloCollegatoCommand;

	private PecInPraticheDB praticheDb;
	private final EventBus eventBus;

	public interface Binder extends UiBinder<Widget, DettaglioComunicazioneView> {/**/}

	@Inject
	public DettaglioComunicazioneView(final Binder binder, final PecInPraticheDB praticheDb, final EventBus eventBus) {
		this.eventiIter = new EventoIterFascicoloWidget();
		this.praticheDb = praticheDb;
		this.eventBus = eventBus;
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void mostraPratica(ComunicazioneDTO comunicazioneDTO) {

		cancellaListaAllegatiSelezionati();

		titoloComunicazione.setInnerText("Comuncazione " + comunicazioneDTO.getCodice());

		idDocumentale.setText(GenericsUtil.sanitizeNull(comunicazioneDTO.getNumeroRepertorio(), "-"));
		codice.setText(GenericsUtil.sanitizeNull(comunicazioneDTO.getCodice(), "-"));
		idDocumentaleTemplate.setText(GenericsUtil.sanitizeNull(comunicazioneDTO.getIdDocumentaleTemplate(), "-"));
		dataInserimento.setText(GenericsUtil.sanitizeNull(comunicazioneDTO.getDataOraCreazione(), "-"));
		stato.setText(GenericsUtil.sanitizeNull(comunicazioneDTO.getStatoLabel(), "-"));

		String oggetto = GenericsUtil.sanitizeNull(comunicazioneDTO.getDescrizione(), "-");
		SafeHtml oggettoHtml = SafeHtmlUtils.fromString(oggetto);
		HTML hOggetto = new HTML(oggettoHtml);

		descrizionePanel.clear();
		descrizionePanel.add(hOggetto);

		mostraAllegati(comunicazioneDTO);
		mostraCollegamenti(comunicazioneDTO);

		impostaAbilitazioniPulsantiera(comunicazioneDTO);

		eventiIter.initWidget(comunicazioneDTO.getEventiIterDTO());
	}

	@Override
	public void setChiudiDettaglioCommand(final com.google.gwt.user.client.Command chiudiDettaglioCommand) {
		chiudiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				removeFromSlot(MainPresenter.TYPE_SetMainContent, widget);
				chiudiDettaglioCommand.execute();
			}
		});

	}

	private void impostaAbilitazioniPulsantiera(ComunicazioneDTO comunicazione) {
		caricaAllegatoButton.setEnabled(comunicazione.isCaricaAllegatoAbilitato());
		creaComunicazionePerCopiaButton.setEnabled(comunicazione.isCreaComunicazionePerCopiaAbilitato());
	}

	private void mostraAllegati(ComunicazioneDTO comunicazione) {
		List<AllegatoDTO> allegati = comunicazione.getAllegati();

		elencoAllegatiPanel.clear();
		while (elencoAllegatiPanel.getElement().hasChildNodes()) {
			elencoAllegatiPanel.getElement().removeChild(elencoAllegatiPanel.getElement().getLastChild());
		}

		inviaCsvButton.setEnabled(false);
		inviaCsvTestButton.setEnabled(false);
		eliminaAllegatoButton.setEnabled(false);

		if (allegati.size() > 0) {

			HTMLPanel pannelloGruppo = VisualizzazioneElementiProtocollati.buildGruppoPanel("Allegati", elencoAllegatiPanel);
			elencoAllegatiPanel.setVisible(true);
			// dettaglioAllegatiPanel.setVisible(true);
			// dettaglioAllegatiHR.getStyle().setDisplay(Display.BLOCK);
			for (final AllegatoDTO allegato : allegati) {
				ElementoAllegatoElencoWidget allgwidget = new ElementoAllegatoElencoWidget();

				/*
				 * if (allegatiSelezionati != null && allegatiSelezionati.contains(allegato)) { allgwidget.setCheckBoxVisible(true); eliminaAllegatoButton.setEnabled(true); } else {
				 * eliminaAllegatoButton.setEnabled(false); }
				 */
				if (comunicazione.isEliminaAllegatoAbilitato()) {
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

							if (controllaAllegatiInvioComunicazioneAbilitato()) {
								inviaCsvButton.setEnabled(true);
								inviaCsvTestButton.setEnabled(true);
							} else {
								inviaCsvButton.setEnabled(false);
								inviaCsvTestButton.setEnabled(false);
							}

							return null;
						}
					});
				}

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
						DettaglioComunicazioneView.this.mostraDettaglioAllegatoCommand.exe(allegato);
						return null;
					}
				});

				pannelloGruppo.add(allgwidget);
				allgwidget.mostraDettaglio(allegato);

			}

		} else {
			elencoAllegatiPanel.setVisible(false);
			// dettaglioAllegatiPanel.setVisible(false);
			// dettaglioAllegatiHR.getStyle().setDisplay(Display.NONE);
		}
	}

	private boolean controllaAllegatiInvioComunicazioneAbilitato() {
		if (allegatiSelezionati.size() != 1) { // solo un csv selezionato
			return false;
		}
		for (AllegatoDTO selezionato : allegatiSelezionati) {
			if (!selezionato.isInvioComunicazioneAbilitato())
				return false;
		}
		return true;
	}

	@Override
	public void startUpload() {
		uploadWidget.startUpload();
	}

	@Override
	public void setUploadAllegatoCommand(final UploadAllegatoCommand uploadAllegatoCommand) {
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
				DettaglioComunicazioneView.this.uploadAllegatoCommand.onFileUploaded(dto);
			}

			@Override
			public void onFileSelected(String fileName) {
				DettaglioComunicazioneView.this.uploadAllegatoCommand.onFileSelected(fileName);
			}

			@Override
			public boolean onSubmitUpload(Integer fileNumber, String[] fileNames, Long[] fileLength) {
				return true;
			}
		});
	}

	@Override
	public void setMostraDettaglioAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand) {
		this.mostraDettaglioAllegatoCommand = mostraDettaglioAllegatoCommand;
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	@Override
	public void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.downloadAllegatoCommand = downloadAllegatoCommand;
	}

	private void cancellaListaAllegatiSelezionati() {
		allegatiSelezionati.clear();
		eliminaAllegatoButton.setEnabled(false);
	}

	@Override
	public void mostraPulsantiera(boolean mostra) {
		fildsetPulsantiera.getStyle().setDisplay(mostra ? Display.BLOCK : Display.NONE);
	}

	@Override
	public void mostraTitolo(boolean mostra) {
		titoloComunicazione.setInnerText(mostra ? "Dettaglio Comunicazione" : "");
	}

	@Override
	public void clear() {

	}

	@Override
	public void resetDisclosurePanels(boolean showActions) {
		if (showActions) {
			this.dettaglioComunicazionePanel.setOpen(false);
			this.iterFascicoloPanel.setOpen(true);
			this.fascicoliCollegatiPanel.setOpen(false);

		} else {
			this.dettaglioComunicazionePanel.setOpen(true);
			this.iterFascicoloPanel.setOpen(false);
			this.fascicoliCollegatiPanel.setOpen(false);

		}
	}

	@Override
	public void setCancellaAllegatoCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> cancellaAllegatoCommand) {
		eliminaAllegatoButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancellaAllegatoCommand.exe(allegatiSelezionati);
			}
		});

	}

	@Override
	public void setInviaCsvTestCommand(final Command<Object, AllegatoDTO> inviaCsvTestCommand) {
		inviaCsvTestButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// prendo sempre il primo(eseguo i controlli in fase di validazione)
				inviaCsvTestCommand.exe(allegatiSelezionati.iterator().next());
			}
		});

	}

	@Override
	public void setInviaCsvCommand(final InviaCsvCommand inviaCsvCommand) {
		inviaCsvButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				// prendo sempre il primo(eseguo i controlli in fase di validazione)
				inviaCsvCommand.setAllegato(allegatiSelezionati.iterator().next());
				inviaCsvCommand.execute();
			}
		});

	}

	@Override
	public void setCreaComunicazionePerCopiaCommand(final com.google.gwt.user.client.Command creaComunicazionePerCopiaCommand) {
		creaComunicazionePerCopiaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				creaComunicazionePerCopiaCommand.execute();
			}
		});

	}

	private void mostraCollegamenti(ComunicazioneDTO comunicazione) {

		elencoFascicoliCollegatiPanel.getElement().setInnerHTML(""); // reset

		elencoFascicoliCollegatiPanel.setStyleName("single");

		UListElement ulNonProt = UListElement.as(DOM.createElement("ul"));
		ulNonProt.setClassName("contenitore-lista-gruppi");
		elencoFascicoliCollegatiPanel.getElement().appendChild(ulNonProt);

		LIElement curLi = LIElement.as(DOM.createElement("li"));
		curLi.setClassName("gruppo clearfix");

		ulNonProt.appendChild(curLi);

		SpanElement pgSpan = SpanElement.as(DOM.createSpan());
		pgSpan.setClassName("label nessun-protocollo");
		pgSpan.setInnerHTML("Fascicoli");
		curLi.appendChild(pgSpan);
		/* div corpo */
		HTMLPanel corpoDIV = new HTMLPanel("");
		corpoDIV.setStylePrimaryName("corpo");
		elencoFascicoliCollegatiPanel.add(corpoDIV, curLi);

		final HTMLPanel boxDIV = new HTMLPanel("");
		boxDIV.setStyleName("box-mail last");
		corpoDIV.add(boxDIV);

		for (final CollegamentoDto collegamento : comunicazione.getCollegamenti()) {

			praticheDb.getFascicoloByPath(collegamento.getClientId(), false, new PraticaFascicoloLoaded() {

				@Override
				public void onPraticaLoaded(FascicoloDTO pec) {
					CollegamentoDto tmp = new CollegamentoDto();
					tmp.setAnnoPg(pec.getAnnoPG());
					tmp.setClientId(collegamento.getClientId());
					tmp.setNumeroPg(pec.getNumeroPG());
					tmp.setOggetto(pec.getTitolo());

					ElementoFascicoloCollegato efc = new ElementoFascicoloCollegato(tmp, goToFascicoloCollegatoCommand, null, new ClickHandler() {

						@Override
						public void onClick(ClickEvent event) {}
					});

					efc.setCheckBoxVisibility(false);

					boxDIV.add(efc);
				}

				@Override
				public void onPraticaError(String error) {
					ShowMessageEvent event = new ShowMessageEvent();
					event.setErrorMessage(ConsolePecConstants.ERROR_MESSAGE);
					eventBus.fireEvent(event);

				}
			});
		}

	}

	@Override
	public void setGoToFascicoloCollegatoCommand(GoToFascicoloCollegatoCommand goToFascicoloCollegatoCommand) {
		this.goToFascicoloCollegatoCommand = goToFascicoloCollegatoCommand;
	}

}
