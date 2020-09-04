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
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.EmailComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna.WidgetEmailComposizione;

/**
 * @author GiacomoFM
 * @since 08/gen/2019
 */
public class TabComunicazioni extends TabComposizione<EmailComposizione> {

	public static final String NAME = "Comunicazioni";

	@Override
	String getTabName() {
		return NAME;
	}

	public TabComunicazioni(EventBus eventBus) {
		super(eventBus);
	}

	@Override
	protected List<EmailComposizione> getList() {
		return dto.getComposizioneEmail();
	}

	@Override
	protected Widget drawWidget(EmailComposizione elemento, Set<ElementoComposizione> elementiSelezionati) {
		return new WidgetEmailComposizione(elemento, elementiSelezionati, eventBus, dto, this);
	}

	@Override
	public void filtraRicerca(final Set<?> search) {
		Collection<EmailComposizione> filter = Collections2.filter(getList(), new Predicate<EmailComposizione>() {
			@Override
			public boolean apply(EmailComposizione input) {
				for (Object o : search) {
					if (input.equals(o)) {
						return true;
					}
					if (innerApply(input, o)) {
						return true;
					}
				}
				return false;
			}

			private boolean innerApply(EmailComposizione input, Object o) {
				for (EmailComposizione mail : input.getConversazione()) {
					if (mail.equals(o)) {
						return true;
					}
					return innerApply(mail, o);
				}
				return false;
			}
		});
		if (filter.isEmpty()) {
			redrawInternalPanel(Collections.<EmailComposizione> emptyList());
		} else {
			redrawInternalPanel(new ArrayList<>(filter));
		}
	}

}
