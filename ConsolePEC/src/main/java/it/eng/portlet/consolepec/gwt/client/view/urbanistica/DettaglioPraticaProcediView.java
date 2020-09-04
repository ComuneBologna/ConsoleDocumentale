package it.eng.portlet.consolepec.gwt.client.view.urbanistica;

import it.eng.cobo.consolepec.commons.urbanistica.AllegatoProcedi;
import it.eng.cobo.consolepec.commons.urbanistica.Nominativo;
import it.eng.cobo.consolepec.commons.urbanistica.PraticaProcedi;
import it.eng.portlet.consolepec.gwt.client.presenter.fascicolo.command.DownloadAllegatoProcediCommand;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.DettaglioPraticaProcediPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.command.DettaglioAllegatoProcediCommand;
import it.eng.portlet.consolepec.gwt.client.view.OpenCloseImagePanel;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.client.widget.MessageAlertWidget;
import it.eng.portlet.consolepec.gwt.client.widget.images.ConsolePECIcons;

import java.util.List;

import lombok.Getter;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.ConsoleDisclosurePanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * @author GiacomoFM
 * @since 06/nov/2017
 */
public class DettaglioPraticaProcediView extends ViewImpl implements DettaglioPraticaProcediPresenter.MyView {

	private final Widget widget;

	@UiField(provided = true) MessageAlertWidget messageAlertWidget;

	@UiField DownloadAllegatoWidget downloadWidget;

	@UiField Label pgLabel;
	@UiField Label titoloLabel;
	@UiField Label tipologiaPraticaLabel;
	@UiField Label ambitoLabel;
	@UiField Label indirizzoLabel;

	@UiField HTMLPanel nominativiPanel;

	@UiField Button indietroButton;

	@UiField HTMLPanel elencoPanel;

	@UiField(provided = true) ConsoleDisclosurePanel praticaProcediDisclosure = new ConsoleDisclosurePanel(
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Pratica Procedi");

	@UiField(provided = true) ConsoleDisclosurePanel allegatiProcediDisclosure = new ConsoleDisclosurePanel(
			OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelOpen(), OpenCloseImagePanel.DEFAULT_IMAGES.disclosurePanelClosed(), "Allegati Procedi");

	@Getter private DettaglioPraticaProcediPresenter presenter;

	@Override
	public void setPresenter(DettaglioPraticaProcediPresenter presenter) {
		this.presenter = presenter;
	}

	public interface Binder extends UiBinder<Widget, DettaglioPraticaProcediView> {
		//
	}

	@Inject
	public DettaglioPraticaProcediView(final Binder binder, final EventBus eventBus) {
		messageAlertWidget = new MessageAlertWidget(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	@Override
	public void setIndietroCommand(final Command command) {
		indietroButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				command.execute();
			}
		});
	}

	@Override
	public void mostraPraticaProcedi(PraticaProcedi praticaProcedi) {
		praticaProcediDisclosure.setOpen(true);
		impostaLabel(pgLabel, (praticaProcedi.getNumeroProtocollo() + "/" + praticaProcedi.getAnnoProtocollo()));
		impostaLabel(titoloLabel, praticaProcedi.getOggetto());
		impostaLabel(tipologiaPraticaLabel, praticaProcedi.getTipoPratica());
		impostaLabel(ambitoLabel, praticaProcedi.getAmbito());
		impostaLabel(indirizzoLabel, (praticaProcedi.getIndirizzoVia() + ", " + praticaProcedi.getIndirizzoCivico()));
		creaPanelNominativi(praticaProcedi.getNominativi());
		impostaComposizioneFascicolo(praticaProcedi);
	}

	private static void impostaLabel(Label label, String text) {
		if (Strings.isNullOrEmpty(text)) {
			label.setText("-");
		} else {
			label.setText(text);
		}
	}

