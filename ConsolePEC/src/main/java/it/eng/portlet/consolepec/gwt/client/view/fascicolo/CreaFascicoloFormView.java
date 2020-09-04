package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaFascicolo;
import it.eng.cobo.consolepec.commons.configurazioni.TipologiaPratica;
import it.eng.cobo.consolepec.commons.pratica.datiaggiuntivi.DatoAggiuntivo;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.pratica.PraticaUtil;
import it.eng.portlet.consolepec.gwt.client.handler.configurazioni.ConfigurazioniHandler;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.CreaFascicoloFormPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.CreaFascicoloFormPresenter.AnnullaCreaFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.CreaFascicoloFormPresenter.AvantiCreaFascicoloCommand;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.datiaggiuntivi.FormDatiAggiuntiviWidget;
import it.eng.portlet.consolepec.gwt.shared.model.ValidazioneDatoAggiuntivoDTO;

public class CreaFascicoloFormView extends ViewImpl implements CreaFascicoloFormPresenter.MyView {

	@UiField
	TextBox titoloTextBox;
	@UiField
	TextBox utenteTextBox;
	@UiField
	TextArea noteTextAreaBox;
	@UiField
	DateBox dataCreazionePraticaDateBox;
	@UiField
	Button avantiButton;
	@UiField
	Button annullaButton;
	@UiField
	ListBox tipologiaFascicolo;
	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;
	@UiField
	CheckBox salvaFascicoloDefault;
	@UiField
	HTMLPanel elencoGruppiSuggestBoxPanel;
	@UiField
	HTMLPanel datiAggiuntiviPanel;

	private final Widget widget;
	private FormDatiAggiuntiviWidget formDatiAggiuntiviWidget;
	private Command onChangeStatoFascicoloCommand;
	private ConfigurazioniHandler configurazioniHandler;

	public interface Binder extends UiBinder<Widget, CreaFascicoloFormView> {
		//
	}

	@Inject
	public CreaFascicoloFormView(final Binder binder, final EventBus eventBus, ConfigurazioniHandler configurazioniHandler) {
		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);

		this.configurazioniHandler = configurazioniHandler;

