package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.gwt.validation.client.impl.Validation;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import lombok.Getter;
import lombok.Setter;

public abstract class FormRicercaBase extends Composite {
	@UiField
	TextBox provenienzaTextBox;
	@UiField
	DateBox dataFrom;
	@UiField
	DateBox dataTo;
	@UiField
	Button pulisciButton;
	@UiField
	Button cercaButton;
	@UiField
	TextBox titoloTextBox;
	@UiField
	TextBox annoPgTextBox;
	@UiField
	TextBox numeroPgTextBox;
	@UiField
	TextBox idDocumentaleTextBox;
	@UiField(provided = true)
	SuggestBox assegnatarioSuggestBox = new SuggestBox(new AnagraficheRuoliSuggestOracle(new ArrayList<AnagraficaRuolo>()));

	/* pannello avanzate */
	@UiField
	HTMLPanel chiudiAvanzatePanel;
	@UiField
	HTMLPanel avanzatePanel;
	@UiField
	Button chiudiAvanzatePanelButton;
	@UiField
	Button avanzateButton;
	@UiField
	MessageAlertWidget messageWidget;
	@UiField
	HTMLPanel messageWidgetPanel;
	@UiField
	CheckBox daLeggereCheckBox;

	/* pannello estensione form */
	@UiField
	protected HTMLPanel gruppoFiltriFissiPanel;
	protected List<AbstractGruppoFiltriWidget> listFiltri = new ArrayList<AbstractGruppoFiltriWidget>();
	protected final ConfigurazioneCampiPerTipo CONFIG_CAMPI_BASE = new ConfigurazioneCampiPerTipo();
	protected KeyDownHandler invioBt;
	protected List<TipologiaPratica> tipoPraticheAbilitate = new ArrayList<TipologiaPratica>();

	protected Command pulisciCommand;

	public FormRicercaBase() {
		super();
	}

