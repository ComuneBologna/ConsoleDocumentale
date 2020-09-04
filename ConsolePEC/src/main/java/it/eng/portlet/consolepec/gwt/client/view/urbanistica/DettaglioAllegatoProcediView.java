package it.eng.portlet.consolepec.gwt.client.view.urbanistica;

import it.eng.cobo.consolepec.commons.firmadigitale.FirmaDigitale;
import it.eng.cobo.consolepec.commons.firmadigitale.Firmatario;
import it.eng.cobo.consolepec.commons.urbanistica.AllegatoProcedi;
import it.eng.cobo.consolepec.util.console.ConsoleConstants;
import it.eng.cobo.consolepec.util.firmadigitale.FirmaDigitaleUtil;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.DettaglioAllegatoProcediPresenter;
import it.eng.portlet.consolepec.gwt.client.presenter.urbanistica.command.DettaglioPraticaProcediCommand;
import it.eng.portlet.consolepec.gwt.client.widget.DownloadAllegatoWidget;
import it.eng.portlet.consolepec.gwt.shared.UriMapping;

import java.util.Date;

import com.google.common.base.Strings;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

/**
 * @author GiacomoFM
 * @since 09/feb/2018
 */
public class DettaglioAllegatoProcediView extends ViewImpl implements DettaglioAllegatoProcediPresenter.MyView {

	private Widget widget;

	@Override
	public Widget asWidget() {
		return widget;
	}

	@UiField HTMLPanel content;
	@UiField HTMLPanel fdPanel;
	@UiField Button indietroButton;

	public interface Binder extends UiBinder<Widget, DettaglioAllegatoProcediView> {
		//
	}

	@Inject
	public DettaglioAllegatoProcediView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public void mostraAllegato(AllegatoProcedi allegatoProcedi, FirmaDigitale info) {
		if (FirmaDigitaleUtil.isFirmato(info)) {
			creaVociComuni(allegatoProcedi);
			fdPanel.getElement().removeAllChildren();
			fdPanel.setVisible(true);
			creaVociFirmaDigitale(allegatoProcedi, info);
		} else if (FirmaDigitaleUtil.isFirmaDigitaleValida(info)) {
			mostraAllegato(allegatoProcedi, "File non firmato");
		} else {
			mostraAllegato(allegatoProcedi, "Errore controllo firma digitale" + (info != null ? " - " + info.getDescrizione() : ""));
		}
	}

	@Override
	public void mostraAllegato(AllegatoProcedi allegatoProcedi, String info) {
		creaVociComuni(allegatoProcedi);
		creaVoce(content, "Controllo firma", info);
		fdPanel.setVisible(false);
	}

	private void creaVociComuni(AllegatoProcedi allegatoProcedi) {
		Window.scrollTo(0, 0);
		content.getElement().removeAllChildren();
		content.clear();
		creaVoce(content, "Tipologa", allegatoProcedi.getTipologia());
		creaVoce(content, "Nome", allegatoProcedi.getNome());
		creaVoce(content, "Numero/Anno PG", allegatoProcedi.getNumeroAnnoPG());
		creaVoce(content, "Id Alfresco", allegatoProcedi.getIdAlfresco());
	}

	private void creaVociFirmaDigitale(AllegatoProcedi allegatoProcedi, FirmaDigitale firmaDigitale) {
		Element h6 = DOM.createDiv();
		h6.setInnerHTML("<h6>&nbsp;Firma Digitale</h6>");
		fdPanel.getElement().appendChild(h6);

		int i = 1;
		for (Firmatario firmatario : firmaDigitale.getFirmatari()) {

			Element span = DOM.createSpan();
			span.setClassName("label");
			span.setPropertyString("style", "text-overflow: ellipsis");
			span.setInnerHTML("Allegato non firmato");
			fdPanel.getElement().appendChild(span);

			HTMLPanel fileSbustatoPanel = new HTMLPanel("");
			fileSbustatoPanel.setStyleName("abstract-anchor abstract");
			DownloadAllegatoWidget downloadAllegatoWidget = new DownloadAllegatoWidget();
			Anchor a = new Anchor();
			a.addClickHandler(new DownloadFileSbustatoHandler(allegatoProcedi.getNome(), allegatoProcedi.getIdAlfresco(), downloadAllegatoWidget));
			a.getElement().setAttribute("style", "cursor:pointer");
			if (!allegatoProcedi.getNome().toLowerCase().contains(".p7m")) a.setText(allegatoProcedi.getNome());
			else a.setText(allegatoProcedi.getNome().replaceAll(".p7m", "").replaceAll(".P7M", "").replaceAll(".p7M", "").replaceAll(".P7m", ""));
			fileSbustatoPanel.add(a);
			fileSbustatoPanel.add(downloadAllegatoWidget);
			fdPanel.add(fileSbustatoPanel);

			creaVoce(fdPanel, "<b>Firmatario</b>", String.valueOf(i++));
			creaVoce(fdPanel, "Stato", firmatario.getStato().toString());
			creaVoce(fdPanel, "Descrizione", firmatario.getDescrizione());
			creaVoce(fdPanel, "CRL", firmatario.getCRL() != null ? firmatario.getCRL().getValue() : null);
			creaVoce(fdPanel, "DN", firmatario.getDN());
			creaVoce(fdPanel, "CA", firmatario.getCA());
			creaVoce(fdPanel, "Periodo validit&agrave;", "Dal " + castDate(firmatario.getValidoDal()) + " al " + castDate(firmatario.getValidoAl()));
			creaVoce(fdPanel, "Data firma", castDate(firmatario.getDataFirma()));
			creaVoce(fdPanel, "Tipo firma", firmatario.getTipoFirma().toString());
		}

	}

	private static String castDate(Date date) {
		if (date == null) {
			return " - ";
		}
		DateTimeFormat dtf = DateTimeFormat.getFormat(ConsoleConstants.FORMATO_DATAORA);
		return dtf.format(date);
	}

	private static void creaVoce(HTMLPanel panel, String etichetta, String valore) {
		if (Strings.isNullOrEmpty(valore)) {
			valore = " - ";
		}

		Element span = DOM.createSpan();
		span.setClassName("label");
		span.setPropertyString("style", "text-overflow: ellipsis");
		span.setInnerHTML(etichetta);
		panel.getElement().appendChild(span);

		Element div = DOM.createDiv();
		div.setClassName("abstract");
		Element label = DOM.createLabel();
		label.setClassName("gwt-Label");
		label.setInnerHTML(valore);
		div.appendChild(label);
		panel.getElement().appendChild(div);
	}

	@Override
	public void setIndietroCommand(final DettaglioPraticaProcediCommand dettaglioPraticaProcediCommand) {
		indietroButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				dettaglioPraticaProcediCommand.execute();
			}
		});
	}

	class DownloadFileSbustatoHandler implements com.google.gwt.event.dom.client.ClickHandler {
		private final String uuid;
		private final String fileName;
		private final DownloadAllegatoWidget downloadAllegatoWidget;

		public DownloadFileSbustatoHandler(String fileName, String uuid, DownloadAllegatoWidget downloadAllegatoWidget) {
			this.uuid = uuid;
			this.fileName = fileName;
			this.downloadAllegatoWidget = downloadAllegatoWidget;
		}

		@Override
		public void onClick(ClickEvent event) {
			SafeUri uri = UriMapping.generaDownloadAllegatoSbustatoServletURL(fileName, uuid);
			downloadAllegatoWidget.sendDownload(uri);

		}
	}
}
