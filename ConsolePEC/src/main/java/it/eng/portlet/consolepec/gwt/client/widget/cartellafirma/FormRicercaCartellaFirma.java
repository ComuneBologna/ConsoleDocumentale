package it.eng.portlet.consolepec.gwt.client.widget.cartellafirma;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.configurazioni.abilitazione.VisibilitaRuoloAbilitazione;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeCartellaAttivita;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeCartellaAttivita.StatoFinaleRicerca;
import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeCartellaAttivita.TipoRicerca;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.generics.GenericsUtil;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.operazioni.cartellafirma.CartellaFirmaRicercaApiClient;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton;
import it.eng.portlet.consolepec.gwt.client.widget.YesNoRadioButton.YesNoRadioButtonCommand;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliSuggestOracle;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.action.cartellafirma.CercaDocumentoFirmaVistoAction;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoDestinatarioTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.StatoTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoPropostaTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoStatoTaskFirmaDTO;
import it.eng.portlet.consolepec.gwt.shared.model.cartellafirma.TipoUtenteTaskFirmaDTO;

/**
 * 
 * @author biagiot
 * 
 */
public class FormRicercaCartellaFirma extends Composite {

	private static FormRicercaCartellaFirmaWidgetUiBinder uiBinder = GWT.create(FormRicercaCartellaFirmaWidgetUiBinder.class);

	interface FormRicercaCartellaFirmaWidgetUiBinder extends UiBinder<Widget, FormRicercaCartellaFirma> {/**/}

