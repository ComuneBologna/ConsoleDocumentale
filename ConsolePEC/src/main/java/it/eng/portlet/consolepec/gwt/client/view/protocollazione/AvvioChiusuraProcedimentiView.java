package it.eng.portlet.consolepec.gwt.client.view.protocollazione;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.procedimento.ChiusuraProcedimentoInput.ModalitaChiusuraProcedimento;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.presenter.Command;
import it.eng.portlet.consolepec.gwt.client.presenter.protocollazione.AvvioChiusuraProcedimentiPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.ElementoProcedimentoElencoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.GroupSuggestBoxProtocollazione;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton.YesNoRadioButtonCommand;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.InputListWidget;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.ProcedimentoMiniDto;

public class AvvioChiusuraProcedimentiView extends ViewImpl implements AvvioChiusuraProcedimentiPresenter.MyView {

	private final Widget widget;

	@UiField
	Label titoloPagina;
	@UiField
	HTMLPanel riepilogoPanel;
	@UiField
	Button indietroButton;
	@UiField
	Button annullaButton;
	@UiField
	Button confermaButton;
	@UiField
	HTMLPanel quartieriSuggestBoxPanel;
	@UiField
	HTMLPanel procedimentiSuggestBoxPanel;
	@UiField
	DateBox dataInizioDateBox;

	@UiField
	HTMLPanel emailPanel;

	@UiField
	HTMLPanel procedimentiPanel;
	@UiField
	HTMLPanel procedimentiListPanel;

	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;
	@UiField
	ListBox modAvvioListBox;
	@UiField
	HTMLPanel modAvvioPanel;

	@UiField(provided = true)
	YesNoRadioButton sceltaCambioDataAvvio;

	@UiField(provided = true)
	YesNoRadioButton sceltaInvioMailAvvio;

	@UiField
	HeadingElement riepilogoTitle;
	@UiField
	HTMLPanel avvioProcedimentoPanel;
	@UiField
	HTMLPanel chiusuraProcedimentoPanel;
	@UiField
	HTMLPanel inputChiusuraPanel;
	@UiField
	HTMLPanel modChiusuraPanel;
	@UiField
	ListBox modChiusuraListBox;
	@UiField
	DateBox dataChiusuraDateBox;
	@UiField
	HeadingElement chiusuraWarning;
	@UiField
	HTMLPanel warningPanel;

	private InputListWidget inputListWidgetDestinatari;

	private com.google.gwt.user.client.Command confermaCommand;
	private Date dataPropostaAvvioProcedimento;
	private List<String> emailPropostaAvvioProcedimento;

	private com.google.gwt.user.client.Command indietroCommand;

	public interface Binder extends UiBinder<Widget, AvvioChiusuraProcedimentiView> {}

