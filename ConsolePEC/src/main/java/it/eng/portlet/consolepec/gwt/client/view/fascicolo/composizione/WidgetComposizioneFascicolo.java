package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.DettaglioFascicoloGenericoView;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.AggiornaPostSelezioneEvent;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.AggiornaPostSelezioneEvent.AggiornaPostSelezioneEventHandler;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author GiacomoFM
 * @since 28/dic/2018
 */
public class WidgetComposizioneFascicolo extends Composite implements AggiornaPostSelezioneEventHandler {

	@UiField
	HTMLPanel basePanel;
	@UiField
	TabLayoutPanel tabPanel;

	private FascicoloDTO dto;
	private Map<String, TabComposizione<?>> tabs = new LinkedHashMap<>();
	private Integer currentTab = 0;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetComposizioneFascicolo> {/**/}

	@Setter
	private DettaglioFascicoloGenericoView view;

	private final WidgetComposizioneRicerca widgetComposizioneRicerca;

	@Getter
	private Set<ElementoComposizione> elementiSelezionati = new HashSet<>();

	public WidgetComposizioneFascicolo(EventBus eventBus, final WidgetComposizioneRicerca widgetComposizioneRicerca) {
		this.widgetComposizioneRicerca = widgetComposizioneRicerca;
		initWidget(binder.createAndBindUi(this));

		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				openTab(event.getSelectedItem());
			}
		});

		widgetComposizioneRicerca.addRicercaClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (widgetComposizioneRicerca.getRicercaText() == null) {
					widgetComposizioneRicerca.reset();
				} else {
					elementiSelezionati.clear();

					Set<Object> search = widgetComposizioneRicerca.getRicercaHandler().search(widgetComposizioneRicerca.getRicercaText());
					filtraTabInRicerca(search);
				}
			}
		});

		widgetComposizioneRicerca.addResetClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				resetTab();
			}
		});

		eventBus.addHandler(AggiornaPostSelezioneEvent.TYPE, this);
	}

	public WidgetComposizioneFascicolo addTab(TabComposizione<?> tab) {
		tabs.put(tab.getTabName(), tab);
		return this;
	}

	public void init(FascicoloDTO fascicoloDTO) {
		tabPanel.clear();
		this.dto = fascicoloDTO;
		if (this.dto == null) {
			throw new IllegalStateException("Nessun fascicolo inizializzato");
		}

		if (this.tabs == null || this.tabs.isEmpty()) {
			throw new IllegalStateException("Nessuna tab impostata per la composizione del fascicolo");
		}

		widgetComposizioneRicerca.getRicercaHandler().clear();
		widgetComposizioneRicerca.getRicercaHandler().index(fascicoloDTO);

		for (Object tab : this.tabs.values()) {
			((TabComposizione<?>) tab).initTab(fascicoloDTO, elementiSelezionati, tabPanel);
		}
		openTab(currentTab);

		clearGWTstyle(tabPanel.getElement().getChildNodes());
		update();
	}

	/**
	 * GWT imposta uno stile con position: absolute, io lo tolgo.
	 */
	private void clearGWTstyle(NodeList<Node> childNodes) {
		for (int index = 0; index < childNodes.getLength(); index++) {
			childNodes.getItem(index).getParentElement().getStyle().clearPosition();
			childNodes.getItem(index).getParentElement().getStyle().clearWidth();
			clearGWTstyle(childNodes.getItem(index).getChildNodes());
		}
	}

	public void openTab(Integer tabPosition) {
		if (tabPosition >= this.tabs.size())
			currentTab = 0;
		else
			currentTab = tabPosition;

		if (tabPanel.getWidgetCount() >= this.tabs.size() && this.tabs.size() > currentTab) {
			tabPanel.selectTab(currentTab);
		}
	}

	public void resetTab() {
		widgetComposizioneRicerca.setRicercaText("");
		for (Entry<String, TabComposizione<?>> tab : this.tabs.entrySet()) {
			tab.getValue().reset();
		}
		update();
	}

	protected void filtraTabInRicerca(Set<Object> search) {
		for (Entry<String, TabComposizione<?>> tab : this.tabs.entrySet()) {
			tab.getValue().filtraRicerca(search);
		}
	}

	@Override
	public void update() {
		if (this.tabs.containsKey(TabSelezionati.NAME) && this.tabs.values().size() == tabPanel.getWidgetCount()) {
			for (int i = 0; i < this.tabs.values().size(); i++) {
				if (TabSelezionati.NAME.startsWith(((TabComposizione<?>) this.tabs.values().toArray()[i]).getTabName())) {
					aggiornaContatoreSelezionati(tabPanel.getTabWidget(i), ((TabComposizione<?>) this.tabs.values().toArray()[i]).getList());
					tabPanel.getTabWidget(i).setVisible(!elementiSelezionati.isEmpty());
					tabPanel.getTabWidget(i).getParent().setVisible(!elementiSelezionati.isEmpty());
					if (elementiSelezionati.isEmpty() && tabPanel.getSelectedIndex() == i) {
						openTab(0);
					}
				}
			}
		}

		if (view != null) {
			view.updateViewPostSelezione(dto);
		}
	}

	private static void aggiornaContatoreSelezionati(Widget tabWidget, List<?> list) {
		if (tabWidget != null) {
			StringBuilder sb = new StringBuilder(TabSelezionati.NAME);
			if (list.size() > 0)
				sb.append(" (").append(list.size()).append(")");
			tabWidget.getElement().setInnerText(sb.toString());
		}
	}

}
