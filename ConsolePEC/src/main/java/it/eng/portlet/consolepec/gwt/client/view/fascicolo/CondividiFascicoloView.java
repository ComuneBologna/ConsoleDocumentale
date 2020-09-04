package it.eng.portlet.consolepec.gwt.client.view.fascicolo;

import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.CondividiFascicoloPresenter;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.operazioni.ElencoOperazioniWidget;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

public class CondividiFascicoloView extends ViewImpl implements CondividiFascicoloPresenter.MyView {

	private final Widget widget;
	@UiField(provided = true)
	MessageAlertWidget messageWidget;
	@UiField
	HTMLPanel elencoGruppiSuggestBoxPanel;
	@UiField
	Button confermaButton;
	@UiField
	Button annullaButton;
	@UiField
	Button aggiungiButton;
	@UiField
	HTMLPanel mainCollegaPanel;

	private String gruppoAssegnazione;
	private HashMap<String, ElencoOperazioniWidget> mappaWidgetOperazioni = new HashMap<String, ElencoOperazioniWidget>();

	public interface Binder extends UiBinder<Widget, CondividiFascicoloView> {
	}

	@Inject
	public CondividiFascicoloView(final Binder binder, final EventBus eventBus) {
		messageWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	protected void onAttach() {
		super.onAttach();
		Window.scrollTo(0, 0);
	}
	
	@Override
	public void setGruppiSuggestBox(SuggestBox suggestBox) {
		elencoGruppiSuggestBoxPanel.clear();
		elencoGruppiSuggestBoxPanel.getElement().setInnerHTML("");
		elencoGruppiSuggestBoxPanel.add(suggestBox);
	}

	@Override
	public void setAnnullaCommand(final Command annullaCommand) {
		annullaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				annullaCommand.execute();
			}
		});
	}

	@Override
	public void setConfermaCommand(final Command confermaCommand) {
		confermaButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				confermaCommand.execute();
			}
		});
	}

	@Override
	public void setAggiungiCommand(final Command aggiungiCommand) {
		aggiungiButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				aggiungiCommand.execute();
			}
		});
	}

	@Override
	public void attivaPulsanteAggiungi(boolean enabled) {
		aggiungiButton.setEnabled(enabled);
	}

	@Override
	public void popolaCondivisioneGruppo(String gruppo, ImageResource icona, HashMap<String, Boolean> operazioni, boolean mostraPulsanteElimina, Command onSelezioneCommand) {
		ElencoOperazioniWidget elencoOperazioniWidget = new ElencoOperazioniWidget();
		elencoOperazioniWidget.init(gruppo, icona, operazioni, false, mostraPulsanteElimina);
		elencoOperazioniWidget.setOnSelezioneCommand(onSelezioneCommand);
		mappaWidgetOperazioni.put(gruppo, elencoOperazioniWidget);
		refreshCondivisioni();
	}

	@Override
	public void refreshCondivisioni() {
		mainCollegaPanel.clear();
		for (String nomeGruppo : mappaWidgetOperazioni.keySet()) {
			mainCollegaPanel.add(mappaWidgetOperazioni.get(nomeGruppo));
			if (nomeGruppo.equals(gruppoAssegnazione))
				mappaWidgetOperazioni.get(nomeGruppo).setVisible(false);
		}
	}

	@Override
	public void clearCondivisioni() {
		mainCollegaPanel.clear();
		mappaWidgetOperazioni.clear();
	}

	@Override
	public List<String> getOperazioniSelezionate(String nomeGruppo) {
		return mappaWidgetOperazioni.get(nomeGruppo).getOperazioniSelezionate();
	}

	@Override
	public HashMap<String, List<String>> getOperazioniSelezionatePerGruppo() {
		HashMap<String, List<String>> mappa = new HashMap<String, List<String>>();
		for (String gruppo : mappaWidgetOperazioni.keySet())
			mappa.put(gruppo, getOperazioniSelezionate(gruppo));
		return mappa;
	}

	@Override
	public void attivaPulsanteConferma(boolean enabled) {
		confermaButton.setEnabled(enabled);
	}

	@Override
	public void attivaPulsanteElimina(String nomeGruppo, boolean enabled) {
		mappaWidgetOperazioni.get(nomeGruppo).attivaPulsanteElimina(enabled);
	}

	@Override
	public void setEliminaCommand(String nomeGruppo, Command eliminaCommand) {
		mappaWidgetOperazioni.get(nomeGruppo).setOnEliminaCommand(eliminaCommand);
	}

	@Override
	public void setGruppoAssegnazione(String gruppo) {
		gruppoAssegnazione = gruppo;
	}

}
