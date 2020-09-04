package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.PubblicazioneAllegatiPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.InputListWidget;

import java.util.Date;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class PubblicazioneAllegatiView extends ViewImpl implements PubblicazioneAllegatiPresenter.MyView {

	private final Widget widget;

	@UiField
	Button pubblicaButton;
	@UiField
	Button annullaButton;
	@UiField
	TextArea urlTextBox;
	@UiField
	DateBox dataFromDateBox;
	@UiField
	DateBox dataToDateBox;
	@UiField
	HTMLPanel destinatarioPanel;
	@UiField
	TextArea testoTextAreaBox;

	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	private InputListWidget inputListWidgetDestinatari;

	public interface Binder extends UiBinder<Widget, PubblicazioneAllegatiView> {
	}

	@Inject
	public PubblicazioneAllegatiView(final Binder binder, final EventBus eventBus) {
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
		Format dateFormat = new DateBox.DefaultFormat(DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA));
		dataFromDateBox.getDatePicker().setYearArrowsVisible(true);
		dataFromDateBox.setFormat(dateFormat);
		dataToDateBox.getDatePicker().setYearArrowsVisible(true);
		dataToDateBox.setFormat(dateFormat);
		urlTextBox.addStyleName("disabilitato");
		urlTextBox.setReadOnly(true);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setAnnullaCommand(final Command command) {
		annullaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setPubblicaCommand(final Command command) {
		pubblicaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public Date getDataInizio() {
		return dataFromDateBox.getDatePicker().getValue();
	}

	@Override
	public Date getDataFine() {
		return dataToDateBox.getDatePicker().getValue();
	}

	@Override
	public void popolaGUI(String url, Date dataInizio, Date dataFine, String confermaLabel) {
		urlTextBox.setValue(url);
		dataFromDateBox.setValue(dataInizio);
		dataToDateBox.setValue(dataFine);
		inputListWidgetDestinatari.getItemSelected().clear();
		testoTextAreaBox.setValue(null);
		pubblicaButton.setText(confermaLabel);
	}

	@Override
	public void abilitaConfermaPubblicazione(boolean abilita) {
		this.pubblicaButton.setEnabled(abilita);
	}
	
	@Override
	public void setSuggestOracleDestinatari(SuggestOracle suggestOracle, Command command) {
		inputListWidgetDestinatari = new InputListWidget(suggestOracle, "des");
		inputListWidgetDestinatari.addItemCommand(command);
		destinatarioPanel.clear();
		RootPanel.get().add(inputListWidgetDestinatari);
		destinatarioPanel.add(inputListWidgetDestinatari);
		
	}

	@Override
	public void setDataFromValueChangeHandler(ValueChangeHandler<Date> handler) {
		this.dataFromDateBox.addValueChangeHandler(handler);
	}

	@Override
	public void setDataToValueChangeHandler(ValueChangeHandler<Date> handler) {
		this.dataToDateBox.addValueChangeHandler(handler);
	}

	@Override
	public List<String> getDestinatariEmail() {
		return inputListWidgetDestinatari.getItemSelected();
	}

	@Override
	public String getTestoEmail() {
		return testoTextAreaBox.getValue();
	}

	@Override
	public void impostaConfermaPubblicazione(String confermaLabel) {
		pubblicaButton.setText(confermaLabel);
	}

	@Override
	public void abilitaDestinatariEmail(boolean abilita) {
		if(!abilita)
			inputListWidgetDestinatari.reset();
		inputListWidgetDestinatari.setAbilitato(!abilita);
	}

	@Override
	public void abilitaTestoEmail(boolean abilita) {
		if(!abilita)
			testoTextAreaBox.setText("");
		testoTextAreaBox.setEnabled(abilita);
	}
	
	@Override
	public void abilitaDataInizio(boolean abilita) {
		dataFromDateBox.setEnabled(abilita);
//		styleName="disabilitato"
		if(abilita)
			dataFromDateBox.removeStyleName("disabilitato");
		else
			dataFromDateBox.setStyleName("disabilitato");
	}

}
