package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.AllegatoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.WidgetAllegatoComposizione;

/**
 * @author GiacomoFM
 * @since 08/gen/2019
 */
public class TabAllegati extends TabComposizione<AllegatoComposizione> {

	public static final String NAME = "Allegati";

	@Override
	String getTabName() {
		return NAME;
	}

	public TabAllegati(EventBus eventBus) {
		super(eventBus);
	}

	@Override
	protected List<AllegatoComposizione> getList() {
		return dto.getComposizioneAllegati();
	}

	@Override
	protected Widget drawWidget(AllegatoComposizione allegato, Set<ElementoComposizione> elementiSelezionati) {
		return new WidgetAllegatoComposizione(allegato, elementiSelezionati, eventBus, dto, this);
	}

	@Override
	public void filtraRicerca(final Set<?> search) {
		Collection<AllegatoComposizione> filter = Collections2.filter(getList(), new Predicate<AllegatoComposizione>() {
			@Override
			public boolean apply(AllegatoComposizione input) {
				for (Object o : search) {
					if (input.equals(o)) {
						return true;
					}
				}
				return false;
			}
		});
		if (filter.isEmpty()) {
			redrawInternalPanel(Collections.<AllegatoComposizione> emptyList());
		} else {
			redrawInternalPanel(new ArrayList<>(filter));
		}
	}

}
