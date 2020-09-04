package it.eng.portlet.consolepec.gwt.client.view.pec;

import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HRElement;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.MainPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.DettaglioPecInPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.DettaglioPecInPresenter.GoToPraticaCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.command.AnnullaElettoraleCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.pec.command.ImportaElettoraleCommand;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.DettaglioAllegatiWidget;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoPraticaCollegataWidget;
import it.eng.portlet.consolepec.gwt.client.widget.EventoIterFascicoloWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;
import it.eng.portlet.consolepec.gwt.shared.UserAgentUtils;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.Destinatario;
import it.eng.portlet.consolepec.gwt.shared.model.PecInDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.PraticaDTO.TipoPresaInCarico;

public class DettaglioPecInView extends ViewImpl implements DettaglioPecInPresenter.MyView {

	private final Widget widget;
	@UiField
	Label visibileA;
	@UiField
	Button chiudiButton;
	@UiField
	HTMLPanel oggettoPanel;
	@UiField
	HTMLPanel idPanel;
	@UiField
	HTMLPanel bodyPanel;
	@UiField
	DettaglioAllegatiWidget dettaglioAllegatiWidget;
	@UiField
	HTMLPanel destinatariCCPanel;
	@UiField
	HTMLPanel destinatariPanel;
	@UiField
	HTMLPanel protocollazionePanel;
	@UiField
	HTMLPanel inoltratoDaEProtocolloPanel;
	@UiField
	Label mittente;
	@UiField
	Label PG;
	@UiField
	Label stato;
	@UiField
	Label tipoMail;
	@UiField
	Label numeroRepertorio;
	@UiField
	Label assegantario;
	@UiField
	Label operatore;

	@UiField
	Label data;
	@UiField
	Label dataRicezione;
	@UiField
	Label inCaricoALabel;
	@UiField
	Button riassegnaButton;
	@UiField
	LIElement newAggiungiEsistente;
	@UiField
	LIElement newArchivia;
	@UiField
	LIElement newElimina;
	@UiField
	LIElement newRiportaInGestione;
	@UiField
	LIElement newVerificaMail;
	@UiField
	LIElement newRielaboraMail;
	@UiField
	LIElement newRiportaInLettura;
	@UiField
	LIElement newCreaNuovoFascicolo;
	@UiField
	LIElement newModificaOperatore;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	@UiField
	ButtonElement buttonFascicolo;
	@UiField
	ButtonElement buttonStato;
	@UiField
	ButtonElement buttonAltroDettaglio;
	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	@UiField
	Button salvaNote;
	@UiField
	Button annullaModificheNote;
	@UiField
	TextArea noteTextArea;
	@UiField
	HRElement praticheCollegateHR;

	@UiField(provided = true)
	EventoIterFascicoloWidget eventiIter;
	@UiField(provided = true)
	DisclosurePanel iterPecPanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Azioni");

	@UiField(provided = true)
	DisclosurePanel dettaglioPecPanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Dettaglio Pec");

	@UiField(provided = true)
	DisclosurePanel notePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Note");

