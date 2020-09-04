package it.eng.portlet.consolepec.gwt.client.view;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.portlet.consolepec.gwt.client.presenter.OperatorePresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;

public class OperatoreView extends ViewImpl implements OperatorePresenter.MyView {

	private final Widget widget;
	@UiField
	Button confermaButton;
	@UiField
	Button annullaButton;
	@UiField(provided = true)
	SuggestBox operatoreSuggestBox;
	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;

	private SpacebarSuggestOracle operatoriSuggest = new SpacebarSuggestOracle(new ArrayList<String>());

	public interface Binder extends UiBinder<Widget, OperatoreView> {
	}

	@Inject
	public OperatoreView(final Binder binder, final EventBus eventBus) {
		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		operatoreSuggestBox = new SuggestBox(operatoriSuggest);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}



	@Override
	public void clearForm() {
		operatoreSuggestBox.setEnabled(false);
		messaggioAlertWidget.reset();
	}

	@Override
	public void setOperatori(List<String> operatori, String operatoreDefault) {
		if(!operatori.isEmpty()){
			operatoriSuggest.setSuggestions(operatori);

		} else {
			operatoriSuggest.setSuggestions(new ArrayList<String>());
		}

		if(operatoreDefault != null && !operatoreDefault.trim().equals("")){
			operatoreSuggestBox.setValue(operatoreDefault);

		} else {
			operatoreSuggestBox.setValue(null);
		}
	}

	@Override
	public String getOperatore() {
		return operatoreSuggestBox.getValue();
	}

	@Override
	public void setAnnullaCommand(final Command annullaCommand) {
		annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				annullaCommand.execute();

			}
		});
	}

	@Override
	public void setConfermaCommand(final Command confermaCommand) {
		confermaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				confermaCommand.execute();

			}
		});
	}

	@Override
	public void setErrorMessage(String message) {
		this.messaggioAlertWidget.showWarningMessage(message);
	}


}
