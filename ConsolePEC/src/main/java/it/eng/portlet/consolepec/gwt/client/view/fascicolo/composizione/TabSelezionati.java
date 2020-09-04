package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.SelezioneElementoEvent;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.SelezioneElementoEvent.SelezioneElementoEventHandler;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.WidgetElementoSelezionato;

/**
 * @author GiacomoFM
 * @since 01/feb/2019
 */
public class TabSelezionati extends TabComposizione<ElementoComposizione> implements SelezioneElementoEventHandler {

	public static final String NAME = "Selezionati";

	private Set<ElementoComposizione> elementiTab = new HashSet<>();

	public TabSelezionati(EventBus eventBus) {
		super(eventBus);
		eventBus.addHandler(SelezioneElementoEvent.TYPE, this);
	}

	@Override
	String getTabName() {
		return NAME;
	}

	@Override
	protected List<ElementoComposizione> getList() {
		return new ArrayList<>(elementiTab);
	}

	@Override
	protected Widget drawWidget(ElementoComposizione elemento, Set<ElementoComposizione> elementiSelezionati) {
		return new WidgetElementoSelezionato(eventBus, dto, this, elemento, elementiSelezionati);
	}

	@Override
	public void onSelezionaElemento(ElementoComposizione elementoSelezionato) {
		if (elementiTab.add(elementoSelezionato)) {
			redrawInternalPanel(getList());
		}
	}

	@Override
	public void onDeselezionaElemento(ElementoComposizione elementoDeselezionato) {
		if (elementiTab.remove(elementoDeselezionato)) {
			redrawInternalPanel(getList());
		}
	}

	@Override
	public void filtraRicerca(Set<?> search) {
		if (!elementiTab.isEmpty()) {
			elementiTab.clear();
			redrawInternalPanel(getList());
		}
	}

	@Override
	public void reset() {
		elementiTab.clear();
		super.reset();
	}

}
