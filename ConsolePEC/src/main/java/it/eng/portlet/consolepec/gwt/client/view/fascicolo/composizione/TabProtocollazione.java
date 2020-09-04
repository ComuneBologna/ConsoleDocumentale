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

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoProtocollato;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ProtocollazioneComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.WidgetProtocollazione;

/**
 * @author GiacomoFM
 * @since 08/gen/2019
 */
public class TabProtocollazione extends TabComposizione<ProtocollazioneComposizione> {

	public static final String NAME = "Tutti";

	@Override
	String getTabName() {
		return NAME;
	}

	public TabProtocollazione(EventBus eventBus) {
		super(eventBus);
	}

	@Override
	protected List<ProtocollazioneComposizione> getList() {
		return dto.getComposizioneProtocollazioni();
	}

	@Override
	protected Widget drawWidget(ProtocollazioneComposizione protocollazione, Set<ElementoComposizione> elementiSelezionati) {
		return new WidgetProtocollazione(protocollazione, elementiSelezionati, eventBus, dto, this);
	}

	@Override
	public void filtraRicerca(final Set<?> search) {
		Collection<ProtocollazioneComposizione> filter = Collections2.filter(getList(), new Predicate<ProtocollazioneComposizione>() {
			@Override
			public boolean apply(ProtocollazioneComposizione input) {
				for (Object o : search) {
					for (ElementoProtocollato ep : input.getElementi()) {
						if (ep.equals(o)) {
							return true;
						}
					}
					for (ProtocollazioneComposizione pc : input.getNonCapofila()) {
						for (ElementoProtocollato epi : pc.getElementi()) {
							if (epi.equals(o)) {
								return true;
							}
						}
					}
				}
				return false;
			}
		});
		if (filter.isEmpty()) {
			redrawInternalPanel(Collections.<ProtocollazioneComposizione> emptyList());
		} else {
			redrawInternalPanel(new ArrayList<>(filter));
		}
	}
}