	private void creaPanelNominativi(List<Nominativo> nominativi) {
		nominativiPanel.getElement().removeAllChildren();
		nominativiPanel.clear();
		for (Nominativo nominativo : nominativi) {
			Element span = DOM.createSpan();
			span.setClassName("label");
			span.setPropertyString("style", "text-overflow: ellipsis");
			span.setInnerHTML(nominativo.getTipoTitolo());
			nominativiPanel.getElement().appendChild(span);

			Element div = DOM.createDiv();
			div.setClassName("abstract");
			Element label = DOM.createLabel();
			label.setClassName("gwt-Label");
			label.setInnerHTML(nominativo.getCognomeNome());
			div.appendChild(label);
			nominativiPanel.getElement().appendChild(div);
		}
	}

	private void impostaComposizioneFascicolo(PraticaProcedi praticaProcedi) {
		elencoPanel.clear();
		for (AllegatoProcedi allegatoProcedi : praticaProcedi.getAllegati()) {
			DownloadAllegatoProcediCommand downloadAllegatoProcediCommand = new DownloadAllegatoProcediCommand(presenter,
					allegatoProcedi.getIdAlfresco());
			DettaglioAllegatoProcediCommand dettaglioAllegatoProcediCommand = new DettaglioAllegatoProcediCommand(presenter,
					praticaProcedi.getChiaveAllegati(), allegatoProcedi);
			elencoPanel.add(new AllegatoProcediWidget(allegatoProcedi, downloadAllegatoProcediCommand, dettaglioAllegatoProcediCommand));
		}
	}

	@Override
	public void sendDownload(SafeUri uri) {
		downloadWidget.sendDownload(uri);
	}

	private static class AllegatoProcediWidget extends ComplexPanel {
		private final Element container;
		private final AllegatoProcedi allegato;

		public AllegatoProcediWidget(final AllegatoProcedi allegato, final DownloadAllegatoProcediCommand downloadAllegatoProcediCommand,
				final DettaglioAllegatoProcediCommand dettaglioAllegatoProcediCommand) {
			this.allegato = allegato;
			container = DOM.createDiv();
			setElement(container);
			container.setClassName("box-mail documenti-mail");
			container.setPropertyString("style", "border: 1px solid #ddd;");
			composizioneStruttura(downloadAllegatoProcediCommand, dettaglioAllegatoProcediCommand);
		}

		private void composizioneStruttura(final DownloadAllegatoProcediCommand downloadAllegatoProcediCommand,
				final DettaglioAllegatoProcediCommand dettaglioAllegatoProcediCommand) {
			Element divContent = DOM.createDiv();
			divContent.addClassName("documento-container");
			container.appendChild(divContent);

			// NUMERO PROTOCOLLO
			Element spanH = DOM.createSpan();
			spanH.setClassName("capofila");
			spanH.setPropertyString("style", "margin-left: 27px;");
			spanH.setInnerHTML("PROTOCOLLO: ");
			divContent.appendChild(spanH);

			Element spanB = DOM.createSpan();
			spanB.setClassName("protocollo");
			spanB.setInnerHTML(allegato.getNumeroAnnoPG());
			divContent.appendChild(spanB);

			// ICONA
			Image icona = new Image(ConsolePECIcons._instance.allegato());
			icona.setStyleName("ico-mail");
			icona.setHeight("16px");
			icona.setWidth("16px");

			Anchor iconaAnchor = new Anchor();
			iconaAnchor.setTitle("Visualizza dettagli");
			iconaAnchor.getElement().setClassName("ico verifica");
			iconaAnchor.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					dettaglioAllegatoProcediCommand.execute();
				}
			});
			iconaAnchor.getElement().appendChild(icona.getElement());

			Element dettaglio = DOM.createSpan();
			dettaglio.setClassName("documento");
			dettaglio.setPropertyString("style", "margin-bottom: 10px;");
			add(iconaAnchor, dettaglio);

			// LINK
			Anchor link = new Anchor(componiTitolo(allegato));
			link.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					downloadAllegatoProcediCommand.execute();
				}
			});
			add(link, dettaglio);
			divContent.appendChild(dettaglio);
		}

		private static String componiTitolo(final AllegatoProcedi allegato) {
			StringBuilder sb = new StringBuilder();
			if (!Strings.isNullOrEmpty(allegato.getTipologia())) {
				sb.append(allegato.getTipologia()).append(" - ");
			}
			return sb.append(allegato.getNome()).toString();
		}
	}

}
