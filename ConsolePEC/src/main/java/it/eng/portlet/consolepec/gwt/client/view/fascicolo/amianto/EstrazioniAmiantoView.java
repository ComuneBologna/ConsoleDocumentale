package it.eng.portlet.consolepec.gwt.client.view.fascicolo.amianto;

import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.amianto.EstrazioniAmiantoPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.shared.model.amianto.EstrazioneAmiantoDTO;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.Format;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class EstrazioniAmiantoView extends ViewImpl implements EstrazioniAmiantoPresenter.MyView {

	//Estrazione verifica
	@UiField
	DateBox dataInizio;
	@UiField
	DateBox dataFine;
	
	//Button
	@UiField
	Button estrai;
	
	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;
	@UiField
	DownloadAllegatoWidget downloadWidget;
	
	private final Widget widget;
	
	public interface Binder extends UiBinder<Widget, EstrazioniAmiantoView> {
	}

	@Inject
	public EstrazioniAmiantoView(final Binder binder, final EventBus eventBus) {
		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
		init();
	}
	
	public void init(){
		DateTimeFormat f = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATA);
		Format format = new DateBox.DefaultFormat(f);
		dataInizio.setFormat(format);
		dataInizio.getDatePicker().setYearArrowsVisible(true);
		dataFine.setFormat(format);
		dataFine.getDatePicker().setYearArrowsVisible(true);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setEstrazioneCommand(final Command command) {
		this.estrai.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent arg0) {
				command.execute();
			}
		});
	}
	
	
	@Override
	public boolean controllaCampiObbligatori() {
		boolean ok = true;
		if (dataInizio.getValue() == null){
			dataInizio.getElement().setAttribute("required", "required");
			ok = false;
		} else {
			dataInizio.getElement().removeAttribute("required");
		}
		if (dataFine.getValue() == null){
			dataFine.getElement().setAttribute("required", "required");
			ok = false;
		} else {
			dataFine.getElement().removeAttribute("required");
		}
		return ok;
	}
	
	@Override
	public void pulisciCampi(){
		dataInizio.setValue(null);
		dataFine.setValue(null);
	}


	//Estrazione
	@Override
	public EstrazioneAmiantoDTO getEstrazioneAmiantoDTO() {
		EstrazioneAmiantoDTO estrazioneAmiantoDTO = new EstrazioneAmiantoDTO();
		estrazioneAmiantoDTO.setInizio(dataInizio.getValue());
		estrazioneAmiantoDTO.setFine(dataFine.getValue());
		return estrazioneAmiantoDTO;
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}
	
	

}