	@Inject
	public AvvioChiusuraProcedimentiView(final Binder binder, final EventBus eventBus) {
		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		sceltaCambioDataAvvio = new YesNoRadioButton("Si vuole cambiare la data di decorrenza del procedimento? ");
		sceltaCambioDataAvvio.selectNo();
		sceltaCambioDataAvvio.setYesNoRadioButtonCommand(new YesNoRadioButtonCommand() {

			@Override
			public void execute(Boolean value) {
				abilitaCampoDataInizio(value);
				if (!value && dataPropostaAvvioProcedimento != null) // ripristino la data proposta
					dataInizioDateBox.setValue(dataPropostaAvvioProcedimento);
			}
		});
		sceltaInvioMailAvvio = new YesNoRadioButton("Si vuole indicare un indirizzo email al quale inviare la comunicazione di avvio procedimento? ");
		sceltaInvioMailAvvio.selectNo();
		sceltaInvioMailAvvio.setYesNoRadioButtonCommand(new YesNoRadioButtonCommand() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					if (emailPropostaAvvioProcedimento != null && !emailPropostaAvvioProcedimento.isEmpty())
						for (String s : emailPropostaAvvioProcedimento) {
							inputListWidgetDestinatari.addValueItem(s);
						}
				} else {
					inputListWidgetDestinatari.clear();
				}
				abilitaCampoEmail(value);
			}
		});
		widget = binder.createAndBindUi(this);
		Format dateFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA));
		dataInizioDateBox.getDatePicker().setYearArrowsVisible(true);
		dataInizioDateBox.setFormat(dateFormat);
		dataChiusuraDateBox.getDatePicker().setYearArrowsVisible(true);
		dataChiusuraDateBox.setFormat(dateFormat);
		confermaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				confermaCommand.execute();
			}
		});
		indietroButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				indietroCommand.execute();
			}
		});

		emailPanel.clear();

		inputListWidgetDestinatari = new InputListWidget(new MultiWordSuggestOracle(), "des");

		RootPanel.get().add(inputListWidgetDestinatari);
		emailPanel.add(inputListWidgetDestinatari);

	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		Window.scrollTo(0, 0);
	}

	@Override
	public void initializeRiepilogoProtocollazione(String anno, String pg, String pgCapofila, String annoCapofila, String tipologiaDocumento, String titolo, String rubrica, String sezione) {
		riepilogoPanel.clear();

		StringBuilder sb = new StringBuilder();
		sb.append(getValue(pg + "/" + anno, "PG"));
		sb.append(getValue(pgCapofila + "/" + annoCapofila, "PG Capofila"));
		sb.append(getValue(clearTitolazione(tipologiaDocumento) + " - " + GroupSuggestBoxProtocollazione.getTipoDocumentoNameByIdDisplayName(clearTitolazione(tipologiaDocumento)),
				"Tipologia documento"));
		sb.append(getValue(clearTitolazione(titolo) + " - " + GroupSuggestBoxProtocollazione.getTitoloNameByIdDisplayName(clearTitolazione(titolo)), "Titolo"));
		sb.append(getValue(clearTitolazione(rubrica) + " - " + GroupSuggestBoxProtocollazione.getRubricaNameByIdTitolo(clearTitolazione(rubrica), clearTitolazione(titolo)), "Rubrica"));
		sb.append(getValue(clearTitolazione(sezione) + " - " + GroupSuggestBoxProtocollazione.getSezioneNameByIdRubrica(clearTitolazione(sezione), clearTitolazione(titolo), clearTitolazione(rubrica)),
				"Sezione"));

		riepilogoPanel.add(new HTMLPanel(sb.toString()));

	}

	private String clearTitolazione(String descr) {
		if (descr.contains(" - ")) {
			descr = descr.split(" - ")[0].trim();
		}
		return descr;
	}

	private String getValue(String valore, String etichetta) {
		return " <div class='cell'><span class='label'>" + etichetta + ":</span>" + "<div class='abstract'><span>" + (valore == null ? " - " : valore).toUpperCase() + " </span></div></div>";
	}

	@Override
	public void setProcedimentiSuggestBox(final SuggestBox suggestBox, final Command<Void, String> selectionCommand) {
		procedimentiSuggestBoxPanel.clear();
		procedimentiSuggestBoxPanel.getElement().setInnerHTML("");
		procedimentiSuggestBoxPanel.add(suggestBox);

		suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String procedimento = suggestBox.getValue();
				selectionCommand.exe(procedimento);
			}
		});

	}

	@Override
	public void setQuartieriSuggestBox(final SuggestBox suggestBox, final Command<Void, String> selectionCommand) {
		quartieriSuggestBoxPanel.clear();
		quartieriSuggestBoxPanel.getElement().setInnerHTML("");
		quartieriSuggestBoxPanel.add(suggestBox);

		suggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				String quartiere = suggestBox.getValue();
				selectionCommand.exe(quartiere);
			}
		});
	}

	@Override
	public void setModAvvioListBox(HashMap<String, String> mapModAvvio) {
		modAvvioListBox.clear();
		for (String key : mapModAvvio.keySet())
			modAvvioListBox.addItem(mapModAvvio.get(key), key);
	}

	@Override
	public void setModChiusuraListBox(List<String> modalitaChiusura) {
		modChiusuraListBox.clear();
		for (String key : modalitaChiusura)
			modChiusuraListBox.addItem(key);
	}

	@Override
	public void setAnnullaCommand(final com.google.gwt.user.client.Command command) {
		annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setIndietroCommand(final com.google.gwt.user.client.Command command) {
		this.indietroCommand = command;
	}

	@Override
	public void setConfermaCommand(com.google.gwt.user.client.Command command) {
		this.confermaCommand = command;
	}

	@Override
	public InputListWidget getCampoEmail() {
		return inputListWidgetDestinatari;
	}

	@Override
	public boolean getSceltaInvioMailAvvio() {
		return sceltaInvioMailAvvio.getValue();
	}

	@Override
	public DateBox getCampoDataInizio() {
		return dataInizioDateBox;
	}

	@Override
	public ListBox getCampoModAvvio() {
		return modAvvioListBox;
	}

	@Override
	public DateBox getCampoDataChiusura() {
		return dataChiusuraDateBox;
	}

	@Override
	public ListBox getCampoModChiusura() {
		return modChiusuraListBox;
	}

	@Override
	public void abilitaCampoModAvvio(boolean abilita) {
		modAvvioListBox.setEnabled(abilita);
		if (abilita) {
			modAvvioListBox.removeStyleName("testo disabilitato");
			modAvvioListBox.setStyleName("testo");
		} else {
			modAvvioListBox.removeStyleName("testo");
			modAvvioListBox.setStyleName("testo disabilitato");
		}
	}

	@Override
	public void abilitaCampoModChiusura(boolean abilita) {
		modChiusuraListBox.setEnabled(abilita);
		if (abilita) {
			modChiusuraListBox.removeStyleName("testo disabilitato");
			modChiusuraListBox.setStyleName("testo");
		} else {
			modChiusuraListBox.removeStyleName("testo");
			modChiusuraListBox.setStyleName("testo disabilitato");
		}
	}

	@Override
	public void abilitaCampoDataInizio(boolean abilita) {
		dataInizioDateBox.setEnabled(abilita);
		if (abilita) {
			dataInizioDateBox.removeStyleName("testo disabilitato");
			dataInizioDateBox.setStyleName("testo");
		} else {
			dataInizioDateBox.removeStyleName("testo");
			dataInizioDateBox.setStyleName("testo disabilitato");
		}
	}

	@Override
	public void abilitaCampoDataChiusura(boolean abilita) {
		dataChiusuraDateBox.setEnabled(abilita);
		if (abilita) {
			dataChiusuraDateBox.removeStyleName("testo disabilitato");
			dataChiusuraDateBox.setStyleName("testo");
		} else {
			dataChiusuraDateBox.removeStyleName("testo");
			dataChiusuraDateBox.setStyleName("testo disabilitato");
		}
	}

	@Override
	public void abilitaCampoEmail(boolean abilita) {

		inputListWidgetDestinatari.setAbilitato(!abilita);

	}

	@Override
	public String getSelectedModAvvio() {
		return modAvvioListBox.getValue(modAvvioListBox.getSelectedIndex());
	}

	@Override
	public void initCheckBoxes() {
		sceltaCambioDataAvvio.selectNo();
		sceltaInvioMailAvvio.selectYes();
	}

	@Override
	public void getUpdatePrevalorizzazioni(Date dataInizio, List<String> destinatarioEmail) {
		if (destinatarioEmail != null) {
			for (String d : destinatarioEmail) {
				inputListWidgetDestinatari.addValueItem(d);
			}
		}
		dataInizioDateBox.setValue(dataInizio);

	}

	@Override
	public void mostraAvvio() {
		titoloPagina.setText(ConsolePecConstants.TITOLO_AVVIO_PROCEDIMENTO);
		riepilogoTitle.setInnerText(ConsolePecConstants.RIEPILOGO_AVVIO_PROCEDIMENTO);
		avvioProcedimentoPanel.setVisible(true);
		chiusuraProcedimentoPanel.setVisible(false);
	}

	@Override
	public void mostraChiusura() {
		titoloPagina.setText(ConsolePecConstants.TITOLO_CHIUSURA_PROCEDIMENTO);
		riepilogoTitle.setInnerText(ConsolePecConstants.RIEPILOGO_CHIUSURA_PROCEDIMENTO);
		avvioProcedimentoPanel.setVisible(false);
		chiusuraProcedimentoPanel.setVisible(true);
	}

	@Override
	public void popolaProcedimenti(List<ProcedimentoMiniDto> procedimenti) {
		procedimentiListPanel.clear();
		procedimentiListPanel.setVisible(false);
		HTMLPanel innerPanel = new HTMLPanel("");
		for (ProcedimentoMiniDto procedimento : procedimenti)
			addProcedimentiSection(procedimento, innerPanel, procedimenti != null && procedimenti.size() == 1);
		procedimentiListPanel.setVisible(true);
		procedimentiListPanel.add(innerPanel);
	}

	private void addProcedimentiSection(ProcedimentoMiniDto procedimento, HTMLPanel innerPanel, boolean checkBoxEnabled) {
		Label mailId = new Label(procedimento.getNumeroPG() + "/" + procedimento.getAnnoPG());
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
		ElementoProcedimentoElencoWidget dettaglioWiget = new ElementoProcedimentoElencoWidget();
		dettaglioWiget.setCheckBoxVisible(checkBoxEnabled);
		dettaglioWiget.setCheckBoxEnabled(checkBoxEnabled);
		panel.add(dettaglioWiget);
		dettaglioWiget.mostraDettaglio(procedimento);
		innerPanel.getElement().appendChild(ul);
		innerPanel.add(mailId, span);
		innerPanel.add(panel, div);
	}

	@Override
	public void abilitaChiusura(boolean abilita) {
		inputChiusuraPanel.setVisible(abilita);
		confermaButton.setEnabled(abilita);
	}

	@Override
	public ModalitaChiusuraProcedimento getSelectedModChiusura() {
		return ModalitaChiusuraProcedimento.fromDescrizione(modChiusuraListBox.getSelectedValue());
	}

	@Override
	public void setWarningChiusura(String message) {
		warningPanel.setVisible(message != null || !"".equals(message));
		chiusuraWarning.setInnerText(message);
	}

	@Override
	public void setDataPropostaAvvioProcedimento(Date dataPropostaAvvioProcedimento) {
		this.dataPropostaAvvioProcedimento = dataPropostaAvvioProcedimento;
	}

	@Override
	public void setEmailPropostaAvvioProcedimento(List<String> emailPropostaAvvioProcedimento) {
		this.emailPropostaAvvioProcedimento = emailPropostaAvvioProcedimento;
	}

	@Override
	public void abilitaAvvio(boolean abilita) {
		confermaButton.setEnabled(abilita);
	}

	@Override
	public void resetDestinatariEmail() {
		inputListWidgetDestinatari.clear();
	}

	@Override
	public String getTextFromInputListWidgetDestinatari() {
		return inputListWidgetDestinatari.getText();
	}

}
