package it.eng.portlet.consolepec.gwt.client.view.pecout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.db.PecInPraticheDB;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.DettaglioPecOutBozzaPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.DettaglioPecOutBozzaPresenter.UploadAllegatoCommand;
import it.eng.portlet.consolepec.gwt.client.util.VisualizzazioneElementiProtocollati;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoAllegatoElencoWidget.SelezioneAllegato;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UploadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UploadAllegatoWidget.UploadAllegatoHandler;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.InputListWidget;
import it.eng.portlet.consolepec.gwt.shared.TinyMCEUtils;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoAllegato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoElenco;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollato;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.ElementoGruppoProtocollatoCapofila;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO.StatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PecOutDTO.TipologiaInteroperabileDTO;
import it.eng.portlet.consolepec.gwt.shared.model.RispostaFileUploaderDTO;
import it.eng.portlet.consolepec.gwt.shared.model.TipoEmail;

public class DettaglioPecOutBozzaView extends ViewImpl implements DettaglioPecOutBozzaPresenter.MyView {

	private final Widget widget;

	@UiField
	Button eliminaAllegatoButton;
	@UiField
	Button firmaAllegatoButton;
	@UiField
	Button eliminaButton;
	@UiField
	Button inviaButton;
	@UiField
	Button chiudiButton;
	@UiField
	Button caricaAllegatoButton;
	@UiField
	Button caricaAllegatoDaPraticaButton;
	@UiField
	TextBox oggettoTextBox;
	@UiField
	ListBox mittenteListBox;
	@UiField
	HTMLPanel destinatarioPanel;
	@UiField
	HTMLPanel inCopiaPanel;
	@UiField
	HTMLPanel elencoAllegatiPanel;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	@UiField
	UploadAllegatoWidget uploadWidget;
	@UiField
	Label idMail;
	@UiField
	Label idDocumentale;
	@UiField(provided = true)
	DisclosurePanel dettaglioMailPanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Dettaglio Email");
	@UiField(provided = true)
	DisclosurePanel firmaPanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Firma");
	@UiField(provided = true)
	DisclosurePanel dettaglioAllegatiPanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Allegati");
	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;
	@UiField
	CheckBox salvaFirmaCheckbox;

	// campi interoperabili
	@UiField
	HTMLPanel documentoPrincipaleInteroperabilePanel;
	@UiField
	ListBox documentoPrincipaleInteroperabileListBox;

	private InputListWidget inputListWidgetDestinatari;
	private InputListWidget inputListWidgetInCopia;
	private PecOutDTO pecOutBozzaDTO = null; // new PecOutDTO();
	private Set<AllegatoDTO> allegatiSelezionati = new HashSet<AllegatoDTO>();
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> firmaAllegatoCommand;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> cancellaAllegatoCommand;
	private UploadAllegatoCommand uploadAllegatoCommand;
	private SuggestOracle suggestOracle;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, AllegatoDTO> mostraDettaglioAllegatoCommand;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand;
	private Command mostraCaricaAllegatiDaPraticaCommand;
	private PecOutDTO pec = null;

	public static final String DOM_FIRMA_ID = "firma";
	public static final String DOM_BODY_ID = "corpoMessaggio";
	private final static String MAIL_PRINCIPALE = "Mail";

	public interface Binder extends UiBinder<Widget, DettaglioPecOutBozzaView> {/**/}

	@Inject
	public DettaglioPecOutBozzaView(final Binder binder, final DispatchAsync dispatcher, final PecInPraticheDB pecInDb, final EventBus eventBus) {

		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);

