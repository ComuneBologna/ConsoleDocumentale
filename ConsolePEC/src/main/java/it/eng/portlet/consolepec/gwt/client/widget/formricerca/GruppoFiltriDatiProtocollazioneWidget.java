package it.eng.portlet.consolepec.gwt.client.widget.formricerca;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;

import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.handler.profilazione.ProfilazioneUtenteHandler;
import it.eng.portlet.consolepec.gwt.client.widget.CustomSuggestBox;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.action.CercaPratiche;
import it.eng.portlet.consolepec.gwt.shared.model.TipoProtocollazione;

public class GruppoFiltriDatiProtocollazioneWidget extends AbstractGruppoFiltriWidget {

	private static FormRicercaDatiProtocollazioneUiBinder uiBinder = GWT.create(FormRicercaDatiProtocollazioneUiBinder.class);

	interface FormRicercaDatiProtocollazioneUiBinder extends UiBinder<Widget, GruppoFiltriDatiProtocollazioneWidget> {
		//
	}

	@UiField
	TextBox numeroFascicoloTextBox;
	@UiField
	TextBox annoRegistroTextBox;
	@UiField
	TextBox numeroRegistroTextBox;
	@UiField
	TextBox oggettoTextBox;
	@UiField
	TextBox riferimentoProvenienzaTextBox;
	@UiField
	TextBox provenienzaTextBox;
	@UiField
	TextBox cfProvenienzaTextBox;
	@UiField
	DateBox dataDocumentoProtocollazione;
	@UiField
	HTMLPanel tipologiaDelDocumentoHTMLPanel;
	@UiField
	HTMLPanel titoloDocumentoHTMLPanel;
	@UiField
	HTMLPanel rubricaProtocolloHTMLPanel;
	@UiField
	HTMLPanel sezioneProtocolloHTMLPanel;
	@UiField(provided = true)
	SuggestBox tipoProt;

	private CustomSuggestBox tipologiaDelDocumentoSuggestBox = null;
	private CustomSuggestBox titoloDocumentoSuggestBox = null;
	private CustomSuggestBox rubricaProtocolloSuggestBox = null;
	private CustomSuggestBox sezioneProtocolloSuggestBox = null;

	public GruppoFiltriDatiProtocollazioneWidget(ProfilazioneUtenteHandler profilazioneUtenteHandler, ConfigurazioniHandler configurazioniHandler) {
		this.profilazioneUtenteHandler = profilazioneUtenteHandler;
		this.configurazioniHandler = configurazioniHandler;
		initSuggestBoxTipoProtocollazione();
		initWidget(uiBinder.createAndBindUi(this));
	}

