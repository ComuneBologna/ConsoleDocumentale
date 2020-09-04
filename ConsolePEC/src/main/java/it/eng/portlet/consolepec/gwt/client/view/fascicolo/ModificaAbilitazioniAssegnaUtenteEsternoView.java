package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.ModificaAbilitazioniAssegnaUtenteEsternoPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.operazioni.ElencoOperazioniWidget;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class ModificaAbilitazioniAssegnaUtenteEsternoView extends ViewImpl implements ModificaAbilitazioniAssegnaUtenteEsternoPresenter.MyView {

	private final Widget widget;

	@UiField
	HTMLPanel mainOperazioniPanel;
	
	@UiField
	HTMLPanel destinatariInoltroLabel;
	@UiField
	Label dataNotificaLabel;
	
	@UiField
	Button confermaButton;
	@UiField
	Button annullaButton;
	
	
	@UiField(provided = true)
	MessageAlertWidget messageWidget;

	private ElencoOperazioniWidget elencoOperazioniWidget;
	
	public interface Binder extends UiBinder<Widget, ModificaAbilitazioniAssegnaUtenteEsternoView> {
	}


	@Inject
	public ModificaAbilitazioniAssegnaUtenteEsternoView(final Binder binder, final EventBus eventBus) {
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
		
	}
	@Override
	public Widget asWidget() {
		return widget;
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
	public List<String> getOperazioni() {
		return elencoOperazioniWidget.getOperazioniSelezionate();
	}

	@Override
	public void setDestinatario(List<String> destinatari){
		StringBuilder bld = new StringBuilder();
		Iterator<String> it = destinatari.iterator();
		while(it.hasNext()) {
			bld.append(it.next() + (it.hasNext() ? "<br/>" : "") );
		}
		destinatariInoltroLabel.clear();
		destinatariInoltroLabel.add(new HTML(bld.toString()));
	}
	
	@Override
	public void setDataNotifica(String dataNotifica){
		dataNotificaLabel.setText(dataNotifica);
	}
}