		Widget dettaglioAllegatiWidget = dettaglioAllegatiPanel.getHeader();
		dettaglioAllegatiWidget.setStyleName("header");
		dettaglioAllegatiWidget = dettaglioMailPanel.getHeader();
		dettaglioAllegatiWidget.setStyleName("header");
		dettaglioAllegatiWidget = firmaPanel.getHeader();
		dettaglioAllegatiWidget.setStyleName("header");
	}

	@Override
	public PecOutDTO getPecOutDTO() {
		return pec;
	}

	@Override
	public void mostraBozza(PecOutDTO pec, FascicoloDTO fascicolo) {
		/* se cambia la pratica, reset dei disclosure */
		this.idDocumentale.setText(sanitizeNull(pec.getNumeroRepertorio()));
		this.idMail.setText(sanitizeNull(pec.getMailId()));

		this.setSuggestDestinatari(this.suggestOracle);
		if (pecOutBozzaDTO == null || pecOutBozzaDTO.getClientID() != pec.getClientID()) {
			dettaglioAllegatiPanel.setOpen(false);
			firmaPanel.setOpen(false);
			dettaglioMailPanel.setOpen(true);
		}

		pecOutBozzaDTO = pec;
		oggettoTextBox.setText(pecOutBozzaDTO.getTitolo());
		oggettoTextBox.setEnabled(true);
		oggettoTextBox.removeStyleName("disabilitato");

		this.pec = pec;

		if (pec.getMittente() != null) {
			for (int i = 0; i < mittenteListBox.getItemCount(); i++) {
				String m = mittenteListBox.getValue(i);
				if (pec.getMittente().equals(m)) {
					mittenteListBox.setSelectedIndex(i);
				}
			}
		}

		mittenteListBox.setEnabled(true);
		mittenteListBox.removeStyleName("disabilitato");

		aggiornaTabellaAllegati(pec, fascicolo, true);

		for (Destinatario cc : pecOutBozzaDTO.getDestinatariCC()) {
			inputListWidgetInCopia.addValueItem(cc.toString());
		}
		for (Destinatario des : pecOutBozzaDTO.getDestinatari()) {
			inputListWidgetDestinatari.addValueItem(des.toString());
		}

		if (pecOutBozzaDTO.isInteroperabile()) {
			documentoPrincipaleInteroperabilePanel.setVisible(true);

		} else {
			documentoPrincipaleInteroperabilePanel.setVisible(false);
		}

		disabilitaGUI(pec, fascicolo);
	}

	@Override
	public void mostraReinoltro(PecOutDTO pec, FascicoloDTO fascicolo) {
		/* se cambia la pratica, reset dei disclosure */

		this.idDocumentale.setText(sanitizeNull(null));
		this.idMail.setText(sanitizeNull(null));

		this.setSuggestDestinatari(this.suggestOracle);
		if (pecOutBozzaDTO == null || pecOutBozzaDTO.getClientID() != pec.getClientID()) {
			dettaglioAllegatiPanel.setOpen(false);
			firmaPanel.setOpen(false);
			dettaglioMailPanel.setOpen(true);

		}

		pecOutBozzaDTO = pec;
		oggettoTextBox.setText(pecOutBozzaDTO.getTitolo());
		aggiornaTabellaAllegati(pec, fascicolo, false);

		firmaAllegatoButton.setEnabled(false);
		eliminaAllegatoButton.setEnabled(false);

		this.caricaAllegatoButton.setEnabled(false);
		this.caricaAllegatoDaPraticaButton.setEnabled(false);
		this.eliminaButton.setEnabled(false);

		this.mittenteListBox.setEnabled(false);
		this.oggettoTextBox.setEnabled(false);
		this.inputListWidgetInCopia.setAbilitato(false);
		this.inputListWidgetDestinatari.setAbilitato(false);

		this.oggettoTextBox.setStyleName("disabilitato");
		this.mittenteListBox.setStyleName("disabilitato");

		if (pec.getMittente() != null) {
			for (int i = 0; i < mittenteListBox.getItemCount(); i++) {
				String m = mittenteListBox.getValue(i);
				if (pec.getMittente().equals(m)) {
					mittenteListBox.setSelectedIndex(i);
				}
			}
		}

		this.inviaButton.setEnabled(pecOutBozzaDTO.isInviaAbilitato());

		for (Destinatario cc : pecOutBozzaDTO.getDestinatariCC()) {
			inputListWidgetInCopia.addValueItem(cc.toString());
		}
		for (Destinatario des : pecOutBozzaDTO.getDestinatari()) {
			inputListWidgetDestinatari.addValueItem(des.toString());
		}

		this.inviaButton.setEnabled(true);
	}

	private static String sanitizeNull(String input) {
		if (input == null || input.trim().isEmpty()) {
			return "-";
		}

		return input;
	}

	@Override
	public void aggiornaTabellaAllegati(PecOutDTO bozzaPecOut, FascicoloDTO fascicoloCollegato, boolean checkBoxEnabled) {
		List<AllegatoDTO> allegati = bozzaPecOut.getAllegati();
		this.elencoAllegatiPanel.clear();
		this.documentoPrincipaleInteroperabileListBox.clear();
		this.documentoPrincipaleInteroperabileListBox.addItem(MAIL_PRINCIPALE, MAIL_PRINCIPALE);

		while (this.elencoAllegatiPanel.getElement().hasChildNodes()) {
			this.elencoAllegatiPanel.getElement().removeChild(this.elencoAllegatiPanel.getElement().getLastChild());
		}

		if (allegati.size() > 0) {

			Map<ElementoGruppoProtocollato, List<AllegatoDTO>> allegatiProtocollati = new LinkedHashMap<FascicoloDTO.ElementoGruppoProtocollato, List<AllegatoDTO>>();
			List<AllegatoDTO> allegatiNonProtocollati = new ArrayList<AllegatoDTO>();

			for (final AllegatoDTO allegato : allegati) {
				ElementoGruppoProtocollato elementoProtocollato = getProtocollazione(allegato, fascicoloCollegato);

				if (elementoProtocollato != null) {
					if (allegatiProtocollati.containsKey(elementoProtocollato)) {
						allegatiProtocollati.get(elementoProtocollato).add(allegato);
					} else {
						List<AllegatoDTO> alls = new ArrayList<AllegatoDTO>();
						alls.add(allegato);
						allegatiProtocollati.put(elementoProtocollato, alls);
					}
				} else {
					allegatiNonProtocollati.add(allegato);
				}

			}

			dettaglioAllegatiPanel.setVisible(true);

			if (allegatiNonProtocollati.size() > 0) {
				HTMLPanel pannelloGruppo = VisualizzazioneElementiProtocollati.buildGruppoPanel("Non Protocollati", elencoAllegatiPanel);
				elencoAllegatiPanel.setVisible(true);

				for (final AllegatoDTO allegato : allegatiNonProtocollati) {

					ElementoAllegatoElencoWidget allgwidget = getAllegatoWidget(allegato);

					pannelloGruppo.add(allgwidget);
					allgwidget.mostraDettaglio(allegato);

					if (!checkBoxEnabled) {
						allgwidget.setCheckBoxEnabled(false);
						allgwidget.setCheckBoxVisible(false);
						firmaAllegatoButton.setEnabled(false);
						eliminaAllegatoButton.setEnabled(false);
					}
				}

			}
			if (allegatiProtocollati.size() > 0) {
				elencoAllegatiPanel.setVisible(true);

				for (Entry<ElementoGruppoProtocollato, List<AllegatoDTO>> entry : allegatiProtocollati.entrySet()) {
					ElementoGruppoProtocollato egp = entry.getKey();
					List<AllegatoDTO> ap = entry.getValue();

					HTMLPanel pannelloGruppo = VisualizzazioneElementiProtocollati.buildGruppoPanel(egp.getNumeroPG() + "/" + egp.getAnnoPG(), elencoAllegatiPanel);

					for (final AllegatoDTO allegato : ap) {

						ElementoAllegatoElencoWidget allgwidget = getAllegatoWidget(allegato);

						pannelloGruppo.add(allgwidget);
						allgwidget.mostraDettaglio(allegato);

						if (!checkBoxEnabled) {
							allgwidget.setCheckBoxEnabled(false);
							allgwidget.setCheckBoxVisible(false);
							firmaAllegatoButton.setEnabled(false);
							eliminaAllegatoButton.setEnabled(false);
						}

					}
				}
			}

			// interoperabilità
			if (pecOutBozzaDTO.isInteroperabile()) {
				popolaAllegatoPrincipaleInteroperabile(pecOutBozzaDTO, fascicoloCollegato);
			}
		} else {
			elencoAllegatiPanel.setVisible(false);
			dettaglioAllegatiPanel.setVisible(false);
		}
	}

	private ElementoAllegatoElencoWidget getAllegatoWidget(final AllegatoDTO allegato) {
		ElementoAllegatoElencoWidget allgwidget = new ElementoAllegatoElencoWidget();

		if (allegatiSelezionati != null && allegatiSelezionati.contains(allegato)) {
			allgwidget.setCheckBoxVisible(true);
			firmaAllegatoButton.setEnabled(true);
			eliminaAllegatoButton.setEnabled(true);
		} else {
			firmaAllegatoButton.setEnabled(false);
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
					firmaAllegatoButton.setEnabled(false);
					eliminaAllegatoButton.setEnabled(false);
				} else {
					firmaAllegatoButton.setEnabled(true);
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
				DettaglioPecOutBozzaView.this.mostraDettaglioAllegatoCommand.exe(allegato);
				return null;
			}
		});

		return allgwidget;
	}

	private static ElementoGruppoProtocollato getProtocollazione(AllegatoDTO allegato, FascicoloDTO fascicoloCollegato) {
		for (ElementoElenco elementoElenco : fascicoloCollegato.getElenco()) {

			if (elementoElenco instanceof ElementoGruppoProtocollatoCapofila) {
				ElementoGruppoProtocollatoCapofila capofila = (ElementoGruppoProtocollatoCapofila) elementoElenco;

				for (ElementoElenco elementoDelCapofila : capofila.getElementi()) {

					if (elementoDelCapofila instanceof ElementoAllegato) {
						ElementoAllegato ea = (ElementoAllegato) elementoDelCapofila;

						String nomeInEmail = fascicoloCollegato.getNumeroRepertorio().toLowerCase() + "_" + ea.getNome();
						String nomeRiversamentoCartaceo = fascicoloCollegato.getNumeroRepertorio() + "(" + capofila.getNumeroPG() + "," + capofila.getAnnoPG() + ")" + "RiversamentoCartaceo.pdf";

						if (allegato.getNome().equals(nomeInEmail) || allegato.getNome().equals(nomeRiversamentoCartaceo)) {
							return capofila;
						}
					}

					if (elementoDelCapofila instanceof ElementoGruppoProtocollato) {
						ElementoGruppoProtocollato nonCapofila = (ElementoGruppoProtocollato) elementoDelCapofila;
						// controllo gli elementi della protocollazione collegata
						for (ElementoElenco elementoDelNonCapofila : nonCapofila.getElementi()) {

							if (elementoDelNonCapofila instanceof ElementoAllegato) {
								ElementoAllegato ea = (ElementoAllegato) elementoDelNonCapofila;

								String nomeInEmail = fascicoloCollegato.getNumeroRepertorio().toLowerCase() + "_" + ea.getNome();
								String nomeRiversamentoCartaceo = fascicoloCollegato.getNumeroRepertorio() + "(" + capofila.getNumeroPG() + "," + capofila.getAnnoPG() + ")"
										+ "RiversamentoCartaceo.pdf";

								if (allegato.getNome().equals(nomeInEmail) || allegato.getNome().equals(nomeRiversamentoCartaceo)) {

									return nonCapofila;
								}
							}
						}
					}
				}

			}
		}
		return null;
	}

	private void popolaAllegatoPrincipaleInteroperabile(PecOutDTO pecBozza, FascicoloDTO fascicoloCollegato) {
		for (ElementoElenco elementoElenco : fascicoloCollegato.getElenco()) {

			if (elementoElenco instanceof ElementoGruppoProtocollatoCapofila) {
				ElementoGruppoProtocollatoCapofila capofila = (ElementoGruppoProtocollatoCapofila) elementoElenco;

				for (ElementoElenco elementoDelCapofila : capofila.getElementi()) {

					if (elementoDelCapofila instanceof ElementoAllegato) {
						ElementoAllegato ea = (ElementoAllegato) elementoDelCapofila;
						popoloaElementoAllegato(ea, pecBozza);
					}

					if (elementoDelCapofila instanceof ElementoGruppoProtocollato) {
						ElementoGruppoProtocollato nonCapofila = (ElementoGruppoProtocollato) elementoDelCapofila;
						// controllo gli elementi della protocollazione collegata
						for (ElementoElenco elementoDelNonCapofila : nonCapofila.getElementi()) {

							if (elementoDelNonCapofila instanceof ElementoAllegato) {
								ElementoAllegato ea = (ElementoAllegato) elementoDelNonCapofila;
								popoloaElementoAllegato(ea, pecBozza);
							}
						}
					}
				}

			}
		}

	}

	private void popoloaElementoAllegato(ElementoAllegato ea, PecOutDTO pecBozza) {
		String nome = ea.getNome();
		for (AllegatoDTO allegato : pecBozza.getAllegati()) {
			if (allegato.getNome().endsWith(nome)) {
				this.documentoPrincipaleInteroperabileListBox.addItem(allegato.getNome(), allegato.getNome());

				if (allegato.getNome().equals(pecBozza.getIdentificativoAllegatoPrincipale())) {
					// l'elemento selezionato è quello all'indice corrente
					this.documentoPrincipaleInteroperabileListBox.setSelectedIndex(this.documentoPrincipaleInteroperabileListBox.getItemCount() - 1);
				}
			}

		}
	}

	@Override
	public void init() {
		caricaAllegatoDaPraticaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				mostraCaricaAllegatiDaPraticaCommand.execute();
			}
		});
		eliminaAllegatoButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cancellaAllegatoCommand.exe(allegatiSelezionati);
			}
		});
		firmaAllegatoButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				firmaAllegatoCommand.exe(allegatiSelezionati);
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public Button getChiudiButton() {
		return this.chiudiButton;
	}

	@Override
	public List<String> getDestinatari() {
		return this.inputListWidgetDestinatari.getItemSelected();

	}

	@Override
	public List<String> getDestinatariCC() {
		return this.inputListWidgetInCopia.getItemSelected();

	}

	@Override
	public String getOggetto() {

		return this.oggettoTextBox.getValue();
	}

	@Override
	public String getBodyEmail() {
		return TinyMCEUtils.getContent(DOM_BODY_ID);
	}

	@Override
	public String getFirmaEmail() {
		return TinyMCEUtils.getContent(DOM_FIRMA_ID);
	}

	@Override
	public String getMittenteEmail() {

		int index = this.mittenteListBox.getSelectedIndex();
		String mittente = "";
		if (index >= 0) {
			mittente = this.mittenteListBox.getValue(index);
		}
		return mittente;
	}

	@Override
	public void setSuggestDestinatari(SuggestOracle suggestOracle) {

		this.suggestOracle = suggestOracle;
		inputListWidgetDestinatari = new InputListWidget(suggestOracle, "des");
		inputListWidgetDestinatari.addItemCommand(new Command() {
			@Override
			public void execute() {
				// nascondiErrore();
				// TODO
			}
		});
		inputListWidgetInCopia = new InputListWidget(suggestOracle, "copia");
		inCopiaPanel.clear();
		destinatarioPanel.clear();
		RootPanel.get().add(inputListWidgetDestinatari);
		RootPanel.get().add(inputListWidgetInCopia);

		inCopiaPanel.add(inputListWidgetInCopia);
		destinatarioPanel.add(inputListWidgetDestinatari);

	}

	@Override
	public PecOutDTO getBozzaPecOutDto() {

		List<String> destinatari = inputListWidgetDestinatari.getItemSelected();
		List<String> destinatariCC = inputListWidgetInCopia.getItemSelected();
		String corpoMessaggio = getBodyEmail();
		String firma = getFirmaEmail();
		this.pecOutBozzaDTO.setMittente(getMittenteEmail());
		this.pecOutBozzaDTO.setTitolo(oggettoTextBox.getValue());
		this.pecOutBozzaDTO.setDestinatari(getDestinatari(destinatari));
		this.pecOutBozzaDTO.setDestinatariCC(getDestinatari(destinatariCC));
		this.pecOutBozzaDTO.setBody(corpoMessaggio);
		this.pecOutBozzaDTO.setFirma(firma);

		if (this.pecOutBozzaDTO.isInteroperabile()) {
			this.pecOutBozzaDTO.setTipologiaInteroperabile(getTipologiaInteroperabile());
			if (getTipologiaInteroperabile() == TipologiaInteroperabileDTO.ALLEGATI) {
				this.pecOutBozzaDTO.setIdentificativoAllegatoPrincipale(getAllegatoPrincipale());
			}

		}

		return this.pecOutBozzaDTO;
	}

	private static TreeSet<Destinatario> getDestinatari(List<String> destinatari) {
		TreeSet<Destinatario> dests = new TreeSet<Destinatario>();
		for (String d : destinatari) {
			dests.add(new Destinatario(d, null, TipoEmail.PEC, false));
		}
		return dests;
	}

	@Override
	public void setBozzaPecOutDto(PecOutDTO bozza) {
		this.pecOutBozzaDTO = bozza;
	}

	@Override
	public void setCommandFirmaAllegato(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> command) {
		this.firmaAllegatoCommand = command;

	}

	@Override
	public void setCommandCancellaAllegato(it.eng.portlet.consolepec.gwt.client.presenter.Command<Object, Set<AllegatoDTO>> command) {
		this.cancellaAllegatoCommand = command;

	}

	@Override
	public void setCommandMostraCaricaAllegatiDaPratica(Command mostraCaricaAllegatiDaPraticaCommand) {
		this.mostraCaricaAllegatiDaPraticaCommand = mostraCaricaAllegatiDaPraticaCommand;
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
				DettaglioPecOutBozzaView.this.uploadAllegatoCommand.onFileUploaded(dto);
			}

			@Override
			public void onFileSelected(String fileName) {
				DettaglioPecOutBozzaView.this.uploadAllegatoCommand.onFileSelected(fileName);
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
	public Button getEliminaBozzaButton() {
		return eliminaButton;
	}

	@Override
	public ListBox getMittentiListBox() {
		return this.mittenteListBox;
	}

	@Override
	public void setSuggestOracleDestinatari(SuggestOracle suggestBox) {
		this.suggestOracle = suggestBox;
	}

	@Override
	public void setDestinatarioRequired(boolean b) {
		this.inputListWidgetDestinatari.setRequired(b);
	}

	@Override
	public void disabilitaGUI(PecOutDTO bozza, FascicoloDTO fascicolo) {

		boolean fascicoloArchiviato = StatoDTO.ARCHIVIATO.equals(fascicolo.getStato());

		boolean disabilita = bozza.isAzioniDisabilitate();

		if (allegatiSelezionati.size() > 0) {
			firmaAllegatoButton.setEnabled(bozza.isFirmaAllegatoAbilitato() && !disabilita);
			eliminaAllegatoButton.setEnabled(bozza.isEliminaAllegatoAbilitato() && !disabilita);
		} else {
			firmaAllegatoButton.setEnabled(false);
			eliminaAllegatoButton.setEnabled(false);
		}

		this.caricaAllegatoButton.setEnabled(!disabilita && bozza.isCaricaAllegatoAbilitato());
		this.caricaAllegatoDaPraticaButton.setEnabled(!disabilita && bozza.isCaricaAllegatoDaPraticaAbilitato());
		this.inviaButton.setEnabled(!disabilita && bozza.isInviaAbilitato());
		this.eliminaButton.setEnabled(!disabilita && bozza.isEliminaAbilitato() && !fascicoloArchiviato);
	}

	@Override
	public void setInviaBozza(final Command inviaBozzaCommand) {
		inviaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				inviaBozzaCommand.execute();
			}
		});
	}

	@Override
	public void setDownloadAllegatoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.downloadAllegatoCommand = downloadAllegatoCommand;
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	@Override
	public Set<AllegatoDTO> getAllegatiSelezionati() {

		return allegatiSelezionati;
	}

	public void setAllegatiSelezionati(Set<AllegatoDTO> allegatiSelezionati) {

		this.allegatiSelezionati = allegatiSelezionati;
	}

	@Override
	public void cancellaListaAllegatiSelezionati() {
		allegatiSelezionati.clear();
		firmaAllegatoButton.setEnabled(false);
		eliminaAllegatoButton.setEnabled(false);
	}

	@Override
	public void scrollDown() {
		dettaglioAllegatiPanel.setOpen(true);
		// int top = dettaglioAllegatiPanel.getAbsoluteTop();
		int top = Document.get().getScrollHeight();
		Window.scrollTo(0, top);
	}

	@Override
	public void scrollUp() {
		Window.scrollTo(0, 0);

	}

	@Override
	public void resetFocus() {
		oggettoTextBox.setFocus(true);
	}

	@Override
	public void setButtonSalvaLabel(boolean isSalvaAbilitato) {
		if (isSalvaAbilitato) {
			chiudiButton.setText("Chiudi");
		} else {
			chiudiButton.setText("Salva e chiudi");
		}

	}

	@Override
	public TreeSet<Destinatario> getNuoviDestinatari() {
		return getDestinatari(this.inputListWidgetDestinatari.getItemSelected());
	}

	@Override
	public TreeSet<Destinatario> getNuoviDestinatariCC() {
		return getDestinatari(this.inputListWidgetInCopia.getItemSelected());
	}

	@Override
	public boolean isAllegatoPrincipaleSelezionato() {
		return getAllegatoPrincipale() != null;
	}

	@Override
	public TipologiaInteroperabileDTO getTipologiaInteroperabile() {
		return getAllegatoPrincipale().equals(MAIL_PRINCIPALE) ? TipologiaInteroperabileDTO.EMAIL : TipologiaInteroperabileDTO.ALLEGATI;
	}

	@Override
	public void setSalvaFirma(boolean salvaFirma) {
		salvaFirmaCheckbox.setValue(salvaFirma);
	}

	@Override
	public boolean isSalvaFirma() {
		return salvaFirmaCheckbox.getValue();
	}

	private String getAllegatoPrincipale() {
		int selected = documentoPrincipaleInteroperabileListBox.getSelectedIndex();
		if (selected != -1) {
			return documentoPrincipaleInteroperabileListBox.getValue(selected);
		}

		return null;
	}
}
