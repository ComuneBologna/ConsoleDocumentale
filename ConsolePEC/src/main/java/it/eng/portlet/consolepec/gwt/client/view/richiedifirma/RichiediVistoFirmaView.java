package it.eng.portlet.consolepec.gwt.client.view.richiedifirma;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.profilazione.Utente;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.richiedifirma.RichiediVistoFirmaPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.GruppoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.UtenteWidget;
import it.eng.portlet.consolepec.gwt.client.widget.configurazioni.AnagraficheRuoliWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.InputListWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.IndirizziEmailSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.TimePickerSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.UtentiSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoPropostaTaskFirmaDTO;

/**
 *
 * @author biagiot
 *
 */
public class RichiediVistoFirmaView extends ViewImpl implements RichiediVistoFirmaPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, RichiediVistoFirmaView> {
		//
	}

	@UiField
	protected HeadingElement titolo;
	@UiField
	DateBox propostaDateBox;
	@UiField
	TextBox oggettoDocumento;
	@UiField
	TextArea noteTextArea;
	@UiField
	HTMLPanel indirizziNotificaPanel;
	@UiField
	HTMLPanel indirizziNotificaContainer;
	@UiField
	Button aggiungiRuolo;
	@UiField
	Button aggiungiUtente;
	@UiField
	ListBox tipoRichiestaListBox;
	@UiField
	HTMLPanel destinatariPanel;
	@UiField
	Button annullaButton;
	@UiField
	Button confermaButton;
	@UiField(provided = true)
	MessageAlertWidget messageWidget;
	@UiField
	TextBox gruppoProponenteTextBox;
	@UiField
	HTMLPanel gruppiSuggestPanel;
	@UiField
	HTMLPanel utentiSuggestPanel;
	@UiField
	HTMLPanel gruppiPanel;
	@UiField
	HTMLPanel utentiPanel;
	@UiField
	DateBox dataScadenzaDateBox;
	@UiField(provided = true)
	SuggestBox timeScadenzaSuggestBox = new SuggestBox(new TimePickerSuggestOracle());
	@UiField
	TextBox mittenteOriginale;
	@UiField
	RadioButton utenteRadioButton;
	@UiField
	RadioButton gruppoRadioButton;

	private InputListWidget inputListWidgetDestinatariNotifica;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Utente> eliminaUtenteCommand;
	private it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AnagraficaRuolo> eliminaGruppoCommand;
	private SelectionHandler<SuggestOracle.Suggestion> elencoUtentiSelectionHandler;
	private SelectionHandler<SuggestOracle.Suggestion> elencoGruppiSelectionHandler;
	private ClickHandler elencoUtentiClickHandler;
	private ClickHandler elencoGruppiClickHandler;
	private SuggestBox elencoUtentiSuggestBox;
	private final UtentiSuggestOracle utentiSuggestOracle;
	private Command onChangeTipoDestinatarioCommand;
	private AnagraficheRuoliWidget ruoliWidget;
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	@Inject
	public RichiediVistoFirmaView(final Binder binder, final EventBus eventBus, final UtentiSuggestOracle utentiSuggestOracle, ConfigurazioniHandler configurazioniHandler,
			ProfilazioneUtenteHandler profilazioneUtenteHandler) {
		this.utentiSuggestOracle = utentiSuggestOracle;
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public void initViewElements() {
		initDateBox();
		initIndirizziNotifica();
		initDestinatari();
	}

	private void initDestinatari() {
		destinatariPanel.clear();

		utenteRadioButton.setValue(true);
		utentiPanel.setVisible(true);
		elencoUtentiSuggestBox = new SuggestBox(utentiSuggestOracle);
		elencoUtentiSuggestBox.setStyleName("testo");
		elencoUtentiSuggestBox.addSelectionHandler(elencoUtentiSelectionHandler);
		utentiSuggestPanel.add(elencoUtentiSuggestBox);
		aggiungiUtente.addClickHandler(elencoUtentiClickHandler);

		utenteRadioButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onChangeTipoDestinatarioCommand.execute();
				resetDestinatari();
			}
		});

		gruppoRadioButton.setValue(false);
		gruppiPanel.setVisible(false);
		ruoliWidget = new AnagraficheRuoliWidget(configurazioniHandler, profilazioneUtenteHandler, utentiSuggestOracle, true, false, true) //
				.rewriteLabel("Filtra per settore", null, "Filtra per utente");
		ruoliWidget.addRuoliSelectionHandler(elencoGruppiSelectionHandler);
		ruoliWidget.showWidget(null, null, false, true, true);
		gruppiSuggestPanel.add(ruoliWidget);
		aggiungiRuolo.addClickHandler(elencoGruppiClickHandler);

		gruppoRadioButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onChangeTipoDestinatarioCommand.execute();
				destinatariPanel.clear();
				utentiPanel.setVisible(false);
				gruppiPanel.setVisible(true);
			}
		});
	}

	private void resetDestinatari() {
		destinatariPanel.clear();
		gruppiPanel.setVisible(false);
		utentiPanel.setVisible(true);
		gruppoRadioButton.setValue(false);
		utenteRadioButton.setValue(true);
	}

	@Override
	public void initTipoRichiestaListBox(boolean allegatoProtocollato) {
		for (TipoPropostaTaskFirmaDTO tipoProposta : TipoPropostaTaskFirmaDTO.values()) {

			if (allegatoProtocollato) {
				if (tipoProposta.isAllegatoProtocollatoEnabled())
					tipoRichiestaListBox.addItem(tipoProposta.getLabel());

			} else {
				tipoRichiestaListBox.addItem(tipoProposta.getLabel());
			}
		}

		if (!allegatoProtocollato)
			tipoRichiestaListBox.setSelectedIndex(TipoPropostaTaskFirmaDTO.getLabels().indexOf(TipoPropostaTaskFirmaDTO.FIRMA.getLabel()));
		else
			tipoRichiestaListBox.setSelectedIndex(TipoPropostaTaskFirmaDTO.getLabels().indexOf(TipoPropostaTaskFirmaDTO.PARERE.getLabel()));
	}

	private void initIndirizziNotifica() {
		inputListWidgetDestinatariNotifica = new InputListWidget(new IndirizziEmailSuggestOracle(new ArrayList<String>()), "notifiche");
		RootPanel.get().add(inputListWidgetDestinatariNotifica);
		indirizziNotificaContainer.add(inputListWidgetDestinatariNotifica);
	}

	private void initDateBox() {
		DateTimeFormat f = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
		Format format = new DateBox.DefaultFormat(f);
		propostaDateBox.setFormat(format);

		DateTimeFormat f1 = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
		Format format1 = new DateBox.DefaultFormat(f1);
		dataScadenzaDateBox.setFormat(format1);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setTitle(String title) {
		titolo.setInnerText(title);
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		titolo.scrollIntoView();
		Window.scrollTo(0, 0);
	}

	@Override
	public void setAnnullaCommand(final Command cmd) {
		annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cmd.execute();
			}
		});
	}

	@Override
	public void setConfermaCommand(final Command cmd) {
		confermaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cmd.execute();
			}
		});
	}

	@Override
	public Utente getUtenteSelezionato() {
		UtentiSuggestOracle oracle = (UtentiSuggestOracle) elencoUtentiSuggestBox.getSuggestOracle();
		Utente utente = oracle.getUtenteFromSuggest(elencoUtentiSuggestBox.getValue());
		return utente;
	}

	@Override
	public void reset() {
		messageWidget.reset();
		oggettoDocumento.setText(null);
		noteTextArea.setText(null);
		tipoRichiestaListBox.clear();
		destinatariPanel.clear();
		mittenteOriginale.setText(null);
		elencoUtentiSuggestBox.setValue(null);
		ruoliWidget.clear();
		if (inputListWidgetDestinatariNotifica != null)
			inputListWidgetDestinatariNotifica.reset();
		dataScadenzaDateBox.setValue(null);
		timeScadenzaSuggestBox.setValue(null);
		resetDestinatari();
	}

	@Override
	public Date getDataProposta() {
		return propostaDateBox.getValue();
	}

	@Override
	public void setDataProposta(Date date) {
		this.propostaDateBox.setValue(date);
	}

	@Override
	public Date getDataScadenza() {
		return dataScadenzaDateBox.getValue();
	}

	@Override
	public Integer getOraScadenza() {
		return TimePickerSuggestOracle.getHour(timeScadenzaSuggestBox.getValue());
	}

	@Override
	public Integer getMinutoScadenza() {
		return TimePickerSuggestOracle.getMinute(timeScadenzaSuggestBox.getValue());
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
			messageWidget.showWarningMessage(w.getHTML());

		} else {
			messageWidget.reset();
		}
	}

	@Override
	public List<String> getIndirizziNotifica() {
		return inputListWidgetDestinatariNotifica.getItemSelected();
	}

	@Override
	public String getProponente() {
		return gruppoProponenteTextBox.getText();
	}

	@Override
	public String getMittenteOriginale() {
		return mittenteOriginale.getText();
	}

	@Override
	public String getOggetto() {
		return oggettoDocumento.getText();
	}

	@Override
	public String getNote() {
		return noteTextArea.getText();
	}

	@Override
	public String getTextFromInputListIndirizziNotifica() {
		return inputListWidgetDestinatariNotifica.getText();
	}

	@Override
	public TipoPropostaTaskFirmaDTO getTipoRichiesta() {
		return TipoPropostaTaskFirmaDTO.fromLabel(tipoRichiestaListBox.getSelectedValue());
	}

	@Override
	public void setEliminaUtenteCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, Utente> eliminaUtenteCommand) {
		this.eliminaUtenteCommand = eliminaUtenteCommand;
	}

	@Override
	public void clearElencoUtentiSuggestBox() {
		elencoUtentiSuggestBox.setValue(null);
	}

	@Override
	public void addUtenteWidget(Utente utente, boolean editable) {
		UtenteWidget widget = new UtenteWidget();
		widget.setEliminaCommand(eliminaUtenteCommand);
		destinatariPanel.add(widget);
		widget.showWidget(utente, editable);
	}

	@Override
	public void setElencoUtentiSelectionHandler(SelectionHandler<SuggestOracle.Suggestion> selectionHandler) {
		this.elencoUtentiSelectionHandler = selectionHandler;
	}

	@Override
	public void setElencoUtentiClickHandler(ClickHandler clickHandler) {
		this.elencoUtentiClickHandler = clickHandler;
	}

	@Override
	public AnagraficaRuolo getRuoloSelezionato() {
		return ruoliWidget.getAnagraficaRuoloSelezionata();
	}

	@Override
	public void addGruppoWidget(AnagraficaRuolo ruoloSelezionato, boolean editable) {
		GruppoWidget widget = new GruppoWidget();
		widget.setEliminaCommand(eliminaGruppoCommand);
		destinatariPanel.add(widget);
		widget.showWidget(ruoloSelezionato, editable);

	}

	@Override
	public void clearElencoGruppiSuggestBox() {
		ruoliWidget.clear();
	}

	@Override
	public void setElencoGruppiSelectionHandler(SelectionHandler<Suggestion> selectionHandler) {
		this.elencoGruppiSelectionHandler = selectionHandler;
	}

	@Override
	public void setElencoGruppiClickHandler(ClickHandler clickHandler) {
		this.elencoGruppiClickHandler = clickHandler;
	}

	@Override
	public void setEliminaGruppoCommand(it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, AnagraficaRuolo> eliminaGruppoCommand) {
		this.eliminaGruppoCommand = eliminaGruppoCommand;
	}

	@Override
	public void setOnChangeTipoDestinatarioCommand(Command command) {
		this.onChangeTipoDestinatarioCommand = command;
	}

	@Override
	public void aggiungiIndirizzoNotifica(AnagraficaRuolo ruolo) {
		if (ruolo != null) {
			inputListWidgetDestinatariNotifica.addValueItem(Strings.nullToEmpty(ruolo.getEmailPredefinita()));
		}
	}

	@Override
	public void setGruppoProponente(String gruppoProponente) {
		this.gruppoProponenteTextBox.setText(gruppoProponente);
	}

	@Override
	public void enableAggiungiUtenteButton(boolean enable) {
		aggiungiUtente.setEnabled(enable);
	}

	@Override
	public void enableAggiungiRuoloButton(boolean enable) {
		aggiungiRuolo.setEnabled(enable);
	}
}