	public void configura(final Command cercaCommand, CustomSuggestBox tipologiaDelDocumentoSuggestBox, CustomSuggestBox titoloDocumentoSuggestBox, CustomSuggestBox rubricaProtocolloSuggestBox,
			CustomSuggestBox sezioneProtocolloSuggestBox) {
		super.configura(cercaCommand);
		configuraCampo(numeroFascicoloTextBox, "Numero fascicolo");
		configuraCampo(annoRegistroTextBox, "Anno registro");
		configuraCampo(numeroRegistroTextBox, "Numero Registro");
		configuraCampo(oggettoTextBox, "Oggetto");
		configuraCampo(riferimentoProvenienzaTextBox, "Riferimento provenienza");
		configuraCampo(provenienzaTextBox, "Provenienza");
		configuraCampo(cfProvenienzaTextBox, "Cod fis provenienza");
		configuraCampo(dataDocumentoProtocollazione, "Data");
		configuraCampo(tipoProt, "Tipo protocollazione");

		setTipologiaDelDocumentoSuggestBox(tipologiaDelDocumentoSuggestBox);
		setTitoloDocumentoSuggestBox(titoloDocumentoSuggestBox);
		setRubricaProtocolloSuggestBox(rubricaProtocolloSuggestBox);
		setSezioneProtocolloSuggestBox(sezioneProtocolloSuggestBox);

		Format dateFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA));
		dataDocumentoProtocollazione.setFormat(dateFormat);
		dataDocumentoProtocollazione.getDatePicker().setYearArrowsVisible(true);
	}

	@Override
	public void serializza(CercaPratiche dto) {
		DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
		dto.setNumeroFascicolo(numeroFascicoloTextBox.getValue());
		dto.setAnnoRegistro(annoRegistroTextBox.getValue());
		dto.setNumeroRegistro(numeroRegistroTextBox.getValue());

		dto.setOggettoProtocollazione(this.oggettoTextBox.getValue());
		dto.setRiferimentoProvenienzaProtocollazione(riferimentoProvenienzaTextBox.getValue());
		dto.setProvenienzaProtocollazione(provenienzaTextBox.getValue());
		dto.setCFprovenienzaProtocollazione(cfProvenienzaTextBox.getValue());
		if (tipologiaDelDocumentoSuggestBox != null) {
			dto.setTipologiadelDocumentoProtocollazione(tipologiaDelDocumentoSuggestBox.getIdentificativo());
		}
		if (titoloDocumentoSuggestBox != null) {
			dto.setTitoloDocumentoProtocollazione(titoloDocumentoSuggestBox.getIdentificativo());
		}
		if (rubricaProtocolloSuggestBox != null) {
			dto.setRubricaProtocollazione(rubricaProtocolloSuggestBox.getIdentificativo());
		}
		if (sezioneProtocolloSuggestBox != null) {
			dto.setSezioneProtocollazione(sezioneProtocolloSuggestBox.getIdentificativo());
		}

		if (this.dataDocumentoProtocollazione.getValue() != null) {
			dto.setDataProtocollazione(dtf.format(this.dataDocumentoProtocollazione.getValue()));
		} else {
			dto.setDataProtocollazione(null);
		}
		if (tipoProt.getText() != null) {
			dto.setTipoProtocollazione(TipoProtocollazione.fromLabel(tipoProt.getText().trim()));
		}
	}

	@Override
	public String getDescrizione() {
		return "Ricerca per dati di protocollazione";
	}

	@Override
	public void reset() {
		oggettoTextBox.setValue(null);
		riferimentoProvenienzaTextBox.setValue(null);
		provenienzaTextBox.setValue(null);
		cfProvenienzaTextBox.setValue(null);
		dataDocumentoProtocollazione.setValue(null);
		tipologiaDelDocumentoSuggestBox.setValue(null);
		titoloDocumentoSuggestBox.setValue(null);
		rubricaProtocolloSuggestBox.setValue(null);
		sezioneProtocolloSuggestBox.setValue(null);
		numeroFascicoloTextBox.setValue(null);
		annoRegistroTextBox.setValue(null);
		numeroRegistroTextBox.setValue(null);
		tipoProt.setValue(null);

	}

	private void setTipologiaDelDocumentoSuggestBox(CustomSuggestBox s) {
		tipologiaDelDocumentoHTMLPanel.clear();
		tipologiaDelDocumentoHTMLPanel.add(s);
		s.setStyleName("testo");
		setPlaceholder(s.getElement(), "Tipologia del documento");
		tipologiaDelDocumentoSuggestBox = s;

	}

	private void setTitoloDocumentoSuggestBox(CustomSuggestBox s) {
		titoloDocumentoHTMLPanel.clear();
		titoloDocumentoHTMLPanel.add(s);
		s.setStyleName("testo");
		setPlaceholder(s.getElement(), "Titolo");
		titoloDocumentoSuggestBox = s;
	}

	private void setRubricaProtocolloSuggestBox(CustomSuggestBox s) {
		rubricaProtocolloHTMLPanel.clear();
		rubricaProtocolloHTMLPanel.add(s);
		s.setStyleName("testo");
		setPlaceholder(s.getElement(), "Rubrica");
		this.rubricaProtocolloSuggestBox = s;
	}

	private void setSezioneProtocolloSuggestBox(CustomSuggestBox s) {
		sezioneProtocolloHTMLPanel.clear();
		sezioneProtocolloHTMLPanel.add(s);
		s.setStyleName("testo");
		setPlaceholder(s.getElement(), "Sezione protocollazione");
		this.sezioneProtocolloSuggestBox = s;
	}

	@Override
	public List<TipologiaPratica> getTipiPraticheGestite() {
		return PraticaUtil.toTipologiePratiche(configurazioniHandler.filtraFascicoloPersonale(configurazioniHandler.getAnagraficheFascicoli(true)), configurazioniHandler.getAnagraficheIngressi(true),
				configurazioniHandler.getAnagraficheMailInUscita(true), configurazioniHandler.getAnagraficheComunicazioni(true), configurazioniHandler.getAnagrafichePraticaModulistica(true),
				configurazioniHandler.getAnagraficheModelli(true));
	}

	private void initSuggestBoxTipoProtocollazione() {
		List<String> suggestions = new ArrayList<String>();
		for (TipoProtocollazione tipo : TipoProtocollazione.values()) {
			suggestions.add(tipo.getLabel());
		}
		SpacebarSuggestOracle oracle = new SpacebarSuggestOracle(suggestions);
		tipoProt = new SuggestBox(oracle);
	}
}
