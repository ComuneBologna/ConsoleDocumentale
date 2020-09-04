package it.eng.portlet.consolepec.gwt.client.view.inviomassivo;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.CreaComunicazionePresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliSuggestOracle;
import it.eng.portlet.consolepec.gwt.shared.model.ComunicazioneDTO;

import java.util.Date;
import java.util.List;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class CreaComunicazioneView extends ViewImpl implements CreaComunicazionePresenter.MyView {

	private final Widget widget;
	@UiField
	TextBox codiceTextBox;
	@UiField
	TextBox descrizioneTextBox;
	@UiField
	TextBox utenteTextBox;
	@UiField
	DateBox dataCreazionePraticaDateBox;
	@UiField
	Button avantiButton;
	@UiField
	Button annullaButton;
	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;
	SuggestBox gruppiSuggestBox;
	
	@UiField
	HTMLPanel elencoGruppiSuggestBoxPanel;
	
	private Command avantiCommand;
	private Command annullaCommand;
	
	public interface Binder extends UiBinder<Widget, CreaComunicazioneView> {
	}

	@Inject
	public CreaComunicazioneView(final Binder binder, final EventBus eventBus) {
		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);

		this.avantiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				avantiCommand.execute();

			}
		});
		this.annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setAvantiCommand(final Command avantiCommand) {
		this.avantiCommand = avantiCommand;
	}

	@Override
	public void setAnnullaCommand(final Command annullaCommand) {
		this.annullaCommand = annullaCommand;


	}

	
	@Override
	public void svuotaCampi() {
		codiceTextBox.setValue("");
		codiceTextBox.getElement().removeAttribute("required");
		descrizioneTextBox.setValue("");
		utenteTextBox.setValue("");
	}

	@Override
	public void setUtente(String utente) {
		this.utenteTextBox.setValue(utente);
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
	public void hideButtonAnnulla() {
		this.annullaButton.setVisible(false);

	}

	@Override
	public void showButtonAnnulla() {
		this.annullaButton.setVisible(true);
	}

	@Override
	public void setGruppiSuggestBox(List<AnagraficaRuolo> ruoli) {
		AnagraficheRuoliSuggestOracle suggestOracleRuoli = new AnagraficheRuoliSuggestOracle(ruoli);
		gruppiSuggestBox = new SuggestBox(suggestOracleRuoli);

		if (ruoli.size() == 1) {
			gruppiSuggestBox.setValue(ruoli.iterator().next().getEtichetta());
			gruppiSuggestBox.setEnabled(false);
			gruppiSuggestBox.setStyleName("testo disabilitato");
			
		} else {
			gruppiSuggestBox.removeStyleName("disabilitato");
		}

		elencoGruppiSuggestBoxPanel.clear();
		elencoGruppiSuggestBoxPanel.getElement().setInnerHTML("");
		elencoGruppiSuggestBoxPanel.add(gruppiSuggestBox);
	}

	@Override
	public void setAvantiEnabled(boolean enabled) {
		this.avantiButton.setEnabled(enabled);
	}

	@Override
	public ComunicazioneDTO getComunicazione() {
		ComunicazioneDTO c = new ComunicazioneDTO();
		
		c.setCodice(codiceTextBox.getValue());
		c.setDescrizione(descrizioneTextBox.getValue());
		
		c.setUtenteCreazione(utenteTextBox.getValue());
		c.setAssegnatario(gruppiSuggestBox.getValue());
		
		return c;
	}
	
	@Override
	public void setComunicazione(ComunicazioneDTO comunicazione) {
		codiceTextBox.setValue(comunicazione.getCodice());
		descrizioneTextBox.setValue(comunicazione.getDescrizione());
	}

	@Override
	public boolean controlloCampi(List<String> errori) {
		boolean valid = true;
		
		if(!Strings.isNullOrEmpty(codiceTextBox.getValue())){
			codiceTextBox.getElement().removeAttribute("required");
		} else {
			valid = false;
			codiceTextBox.getElement().setAttribute("required", "required");
		}
		
		return valid;
	}

	
	
}
