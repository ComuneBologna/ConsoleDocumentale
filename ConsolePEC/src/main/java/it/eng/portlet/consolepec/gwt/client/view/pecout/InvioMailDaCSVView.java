package it.eng.portlet.consolepec.gwt.client.view.pecout;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

import it.eng.cobo.consolepec.commons.configurazioni.AnagraficaRuolo;
import it.eng.portlet.consolepec.gwt.client.presenter.pecout.InvioMailDaCSVPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.AnagraficheRuoliSuggestOracle;

public class InvioMailDaCSVView extends ViewImpl implements InvioMailDaCSVPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, InvioMailDaCSVView> {
		//
	}

	@UiField
	HeadingElement title;
	@UiField
	HTMLPanel messageWidgetPanel;
	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	@UiField(provided = true)
	ListBox separatoreListBox;
	@UiField
	IntegerBox headerIntegerBox;
	@UiField
	IntegerBox indirizzoEmailIntegerBox;
	@UiField
	CheckBox indirizzoDestinatarioFromModelloCheckbox;
	@UiField
	IntegerBox idDocumentaleIntegerBox;
	@UiField
	CheckBox preValidazioneCheckBox;
	@UiField(provided = true)
	SuggestBox assegnatarioSuggestBox = new SuggestBox(new AnagraficheRuoliSuggestOracle(new ArrayList<AnagraficaRuolo>()));

	@UiField
	Button annullaInvio;
	@UiField
	Button avantiInvio;

	private Command salvaCommand;
	private Command annullaCommand;

	@Inject
	public InvioMailDaCSVView(final Binder binder, final EventBus eventBus) {
		messageWidget = new MessageAlertWidget(eventBus);
		separatoreListBox = new ListBox();
		widget = binder.createAndBindUi(this);
		configuraBottoni();
	}

	private void configuraBottoni() {
		this.avantiInvio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				salvaCommand.execute();
			}
		});

		this.annullaInvio.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent arg0) {
				annullaCommand.execute();
			}
		});
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		this.title.scrollIntoView();
		Window.scrollTo(0, 0);
	}

	@Override
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

	@Override
	public void clear() {
		messageWidgetPanel.setVisible(false);
		messageWidget.reset();

		headerIntegerBox.setValue(0);
		indirizzoEmailIntegerBox.setValue(0);
		indirizzoDestinatarioFromModelloCheckbox.setValue(false);
		idDocumentaleIntegerBox.setValue(0);
		separatoreListBox.setSelectedIndex(0);
		assegnatarioSuggestBox.setText(null);
	}

	@Override
	public void setConfermaInvio(Command command) {
		this.salvaCommand = command;

	}

	@Override
	public void setAnnullaInvio(Command command) {
		this.annullaCommand = command;

	}

	@Override
	public Integer getHeader() {
		return headerIntegerBox.getValue();
	}

	@Override
	public Integer getPosizioneIndirizzo() {
		return indirizzoEmailIntegerBox.getValue();
	}

	@Override
	public Integer getPosizioneIdDocumentale() {
		return idDocumentaleIntegerBox.getValue();
	}

	@Override
	public String getSeparatore() {
		return separatoreListBox.getSelectedValue();
	}

	@Override
	public boolean isIndirizzoDestinatarioFromModello() {
		return indirizzoDestinatarioFromModelloCheckbox.getValue();
	}

	@Override
	public String getAssegnatario() {
		return assegnatarioSuggestBox.getValue();
	}

	@Override
	public void setAssegnatariSuggestValues(List<AnagraficaRuolo> gruppi) {
		AnagraficheRuoliSuggestOracle so = (AnagraficheRuoliSuggestOracle) this.assegnatarioSuggestBox.getSuggestOracle();
		so.setAnagraficheRuoli(gruppi);
	}

	@Override
	public void setSeparatoriListValues(List<String> separatori) {
		for (String separatore : separatori) {
			separatoreListBox.addItem(separatore);
		}
	}

	@Override
	public boolean isPreValidazione() {
		return preValidazioneCheckBox.getValue();
	}

}
