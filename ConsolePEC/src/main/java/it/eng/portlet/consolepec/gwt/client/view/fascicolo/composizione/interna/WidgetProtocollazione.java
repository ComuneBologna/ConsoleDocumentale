package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna;

import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoProtocollato;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ProtocollazioneComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabComposizione;
import it.eng.portlet.consolepec.gwt.shared.ConsolePecConstants;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

public class WidgetProtocollazione extends Composite {

	@UiField
	HTMLPanel protocollazionePanel;
	@UiField
	InlineLabel infoProtocollazione;
	@UiField
	HTMLPanel gruppoElementi;
	@UiField
	HTMLPanel gruppoProtocollazioni;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetProtocollazione> {/**/}

	public WidgetProtocollazione(ProtocollazioneComposizione protocollazione, Set<ElementoComposizione> elementiSelezionati, final EventBus eventBus, final FascicoloDTO dto,
			final TabComposizione<?> tab) {
		initWidget(binder.createAndBindUi(this));
		infoProtocollazione(protocollazione);
		gruppoElementi(protocollazione, elementiSelezionati, eventBus, dto, tab);
		gruppoProtocollazioni(protocollazione, elementiSelezionati, eventBus, dto, tab);
	}

	private void infoProtocollazione(ProtocollazioneComposizione protocollazione) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		if (protocollazione.isProtocollata()) {
			if (protocollazione.isCapofila()) {
				protocollazionePanel.setStylePrimaryName("protocollazione");
				sb.appendEscaped(ConsolePecConstants.CAPOFILA_LABEL).appendEscaped(" ");
			}
			sb.appendHtmlConstant("<b>");
			sb.appendEscaped(protocollazione.getNumeroPg()).appendEscaped("/").appendEscaped(protocollazione.getAnnoPg());
			sb.appendHtmlConstant("</b>");
		} else {
			protocollazionePanel.setStylePrimaryName("non-protocollazione");
			sb.appendHtmlConstant("<b>");
			sb.appendEscaped("NON PROTOCOLLATO");
			sb.appendHtmlConstant("</b>");
		}
		infoProtocollazione.getElement().setInnerSafeHtml(sb.toSafeHtml());
	}

	private void gruppoElementi(ProtocollazioneComposizione protocollazione, Set<ElementoComposizione> elementiSelezionati, EventBus eventBus, FascicoloDTO dto, TabComposizione<?> tab) {
		for (ElementoProtocollato allegato : protocollazione.getElementi()) {
			gruppoElementi.add(new WidgetElementoProtocollazione(allegato, elementiSelezionati, eventBus, dto, tab));
		}
		// if (gruppoElementi.getWidgetCount() == 0 && protocollazione.isCapofila()) {
		// HTML div = new HTML();
		// div.setStylePrimaryName("composizione-elemento-protocollato");
		// InlineLabel label = new InlineLabel("Elemento capofila non all'interno di questo fascicolo.");
		// label.setStylePrimaryName("elemento-composizione-mancante");
		// div.getElement().appendChild(label.getElement());
		// gruppoElementi.add(div);
		// }
	}

	private void gruppoProtocollazioni(ProtocollazioneComposizione protocollazione, Set<ElementoComposizione> elementiSelezionati, EventBus eventBus, FascicoloDTO dto, TabComposizione<?> tab) {
		for (ProtocollazioneComposizione nonCapofila : protocollazione.getNonCapofila()) {
			gruppoProtocollazioni.add(new WidgetProtocollazione(nonCapofila, elementiSelezionati, eventBus, dto, tab));
		}
	}
}