	public void configura(final com.google.gwt.user.client.Command cercaCommand) {
		invioBt = new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					cercaCommand.execute();
				}
			}
		};
		cercaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				resetMessageWidget();
				cercaCommand.execute();
			}
		});
		pulisciButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				pulisci();
				if (pulisciCommand != null) {
					pulisciCommand.execute();
				}
			}
		});

		avanzateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (avanzatePanel.isVisible()) {
					avanzatePanel.setVisible(false);
					// resetAvanzate();
				} else {
					avanzatePanel.setVisible(true);
				}

				chiudiAvanzatePanel.setVisible(chiudiAvanzatePanel.isVisible() ? false : true);
			}
		});

		chiudiAvanzatePanelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				avanzatePanel.setVisible(false);
				chiudiAvanzatePanel.setVisible(false);
				// resetAvanzate();
			}
		});
		configuraHandlerCampo(titoloTextBox);
		configuraHandlerCampo(annoPgTextBox);
		configuraHandlerCampo(numeroPgTextBox);
		configuraHandlerCampo(dataFrom);
		configuraHandlerCampo(dataTo);
		configuraHandlerCampo(idDocumentaleTextBox);
		configuraHandlerCampo(provenienzaTextBox);
		configuraHandlerCampo(assegnatarioSuggestBox);

		/* configurazione di base dei placeholder */
		applicaConfigurazionePerTipo(CONFIG_CAMPI_BASE);

		Format dateFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA));
		dataFrom.setFormat(dateFormat);
		dataFrom.getDatePicker().setYearArrowsVisible(true);
		dataTo.setFormat(dateFormat);
		dataTo.getDatePicker().setYearArrowsVisible(true);
		daLeggereCheckBox.setText(" Da leggere");
	}

	public void resetForm() {
		titoloTextBox.setText(null);
		annoPgTextBox.setText(null);
		numeroPgTextBox.setText(null);
		// assegnatoATextBox.setText(null);
		dataFrom.setValue(null);
		dataFrom.getDatePicker().setValue(null);
		dataTo.getDatePicker().setValue(null);
		dataTo.setValue(null);
		resetMessageWidget();
		resetAvanzate();
	}

	public void addGruppoFiltriFissi(AbstractGruppoFiltriWidget widget) {
		listFiltri.add(widget);
		DisclosurePanel dp = new DisclosurePanel(OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), widget.getDescrizione());
		dp.add(widget);
		dp.setStyleName("disclosure-ricerca");
		widget.setDisclosurePanel(dp);
		gruppoFiltriFissiPanel.add(dp);
		gruppoFiltriFissiPanel.setVisible(true);

	}

	protected void resetAvanzate() {
		idDocumentaleTextBox.setText(null);
		assegnatarioSuggestBox.setText(null);
		provenienzaTextBox.setText(null);
		daLeggereCheckBox.setValue(false);
		for (AbstractGruppoFiltriWidget widget : listFiltri) {
			if (widget.isAttached())
				widget.reset();
		}
	}

	public void resetMessageWidget() {
		messageWidgetPanel.setVisible(false);
		messageWidget.reset();
	}

	public final Set<ConstraintViolation<CercaPratiche>> serializzaEValida(CercaPratiche dto) {
		serializza(dto);
		/* validazione */
		Set<ConstraintViolation<CercaPratiche>> violations = valida(dto);
		if (violations.size() > 0)
			this.showErrors(violations);
		return violations;
	}

	private void showErrors(Set<ConstraintViolation<CercaPratiche>> violations) {
		if (violations.size() > 0) {
			SafeHtmlBuilder sb = new SafeHtmlBuilder();
			sb.appendHtmlConstant("<ul>");
			for (ConstraintViolation<CercaPratiche> violation : violations) {
				sb.appendHtmlConstant("<li>");
				sb.appendEscaped(violation.getMessage());
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

	protected void configuraHandlerCampo(Widget w) {
		w.addDomHandler(invioBt, KeyDownEvent.getType());
	}

	protected void setLabelCampo(Widget w, String placeholder) {
		InputElement inputElement = w.getElement().cast();
		inputElement.setAttribute("placeholder", placeholder);
	}

	protected void serializza(CercaPratiche dto) {
		DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
		dto.setTitolo(titoloTextBox.getText());
		dto.setAnno(annoPgTextBox.getText());
		dto.setNumero(numeroPgTextBox.getText());

		if (dataRicezione) {
			if (dataFrom.getValue() != null)
				dto.setDataRicezioneFrom(dtf.format(dataFrom.getDatePicker().getValue()));
			else
				dto.setDataRicezioneFrom(null);
			if (dataTo.getValue() != null)
				dto.setDataRicezioneTo(dtf.format(dataTo.getDatePicker().getValue()));
			else
				dto.setDataRicezioneTo(null);

		} else {
			if (dataFrom.getValue() != null)
				dto.setDataCreazioneFrom(dtf.format(dataFrom.getDatePicker().getValue()));
			else
				dto.setDataCreazioneFrom(null);
			if (dataTo.getValue() != null)
				dto.setDataCreazioneTo(dtf.format(dataTo.getDatePicker().getValue()));
			else
				dto.setDataCreazioneTo(null);
		}

		dto.setIdDocumentale(idDocumentaleTextBox.getText());
		dto.setProvenienza(provenienzaTextBox.getText());
		dto.setDaLeggere(daLeggereCheckBox.getValue());
		/* serializzo le ulteriori widget filtri */
		for (AbstractGruppoFiltriWidget w : listFiltri) {
			if (w.isAttached())
				w.serializza(dto);
		}

		/* recupero l'assegnatario */
		String assegnatario = assegnatarioSuggestBox.getText();
		if (assegnatario != null) {
			AnagraficheRuoliSuggestOracle oracle = (AnagraficheRuoliSuggestOracle) assegnatarioSuggestBox.getSuggestOracle();
			for (AnagraficaRuolo ruolo : oracle.getAnagraficheRuoli()) {
				if (assegnatario.equals(ruolo.getEtichetta())) {
					List<String> arrayList = new ArrayList<String>();
					arrayList.add(ruolo.getRuolo());
					dto.setAssegnatoA(arrayList);
					break;
				}
			}
		}

	}

	protected Set<ConstraintViolation<CercaPratiche>> valida(CercaPratiche dto) {
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<CercaPratiche>> violations = validator.validate(dto);
		return violations;
	}

	private boolean dataRicezione = false;

	protected void applicaConfigurazionePerTipo(ConfigurazioneCampiPerTipo config) {

		dataRicezione = TipologiaPratica.EMAIL_IN.equals(config.getTipologiaPratica());

		setLabelCampo(this.annoPgTextBox, config.getTitoloAnnoPG());
		setLabelCampo(this.numeroPgTextBox, config.getTitoloNumeroPG());
		setLabelCampo(this.titoloTextBox, config.getTitoloTitolo());
		setLabelCampo(this.dataFrom, config.getTitoloDataDa());
		setLabelCampo(this.dataTo, config.getTitoloDataA());
		setLabelCampo(this.provenienzaTextBox, config.getTitoloProvenienza());
		setLabelCampo(this.idDocumentaleTextBox, config.getTitoloIdDocumentale());
		setLabelCampo(this.assegnatarioSuggestBox, config.getTitoloAssegnatario());

	}

	protected void pulisci() {
		resetForm();
		resetAvanzate();
	}

	public void setGruppiAbilitati(List<AnagraficaRuolo> gruppiAbilitati) {
		AnagraficheRuoliSuggestOracle so = (AnagraficheRuoliSuggestOracle) this.assegnatarioSuggestBox.getSuggestOracle();
		so.setAnagraficheRuoli(gruppiAbilitati);
	}

	public void setElencoTipoPratiche(List<TipologiaPratica> tipoPraticheAbilitate) {
		this.tipoPraticheAbilitate = tipoPraticheAbilitate;
	}

	public void setPulisciCommand(Command pulisciCommand) {
		this.pulisciCommand = pulisciCommand;
	}

	/* classi interne */
	@Getter
	@Setter
	public static final class ConfigurazioneCampiPerTipo {
		private String titoloNumeroPG, titoloAnnoPG, titoloTitolo, titoloDataDa, titoloDataA, titoloProvenienza, titoloIdDocumentale, titoloAssegnatario, titoloNomeTemplate,
				titoloDestinatarioAssegnaUtenteEsterno;
		private TipologiaPratica tipologiaPratica;

		protected ConfigurazioneCampiPerTipo() {
			setTitoloTitolo("Titolo");
			setTitoloAnnoPG("Anno P.G.");
			setTitoloNumeroPG("Numero P.G.");
			setTitoloDataDa("Data creazione da");
			setTitoloDataA("Data creazione a");
			setTitoloIdDocumentale("ID documentale");
			setTitoloProvenienza("Provenienza");
			setTitoloAssegnatario("Assegnatario");
			setTitoloAssegnatario("Assegnatario");
			setTitoloNomeTemplate("Nome");
			setTitoloDestinatarioAssegnaUtenteEsterno("Destinatario Inoltro");
		}

		public ConfigurazioneCampiPerTipo(TipologiaPratica tipologiaPratica) {
			this();
			this.setTipologiaPratica(tipologiaPratica);
		}
	}
}
