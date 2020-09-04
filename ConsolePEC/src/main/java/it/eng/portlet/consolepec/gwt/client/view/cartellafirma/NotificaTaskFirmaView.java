package it.eng.portlet.consolepec.gwt.client.view.cartellafirma;

import it.eng.cobo.consolepec.commons.datigenerici.PreferenzeUtente.PreferenzeCartellaAttivita;
import it.eng.cobo.consolepec.util.validation.ValidationUtilities;
import it.eng.portlet.consolepec.gwt.client.presenter.cartellafirma.NotificaTaskFirmaPresenter.MyView;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.InputListWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.IndirizziEmailSuggestOracle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.base.Strings;
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
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * 
 * @author biagiot
 *
 */
public class NotificaTaskFirmaView extends ViewImpl implements MyView {
	
	private final Widget widget;
	
	@UiField
	TextArea noteTextArea;
	@UiField
	HTMLPanel indirizziNotificaPanel;
	@UiField
	HTMLPanel indirizziNotificaContainer;
	@UiField
	CheckBox ricordaSceltaCheckBox;
	@UiField(provided = true)
	MessageAlertWidget messaggioAlertWidget;
	@UiField
	HeadingElement mainTitle;
	@UiField
	Button annullaButton;
	@UiField
	Button confermaButton;
	
	InputListWidget inputListWidgetDestinatari;
	
	public interface Binder extends UiBinder<Widget, NotificaTaskFirmaView> {
		//
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		mainTitle.scrollIntoView();
	}
	
	@Inject
	public NotificaTaskFirmaView(Binder binder, EventBus eventBus) {
		messaggioAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
		
		inputListWidgetDestinatari = new InputListWidget(new IndirizziEmailSuggestOracle(new ArrayList<String>()), "notifiche");
		RootPanel.get().add(inputListWidgetDestinatari);
		indirizziNotificaContainer.add(inputListWidgetDestinatari);
	}
		
	@Override
	public void prevalorizzaForm(PreferenzeCartellaAttivita preferenzeCartellaAttivita) {
		
		reset();
		
		if (preferenzeCartellaAttivita != null && preferenzeCartellaAttivita.getIndirizziNotifica() != null)
			for(String indirizzo : preferenzeCartellaAttivita.getIndirizziNotifica())
				inputListWidgetDestinatari.addValueItem(indirizzo);
	}
	
	@Override
	public List<String> controllaForm() {
		
		List<String> result = new ArrayList<String>();
		
		Set<String> set = new HashSet<String>(getIndirizziNotifica());
		if(set.size() < getIndirizziNotifica().size()){
			result.add("Ci sono duplicati tra gli indirizzi notifica.");
		}

		String textFromInputListWidgetDestinatari = getTextFromInputListWidgetDestinatari();
		if(!Strings.isNullOrEmpty(textFromInputListWidgetDestinatari) && !ValidationUtilities.validateEmailAddress(textFromInputListWidgetDestinatari)) {
			result.add("Indirizzo di notifica non valido.");
		}

		return result;
	}
	
	@Override
	public String getNote() {
		return noteTextArea.getText();
	}
	
	@Override
	public List<String> getIndirizziNotifica() {
		return inputListWidgetDestinatari.getItemSelected();
	}
	
	@Override
	public String getTextFromInputListWidgetDestinatari(){
		return inputListWidgetDestinatari.getText();
	}
	
	@Override
	public boolean isRicordaSceltaEnabled() {
		return ricordaSceltaCheckBox.getValue();
	}
	
	@Override
	public void setTitolo(String titolo) {
		this.mainTitle.setInnerText(titolo);
	}
	
	@Override
	public void setAnnullaCommand(final Command command) {
		this.annullaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setConfermaCommand(final Command command) {
		this.confermaButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
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
			messaggioAlertWidget.showWarningMessage(w.getHTML());

		} else {
			messaggioAlertWidget.reset();
		}
	}
	
	@Override
	public void reset() {
		Window.scrollTo(0, 0);
		messaggioAlertWidget.reset();
		inputListWidgetDestinatari.clear();
		noteTextArea.setText("");
		ricordaSceltaCheckBox.setValue(false);
	}
}
