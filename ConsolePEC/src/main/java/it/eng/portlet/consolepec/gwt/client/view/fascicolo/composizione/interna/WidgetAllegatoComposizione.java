package it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.interna;

import java.util.Arrays;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.AllegatoComposizione;
import it.eng.cobo.consolepec.commons.pratica.fascicolo.composizione.ElementoComposizione;
import it.eng.portlet.consolepec.gwt.client.view.fascicolo.composizione.TabComposizione;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.shared.model.AllegatoDTO;
import it.eng.portlet.consolepec.gwt.shared.model.FascicoloDTO;

/**
 * @author GiacomoFM
 * @since 24/gen/2019
 */
public class WidgetAllegatoComposizione extends WidgetComposizioneInterna {

	@UiField
	DownloadAllegatoWidget downloadWidget;

	@Override
	protected DownloadAllegatoWidget getDownloadAllegatoWidget() {
		return downloadWidget;
	}

	@UiField
	InlineLabel infoProtocollazione;
	@UiField
	InlineLabel infoData;
	@UiField
	CheckBox checkAllegato;
	@UiField
	Anchor iconaAllegato;
	@UiField
	Anchor infoAllegato;
	@UiField
	InlineLabel infoAggiuntive;

	private static Binder binder = GWT.create(Binder.class);

	public interface Binder extends UiBinder<Widget, WidgetAllegatoComposizione> {/**/}

	public WidgetAllegatoComposizione(AllegatoComposizione allegatoFile, Set<ElementoComposizione> elementiSelezionati, final EventBus eventBus, final FascicoloDTO dto, final TabComposizione<?> tab) {
		super(eventBus, dto, tab, allegatoFile, elementiSelezionati);
		initWidget(binder.createAndBindUi(this));
		impostaCheckElemento(checkAllegato);
		info(allegatoFile);
	}

	private void info(AllegatoComposizione allegatoFile) {
		infoProtocollazione(allegatoFile);
		infoAllegato(allegatoFile);
		infoAggiuntive(allegatoFile);
		infoData.setText(dateTimeFormat.format(allegatoFile.getDataCaricamento()));
	}

	private void infoProtocollazione(AllegatoComposizione allegatoFile) {
		SafeHtmlBuilder sb = new SafeHtmlBuilder();
		sb.appendHtmlConstant("<b>");
		if (allegatoFile.isProtocollato()) {
			sb.appendEscaped(allegatoFile.getNumeroPg()).appendEscaped("/").appendEscaped(allegatoFile.getAnnoPg());
		} else {
			sb.appendEscaped("NON PROTOCOLLATO");
		}
		sb.appendHtmlConstant("</b>");
		infoProtocollazione.getElement().setInnerSafeHtml(sb.toSafeHtml());
	}

	private void infoAllegato(AllegatoComposizione allegatoFile) {
		Image iconaFirma = new Image(caricaIcona(allegatoFile));
		iconaFirma.setStyleName("ico-mail");
		iconaFirma.setHeight("16px");
		iconaFirma.setWidth("16px");

		iconaAllegato.setStyleName("ico verifica");
		iconaAllegato.setHref("javascript:;");
		final Object o = loadAllegato(allegatoFile);
		if (o != null && o instanceof AllegatoDTO) {
			iconaAllegato.setTitle("Visualizza dettagli");
			iconaAllegato.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					mostraDettaglioCommand.exe((AllegatoDTO) o);
				}
			});
		} else {
			iconaAllegato.setTitle("Nessun dettaglio visualizzabile");
		}
		iconaAllegato.getElement().appendChild(iconaFirma.getElement());
		impostaAnchor(infoAllegato);
	}

	private void infoAggiuntive(AllegatoComposizione allegatoFile) {
		StringBuilder sb = new StringBuilder();
		if (allegatoFile.getFolderOriginPath() != null && !allegatoFile.getFolderOriginPath().isEmpty()) {
			sb.append("Percorso originale: ").append(allegatoFile.getFolderOriginPath());
			sb.append(" - ");
		}
		sb.append("Tag: ");
		if (allegatoFile.getTag().isEmpty()) {
			sb.append("nessun tag allegato");
		} else {
			sb.append(Arrays.toString(allegatoFile.getTag().toArray()).replace("[", "").replace("]", ""));
		}
		infoAggiuntive.setText(sb.toString());
	}

	@Override
	protected Object loadAllegato(ElementoComposizione allegato) {
		for (AllegatoDTO a : dto.getAllegati()) {
			if (a.getNome().equals(allegato.getNome()) && a.getVersioneCorrente().equals(allegato.getVersione())) {
				return a;
			}
		}
		return null;
	}

	@Override
	protected void onClickExecution(Object o) {
		if (o instanceof AllegatoDTO) {
			scaricaElementoCommand.exe((AllegatoDTO) o);
		}
	}

	@Override
	protected void postSelezione(boolean selezionato) {
		checkAllegato.setValue(selezionato);
	}

}
