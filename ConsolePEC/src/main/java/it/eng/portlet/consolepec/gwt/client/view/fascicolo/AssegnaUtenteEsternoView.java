package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.AssegnaUtenteEsternoPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.operazioni.ElencoOperazioniWidget;
import it.eng.portlet.consolepec.gwt.client.widget.suggestBox.oracle.SpacebarSuggestOracle;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class AssegnaUtenteEsternoView extends ViewImpl implements AssegnaUtenteEsternoPresenter.MyView {

	private final Widget widget;

	@UiField
	HTMLPanel destinatarioPanel;
	@UiField
	TextArea testoTextAreaBox;

	@UiField
	HTMLPanel mainOperazioniPanel;

	@UiField
	Button confermaButton;
	@UiField
	Button annullaButton;

	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	private SuggestBox inputListWidgetDestinatari;

	private ElencoOperazioniWidget elencoOperazioniWidget;

	public interface Binder extends UiBinder<Widget, AssegnaUtenteEsternoView> {
	}

	@Inject
	public AssegnaUtenteEsternoView(final Binder binder, final EventBus eventBus) {
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);

	}

	@Override
	public void setSuggestOracleDestinatari(List<String> destinatari, Command command) {
		// inputListWidgetDestinatari = new InputListWidget(new SpacebarSuggestOracle(destinatari), "des");
		// inputListWidgetDestinatari.addItemCommand(command);
		inputListWidgetDestinatari = new SuggestBox(new SpacebarSuggestOracle(destinatari));
		destinatarioPanel.clear();
		RootPanel.get().add(inputListWidgetDestinatari);
		destinatarioPanel.add(inputListWidgetDestinatari);
	}

	@Override
	public void popolaOperazioniFascicolo(String titolo, ImageResource icona, HashMap<String, Boolean> operazioni, Command onSelezioneCommand) {
		mainOperazioniPanel.clear();
		elencoOperazioniWidget = new ElencoOperazioniWidget();
		elencoOperazioniWidget.init(titolo, icona, operazioni, true, false);
		elencoOperazioniWidget.setOnSelezioneCommand(onSelezioneCommand);
		mainOperazioniPanel.add(elencoOperazioniWidget);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setAnnullaCommand(final com.google.gwt.user.client.Command command) {
		annullaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void setConfermaCommand(final com.google.gwt.user.client.Command command) {
		confermaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public List<String> getDestinatari() {
		if (Strings.isNullOrEmpty(inputListWidgetDestinatari.getValue()) || Strings.isNullOrEmpty(inputListWidgetDestinatari.getValue().trim())) {
			return Lists.newArrayList();
		} else{
			return Arrays.asList(inputListWidgetDestinatari.getValue().trim().split(","));
		}
	}

	@Override
	public String getTestoEmail() {
		return testoTextAreaBox.getValue();
	}

	@Override
	public void resetTestoEmail() {
		testoTextAreaBox.setValue(null);
	}

	@Override
	public List<String> getOperazioni() {
		return elencoOperazioniWidget.getOperazioniSelezionate();
	}

}
