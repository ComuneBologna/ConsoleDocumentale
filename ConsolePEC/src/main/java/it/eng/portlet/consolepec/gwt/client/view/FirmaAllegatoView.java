package it.eng.portlet.consolepec.gwt.client.view;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.event.ShowMessageEvent;
import it.eng.portlet.consolepec.gwt.client.presenter.FirmaAllegatoPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO.TipologiaFirma;

public class FirmaAllegatoView extends ViewImpl implements FirmaAllegatoPresenter.MyView {

	private final Widget widget;
	@UiField
	Button chiudiButton;
	@UiField
	Button firmaButton;
	@UiField
	Button otpSMSButton;
	@UiField
	TextBox otpTextBox;
	@UiField
	PasswordTextBox passwordTextBox;
	@UiField
	TextBox userNameTextBox;
	@UiField
	CheckBox salvaCredenzialiChechBox;
	@UiField
	ListBox tipoFirma;

	EventBus eventBus;
	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;

	boolean credenzialiUserModificate, credenzialiPasswordModificate;

	public interface Binder extends UiBinder<Widget, FirmaAllegatoView> {}

	@Inject
	public FirmaAllegatoView(final Binder binder, final EventBus eventBus) {
		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
		this.eventBus = eventBus;

		userNameTextBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				credenzialiUserModificate = true;
			}
		});

		passwordTextBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				credenzialiPasswordModificate = true;
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setChiudiPageFirmaCommand(final Command command) {
		this.chiudiButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();

			}
		});

	}

	@Override
	public void setFirmaCommand(final Command command) {
		this.firmaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean invokeExecuteCommand = true;
				resetObbligatorio(otpTextBox);
				resetObbligatorio(passwordTextBox);
				resetObbligatorio(userNameTextBox);
				resetObbligatorio(tipoFirma);

				if (getPassword() == null || "".equals(getPassword().trim())) {
					mostraObbligatorio(passwordTextBox);
					invokeExecuteCommand = false;
				}
				if (getUserName() == null || "".equals(getUserName().trim())) {
					mostraObbligatorio(userNameTextBox);
					invokeExecuteCommand = false;
				}
				if (getOtp() == null || "".equals(getOtp().trim())) {
					mostraObbligatorio(otpTextBox);
					invokeExecuteCommand = false;
				}

				if (getSelectedTipologiaFirma() == null || "".equals(getSelectedTipologiaFirma().getLabel())) {
					mostraObbligatorio(tipoFirma);
					invokeExecuteCommand = false;
				}

				if (invokeExecuteCommand)
					command.execute();
			}
		});

	}

	@Override
	public void setOTPSMSRequestCommand(final it.eng.portlet.consolepec.gwt.client.presenter.Command<Void, FirmaAllegatoPresenter.OTPCredentialTypeEnum> command) {
		this.otpSMSButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				boolean invokeExecuteCommand = true;
				resetObbligatorio(passwordTextBox);
				resetObbligatorio(userNameTextBox);
				resetObbligatorio(otpTextBox);
				if (getPassword() == null || "".equals(getPassword().trim())) {
					mostraObbligatorio(passwordTextBox);
					invokeExecuteCommand = false;
				}
				if (getUserName() == null || "".equals(getUserName().trim())) {
					mostraObbligatorio(userNameTextBox);
					invokeExecuteCommand = false;
				}
				if (invokeExecuteCommand)
					command.exe(FirmaAllegatoPresenter.OTPCredentialTypeEnum.SMS);
			}
		});

	}

	private void mostraObbligatorio(TextBox tb) {
		tb.getElement().setAttribute("required", "required");
		ShowMessageEvent event = new ShowMessageEvent();
		event.setWarningMessage("I campi in rosso sono obbligatori");
		eventBus.fireEvent(event);
	}

	private void mostraObbligatorio(ListBox lb) {
		lb.getElement().setAttribute("required", "required");
		ShowMessageEvent event = new ShowMessageEvent();
		event.setWarningMessage("I campi in rosso sono obbligatori");
		eventBus.fireEvent(event);
	}

	private void resetObbligatorio(TextBox tb) {
		tb.getElement().removeAttribute("required");
	}

	private void resetObbligatorio(ListBox lb) {
		lb.getElement().removeAttribute("required");
	}

	@Override
	public String getPassword() {
		return this.passwordTextBox.getValue();
	}

	@Override
	public String getUserName() {
		return this.userNameTextBox.getValue();
	}

	@Override
	public String getOtp() {
		return this.otpTextBox.getValue();
	}

	@Override
	public void reset() {
		// TODO svuota campi
		passwordTextBox.setText("");
		passwordTextBox.getElement().removeAttribute("required");
		otpTextBox.getElement().removeAttribute("required");
		otpTextBox.setText("");
		userNameTextBox.setText("");
		userNameTextBox.getElement().removeAttribute("required");
		tipoFirma.clear();
		tipoFirma.getElement().removeAttribute("required");
		credenzialiUserModificate = false;
		credenzialiPasswordModificate = false;
		setSalvaCredenziali(false);
		/* finestra piccola, rimandiamo sempre in alto */
		Window.scrollTo(0, 0);
	}

	@Override
	public void focusOtpField() {
		otpTextBox.setFocus(true);
	}

	@Override
	public void setPassword(String password) {
		passwordTextBox.setText(password);
	}

	@Override
	public void setUserName(String username) {
		userNameTextBox.setText(username);
	}

	@Override
	public void setSalvaCredenziali(boolean salvaCredenziali) {
		salvaCredenzialiChechBox.setValue(salvaCredenziali);
	}

	@Override
	public boolean isCredenzialiUserModificate() {
		return credenzialiUserModificate;
	}

	@Override
	public boolean isCredenzialiPasswordModificate() {
		return credenzialiPasswordModificate;
	}

	@Override
	public boolean isSalvaCredenziali() {
		return salvaCredenzialiChechBox.getValue();
	}

	@Override
	public TipologiaFirma getSelectedTipologiaFirma() {
		return TipologiaFirma.fromLabel(this.tipoFirma.getSelectedValue());
	}

	@Override
	public void addTipologiaFirma(String tipologia) {
		this.tipoFirma.addItem(tipologia);
	}
}