		this.tipologiaFascicolo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				onChangeStatoFascicoloCommand.execute();

			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setAvantiCommand(final AvantiCreaFascicoloCommand avantiCreaFascicoloCommand) {
		this.avantiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				avantiCreaFascicoloCommand.execute();

			}
		});
	}

	@Override
	public void setAnnullaCommand(final AnnullaCreaFascicoloCommand annullaCreaFascicoloCommand) {
		this.annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				annullaCreaFascicoloCommand.execute();
			}
		});

	}

	@Override
	public String getTitolo() {
		return titoloTextBox.getValue();
	}

	@Override
	public String getUtente() {
		return utenteTextBox.getValue();
	}

	@Override
	public void setRequiredField() {

		if (titoloTextBox.getValue().trim().equals("")) {
			titoloTextBox.getElement().setAttribute("required", "required");

		} else {
			titoloTextBox.getElement().removeAttribute("required");
		}
	}

	@Override
	public void svuotaCampi() {
		abilitaTitolo(true, true);
		utenteTextBox.setValue("");
		noteTextAreaBox.setValue("");
		titoloTextBox.getElement().removeAttribute("required");
		formDatiAggiuntiviWidget.resetDatiAggiuntivi();
	}

	@Override
	public void abilitaTitolo(boolean enable, boolean forceClear) {
		if (enable) {
			if (forceClear || "...".equals(titoloTextBox.getValue())) {
				titoloTextBox.setValue("");
			}
			titoloTextBox.removeStyleName("disabilitato");
		} else {
			titoloTextBox.setValue("...");
			titoloTextBox.setStyleName("testo disabilitato");
		}
		titoloTextBox.setEnabled(enable);

	}

	@Override
	public Date getDataCreazionePratica() {
		return dataCreazionePraticaDateBox.getValue();
	}

	@Override
	public void setUtente(String utente) {
		this.utenteTextBox.setValue(utente);
	}

	@Override
	public void clearListBox() {
		this.tipologiaFascicolo.clear();
	}

	@Override
	public TipologiaPratica getTipologiaFascicolo() {
		return PraticaUtil.toTipologiaPratica(configurazioniHandler.getAnagraficaFascicoloByEtichetta(this.tipologiaFascicolo.getItemText(this.tipologiaFascicolo.getSelectedIndex())));
	}

	@Override
	public void setTipoFascicolo(String tipoFascicolo) {
		for (int i = 0; i < this.tipologiaFascicolo.getItemCount(); i++) {
			if (this.tipologiaFascicolo.getItemText(i).equals(tipoFascicolo)) {
				return;
			}
		}
		this.tipologiaFascicolo.addItem(tipoFascicolo);
		this.tipologiaFascicolo.setEnabled(this.tipologiaFascicolo.getItemCount() > 1);
	}

	@Override
	public void setDataCreazionePratica(Date date) {
		this.dataCreazionePraticaDateBox.setValue(date);
	}

	@Override
	public void init() {
		DateTimeFormat f = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
		Format format = new DateBox.DefaultFormat(f);
		this.dataCreazionePraticaDateBox.setFormat(format);

	}

	@Override
	public String getNote() {
		return this.noteTextAreaBox.getValue();
	}

	@Override
	public void hideButtonAnnulla() {
		this.annullaButton.setVisible(false);

	}

	@Override
	public void showButtonAnnulla() {
		this.annullaButton.setVisible(true);
	}

	@Override
	public void setGruppiSuggestBox(SuggestBox gruppiSuggestBox) {
		elencoGruppiSuggestBoxPanel.clear();
		elencoGruppiSuggestBoxPanel.getElement().setInnerHTML("");
		elencoGruppiSuggestBoxPanel.add(gruppiSuggestBox);
	}

	@Override
	public void setAvantiEnabled(boolean enabled) {
		this.avantiButton.setEnabled(enabled);
	}

	@Override
	public void setOnChangeTipoFascicoloCommand(Command onChangeStatoFascicoloCommand) {
		this.onChangeStatoFascicoloCommand = onChangeStatoFascicoloCommand;

	}

	@Override
	public void setTipologiaFascicoloDefault(AnagraficaFascicolo tipoFascicolo) {
		int itemCount = tipologiaFascicolo.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			String value = tipologiaFascicolo.getValue(i);
			if (value.equals(tipoFascicolo.getEtichettaTipologia())) {
				tipologiaFascicolo.setItemSelected(i, true);
			}
		}
	}

	@Override
	public void enableListTipologiePratiche() {
		if (tipologiaFascicolo.getItemCount() == 1) {
			tipologiaFascicolo.setEnabled(false);
			tipologiaFascicolo.setStyleName("disabilitato");
		} else {
			tipologiaFascicolo.setEnabled(true);
			tipologiaFascicolo.removeStyleName("disabilitato");
		}
	}

	@Override
	public void setDatiAggiuntivi(List<DatoAggiuntivo> dati) {
		formDatiAggiuntiviWidget.setDatiAggiuntiviPerCreazione(dati);
	}

	@Override
	public boolean controlloDatiAggiuntivi() {
		return formDatiAggiuntiviWidget.validazioneClient();
	}

	@Override
	public List<DatoAggiuntivo> getDatiAggiuntivi() {
		return formDatiAggiuntiviWidget.getDatiAggiuntivi();
	}

	@Override
	public boolean controlloServerDatiAggiuntivi(List<ValidazioneDatoAggiuntivoDTO> validazioneDatiAggiuntivi) {
		return formDatiAggiuntiviWidget.validazioneServer(validazioneDatiAggiuntivi);
	}

	@Override
	public void setSalvaFascicoloDefault(boolean salvaFascicoloDefault) {
		this.salvaFascicoloDefault.setValue(salvaFascicoloDefault);
	}

	@Override
	public boolean isSalvaFascicoloDefault() {
		return salvaFascicoloDefault.getValue();
	}

	@Override
	public void loadFormDatiAggiuntivi(EventBus eventBus, Object openingRequestor, DispatchAsync dispatcher) {
		formDatiAggiuntiviWidget = new FormDatiAggiuntiviWidget(eventBus, openingRequestor, dispatcher);
		datiAggiuntiviPanel.add(formDatiAggiuntiviWidget);
	}

	@Override
	public FormDatiAggiuntiviWidget getFormDatiAggiuntivi() {
		return formDatiAggiuntiviWidget;
	}
}