	@UiField TextBox oggettoTextBox;
	@UiField(provided = true) SuggestBox gruppoProponenteSuggestBox = new SuggestBox(new AnagraficheRuoliSuggestOracle(new ArrayList<AnagraficaRuolo>()));
	@UiField DateBox dataFrom;
	@UiField DateBox dataTo;
	@UiField(provided = true) SuggestBox tipoRichiestaSuggestBox = new SuggestBox(new SpacebarSuggestOracle(TipoPropostaTaskFirmaDTO.getLabels()));
	@UiField TextBox idDocumentaleTextBox;
	@UiField(provided = true) SuggestBox tipoStatoSuggestBox = new SuggestBox(new SpacebarSuggestOracle(TipoStatoTaskFirmaDTO.getLabels()));
	@UiField TextBox titoloFascicoloTextBox;
	@UiField(provided = true) SuggestBox tipologiaFascicoloSuggestBox = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));
	@UiField(provided = true) SuggestBox statoSuggestBox = new SuggestBox(new SpacebarSuggestOracle(new ArrayList<String>()));
	@UiField(provided = true) SuggestBox statoDestinatarioSuggestBox = new SuggestBox(new SpacebarSuggestOracle(StatoDestinatarioTaskFirmaDTO.getLabels()));
	@UiField Button pulisciButton;
	@UiField Button cercaButton;
	@UiField HTMLPanel chiudiAvanzatePanel;
	@UiField HTMLPanel avanzatePanel;
	@UiField Button chiudiAvanzatePanelButton;
	@UiField Button avanzateButton;
	@UiField MessageAlertWidget messageWidget;
	@UiField HTMLPanel messageWidgetPanel;
	@UiField(provided = true) YesNoRadioButton ricercaDaDestinatario = new YesNoRadioButton("Ricerca da destinatario", true);
	@UiField CheckBox salvaFiltri;
	@UiField TextBox mittenteOriginaleTextBox;
	@UiField DateBox dataScadenzaFrom;
	@UiField DateBox dataScadenzaTo;

	@UiField HTMLPanel filtraPanel;
	@UiField HTMLPanel filtriPanel;
	@UiField Button filtraButton;

	private KeyDownHandler invioButton;
	private CartellaFirmaRicercaApiClient cartellaFirmaRicercaApiClient;
	private DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
	private List<String> filtriValorizzati = new ArrayList<String>();
	private ConfigurazioniHandler configurazioniHandler;
	private ProfilazioneUtenteHandler profilazioneUtenteHandler;

	public FormRicercaCartellaFirma(CartellaFirmaRicercaApiClient cartellaFirmaRicercaApiClient) {
		super();
		this.cartellaFirmaRicercaApiClient = cartellaFirmaRicercaApiClient;
		initWidget(uiBinder.createAndBindUi(this));

		if (Window.getClientWidth() > ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
			filtraPanel.setVisible(false);
			filtriPanel.setVisible(true);
		} else {
			filtraPanel.setVisible(true);
			filtriPanel.setVisible(false);

			filtraButton.setText("Filtra");
			filtraButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					boolean visibility = filtriPanel.isVisible();
					filtriPanel.setVisible(!visibility);
				}
			});
		}
	}

	public void initForm(ConfigurazioniHandler configurazioniHandler, ProfilazioneUtenteHandler profilazioneUtenteHandler, final Command cercaCommand) {
		this.configurazioniHandler = configurazioniHandler;
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;

		final int clientWidth = Window.getClientWidth();

		invioButton = new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					resetMessageWidget();

					if (clientWidth < ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
						gestioneFiltri();
						filtriPanel.setVisible(false);
					}
					cercaCommand.execute();
				}
			}
		};

		cercaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetMessageWidget();

				if (clientWidth < ConsolePecConstants.MIN_DESKTOP_WIDTH_PIXELS) {
					gestioneFiltri();
					filtriPanel.setVisible(false);
				}

				cercaCommand.execute();
			}
		});

		pulisciButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				pulisciForm();
			}
		});

		avanzateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				avanzatePanel.setVisible(!avanzatePanel.isVisible());
				chiudiAvanzatePanel.setVisible(!chiudiAvanzatePanel.isVisible());
			}
		});

		chiudiAvanzatePanelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				avanzatePanel.setVisible(false);
				chiudiAvanzatePanel.setVisible(false);
			}
		});

		configuraHandlerCampi();
		prevalorizzaCampi();
		initTipologiaFascicoloSuggestBox();
		initTipoStatoRichiestaSuggestBox();
		initTipoPropostaSuggestBox();
		initRicercaRadioButton();
		loadPreferenzeFiltriDiRicerca();
		gestioneFiltri();

		Format dateFormat = new DateBox.DefaultFormat(this.dateTimeFormat);
		dataFrom.setFormat(dateFormat);
		dataFrom.getDatePicker().setYearArrowsVisible(true);
		dataTo.setFormat(dateFormat);
		dataTo.getDatePicker().setYearArrowsVisible(true);
		dataScadenzaFrom.setFormat(dateFormat);
		dataScadenzaFrom.getDatePicker().setYearArrowsVisible(true);
		dataScadenzaTo.setFormat(dateFormat);
		dataScadenzaTo.getDatePicker().setYearArrowsVisible(true);
	}

	public void resetMessageWidget() {
		messageWidgetPanel.setVisible(false);
		messageWidget.reset();
	}

	public void pulisciForm() {
		oggettoTextBox.setText(null);
		mittenteOriginaleTextBox.setText(null);
		dataFrom.setValue(null);
		dataFrom.getDatePicker().setValue(null);
		dataTo.setValue(null);
		dataTo.getDatePicker().setValue(null);
		dataScadenzaFrom.setValue(null);
		dataScadenzaFrom.getDatePicker().setValue(null);
		dataScadenzaTo.setValue(null);
		dataScadenzaTo.getDatePicker().setValue(null);
		tipoRichiestaSuggestBox.setValue(null);
		gruppoProponenteSuggestBox.setValue(null);
		pulisciFormAvanzate();
		resetMessageWidget();
		salvaFiltri.setValue(false);
		loadPreferenzeFiltriDiRicerca();
		gestioneFiltri();
	}

	public void pulisciFormAvanzate() {
		idDocumentaleTextBox.setText(null);
		titoloFascicoloTextBox.setText(null);
		tipologiaFascicoloSuggestBox.setValue(null);
		statoSuggestBox.setValue(null);
		statoDestinatarioSuggestBox.setValue(null);
	}

	private void configuraHandlerCampi() {
		configuraHandlerCampo(oggettoTextBox);
		configuraHandlerCampo(mittenteOriginaleTextBox);
		configuraHandlerCampo(dataFrom);
		configuraHandlerCampo(dataTo);
		configuraHandlerCampo(dataScadenzaFrom);
		configuraHandlerCampo(dataScadenzaTo);
		configuraHandlerCampo(gruppoProponenteSuggestBox);
		configuraHandlerCampo(tipoRichiestaSuggestBox);
		configuraHandlerCampo(tipoStatoSuggestBox);
		configuraHandlerCampo(tipologiaFascicoloSuggestBox);
		configuraHandlerCampo(titoloFascicoloTextBox);
		configuraHandlerCampo(idDocumentaleTextBox);
		configuraHandlerCampo(statoSuggestBox);
		configuraHandlerCampo(statoDestinatarioSuggestBox);
	}

	private void gestioneFiltri() {

		/*
		 * Oggetto
		 */
		if (oggettoTextBox.getText() == null || oggettoTextBox.getText().trim().isEmpty()) {
			if (filtriValorizzati.contains("oggettoTextBox")) filtriValorizzati.remove("oggettoTextBox");
		} else {
			if (!filtriValorizzati.contains("oggettoTextBox")) filtriValorizzati.add("oggettoTextBox");
		}

		/*
		 * Mittente
		 */
		if (mittenteOriginaleTextBox.getText() == null || mittenteOriginaleTextBox.getText().trim().isEmpty()) {
			if (filtriValorizzati.contains("mittenteOriginaleTextBox")) filtriValorizzati.remove("mittenteOriginaleTextBox");
		} else {
			if (!filtriValorizzati.contains("mittenteOriginaleTextBox")) filtriValorizzati.add("mittenteOriginaleTextBox");
		}

		/*
		 * Data from
		 */
		if (dataFrom.getValue() == null || Strings.isNullOrEmpty(dateTimeFormat.format(dataFrom.getDatePicker().getValue()))) {
			if (filtriValorizzati.contains("dataFrom")) filtriValorizzati.remove("dataFrom");
		} else {
			if (!filtriValorizzati.contains("dataFrom")) filtriValorizzati.add("dataFrom");
		}

		/*
		 * Data to
		 */
		if (dataTo.getValue() == null || Strings.isNullOrEmpty(dateTimeFormat.format(dataTo.getDatePicker().getValue()))) {
			if (filtriValorizzati.contains("dataTo")) filtriValorizzati.remove("dataTo");
		} else {
			if (!filtriValorizzati.contains("dataTo")) filtriValorizzati.add("dataTo");
		}

		/*
		 * Data scadenza from
		 */
		if (dataScadenzaFrom.getValue() == null || Strings.isNullOrEmpty(dateTimeFormat.format(dataScadenzaFrom.getDatePicker().getValue()))) {
			if (filtriValorizzati.contains("dataScadenzaFrom")) filtriValorizzati.remove("dataScadenzaFrom");
		} else {
			if (!filtriValorizzati.contains("dataScadenzaFrom")) filtriValorizzati.add("dataScadenzaFrom");
		}

		/*
		 * Data scadenza to
		 */
		if (dataScadenzaTo.getValue() == null || Strings.isNullOrEmpty(dateTimeFormat.format(dataScadenzaTo.getDatePicker().getValue()))) {
			if (filtriValorizzati.contains("dataScadenzaTo")) filtriValorizzati.remove("dataScadenzaTo");
		} else {
			if (!filtriValorizzati.contains("dataScadenzaTo")) filtriValorizzati.add("dataScadenzaTo");
		}

		/*
		 * Gruppo proponente
		 */
		if (gruppoProponenteSuggestBox.getValue() == null || gruppoProponenteSuggestBox.getValue().trim().isEmpty()) {
			if (filtriValorizzati.contains("gruppoProponenteSuggestBox")) filtriValorizzati.remove("gruppoProponenteSuggestBox");
		} else {
			if (!filtriValorizzati.contains("gruppoProponenteSuggestBox")) filtriValorizzati.add("gruppoProponenteSuggestBox");
		}

		/*
		 * Tipo richiesta
		 */
		if (tipoRichiestaSuggestBox.getValue() == null || tipoRichiestaSuggestBox.getValue().trim().isEmpty()) {
			if (filtriValorizzati.contains("tipoRichiestaSuggestBox")) filtriValorizzati.remove("tipoRichiestaSuggestBox");
		} else {
			if (!filtriValorizzati.contains("tipoRichiestaSuggestBox")) filtriValorizzati.add("tipoRichiestaSuggestBox");
		}

		/*
		 * Tipo stato
		 */
		if (tipoStatoSuggestBox.getValue() == null || tipoStatoSuggestBox.getValue().trim().isEmpty()) {
			if (filtriValorizzati.contains("tipoStatoSuggestBox")) filtriValorizzati.remove("tipoStatoSuggestBox");
		} else {
			if (!filtriValorizzati.contains("tipoStatoSuggestBox")) filtriValorizzati.add("tipoStatoSuggestBox");
		}

		/*
		 * Tipo fascicolo
		 */
		if (tipologiaFascicoloSuggestBox.getValue() == null || tipologiaFascicoloSuggestBox.getValue().trim().isEmpty()) {
			if (filtriValorizzati.contains("tipologiaFascicoloSuggestBox")) filtriValorizzati.remove("tipologiaFascicoloSuggestBox");
		} else {
			if (!filtriValorizzati.contains("tipologiaFascicoloSuggestBox")) filtriValorizzati.add("tipologiaFascicoloSuggestBox");
		}

		/*
		 * Titolo fascicolo
		 */
		if (titoloFascicoloTextBox.getValue() == null || titoloFascicoloTextBox.getValue().trim().isEmpty()) {
			if (filtriValorizzati.contains("titoloFascicoloTextBox")) filtriValorizzati.remove("titoloFascicoloTextBox");
		} else {
			if (!filtriValorizzati.contains("titoloFascicoloTextBox")) filtriValorizzati.add("titoloFascicoloTextBox");
		}

		/*
		 * Id documentale
		 */
		if (idDocumentaleTextBox.getValue() == null || idDocumentaleTextBox.getValue().trim().isEmpty()) {
			if (filtriValorizzati.contains("idDocumentaleTextBox")) filtriValorizzati.remove("idDocumentaleTextBox");
		} else {
			if (!filtriValorizzati.contains("idDocumentaleTextBox")) filtriValorizzati.add("idDocumentaleTextBox");
		}

		/*
		 * Stato
		 */
		if (statoSuggestBox.getValue() == null || statoSuggestBox.getValue().trim().isEmpty()) {
			if (filtriValorizzati.contains("statoSuggestBox")) filtriValorizzati.remove("statoSuggestBox");
		} else {
			if (!filtriValorizzati.contains("statoSuggestBox")) filtriValorizzati.add("statoSuggestBox");
		}

		/*
		 * Stato destinatario
		 */
		if (statoDestinatarioSuggestBox.getValue() == null || statoDestinatarioSuggestBox.getValue().trim().isEmpty()) {
			if (filtriValorizzati.contains("statoDestinatarioSuggestBox")) filtriValorizzati.remove("statoDestinatarioSuggestBox");
		} else {
			if (!filtriValorizzati.contains("statoDestinatarioSuggestBox")) filtriValorizzati.add("statoDestinatarioSuggestBox");
		}

		filtraButton.setText("Filtra" + (filtriValorizzati.size() > 0 ? " (" + filtriValorizzati.size() + ")" : ""));
	}

	private void configuraHandlerCampo(final Widget widget) {
		widget.addDomHandler(invioButton, KeyDownEvent.getType());
	}

	private void prevalorizzaCampi() {
		setPlaceholder(oggettoTextBox, "Oggetto proposta");
		setPlaceholder(mittenteOriginaleTextBox, "Mittente originale");
		setPlaceholder(dataFrom, "Data proposta da");
		setPlaceholder(dataTo, "Data proposta a");
		setPlaceholder(dataScadenzaFrom, "Data scadenza proposta da");
		setPlaceholder(dataScadenzaTo, "Data scadenza proposta a");
		setPlaceholder(gruppoProponenteSuggestBox, "Proponente");
		setPlaceholder(tipoRichiestaSuggestBox, "Tipo proposta");
		setPlaceholder(tipologiaFascicoloSuggestBox, "Tipologia fascicolo");
		setPlaceholder(titoloFascicoloTextBox, "Titolo fascicolo");
		setPlaceholder(idDocumentaleTextBox, "ID documentale");
		setPlaceholder(tipoStatoSuggestBox, "Stato proposta");
		setPlaceholder(statoSuggestBox, "Stato di dettaglio");
		setPlaceholder(statoDestinatarioSuggestBox, "Stato destinatario");
	}

	private void setPlaceholder(Widget widget, String placeholder) {
		InputElement inputElement = widget.getElement().cast();
		inputElement.setAttribute("placeholder", placeholder);
	}

	private void initRicercaRadioButton() {
		ricercaDaDestinatario.setYesNoRadioButtonCommand(new YesNoRadioButtonCommand() {

			@Override
			public void execute(Boolean value) {
				if (value) {
					initGruppoProponenteSuggestBox(configurazioniHandler.getAnagraficheRuoli());
				} else {
					List<AnagraficaRuolo> ruoliPersonaliESubordinati = new ArrayList<AnagraficaRuolo>();
					ruoliPersonaliESubordinati.addAll(profilazioneUtenteHandler.getAnagraficheRuoloUtente());
					ruoliPersonaliESubordinati.addAll(profilazioneUtenteHandler.getAnagraficheRuoliSubordinati(VisibilitaRuoloAbilitazione.class));
					initGruppoProponenteSuggestBox(ruoliPersonaliESubordinati);
				}
			}
		});
	}

	private void manageStatoDettaglio(TipoStatoTaskFirmaDTO tipoStato, TipoPropostaTaskFirmaDTO tipoProposta) {
		List<String> statiDettaglio = Lists.transform(StatoTaskFirmaDTO.intersect(tipoStato, tipoProposta), new Function<StatoTaskFirmaDTO, String>() {

			@Override
			public String apply(StatoTaskFirmaDTO input) {
				return input.getLabel();
			}
		});
		String value = null;
		if (statiDettaglio.contains(statoSuggestBox.getValue())) {
			value = statoSuggestBox.getValue();
		}

		SpacebarSuggestOracle sso = (SpacebarSuggestOracle) statoSuggestBox.getSuggestOracle();
		sso.setSuggestions(statiDettaglio);

		statoSuggestBox.setValue(value);
	}

	private void manageStatoDestinatario(TipoPropostaTaskFirmaDTO tipoProposta) {
		List<StatoDestinatarioTaskFirmaDTO> statiDestinatario = tipoProposta != null ? StatoDestinatarioTaskFirmaDTO.fromTipoProposta(tipoProposta)
				: Arrays.asList(StatoDestinatarioTaskFirmaDTO.values());
		List<String> statiDestinatarioLabel = Lists.transform(statiDestinatario, new Function<StatoDestinatarioTaskFirmaDTO, String>() {

			@Override
			public String apply(StatoDestinatarioTaskFirmaDTO input) {
				return input.getLabel();
			}
		});

		String value = null;
		if (statiDestinatarioLabel.contains(statoDestinatarioSuggestBox.getValue())) value = statoDestinatarioSuggestBox.getValue();

		SpacebarSuggestOracle sso = (SpacebarSuggestOracle) statoDestinatarioSuggestBox.getSuggestOracle();
		sso.setSuggestions(statiDestinatarioLabel);

		statoDestinatarioSuggestBox.setValue(value);
	}

	private void initTipoStatoRichiestaSuggestBox() {
		tipoStatoSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				TipoStatoTaskFirmaDTO sf = getTipoStatoRichiesta();
				TipoPropostaTaskFirmaDTO tp = getTipoProposta();
				manageStatoDettaglio(sf, tp);
			}
		});

		tipoStatoSuggestBox.getValueBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				TipoStatoTaskFirmaDTO sf = getTipoStatoRichiesta();
				TipoPropostaTaskFirmaDTO tp = getTipoProposta();
				manageStatoDettaglio(sf, tp);
			}
		});
	}

	private void initTipoPropostaSuggestBox() {
		tipoRichiestaSuggestBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {

			@Override
			public void onSelection(SelectionEvent<Suggestion> event) {
				TipoStatoTaskFirmaDTO sf = getTipoStatoRichiesta();
				TipoPropostaTaskFirmaDTO tp = getTipoProposta();
				manageStatoDettaglio(sf, tp);
				manageStatoDestinatario(tp);
			}
		});

		tipoRichiestaSuggestBox.getValueBox().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				TipoStatoTaskFirmaDTO sf = getTipoStatoRichiesta();
				TipoPropostaTaskFirmaDTO tp = getTipoProposta();
				manageStatoDettaglio(sf, tp);
				manageStatoDestinatario(tp);
			}
		});
	}

	private void loadPreferenzeFiltriDiRicerca() {
		if (profilazioneUtenteHandler.getPreferenzeUtente() != null && profilazioneUtenteHandler.getPreferenzeUtente().getPreferenzeCartellaAttivita() != null) {
			PreferenzeCartellaAttivita preferenzeCartellaFirma = profilazioneUtenteHandler.getPreferenzeUtente().getPreferenzeCartellaAttivita();

			if (preferenzeCartellaFirma.getTipoRicerca() != null) {
				switch (preferenzeCartellaFirma.getTipoRicerca()) {
				case DESTINATARIO:
					ricercaDaDestinatario.selectYes();
					initGruppoProponenteSuggestBox(configurazioniHandler.getAnagraficheRuoli());
					break;

				case PROPONENTE:
					ricercaDaDestinatario.selectNo();
					List<AnagraficaRuolo> ruoliPersonaliESubordinati = new ArrayList<AnagraficaRuolo>();
					ruoliPersonaliESubordinati.addAll(profilazioneUtenteHandler.getAnagraficheRuoloUtente());
					ruoliPersonaliESubordinati.addAll(profilazioneUtenteHandler.getAnagraficheRuoliSubordinati(VisibilitaRuoloAbilitazione.class));
					initGruppoProponenteSuggestBox(ruoliPersonaliESubordinati);
					break;

				default:
					ricercaDaDestinatario.selectYes();
					initGruppoProponenteSuggestBox(configurazioniHandler.getAnagraficheRuoli());
					break;
				}
			} else {
				ricercaDaDestinatario.selectYes();
				initGruppoProponenteSuggestBox(configurazioniHandler.getAnagraficheRuoli());
			}

			if (preferenzeCartellaFirma.getStatoFinaleRicerca() != null) {
				switch (preferenzeCartellaFirma.getStatoFinaleRicerca()) {
				case IN_APPROVAZIONE:
					tipoStatoSuggestBox.setValue(TipoStatoTaskFirmaDTO.IN_APPROVAZIONE.getLabel());
					manageStatoDettaglio(TipoStatoTaskFirmaDTO.IN_APPROVAZIONE, getTipoProposta());
					break;

				case CONCLUSO:
					tipoStatoSuggestBox.setValue(TipoStatoTaskFirmaDTO.CONCLUSO.getLabel());
					manageStatoDettaglio(TipoStatoTaskFirmaDTO.CONCLUSO, getTipoProposta());
					break;

				case EVASO:
					tipoStatoSuggestBox.setValue(TipoStatoTaskFirmaDTO.EVASO.getLabel());
					manageStatoDettaglio(TipoStatoTaskFirmaDTO.EVASO, getTipoProposta());
					break;

				default:
					tipoStatoSuggestBox.setValue(TipoStatoTaskFirmaDTO.IN_APPROVAZIONE.getLabel());
					manageStatoDettaglio(TipoStatoTaskFirmaDTO.IN_APPROVAZIONE, getTipoProposta());
					break;
				}
			} else {
				tipoStatoSuggestBox.setValue(TipoStatoTaskFirmaDTO.IN_APPROVAZIONE.getLabel());
				manageStatoDettaglio(TipoStatoTaskFirmaDTO.IN_APPROVAZIONE, getTipoProposta());
			}
		} else {
			ricercaDaDestinatario.selectYes();
			tipoStatoSuggestBox.setValue(TipoStatoTaskFirmaDTO.IN_APPROVAZIONE.getLabel());
			initGruppoProponenteSuggestBox(configurazioniHandler.getAnagraficheRuoli());
			manageStatoDettaglio(TipoStatoTaskFirmaDTO.IN_APPROVAZIONE, getTipoProposta());
		}
	}

	private void initTipologiaFascicoloSuggestBox() {
		List<AnagraficaFascicolo> copy = new ArrayList<>(configurazioniHandler.getAnagraficheFascicoli(true));
		Collections.sort(copy, new Comparator<AnagraficaFascicolo>() {
			@Override
			public int compare(AnagraficaFascicolo o1, AnagraficaFascicolo o2) {
				if (o1 == null || o2 == null) return 0;
				return o1.getEtichettaTipologia().compareTo(o2.getEtichettaTipologia());
			}
		});
		tipologiaFascicoloSuggestBox.setVisible(false);
		SpacebarSuggestOracle spacebarSuggestOracle = (SpacebarSuggestOracle) tipologiaFascicoloSuggestBox.getSuggestOracle();
		spacebarSuggestOracle.setSuggestions(new ArrayList<String>(new TreeSet<String>(Lists.transform(copy, new Function<AnagraficaFascicolo, String>() {
			@Override
			public String apply(AnagraficaFascicolo input) {
				return input.getEtichettaTipologia();
			}
		}))));
		tipologiaFascicoloSuggestBox.setVisible(true);
	}

	private void initGruppoProponenteSuggestBox(List<AnagraficaRuolo> ruoli) {
		AnagraficheRuoliSuggestOracle so = (AnagraficheRuoliSuggestOracle) this.gruppoProponenteSuggestBox.getSuggestOracle();
		so.setAnagraficheRuoli(ruoli);
	}

	public List<String> getFiltroDiRicerca(CercaDocumentoFirmaVistoAction action) {
		return cartellaFirmaRicercaApiClient.creaFiltroDiRicerca(this, action);
	}

	public String getOggetto() {
		return GenericsUtil.safeTrim(oggettoTextBox.getText());
	}

	public String getMittenteOriginale() {
		return GenericsUtil.safeTrim(mittenteOriginaleTextBox.getText());
	}

	public AnagraficaRuolo getProponente() {
		AnagraficheRuoliSuggestOracle oracle = (AnagraficheRuoliSuggestOracle) gruppoProponenteSuggestBox.getSuggestOracle();
		return oracle.getAnagraficaRuolo(gruppoProponenteSuggestBox.getValue());
	}

	public String getDataFrom() {
		if (dataFrom.getValue() != null) return this.dateTimeFormat.format(dataFrom.getDatePicker().getValue());
		else return null;
	}

	public String getDataTo() {
		if (dataTo.getValue() != null) return this.dateTimeFormat.format(dataTo.getDatePicker().getValue());
		else return null;
	}

	public String getDataScadenzaFrom() {
		if (dataScadenzaFrom.getValue() != null) return this.dateTimeFormat.format(dataScadenzaFrom.getDatePicker().getValue());
		else return null;
	}

	public String getDataScadenzaTo() {
		if (dataScadenzaTo.getValue() != null) return this.dateTimeFormat.format(dataScadenzaTo.getDatePicker().getValue());
		else return null;
	}

	public String getIdDocumentale() {
		return GenericsUtil.safeTrim(idDocumentaleTextBox.getText());
	}

	public String getTitoloFascicolo() {
		return GenericsUtil.safeTrim(titoloFascicoloTextBox.getText());
	}

	public TipoStatoTaskFirmaDTO getTipoStatoRichiesta() {
		return TipoStatoTaskFirmaDTO.fromValue(tipoStatoSuggestBox.getValue());
	}

	public TipologiaPratica getTipologiaPratica() {
		AnagraficaFascicolo af = configurazioniHandler.getAnagraficaFascicoloByEtichetta(tipologiaFascicoloSuggestBox.getValue());
		return PraticaUtil.toTipologiaPratica(af);
	}

	public StatoTaskFirmaDTO getStatoDiDettaglio() {
		return StatoTaskFirmaDTO.fromLabel(statoSuggestBox.getValue());
	}

	public StatoDestinatarioTaskFirmaDTO getStatoDestinatario() {
		return StatoDestinatarioTaskFirmaDTO.fromLabel(statoDestinatarioSuggestBox.getValue());
	}

	public TipoPropostaTaskFirmaDTO getTipoProposta() {
		return TipoPropostaTaskFirmaDTO.fromLabel(tipoRichiestaSuggestBox.getValue());
	}

	public boolean isAvanzatePanel() {
		return avanzatePanel.isVisible();
	}

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
			messageWidgetPanel.setVisible(true);

		} else {
			messageWidgetPanel.setVisible(false);
			messageWidget.reset();
		}
	}

	public boolean isDestinatario() {
		return ricercaDaDestinatario.getValue();
	}

	public boolean isSalvaFiltri() {
		return salvaFiltri.getValue();
	}

	public PreferenzeCartellaAttivita getPreferenzeFiltriRicerca() {
		PreferenzeCartellaAttivita preferenzeCartellaFirmaDTO = new PreferenzeCartellaAttivita();
		TipoUtenteTaskFirmaDTO tipoRicerca = isDestinatario() ? TipoUtenteTaskFirmaDTO.DESTINATARIO : TipoUtenteTaskFirmaDTO.PROPONENTE;
		preferenzeCartellaFirmaDTO.setTipoRicerca(TipoRicerca.valueOf(tipoRicerca.name()));
		if (getTipoStatoRichiesta() != null) {
			TipoStatoTaskFirmaDTO statoRichiesta = getTipoStatoRichiesta();
			preferenzeCartellaFirmaDTO.setStatoFinaleRicerca(StatoFinaleRicerca.valueOf(statoRichiesta.name()));
		}

		return preferenzeCartellaFirmaDTO;
	}
}