	@UiField(provided = true)
	DisclosurePanel praticheCollegatePanel = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(),
			"Pratiche collegate");

	@UiField
	Label interoperabile;
	@UiField
	HTMLPanel interoperabilePanel;
	@UiField
	HTMLPanel operatorePanel;

	private PecInDTO pecIn;
	private com.google.gwt.user.client.Command aggiungiPecInPraticaEsistenteCommand;
	private com.google.gwt.user.client.Command chiudiDettaglioCommand;
	private GoToPraticaCommand goToPraticaCommand;
	private HTMLPanel praticheCollegateInnerPanel;

	private String bodyEmail;

	public interface Binder extends UiBinder<Widget, DettaglioPecInView> {}

	@Inject
	public DettaglioPecInView(final Binder binder, final EventBus eventBus) {
		messageWidget = new MessageAlertWidget(eventBus);
		eventiIter = new EventoIterFascicoloWidget();
		widget = binder.createAndBindUi(this);

		Event.sinkEvents(this.newAggiungiEsistente, Event.ONCLICK);
		Event.setEventListener(this.newAggiungiEsistente, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					aggiungiPecInPraticaEsistenteCommand.execute();
				}
			}
		});

		buttonFascicolo.appendChild(new Image(ConsolePECIcons._instance.triangolinoBianco()).getElement());
		buttonStato.appendChild(new Image(ConsolePECIcons._instance.triangolinoBianco()).getElement());
		buttonAltroDettaglio.appendChild(new Image(ConsolePECIcons._instance.triangolinoBianco()).getElement());

		chiudiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				removeFromSlot(MainPresenter.TYPE_SetMainContent, widget);
				chiudiDettaglioCommand.execute();

			}
		});
	}

	private LIElement showHideMenuItem(LIElement liElement, boolean visible) {

		if (visible) {
			liElement.removeClassName("dropdown-disabilitato");
			liElement.addClassName("dropdown-abilitato");
		} else {
			liElement.removeClassName("dropdown-abilitato");
			liElement.addClassName("dropdown-disabilitato");
		}
		return liElement;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setChiudiDettaglioCommand(final com.google.gwt.user.client.Command chiudiDettaglioCommand) {
		this.chiudiDettaglioCommand = chiudiDettaglioCommand;
	}

	private String sanitizeNull(String input) {
		if (input == null || input.trim().isEmpty()) {
			return "-";
		} else {
			return input;
		}
	}

	@Override
	public void mostraPratica(final PecInDTO doc) {

		this.pecIn = doc;

		String stato = sanitizeNull(doc.getStatoLabel());
		this.stato.setText(stato);

		visibileA.setText(doc.getVisibileA());

		String mittente = sanitizeNull(doc.getMittente());
		this.mittente.setText(mittente);

		String oggetto = sanitizeNull(doc.getTitolo());
		SafeHtml oggettoHtml = SafeHtmlUtils.fromString(oggetto);
		HTML hOggetto = new HTML(oggettoHtml);

		this.oggettoPanel.clear();
		this.oggettoPanel.add(hOggetto);

		String data = sanitizeNull(doc.getDataOraArrivo());
		this.data.setText(data);

		String dataRicezione = sanitizeNull(doc.getDataOraCreazione());
		this.dataRicezione.setText(dataRicezione);

		String id = sanitizeNull(doc.getMailId());
		HTML hId = new HTML(id);
		this.idPanel.clear();
		this.idPanel.add(hId);

		// Body
		bodyEmail = sanitizeNull(doc.getBody());
		bodyEmail = formatText(bodyEmail);
		this.bodyPanel.clear();

		Frame bodyFrame = new Frame();
		final IFrameElement iFrameElement = IFrameElement.as(bodyFrame.getElement());
		iFrameElement.setId("chromeFrame");
		this.bodyPanel.add(bodyFrame);

		if (UserAgentUtils.isChrome()) {

			/*
			 * SU CHROME NON ESISTE PIÙ IL CONTENTDOCUMENT -> TRASFORMATO IN CONTENTWINDOW -> NON ESISTE LA PROPRIETA' IN GWT -> HO SCRITTO IL JAVASCRIPT
			 */

			// iFrameElement.getContentDocument().getBody().setInnerHTML(bodyEmail);
			iFrameElement.setAttribute("frameBorder", "no");
			// iFrameElement.setAttribute("width", iFrameElement.getContentDocument().getBody().getScrollWidth() + "px");
			iFrameElement.setAttribute("width", "90%");
			iFrameElement.setScrolling("yes");
			// iFrame.setAttribute("sandbox", "");
			setHtmlIFrameChrome(bodyEmail);
			// iFrameElement.setAttribute("height", iFrameElement.getContentDocument().getBody().getScrollHeight() + "px");

		} else {

			bodyFrame.addLoadHandler(new LoadHandler() {

				@Override
				public void onLoad(LoadEvent event) {
					iFrameElement.getContentDocument().getBody().setInnerHTML(bodyEmail);
					iFrameElement.setAttribute("frameBorder", "no");
					iFrameElement.setAttribute("height", iFrameElement.getContentDocument().getBody().getScrollHeight() + "px");
					// iFrameElement.setAttribute("width", iFrameElement.getContentDocument().getBody().getScrollWidth() + "px");
					iFrameElement.setAttribute("width", "90%");
					iFrameElement.setScrolling("yes");
					// iFrame.setAttribute("sandbox", "");
				}
			});

		}

		if (doc.getNumeroPG() != null && doc.getAnnoPG() != null) {
			this.PG.setText(doc.getNumeroPG() + "/" + doc.getAnnoPG());
		} else {
			this.PG.setText(sanitizeNull(null));
		}
		this.numeroRepertorio.setText(sanitizeNull(doc.getNumeroRepertorio()));
		this.assegantario.setText(sanitizeNull(doc.getAssegnatario()));
		// this.tipoDocumento.setText(sanitizeNull(doc.getTipoDocumento().name()));
		this.tipoMail.setText(sanitizeNull(doc.getTipoEmail()));
		/* carico elenco allegati */
		aggiornaElencoAllegati(doc);

		StringBuilder sb = new StringBuilder();
		if (doc.getDestinatari() != null) {

			if (doc.getDestinatari().size() != 0) {
				for (Destinatario desc : doc.getDestinatari()) {
					sb.append("<span>" + desc.toString() + "</span> <br/>");
				}
			} else {
				sb.append(sanitizeNull(null));
			}
		} else {
			sb.append(sanitizeNull(null));
		}

		HTML destinatari = new HTML(sb.toString());
		this.destinatariPanel.clear();
		this.destinatariPanel.add(destinatari);

		sb = new StringBuilder();
		if (doc.getDestinatariCC() != null) {

			if (doc.getDestinatariCC().size() != 0) {
				for (Destinatario desc : doc.getDestinatariCC()) {
					sb.append("<span>" + desc.toString() + "</span> <br/>");
				}
			} else {
				sb.append(sanitizeNull(null));
			}
		} else {
			sb.append(sanitizeNull(null));
		}

		HTML destinatariCC = new HTML(sb.toString());

		this.destinatariCCPanel.clear();
		this.destinatariCCPanel.add(destinatariCC);

		if (doc.getTipoPresaInCarico().equals(TipoPresaInCarico.NESSUNO)) {
			inCaricoALabel.setText(sanitizeNull(null));
		} else {
			inCaricoALabel.setText(doc.getInCaricoALabel());
		}

		eventiIter.initWidget(doc.getEventiIterDTO());

		salvaNote.setEnabled(doc.isSalvaNote());
		annullaModificheNote.setEnabled(salvaNote.isEnabled());

		noteTextArea.setText(doc.getNote());

		inoltratoDaEProtocolloPanel.setVisible(pecIn.isInoltratoDaEProtocollo());

		praticheCollegatePanel.setVisible(false);
		praticheCollegatePanel.clear();
		if (doc.getIdPraticheCollegate().size() > 0) {
			praticheCollegateInnerPanel = new HTMLPanel("");
			praticheCollegateHR.getStyle().setDisplay(Display.BLOCK);
		} else {

			praticheCollegateHR.getStyle().setDisplay(Display.NONE);
		}
		praticheCollegatePanel.setOpen(false);

		if (doc.isInteroperabile()) {
			interoperabilePanel.setVisible(true);
			interoperabile.setText("Si");
		} else {
			interoperabilePanel.setVisible(false);
			interoperabile.setText("-");
		}

		String operatoreValue = doc.getOperatore();
		if (operatoreValue != null && !operatoreValue.trim().equals("")) {
			operatorePanel.setVisible(true);
			operatore.setText(sanitizeNull(operatoreValue));
		} else {
			operatore.setText(sanitizeNull(null));
			operatorePanel.setVisible(false);
		}

		/**
		 * ABILITAZIONE DELLA PULSANTIERA
		 *
		 */
		this.riassegnaButton.setEnabled(doc.isRiassegnaAbilitato());
		showHideMenuItem(newArchivia, doc.isArchiviabile());
		showHideMenuItem(newElimina, doc.isEliminabile());
		showHideMenuItem(newCreaNuovoFascicolo, doc.isCreaFascicoloAbilitato());
		showHideMenuItem(newAggiungiEsistente, doc.isAgganciaFascicoloAbilitato());
		showHideMenuItem(newRiportaInGestione, doc.isRiportaInGestioneAbilitato());
		showHideMenuItem(newRiportaInLettura, doc.isRiportaInLetturaAbilitato());
		showHideMenuItem(newVerificaMail, doc.isImportaElettoraleAbilitato());
		showHideMenuItem(newRielaboraMail, doc.isAnnullaElettoraleAbilitato());
		showHideMenuItem(newModificaOperatore, doc.isModificaOperatoreAbilitato());
	}

	private native String setHtmlIFrameChrome(String html)/*-{
		var exec = function(t) {
			try {
				var iframe = $doc.getElementById('chromeFrame');
				if (iframe == null) {
					setTimeout(function() {
						exec(t);
					}, 1000);
				} else {
					iframe.contentWindow.document.body.innerHTML = t;
					iframe.height = iframe.contentWindow.document.body.scrollHeight;
					iframe.width = '90%';

					iframe.onload = function() {
						var domdoc = iframe.contentDocument
								|| iframe.contentWindow.document;
						domdoc.body.innerHTML = t;
						iframe.height = iframe.contentWindow.document.body.scrollHeight;
						iframe.width = '90%';
					}

				}
			} catch (err) {
				console.log("errore: ", err);
			}

		};

		exec(html);
	}-*/;

	@Override
	public void addPraticaCollegataSection(PraticaDTO pratica) {
		UListElement ul = Document.get().createULElement();
		ul.addClassName("contenitore-lista-gruppi");
		LIElement li = Document.get().createLIElement();
		li.addClassName("gruppo last clearfix");
		ul.appendChild(li);
		SpanElement span = Document.get().createSpanElement();
		span.addClassName("label nessun-protocollo");
		li.appendChild(span);
		DivElement div = Document.get().createDivElement();
		div.setClassName("corpo");
		li.appendChild(div);
		HTMLPanel panel = new HTMLPanel("");
		panel.setStyleName("box-mail");
		ElementoPraticaCollegataWidget dettaglioWiget = new ElementoPraticaCollegataWidget();
		dettaglioWiget.setCheckBoxVisible(false);
		dettaglioWiget.setCheckBoxEnabled(false);
		dettaglioWiget.setCommand(this.goToPraticaCommand);
		panel.add(dettaglioWiget);
		dettaglioWiget.mostraDettaglio(pratica);
		praticheCollegateInnerPanel.getElement().appendChild(ul);
		praticheCollegateInnerPanel.add(panel, div);
	}

	@Override
	public void buildPanelPraticheCollegate() {
		praticheCollegatePanel.add(praticheCollegateInnerPanel);
		praticheCollegatePanel.setVisible(true);
	}

	private String formatText(String body) {

		body = escapeSpecialMail(body);

		if (body.contains("<")) {
			return body;
		}
		body = body.replaceAll("(\r\n|\n\r|\r|\n)", "<br/>");
		body = body.replaceAll("\t", "&nbsp;");
		// body = body.replaceAll(" ", "&nbsp;");
		body = body.replaceAll("  ", "&nbsp;&nbsp;");
		body = body.replaceAll("&nbsp; ", "&nbsp;&nbsp;");
		return body;
	}

	private String escapeSpecialMail(String body) {

		RegExp regex = RegExp.compile("<[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*>", "g");

		MatchResult match = regex.exec(body);

		if (match != null) {
			for (int i = 0; i < match.getGroupCount(); i++) {
				String mail = match.getGroup(i);
				body = body.replace(mail, SafeHtmlUtils.htmlEscape(mail));
			}
		}

		return body;

	}

	private void aggiornaElencoAllegati(PecInDTO pecIn) {
		dettaglioAllegatiWidget.mostraAllegati(pecIn.getAllegati(), false);
	}

	@Override
	public void setMostraGruppiCommand(final com.google.gwt.user.client.Command mostraGruppiCommand) {
		this.riassegnaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				mostraGruppiCommand.execute();
			}
		});

	}

	@Override
	public void setRiportaInLetturaCommand(final com.google.gwt.user.client.Command riportaInLettura) {
		Event.sinkEvents(newRiportaInLettura, Event.ONCLICK);
		Event.setEventListener(newRiportaInLettura, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					riportaInLettura.execute();
				}
			}
		});
	}

	@Override
	public void setDownloadAllegatoCommand(Command<Void, AllegatoDTO> downloadAllegatoCommand) {
		this.dettaglioAllegatiWidget.setDownloadAllegatoCommand(downloadAllegatoCommand);
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	@Override
	public void setMostraCreaFascicoloFormCommand(final com.google.gwt.user.client.Command mostraCreaFascicoloFormCommand) {
		Event.sinkEvents(newCreaNuovoFascicolo, Event.ONCLICK);
		Event.setEventListener(newCreaNuovoFascicolo, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					mostraCreaFascicoloFormCommand.execute();
				}
			}
		});

	}

	@Override
	public void setEliminaCommand(final com.google.gwt.user.client.Command eliminaCommand) {
		Event.sinkEvents(newElimina, Event.ONCLICK);
		Event.setEventListener(newElimina, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					eliminaCommand.execute();
				}
			}
		});
	}

	@Override
	public void setArchiviaCommand(final com.google.gwt.user.client.Command archiviaCommand) {
		Event.sinkEvents(newArchivia, Event.ONCLICK);
		Event.setEventListener(newArchivia, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					archiviaCommand.execute();
				}
			}
		});
	}

	@Override
	public void setMostraDettaglioAllegatoCommand(Command<Void, AllegatoDTO> mostraDettaglioAllegatoCommand) {
		this.dettaglioAllegatiWidget.setMostraDettaglioAllegatoCommand(mostraDettaglioAllegatoCommand);
	}

	@Override
	public void setRiportaInGestione(final com.google.gwt.user.client.Command riportaInGestioneCommand) {
		Event.sinkEvents(newRiportaInGestione, Event.ONCLICK);
		Event.setEventListener(newRiportaInGestione, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					riportaInGestioneCommand.execute();
				}
			}
		});
	}

	@Override
	public void setAggiungiPecInAFascicoloEsistenteCommand(com.google.gwt.user.client.Command aggiungiPecInPraticaEsistenteCommand) {
		this.aggiungiPecInPraticaEsistenteCommand = aggiungiPecInPraticaEsistenteCommand;

	}

	@Override
	public void setGotoPraticaCommand(GoToPraticaCommand goToPraticaCommand) {
		this.goToPraticaCommand = goToPraticaCommand;

	}

	@Override
	public void resetDisclosurePanels(boolean showActions) {
		if (showActions) {
			this.dettaglioPecPanel.setOpen(false);
			this.iterPecPanel.setOpen(true);
			this.notePanel.setOpen(false);
			this.praticheCollegatePanel.setOpen(false);
		} else {
			this.dettaglioPecPanel.setOpen(true);
			this.iterPecPanel.setOpen(false);
			this.notePanel.setOpen(false);
			this.praticheCollegatePanel.setOpen(false);
		}
	}

	@Override
	public void setSalvaPECCommand(final com.google.gwt.user.client.Command salvaPECCommand) {
		salvaNote.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				salvaPECCommand.execute();
			}
		});

	}

	@Override
	public void setAnnullaSalvaPECCommand(final com.google.gwt.user.client.Command annullaSalvaPECCommand) {
		annullaModificheNote.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				annullaSalvaPECCommand.execute();
			}
		});
	}

	@Override
	public PecInDTO getPecIn() {
		pecIn.setNote(noteTextArea.getText());

		return pecIn;
	}

	@Override
	public void setImportaElettoraleCommand(final ImportaElettoraleCommand importaElettoraleCommand) {
		Event.sinkEvents(newVerificaMail, Event.ONCLICK);
		Event.setEventListener(newVerificaMail, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					importaElettoraleCommand.execute();
				}
			}
		});
	}

	@Override
	public void setAnnullaElettoraleCommand(final AnnullaElettoraleCommand annullaElettoraleCommand) {
		Event.sinkEvents(newRielaboraMail, Event.ONCLICK);
		Event.setEventListener(newRielaboraMail, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					annullaElettoraleCommand.execute();
				}
			}
		});
	}

	@Override
	public void setModificaOperatoreCommand(final com.google.gwt.user.client.Command command) {
		Event.sinkEvents(newModificaOperatore, Event.ONCLICK);
		Event.setEventListener(newModificaOperatore, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if (Event.ONCLICK == event.getTypeInt()) {
					command.execute();
				}
			}
		});
	}

	@Override
	public String getNote() {
		return this.noteTextArea.getText();
	}
}
