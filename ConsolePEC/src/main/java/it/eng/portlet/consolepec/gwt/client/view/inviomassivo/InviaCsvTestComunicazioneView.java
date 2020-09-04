package it.eng.portlet.consolepec.gwt.client.view.inviomassivo;

import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.presenter.inviomassivo.InviaCsvTestComunicazionePresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;

import com.google.common.base.Strings;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class InviaCsvTestComunicazioneView extends ViewImpl implements InviaCsvTestComunicazionePresenter.MyView {

	private final Widget widget;
	@UiField
	TextBox destinatarioTextBox;
	@UiField
	IntegerBox numeroInviiIntegerBox;
	@UiField
	Button avantiButton;
	@UiField
	Button annullaButton;
	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;
	
	private Command avantiCommand;
	private Command annullaCommand;
	
	public interface Binder extends UiBinder<Widget, InviaCsvTestComunicazioneView> {
	}

	@Inject
	public InviaCsvTestComunicazioneView(final Binder binder, final EventBus eventBus) {
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
	public void clear() {
		destinatarioTextBox.setValue("");
		destinatarioTextBox.getElement().removeAttribute("required");
		numeroInviiIntegerBox.setValue(null);
		numeroInviiIntegerBox.getElement().removeAttribute("required");
	}

	@Override
	public String getDestinatario() {
		return destinatarioTextBox.getValue();
	}

	@Override
	public Integer getNumeroInvii() {
		return numeroInviiIntegerBox.getValue();
	}

	@Override
	public boolean controllaCampi() {
		boolean valid = true;
		
		if(Strings.isNullOrEmpty(destinatarioTextBox.getText())){
			destinatarioTextBox.getElement().setAttribute("required", "required"); 
			valid = false;
		} else {
			if(!Strings.isNullOrEmpty(destinatarioTextBox.getText()) && !ValidationUtilities.validateEmailAddress(destinatarioTextBox.getText()) ){
				destinatarioTextBox.getElement().setAttribute("required", "required");
				valid = false;
			} else {
				destinatarioTextBox.getElement().removeAttribute("required");
			}
		}
		
		if(Strings.isNullOrEmpty(numeroInviiIntegerBox.getText())){
			numeroInviiIntegerBox.getElement().setAttribute("required", "required");
			valid = false;
		} else {
			if(!Strings.isNullOrEmpty(numeroInviiIntegerBox.getText()) && !ValidationUtilities.isIntNumber(numeroInviiIntegerBox.getText()) ){
				numeroInviiIntegerBox.getElement().setAttribute("required", "required");
				valid = false;
			} else {
				numeroInviiIntegerBox.getElement().removeAttribute("required");
			}
		}
		
		
		return valid;
	}

	
	
	
	
	
}
